package com.fireflycast.client.internal;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.fireflycast.client.internal.FireflyClient.FireflyClientServiceConnection_f;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

public class FireflyServiceManager implements Handler.Callback {

	private static final Object mLock_BQ = new Object();
	private static FireflyServiceManager mInstance_BR;
	private final Context mContext_qe;
	private final HashMap<String, ServiceStatusHolder_a> mServiceStatusHolderMap_BS = new HashMap();
	private final Handler mHandler;

	public static FireflyServiceManager getInstance_y(Context context) {
		synchronized (mLock_BQ) {
			if (mInstance_BR == null)
				mInstance_BR = new FireflyServiceManager(
						context.getApplicationContext());
		}
		return mInstance_BR;
	}

	private FireflyServiceManager(Context context) {
		mContext_qe = context.getApplicationContext();
		mHandler = new Handler(context.getMainLooper(), this);
	}

	public boolean bindService_a(String serviceName,
			FireflyClientServiceConnection_f serviceConnection) {
		synchronized (mServiceStatusHolderMap_BS) {
			ServiceStatusHolder_a holder = mServiceStatusHolderMap_BS
					.get(serviceName);
			if (holder == null) {
				holder = new ServiceStatusHolder_a(serviceName);
				holder.addToConnectionsSet_a(serviceConnection);
				Intent intent = new Intent(serviceName).setPackage(mContext_qe
						.getPackageName());
				boolean isBounded = mContext_qe.bindService(intent,
						holder.getInnerServiceConnection_ee(),
						Context.BIND_ADJUST_WITH_ACTIVITY
								| Context.BIND_AUTO_CREATE); // flag = 129
				holder.setBound_w(isBounded);
				mServiceStatusHolderMap_BS.put(serviceName, holder);
			} else {
				mHandler.removeMessages(0, holder);
				if (holder.isConnectionsSetContains_c(serviceConnection)) {
					throw new IllegalStateException(
							"Trying to bind a FireflyServiceConnection that was already connected before.  startServiceAction="
									+ serviceName);
				}
				holder.addToConnectionsSet_a(serviceConnection);
				switch (holder.getState()) {
				case 1:
					serviceConnection.onServiceConnected(
							holder.getComponentName(), holder.getBinder());
					break;
				case 2:
					Intent intent = new Intent(serviceName)
							.setPackage(mContext_qe.getPackageName());
					boolean isBounded = mContext_qe.bindService(intent,
							holder.getInnerServiceConnection_ee(),
							Context.BIND_ADJUST_WITH_ACTIVITY
									| Context.BIND_AUTO_CREATE); // flag = 129
					holder.setBound_w(isBounded);
				}
			}
			return holder.isBound();
		}
	}

	public void unbindService_b(String paramString,
			FireflyClientServiceConnection_f serviceConnection) {
		synchronized (this.mServiceStatusHolderMap_BS) {
			ServiceStatusHolder_a holder = mServiceStatusHolderMap_BS
					.get(paramString);
			if (holder == null) {
				throw new IllegalStateException(
						"Nonexistent connection status for service action: "
								+ paramString);
			}
			if (!(holder.isConnectionsSetContains_c(serviceConnection))) {
				throw new IllegalStateException(
						"Trying to unbind a FireflyServiceConnection  that was not bound before.  startServiceAction="
								+ paramString);
			}
			holder.removeFromConnectionsSet_b(serviceConnection);
			if (holder.isConnectionsSetEmpty_eg()) {
				Message message = mHandler.obtainMessage(0, holder);
				mHandler.sendMessageDelayed(message, 5000L);
			}
		}
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 0:
			ServiceStatusHolder_a holder = (ServiceStatusHolder_a) msg.obj;
			synchronized (mServiceStatusHolderMap_BS) {
				if (holder.isConnectionsSetEmpty_eg()) {
					mContext_qe.unbindService(holder
							.getInnerServiceConnection_ee());
					mServiceStatusHolderMap_BS.remove(holder
							.getServiceName_ef());
				}
			}
			return true;
		}
		return false;
	}

	final class ServiceStatusHolder_a {
		private final String serviceName_BT;
		private final InnerServiceConnection_a innerServiceConnection_BU;
		private final HashSet<FireflyClientServiceConnection_f> connectionsSet_BV;
		private int mState;
		private boolean isBound_BW;
		private IBinder service_BX;
		private ComponentName componentName_BY;

		public ServiceStatusHolder_a(String serviceName) {
			this.serviceName_BT = serviceName;
			this.innerServiceConnection_BU = new InnerServiceConnection_a();
			this.connectionsSet_BV = new HashSet<FireflyClientServiceConnection_f>();
			this.mState = 0;
		}

		public void addToConnectionsSet_a(FireflyClientServiceConnection_f connection) {
			this.connectionsSet_BV.add(connection);
		}

		public void removeFromConnectionsSet_b(FireflyClientServiceConnection_f connection) {
			this.connectionsSet_BV.remove(connection);
		}

		public InnerServiceConnection_a getInnerServiceConnection_ee() {
			return this.innerServiceConnection_BU;
		}

		public String getServiceName_ef() {
			return this.serviceName_BT;
		}

		public void setBound_w(boolean isBound) {
			this.isBound_BW = isBound;
		}

		public boolean isBound() {
			return this.isBound_BW;
		}

		public int getState() {
			return this.mState;
		}

		public boolean isConnectionsSetContains_c(FireflyClientServiceConnection_f parameh) {
			return this.connectionsSet_BV.contains(parameh);
		}

		public boolean isConnectionsSetEmpty_eg() {
			return this.connectionsSet_BV.isEmpty();
		}

		public IBinder getBinder() {
			return this.service_BX;
		}

		public ComponentName getComponentName() {
			return this.componentName_BY;
		}

		public class InnerServiceConnection_a implements ServiceConnection {
			public void onServiceConnected(ComponentName component,
					IBinder binder) {
				synchronized (mServiceStatusHolderMap_BS) {
					service_BX = binder;
					componentName_BY = component;
					Iterator it = connectionsSet_BV.iterator();
					while (it.hasNext()) {
						FireflyClientServiceConnection_f c = (FireflyClientServiceConnection_f) it
								.next();
						c.onServiceConnected(component, binder);
					}
					mState = 1;
				}
			}

			public void onServiceDisconnected(ComponentName component) {
				synchronized (mServiceStatusHolderMap_BS) {
					service_BX = null;
					componentName_BY = component;
					Iterator it = connectionsSet_BV.iterator();
					while (it.hasNext()) {
						FireflyClientServiceConnection_f c = (FireflyClientServiceConnection_f) it
								.next();
						c.onServiceDisconnected(component);
					}
					mState = 2;
				}
			}
		}
	}

}
