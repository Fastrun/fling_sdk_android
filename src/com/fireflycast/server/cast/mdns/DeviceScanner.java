
package com.fireflycast.server.cast.mdns;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.utils.Logs;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.net.InterfaceAddress;

public abstract class DeviceScanner {
    static final Logs mLogs_a = new Logs("DeviceScanner");
    final Handler mHandler_b = new Handler(Looper.getMainLooper());
    private final Context mContext_c;
    private final List mListenerList_d = new ArrayList();
    private int e;
    private final AtomicBoolean f = new AtomicBoolean();
    private final ConnectivityManager mConnectivityManager_g;
    private BroadcastReceiver mConnectChangeReceiver_h;
    private final WifiManager mWifiManager_i;
    private String mBSSID_j;
    private volatile boolean k;
    private boolean mScanning_l;
    private boolean m;
    private boolean mErrorState_n;

    protected DeviceScanner(Context context) {
        e = 0;
        mContext_c = context;
        mConnectivityManager_g = (ConnectivityManager) context
                .getSystemService("connectivity");
        mWifiManager_i = (WifiManager) context.getSystemService("wifi");
    }

    static ConnectivityManager a(DeviceScanner aur1) {
        return aur1.mConnectivityManager_g;
    }

    private void a(int i1) {
        if (e != i1) {
            e = i1;
            final List list = getDeviceScannerListenerList_e();
            if (list != null) {
                // mHandler_b.post(new C_aus(this, list, i1));
                mHandler_b.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // todo. do nothing?
                        for (Iterator iterator = list.iterator(); iterator.hasNext();) {
                            iterator.next();
                            // int = b;
                        }
                    }

                });
            }
        }
    }

    static boolean getErrorState_b(DeviceScanner aur1) {
        return aur1.mErrorState_n;
    }

    static void checkBSSID_c(DeviceScanner aur1) {
        aur1.checkBSSID_l();
    }

    static boolean d(DeviceScanner aur1) {
        return aur1.m;
    }

    static void stopScanInit_e(DeviceScanner aur1) {
        aur1.stopScanInit_k();
    }

    static boolean f(DeviceScanner aur1) {
        aur1.m = false;
        return false;
    }

    static void startScanInit_g(DeviceScanner aur1) {
        aur1.startScanInit_j();
    }

    static Logs getLogs_h() {
        return mLogs_a;
    }

    private static List getCastNetworkInterfaceList_i() {
        ArrayList arraylist = new ArrayList();
        try {
            Enumeration enumeration = NetworkInterface.getNetworkInterfaces();
            if (enumeration != null) {

                while (enumeration.hasMoreElements()) {
                    NetworkInterface networkinterface = (NetworkInterface) enumeration
                            .nextElement();
                    if (networkinterface.isUp()
                            && !networkinterface.isLoopback()
                            && !networkinterface.isPointToPoint()
                            && networkinterface.supportsMulticast()) {
                        Iterator iterator = networkinterface
                                .getInterfaceAddresses().iterator();
                        while (iterator.hasNext()) {
                            if (((InterfaceAddress) iterator.next())
                                    .getAddress() instanceof Inet4Address)
                                arraylist.add(networkinterface);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            mLogs_a.d(e, "Exception while selecting network interface",
                    new Object[0]);
        }
        return arraylist;
    }

    private void startScanInit_j() {
        mLogs_a.d("startScanInit", new Object[0]);
        mErrorState_n = false;
        k = false;
        checkBSSID_l();
        startScanInternal_a(getCastNetworkInterfaceList_i());
    }

    private void stopScanInit_k() {
        mLogs_a.d("stopScanInit", new Object[0]);
        k = true;
        stopScanInternal_c();
    }

    private void checkBSSID_l() {
        WifiInfo wifiinfo = mWifiManager_i.getConnectionInfo();
        String bssId = null;
        if (wifiinfo != null)
            bssId = wifiinfo.getBSSID();
        if (mBSSID_j == null || bssId == null || !mBSSID_j.equals(bssId)) {
            mLogs_a.d("BSSID changed", new Object[0]);
            onAllDevicesOffline_d();
        }
        mBSSID_j = bssId;
    }

    public final void startScan_a() {
        if (mScanning_l)
            return;
        mScanning_l = true;
        a(1);
        if (mConnectChangeReceiver_h == null) {
            // mConnectChangeReceiver_h = new C_aux(this, (byte) 0); // todo
            mConnectChangeReceiver_h = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    // TODO Auto-generated method stub
                    NetworkInfo localNetworkInfo = mConnectivityManager_g.getActiveNetworkInfo();
                    boolean connected;
                    if ((localNetworkInfo != null) && (localNetworkInfo.isConnected()))
                    {
                        connected = true;
                        Logs localavu = DeviceScanner.getLogs_h();
                        Object[] arrayOfObject = new Object[2];
                        arrayOfObject[0] = Boolean.valueOf(connected);
                        arrayOfObject[1] = mErrorState_n;
                        localavu.d("connectivity state changed. connected? %b, errorState? %b",
                                arrayOfObject);
                        checkBSSID_l();
                        if (!connected)
                            onAllDevicesOffline_d();
                        if (m)
                        {
                            stopScanInit_k();
                            m = false;
                        }
                        if (connected) {
                            DeviceScanner
                                    .getLogs_h()
                                    .d(
                                            "re-established connectivity after connectivity changed;  restarting scan",
                                            new Object[0]);
                            startScanInit_j();

                            return;
                        }

                        if (mErrorState_n) {
                            return;
                        }

                        DeviceScanner.getLogs_h().d("lost connectivity while scanning;", new Object[0]);
                        g();
                    }
                }

            };

            IntentFilter intentfilter = new IntentFilter(
                    "android.net.conn.CONNECTIVITY_CHANGE");
            mContext_c.registerReceiver(mConnectChangeReceiver_h, intentfilter);
        }
        startScanInit_j();
        mLogs_a.d("scan started", new Object[0]);
    }

    public final void addListener_a(IDeviceScanListener auy) {
        if (auy == null)
            throw new IllegalArgumentException("listener cannot be null");
        synchronized (mListenerList_d) {
            if (mListenerList_d.contains(auy))
                throw new IllegalArgumentException(
                        "the same listener cannot be added twice");
            mListenerList_d.add(auy);
        }
    }

    protected final void notifyDeviceOffline_a(final CastDevice castdevice) {
        mLogs_a.d("notifyDeviceOffline: %s", new Object[] {
                castdevice
        });
        final List list = getDeviceScannerListenerList_e();
        if (list != null) {
            // mHandler_b.post(new C_auu(this, list, castdevice));

            mHandler_b.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    for (Iterator iterator = list.iterator(); iterator.hasNext(); ((IDeviceScanListener) iterator
                            .next()).onDeviceOffline_b(castdevice))
                        ;
                }

            });
        }
    }

    public abstract void setDeviceOffline_a(String s);

    protected abstract void startScanInternal_a(List list);

    public final void stopScan_b() {
        if (!mScanning_l)
            return;
        if (mConnectChangeReceiver_h != null) {
            try {
                mContext_c.unregisterReceiver(mConnectChangeReceiver_h);
            } catch (IllegalArgumentException illegalargumentexception) {
            }
            mConnectChangeReceiver_h = null;
        }
        stopScanInit_k();
        m = false;
        mHandler_b.removeCallbacksAndMessages(null);
        mScanning_l = false;
        a(0);
        mLogs_a.d("scan stopped", new Object[0]);
    }

    protected abstract void stopScanInternal_c();

    public abstract void onAllDevicesOffline_d();

    List getDeviceScannerListenerList_e() {
        ArrayList arraylist = null;
        synchronized (mListenerList_d) {
            boolean flag = mListenerList_d.isEmpty();

            arraylist = null;
            if (flag) {
                return null;
            }
            arraylist = new ArrayList(mListenerList_d);
        }
        return arraylist;
    }

    protected final void reportNetworkError_f() {
        if (!mErrorState_n) {
            mLogs_a.d("reportNetworkError; errorState now true", new Object[0]);
            mErrorState_n = true;
            onAllDevicesOffline_d();
            a(2);
        }
    }

    protected final void g() {
        if (!f.getAndSet(true)) {
            // mHandler_b.post(new C_auw(this));

            mHandler_b.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    reportNetworkError_f();
                }

            });
        }
    }
}
