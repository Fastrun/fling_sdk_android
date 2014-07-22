package tv.matchstick.client.common.api;

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

import tv.matchstick.client.internal.AccountInfo;
import tv.matchstick.client.internal.FlingClientEvents;
import tv.matchstick.client.internal.ValueChecker;
import tv.matchstick.fling.ConnectionResult;
import tv.matchstick.fling.FlingManager;
import tv.matchstick.fling.Result;
import tv.matchstick.fling.internal.Api;
import tv.matchstick.fling.internal.MatchStickApi.MatchStickApiImpl;
import android.content.Context;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Implementation of FlingManager.
 *
 * It's a concrete implementation of FlingManager interface.
 */
public final class FlingManagerImpl implements FlingManager {
	private final FlingClientEvents mFlingClientEvents;

	final Queue<FlingApiClientTask<?>> mPendingTaskQueue = new LinkedList<FlingApiClientTask<?>>();
	final Handler mHander;
	final Set<FlingApiClientTask<?>> mReleaseableTasks = new HashSet<FlingApiClientTask<?>>();

	private final Lock mLock = new ReentrantLock();
	private final Condition mCondition = mLock.newCondition();
	private final Bundle mBundle = new Bundle();
	private final Map<Api.ConnectionBuilder<?>, Api.ConnectionApi> mConnectionMap = new HashMap<Api.ConnectionBuilder<?>, Api.ConnectionApi>();

	private int mCurrentPrority;
	private int mConnectState = 4; // 1: connecting, 2: connected, 3:
	private int mRetryConnectCounter = 0;
	private int mConnectionMapSize;
	private boolean zD = false;
	private boolean mCanReceiveEvent;
	private long mMessageDelay = 5000L;
	private ConnectionResult mConnectionFailedResult;

	/**
	 * Called when release resource.
	 */
	private final ReleaseCallback mReleaseCallback = new ReleaseCallback() {
		public void onRelease(
				FlingManagerImpl.FlingApiClientTask paramc) {
			mLock.lock();
			try {
				mReleaseableTasks.remove(paramc);
			} finally {
				mLock.unlock();
			}
		}
	};

	/**
	 * Called when connected.
	 */
	final ConnectionCallbacks mConnectionCallbacks = new ConnectionCallbacks() {
		/**
		 * Connected event
		 */
		public void onConnected(Bundle connectionHint) {
			mLock.lock();
			try {
				if (mConnectState == 1) {
					if (connectionHint != null) {
						mBundle.putAll(connectionHint);
					}
					notifyConnectionResult();
				}
			} finally {
				mLock.unlock();
			}
		}

		/**
		 * Connection suspended event
		 */
		public void onConnectionSuspended(int cause) {
			mLock.lock();
			try {
				onDisconnected(cause);
				switch (cause) {
				case 2:
					connect();
					break;
				case 1:
					if (canRetryConnect()) {
						return;
					}
					mRetryConnectCounter = 2;
					mHander.sendMessageDelayed(
							mHander.obtainMessage(MSG_WHAT_DO_CONNECT),
							mMessageDelay);
				}
			} finally {
				mLock.unlock();
			}
		}
	};

	/*
	 * b$3.smali : OK
	 */
	private final FlingClientEvents.FmsClientEventCallback mFmsClientEventCallback = new FlingClientEvents.FmsClientEventCallback() {
		public boolean canReceiveEvent() {
			return mCanReceiveEvent;
		}

		public boolean isConnected() {
			return FlingManagerImpl.this.isConnected();
		}

		public Bundle getBundle() {
			return null;
		}
	};

	/**
	 * Implementation of FlingManager
	 *
	 * @param context
	 * @param looper
	 * @param account
	 * @param apiOptionsMap
	 * @param connCallbacksSet
	 * @param connFailedListenerSet
	 */
	public FlingManagerImpl(Context context, Looper looper,
			AccountInfo account, Map<Api, ApiOptions> apiOptionsMap,
			Set<ConnectionCallbacks> connCallbacksSet,
			Set<OnConnectionFailedListener> connFailedListenerSet) {
		mFlingClientEvents = new FlingClientEvents(context, looper,
				mFmsClientEventCallback);
		mHander = new FlingApiClientHandler(looper);

		Iterator<ConnectionCallbacks> itCallbacks = connCallbacksSet.iterator();
		while (itCallbacks.hasNext()) {
			ConnectionCallbacks cb = itCallbacks.next();
			mFlingClientEvents.registerConnectionCallbacks(cb);
		}

		Iterator<OnConnectionFailedListener> itListener = connFailedListenerSet
				.iterator();
		while (itListener.hasNext()) {
			mFlingClientEvents.registerConnectionFailedListener(itListener
					.next());
		}

		Iterator<Api> apis = apiOptionsMap.keySet().iterator();
		while (apis.hasNext()) {
			Api api = apis.next();
			final Api.ConnectionBuilder<?> builder = api
					.getConnectionBuilder();
			ApiOptions apiOption = apiOptionsMap.get(api);
			mConnectionMap.put(builder, builder.build(
					context, looper, account, apiOption,
					mConnectionCallbacks,
					/**
					 * Connection failed listener 
					 */
					new OnConnectionFailedListener() {
						
						/**
						 * Connection failed event happens
						 */
						public void onConnectionFailed(ConnectionResult result) {
							mLock.lock();
							try {
								if ((mConnectionFailedResult == null)
										|| (builder.getPriority() < mCurrentPrority)) {
									mConnectionFailedResult = result;
									mCurrentPrority = builder.getPriority();
								}
								notifyConnectionResult();
							} finally {
								mLock.unlock();
							}
						}
					}));
		}
	}

	/*************************************************/
	// private methods
	/*************************************************/

	// uncheck smali
	private void notifyConnectionResult() {
		mLock.lock();
		try {
			mConnectionMapSize -= 1;
			if (mConnectionMapSize == 0) {
				if (mConnectionFailedResult != null) {
					zD = false;
					onDisconnected(3);
					if (canRetryConnect()) {
						mRetryConnectCounter -= 1;
					}
					if (canRetryConnect()) {
						mHander.sendMessageDelayed(
								mHander.obtainMessage(MSG_WHAT_DO_CONNECT),
								mMessageDelay);
					} else {
						mFlingClientEvents
								.notifyOnConnectionFailed(mConnectionFailedResult);
					}
					mCanReceiveEvent = false;
				} else {
					mConnectState = 2;
					cancelReconnect();
					mCondition.signalAll();
					flushQueue();
					if (zD) {
						zD = false;
						onDisconnected(-1);
					} else {
						Bundle localBundle = (mBundle.isEmpty()) ? null
								: mBundle;
						mFlingClientEvents
								.notifyOnConnected(localBundle);
					}
				}
			}
		} finally {
			mLock.unlock();
		}
	}

	private <A extends Api.ConnectionApi> void execute(
			FlingApiClientTask<A> task) throws DeadObjectException {
		mLock.lock();
		try {
			ValueChecker.checkTrueWithErrorMsg(isConnected(),
					"FlingManager is not connected yet.");
			ValueChecker
					.checkTrueWithErrorMsg(
							task.getConnectionBuiler() != null,
							"This task can not be executed or enqueued (it's probably a Batch or malformed)");
			if (task instanceof Releasable) {
				mReleaseableTasks.add(task);
				task.setReleaseCallback(mReleaseCallback);
			}

			A connection = getConnectionApi(task.getConnectionBuiler());
			task.execute_b(connection);
		} finally {
			mLock.unlock();
		}
	}

	/**
	 * Flush pending task queue.
	 */
	private void flushQueue() {
		ValueChecker.checkTrueWithErrorMsg(isConnected(),
				"FlingManager is not connected yet.");
		mLock.lock();
		try {
			while (!(mPendingTaskQueue.isEmpty())) {
				try {
					execute((FlingApiClientTask) mPendingTaskQueue
							.remove());
				} catch (DeadObjectException localDeadObjectException) {
					Log.w("FlingManagerImpl",
							"Service died while flushing queue",
							localDeadObjectException);
				}
			}
		} finally {
			mLock.unlock();
		}
	}

	/**
	 * Disconnected event
	 *
	 * cause: -1 : cancel
	 */
	private void onDisconnected(int cause) {
		mLock.lock();
		try {
			if (mConnectState != 3) {
				if (cause == -1) { //cancel action
					if (isConnecting()) {
						Iterator tasks = mPendingTaskQueue.iterator();
						while (tasks.hasNext()) {
							FlingApiClientTask task = (FlingApiClientTask) tasks
									.next();
							if (task.getFlag() != 1) {
								tasks.remove();
							}
						}
					} else {
						mPendingTaskQueue.clear();
					}
					if ((mConnectionFailedResult == null)
							&& (!(mPendingTaskQueue.isEmpty()))) {
						zD = true;
						return;
					}
				}
				boolean isConnecting = isConnecting();
				boolean isConnected = isConnected();
				mConnectState = 3;
				if (isConnecting) {
					if (cause == -1) { //canceled
						mConnectionFailedResult = null;
					}
					this.mCondition.signalAll();
				}
				Iterator tasks = mReleaseableTasks.iterator();
				while (tasks.hasNext()) {
					FlingApiClientTask c = (FlingApiClientTask) tasks
							.next();
					c.release();
				}
				mReleaseableTasks.clear();
				mCanReceiveEvent = false;
				Iterator connections = mConnectionMap.values().iterator();
				while (connections.hasNext()) {
					Api.ConnectionApi c = (Api.ConnectionApi) connections
							.next();
					if (c.isConnected()) {
						c.disconnect();
					}
				}
				mCanReceiveEvent = true;
				mConnectState = 4;
				if (isConnected) {
					if (cause != -1) {
						mFlingClientEvents
								.notifyOnConnectionSuspended(cause);
					}
					mCanReceiveEvent = false;
				}
			}
		} finally {
			this.mLock.unlock();
		}
	}

	/**
	 * Whether we can retry connect.
	 *
	 * @return
	 */
	private boolean canRetryConnect() {
		mLock.lock();
		try {
			return (mRetryConnectCounter != 0) ? true : false;
		} finally {
			mLock.unlock();
		}
	}

	/**
	 * Cancel reconnect action.
	 */
	private void cancelReconnect() {
		mLock.lock();
		try {
			mRetryConnectCounter = 0;
			mHander.removeMessages(MSG_WHAT_DO_CONNECT);
		} finally {
			mLock.unlock();
		}
	}

	// unused
	/*
	public <A extends Api.ConnectionApi, T extends MatchStickApiImpl<? extends Result, A>> T a(
			T task) {
		mLock.lock();
		try {
			if (isConnected()) {
				executeTask(task);
			} else {
				mPendingTaskQueue.add(task);
			}
			return task;
		} finally {
			mLock.unlock();
		}
	}
	*/

	/**
	 * Do Fling actions with Fling service.
	 */
	public <A extends Api.ConnectionApi, T extends MatchStickApiImpl<? extends Result, A>> T executeTask(
			T task) {
		ValueChecker.checkTrueWithErrorMsg(isConnected(),
				"FlingManager is not connected yet.");
		flushQueue();
		try {
			execute(task);
		} catch (DeadObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return task;
	}

	/**
	 * Get connection api in connection map by builder
	 */
	public <C extends Api.ConnectionApi> C getConnectionApi(
			Api.ConnectionBuilder<C> builder) {
		C connection = (C) mConnectionMap.get(builder);
		ValueChecker.checkNullPointer(connection,
				"Appropriate Api was not requested.");
		return connection;
	}

	/**
	 * Ready to connect to Fling device.
	 */
	public void connect() {
		mLock.lock();
		try {
			zD = false;
			if ((isConnected()) || (isConnecting())) {
				return;
			}
			mCanReceiveEvent = true;
			mConnectionFailedResult = null;
			mConnectState = 1; // connecting...
			mBundle.clear();
			mConnectionMapSize = mConnectionMap.size();
			Iterator<Api.ConnectionApi> connections = mConnectionMap
					.values().iterator();
			while (connections.hasNext()) {
				Api.ConnectionApi c = connections.next();
				c.connect(); //call connect() in FlingApi
			}
		} finally {
			mLock.unlock();
		}
	}

	/**
	 * Blocking connect.
	 */
	public ConnectionResult blockingConnect(long timeout, TimeUnit unit) {
		ValueChecker.checkTrueWithErrorMsg(
				Looper.myLooper() != Looper.getMainLooper(),
				"blockingConnect must not be called on the UI thread");
		mLock.lock();
		try {
			connect();
			long l = unit.toNanos(timeout);
			while (isConnecting()) {
				try {
					l = mCondition.awaitNanos(l);
					if (l <= 0L) {
						ConnectionResult timeoutResult = new ConnectionResult(
								ConnectionResult.TIMEOUT, null);
						mLock.unlock();
						return timeoutResult;
					}
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					ConnectionResult interruptResult = new ConnectionResult(
							ConnectionResult.INTERRUPTED, null);
					mLock.unlock();
					return interruptResult;
				}
			}
			if (isConnected()) {
				return ConnectionResult.connectResult;
			}
			if (this.mConnectionFailedResult != null) {
				return mConnectionFailedResult;
			}
			ConnectionResult conceledResult = new ConnectionResult(ConnectionResult.CANCELED,
					null);
			return conceledResult;
		} finally {
			mLock.unlock();
		}
	}

	/**
	 * Disconnect
	 */
	public void disconnect() {
		/**
		 * cancel reconnect action
		 */
		cancelReconnect();
		
		/**
		 * call disconnect callback
		 */
		onDisconnected(-1);
	}

	/**
	 * Do reconnect
	 */
	public void reconnect() {
		/**
		 * disconnect first
		 */
		disconnect();
		
		/**
		 * connect
		 */
		connect();
	}

	/**
	 * Connected?!
	 */
	public boolean isConnected() {
		mLock.lock();
		try {
			return (mConnectState == 2) ? true : false;
		} finally {
			mLock.unlock();
		}
	}

	/**
	 * Is connecting?
	 */
	public boolean isConnecting() {
		mLock.lock();
		try {
			return (mConnectState == 1) ? true : false;
		} finally {
			mLock.unlock();
		}
	}
	
	@Override
	public void registerConnectionCallbacks(ConnectionCallbacks listener) {
		mFlingClientEvents.registerConnectionCallbacks(listener);
	}

	@Override
	public boolean isConnectionCallbacksRegistered(ConnectionCallbacks listener) {
		return mFlingClientEvents
				.isConnectionCallbacksRegistered(listener);
	}

	@Override
	public void unregisterConnectionCallbacks(ConnectionCallbacks listener) {
		mFlingClientEvents.unregisterConnectionCallbacks(listener);
	}

	@Override
	public void registerConnectionFailedListener(
			OnConnectionFailedListener listener) {
		mFlingClientEvents.registerConnectionFailedListener(listener);
	}

	@Override
	public boolean isConnectionFailedListenerRegistered(
			OnConnectionFailedListener listener) {
		return mFlingClientEvents
				.isConnectionFailedListenerRegistered(listener);
	}

	@Override
	public void unregisterConnectionFailedListener(
			OnConnectionFailedListener listener) {
		mFlingClientEvents.unregisterConnectionFailedListener(listener);
	}

	/**
	 * Release callback interface
	 */
	public interface ReleaseCallback {
		public void onRelease(FlingApiClientTask task);
	}

	static final int MSG_WHAT_DO_CONNECT = 1;

	/**
	 * Handler
	 */
	class FlingApiClientHandler extends Handler {
		FlingApiClientHandler(Looper paramLooper) {
			super(paramLooper);
		}

		public void handleMessage(Message msg) {
			if (msg.what == MSG_WHAT_DO_CONNECT) {
				mLock.lock();
				try {
					if (!isConnected() && !isConnecting()) {
						connect();
					}
					return;
				} finally {
					mLock.unlock();
				}
			}
			Log.wtf("FlingManagerImpl",
					"Don't know how to handle this message.");
		}
	}

	/**
	 * Fling tasks
	 *
	 * @param <A>
	 */
	public interface FlingApiClientTask<A extends Api.ConnectionApi> {
		public Api.ConnectionBuilder<A> getConnectionBuiler();

		public void execute_b(A connection) throws DeadObjectException;

		public void setReleaseCallback(ReleaseCallback callback);

		public void release();

		// The task can be removed if return non 1.
		public int getFlag();
	}
}
