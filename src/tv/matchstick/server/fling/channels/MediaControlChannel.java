
package tv.matchstick.server.fling.channels;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.server.fling.MediaInfo;
import tv.matchstick.server.fling.MediaStatus;
import tv.matchstick.server.utils.IStatusRequest;
import tv.matchstick.server.utils.LOG;
import tv.matchstick.server.utils.RequestTracker;

import java.util.concurrent.TimeUnit;

public class MediaControlChannel extends FlingChannel {
    private static final long a;
    private static final long b;
    private static final long c;
    private static final long d;
    private long mElapsedTime;
    private MediaStatus mMediaStatus;
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final RequestTracker mRequestTracker_h;
    private final RequestTracker mRequestTracker_i;
    private final RequestTracker mRequestTracker_l;
    private final RequestTracker mRequestTracker_m;
    private final RequestTracker mRequestTracker_n;

    private final Runnable mRequestTrackerRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            boolean flag;
            p = false; // MediaControlChannel_avv.a(a);
            long time = SystemClock.elapsedRealtime();
            mRequestTracker_h.checkTimeout(time, 3);
            mRequestTracker_i.checkTimeout(time, 3);
            mRequestTracker_l.checkTimeout(time, 3);
            mRequestTracker_m.checkTimeout(time, 3);
            mRequestTracker_n.checkTimeout(time, 3);
            synchronized (RequestTracker.mLock_a) {
                if (!mRequestTracker_h.isRunning() && !mRequestTracker_i.isRunning()
                        && !mRequestTracker_l.isRunning() && !mRequestTracker_m.isRunning()
                        && !mRequestTracker_n.isRunning())
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
            mHandler.removeCallbacks(mRequestTrackerRunnable);
        } else {
            mHandler.postDelayed(mRequestTrackerRunnable, d);
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
        mElapsedTime = 0L;
        mMediaStatus = null;
        mRequestTracker_h.a();
        mRequestTracker_i.a();
        mRequestTracker_l.a();
    }

    public final long a()
    {
        MediaInfo atz1 = getMediaInfo();
        if (atz1 == null || mElapsedTime == 0L) {
            return 0L;
        }

        double d1 = mMediaStatus.mPlaybackRate;
        long l1 = mMediaStatus.mCurrentTime;
        int k = mMediaStatus.mPlayerState;
        if (d1 == 0.0D || k != 2) {
            return l1;
        }
        long l2 = SystemClock.elapsedRealtime() - mElapsedTime;
        long l3;
        long l4;
        long l5;
        if (l2 < 0L)
            l3 = 0L;
        else
            l3 = l2;
        if (l3 == 0L)
            return l1;
        l4 = atz1.mDuration;
        l5 = l1 + (long) (d1 * (double) l3);
        if (l5 <= l4)
            if (l5 < 0L)
                l4 = 0L;
            else
                l4 = l5;
        return l4;
    }

    public final long getStatus(IStatusRequest avy)
    {
        JSONObject jsonobject = new JSONObject();
        long l1 = getId();
        mRequestTracker_n.start(l1, avy);
        a(true);
        try
        {
            jsonobject.put("requestId", l1);
            jsonobject.put("type", "GET_STATUS");
            if (mMediaStatus != null)
                jsonobject.put("mediaSessionId", mMediaStatus.mMediaSessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage(jsonobject.toString(), l1, null);

        return l1;
    }

    public final long seekTime(IStatusRequest avy, long currentTime, JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId;
        jsonobject1 = new JSONObject();
        requestId = getId();
        mRequestTracker_i.start(requestId, avy);
        a(true);
        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "SEEK");
            jsonobject1.put("mediaSessionId", getMediaSessionId());
            jsonobject1.put("currentTime", (double) currentTime / 1000D);
            if (customData != null)
                jsonobject1.put("customData", customData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage(jsonobject1.toString(), requestId, null);
        return requestId;
    }

    public final long load(IStatusRequest avy, MediaInfo atz1, long currentTime, JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId;
        jsonobject1 = new JSONObject();
        requestId = getId();
        mRequestTracker_h.start(requestId, avy);
        a(true);
        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "LOAD");
            jsonobject1.put("media", atz1.buildJsonObj());
            jsonobject1.put("autoplay", true);
            jsonobject1.put("currentTime", (double) currentTime / 1000D);
            if (customData != null)
                jsonobject1.put("customData", customData);
        } catch (JSONException e) {

        }
        sendMessage(jsonobject1.toString(), requestId, null);
        return requestId;
    }

    public final void pause(JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId;
        jsonobject1 = new JSONObject();
        requestId = getId();

        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "PAUSE");
            jsonobject1.put("mediaSessionId", getMediaSessionId());
            if (customData != null)
                jsonobject1.put("customData", customData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage(jsonobject1.toString(), requestId, null);
        return;
    }

    public final void checkReceivedMessage(String message)
    {
        mLogs.d("message received: %s", new Object[] {
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
                    mMediaStatus = null;
                    onStatusUpdated();
                    sendItemStatusUpdate_i();
                    mRequestTracker_n.onResult(requestId, 0);
                    return;
                }

                JSONObject jsonobject5;
                boolean flag;
                jsonobject5 = jsonarray.getJSONObject(0);
                flag = mRequestTracker_h.a(requestId);

                boolean flag1 = false;
                int k = 0;
                int i1 = 0;
                if (mRequestTracker_i.isRunning() && !mRequestTracker_i.a(requestId))
                    flag1 = true;
                else
                    flag1 = false;

                boolean flag2;
                if (mRequestTracker_l.isRunning() && !mRequestTracker_l.a(requestId)
                        || mRequestTracker_m.isRunning() && !mRequestTracker_m.a(requestId))
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
                    mMediaStatus = new MediaStatus(jsonobject5);
                    mElapsedTime = SystemClock.elapsedRealtime();
                    i1 = 7;
                } else {
                    if (mMediaStatus != null) {
                        i1 = mMediaStatus.init(jsonobject5, k);
                    }
                }

                if ((i1 & 1) != 0) {
                    mElapsedTime = SystemClock.elapsedRealtime();
                    onStatusUpdated();
                }

                if ((i1 & 2) == 0) {
                    mElapsedTime = SystemClock.elapsedRealtime();
                    onStatusUpdated();
                }

                if ((i1 & 4) != 0) {
                    sendItemStatusUpdate_i();
                }

                mRequestTracker_h.onResult(requestId, 0);
                mRequestTracker_i.onResult(requestId, 0);
                mRequestTracker_l.onResult(requestId, 0);
                mRequestTracker_m.onResult(requestId, 0);
                mRequestTracker_n.onResult(requestId, 0);
                return;
            }
            if (type.equals("INVALID_PLAYER_STATE"))
            {
                mLogs.w("received unexpected error: Invalid Player State.", new Object[0]);
                jsonobject4 = jsonobject.optJSONObject("customData");
                mRequestTracker_h.onResult(requestId, 1, jsonobject4);
                mRequestTracker_i.onResult(requestId, 1, jsonobject4);
                mRequestTracker_l.onResult(requestId, 1, jsonobject4);
                mRequestTracker_m.onResult(requestId, 1, jsonobject4);
                mRequestTracker_n.onResult(requestId, 1, jsonobject4);
                return;
            }
            if (type.equals("LOAD_FAILED"))
            {
                jsonobject3 = jsonobject.optJSONObject("customData");
                mRequestTracker_h.onResult(requestId, 1, jsonobject3);
                return;
            }
            if (type.equals("LOAD_CANCELLED"))
            {
                jsonobject2 = jsonobject.optJSONObject("customData");
                mRequestTracker_h.onResult(requestId, 2, jsonobject2);
                return;
            }
            if (type.equals("INVALID_REQUEST"))
            {
                mLogs.w("received unexpected error: Invalid Request.", new Object[0]);
                jsonobject1 = jsonobject.optJSONObject("customData");
                mRequestTracker_h.onResult(requestId, 1, jsonobject1);
                mRequestTracker_i.onResult(requestId, 1, jsonobject1);
                mRequestTracker_l.onResult(requestId, 1, jsonobject1);
                mRequestTracker_m.onResult(requestId, 1, jsonobject1);
                mRequestTracker_n.onResult(requestId, 1, jsonobject1);
                return;
            }
        } catch (JSONException e) {
            LOG avu1;
            Object aobj[];
            avu1 = mLogs;
            aobj = new Object[2];
            aobj[0] = e.getMessage();
            aobj[1] = message;
            avu1.w("Message is malformed (%s); ignoring: %s", aobj);
            return;
        }
    }

    public final long getContentDuration()
    {
        MediaInfo info = getMediaInfo();
        if (info != null)
            return info.mDuration;
        else
            return 0L;
    }

    public final void stop(JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId = getId();
        jsonobject1 = new JSONObject();

        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "STOP");
            jsonobject1.put("mediaSessionId", getMediaSessionId());
            if (customData != null)
                jsonobject1.put("customData", customData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage(jsonobject1.toString(), requestId, null);
        return;
    }

    public final void play(JSONObject customData)
    {
        JSONObject jsonobject1;
        long requestId = getId();
        jsonobject1 = new JSONObject();
        try {
            jsonobject1.put("requestId", requestId);
            jsonobject1.put("type", "PLAY");
            jsonobject1.put("mediaSessionId", getMediaSessionId());
            if (customData != null) {
                jsonobject1.put("customData", customData);
            }
        } catch (JSONException jsonexception) {
        }
        sendMessage(jsonobject1.toString(), requestId, null);
        return;
    }

    public final void d()
    {
        j();
    }

    public final MediaStatus getMediaStatus()
    {
        return mMediaStatus;
    }

    public final MediaInfo getMediaInfo()
    {
        if (mMediaStatus == null)
            return null;
        else
            return mMediaStatus.mMedia;
    }

    public final long getMediaSessionId()
    {
        if (mMediaStatus == null)
            throw new IllegalStateException("No current media session");
        else
            return mMediaStatus.mMediaSessionId;
    }

    protected void onStatusUpdated()
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
