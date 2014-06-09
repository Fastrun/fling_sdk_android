
package com.fireflycast.server.cast.channels;

import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

public final class ConnectionControlChannel extends CastChannel {
    private static final String mUserAgent_a;
    private final String mPackage_b;

    public ConnectionControlChannel(String pName)
    {
        super("urn:x-cast:com.google.cast.tp.connection", "ConnectionControlChannel");
        mPackage_b = pName;
    }

    public final void connect_a(String transportId)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("type", "CONNECT");
            JSONObject jsonobject1 = new JSONObject();
            jsonobject1.put("package", mPackage_b);
            jsonobject.put("origin", jsonobject1);
            jsonobject.put("userAgent", mUserAgent_a);
        } catch (JSONException jsonexception) {
        }
        sendMessage_a(jsonobject.toString(), 0L, transportId);
    }

    public final void close_b(String s)
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("type", "CLOSE");
        } catch (JSONException jsonexception) {
        }
        sendMessage_a(jsonobject.toString(), 0L, s);
    }

    static
    {
        Object aobj[] = new Object[4];
        aobj[0] = Integer.valueOf(0x40be38);
        aobj[1] = Build.MODEL;
        aobj[2] = Build.PRODUCT;
        aobj[3] = android.os.Build.VERSION.RELEASE;
        mUserAgent_a = String.format("Android CastSDK,%d,%s,%s,%s", aobj);
    }
}
