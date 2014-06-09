package com.fireflycast.cast;

import org.json.JSONException;
import org.json.JSONObject;

import com.fireflycast.client.internal.DoubleAndLongConverter;

public final class MediaStatus {

	public static final long COMMAND_PAUSE = 1L;
	public static final long COMMAND_SEEK = 2L;
	public static final long COMMAND_SET_VOLUME = 4L;
	public static final long COMMAND_TOGGLE_MUTE = 8L;
	public static final long COMMAND_SKIP_FORWARD = 16L;
	public static final long COMMAND_SKIP_BACKWARD = 32L;
	public static final int PLAYER_STATE_UNKNOWN = 0;
	public static final int PLAYER_STATE_IDLE = 1;
	public static final int PLAYER_STATE_PLAYING = 2;
	public static final int PLAYER_STATE_PAUSED = 3;
	public static final int PLAYER_STATE_BUFFERING = 4;
	public static final int IDLE_REASON_NONE = 0;
	public static final int IDLE_REASON_FINISHED = 1;
	public static final int IDLE_REASON_CANCELED = 2;
	public static final int IDLE_REASON_INTERRUPTED = 3;
	public static final int IDLE_REASON_ERROR = 4;

	private long mMediaSessionId_wY;
	private MediaInfo mMediaInfo_wQ;
	private double mPlaybackRate_wZ;
	private int mPlayerState_xa;
	private int mIdleReason_xb;
	private long mCurrentTime_xc;
	private long mSupportedMediaCommands_xd;
	private double mVolumeLevel_xe;
	private boolean mIsMuted_xf;
	private JSONObject mCustomData_wP;

	public MediaStatus(JSONObject json) throws JSONException {
		setMediaStatusWithJson_a(json, 0);
	}

	public long getMediaSessionId_cU() {
		return this.mMediaSessionId_wY;
	}

	public int getPlayerState() {
		return this.mPlayerState_xa;
	}

	public int getIdleReason() {
		return this.mIdleReason_xb;
	}

	public double getPlaybackRate() {
		return this.mPlaybackRate_wZ;
	}

	public MediaInfo getMediaInfo() {
		return this.mMediaInfo_wQ;
	}

	public long getStreamPosition() {
		return this.mCurrentTime_xc;
	}

	public boolean isMediaCommandSupported(long mediaCommand) {
		return ((this.mSupportedMediaCommands_xd & mediaCommand) != 0L);
	}

	public double getStreamVolume() {
		return this.mVolumeLevel_xe;
	}

	public boolean isMute() {
		return this.mIsMuted_xf;
	}

	public JSONObject getCustomData() {
		return this.mCustomData_wP;
	}

	/*
	 * return value: update flag i: first bit: update mediaSessionId seconde
	 * bit: update media status third bit: update metadata
	 */
	public static final int UPDATE_SESSION_MASK = 0x01;
	public static final int UPDATE_MEDIA_STATUS_MASK = 0x02;
	public static final int UPDATE_METADATA_MASK = 0x04;

	public int setMediaStatusWithJson_a(JSONObject json, int updateMask)
			throws JSONException {
		int updated = 0;
		long sessionId = json.getLong("mediaSessionId");
		if (sessionId != mMediaSessionId_wY) {
			mMediaSessionId_wY = sessionId;
			updated |= UPDATE_SESSION_MASK;
		}
		if (json.has("playerState")) {
			int playerState = PLAYER_STATE_UNKNOWN;
			String str = json.getString("playerState");
			if (str.equals("IDLE")) {
				playerState = PLAYER_STATE_IDLE;
			} else if (str.equals("PLAYING")) {
				playerState = PLAYER_STATE_PLAYING;
			} else if (str.equals("PAUSED")) {
				playerState = PLAYER_STATE_PAUSED;
			} else if (str.equals("BUFFERING")) {
				playerState = PLAYER_STATE_BUFFERING;
			}
			if (playerState != mPlayerState_xa) {
				mPlayerState_xa = playerState;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
			if ((playerState == PLAYER_STATE_IDLE) && (json.has("idleReason"))) {
				int idleReason = IDLE_REASON_NONE;
				str = json.getString("idleReason");
				if (str.equals("CANCELLED")) {
					idleReason = IDLE_REASON_CANCELED;
				} else if (str.equals("INTERRUPTED")) {
					idleReason = IDLE_REASON_INTERRUPTED;
				} else if (str.equals("FINISHED")) {
					idleReason = IDLE_REASON_FINISHED;
				} else if (str.equals("ERROR")) {
					idleReason = IDLE_REASON_ERROR;
				}
				if (idleReason != mIdleReason_xb) {
					mIdleReason_xb = idleReason;
					updated |= UPDATE_MEDIA_STATUS_MASK;
				}
			}
		}
		if (json.has("playbackRate")) {
			double playbackRate = json.getDouble("playbackRate");
			if (mPlaybackRate_wZ != playbackRate) {
				mPlaybackRate_wZ = playbackRate;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
		}
		if ((json.has("currentTime")) && ((updateMask & 0x2) == 0)) {
			long currentTime = DoubleAndLongConverter.double2long_b(json
					.getDouble("currentTime"));
			if (currentTime != mCurrentTime_xc) {
				this.mCurrentTime_xc = currentTime;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
		}
		if (json.has("supportedMediaCommands")) {
			long supportedMediaCommands = json
					.getLong("supportedMediaCommands");
			if (supportedMediaCommands != mSupportedMediaCommands_xd) {
				mSupportedMediaCommands_xd = supportedMediaCommands;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
		}
		if ((json.has("volume")) && ((updateMask & 0x1) == 0)) {
			JSONObject volume = json.getJSONObject("volume");
			double level = volume.getDouble("level");
			if (level != this.mVolumeLevel_xe) {
				this.mVolumeLevel_xe = level;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
			boolean muted = volume.getBoolean("muted");
			if (muted != this.mIsMuted_xf) {
				this.mIsMuted_xf = muted;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
		}
		if (json.has("customData")) {
			this.mCustomData_wP = json.getJSONObject("customData");
			updated |= UPDATE_MEDIA_STATUS_MASK;
		}
		if (json.has("media")) {
			JSONObject media = json.getJSONObject("media");
			this.mMediaInfo_wQ = new MediaInfo(media);
			updated |= UPDATE_MEDIA_STATUS_MASK;
			if (media.has("metadata")) {
				updated |= UPDATE_METADATA_MASK;
			}
		}
		return updated;
	}

}
