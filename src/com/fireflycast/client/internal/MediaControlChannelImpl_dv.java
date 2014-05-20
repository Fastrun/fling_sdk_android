package com.fireflycast.client.internal;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.fireflycast.cast.MediaInfo;
import com.fireflycast.cast.MediaStatus;
import com.fireflycast.client.internal.RequestTracker_dy;
import com.fireflycast.client.internal.DoubleAndLongConverter_dr;
import com.fireflycast.client.internal.RequestTrackerCallback_dx;

public class MediaControlChannelImpl_dv extends MediaControlChannel_dp {

	private static final long yi = TimeUnit.HOURS.toMillis(24L);
	private static final long yj = TimeUnit.HOURS.toMillis(24L);
	private static final long yk = TimeUnit.HOURS.toMillis(24L);
	private static final long MillisPerSecond_yl = TimeUnit.SECONDS
			.toMillis(1L);
	private long mMediaStartTime_ym;
	private MediaStatus mMediaStatus_yn;
	private final Handler mHandler = new Handler(Looper.getMainLooper());
	private final RequestTracker_dy mRequestTrackerLoad_yo = new RequestTracker_dy(
			yj);
	private final RequestTracker_dy mRequestTrackerPause_yp = new RequestTracker_dy(
			yi);
	private final RequestTracker_dy mRequestTrackerPlay_yq = new RequestTracker_dy(
			yi);
	private final RequestTracker_dy mRequestTrackerStop_yr = new RequestTracker_dy(
			yi);
	private final RequestTracker_dy mRequestTrackerSeed_ys = new RequestTracker_dy(
			yk);
	private final RequestTracker_dy mRequestTrackerVolume_yt = new RequestTracker_dy(
			yi);
	private final RequestTracker_dy mRequestTrackerMute_yu = new RequestTracker_dy(
			yi);
	private final RequestTracker_dy mRequestTrackerRequestStatus_yv = new RequestTracker_dy(
			yi);
	private final Runnable mTrackerTask_yw = new RequestTrackerTask_a();
	private boolean yx;

	public MediaControlChannelImpl_dv() {
		super("urn:x-cast:com.google.cast.media", "MediaControlChannel");
		clean_dj();
	}

	public long load_a(RequestTrackerCallback_dx paramdx, MediaInfo mediaInfo, boolean autoplay,
			long currentTime, JSONObject customData) throws IOException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId_cW();
		mRequestTrackerLoad_yo.startTrack_a(requestId, paramdx);
		handlerTrackerTask_u(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "LOAD");
			json.put("media", mediaInfo.buildJson_cT());
			json.put("autoplay", autoplay);
			json.put("currentTime",
					DoubleAndLongConverter_dr.long2double_l(currentTime));
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage_a(json.toString(), requestId, null);
		return requestId;
	}

	public long pause_a(RequestTrackerCallback_dx paramdx, JSONObject customData) throws IOException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId_cW();
		mRequestTrackerPause_yp.startTrack_a(requestId, paramdx);
		handlerTrackerTask_u(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "PAUSE");
			json.put("mediaSessionId", getMediaSessionId_cU());
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage_a(json.toString(), requestId, null);
		return requestId;
	}

	public long stop_b(RequestTrackerCallback_dx paramdx, JSONObject customData) throws IOException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId_cW();
		mRequestTrackerStop_yr.startTrack_a(requestId, paramdx);
		handlerTrackerTask_u(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "STOP");
			json.put("mediaSessionId", getMediaSessionId_cU());
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage_a(json.toString(), requestId, null);
		return requestId;
	}

	public long play_c(RequestTrackerCallback_dx paramdx, JSONObject customData)
			throws IOException, IllegalStateException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId_cW();
		mRequestTrackerPlay_yq.startTrack_a(requestId, paramdx);
		handlerTrackerTask_u(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "PLAY");
			json.put("mediaSessionId", getMediaSessionId_cU());
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage_a(json.toString(), requestId, null);
		return requestId;
	}

	public long seed_a(RequestTrackerCallback_dx paramdx, long currentTime, int resumeState,
			JSONObject customData) throws IOException,
			IllegalStateException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId_cW();
		mRequestTrackerSeed_ys.startTrack_a(requestId, paramdx);
		handlerTrackerTask_u(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "SEEK");
			json.put("mediaSessionId", getMediaSessionId_cU());
			json.put("currentTime",
					DoubleAndLongConverter_dr.long2double_l(currentTime));
			if (resumeState == 1) {
				json.put("resumeState", "PLAYBACK_START");
			} else if (resumeState == 2) {
				json.put("resumeState", "PLAYBACK_PAUSE");
			}
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage_a(json.toString(), requestId, null);
		return requestId;
	}

	public long setStreamVolume_a(RequestTrackerCallback_dx paramdx, double level,
			JSONObject customData) throws IOException,
			IllegalStateException, IllegalArgumentException {
		if ((Double.isInfinite(level)) || (Double.isNaN(level))) {
			throw new IllegalArgumentException("Volume cannot be "
					+ level);
		}
		JSONObject json = new JSONObject();
		long requestId = getRequestId_cW();
		mRequestTrackerVolume_yt.startTrack_a(requestId, paramdx);
		handlerTrackerTask_u(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "SET_VOLUME");
			json.put("mediaSessionId", getMediaSessionId_cU());
			JSONObject volumeJson = new JSONObject();
			volumeJson.put("level", level);
			json.put("volume", volumeJson);
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage_a(json.toString(), requestId, null);
		return requestId;
	}

	public long setStreamMute_a(RequestTrackerCallback_dx paramdx, boolean muted, JSONObject customData)
			throws IOException, IllegalStateException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId_cW();
		mRequestTrackerMute_yu.startTrack_a(requestId, paramdx);
		handlerTrackerTask_u(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "SET_VOLUME");
			json.put("mediaSessionId", getMediaSessionId_cU());
			JSONObject volumeJson = new JSONObject();
			volumeJson.put("muted", muted);
			json.put("volume", volumeJson);
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException localJSONException) {
		}
		sendTextMessage_a(json.toString(), requestId, null);
		return requestId;
	}

	public long requestStatus_a(RequestTrackerCallback_dx paramdx) throws IOException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId_cW();
		mRequestTrackerRequestStatus_yv.startTrack_a(requestId, paramdx);
		handlerTrackerTask_u(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "GET_STATUS");
			if (mMediaStatus_yn != null) {
				json.put("mediaSessionId",
						mMediaStatus_yn.getMediaSessionId_cU());
			}
		} catch (JSONException e) {
		}
		sendTextMessage_a(json.toString(), requestId, null);
		return requestId;
	}

	public long getApproximateStreamPosition() {
		MediaInfo mediaInfo = getMediaInfo();
		if (mediaInfo == null) {
			return 0L;
		}
		if (mMediaStartTime_ym == 0L) {
			return 0L;
		}
		double playbackRate = mMediaStatus_yn.getPlaybackRate();
		long streamPosition = mMediaStatus_yn.getStreamPosition();
		int playState = mMediaStatus_yn.getPlayerState();
		if ((playbackRate == 0.0D)
				|| (playState != MediaStatus.PLAYER_STATE_PLAYING)) {
			return streamPosition;
		}
		long playedTime = SystemClock.elapsedRealtime() - mMediaStartTime_ym;
		if (playedTime < 0L) {
			playedTime = 0L;
		}
		if (playedTime == 0L) {
			return streamPosition;
		}

		long duration = mediaInfo.getStreamDuration();
		long position = (long) (streamPosition + (playedTime * playbackRate));
		if (position > duration) {
			position = duration;
		} else if (position < 0L) {
			position = 0L;
		}
		return position;
	}

	public long getStreamDuration() {
		MediaInfo mediaInfo = getMediaInfo();
		return ((mediaInfo != null) ? mediaInfo.getStreamDuration() : 0L);
	}

	public MediaStatus getMediaStatus() {
		return this.mMediaStatus_yn;
	}

	public MediaInfo getMediaInfo() {
		return ((this.mMediaStatus_yn == null) ? null : this.mMediaStatus_yn
				.getMediaInfo());
	}

	public final void onMessageReceived_P(String message) {
		mLogUtil_xB.logd_b("message received: %s", new Object[] { message });
		try {
			JSONObject jsonMessage = new JSONObject(message);
			String type = jsonMessage.getString("type");
			long requestId = jsonMessage.optLong("requestId", -1L);
			if (type.equals("MEDIA_STATUS")) {
				JSONArray status = jsonMessage.getJSONArray("status");
				if (status.length() > 0) {
					updateMediaStatus_a(requestId, status.getJSONObject(0));
				} else {
					this.mMediaStatus_yn = null;
					onStatusUpdated();
					onMetadataUpdated();
					mRequestTrackerRequestStatus_yv.trackRequest_c(requestId, 0);
				}
			} else if (type.equals("INVALID_PLAYER_STATE")) {
				this.mLogUtil_xB.logw_d(
						"received unexpected error: Invalid Player State.",
						new Object[0]);
				JSONObject customData = jsonMessage.optJSONObject("customData");
				this.mRequestTrackerLoad_yo.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerPause_yp.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerPlay_yq.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerStop_yr.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerSeed_ys.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerVolume_yt.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerMute_yu.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerRequestStatus_yv
						.trackRequest_b(requestId, 1, customData);
			} else if (type.equals("LOAD_FAILED")) {
				JSONObject customData = jsonMessage.optJSONObject("customData");
				this.mRequestTrackerLoad_yo.trackRequest_b(requestId, 1,
						(JSONObject) customData);
			} else if (type.equals("LOAD_CANCELLED")) {
				JSONObject customData = jsonMessage.optJSONObject("customData");
				this.mRequestTrackerLoad_yo.trackRequest_b(requestId, 2,
						(JSONObject) customData);
			} else if (type.equals("INVALID_REQUEST")) {
				this.mLogUtil_xB.logw_d(
						"received unexpected error: Invalid Request.",
						new Object[0]);
				JSONObject customData = jsonMessage.optJSONObject("customData");
				this.mRequestTrackerLoad_yo.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerPause_yp.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerPlay_yq.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerStop_yr.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerSeed_ys.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerVolume_yt.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerMute_yu.trackRequest_b(requestId, 1, customData);
				this.mRequestTrackerRequestStatus_yv
						.trackRequest_b(requestId, 1, customData);
			}
		} catch (JSONException e) {
			this.mLogUtil_xB.logw_d("Message is malformed (%s); ignoring: %s",
					new Object[] { e.getMessage(), message });
		}
	}

	public void trackUnSuccess_a(long requestId, int statusCode) {
		this.mRequestTrackerLoad_yo.trackRequest_c(requestId, statusCode);
		this.mRequestTrackerPause_yp.trackRequest_c(requestId, statusCode);
		this.mRequestTrackerPlay_yq.trackRequest_c(requestId, statusCode);
		this.mRequestTrackerStop_yr.trackRequest_c(requestId, statusCode);
		this.mRequestTrackerSeed_ys.trackRequest_c(requestId, statusCode);
		this.mRequestTrackerVolume_yt.trackRequest_c(requestId, statusCode);
		this.mRequestTrackerMute_yu.trackRequest_c(requestId, statusCode);
		this.mRequestTrackerRequestStatus_yv.trackRequest_c(requestId, statusCode);
	}

	private void updateMediaStatus_a(long requestId, JSONObject json)
			throws JSONException {
		boolean bool = this.mRequestTrackerLoad_yo.isCurrentRequestId_m(requestId);
		int i = ((this.mRequestTrackerSeed_ys.isRequestIdAvailable_dl()) && (!(this.mRequestTrackerSeed_ys
				.isCurrentRequestId_m(requestId)))) ? 1 : 0;
		int j = (((this.mRequestTrackerVolume_yt.isRequestIdAvailable_dl()) && (!(this.mRequestTrackerVolume_yt
				.isCurrentRequestId_m(requestId)))) || ((this.mRequestTrackerMute_yu.isRequestIdAvailable_dl()) && (!(this.mRequestTrackerMute_yu
				.isCurrentRequestId_m(requestId))))) ? 1 : 0;
		int k = 0;
		if (i != 0) {
			k |= 2;
		}
		if (j != 0) {
			k |= 1;
		}
		int updateBits = 0;
		if ((bool) || (this.mMediaStatus_yn == null)) {
			this.mMediaStatus_yn = new MediaStatus(json);
			this.mMediaStartTime_ym = SystemClock.elapsedRealtime();
			updateBits = 7;
		} else {
			updateBits = this.mMediaStatus_yn.setMediaStatusWithJson_a(json, k);
		}
		if ((updateBits & MediaStatus.UPDATE_SESSION_MASK) != 0) {
			this.mMediaStartTime_ym = SystemClock.elapsedRealtime();
			onStatusUpdated();
		}
		if ((updateBits & MediaStatus.UPDATE_MEDIA_STATUS_MASK) != 0) {
			this.mMediaStartTime_ym = SystemClock.elapsedRealtime();
			onStatusUpdated();
		}
		if ((updateBits & MediaStatus.UPDATE_METADATA_MASK) != 0) {
			onMetadataUpdated();
		}
		this.mRequestTrackerLoad_yo.trackRequest_c(requestId, 0);
		this.mRequestTrackerPause_yp.trackRequest_c(requestId, 0);
		this.mRequestTrackerPlay_yq.trackRequest_c(requestId, 0);
		this.mRequestTrackerStop_yr.trackRequest_c(requestId, 0);
		this.mRequestTrackerSeed_ys.trackRequest_c(requestId, 0);
		this.mRequestTrackerVolume_yt.trackRequest_c(requestId, 0);
		this.mRequestTrackerMute_yu.trackRequest_c(requestId, 0);
		this.mRequestTrackerRequestStatus_yv.trackRequest_c(requestId, 0);
	}

	public long getMediaSessionId_cU() throws IllegalStateException {
		if (mMediaStatus_yn == null) {
			throw new IllegalStateException("No current media session");
		}
		return mMediaStatus_yn.getMediaSessionId_cU();
	}

	protected void onStatusUpdated() {
	}

	protected void onMetadataUpdated() {
	}

	private void clean_dj() {
		handlerTrackerTask_u(false);
		mMediaStartTime_ym = 0L;
		mMediaStatus_yn = null;
		this.mRequestTrackerLoad_yo.clear();
		this.mRequestTrackerSeed_ys.clear();
		this.mRequestTrackerVolume_yt.clear();
	}

	public void clean_cX() {
		clean_dj();
	}

	private void handlerTrackerTask_u(boolean postOrRemove) {
		if (this.yx == postOrRemove) {
			return;
		}
		this.yx = postOrRemove;
		if (postOrRemove) {
			mHandler.postDelayed(this.mTrackerTask_yw, MillisPerSecond_yl);
		} else {
			mHandler.removeCallbacks(this.mTrackerTask_yw);
		}
	}

	private class RequestTrackerTask_a implements Runnable {
		public void run() {
			yx = false;
			long timestamp = SystemClock.elapsedRealtime();
			mRequestTrackerLoad_yo.trackRequestTimeout_d(timestamp, 3);
			mRequestTrackerPause_yp.trackRequestTimeout_d(timestamp, 3);
			mRequestTrackerPlay_yq.trackRequestTimeout_d(timestamp, 3);
			mRequestTrackerStop_yr.trackRequestTimeout_d(timestamp, 3);
			mRequestTrackerSeed_ys.trackRequestTimeout_d(timestamp, 3);
			mRequestTrackerVolume_yt.trackRequestTimeout_d(timestamp, 3);
			mRequestTrackerMute_yu.trackRequestTimeout_d(timestamp, 3);
			mRequestTrackerRequestStatus_yv.trackRequestTimeout_d(timestamp, 3);
			boolean bool = false;
			synchronized (RequestTracker_dy.mLock_yD) {
				bool = (mRequestTrackerLoad_yo.isRequestIdAvailable_dl())
						|| (mRequestTrackerSeed_ys.isRequestIdAvailable_dl())
						|| (mRequestTrackerVolume_yt.isRequestIdAvailable_dl())
						|| (mRequestTrackerMute_yu.isRequestIdAvailable_dl())
						|| (mRequestTrackerRequestStatus_yv.isRequestIdAvailable_dl());
			}
			handlerTrackerTask_u(bool);
		}
	}

}
