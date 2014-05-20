package com.fireflycast.client.internal;

import java.util.ArrayList;

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

import com.fireflycast.cast.Api;
import com.fireflycast.cast.ConnectionResult;
import com.fireflycast.cast.FireflyApiClient;
import com.fireflycast.client.common.FireflyPlayServicesClient;

//import com.fireflycast.client.common.data.DataHolder;

public abstract class FireflyClient_eh<T extends IInterface> implements
		FireflyPlayServicesClient, Api.ConnectionApi_a,
		FireflyClientEvents_ei.FmsClientEventCallback_b {
	static final int CONNECTION_STATUS_DISCONNECTED = 1;
	static final int CONNECTION_STATUS_CONNECTING = 2;
	static final int CONNECTION_STATUS_CONNECTED = 3;

	private final Context mContext;
	private final Looper mLooper_zs;
	final Handler mFireflyClientHandler;
	private T mService_Bv;
	private final ArrayList<CallbackProxy_b<?>> mCallbackProxyList_Bw = new ArrayList<CallbackProxy_b<?>>();
	private FireflyClientServiceConnection_f mFmsClientServiceConnection_Bx;
	private volatile int mConnectedState_By = CONNECTION_STATUS_DISCONNECTED;
	boolean mConnected_BA = false;
	private final FireflyClientEvents_ei mFmsClientEvent_zx;

	protected FireflyClient_eh(Context context, Looper looper,
			FireflyApiClient.ConnectionCallbacks callbacks,
			FireflyApiClient.OnConnectionFailedListener failedListener,
			String[] strArray) {
		ValueChecker_er.checkNullPointer_f(context);
		mContext = context;
		ValueChecker_er.checkNullPointer_b(looper, "Looper must not be null");
		mLooper_zs = looper;
		mFmsClientEvent_zx = new FireflyClientEvents_ei(context, looper, this);
		mFireflyClientHandler = new FireflyClientHandler_a(looper);

		ValueChecker_er.checkNullPointer_f(callbacks);
		registerConnectionCallbacks(callbacks);

		ValueChecker_er.checkNullPointer_f(failedListener);
		registerConnectionFailedListener(failedListener);
	}

	protected FireflyClient_eh(
			Context paramContext,
			FireflyPlayServicesClient.ConnectionCallbacks callbacks,
			FireflyPlayServicesClient.OnConnectionFailedListener failedListener,
			String[] strArray) {
		this(paramContext, paramContext.getMainLooper(),
				new FmsClientConnectionCallbacks_c(callbacks),
				new FmsClientOnConnectionFailedListener_g(failedListener),
				strArray);
	}

	protected abstract String getServiceName_aF();

	protected abstract String getInterfaceDescriptor_aG();

	protected abstract T getService_p(IBinder binder);

	protected abstract void getServiceFromBroker_a(
			IFireflyServiceBroker_en serviceBroker,
			IFireflyCallbackImpl_e fireflyCallback) throws RemoteException;

	public void connect() {
		mConnected_BA = true;
		mConnectedState_By = CONNECTION_STATUS_CONNECTING;
		if (mFmsClientServiceConnection_Bx != null) {
			Log.e("FireflyClient",
					"Calling connect() while still connected, missing disconnect().");
			mService_Bv = null;
			FireflyServiceManager_ej.getInstance_y(mContext).unbindService_b(
					getServiceName_aF(), mFmsClientServiceConnection_Bx);
		}
		mFmsClientServiceConnection_Bx = new FireflyClientServiceConnection_f();
		boolean bool = FireflyServiceManager_ej.getInstance_y(mContext)
				.bindService_a(getServiceName_aF(),
						mFmsClientServiceConnection_Bx);
		if (!bool) {
			Log.e("FireflyClient", "unable to connect to service: "
					+ getServiceName_aF());
			mFireflyClientHandler.sendMessage(mFireflyClientHandler
					.obtainMessage(MSG_WHAT_CONNECTION_FAILED,
							Integer.valueOf(9)));
			return;
		}
	}

	public boolean isConnected() {
		return this.mConnectedState_By == CONNECTION_STATUS_CONNECTED;
	}

	public boolean isConnecting() {
		return this.mConnectedState_By == CONNECTION_STATUS_CONNECTING;
	}

	public void disconnect() {
		this.mConnected_BA = false;
		synchronized (this.mCallbackProxyList_Bw) {
			int i = this.mCallbackProxyList_Bw.size();
			for (int j = 0; j < i; j++) {
				this.mCallbackProxyList_Bw.get(j).ed();
			}
			this.mCallbackProxyList_Bw.clear();
		}
		this.mConnectedState_By = CONNECTION_STATUS_DISCONNECTED;
		this.mService_Bv = null;
		if (this.mFmsClientServiceConnection_Bx != null) {
			FireflyServiceManager_ej.getInstance_y(this.mContext)
					.unbindService_b(getServiceName_aF(),
							this.mFmsClientServiceConnection_Bx);
			this.mFmsClientServiceConnection_Bx = null;
		}
	}

	public void O(int paramInt) {
		this.mFireflyClientHandler
				.sendMessage(this.mFireflyClientHandler.obtainMessage(
						MSG_WHAT_DISCONNECTED, Integer.valueOf(paramInt)));
	}

	public final Context getContext() {
		return this.mContext;
	}

	public final Looper getLooper() {
		return this.mLooper_zs;
	}

	protected void sendMessage_a(int statusCode, IBinder binder, Bundle bundle) {
		this.mFireflyClientHandler.sendMessage(this.mFireflyClientHandler
				.obtainMessage(1, new FireflyClientCallbackProxy_h(statusCode,
						binder, bundle)));
	}

	protected final void checkConnected_bm() {
		if (!isConnected()) {
			throw new IllegalStateException(
					"Not connected. Call connect() and wait for onConnected() to be called.");
		}
	}

	public Bundle getBundle_cY() {
		return null;
	}

	protected final T getService_eb() {
		checkConnected_bm();
		return this.mService_Bv;
	}

	public final void addCallback_a(CallbackProxy_b<?> callback) {
		synchronized (this.mCallbackProxyList_Bw) {
			this.mCallbackProxyList_Bw.add(callback);
		}
		this.mFireflyClientHandler.sendMessage(this.mFireflyClientHandler
				.obtainMessage(2, callback));
	}

	public boolean canReceiveEvent_dC() {
		return this.mConnected_BA;
	}

	public void registerConnectionCallbacks(
			FireflyApiClient.ConnectionCallbacks callbacks) {
		this.mFmsClientEvent_zx.registerConnectionCallbacks(callbacks);
	}

	public void registerConnectionFailedListener(
			FireflyApiClient.OnConnectionFailedListener listener) {
		this.mFmsClientEvent_zx.registerConnectionFailedListener(listener);
	}

	public void registerConnectionCallbacks(
			FireflyPlayServicesClient.ConnectionCallbacks callbacks) {
		this.mFmsClientEvent_zx
				.registerConnectionCallbacks(new FmsClientConnectionCallbacks_c(
						callbacks));
	}

	public boolean isConnectionCallbacksRegistered(
			FireflyPlayServicesClient.ConnectionCallbacks callbacks) {
		return this.mFmsClientEvent_zx
				.isConnectionCallbacksRegistered(new FmsClientConnectionCallbacks_c(
						callbacks));
	}

	public void unregisterConnectionCallbacks(
			FireflyPlayServicesClient.ConnectionCallbacks callbacks) {
		this.mFmsClientEvent_zx
				.unregisterConnectionCallbacks(new FmsClientConnectionCallbacks_c(
						callbacks));
	}

	public void registerConnectionFailedListener(
			FireflyPlayServicesClient.OnConnectionFailedListener listener) {
		this.mFmsClientEvent_zx.registerConnectionFailedListener(listener);
	}

	public boolean isConnectionFailedListenerRegistered(
			FireflyPlayServicesClient.OnConnectionFailedListener listener) {
		return this.mFmsClientEvent_zx
				.isConnectionFailedListenerRegistered(listener);
	}

	public void unregisterConnectionFailedListener(
			FireflyPlayServicesClient.OnConnectionFailedListener listener) {
		this.mFmsClientEvent_zx.unregisterConnectionFailedListener(listener);
	}

	public static final class IFireflyCallbackImpl_e extends
			IFireflyCallbacks_em.Stub_a {
		private FireflyClient_eh castClient_BF;

		public IFireflyCallbackImpl_e(FireflyClient_eh client) {
			castClient_BF = client;
		}

		public void onPostInitComplete_b(int statusCode, IBinder binder,
				Bundle bundle) {
			new Exception().printStackTrace();
			ValueChecker_er
					.checkNullPointer_b(castClient_BF,
							"onPostInitComplete can be called only once per call to getServiceFromBroker");
			castClient_BF.sendMessage_a(statusCode, binder, bundle);
			castClient_BF = null;
		}
	}

	public static final class FmsClientOnConnectionFailedListener_g implements
			FireflyApiClient.OnConnectionFailedListener {
		private final FireflyPlayServicesClient.OnConnectionFailedListener failedListener_BG;

		public FmsClientOnConnectionFailedListener_g(
				FireflyPlayServicesClient.OnConnectionFailedListener listener) {
			this.failedListener_BG = listener;
		}

		public void onConnectionFailed(ConnectionResult result) {
			this.failedListener_BG.onConnectionFailed(result);
		}

		public boolean equals(Object other) {
			if ((other instanceof FmsClientOnConnectionFailedListener_g)) {
				return this.failedListener_BG
						.equals(((FmsClientOnConnectionFailedListener_g) other).failedListener_BG);
			}
			return this.failedListener_BG.equals(other);
		}
	}

	public static final class FmsClientConnectionCallbacks_c implements
			FireflyApiClient.ConnectionCallbacks {
		private final FireflyPlayServicesClient.ConnectionCallbacks callback_BE;

		public FmsClientConnectionCallbacks_c(
				FireflyPlayServicesClient.ConnectionCallbacks callback) {
			this.callback_BE = callback;
		}

		public void onConnected(Bundle connectionHint) {
			this.callback_BE.onConnected(connectionHint);
		}

		public void onConnectionSuspended(int cause) {
			this.callback_BE.onDisconnected();
		}

		public boolean equals(Object other) {
			if ((other instanceof FmsClientConnectionCallbacks_c)) {
				return this.callback_BE
						.equals(((FmsClientConnectionCallbacks_c) other).callback_BE);
			}
			return this.callback_BE.equals(other);
		}
	}

	protected abstract class CallbackProxy_b<TListener> {
		private TListener mListener;
		private boolean isUsed_BD;

		public CallbackProxy_b(TListener obj) {
			this.mListener = obj;
			this.isUsed_BD = false;
		}

		protected abstract void onCallback_a(TListener paramTListener);

		protected abstract void onFailed_cP();

		public void call_ec() {
			TListener listener;
			synchronized (this) {
				listener = this.mListener;
				if (this.isUsed_BD) {
					Log.w("FireflyClient", "Callback proxy " + this
							+ " being reused. This is not safe.");
				}
			}
			if (listener != null) {
				try {
					onCallback_a(listener);
				} catch (RuntimeException re) {
					onFailed_cP();
					throw re;
				}
			} else {
				onFailed_cP();
			}
			synchronized (this) {
				this.isUsed_BD = true;
			}
			unregister();
		}

		public void unregister() {
			ed();
			synchronized (mCallbackProxyList_Bw) {
				mCallbackProxyList_Bw.remove(this);
			}
		}

		public void ed() {
			synchronized (this) {
				mListener = null;
			}
		}
	}

	public final class FireflyClientCallbackProxy_h extends
			CallbackProxy_b<Boolean> {
		public final int statusCode;
		public final Bundle bundle_BH;
		public final IBinder binder_BI;

		public FireflyClientCallbackProxy_h(int statusCode, IBinder binder,
				Bundle bundle) {
			super(Boolean.valueOf(true));
			this.statusCode = statusCode;
			this.binder_BI = binder;
			this.bundle_BH = bundle;
		}

		protected void onFailed_cP() {
		}

		protected void onCallback_a(Boolean paramTListener) {
			if (paramTListener == null) {
				mConnectedState_By = CONNECTION_STATUS_DISCONNECTED;
				return;
			}
			switch (this.statusCode) {
			case 0:
				String str = null;
				try {
					str = this.binder_BI.getInterfaceDescriptor();
					if (getInterfaceDescriptor_aG().equals(str)) {
						mService_Bv = getService_p(this.binder_BI);
						if (mService_Bv != null) {
							mConnectedState_By = CONNECTION_STATUS_CONNECTED;
							mFmsClientEvent_zx.notifyOnConnected_bo();
							return;
						}
					}
				} catch (RemoteException e) {
				}
				FireflyServiceManager_ej.getInstance_y(mContext)
						.unbindService_b(getServiceName_aF(),
								mFmsClientServiceConnection_Bx);
				mFmsClientServiceConnection_Bx = null;
				mConnectedState_By = CONNECTION_STATUS_DISCONNECTED;
				mService_Bv = null;
				mFmsClientEvent_zx
						.notifyOnConnectionFailed_a(new ConnectionResult(8,
								null));
				break;
			case 10:
				mConnectedState_By = CONNECTION_STATUS_DISCONNECTED;
				throw new IllegalStateException(
						"A fatal developer error has occurred. Check the logs for further information.");
			default:
				PendingIntent pendingIntent = null;
				if (this.bundle_BH != null) {
					pendingIntent = (PendingIntent) this.bundle_BH
							.getParcelable("pendingIntent");
				}
				if (mFmsClientServiceConnection_Bx != null) {
					FireflyServiceManager_ej.getInstance_y(mContext)
							.unbindService_b(
									FireflyClient_eh.this.getServiceName_aF(),
									mFmsClientServiceConnection_Bx);
					mFmsClientServiceConnection_Bx = null;
				}
				mConnectedState_By = CONNECTION_STATUS_DISCONNECTED;
				mService_Bv = null;
				mFmsClientEvent_zx
						.notifyOnConnectionFailed_a(new ConnectionResult(
								this.statusCode, pendingIntent));
			}

		}
	}

	static final int MSG_WHAT_CONNECTION_FAILED = 3;
	static final int MSG_WHAT_DISCONNECTED = 4;

	final class FireflyClientHandler_a extends Handler {
		public FireflyClientHandler_a(Looper arg2) {
			super();
		}

		public void handleMessage(Message msg) {
			if ((msg.what == 2) || (msg.what == 1)) {
				if (!isConnecting()) {
					CallbackProxy_b cb = (CallbackProxy_b) msg.obj;
					cb.onFailed_cP();
					cb.unregister();
					return;
				} else {
					CallbackProxy_b cb = (CallbackProxy_b) msg.obj;
					cb.call_ec();
					return;
				}
			}
			if (msg.what == MSG_WHAT_CONNECTION_FAILED) {
				mFmsClientEvent_zx
						.notifyOnConnectionFailed_a(new ConnectionResult(
								((Integer) msg.obj).intValue(), null));
				return;
			}
			if (msg.what == MSG_WHAT_DISCONNECTED) {
				mConnectedState_By = CONNECTION_STATUS_DISCONNECTED;
				mService_Bv = null;
				mFmsClientEvent_zx
						.notifyOnConnectionSuspended_P(((Integer) msg.obj)
								.intValue());
				return;
			}
			Log.wtf("FireflyClient", "Don't know how to handle this message.");
		}
	}

	protected final void getServiceFromBroker_x(IBinder paramIBinder) {
		try {
			IFireflyServiceBroker_en serviceBroker = IFireflyServiceBroker_en.Stub_a
					.asInterface_z(paramIBinder);
			getServiceFromBroker_a(serviceBroker, new IFireflyCallbackImpl_e(
					this));
		} catch (RemoteException e) {
			Log.w("FireflyClient", "service died");
		}
	}

	final class FireflyClientServiceConnection_f implements ServiceConnection {
		FireflyClientServiceConnection_f() {
		}

		public void onServiceConnected(ComponentName component, IBinder binder) {
			getServiceFromBroker_x(binder);
		}

		public void onServiceDisconnected(ComponentName component) {
			mFireflyClientHandler.sendMessage(mFireflyClientHandler
					.obtainMessage(MSG_WHAT_DISCONNECTED, Integer.valueOf(1)));
		}
	}
}
