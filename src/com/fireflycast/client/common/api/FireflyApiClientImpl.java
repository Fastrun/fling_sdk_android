package com.fireflycast.client.common.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.fireflycast.cast.Api;
import com.fireflycast.cast.ConnectionResult;
import com.fireflycast.cast.FireflyApiClient;
import com.fireflycast.cast.Result;
import com.fireflycast.cast.FireflyApi.FireflyApiImpl_a;
import com.fireflycast.client.internal.AccountInfo;
import com.fireflycast.client.internal.FireflyClientEvents;
import com.fireflycast.client.internal.ValueChecker;

/*
 * b.smali : OK
 * original name: GoogleApiClientImpl_b
 */
public final class FireflyApiClientImpl implements FireflyApiClient {
	private final FireflyClientEvents mFireflyClientEvents_zx;

	final Queue<FireflyApiClientTask<?>> mPendingTaskQueue_zy = new LinkedList<FireflyApiClientTask<?>>();
	final Handler mHander_zG;
	final Set<FireflyApiClientTask<?>> mReleaseableTasks_zK = new HashSet<FireflyApiClientTask<?>>();

	private final Lock mLock_zv = new ReentrantLock();
	private final Condition mCondition_zw = mLock_zv.newCondition();
	private final Bundle mBundle_zH = new Bundle();
	private final Map<Api.ConnectionBuilder_b<?>, Api.ConnectionApi_a> mConnectionMap_zI = new HashMap<Api.ConnectionBuilder_b<?>, Api.ConnectionApi_a>();

	private int zA;
	private int mConnectState_zB = 4;
	private int mRetryConnectCounter_zC = 0;
	private int zE;
	private boolean zD = false;
	private boolean mCanReceiveEvent_zJ;
	private long mMessageDelay_zF = 5000L;
	private ConnectionResult mConnectionFailedResult_zz;

	/*
	 * b$1.smali : OK
	 */
	private final ReleaseCallback_a mReleaseCallback_zm = new ReleaseCallback_a() {
		public void onRelease_b(
				FireflyApiClientImpl.FireflyApiClientTask paramc) {
			mLock_zv.lock();
			try {
				mReleaseableTasks_zK.remove(paramc);
			} finally {
				mLock_zv.unlock();
			}
		}
	};

	/*
	 * b$2.smali : OK
	 */
	final ConnectionCallbacks mConnectionCallbacks_zL = new ConnectionCallbacks() {
		public void onConnected(Bundle connectionHint) {
			mLock_zv.lock();
			try {
				if (mConnectState_zB == 1) {
					if (connectionHint != null) {
						mBundle_zH.putAll(connectionHint);
					}
					notifyConnectionResult_dy();
				}
			} finally {
				mLock_zv.unlock();
			}
		}

		public void onConnectionSuspended(int cause) {
			mLock_zv.lock();
			try {
				onDisconnected_G(cause);
				switch (cause) {
				case 2:
					connect();
					break;
				case 1:
					if (canRetryConnect_dA()) {
						return;
					}
					mRetryConnectCounter_zC = 2;
					mHander_zG.sendMessageDelayed(
							mHander_zG.obtainMessage(MSG_WHAT_DO_CONNECT),
							mMessageDelay_zF);
				}
			} finally {
				mLock_zv.unlock();
			}
		}
	};

	/*
	 * b$3.smali : OK
	 */
	private final FireflyClientEvents.FmsClientEventCallback_b mFmsClientEventCallback_zM = new FireflyClientEvents.FmsClientEventCallback_b() {
		public boolean canReceiveEvent_dC() {
			return mCanReceiveEvent_zJ;
		}

		public boolean isConnected() {
			return FireflyApiClientImpl.this.isConnected();
		}

		public Bundle getBundle_cY() {
			return null;
		}
	};

	public FireflyApiClientImpl(Context context, Looper looper,
			AccountInfo account, Map<Api, ApiOptions> apiOptionsMap,
			Set<ConnectionCallbacks> connCallbacksSet,
			Set<OnConnectionFailedListener> connFailedListenerSet) {
		mFireflyClientEvents_zx = new FireflyClientEvents(context, looper,
				mFmsClientEventCallback_zM);
		mHander_zG = new FireflyApiClientHandler_b(looper);

		Iterator<ConnectionCallbacks> itCallbacks = connCallbacksSet.iterator();
		while (itCallbacks.hasNext()) {
			ConnectionCallbacks cb = itCallbacks.next();
			mFireflyClientEvents_zx.registerConnectionCallbacks(cb);
		}

		Iterator<OnConnectionFailedListener> itListener = connFailedListenerSet
				.iterator();
		while (itListener.hasNext()) {
			mFireflyClientEvents_zx.registerConnectionFailedListener(itListener
					.next());
		}

		Iterator<Api> apis = apiOptionsMap.keySet().iterator();
		while (apis.hasNext()) {
			Api api = apis.next();
			final Api.ConnectionBuilder_b<?> builder = api
					.getConnectionBuilder_dp();
			ApiOptions apiOption = apiOptionsMap.get(api);
			mConnectionMap_zI.put(builder, builder.build_b(
					// build_b return CastClientImpl_dq
					context, looper, account, apiOption,
					mConnectionCallbacks_zL,
					/*
					 * b$4.smali : OK
					 */
					new OnConnectionFailedListener() {
						public void onConnectionFailed(ConnectionResult result) {
							mLock_zv.lock();
							try {
								if ((mConnectionFailedResult_zz == null)
										|| (builder.getPriority() < zA)) {
									mConnectionFailedResult_zz = result;
									zA = builder.getPriority();
								}
								notifyConnectionResult_dy();
							} finally {
								mLock_zv.unlock();
							}
						}
					}));
		}
	}

	/*************************************************/
	// private methods
	/*************************************************/

	// uncheck smali
	private void notifyConnectionResult_dy() {
		mLock_zv.lock();
		try {
			zE -= 1;
			if (zE == 0) {
				if (mConnectionFailedResult_zz != null) {
					zD = false;
					onDisconnected_G(3);
					if (canRetryConnect_dA()) {
						mRetryConnectCounter_zC -= 1;
					}
					if (canRetryConnect_dA()) {
						mHander_zG.sendMessageDelayed(
								mHander_zG.obtainMessage(MSG_WHAT_DO_CONNECT),
								mMessageDelay_zF);
					} else {
						mFireflyClientEvents_zx
								.notifyOnConnectionFailed_a(mConnectionFailedResult_zz);
					}
					mCanReceiveEvent_zJ = false;
				} else {
					mConnectState_zB = 2;
					cancelReconnect_dB();
					mCondition_zw.signalAll();
					flushQueue_dz();
					if (zD) {
						zD = false;
						onDisconnected_G(-1);
					} else {
						Bundle localBundle = (mBundle_zH.isEmpty()) ? null
								: mBundle_zH;
						mFireflyClientEvents_zx
								.notifyOnConnected_b(localBundle);
					}
				}
			}
		} finally {
			mLock_zv.unlock();
		}
	}

	private <A extends Api.ConnectionApi_a> void execute_a(
			FireflyApiClientTask<A> task) throws DeadObjectException {
		mLock_zv.lock();
		try {
			ValueChecker.checkTrueWithErrorMsg(isConnected(),
					"GoogleApiClient is not connected yet.");
			ValueChecker
					.checkTrueWithErrorMsg(
							task.getConnectionBuiler_dp() != null,
							"This task can not be executed or enqueued (it's probably a Batch or malformed)");
			if (task instanceof Releasable) {
				mReleaseableTasks_zK.add(task);
				task.setReleaseCallback_a(mReleaseCallback_zm);
			}

			A connection = getConnectionApi_a(task.getConnectionBuiler_dp());
			task.execute_b(connection);
		} finally {
			mLock_zv.unlock();
		}
	}

	private void flushQueue_dz() {
		ValueChecker.checkTrueWithErrorMsg(isConnected(),
				"GoogleApiClient is not connected yet.");
		mLock_zv.lock();
		try {
			while (!(mPendingTaskQueue_zy.isEmpty())) {
				try {
					execute_a((FireflyApiClientTask) mPendingTaskQueue_zy
							.remove());
				} catch (DeadObjectException localDeadObjectException) {
					Log.w("GoogleApiClientImpl",
							"Service died while flushing queue",
							localDeadObjectException);
				}
			}
		} finally {
			mLock_zv.unlock();
		}
	}

	/*
	 * cause: -1 : cancel
	 */
	private void onDisconnected_G(int cause) {
		mLock_zv.lock();
		try {
			if (mConnectState_zB != 3) {
				if (cause == -1) {
					if (isConnecting()) {
						Iterator tasks = mPendingTaskQueue_zy.iterator();
						while (tasks.hasNext()) {
							FireflyApiClientTask task = (FireflyApiClientTask) tasks
									.next();
							if (task.getFlag_dr() != 1) {
								tasks.remove();
							}
						}
					} else {
						mPendingTaskQueue_zy.clear();
					}
					if ((mConnectionFailedResult_zz == null)
							&& (!(mPendingTaskQueue_zy.isEmpty()))) {
						zD = true;
						return;
					}
				}
				boolean isConnecting = isConnecting();
				boolean isConnected = isConnected();
				mConnectState_zB = 3;
				if (isConnecting) {
					if (cause == -1) {
						mConnectionFailedResult_zz = null;
					}
					this.mCondition_zw.signalAll();
				}
				Iterator tasks = mReleaseableTasks_zK.iterator();
				while (tasks.hasNext()) {
					FireflyApiClientTask c = (FireflyApiClientTask) tasks
							.next();
					c.release_du();
				}
				mReleaseableTasks_zK.clear();
				mCanReceiveEvent_zJ = false;
				Iterator connections = mConnectionMap_zI.values().iterator();
				while (connections.hasNext()) {
					Api.ConnectionApi_a c = (Api.ConnectionApi_a) connections
							.next();
					if (c.isConnected()) {
						c.disconnect();
					}
				}
				mCanReceiveEvent_zJ = true;
				mConnectState_zB = 4;
				if (isConnected) {
					if (cause != -1) {
						mFireflyClientEvents_zx
								.notifyOnConnectionSuspended_P(cause);
					}
					mCanReceiveEvent_zJ = false;
				}
			}
		} finally {
			this.mLock_zv.unlock();
		}
	}

	private boolean canRetryConnect_dA() {
		mLock_zv.lock();
		try {
			return (mRetryConnectCounter_zC != 0) ? true : false;
		} finally {
			mLock_zv.unlock();
		}
	}

	private void cancelReconnect_dB() {
		mLock_zv.lock();
		try {
			mRetryConnectCounter_zC = 0;
			mHander_zG.removeMessages(MSG_WHAT_DO_CONNECT);
		} finally {
			mLock_zv.unlock();
		}
	}

	// unused
	public <A extends Api.ConnectionApi_a, T extends FireflyApiImpl_a<? extends Result, A>> T a(
			T task) {
		mLock_zv.lock();
		try {
			if (isConnected()) {
				executeTask_b(task);
			} else {
				mPendingTaskQueue_zy.add(task);
			}
			return task;
		} finally {
			mLock_zv.unlock();
		}
	}

	public <A extends Api.ConnectionApi_a, T extends FireflyApiImpl_a<? extends Result, A>> T executeTask_b(
			T paramT) {
		ValueChecker.checkTrueWithErrorMsg(isConnected(),
				"GoogleApiClient is not connected yet.");
		flushQueue_dz();
		try {
			execute_a(paramT);
		} catch (DeadObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return paramT;
	}

	public <C extends Api.ConnectionApi_a> C getConnectionApi_a(
			Api.ConnectionBuilder_b<C> paramb) {
		C connection = (C) mConnectionMap_zI.get(paramb);
		ValueChecker.checkNullPointer_b(connection,
				"Appropriate Api was not requested.");
		return connection;
	}

	public void connect() {
		mLock_zv.lock();
		try {
			zD = false;
			if ((isConnected()) || (isConnecting())) {
				return;
			}
			mCanReceiveEvent_zJ = true;
			mConnectionFailedResult_zz = null;
			mConnectState_zB = 1;
			mBundle_zH.clear();
			zE = mConnectionMap_zI.size();
			Iterator<Api.ConnectionApi_a> connections = mConnectionMap_zI
					.values().iterator();
			while (connections.hasNext()) {
				Api.ConnectionApi_a c = connections.next();
				c.connect();
			}
		} finally {
			mLock_zv.unlock();
		}
	}

	public ConnectionResult blockingConnect(long timeout, TimeUnit unit) {
		ValueChecker.checkTrueWithErrorMsg(
				Looper.myLooper() != Looper.getMainLooper(),
				"blockingConnect must not be called on the UI thread");
		mLock_zv.lock();
		try {
			connect();
			long l = unit.toNanos(timeout);
			while (isConnecting()) {
				try {
					l = mCondition_zw.awaitNanos(l);
					if (l <= 0L) {
						ConnectionResult localConnectionResult1 = new ConnectionResult(
								14, null);
						mLock_zv.unlock();
						return localConnectionResult1;
					}
				} catch (InterruptedException localInterruptedException1) {
					Thread.currentThread().interrupt();
					ConnectionResult localConnectionResult3 = new ConnectionResult(
							15, null);
					mLock_zv.unlock();
					return localConnectionResult3;
				}
			}
			if (isConnected()) {
				return ConnectionResult.yI;
			}
			if (this.mConnectionFailedResult_zz != null) {
				return mConnectionFailedResult_zz;
			}
			ConnectionResult localConnectionResult2 = new ConnectionResult(13,
					null);
			return localConnectionResult2;
		} finally {
			mLock_zv.unlock();
		}
	}

	public void disconnect() {
		cancelReconnect_dB();
		onDisconnected_G(-1);
	}

	public void reconnect() {
		disconnect();
		connect();
	}

	public boolean isConnected() {
		mLock_zv.lock();
		try {
			return (mConnectState_zB == 2) ? true : false;
		} finally {
			mLock_zv.unlock();
		}
	}

	public boolean isConnecting() {
		mLock_zv.lock();
		try {
			return (mConnectState_zB == 1) ? true : false;
		} finally {
			mLock_zv.unlock();
		}
	}

	public void registerConnectionCallbacks(ConnectionCallbacks listener) {
		mFireflyClientEvents_zx.registerConnectionCallbacks(listener);
	}

	public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
		return mFireflyClientEvents_zx
				.isConnectionCallbacksRegistered(listener);
	}

	public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
		mFireflyClientEvents_zx.unregisterConnectionCallbacks(listener);
	}

	public void registerConnectionFailedListener(
			OnConnectionFailedListener listener) {
		mFireflyClientEvents_zx.registerConnectionFailedListener(listener);
	}

	public boolean isConnectionFailedListenerRegistered(
			OnConnectionFailedListener listener) {
		return mFireflyClientEvents_zx
				.isConnectionFailedListenerRegistered(listener);
	}

	public void unregisterConnectionFailedListener(
			OnConnectionFailedListener listener) {
		mFireflyClientEvents_zx.unregisterConnectionFailedListener(listener);
	}

	public interface ReleaseCallback_a {
		public void onRelease_b(FireflyApiClientTask paramc);
	}

	static final int MSG_WHAT_DO_CONNECT = 1;

	class FireflyApiClientHandler_b extends Handler {
		FireflyApiClientHandler_b(Looper paramLooper) {
			super(paramLooper);
		}

		public void handleMessage(Message msg) {
			if (msg.what == MSG_WHAT_DO_CONNECT) {
				mLock_zv.lock();
				try {
					if (!isConnected() && !isConnecting()) {
						connect();
					}
					return;
				} finally {
					mLock_zv.unlock();
				}
			}
			Log.wtf("GoogleApiClientImpl",
					"Don't know how to handle this message.");
		}
	}

	public interface FireflyApiClientTask<A extends Api.ConnectionApi_a> {
		public Api.ConnectionBuilder_b<A> getConnectionBuiler_dp();

		public void execute_b(A connection) throws DeadObjectException;

		public void setReleaseCallback_a(ReleaseCallback_a callback);

		public void release_du();

		// The task can be removed if return non 1.
		public int getFlag_dr();
	}
}
