package tv.matchstick.client.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import tv.matchstick.client.internal.FlingClient.FlingClientServiceConnection;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class FlingServiceManager implements Handler.Callback {

	private static final Object mLock = new Object();
	private static FlingServiceManager mInstance;
	private final Context mContext;
	private final HashMap<String, ServiceStatusHolder> mServiceStatusHolderMap = new HashMap();
	private final Handler mHandler;

	public static FlingServiceManager getInstance(Context context) {
		synchronized (mLock) {
			if (mInstance == null)
				mInstance = new FlingServiceManager(
						context.getApplicationContext());
		}
		return mInstance;
	}

	private FlingServiceManager(Context context) {
		mContext = context.getApplicationContext();
		mHandler = new Handler(context.getMainLooper(), this);
	}

	public boolean bindService(String serviceName,
			FlingClientServiceConnection serviceConnection) {
		synchronized (mServiceStatusHolderMap) {
			ServiceStatusHolder holder = mServiceStatusHolderMap
					.get(serviceName);
			if (holder == null) {
				holder = new ServiceStatusHolder(serviceName);
				holder.addToConnectionsSet(serviceConnection);
				Intent intent = new Intent(serviceName).setPackage(mContext
						.getPackageName());
				boolean isBounded = mContext.bindService(intent,
						holder.getInnerServiceConnection(),
						Context.BIND_ADJUST_WITH_ACTIVITY
								| Context.BIND_AUTO_CREATE); // flag = 129
				holder.setBound(isBounded);
				mServiceStatusHolderMap.put(serviceName, holder);
			} else {
				mHandler.removeMessages(0, holder);
				if (holder.isConnectionsSetContains(serviceConnection)) {
					throw new IllegalStateException(
							"Trying to bind a FlingServiceConnection that was already connected before.  startServiceAction="
									+ serviceName);
				}
				holder.addToConnectionsSet(serviceConnection);
				switch (holder.getState()) {
				case 1:
					serviceConnection.onServiceConnected(
							holder.getComponentName(), holder.getBinder());
					break;
				case 2:
					Intent intent = new Intent(serviceName)
							.setPackage(mContext.getPackageName());
					boolean isBounded = mContext.bindService(intent,
							holder.getInnerServiceConnection(),
							Context.BIND_ADJUST_WITH_ACTIVITY
									| Context.BIND_AUTO_CREATE); // flag = 129
					holder.setBound(isBounded);
				}
			}
			return holder.isBound();
		}
	}

	public void unbindService(String paramString,
			FlingClientServiceConnection serviceConnection) {
		synchronized (this.mServiceStatusHolderMap) {
			ServiceStatusHolder holder = mServiceStatusHolderMap
					.get(paramString);
			if (holder == null) {
				throw new IllegalStateException(
						"Nonexistent connection status for service action: "
								+ paramString);
			}
			if (!(holder.isConnectionsSetContains(serviceConnection))) {
				throw new IllegalStateException(
						"Trying to unbind a FlingServiceConnection  that was not bound before.  startServiceAction="
								+ paramString);
			}
			holder.removeFromConnectionsSet(serviceConnection);
			if (holder.isConnectionsSetEmpty()) {
				Message message = mHandler.obtainMessage(0, holder);
				mHandler.sendMessageDelayed(message, 5000L);
			}
		}
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 0:
			ServiceStatusHolder holder = (ServiceStatusHolder) msg.obj;
			synchronized (mServiceStatusHolderMap) {
				if (holder.isConnectionsSetEmpty()) {
					mContext.unbindService(holder
							.getInnerServiceConnection());
					mServiceStatusHolderMap.remove(holder
							.getServiceName());
				}
			}
			return true;
		}
		return false;
	}

	final class ServiceStatusHolder {
		private final String serviceName;
		private final InnerServiceConnection innerServiceConnection;
		private final HashSet<FlingClientServiceConnection> connectionsSet;
		private int mState;
		private boolean isBound;
		private IBinder service;
		private ComponentName componentName;

		public ServiceStatusHolder(String serviceName) {
			this.serviceName = serviceName;
			this.innerServiceConnection = new InnerServiceConnection();
			this.connectionsSet = new HashSet<FlingClientServiceConnection>();
			this.mState = 0;
		}

		public void addToConnectionsSet(FlingClientServiceConnection connection) {
			this.connectionsSet.add(connection);
		}

		public void removeFromConnectionsSet(FlingClientServiceConnection connection) {
			this.connectionsSet.remove(connection);
		}

		public InnerServiceConnection getInnerServiceConnection() {
			return this.innerServiceConnection;
		}

		public String getServiceName() {
			return this.serviceName;
		}

		public void setBound(boolean isBound) {
			this.isBound = isBound;
		}

		public boolean isBound() {
			return this.isBound;
		}

		public int getState() {
			return this.mState;
		}

		public boolean isConnectionsSetContains(FlingClientServiceConnection parameh) {
			return this.connectionsSet.contains(parameh);
		}

		public boolean isConnectionsSetEmpty() {
			return this.connectionsSet.isEmpty();
		}

		public IBinder getBinder() {
			return this.service;
		}

		public ComponentName getComponentName() {
			return this.componentName;
		}

		public class InnerServiceConnection implements ServiceConnection {
			public void onServiceConnected(ComponentName component,
					IBinder binder) {
				synchronized (mServiceStatusHolderMap) {
					service = binder;
					componentName = component;
					Iterator it = connectionsSet.iterator();
					while (it.hasNext()) {
						FlingClientServiceConnection c = (FlingClientServiceConnection) it
								.next();
						c.onServiceConnected(component, binder);
					}
					mState = 1;
				}
			}

			public void onServiceDisconnected(ComponentName component) {
				synchronized (mServiceStatusHolderMap) {
					service = null;
					componentName = component;
					Iterator it = connectionsSet.iterator();
					while (it.hasNext()) {
						FlingClientServiceConnection c = (FlingClientServiceConnection) it
								.next();
						c.onServiceDisconnected(component);
					}
					mState = 2;
				}
			}
		}
	}

}
