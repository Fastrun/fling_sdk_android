package tv.matchstick.client.internal;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.client.common.api.CommonStatusCodes;
import tv.matchstick.client.internal.DoubleAndLongConverter;
import tv.matchstick.client.internal.RequestTracker;
import tv.matchstick.client.internal.RequestTrackerCallback;
import tv.matchstick.fling.MediaInfo;
import tv.matchstick.fling.MediaStatus;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

public class MediaControlChannelImpl extends MediaControlChannel {

	private static final long yi = TimeUnit.HOURS.toMillis(24L);
	private static final long yj = TimeUnit.HOURS.toMillis(24L);
	private static final long yk = TimeUnit.HOURS.toMillis(24L);
	private static final long MillisPerSecond_yl = TimeUnit.SECONDS
			.toMillis(1L);
	private long mMediaStartTime;
	private MediaStatus mMediaStatus;
	private final Handler mHandler = new Handler(Looper.getMainLooper());
	private final RequestTracker mRequestTrackerLoad = new RequestTracker(
			yj);
	private final RequestTracker mRequestTrackerPause = new RequestTracker(
			yi);
	private final RequestTracker mRequestTrackerPlay = new RequestTracker(
			yi);
	private final RequestTracker mRequestTrackerStop = new RequestTracker(
			yi);
	private final RequestTracker mRequestTrackerSeed = new RequestTracker(
			yk);
	private final RequestTracker mRequestTrackerVolume = new RequestTracker(
			yi);
	private final RequestTracker mRequestTrackerMute = new RequestTracker(
			yi);
	private final RequestTracker mRequestTrackerRequestStatus = new RequestTracker(
			yi);
	private final Runnable mTrackerTask = new RequestTrackerTask();
	private boolean yx;

	public MediaControlChannelImpl() {
		super("urn:x-cast:com.google.cast.media", "MediaControlChannel");
		cleanInternal();
	}

	public long load(RequestTrackerCallback paramdx, MediaInfo mediaInfo, boolean autoplay,
			long currentTime, JSONObject customData) throws IOException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId();
		mRequestTrackerLoad.startTrack(requestId, paramdx);
		handlerTrackerTask(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "LOAD");
			json.put("media", mediaInfo.buildJson());
			json.put("autoplay", autoplay);
			json.put("currentTime",
					DoubleAndLongConverter.long2double(currentTime));
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage(json.toString(), requestId, null);
		return requestId;
	}

	public long pause(RequestTrackerCallback paramdx, JSONObject customData) throws IOException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId();
		mRequestTrackerPause.startTrack(requestId, paramdx);
		handlerTrackerTask(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "PAUSE");
			json.put("mediaSessionId", getMediaSessionId());
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage(json.toString(), requestId, null);
		return requestId;
	}

	public long stop(RequestTrackerCallback paramdx, JSONObject customData) throws IOException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId();
		mRequestTrackerStop.startTrack(requestId, paramdx);
		handlerTrackerTask(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "STOP");
			json.put("mediaSessionId", getMediaSessionId());
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage(json.toString(), requestId, null);
		return requestId;
	}

	public long play(RequestTrackerCallback paramdx, JSONObject customData)
			throws IOException, IllegalStateException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId();
		mRequestTrackerPlay.startTrack(requestId, paramdx);
		handlerTrackerTask(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "PLAY");
			json.put("mediaSessionId", getMediaSessionId());
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage(json.toString(), requestId, null);
		return requestId;
	}

	public long seek(RequestTrackerCallback paramdx, long currentTime, int resumeState,
			JSONObject customData) throws IOException,
			IllegalStateException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId();
		mRequestTrackerSeed.startTrack(requestId, paramdx);
		handlerTrackerTask(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "SEEK");
			json.put("mediaSessionId", getMediaSessionId());
			json.put("currentTime",
					DoubleAndLongConverter.long2double(currentTime));
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
		sendTextMessage(json.toString(), requestId, null);
		return requestId;
	}

	public long setStreamVolume(RequestTrackerCallback paramdx, double level,
			JSONObject customData) throws IOException,
			IllegalStateException, IllegalArgumentException {
		if ((Double.isInfinite(level)) || (Double.isNaN(level))) {
			throw new IllegalArgumentException("Volume cannot be "
					+ level);
		}
		JSONObject json = new JSONObject();
		long requestId = getRequestId();
		mRequestTrackerVolume.startTrack(requestId, paramdx);
		handlerTrackerTask(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "SET_VOLUME");
			json.put("mediaSessionId", getMediaSessionId());
			JSONObject volumeJson = new JSONObject();
			volumeJson.put("level", level);
			json.put("volume", volumeJson);
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException e) {
		}
		sendTextMessage(json.toString(), requestId, null);
		return requestId;
	}

	public long setStreamMute(RequestTrackerCallback paramdx, boolean muted, JSONObject customData)
			throws IOException, IllegalStateException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId();
		mRequestTrackerMute.startTrack(requestId, paramdx);
		handlerTrackerTask(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "SET_VOLUME");
			json.put("mediaSessionId", getMediaSessionId());
			JSONObject volumeJson = new JSONObject();
			volumeJson.put("muted", muted);
			json.put("volume", volumeJson);
			if (customData != null) {
				json.put("customData", customData);
			}
		} catch (JSONException localJSONException) {
		}
		sendTextMessage(json.toString(), requestId, null);
		return requestId;
	}

	public long requestStatus(RequestTrackerCallback paramdx) throws IOException {
		JSONObject json = new JSONObject();
		long requestId = getRequestId();
		mRequestTrackerRequestStatus.startTrack(requestId, paramdx);
		handlerTrackerTask(true);
		try {
			json.put("requestId", requestId);
			json.put("type", "GET_STATUS");
			if (mMediaStatus != null) {
				json.put("mediaSessionId",
						mMediaStatus.getMediaSessionId());
			}
		} catch (JSONException e) {
		}
		sendTextMessage(json.toString(), requestId, null);
		return requestId;
	}

	public long getApproximateStreamPosition() {
		MediaInfo mediaInfo = getMediaInfo();
		if (mediaInfo == null) {
			return 0L;
		}
		if (mMediaStartTime == 0L) {
			return 0L;
		}
		double playbackRate = mMediaStatus.getPlaybackRate();
		long streamPosition = mMediaStatus.getStreamPosition();
		int playState = mMediaStatus.getPlayerState();
		if ((playbackRate == 0.0D)
				|| (playState != MediaStatus.PLAYER_STATE_PLAYING)) {
			return streamPosition;
		}
		long playedTime = SystemClock.elapsedRealtime() - mMediaStartTime;
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
		return this.mMediaStatus;
	}

	public MediaInfo getMediaInfo() {
		return ((this.mMediaStatus == null) ? null : this.mMediaStatus
				.getMediaInfo());
	}

	public final void onMessageReceived(String message) {
		mLogUtil.logd("message received: %s", new Object[] { message });
		try {
			JSONObject jsonMessage = new JSONObject(message);
			String type = jsonMessage.getString("type");
			long requestId = jsonMessage.optLong("requestId", -1L);
			if (type.equals("MEDIA_STATUS")) {
				JSONArray status = jsonMessage.getJSONArray("status");
				if (status.length() > 0) {
					updateMediaStatus(requestId, status.getJSONObject(0));
				} else {
					this.mMediaStatus = null;
					onStatusUpdated();
					onMetadataUpdated();
					mRequestTrackerRequestStatus.trackRequest(requestId, 0);
				}
			} else if (type.equals("INVALID_PLAYER_STATE")) {
				this.mLogUtil.logw(
						"received unexpected error: Invalid Player State.",
						new Object[0]);
				JSONObject customData = jsonMessage.optJSONObject("customData");
				this.mRequestTrackerLoad.trackRequest(requestId, 1, customData);
				this.mRequestTrackerPause.trackRequest(requestId, 1, customData);
				this.mRequestTrackerPlay.trackRequest(requestId, 1, customData);
				this.mRequestTrackerStop.trackRequest(requestId, 1, customData);
				this.mRequestTrackerSeed.trackRequest(requestId, 1, customData);
				this.mRequestTrackerVolume.trackRequest(requestId, 1, customData);
				this.mRequestTrackerMute.trackRequest(requestId, 1, customData);
				this.mRequestTrackerRequestStatus
						.trackRequest(requestId, 1, customData);
			} else if (type.equals("LOAD_FAILED")) {
				JSONObject customData = jsonMessage.optJSONObject("customData");
				this.mRequestTrackerLoad.trackRequest(requestId, 1,
						(JSONObject) customData);
			} else if (type.equals("LOAD_CANCELLED")) {
				JSONObject customData = jsonMessage.optJSONObject("customData");
				this.mRequestTrackerLoad.trackRequest(requestId, 2,
						(JSONObject) customData);
			} else if (type.equals("INVALID_REQUEST")) {
				this.mLogUtil.logw(
						"received unexpected error: Invalid Request.",
						new Object[0]);
				JSONObject customData = jsonMessage.optJSONObject("customData");
				this.mRequestTrackerLoad.trackRequest(requestId, 1, customData);
				this.mRequestTrackerPause.trackRequest(requestId, 1, customData);
				this.mRequestTrackerPlay.trackRequest(requestId, 1, customData);
				this.mRequestTrackerStop.trackRequest(requestId, 1, customData);
				this.mRequestTrackerSeed.trackRequest(requestId, 1, customData);
				this.mRequestTrackerVolume.trackRequest(requestId, 1, customData);
				this.mRequestTrackerMute.trackRequest(requestId, 1, customData);
				this.mRequestTrackerRequestStatus
						.trackRequest(requestId, 1, customData);
			}
		} catch (JSONException e) {
			this.mLogUtil.logw("Message is malformed (%s); ignoring: %s",
					new Object[] { e.getMessage(), message });
		}
	}

	public void trackUnSuccess(long requestId, int statusCode) {
		this.mRequestTrackerLoad.trackRequest(requestId, statusCode);
		this.mRequestTrackerPause.trackRequest(requestId, statusCode);
		this.mRequestTrackerPlay.trackRequest(requestId, statusCode);
		this.mRequestTrackerStop.trackRequest(requestId, statusCode);
		this.mRequestTrackerSeed.trackRequest(requestId, statusCode);
		this.mRequestTrackerVolume.trackRequest(requestId, statusCode);
		this.mRequestTrackerMute.trackRequest(requestId, statusCode);
		this.mRequestTrackerRequestStatus.trackRequest(requestId, statusCode);
	}

	private void updateMediaStatus(long requestId, JSONObject json)
			throws JSONException {
		boolean bool = this.mRequestTrackerLoad.isCurrentRequestId(requestId);
		int i = ((this.mRequestTrackerSeed.isRequestIdAvailable()) && (!(this.mRequestTrackerSeed
				.isCurrentRequestId(requestId)))) ? 1 : 0;
		int j = (((this.mRequestTrackerVolume.isRequestIdAvailable()) && (!(this.mRequestTrackerVolume
				.isCurrentRequestId(requestId)))) || ((this.mRequestTrackerMute.isRequestIdAvailable()) && (!(this.mRequestTrackerMute
				.isCurrentRequestId(requestId))))) ? 1 : 0;
		int k = 0;
		if (i != 0) {
			k |= 2;
		}
		if (j != 0) {
			k |= 1;
		}
		int updateBits = 0;
		if ((bool) || (this.mMediaStatus == null)) {
			this.mMediaStatus = new MediaStatus(json);
			this.mMediaStartTime = SystemClock.elapsedRealtime();
			updateBits = 7;
		} else {
			updateBits = this.mMediaStatus.setMediaStatusWithJson(json, k);
		}
		if ((updateBits & MediaStatus.UPDATE_SESSION_MASK) != 0) {
			this.mMediaStartTime = SystemClock.elapsedRealtime();
			onStatusUpdated();
		}
		if ((updateBits & MediaStatus.UPDATE_MEDIA_STATUS_MASK) != 0) {
			this.mMediaStartTime = SystemClock.elapsedRealtime();
			onStatusUpdated();
		}
		if ((updateBits & MediaStatus.UPDATE_METADATA_MASK) != 0) {
			onMetadataUpdated();
		}
		this.mRequestTrackerLoad.trackRequest(requestId, 0);
		this.mRequestTrackerPause.trackRequest(requestId, 0);
		this.mRequestTrackerPlay.trackRequest(requestId, 0);
		this.mRequestTrackerStop.trackRequest(requestId, 0);
		this.mRequestTrackerSeed.trackRequest(requestId, 0);
		this.mRequestTrackerVolume.trackRequest(requestId, 0);
		this.mRequestTrackerMute.trackRequest(requestId, 0);
		this.mRequestTrackerRequestStatus.trackRequest(requestId, 0);
	}

	public long getMediaSessionId() throws IllegalStateException {
		if (mMediaStatus == null) {
			throw new IllegalStateException("No current media session");
		}
		return mMediaStatus.getMediaSessionId();
	}

	protected void onStatusUpdated() {
	}

	protected void onMetadataUpdated() {
	}

	private void cleanInternal() {
		handlerTrackerTask(false);
		mMediaStartTime = 0L;
		mMediaStatus = null;
		this.mRequestTrackerLoad.clear();
		this.mRequestTrackerSeed.clear();
		this.mRequestTrackerVolume.clear();
	}

	public void clean() {
		cleanInternal();
	}

	private void handlerTrackerTask(boolean postOrRemove) {
		if (this.yx == postOrRemove) {
			return;
		}
		this.yx = postOrRemove;
		if (postOrRemove) {
			mHandler.postDelayed(this.mTrackerTask, MillisPerSecond_yl);
		} else {
			mHandler.removeCallbacks(this.mTrackerTask);
		}
	}

	private class RequestTrackerTask implements Runnable {
		public void run() {
			yx = false;
			long timestamp = SystemClock.elapsedRealtime();
			mRequestTrackerLoad.trackRequestTimeout(timestamp, 3);
			mRequestTrackerPause.trackRequestTimeout(timestamp, 3);
			mRequestTrackerPlay.trackRequestTimeout(timestamp, 3);
			mRequestTrackerStop.trackRequestTimeout(timestamp, 3);
			mRequestTrackerSeed.trackRequestTimeout(timestamp, 3);
			mRequestTrackerVolume.trackRequestTimeout(timestamp, 3);
			mRequestTrackerMute.trackRequestTimeout(timestamp, 3);
			mRequestTrackerRequestStatus.trackRequestTimeout(timestamp, 3);
			boolean bool = false;
			synchronized (RequestTracker.mLock) {
				bool = (mRequestTrackerLoad.isRequestIdAvailable())
						|| (mRequestTrackerSeed.isRequestIdAvailable())
						|| (mRequestTrackerVolume.isRequestIdAvailable())
						|| (mRequestTrackerMute.isRequestIdAvailable())
						|| (mRequestTrackerRequestStatus.isRequestIdAvailable());
			}
			handlerTrackerTask(bool);
		}
	}

}
