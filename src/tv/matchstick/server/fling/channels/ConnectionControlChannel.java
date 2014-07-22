
package tv.matchstick.server.fling.channels;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

public final class ConnectionControlChannel extends FlingChannel {
    private static final String mUserAgent;
    private final String mPackage;

    public ConnectionControlChannel(String pName)
    {
        super("urn:x-cast:com.google.cast.tp.connection", "ConnectionControlChannel");
        mPackage = pName;
    }

    public final void connect(String transportId)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("type", "CONNECT");
            JSONObject jsonobject1 = new JSONObject();
            jsonobject1.put("package", mPackage);
            jsonobject.put("origin", jsonobject1);
            jsonobject.put("userAgent", mUserAgent);
        } catch (JSONException jsonexception) {
        }
        sendMessage(jsonobject.toString(), 0L, transportId);
    }

    public final void close(String s)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("type", "CLOSE");
        } catch (JSONException jsonexception) {
        }
        sendMessage(jsonobject.toString(), 0L, s);
    }

    static
    {
        Object aobj[] = new Object[4];
        aobj[0] = Integer.valueOf(0x40be38);
        aobj[1] = Build.MODEL;
        aobj[2] = Build.PRODUCT;
        aobj[3] = android.os.Build.VERSION.RELEASE;
        mUserAgent = String.format("Android FlingSDK,%d,%s,%s,%s", aobj);
    }
}
