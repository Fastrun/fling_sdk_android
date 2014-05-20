package com.fireflycast.cast;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.os.DeadObjectException;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.util.Pair;

import com.fireflycast.client.common.api.FireflyApiClientImpl_b;
import com.fireflycast.client.common.api.FireflyApiClientImpl_b.FireflyApiClientTask;
import com.fireflycast.client.common.api.Releasable;
import com.fireflycast.client.internal.ValueChecker_er;

public class FireflyApi_a {
	public static abstract class FireflyApiImpl_a<R extends Result, A extends Api.ConnectionApi_a>
			implements PendingResult<R>, ResultCallback_c<R>,
			FireflyApiClientTask<A> {
		private final Api.ConnectionBuilder_b<A> mConnectionBuilder_zc;
		private final Object mLock_zd;
		private final CountDownLatch mCounter_zf;

		private FireflyApiHandler_b<R> mHandler_ze;
		private ResultCallback<R> mResultCallback_zh;
		private volatile R mCurrentResult_zi;
		private volatile boolean mIsResultConsumed_zj;
		private boolean mReleaseFlag_zk;
		private boolean mIsInterrupted_zl;
		private FireflyApiClientImpl_b.ReleaseCallback_a mReleaseCallback_zm;

		protected FireflyApiImpl_a() {
			this(null);
		}

		protected FireflyApiImpl_a(Api.ConnectionBuilder_b<A> builder) {
			this.mLock_zd = new Object();
			this.mCounter_zf = new CountDownLatch(1);
			this.mConnectionBuilder_zc = builder;
		}

		public final Api.ConnectionBuilder_b<A> getConnectionBuiler_dp() {
			return this.mConnectionBuilder_zc;
		}

		public void release_du() {
			release_dv();
			this.mReleaseFlag_zk = true;
		}

		public int getFlag_dr() {
			return 0;
		}

		public final void execute_b(A connectionApi) throws DeadObjectException {
			this.mHandler_ze = new FireflyApiHandler_b<R>(
					connectionApi.getLooper());
			try {
				execute_a(connectionApi);
			} catch (DeadObjectException e) {
				notifyInternalError_a(e);
				throw e;
			} catch (RemoteException r) {
				notifyInternalError_a(r);
			}
		}

		public void setReleaseCallback_a(
				FireflyApiClientImpl_b.ReleaseCallback_a callback) {
			this.mReleaseCallback_zm = callback;
		}

		public void onResult_b(R result) {
			postResult_a(result);
		}

		protected abstract void execute_a(A connectionApi)
				throws RemoteException;

		protected abstract R createResult_d(Status paramStatus);

		public final boolean isReady() {
			return (this.mCounter_zf.getCount() == 0L);
		}

		private R getResult_ds() {
			synchronized (this.mLock_zd) {
				ValueChecker_er.checkTrueWithErrorMsg(
						!(this.mIsResultConsumed_zj),
						"Result has already been consumed.");
				ValueChecker_er.checkTrueWithErrorMsg(isReady(),
						"Result is not ready.");
				consumeResult_dt();
				return this.mCurrentResult_zi;
			}
		}

		public final R await() {
			ValueChecker_er.checkTrueWithErrorMsg(!(this.mIsResultConsumed_zj),
					"Results has already been consumed");
			ValueChecker_er.checkTrueWithErrorMsg(
					(isReady())
							|| (Looper.myLooper() != Looper.getMainLooper()),
					"await must not be called on the UI thread");
			try {
				this.mCounter_zf.await();
			} catch (InterruptedException localInterruptedException) {
				synchronized (this.mLock_zd) {
					postResult_a(createResult_d(Status.InterruptedStatus_zR));
					this.mIsInterrupted_zl = true;
				}
			}
			ValueChecker_er.checkTrueWithErrorMsg(isReady(),
					"Result is not ready.");
			return getResult_ds();
		}

		public final R await(long time, TimeUnit units) {
			ValueChecker_er.checkTrueWithErrorMsg(!(this.mIsResultConsumed_zj),
					"Result has already been consumed.");
			ValueChecker_er.checkTrueWithErrorMsg(
					(isReady())
							|| (Looper.myLooper() != Looper.getMainLooper()),
					"await must not be called on the UI thread");
			try {
				boolean bool = this.mCounter_zf.await(time, units);
				if (!(bool))
					synchronized (this.mLock_zd) {
						postResult_a(createResult_d(Status.TimeOutStatus_zS));
						this.mIsInterrupted_zl = true;
					}
			} catch (InterruptedException localInterruptedException1) {
				synchronized (this.mLock_zd) {
					postResult_a(createResult_d(Status.InterruptedStatus_zR));
					this.mIsInterrupted_zl = true;
				}
			}
			ValueChecker_er.checkTrueWithErrorMsg(isReady(),
					"Result is not ready.");
			return getResult_ds();
		}

		public final void setResultCallback(ResultCallback<R> callback) {
			ValueChecker_er.checkTrueWithErrorMsg(!(this.mIsResultConsumed_zj),
					"Result has already been consumed.");
			synchronized (this.mLock_zd) {
				if (isReady())
					this.mHandler_ze.notifyResultCallback_a(callback,
							getResult_ds());
				else
					this.mResultCallback_zh = callback;
			}
		}

		public final void setResultCallback(ResultCallback<R> callback,
				long time, TimeUnit units) {
			ValueChecker_er.checkTrueWithErrorMsg(!(this.mIsResultConsumed_zj),
					"Result has already been consumed.");
			synchronized (this.mLock_zd) {
				if (isReady()) {
					this.mHandler_ze.notifyResultCallback_a(callback,
							getResult_ds());
				} else {
					this.mResultCallback_zh = callback;
					this.mHandler_ze.setTimeoutTimer_a(this,
							units.toMillis(time));
				}
			}
		}

		public final void postResult_a(R result) {
			synchronized (this.mLock_zd) {
				if (this.mIsInterrupted_zl) {
					if (result instanceof Releasable) {
						((Releasable) result).release();
					}
					return;
				}
				ValueChecker_er.checkTrueWithErrorMsg(!(isReady()),
						"Results have already been set");
				ValueChecker_er.checkTrueWithErrorMsg(
						!(this.mIsResultConsumed_zj),
						"Result has already been consumed");
				this.mCurrentResult_zi = result;
				if (this.mReleaseFlag_zk) {
					release_dv();
					return;
				}
				this.mCounter_zf.countDown();
				Status status = this.mCurrentResult_zi.getStatus();
				if (this.mResultCallback_zh != null) {
					this.mHandler_ze.removeTimeoutMessage_dw();
					this.mHandler_ze.notifyResultCallback_a(
							this.mResultCallback_zh, getResult_ds());
				}
			}
		}

		private void notifyInternalError_a(RemoteException exception) {
			Status status = new Status(8, exception.getLocalizedMessage(), null);
			postResult_a(createResult_d(status));
		}

		void consumeResult_dt() {
			this.mIsResultConsumed_zj = true;
			/*
			 * If zi set to null here, the
			 * callbacks(ApplicationConnectionResult) who are implemented by app
			 * cannot be called correctly. According to the code from smali
			 * files, zi is set to null indeed. I don't know why! Anyway,
			 * comment this line and demo app could run!
			 */
			// this.zi = null;
			if (this.mReleaseCallback_zm == null) {
				return;
			}
			this.mReleaseCallback_zm.onRelease_b(this);
		}

		private void release_dv() {
			if ((this.mCurrentResult_zi == null)
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
	 * a$b.smali : OK
	 */
	public static class FireflyApiHandler_b<R extends Result> extends Handler {
		public FireflyApiHandler_b() {
			this(Looper.getMainLooper());
		}

		public FireflyApiHandler_b(Looper paramLooper) {
			super(paramLooper);
		}

		public void notifyResultCallback_a(ResultCallback<R> callback, R result) {
			sendMessage(obtainMessage(1, new Pair(callback, result)));
		}

		public void setTimeoutTimer_a(FireflyApiImpl_a<R, ?> parama,
				long paramLong) {
			sendMessageDelayed(obtainMessage(2, parama), paramLong);
		}

		public void removeTimeoutMessage_dw() {
			removeMessages(2);
		}

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: // post result
				Pair localPair = (Pair) msg.obj;
				notifyCallback_b((ResultCallback<R>) localPair.first,
						(R) localPair.second);
				return;
			case 2: // time out
				FireflyApiImpl_a fireflyApi = (FireflyApiImpl_a) msg.obj;
				fireflyApi.postResult_a(fireflyApi
						.createResult_d(Status.TimeOutStatus_zS));
				return;
			}
			Log.wtf("GoogleApi", "Don't know how to handle this message.");
		}

		protected void notifyCallback_b(ResultCallback<R> paramResultCallback,
				R paramR) {
			paramResultCallback.onResult(paramR);
		}
	}

	/*
	 * a$c.smali : OK
	 */
	public interface ResultCallback_c<R> {
		public void onResult_b(R paramR);
	}

}
