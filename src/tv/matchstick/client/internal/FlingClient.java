package tv.matchstick.client.internal;

import java.util.ArrayList;

import tv.matchstick.client.common.FlingPlayServicesClient;
import tv.matchstick.fling.ConnectionResult;
import tv.matchstick.fling.FlingManager;
import tv.matchstick.fling.internal.Api;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

/**
 * Fling client interface with Fling service.
 *
 * @author jim
 *
 * @param <T>
 */
public abstract class FlingClient<T extends IInterface> implements
		FlingPlayServicesClient, Api.ConnectionApi,
		FlingClientEvents.FmsClientEventCallback {
	static final int CONNECTION_STATUS_DISCONNECTED = 1;
	static final int CONNECTION_STATUS_CONNECTING = 2;
	static final int CONNECTION_STATUS_CONNECTED = 3;

	private final Context mContext;
	private final Looper mLooper;
	final Handler mFlingClientHandler;
	private T mService;
	private final ArrayList<CallbackProxy<?>> mCallbackProxyList = new ArrayList<CallbackProxy<?>>();
	private FlingClientServiceConnection mFmsClientServiceConnection;
	private volatile int mConnectedState = CONNECTION_STATUS_DISCONNECTED;
	boolean mConnected = false;
	private final FlingClientEvents mFmsClientEvent;

	/**
	 * constructor.
	 *
	 * @param context
	 * @param looper
	 * @param callbacks
	 * @param failedListener
	 * @param strArray
	 */
	protected FlingClient(Context context, Looper looper,
			FlingManager.ConnectionCallbacks callbacks,
			FlingManager.OnConnectionFailedListener failedListener,
			String[] strArray) {
		ValueChecker.checkNullPointer(context);
		mContext = context;
		ValueChecker.checkNullPointer(looper, "Looper must not be null");
		mLooper = looper;
		mFmsClientEvent = new FlingClientEvents(context, looper, this);
		mFlingClientHandler = new FlingClientHandler(looper);

		ValueChecker.checkNullPointer(callbacks);
		registerConnectionCallbacks(callbacks);

		ValueChecker.checkNullPointer(failedListener);
		registerConnectionFailedListener(failedListener);
	}

	/**
	 * Constructor.
	 *
	 * @param paramContext
	 * @param callbacks
	 * @param failedListener
	 * @param strArray
	 */
	protected FlingClient(
			Context paramContext,
			FlingPlayServicesClient.ConnectionCallbacks callbacks,
			FlingPlayServicesClient.OnConnectionFailedListener failedListener,
			String[] strArray) {
		this(paramContext, paramContext.getMainLooper(),
				new FmsClientConnectionCallbacks(callbacks),
				new FmsClientOnConnectionFailedListener(failedListener),
				strArray);
	}

	/**
	 * Service intent's action name
	 * @return service intent's action name
	 */
	protected abstract String getServiceName();

	/**
	 * Fling service's interface descriptor.
	 * @return
	 */
	protected abstract String getInterfaceDescriptor();

	/**
	 * Get service.
	 *
	 * @param binder
	 * @return
	 */
	protected abstract T getService(IBinder binder);

	protected abstract void getServiceFromBroker(
			IFlingServiceBroker serviceBroker,
			IFlingCallbackImpl flingCallback) throws RemoteException;

	/**
	 * Connect with Fling service. It's the most important API exported to Fling application.
	 *
	 * By Calling this function, Fling application will try to connect with Fling Service. 
	 * 
	 * Please see more info about {@link tv.matchstick.client.internal.FlingClient.FlingClientServiceConnection}
	 */
	public void connect() {
		mConnected = true;
		mConnectedState = CONNECTION_STATUS_CONNECTING;
		if (mFmsClientServiceConnection != null) {
			Log.e("FlingClient",
					"Calling connect() while still connected, missing disconnect().");
			mService = null;
			FlingServiceManager.getInstance(mContext).unbindService(
					getServiceName(), mFmsClientServiceConnection);
		}
		mFmsClientServiceConnection = new FlingClientServiceConnection();
		boolean bool = FlingServiceManager.getInstance(mContext)
				.bindService(getServiceName(),
						mFmsClientServiceConnection);
		if (!bool) {
			Log.e("FlingClient", "unable to connect to service: "
					+ getServiceName());
			mFlingClientHandler.sendMessage(mFlingClientHandler
					.obtainMessage(MSG_WHAT_CONNECTION_FAILED,
							Integer.valueOf(9)));
			return;
		}
	}

	/**
	 * Is connected?
	 */
	public boolean isConnected() {
		return this.mConnectedState == CONNECTION_STATUS_CONNECTED;
	}

	/**
	 * Is connecting?
	 */
	public boolean isConnecting() {
		return this.mConnectedState == CONNECTION_STATUS_CONNECTING;
	}

	/**
	 * Disconnect with Fling service.
	 *
	 * Do some clean up work.
	 */
	public void disconnect() {
		this.mConnected = false;
		synchronized (this.mCallbackProxyList) {
			int i = this.mCallbackProxyList.size();
			for (int j = 0; j < i; j++) {
				this.mCallbackProxyList.get(j).ed();
			}
			this.mCallbackProxyList.clear();
		}
		this.mConnectedState = CONNECTION_STATUS_DISCONNECTED;
		this.mService = null;
		if (this.mFmsClientServiceConnection != null) {
			FlingServiceManager.getInstance(this.mContext)
					.unbindService(getServiceName(),
							this.mFmsClientServiceConnection);
			this.mFmsClientServiceConnection = null;
		}
	}

	/**
	 * Called when disconnected from Fling service.
	 * @param reason
	 */
	public void sendDisconnectedMessage(int reason) {
		this.mFlingClientHandler
				.sendMessage(this.mFlingClientHandler.obtainMessage(
						MSG_WHAT_DISCONNECTED, Integer.valueOf(reason)));
	}

	/**
	 * Get context.
	 * @return
	 */
	public final Context getContext() {
		return this.mContext;
	}

	/**
	 * Get looper
	 */
	public final Looper getLooper() {
		return this.mLooper;
	}

	/**
	 * After connect with Fling service, called to show current connect status
	 *
	 * @param statusCode
	 * @param binder
	 * @param bundle
	 */
	protected void onPostInitResult(int statusCode, IBinder binder, Bundle bundle) {
		this.mFlingClientHandler.sendMessage(this.mFlingClientHandler
				.obtainMessage(1, new FlingClientCallbackProxy(statusCode,
						binder, bundle)));
	}

	/**
	 * Check connected status.
	 */
	protected final void checkConnected() {
		if (!isConnected()) {
			throw new IllegalStateException(
					"Not connected. Call connect() and wait for onConnected() to be called.");
		}
	}

	public Bundle getBundle() {
		return null;
	}

	/**
	 * return current connected Fling service.
	 * @return
	 */
	protected final T getService() {
		checkConnected();
		return this.mService;
	}

	/**
	 * Add connection call back function. Useless.Not called by others?!!!!
	 * @param callback
	 */
	public final void addCallback(CallbackProxy<?> callback) {
		synchronized (this.mCallbackProxyList) {
			this.mCallbackProxyList.add(callback);
		}
		this.mFlingClientHandler.sendMessage(this.mFlingClientHandler
				.obtainMessage(2, callback));
	}

	/**
	 * Whether client can receive event.
	 */
	public boolean canReceiveEvent() {
		return this.mConnected;
	}

	/**
	 * register connection callback function.
	 * @param callbacks connection callback function(onConnected/onConnectionSuspended)
	 */
	public void registerConnectionCallbacks(
			FlingManager.ConnectionCallbacks callbacks) {
		this.mFmsClientEvent.registerConnectionCallbacks(callbacks);
	}

	/**
	 * Register connection failed listener.
	 */
	public void registerConnectionFailedListener(
			FlingManager.OnConnectionFailedListener listener) {
		this.mFmsClientEvent.registerConnectionFailedListener(listener);
	}

	/**
	 * register connection callback.
	 */
	public void registerConnectionCallbacks(
			FlingPlayServicesClient.ConnectionCallbacks callbacks) {
		this.mFmsClientEvent
				.registerConnectionCallbacks(new FmsClientConnectionCallbacks(
						callbacks));
	}

	/**
	 * is connection callback register.
	 */
	public boolean isConnectionCallbacksRegistered(
			FlingPlayServicesClient.ConnectionCallbacks callbacks) {
		return this.mFmsClientEvent
				.isConnectionCallbacksRegistered(new FmsClientConnectionCallbacks(
						callbacks));
	}

	/**
	 * unregister callback function.
	 */
	public void unregisterConnectionCallbacks(
			FlingPlayServicesClient.ConnectionCallbacks callbacks) {
		this.mFmsClientEvent
				.unregisterConnectionCallbacks(new FmsClientConnectionCallbacks(
						callbacks));
	}

	/**
	 * register connection failed listener.
	 */
	public void registerConnectionFailedListener(
			FlingPlayServicesClient.OnConnectionFailedListener listener) {
		this.mFmsClientEvent.registerConnectionFailedListener(listener);
	}

	/**
	 * check whether connection failed listener is registered. 
	 */
	public boolean isConnectionFailedListenerRegistered(
			FlingPlayServicesClient.OnConnectionFailedListener listener) {
		return this.mFmsClientEvent
				.isConnectionFailedListenerRegistered(listener);
	}

	/**
	 * unregister connection failed listener.
	 */
	public void unregisterConnectionFailedListener(
			FlingPlayServicesClient.OnConnectionFailedListener listener) {
		this.mFmsClientEvent.unregisterConnectionFailedListener(listener);
	}

	/**
	 * Fling callback implementation. which will be called when connect/disconnect (ok/failed) with Fling service.
	 * 
	 * @author jim
	 *
	 */
	public static final class IFlingCallbackImpl extends
			IFlingCallbacks.Stub_a {
		
		// Fling client.
		private FlingClient flingClient;

		/**
		 * constructor.
		 * @param client Fling client.
		 */
		public IFlingCallbackImpl(FlingClient client) {
			flingClient = client;
		}

		/**
		 * Called when client connected(ok/failed) with Fling service.
		 */
		public void onPostInitComplete(int statusCode, IBinder binder,
				Bundle bundle) {
			//new Exception().printStackTrace();
			ValueChecker
					.checkNullPointer(flingClient,
							"onPostInitComplete can be called only once per call to getServiceFromBroker");
			
			// return current connect status.
			flingClient.onPostInitResult(statusCode, binder, bundle);
			flingClient = null;
		}
	}

	public static final class FmsClientOnConnectionFailedListener implements
			FlingManager.OnConnectionFailedListener {
		private final FlingPlayServicesClient.OnConnectionFailedListener failedListener;

		public FmsClientOnConnectionFailedListener(
				FlingPlayServicesClient.OnConnectionFailedListener listener) {
			this.failedListener = listener;
		}

		public void onConnectionFailed(ConnectionResult result) {
			this.failedListener.onConnectionFailed(result);
		}

		public boolean equals(Object other) {
			if ((other instanceof FmsClientOnConnectionFailedListener)) {
				return this.failedListener
						.equals(((FmsClientOnConnectionFailedListener) other).failedListener);
			}
			return this.failedListener.equals(other);
		}
	}

	public static final class FmsClientConnectionCallbacks implements
			FlingManager.ConnectionCallbacks {
		private final FlingPlayServicesClient.ConnectionCallbacks callback;

		public FmsClientConnectionCallbacks(
				FlingPlayServicesClient.ConnectionCallbacks callback) {
			this.callback = callback;
		}

		public void onConnected(Bundle connectionHint) {
			this.callback.onConnected(connectionHint);
		}

		public void onConnectionSuspended(int cause) {
			this.callback.onDisconnected();
		}

		public boolean equals(Object other) {
			if ((other instanceof FmsClientConnectionCallbacks)) {
				return this.callback
						.equals(((FmsClientConnectionCallbacks) other).callback);
			}
			return this.callback.equals(other);
		}
	}

	protected abstract class CallbackProxy<TListener> {
		private TListener mListener;
		private boolean isUsed;

		public CallbackProxy(TListener obj) {
			this.mListener = obj;
			this.isUsed = false;
		}

		protected abstract void onCallback(TListener paramTListener);

		protected abstract void onFailed();

		public void call() {
			TListener listener;
			synchronized (this) {
				listener = this.mListener;
				if (this.isUsed) {
					Log.w("FlingClient", "Callback proxy " + this
							+ " being reused. This is not safe.");
				}
			}
			if (listener != null) {
				try {
					onCallback(listener);
				} catch (RuntimeException re) {
					onFailed();
					throw re;
				}
			} else {
				onFailed();
			}
			synchronized (this) {
				this.isUsed = true;
			}
			unregister();
		}

		public void unregister() {
			ed();
			synchronized (mCallbackProxyList) {
				mCallbackProxyList.remove(this);
			}
		}

		public void ed() {
			synchronized (this) {
				mListener = null;
			}
		}
	}

	public final class FlingClientCallbackProxy extends
			CallbackProxy<Boolean> {
		public final int statusCode;
		public final Bundle bundle;
		public final IBinder binder;

		public FlingClientCallbackProxy(int statusCode, IBinder binder,
				Bundle bundle) {
			super(Boolean.valueOf(true));
			this.statusCode = statusCode;
			this.binder = binder;
			this.bundle = bundle;
		}

		protected void onFailed() {
		}

		protected void onCallback(Boolean paramTListener) {
			if (paramTListener == null) {
				mConnectedState = CONNECTION_STATUS_DISCONNECTED;
				return;
			}
			switch (this.statusCode) {
			case 0:
				String str = null;
				try {
					str = this.binder.getInterfaceDescriptor();
					if (getInterfaceDescriptor().equals(str)) {
						mService = getService(this.binder);
						if (mService != null) {
							mConnectedState = CONNECTION_STATUS_CONNECTED;
							mFmsClientEvent.notifyOnConnected();
							return;
						}
					}
				} catch (RemoteException e) {
				}
				FlingServiceManager.getInstance(mContext)
						.unbindService(getServiceName(),
								mFmsClientServiceConnection);
				mFmsClientServiceConnection = null;
				mConnectedState = CONNECTION_STATUS_DISCONNECTED;
				mService = null;
				mFmsClientEvent
						.notifyOnConnectionFailed(new ConnectionResult(8,
								null));
				break;
			case 10:
				mConnectedState = CONNECTION_STATUS_DISCONNECTED;
				throw new IllegalStateException(
						"A fatal developer error has occurred. Check the logs for further information.");
			default:
				PendingIntent pendingIntent = null;
				if (this.bundle != null) {
					pendingIntent = (PendingIntent) this.bundle
							.getParcelable("pendingIntent");
				}
				if (mFmsClientServiceConnection != null) {
					FlingServiceManager.getInstance(mContext)
							.unbindService(
									FlingClient.this.getServiceName(),
									mFmsClientServiceConnection);
					mFmsClientServiceConnection = null;
				}
				mConnectedState = CONNECTION_STATUS_DISCONNECTED;
				mService = null;
				mFmsClientEvent
						.notifyOnConnectionFailed(new ConnectionResult(
								this.statusCode, pendingIntent));
			}

		}
	}

	static final int MSG_WHAT_CONNECTION_FAILED = 3;
	static final int MSG_WHAT_DISCONNECTED = 4;

	/**
	 * This handler will process all client connected/disconnected related messages from Fling service.
	 *
	 * Used when:
	 * 1. connect/disconnect success result with Fling service
	 * 2. connect/disconnect failed result.
	 */
	final class FlingClientHandler extends Handler {
		public FlingClientHandler(Looper arg2) {
			super();
		}

		@Override
		public void handleMessage(Message msg) {
			if ((msg.what == 2) || (msg.what == 1)) {
				if (!isConnecting()) {
					CallbackProxy cb = (CallbackProxy) msg.obj;
					cb.onFailed();
					cb.unregister();
					return;
				} else {
					CallbackProxy cb = (CallbackProxy) msg.obj;
					cb.call();   // when connected with Fling service(device), come to here.
					return;
				}
			}
			
			// when connect failed.
			if (msg.what == MSG_WHAT_CONNECTION_FAILED) {
				mFmsClientEvent
						.notifyOnConnectionFailed(new ConnectionResult(
								((Integer) msg.obj).intValue(), null));
				return;
			}
			
			// when disconnect from service.
			if (msg.what == MSG_WHAT_DISCONNECTED) {
				mConnectedState = CONNECTION_STATUS_DISCONNECTED;
				mService = null;
				mFmsClientEvent
						.notifyOnConnectionSuspended(((Integer) msg.obj)
								.intValue());
				return;
			}
			Log.wtf("FlingClient", "Don't know how to handle this message.");
		}
	}

	/**
	 * Ready to get something(binder,callback,etc) from service, and do some init work.
	 * @param paramIBinder
	 */
	protected final void getServiceFromBroker(IBinder paramIBinder) {
		try {
			IFlingServiceBroker serviceBroker = IFlingServiceBroker.Stub
					.asInterface(paramIBinder);
			getServiceFromBroker(serviceBroker, new IFlingCallbackImpl(
					this));
		} catch (RemoteException e) {
			Log.w("FlingClient", "service died");
		}
	}

	/**
	 * This connection will be invoked when Fling client connected with Fling service. 
	 * @author jim
	 *
	 */
	final class FlingClientServiceConnection implements ServiceConnection {
		FlingClientServiceConnection() {
		}

		/**
		 * Called when service connected.
		 * 
		 * Please note, when client connected with Fling service, call something to init data(callbacks,binders) between client/service.
		 */
		public void onServiceConnected(ComponentName component, IBinder binder) {
			// When connected to Fling device, ready to init server.
			getServiceFromBroker(binder);
		}

		public void onServiceDisconnected(ComponentName component) {
			mFlingClientHandler.sendMessage(mFlingClientHandler
					.obtainMessage(MSG_WHAT_DISCONNECTED, Integer.valueOf(1)));
		}
	}
}
