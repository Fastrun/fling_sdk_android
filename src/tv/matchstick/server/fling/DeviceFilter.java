
package tv.matchstick.server.fling;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import tv.matchstick.fling.FlingDevice;
import tv.matchstick.server.utils.LOG;

abstract class DeviceFilter {
    private static final LOG mLogs = new LOG("DeviceFilter");
    private static AtomicLong b = new AtomicLong(0L);
    private final Context mContext;
    private final String d;
    private final List e = new ArrayList();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private Set mDiscoveryCriterias;

    public DeviceFilter(Context context, Set set, String s)
    {
        mContext = context;
        mDiscoveryCriterias = new HashSet(set);
        d = s;
    }

    static Context getContext_a(DeviceFilter awz1)
    {
        return awz1.mContext;
    }

    static AtomicLong a()
    {
        return b;
    }

    static LOG getLogs()
    {
        return mLogs;
    }

    static String b(DeviceFilter awz1)
    {
        return awz1.d;
    }

    static Set getDiscoveryCriterias(DeviceFilter awz1)
    {
        return awz1.mDiscoveryCriterias;
    }

    static Handler getHandler(DeviceFilter awz1)
    {
        return awz1.mHandler;
    }

    protected abstract void setDeviceOffline(FlingDevice flingdevice);

    protected abstract void onDeviceAccepted(FlingDevice flingdevice, Set set);

    public final void reset(Set set)
    {
        for (Iterator iterator = e.iterator(); iterator.hasNext();)
            ((FlingDeviceManager) iterator.next()).e = false;

        e.clear();
        mDiscoveryCriterias = new HashSet(set);
    }

    public final void connectOrAcceptDevice(FlingDevice flingdevice)
    {
        FlingDeviceManager axb1 = new FlingDeviceManager(this, flingdevice);
        if (!axb1.mNoApp || !axb1.mNoNamespace) {
            try
            {
                mLogs.d("connecting to: %s:%d (%s)", axb1.mFlingDevice.getIpAddress().toString(), axb1.mFlingDevice.getServicePort(), axb1.mFlingDevice.getFriendlyName());
                
                axb1.mFlingSocket.connect(axb1.mFlingDevice.getIpAddress(),
                        axb1.mFlingDevice.getServicePort());
            } catch (Exception ioexception)
            {
                mLogs.e(ioexception, "Exception while connecting socket", new Object[0]);
            }
        } else {
            mLogs.d("accept device to: %s:%d (%s)", axb1.mFlingDevice.getIpAddress().toString(), axb1.mFlingDevice.getServicePort(), axb1.mFlingDevice.getFriendlyName());
            
            axb1.acceptDevice(axb1.mFlingDevice, axb1.mDeviceFilter.mDiscoveryCriterias);
        }
        e.add(axb1);
    }
}
