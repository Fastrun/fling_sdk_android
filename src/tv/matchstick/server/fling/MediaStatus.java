
package tv.matchstick.server.fling;

import org.json.JSONException;
import org.json.JSONObject;

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

    public long mMediaSessionId;
    public MediaInfo mMedia;
    public double mPlaybackRate;
    public int mPlayerState;
    public int mIdleReason;
    public long mCurrentTime;
    public JSONObject mCustomData;
    private long mSupportedMediaCommands;
    private double mLevel;
    private boolean mMuted;

    public MediaStatus(JSONObject jsonobject) throws JSONException {
        init(jsonobject, 0);
    }

    public final int init(JSONObject jsonobject, int k) throws JSONException
    {
        int i1 = 0;

        int l = 2;
        long mediaSessionId = jsonobject.getLong("mediaSessionId");

        if (mediaSessionId != mMediaSessionId) {
            mMediaSessionId = mediaSessionId;
            i1 = 1;
        } else {
            i1 = 0;
        }
        if (jsonobject.has("playerState")) {
            String playerState = jsonobject.getString("playerState");
            int j1;
            if (playerState.equals("IDLE"))
                j1 = 1;
            else if (playerState.equals("PLAYING"))
                j1 = l;
            else if (playerState.equals("PAUSED"))
                j1 = 3;
            else if (playerState.equals("BUFFERING"))
                j1 = 4;
            else
                j1 = 0;
            if (j1 != mPlayerState) {
                mPlayerState = j1;
                i1 |= 2;
            }
            if (j1 == 1 && jsonobject.has("idleReason")) {
                String idleReason = jsonobject.getString("idleReason");

                if (!idleReason.equals("CANCELLED"))
                    if (idleReason.equals("INTERRUPTED"))
                        l = 3;
                    else if (idleReason.equals("FINISHED"))
                        l = 1;
                    else if (idleReason.equals("ERROR"))
                        l = 4;
                    else
                        l = 0;
                if (l != mIdleReason) {
                    mIdleReason = l;
                    i1 |= 2;
                }
            }
        }

        if (jsonobject.has("playbackRate")) {
            double playbackRate = jsonobject.getDouble("playbackRate");
            if (mPlaybackRate != playbackRate) {
                mPlaybackRate = playbackRate;
                i1 |= 2;
            }
        }
        if (jsonobject.has("currentTime") && (k & 2) == 0) {
            long currentTime = (long) (1000D * jsonobject
                    .getDouble("currentTime"));
            if (currentTime != mCurrentTime) {
                mCurrentTime = currentTime;
                i1 |= 2;
            }
        }
        if (jsonobject.has("supportedMediaCommands")) {
            long supportedMediaCommands = jsonobject
                    .getLong("supportedMediaCommands");
            if (supportedMediaCommands != mSupportedMediaCommands) {
                mSupportedMediaCommands = supportedMediaCommands;
                i1 |= 2;
            }
        }
        if (jsonobject.has("volume") && (k & 1) == 0) {
            JSONObject jsonobject2 = jsonobject.getJSONObject("volume");
            double d1 = jsonobject2.getDouble("level");
            if (d1 != mLevel) {
                mLevel = d1;
                i1 |= 2;
            }
            boolean muted = jsonobject2.getBoolean("muted");
            if (muted != mMuted) {
                mMuted = muted;
                i1 |= 2;
            }
        }
        if (jsonobject.has("customData")) {
            mCustomData = jsonobject.getJSONObject("customData");
            i1 |= 2;
        }
        if (jsonobject.has("media")) {
            JSONObject jsonobject1 = jsonobject.getJSONObject("media");
            mMedia = new MediaInfo(jsonobject1);
            i1 |= 2;
            if (jsonobject1.has("metadata"))
                i1 |= 4;
        }

        return i1;
    }
}
