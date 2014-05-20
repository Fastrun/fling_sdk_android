
package com.fireflycast.server.cast;

import org.json.JSONException;
import org.json.JSONObject;

public final class MediaStatus_aud {
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

    public long mMediaSessionId_a;
    public MediaInfo_atz mMedia_b;
    public double mPlaybackRate_c;
    public int mPlayerState_d;
    public int mIdleReason_e;
    public long mCurrentTime_f;
    public JSONObject mCustomData_g;
    private long mSupportedMediaCommands_h;
    private double mLevel_i;
    private boolean mMuted_j;

    public MediaStatus_aud(JSONObject jsonobject) throws JSONException {
        init_a(jsonobject, 0);
    }

    public final int init_a(JSONObject jsonobject, int k) throws JSONException
    {
        int i1 = 0;

        int l = 2;
        long mediaSessionId = jsonobject.getLong("mediaSessionId");

        if (mediaSessionId != mMediaSessionId_a) {
            mMediaSessionId_a = mediaSessionId;
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
            if (j1 != mPlayerState_d) {
                mPlayerState_d = j1;
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
                if (l != mIdleReason_e) {
                    mIdleReason_e = l;
                    i1 |= 2;
                }
            }
        }

        if (jsonobject.has("playbackRate")) {
            double playbackRate = jsonobject.getDouble("playbackRate");
            if (mPlaybackRate_c != playbackRate) {
                mPlaybackRate_c = playbackRate;
                i1 |= 2;
            }
        }
        if (jsonobject.has("currentTime") && (k & 2) == 0) {
            long currentTime = (long) (1000D * jsonobject
                    .getDouble("currentTime"));
            if (currentTime != mCurrentTime_f) {
                mCurrentTime_f = currentTime;
                i1 |= 2;
            }
        }
        if (jsonobject.has("supportedMediaCommands")) {
            long supportedMediaCommands = jsonobject
                    .getLong("supportedMediaCommands");
            if (supportedMediaCommands != mSupportedMediaCommands_h) {
                mSupportedMediaCommands_h = supportedMediaCommands;
                i1 |= 2;
            }
        }
        if (jsonobject.has("volume") && (k & 1) == 0) {
            JSONObject jsonobject2 = jsonobject.getJSONObject("volume");
            double d1 = jsonobject2.getDouble("level");
            if (d1 != mLevel_i) {
                mLevel_i = d1;
                i1 |= 2;
            }
            boolean muted = jsonobject2.getBoolean("muted");
            if (muted != mMuted_j) {
                mMuted_j = muted;
                i1 |= 2;
            }
        }
        if (jsonobject.has("customData")) {
            mCustomData_g = jsonobject.getJSONObject("customData");
            i1 |= 2;
        }
        if (jsonobject.has("media")) {
            JSONObject jsonobject1 = jsonobject.getJSONObject("media");
            mMedia_b = new MediaInfo_atz(jsonobject1);
            i1 |= 2;
            if (jsonobject1.has("metadata"))
                i1 |= 4;
        }

        return i1;
    }
}
