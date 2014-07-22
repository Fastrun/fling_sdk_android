package tv.matchstick.client.internal;

import org.json.JSONObject;

import android.os.SystemClock;

public final class RequestTracker {

	private long mTimeout;
	private long mRequestId;
	private long mCurrentTime;
	private RequestTrackerCallback mCallback;
	private static final LogUtil mLogUtil = new LogUtil("RequestTracker");
	public static final Object mLock = new Object();

	public RequestTracker(long timeOut) {
		this.mTimeout = timeOut;
		this.mRequestId = -1L;
		this.mCurrentTime = 0L;
	}

	public void startTrack(long requestId, RequestTrackerCallback paramdx) {
		RequestTrackerCallback callback = null;
		long l = -1L;
		synchronized (mLock) {
			callback = this.mCallback;
			l = this.mRequestId;
			this.mRequestId = requestId;
			this.mCallback = paramdx;
			this.mCurrentTime = SystemClock.elapsedRealtime();
		}
		if (callback == null) {
			return;
		}
		callback.onSignInRequired(l);
	}

	public void clear() {
		synchronized (mLock) {
			if (this.mRequestId != -1L)
				doClear();
		}
	}

	private void doClear() {
		this.mRequestId = -1L;
		this.mCallback = null;
		this.mCurrentTime = 0L;
	}

	public boolean isRequestIdAvailable() {
		synchronized (mLock) {
			return (this.mRequestId != -1L);
		}
	}

	public boolean isCurrentRequestId(long requestId) {
		synchronized (mLock) {
			return ((this.mRequestId != -1L) && (this.mRequestId == requestId));
		}
	}

	public boolean trackRequest(long reqeustId, int statusCode) {
		return trackRequest(reqeustId, statusCode, null);
	}

	public boolean trackRequest(long requestId, int statusCode,
			JSONObject customData) {
		boolean i = false;
		RequestTrackerCallback callback = null;
		synchronized (mLock) {
			if ((this.mRequestId != -1L)
					&& (this.mRequestId == requestId)) {
				mLogUtil.logd("request %d completed",
						new Object[] { Long.valueOf(this.mRequestId) });
				callback = this.mCallback;
				doClear();
				i = true;
			}
		}
		if (callback != null) {
			callback.onTrackRequest(requestId, statusCode, customData);
		}
		return i;
	}

	public boolean trackRequestTimeout(long timeOut, int statusCode) {
		boolean i = false;
		RequestTrackerCallback callback = null;
		long requestId = 0L;
		synchronized (mLock) {
			if ((this.mRequestId != -1L)
					&& (timeOut - this.mCurrentTime >= this.mTimeout)) {
				mLogUtil.logd("request %d timed out",
						new Object[] { Long.valueOf(this.mRequestId) });
				requestId = this.mRequestId;
				callback = this.mCallback;
				doClear();
				i = true;
			}
		}
		if (callback != null) {
			callback.onTrackRequest(requestId, statusCode, null);
		}
		return i;
	}

}
