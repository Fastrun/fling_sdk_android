package tv.matchstick.fling.internal;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import tv.matchstick.client.common.api.FlingManagerImpl;
import tv.matchstick.client.common.api.Releasable;
import tv.matchstick.client.common.api.FlingManagerImpl.FlingApiClientTask;
import tv.matchstick.client.internal.ValueChecker;
import tv.matchstick.fling.FlingStatusCodes;
import tv.matchstick.fling.PendingResult;
import tv.matchstick.fling.Result;
import tv.matchstick.fling.ResultCallback;
import tv.matchstick.fling.Status;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;

/**
 * Fling API class
 */
public class MatchStickApi {

	/**
	 * Fling api implementation
	 *
	 * @param <R>
	 *            result
	 * @param <A>
	 *            connection api
	 */
	public static abstract class MatchStickApiImpl<R extends Result, A extends Api.ConnectionApi>
			implements PendingResult<R>, ResultCallback_c<R>,
			FlingApiClientTask<A> {

		/**
		 * Connection Builder
		 */
		private final Api.ConnectionBuilder<A> mConnectionBuilder;

		/**
		 * Object lock
		 */
		private final Object mLock;

		/**
		 * Counter
		 */
		private final CountDownLatch mCounter;

		/**
		 * Api handler
		 */
		private FlingApiHandler<R> mHandler;

		/**
		 * Result callback
		 */
		private ResultCallback<R> mResultCallback;

		/**
		 * Current result
		 */
		private volatile R mCurrentResult;

		/**
		 * Whether the result is consumed
		 */
		private volatile boolean mIsResultConsumed;

		/**
		 * Release flag
		 */
		private boolean mReleaseFlag;

		/**
		 * Whether it's interrupted
		 */
		private boolean mIsInterrupted;

		/**
		 * Release callback
		 */
		private FlingManagerImpl.ReleaseCallback mReleaseCallback;

		/**
		 * Internal used constructor
		 */
		protected MatchStickApiImpl() {
			this(null);
		}

		/**
		 * Create instance with the connection builder
		 *
		 * @param builder
		 *            connection builder
		 */
		protected MatchStickApiImpl(Api.ConnectionBuilder<A> builder) {
			this.mLock = new Object();
			this.mCounter = new CountDownLatch(1);
			this.mConnectionBuilder = builder;
		}

		/**
		 * Get related connection builder
		 */
		public final Api.ConnectionBuilder<A> getConnectionBuiler() {
			return this.mConnectionBuilder;
		}

		/**
		 * Release resource
		 */
		public void release() {
			release_dv();
			this.mReleaseFlag = true;
		}

		/**
		 * Get current flag
		 */
		public int getFlag() {
			return 0;
		}

		/**
		 * Execute connection api
		 */
		public final void execute_b(A connectionApi) throws DeadObjectException {
			this.mHandler = new FlingApiHandler<R>(connectionApi.getLooper());
			try {
				execute(connectionApi);
			} catch (DeadObjectException e) {
				notifyInternalError(e);
				throw e;
			} catch (RemoteException r) {
				notifyInternalError(r);
			}
		}

		/**
		 * Set callback function which will be called when release
		 */
		public void setReleaseCallback(
				FlingManagerImpl.ReleaseCallback callback) {
			this.mReleaseCallback = callback;
		}

		/**
		 * Called when result returned
		 */
		public void onResult(R result) {
			postResult(result);
		}

		/**
		 * Execute api
		 *
		 * @param connectionApi
		 * @throws RemoteException
		 */
		protected abstract void execute(A connectionApi) throws RemoteException;

		/**
		 * Create result
		 * 
		 * @param status
		 * @return
		 */
		protected abstract R createResult(Status status);

		/**
		 * Whether it's ready
		 *
		 * @return
		 */
		public final boolean isReady() {
			return (this.mCounter.getCount() == 0L);
		}

		/**
		 * Get current result
		 *
		 * @return current result
		 */
		private R getResult() {
			synchronized (this.mLock) {
				ValueChecker.checkTrueWithErrorMsg(!(this.mIsResultConsumed),
						"Result has already been consumed.");
				ValueChecker.checkTrueWithErrorMsg(isReady(),
						"Result is not ready.");
				consumeResult();
				return this.mCurrentResult;
			}
		}

		/**
		 * Block wait until be waked up by others
		 */
		public final R await() {
			ValueChecker.checkTrueWithErrorMsg(!(this.mIsResultConsumed),
					"Results has already been consumed");
			ValueChecker.checkTrueWithErrorMsg(
					(isReady())
							|| (Looper.myLooper() != Looper.getMainLooper()),
					"await must not be called on the UI thread");
			try {
				this.mCounter.await();
			} catch (InterruptedException localInterruptedException) {
				synchronized (this.mLock) {
					postResult(createResult(Status.InterruptedStatus));
					this.mIsInterrupted = true;
				}
			}
			ValueChecker.checkTrueWithErrorMsg(isReady(),
					"Result is not ready.");
			return getResult();
		}

		/**
		 * Wait until be waked up by others or timeout
		 */
		public final R await(long time, TimeUnit units) {
			ValueChecker.checkTrueWithErrorMsg(!(this.mIsResultConsumed),
					"Result has already been consumed.");
			ValueChecker.checkTrueWithErrorMsg(
					(isReady())
							|| (Looper.myLooper() != Looper.getMainLooper()),
					"await must not be called on the UI thread");
			try {
				boolean bool = this.mCounter.await(time, units);
				if (!(bool))
					synchronized (this.mLock) {
						postResult(createResult(Status.TimeOutStatus));
						this.mIsInterrupted = true;
					}
			} catch (InterruptedException localInterruptedException1) {
				synchronized (this.mLock) {
					postResult(createResult(Status.InterruptedStatus));
					this.mIsInterrupted = true;
				}
			}
			ValueChecker.checkTrueWithErrorMsg(isReady(),
					"Result is not ready.");
			return getResult();
		}

		/**
		 * Set result callback function
		 */
		public final void setResultCallback(ResultCallback<R> callback) {
			ValueChecker.checkTrueWithErrorMsg(!(this.mIsResultConsumed),
					"Result has already been consumed.");
			synchronized (this.mLock) {
				if (isReady())
					this.mHandler.notifyResultCallback(callback, getResult());
				else
					this.mResultCallback = callback;
			}
		}

		/**
		 * Set result callback function and call something later
		 *
		 * @time
		 * @units
		 */
		public final void setResultCallback(ResultCallback<R> callback,
				long time, TimeUnit units) {
			ValueChecker.checkTrueWithErrorMsg(!(this.mIsResultConsumed),
					"Result has already been consumed.");
			synchronized (this.mLock) {
				if (isReady()) {
					this.mHandler.notifyResultCallback(callback, getResult());
				} else {
					this.mResultCallback = callback;
					this.mHandler.setTimeoutTimer(this, units.toMillis(time));
				}
			}
		}

		/**
		 * Post Result
		 *
		 * @param result
		 */
		public final void postResult(R result) {
			synchronized (this.mLock) {
				if (this.mIsInterrupted) {
					if (result instanceof Releasable) {
						((Releasable) result).release();
					}
					return;
				}
				ValueChecker.checkTrueWithErrorMsg(!(isReady()),
						"Results have already been set");
				ValueChecker.checkTrueWithErrorMsg(!(this.mIsResultConsumed),
						"Result has already been consumed");
				this.mCurrentResult = result;
				if (this.mReleaseFlag) {
					release_dv();
					return;
				}
				this.mCounter.countDown();
				Status status = this.mCurrentResult.getStatus();
				if (this.mResultCallback != null) {
					this.mHandler.removeTimeoutMessage();
					this.mHandler.notifyResultCallback(this.mResultCallback,
							getResult());
				}
			}
		}

		/**
		 * Notify internal error
		 *
		 * @param exception
		 */
		private void notifyInternalError(RemoteException exception) {
			Status status = new Status(FlingStatusCodes.INTERNAL_ERROR,
					exception.getLocalizedMessage(), null);
			postResult(createResult(status));
		}

		/**
		 * Release result
		 */
		void consumeResult() {
			this.mIsResultConsumed = true;
			/*
			 * If zi set to null here, the
			 * callbacks(ApplicationConnectionResult) who are implemented by app
			 * cannot be called correctly. According to the code from smali
			 * files, zi is set to null indeed. I don't know why! Anyway,
			 * comment this line and demo app could run!
			 */
			// this.zi = null;
			if (this.mReleaseCallback == null) {
				return;
			}
			this.mReleaseCallback.onRelease(this);
		}

		/**
		 * Release resource
		 */
		private void release_dv() {
			if ((this.mCurrentResult == null)
					|| (!(this instanceof Releasable))) {
				return;
			}
			try {
				((Releasable) this).release();
			} catch (Exception localException) {
				Log.w("GoogleApi", "Unable to release " + this, localException);
			}
		}
	}

	/*
	 * Fling Api Handler
	 */
	public static class FlingApiHandler<R extends Result> extends Handler {
		public FlingApiHandler() {
			this(Looper.getMainLooper());
		}

		public FlingApiHandler(Looper paramLooper) {
			super(paramLooper);
		}

		/**
		 * Notify result callback
		 *
		 * @param callback
		 * @param result
		 */
		public void notifyResultCallback(ResultCallback<R> callback, R result) {
			sendMessage(obtainMessage(1, new Pair(callback, result)));
		}

		/**
		 * Set timeout message
		 * 
		 * @param obj
		 * @param delayMillis
		 *            delay time
		 */
		public void setTimeoutTimer(MatchStickApiImpl<R, ?> obj, long delayMillis) {
			sendMessageDelayed(obtainMessage(2, obj), delayMillis);
		}

		/**
		 * Remove timeout messages
		 */
		public void removeTimeoutMessage() {
			removeMessages(2);
		}

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // post result
				Pair localPair = (Pair) msg.obj;
				notifyCallback((ResultCallback<R>) localPair.first,
						(R) localPair.second);
				return;
			case 2: // time out
				MatchStickApiImpl flingApi = (MatchStickApiImpl) msg.obj;
				flingApi.postResult(flingApi
						.createResult(Status.TimeOutStatus));
				return;
			}
			Log.wtf("FlingApi", "Don't know how to handle this message.");
		}

		/**
		 * Notify callback function
		 * 
		 * @param paramResultCallback
		 * @param paramR
		 */
		protected void notifyCallback(ResultCallback<R> paramResultCallback,
				R paramR) {
			paramResultCallback.onResult(paramR);
		}
	}

	/**
	 * ResultCallback interface
	 */
	public interface ResultCallback_c<R> {
		public void onResult(R paramR);
	}

}
