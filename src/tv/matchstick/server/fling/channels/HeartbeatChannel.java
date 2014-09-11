
package tv.matchstick.server.fling.channels;

import android.os.SystemClock;

import org.json.JSONException;
import org.json.JSONObject;

public final class HeartbeatChannel extends FlingChannel {
    private long mCreateTime;
    private long mTimeout;
    private boolean isPingSent;
    private int mCounter;

    public HeartbeatChannel()
    {
        super("urn:x-cast:com.google.cast.tp.heartbeat", "HeartbeatChannel");
        mTimeout = 10000L;
        reset();
    }

    public final void reset()
    {
        mCreateTime = SystemClock.elapsedRealtime();
        isPingSent = false;
        mCounter = 0;
    }

    public final boolean isTimeout(long currentTime)
    {
        if (mTimeout == 0L) {
            return false;
        }

        long elapsedTime;
        elapsedTime = currentTime - mCreateTime;
        if (elapsedTime >= mTimeout) {
            if (mCounter < 5) {
                android.util.Log.d("HeartbeatChannel", "retry PING: " + mCounter);
                mLogs.v("retry PING", new Object[0]);
                sendPing();
                mCounter++;
            } else {
                mCounter = 0;
                return true;
            }
            
        }
        if (isPingSent || elapsedTime < mTimeout / 2L) {
            return false;
        }

        sendPing();
        return false;
    }
    
    private void sendPing() {
        JSONObject jsonobject;
        mLogs.v("sending PING", new Object[0]);
        jsonobject = new JSONObject();
        try
        {
            jsonobject.put("type", "PING");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage(jsonobject.toString(), 0L, "transport-0");
        isPingSent = true;
    }

    public final void checkReceivedMessage(String message)
    {
        reset();
        mLogs.v("Received: %s", new Object[] {
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
            mLogs.w("Message is malformed (%s); ignoring: %s", aobj);
            return;
        }
        if (!flag) {
            return;
        }
        mLogs.v("sending PONG", new Object[0]);
        jsonobject = new JSONObject();
        try
        {
            jsonobject.put("type", "PONG");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage(jsonobject.toString(), 0L, "transport-0");

        return;
    }
}
