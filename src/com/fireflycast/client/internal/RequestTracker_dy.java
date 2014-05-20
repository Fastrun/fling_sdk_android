package com.fireflycast.client.internal;

import org.json.JSONObject;

import android.os.SystemClock;

public final class RequestTracker_dy {

	private long mTimeout_yz;
	private long mRequestId_yA;
	private long mCurrentTime_yB;
	private RequestTrackerCallback_dx mCallback_yC;
	private static final LogUtil_du mLogUtil_xE = new LogUtil_du("RequestTracker");
	public static final Object mLock_yD = new Object();

	public RequestTracker_dy(long timeOut) {
		this.mTimeout_yz = timeOut;
		this.mRequestId_yA = -1L;
		this.mCurrentTime_yB = 0L;
	}

	public void startTrack_a(long requestId, RequestTrackerCallback_dx paramdx) {
		RequestTrackerCallback_dx callback = null;
		long l = -1L;
		synchronized (mLock_yD) {
			callback = this.mCallback_yC;
			l = this.mRequestId_yA;
			this.mRequestId_yA = requestId;
			this.mCallback_yC = paramdx;
			this.mCurrentTime_yB = SystemClock.elapsedRealtime();
		}
		if (callback == null) {
			return;
		}
		callback.onSignInRequired_k(l);
	}

	public void clear() {
		synchronized (mLock_yD) {
			if (this.mRequestId_yA != -1L)
				doClear_dk();
		}
	}

	private void doClear_dk() {
		this.mRequestId_yA = -1L;
		this.mCallback_yC = null;
		this.mCurrentTime_yB = 0L;
	}

	public boolean isRequestIdAvailable_dl() {
		synchronized (mLock_yD) {
			return (this.mRequestId_yA != -1L);
		}
	}

	public boolean isCurrentRequestId_m(long requestId) {
		synchronized (mLock_yD) {
			return ((this.mRequestId_yA != -1L) && (this.mRequestId_yA == requestId));
		}
	}

	public boolean trackRequest_c(long reqeustId, int statusCode) {
		return trackRequest_b(reqeustId, statusCode, null);
	}

	public boolean trackRequest_b(long requestId, int statusCode,
			JSONObject customData) {
		boolean i = false;
		RequestTrackerCallback_dx callback = null;
		synchronized (mLock_yD) {
			if ((this.mRequestId_yA != -1L)
					&& (this.mRequestId_yA == requestId)) {
				mLogUtil_xE.logd_b("request %d completed",
						new Object[] { Long.valueOf(this.mRequestId_yA) });
				callback = this.mCallback_yC;
				doClear_dk();
				i = true;
			}
		}
		if (callback != null) {
			callback.onTrackRequest_a(requestId, statusCode, customData);
		}
		return i;
	}

	public boolean trackRequestTimeout_d(long timeOut, int statusCode) {
		boolean i = false;
		RequestTrackerCallback_dx callback = null;
		long requestId = 0L;
		synchronized (mLock_yD) {
			if ((this.mRequestId_yA != -1L)
					&& (timeOut - this.mCurrentTime_yB >= this.mTimeout_yz)) {
				mLogUtil_xE.logd_b("request %d timed out",
						new Object[] { Long.valueOf(this.mRequestId_yA) });
				requestId = this.mRequestId_yA;
				callback = this.mCallback_yC;
				doClear_dk();
				i = true;
			}
		}
		if (callback != null) {
			callback.onTrackRequest_a(requestId, statusCode, null);
		}
		return i;
	}

}
