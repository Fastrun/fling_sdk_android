
package com.fireflycast.server.cast.channels;

import android.os.SystemClock;

import org.json.JSONException;
import org.json.JSONObject;

public final class HeartbeatChannel_aul extends CastChannel_avn {
    private long mCreateTime_a;
    private long mTimeout_b;
    private boolean isPingSent_c;

    public HeartbeatChannel_aul()
    {
        super("urn:x-cast:com.google.cast.tp.heartbeat", "HeartbeatChannel");
        mTimeout_b = 10000L;
        reset_a();
    }

    public final void reset_a()
    {
        mCreateTime_a = SystemClock.elapsedRealtime();
        isPingSent_c = false;
    }

    public final boolean isTimeout_a(long currentTime)
    {
        if (mTimeout_b == 0L) {
            return false;
        }

        long elapsedTime;
        elapsedTime = currentTime - mCreateTime_a;
        if (elapsedTime >= mTimeout_b)
            return true;
        if (isPingSent_c || elapsedTime < mTimeout_b / 2L) {
            return false;
        }

        JSONObject jsonobject;
        mLogs_j.v("sending PING", new Object[0]);
        jsonobject = new JSONObject();
        try
        {
            jsonobject.put("type", "PING");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage_a(jsonobject.toString(), 0L, "transport-0");
        isPingSent_c = true;
        return false;
    }

    public final void checkReceivedMessage_a_(String message)
    {
        reset_a();
        mLogs_j.v("Received: %s", new Object[] {
                message
        });
        boolean flag;
        JSONObject jsonobject;
        try
        {
            flag = "PING".equals((new JSONObject(message)).getString("type"));
        } catch (JSONException jsonexception)
        {
            Object aobj[] = new Object[2];
            aobj[0] = jsonexception.getMessage();
            aobj[1] = message;
            mLogs_j.w("Message is malformed (%s); ignoring: %s", aobj);
            return;
        }
        if (!flag) {
            return;
        }
        mLogs_j.v("sending PONG", new Object[0]);
        jsonobject = new JSONObject();
        try
        {
            jsonobject.put("type", "PONG");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage_a(jsonobject.toString(), 0L, "transport-0");

        return;
    }
}
