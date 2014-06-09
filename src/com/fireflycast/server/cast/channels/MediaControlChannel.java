
package com.fireflycast.server.cast.channels;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import com.fireflycast.server.cast.MediaInfo;
import com.fireflycast.server.cast.MediaStatus;
import com.fireflycast.server.utils.IStatusRequest;
import com.fireflycast.server.utils.Logs;
import com.fireflycast.server.utils.RequestTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

public class MediaControlChannel extends CastChannel {
    private static final long a;
    private static final long b;
    private static final long c;
    private static final long d;
    private long mElapsedTime_e;
    private MediaStatus mMediaStatus_f;
    private final Handler mHandler_g = new Handler(Looper.getMainLooper());
    private final RequestTracker mRequestTracker_h;
    private final RequestTracker mRequestTracker_i;
    private final RequestTracker mRequestTracker_l;
    private final RequestTracker mRequestTracker_m;
    private final RequestTracker mRequestTracker_n;

    // private final Runnable o = new C_avw(this, (byte) 0);
    private final Runnable mRequestTrackerRunnable_o = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            boolean flag;
            p = false; // MediaControlChannel_avv.a(a);
            long time = SystemClock.elapsedRealtime();
            mRequestTracker_h.checkTimeout_b(time, 3);
            mRequestTracker_i.checkTimeout_b(time, 3);
            mRequestTracker_l.checkTimeout_b(time, 3);
            mRequestTracker_m.checkTimeout_b(time, 3);
            mRequestTracker_n.checkTimeout_b(time, 3);
            synchronized (RequestTracker.mLock_a) {
                if (!mRequestTracker_h.isRunning_b() && !mRequestTracker_i.isRunning_b()
                        && !mRequestTracker_l.isRunning_b() && !mRequestTracker_m.isRunning_b()
                        && !mRequestTracker_n.isRunning_b())
                    flag = false;
                else
                    flag = true;

            }
            a(flag);
        }

    };

    private boolean p;

    public MediaControlChannel()
    {
        super("urn:x-cast:com.google.cast.media", "MediaControlChannel");
        mRequestTracker_h = new RequestTracker(b);
        mRequestTracker_i = new RequestTracker(c);
        mRequestTracker_l = new RequestTracker(a);
        mRequestTracker_m = new RequestTracker(a);
        mRequestTracker_n = new RequestTracker(a);
        j();
    }

    static void a(MediaControlChannel avv1, boolean flag)
    {
        avv1.a(flag);
    }

    private void a(boolean flag)
    {
        if (p == flag) {
            return;
        }

        p = flag;

        if (!flag) {
            mHandler_g.removeCallbacks(mRequestTrackerRunnable_o);
        } else {
            mHandler_g.postDelayed(mRequestTrackerRunnable_o, d);
        }
    }

    static boolean a(MediaControlChannel avv1)
    {
        avv1.p = false;
        return false;
    }

    static RequestTracker b(MediaControlChannel avv1)
    {
        return avv1.mRequestTracker_h;
    }

    static RequestTracker c(MediaControlChannel avv1)
    {
        return avv1.mRequestTracker_i;
    }

    static RequestTracker d(MediaControlChannel avv1)
    {
        return avv1.mRequestTracker_l;
    }

    static RequestTracker e(MediaControlChannel avv1)
    {
        return avv1.mRequestTracker_m;
    }

    static RequestTracker f(MediaControlChannel avv1)
    {
        return avv1.mRequestTracker_n;
    }

    private void j()
    {
        a(false);
        mElapsedTime_e = 0L;
        mMediaStatus_f = null;
        mRequestTracker_h.a();
        mRequestTracker_i.a();
        mRequestTracker_l.a();
    }

    public final long a()
    {
        MediaInfo atz1 = getMediaInfo_f();
        if (atz1 == null || mElapsedTime_e == 0L) {
            return 0L;
        }

        double d1 = mMediaStatus_f.mPlaybackRate_c;
        long l1 = mMediaStatus_f.mCurrentTime_f;
        int k = mMediaStatus_f.mPlayerState_d;
        if (d1 == 0.0D || k != 2) {
            return l1;
        }
        long l2 = SystemClock.elapsedRealtime() - mElapsedTime_e;
        long l3;
        long l4;
        long l5;
        if (l2 < 0L)
            l3 = 0L;
        else
            l3 = l2;
        if (l3 == 0L)
            return l1;
        l4 = atz1.mDuration_e;
        l5 = l1 + (long) (d1 * (double) l3);
        if (l5 <= l4)
            if (l5 < 0L)
                l4 = 0L;
            else
                l4 = l5;
        return l4;
    }

    public final long getStatus_a(IStatusRequest avy)
    {
        JSONObject jsonobject = new JSONObject();
        long l1 = getId_c();
        mRequestTracker_n.start_a(l1, avy);
        a(true);
        try
        {
            jsonobject.put("requestId", l1);
            jsonobject.put("type", "GET_STATUS");
            if (mMediaStatus_f != null)
                jsonobject.put("mediaSessionId", mMediaStatus_f.mMediaSessionId_a);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage_a(jsonobject.toString(), l1, null);

        return l1;
    }

    public final long seekTime_a(IStatusRequest avy, long currentTime, JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId;
        jsonobject1 = new JSONObject();
        requestId = getId_c();
        mRequestTracker_i.start_a(requestId, avy);
        a(true);
        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "SEEK");
            jsonobject1.put("mediaSessionId", getMediaSessionId_g());
            jsonobject1.put("currentTime", (double) currentTime / 1000D);
            if (customData != null)
                jsonobject1.put("customData", customData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage_a(jsonobject1.toString(), requestId, null);
        return requestId;
    }

    public final long load_a(IStatusRequest avy, MediaInfo atz1, long currentTime, JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId;
        jsonobject1 = new JSONObject();
        requestId = getId_c();
        mRequestTracker_h.start_a(requestId, avy);
        a(true);
        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "LOAD");
            jsonobject1.put("media", atz1.buildJsonObj_a());
            jsonobject1.put("autoplay", true);
            jsonobject1.put("currentTime", (double) currentTime / 1000D);
            if (customData != null)
                jsonobject1.put("customData", customData);
        } catch (JSONException e) {

        }
        sendMessage_a(jsonobject1.toString(), requestId, null);
        return requestId;
    }

    public final void pause_a(JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId;
        jsonobject1 = new JSONObject();
        requestId = getId_c();

        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "PAUSE");
            jsonobject1.put("mediaSessionId", getMediaSessionId_g());
            if (customData != null)
                jsonobject1.put("customData", customData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage_a(jsonobject1.toString(), requestId, null);
        return;
    }

    public final void checkReceivedMessage_a_(String message)
    {
        mLogs_j.d("message received: %s", new Object[] {
                message
        });
        JSONObject jsonobject;
        JSONObject jsonobject1;
        JSONObject jsonobject2;
        JSONObject jsonobject3;
        JSONObject jsonobject4;
        String type;
        long requestId;
        try {
            jsonobject = new JSONObject(message);
            type = jsonobject.getString("type");
            requestId = jsonobject.optLong("requestId", -1L);
            if (type.equals("MEDIA_STATUS")) {
                JSONArray jsonarray = jsonobject.getJSONArray("status");
                if (jsonarray.length() <= 0) {
                    mMediaStatus_f = null;
                    onStatusUpdated_h();
                    sendItemStatusUpdate_i();
                    mRequestTracker_n.onResult_a(requestId, 0);
                    return;
                }

                JSONObject jsonobject5;
                boolean flag;
                jsonobject5 = jsonarray.getJSONObject(0);
                flag = mRequestTracker_h.a(requestId);

                boolean flag1 = false;
                int k = 0;
                int i1 = 0;
                if (mRequestTracker_i.isRunning_b() && !mRequestTracker_i.a(requestId))
                    flag1 = true;
                else
                    flag1 = false;

                boolean flag2;
                if (mRequestTracker_l.isRunning_b() && !mRequestTracker_l.a(requestId)
                        || mRequestTracker_m.isRunning_b() && !mRequestTracker_m.a(requestId))
                    flag2 = true;
                else
                    flag2 = false;
                if (!flag1) {
                    k = 0;
                } else {
                    k = 2;
                }
                if (flag2)
                    k |= 1;

                if (flag) {
                    mMediaStatus_f = new MediaStatus(jsonobject5);
                    mElapsedTime_e = SystemClock.elapsedRealtime();
                    i1 = 7;
                } else {
                    if (mMediaStatus_f != null) {
                        i1 = mMediaStatus_f.init_a(jsonobject5, k);
                    }
                }

                if ((i1 & 1) != 0) {
                    mElapsedTime_e = SystemClock.elapsedRealtime();
                    onStatusUpdated_h();
                }

                if ((i1 & 2) == 0) {
                    mElapsedTime_e = SystemClock.elapsedRealtime();
                    onStatusUpdated_h();
                }

                if ((i1 & 4) != 0) {
                    sendItemStatusUpdate_i();
                }

                mRequestTracker_h.onResult_a(requestId, 0);
                mRequestTracker_i.onResult_a(requestId, 0);
                mRequestTracker_l.onResult_a(requestId, 0);
                mRequestTracker_m.onResult_a(requestId, 0);
                mRequestTracker_n.onResult_a(requestId, 0);
                return;
            }
            if (type.equals("INVALID_PLAYER_STATE"))
            {
                mLogs_j.w("received unexpected error: Invalid Player State.", new Object[0]);
                jsonobject4 = jsonobject.optJSONObject("customData");
                mRequestTracker_h.onResult_a(requestId, 1, jsonobject4);
                mRequestTracker_i.onResult_a(requestId, 1, jsonobject4);
                mRequestTracker_l.onResult_a(requestId, 1, jsonobject4);
                mRequestTracker_m.onResult_a(requestId, 1, jsonobject4);
                mRequestTracker_n.onResult_a(requestId, 1, jsonobject4);
                return;
            }
            if (type.equals("LOAD_FAILED"))
            {
                jsonobject3 = jsonobject.optJSONObject("customData");
                mRequestTracker_h.onResult_a(requestId, 1, jsonobject3);
                return;
            }
            if (type.equals("LOAD_CANCELLED"))
            {
                jsonobject2 = jsonobject.optJSONObject("customData");
                mRequestTracker_h.onResult_a(requestId, 2, jsonobject2);
                return;
            }
            if (type.equals("INVALID_REQUEST"))
            {
                mLogs_j.w("received unexpected error: Invalid Request.", new Object[0]);
                jsonobject1 = jsonobject.optJSONObject("customData");
                mRequestTracker_h.onResult_a(requestId, 1, jsonobject1);
                mRequestTracker_i.onResult_a(requestId, 1, jsonobject1);
                mRequestTracker_l.onResult_a(requestId, 1, jsonobject1);
                mRequestTracker_m.onResult_a(requestId, 1, jsonobject1);
                mRequestTracker_n.onResult_a(requestId, 1, jsonobject1);
                return;
            }
        } catch (JSONException e) {
            Logs avu1;
            Object aobj[];
            avu1 = mLogs_j;
            aobj = new Object[2];
            aobj[0] = e.getMessage();
            aobj[1] = message;
            avu1.w("Message is malformed (%s); ignoring: %s", aobj);
            return;
        }
    }

    public final long getContentDuration_b()
    {
        MediaInfo info = getMediaInfo_f();
        if (info != null)
            return info.mDuration_e;
        else
            return 0L;
    }

    public final void stop_b(JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId = getId_c();
        jsonobject1 = new JSONObject();

        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "STOP");
            jsonobject1.put("mediaSessionId", getMediaSessionId_g());
            if (customData != null)
                jsonobject1.put("customData", customData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage_a(jsonobject1.toString(), requestId, null);
        return;
    }

    public final void play_c(JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId = getId_c();
        jsonobject1 = new JSONObject();
        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "PLAY");
            jsonobject1.put("mediaSessionId", getMediaSessionId_g());
            if (customData != null) {
                jsonobject1.put("customData", customData);
            }
        } catch (JSONException jsonexception) {
        }
        sendMessage_a(jsonobject1.toString(), requestId, null);
        return;
    }

    public final void d()
    {
        j();
    }

    public final MediaStatus getMediaStatus_e()
    {
        return mMediaStatus_f;
    }

    public final MediaInfo getMediaInfo_f()
    {
        if (mMediaStatus_f == null)
            return null;
        else
            return mMediaStatus_f.mMedia_b;
    }

    public final long getMediaSessionId_g()
    {
        if (mMediaStatus_f == null)
            throw new IllegalStateException("No current media session");
        else
            return mMediaStatus_f.mMediaSessionId_a;
    }

    protected void onStatusUpdated_h()
    {
    }

    protected void sendItemStatusUpdate_i()
    {
    }

    static
    {
        a = TimeUnit.SECONDS.toMillis(3L);
        b = TimeUnit.HOURS.toMillis(24L);
        c = TimeUnit.SECONDS.toMillis(5L);
        d = TimeUnit.SECONDS.toMillis(1L);
    }
}
