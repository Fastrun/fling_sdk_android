
package com.fireflycast.server.cast;

import android.util.Log;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.cast.socket.CastSocketListener;
import com.fireflycast.server.cast.socket.CastSocket;
import com.fireflycast.server.cast.socket.data.C_axm;
import com.fireflycast.server.utils.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

final class CastDeviceManager implements CastSocketListener {
    final CastSocket mCastSocket_a;
    final CastDevice mCastDevice_b;
    boolean mNoApp_c;
    boolean mNoNamespace_d;
    boolean e;
    final DeviceFilter mDeviceFilter_f;
    private final String g;
    private final AppInfoHelper h = new AppInfoHelper((byte) 0);
    private final JSONArray i = new JSONArray();

    public CastDeviceManager(DeviceFilter deviceFilter, CastDevice castDevice)
    {
        super();

        mDeviceFilter_f = deviceFilter;

        mNoApp_c = false;
        mNoNamespace_d = false;
        e = true;
        mCastSocket_a = new CastSocket(DeviceFilter.getContext_a(deviceFilter), this);
        mCastDevice_b = castDevice;
        Object aobj[] = new Object[2];
        aobj[0] = DeviceFilter.b(deviceFilter);
        aobj[1] = Long.valueOf(DeviceFilter.a().incrementAndGet());
        g = String.format("%s-%d", aobj);
        if (DeviceFilter.getDiscoveryCriterias_c(deviceFilter).size() > 0)
        {
            Iterator iterator = DeviceFilter.getDiscoveryCriterias_c(deviceFilter).iterator();
            boolean noNameSpace = true;
            boolean noApp = true;
            while (iterator.hasNext())
            {
                DiscoveryCriteria criteria = (DiscoveryCriteria) iterator.next();
                Set set = Collections.unmodifiableSet(criteria.mNamespaceList_c);
                if (criteria.mAppid_b != null)
                {
                    i.put(criteria.mAppid_b);
                    noApp = false;
                }
                if (set.size() > 0)
                    noNameSpace = false;
            }
            if (noApp)
                setNoApp_b();
            if (noNameSpace)
                setNoNameSpace_c();
        }
    }

    private void sendMessage_a(String namespace, String message) throws Exception
    {
        Logs avu1 = DeviceFilter.getLogs_b();
        Object aobj[] = new Object[4];
        aobj[0] = mCastDevice_b.getFriendlyName();
        aobj[1] = namespace;
        aobj[2] = "receiver-0";
        aobj[3] = message;
        avu1.d("Sending text message to %s: (ns=%s, dest=%s) %s", aobj);
        C_axm axm1 = (new C_axm()).a(0).a(g).b("receiver-0").c(namespace).b(0).d(message);
        mCastSocket_a.send_a(ByteBuffer.wrap(axm1.K()));
    }

    private void setNoApp_b()
    {
        mNoApp_c = true;
        if (mNoApp_c && mNoNamespace_d)
            updateStatus_d();
    }

    private void setNoNameSpace_c()
    {
        mNoNamespace_d = true;
        if (mNoApp_c && mNoNamespace_d)
            updateStatus_d();
    }

    private void updateStatus_d()
    {
        HashSet hashset;
        if (mCastSocket_a.isConnected_c())
        {
            Iterator iterator;
            DiscoveryCriteria aty1;
            AppInfoHelper axa1;
            String s;
            try
            {
                sendMessage_a("urn:x-cast:com.google.cast.tp.connection",
                        (new JSONObject()).put("type", "CLOSE").toString());
            } catch (IOException ioexception) {
                DeviceFilter.getLogs_b().d(ioexception, "Failed to send disconnect message",
                        new Object[0]);
            } catch (JSONException jsonexception) {
                DeviceFilter.getLogs_b().e(jsonexception, "Failed to build disconnect message",
                        new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
                DeviceFilter.getLogs_b().d(e, "Failed to send disconnect message",
                        new Object[0]);
            }
            mCastSocket_a.disconnect_b();
        }
        hashset = new HashSet();
        Iterator iterator = DeviceFilter.getDiscoveryCriterias_c(mDeviceFilter_f).iterator();
        do
        {
            if (!iterator.hasNext())
                break;
            DiscoveryCriteria criteria = (DiscoveryCriteria) iterator.next();

            boolean flag;
            if ((criteria.mAppid_b == null || h.mAppAvailabityList_b.contains(criteria.mAppid_b))
                    && h.mAppNamespaceList_a.containsAll(Collections
                            .unmodifiableSet(criteria.mNamespaceList_c)))
                flag = true;
            else
                flag = false;
            if (flag)
                hashset.add(criteria);
        } while (true);

        if (e && hashset.size() > 0)
        {
            acceptDevice_a(mCastDevice_b, hashset);
            return;
        }

        Logs avu1 = DeviceFilter.getLogs_b();
        Object aobj[] = new Object[1];
        aobj[0] = mCastDevice_b;
        avu1.d("rejected device: %s", aobj);
        return;
    }

    public final void onConnected_a()
    {
        try
        {
            sendMessage_a(
                    "urn:x-cast:com.google.cast.tp.connection",
                    (new JSONObject()).put("type", "CONNECT")
                            .put("package", DeviceFilter.b(mDeviceFilter_f))
                            .put("origin", new JSONObject())
                            .toString());

            if (!mNoApp_c)
                sendMessage_a("urn:x-cast:com.google.cast.receiver",
                        (new JSONObject()).put("requestId", 1)
                                .put("type", "GET_APP_AVAILABILITY").put("appId", i).toString());

            if (!mNoNamespace_d)
                sendMessage_a("urn:x-cast:com.google.cast.receiver",
                        (new JSONObject()).put("requestId", 2)
                                .put("type", "GET_STATUS").toString());

            return;
            // } catch (IOException ioexception)
            // {
            // DeviceFilter_awz.b().c(ioexception, "Failed to send messages",
            // new Object[0]);
            // return;
        } catch (JSONException jsonexception)
        {
            DeviceFilter.getLogs_b()
                    .e(jsonexception, "Failed to build messages", new Object[0]);
        } catch (Exception e) {
            DeviceFilter.getLogs_b().e(e, "Failed to send messages", new Object[0]);
            e.printStackTrace();
        }
    }

    public final void onConnectionFailed_a(int j)
    {
        Logs avu1 = DeviceFilter.getLogs_b();
        Object aobj[] = new Object[4];
        aobj[0] = mCastDevice_b.getIpAddress().toString();
        aobj[1] = Integer.valueOf(mCastDevice_b.getServicePort());
        aobj[2] = mCastDevice_b.getFriendlyName();
        aobj[3] = Integer.valueOf(j);
        avu1.w("Connection to %s:%d (%s) failed with error %d", aobj);

        // CastDevice castdevice = mCastDevice_b;
        // DeviceFilter_awz.getHandler_d(mDeviceFilter_f).post(new C_axd(this,
        // castdevice));

        DeviceFilter.getHandler_d(mDeviceFilter_f).post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mDeviceFilter_f.setDeviceOffline_a(mCastDevice_b);
            }

        });
    }

    final void acceptDevice_a(final CastDevice castdevice, final Set set)
    {
        // DeviceFilter_awz.getHandler_d(mDeviceFilter_f).post(new C_axc(this,
        // castdevice, set));

        DeviceFilter.getHandler_d(mDeviceFilter_f).post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mDeviceFilter_f.onDeviceAccepted_a(castdevice, set);
            }

        });
    }

    public final void onMessageReceived_a(ByteBuffer bytebuffer)
    {
        long requestId;
        Logs avu1 = DeviceFilter.getLogs_b();
        Object aobj[] = new Object[1];
        aobj[0] = g;
        avu1.d("onMessageReceived:in[%s]", aobj);
        C_axm axm1;
        String message;
        JSONObject jsonobject;
        try
        {
            axm1 = C_axm.a(bytebuffer.array());
            Logs avu3 = DeviceFilter.getLogs_b();
            Object aobj2[] = new Object[1];
            aobj2[0] = axm1.toString();
            avu3.d("Received a protobuf: %s", aobj2);
            // } catch (C_igt igt1)
            // {
            // Logs_avu avu2 = DeviceFilter_awz.b();
            // Object aobj1[] = new Object[1];
            // aobj1[0] = igt1.getMessage();
            // avu2.b("Received an unparseable protobuf: %s", aobj1);
            // return;
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if (axm1.getPayloadType_d() != 0) {
            return;
        }
        
        message = axm1.getMessage_e();
        Log.d("DeviceFilter","onMessageReceived:" + message);
        try
        {
            jsonobject = new JSONObject(message);
            requestId = jsonobject.optLong("requestId", -1L);
        } catch (JSONException jsonexception)
        {
            Logs avu4 = DeviceFilter.getLogs_b();
            Object aobj3[] = new Object[1];
            aobj3[0] = jsonexception.getMessage();
            avu4.e("Failed to parse response: %s", aobj3);
            return;
        }
        if (requestId == -1L) {
            return;
        }
        if (requestId != 1L) {
            if (requestId != 2L) {
                DeviceFilter.getLogs_b().d(
                        (new StringBuilder("Unrecognized request ID: ")).append(requestId)
                                .toString(),
                        new Object[0]);
                return;
            }
            h.fillNamespaceList_a(jsonobject);
            setNoNameSpace_c();
            return;
        }
        h.fillAppAvailabityList_b(jsonobject);
        setNoApp_b();
        return;
    }

    public final void onDisconnected_b(int j)
    {
        DeviceFilter.getLogs_b().d("Device filter disconnected", new Object[0]);
    }
}
