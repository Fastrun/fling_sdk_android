
package com.fireflycast.server.cast;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.utils.Logs_avu;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

abstract class DeviceFilter_awz {
    private static final Logs_avu mLogs_a = new Logs_avu("DeviceFilter");
    private static AtomicLong b = new AtomicLong(0L);
    private final Context mContext_c;
    private final String d;
    private final List e = new ArrayList();
    private final Handler mHandler_f = new Handler(Looper.getMainLooper());
    private Set mDiscoveryCriterias_g;

    public DeviceFilter_awz(Context context, Set set, String s)
    {
        mContext_c = context;
        mDiscoveryCriterias_g = new HashSet(set);
        d = s;
    }

    static Context getContext_a(DeviceFilter_awz awz1)
    {
        return awz1.mContext_c;
    }

    static AtomicLong a()
    {
        return b;
    }

    static Logs_avu getLogs_b()
    {
        return mLogs_a;
    }

    static String b(DeviceFilter_awz awz1)
    {
        return awz1.d;
    }

    static Set getDiscoveryCriterias_c(DeviceFilter_awz awz1)
    {
        return awz1.mDiscoveryCriterias_g;
    }

    static Handler getHandler_d(DeviceFilter_awz awz1)
    {
        return awz1.mHandler_f;
    }

    protected abstract void setDeviceOffline_a(CastDevice castdevice);

    protected abstract void onDeviceAccepted_a(CastDevice castdevice, Set set);

    public final void reset_a(Set set)
    {
        for (Iterator iterator = e.iterator(); iterator.hasNext();)
            ((CastDeviceManager_axb) iterator.next()).e = false;

        e.clear();
        mDiscoveryCriterias_g = new HashSet(set);
    }

    public final void connectOrAcceptDevice_b(CastDevice castdevice)
    {
        CastDeviceManager_axb axb1 = new CastDeviceManager_axb(this, castdevice);
        if (!axb1.mNoApp_c || !axb1.mNoNamespace_d) {
            try
            {
                Object aobj[] = new Object[3];
                aobj[0] = axb1.mCastDevice_b.getIpAddress().toString();
                aobj[1] = Integer.valueOf(axb1.mCastDevice_b.getServicePort());
                aobj[2] = axb1.mCastDevice_b.getFriendlyName();
                mLogs_a.d("connecting to: %s:%d (%s)", aobj);
                axb1.mCastSocket_a.connect_a(axb1.mCastDevice_b.getIpAddress(),
                        axb1.mCastDevice_b.getServicePort());
            } catch (Exception ioexception)
            {
                mLogs_a.e(ioexception, "Exception while connecting socket", new Object[0]);
            }
        } else {
            Object aobj[] = new Object[3];
            aobj[0] = axb1.mCastDevice_b.getIpAddress().toString();
            aobj[1] = Integer.valueOf(axb1.mCastDevice_b.getServicePort());
            aobj[2] = axb1.mCastDevice_b.getFriendlyName();
            mLogs_a.d("accept device to: %s:%d (%s)", aobj);
            axb1.acceptDevice_a(axb1.mCastDevice_b, axb1.mDeviceFilter_f.mDiscoveryCriterias_g);
        }
        e.add(axb1);
    }
}
