package tv.matchstick.fling;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.client.internal.DoubleAndLongConverter;

/**
 * A class that holds status information about some media. 
 *
 * The current MediaStatus can be obtained from the RemoteMediaPlayer. 
 */
public final class MediaStatus {
	/**
	 * A flag (bitmask) indicating that a media item can be paused.
	 */
	public static final long COMMAND_PAUSE = 1L;

	/**
	 * A flag (bitmask) indicating that a media item supports seeking.
	 */
	public static final long COMMAND_SEEK = 2L;

	/**
	 * A flag (bitmask) indicating that a media item's audio volume can be changed.
	 */
	public static final long COMMAND_SET_VOLUME = 4L;

	/**
	 * A flag (bitmask) indicating that a media item's audio can be muted.
	 */
	public static final long COMMAND_TOGGLE_MUTE = 8L;

	/**
	 * A flag (bitmask) indicating that a media item supports skipping forward.
	 */
	public static final long COMMAND_SKIP_FORWARD = 16L;

	/**
	 * A flag (bitmask) indicating that a media item supports skipping backward.
	 */
	public static final long COMMAND_SKIP_BACKWARD = 32L;

	/**
	 * Constant indicating unknown player state.
	 */
	public static final int PLAYER_STATE_UNKNOWN = 0;

	/**
	 * Constant indicating that the media player is idle.
	 */
	public static final int PLAYER_STATE_IDLE = 1;

	/**
	 * Constant indicating that the media player is playing.
	 */
	public static final int PLAYER_STATE_PLAYING = 2;

	/**
	 * Constant indicating that the media player is paused.
	 */
	public static final int PLAYER_STATE_PAUSED = 3;

	/**
	 * Constant indicating that the media player is buffering.
	 */
	public static final int PLAYER_STATE_BUFFERING = 4;

	/**
	 * Constant indicating that the player currently has no idle reason.
	 */
	public static final int IDLE_REASON_NONE = 0;

	/**
	 * Constant indicating that the player is idle because playback has finished.
	 */
	public static final int IDLE_REASON_FINISHED = 1;

	/**
	 * Constant indicating that the player is idle because playback has been canceled in response to a STOP command.
	 */
	public static final int IDLE_REASON_CANCELED = 2;

	/**
	 * Constant indicating that the player is idle because playback has been interrupted by a LOAD command.
	 */
	public static final int IDLE_REASON_INTERRUPTED = 3;

	/**
	 * Constant indicating that the player is idle because a playback error has occurred.
	 */
	public static final int IDLE_REASON_ERROR = 4;

	/**
	 * Media Session Id
	 */
	private long mMediaSessionId;

	/**
	 * Media Information
	 */
	private MediaInfo mMediaInfo;

	/**
	 * Playback rate
	 */
	private double mPlaybackRate;

	/**
	 * Player state
	 */
	private int mPlayerState;

	/**
	 * Idle reason
	 */
	private int mIdleReason;

	/**
	 * Current time
	 */
	private long mCurrentTime;

	/**
	 * Supported Media commands
	 */
	private long mSupportedMediaCommands;

	/**
	 * Volume level
	 */
	private double mVolumeLevel;

	/**
	 * Device mute state
	 */
	private boolean mIsMuted;

	/**
	 * Custom data
	 */
	private JSONObject mCustomData;

	/**
	 * Create with Json object
	 *
	 * @param json
	 * @throws JSONException
	 */
	public MediaStatus(JSONObject json) throws JSONException {
		setMediaStatusWithJson(json, 0);
	}

	/**
	 * Get Media Session Id
	 *
	 * @return
	 */
	public long getMediaSessionId() {
		return this.mMediaSessionId;
	}

	/**
	 * Gets the current media player state.
	 *
	 * @return
	 */
	public int getPlayerState() {
		return this.mPlayerState;
	}

	/**
	 * Get the player state idle reason.
	 *
	 * @return
	 */
	public int getIdleReason() {
		return this.mIdleReason;
	}

	/**
	 * Get the current stream playback rate.
	 *
	 * @return
	 */
	public double getPlaybackRate() {
		return this.mPlaybackRate;
	}

	/**
	 * Get the MediaInfo for this item.
	 * 
	 * @return
	 */
	public MediaInfo getMediaInfo() {
		return this.mMediaInfo;
	}

	/**
	 * Get the current stream position, in milliseconds.
	 *
	 * @return
	 */
	public long getStreamPosition() {
		return this.mCurrentTime;
	}

	/**
	 * Check whether the stream supports a given control command.
	 *
	 * @param mediaCommand
	 * @return
	 */
	public boolean isMediaCommandSupported(long mediaCommand) {
		return ((this.mSupportedMediaCommands & mediaCommand) != 0L);
	}

	/**
	 * Get the stream's volume.
	 *
	 * @return
	 */
	public double getStreamVolume() {
		return this.mVolumeLevel;
	}

	/**
	 * Whether the stream's mute state.
	 *
	 * @return
	 */
	public boolean isMute() {
		return this.mIsMuted;
	}

	/**
	 * Get custom data in this media item
	 *
	 * @return any custom data that is associated with the media item.
	 */
	public JSONObject getCustomData() {
		return this.mCustomData;
	}

	/**
	 * Mask for update media session
	 */
	public static final int UPDATE_SESSION_MASK = 0x01;

	/**
	 * Mask for update media status
	 */
	public static final int UPDATE_MEDIA_STATUS_MASK = 0x02;

	/**
	 * Mask for update media meta data
	 */
	public static final int UPDATE_METADATA_MASK = 0x04;

	/**
	 * Set media Status
	 * 
	 * @param json
	 * @param updateMask
	 * @return updated status
	 * @throws JSONException
	 */
	public int setMediaStatusWithJson(JSONObject json, int updateMask)
			throws JSONException {
		int updated = 0;
		long sessionId = json.getLong("mediaSessionId");
		if (sessionId != mMediaSessionId) {
			mMediaSessionId = sessionId;
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
			if (playerState != mPlayerState) {
				mPlayerState = playerState;
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
				if (idleReason != mIdleReason) {
					mIdleReason = idleReason;
					updated |= UPDATE_MEDIA_STATUS_MASK;
				}
			}
		}
		if (json.has("playbackRate")) {
			double playbackRate = json.getDouble("playbackRate");
			if (mPlaybackRate != playbackRate) {
				mPlaybackRate = playbackRate;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
		}
		if ((json.has("currentTime")) && ((updateMask & 0x2) == 0)) {
			long currentTime = DoubleAndLongConverter.double2long(json
					.getDouble("currentTime"));
			if (currentTime != mCurrentTime) {
				this.mCurrentTime = currentTime;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
		}
		if (json.has("supportedMediaCommands")) {
			long supportedMediaCommands = json
					.getLong("supportedMediaCommands");
			if (supportedMediaCommands != mSupportedMediaCommands) {
				mSupportedMediaCommands = supportedMediaCommands;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
		}
		if ((json.has("volume")) && ((updateMask & 0x1) == 0)) {
			JSONObject volume = json.getJSONObject("volume");
			double level = volume.getDouble("level");
			if (level != this.mVolumeLevel) {
				this.mVolumeLevel = level;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
			boolean muted = volume.getBoolean("muted");
			if (muted != this.mIsMuted) {
				this.mIsMuted = muted;
				updated |= UPDATE_MEDIA_STATUS_MASK;
			}
		}
		if (json.has("customData")) {
			this.mCustomData = json.getJSONObject("customData");
			updated |= UPDATE_MEDIA_STATUS_MASK;
		}
		if (json.has("media")) {
			JSONObject media = json.getJSONObject("media");
			this.mMediaInfo = new MediaInfo(media);
			updated |= UPDATE_MEDIA_STATUS_MASK;
			if (media.has("metadata")) {
				updated |= UPDATE_METADATA_MASK;
			}
		}
		return updated;
	}

}
