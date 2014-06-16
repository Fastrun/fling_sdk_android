
package com.fireflycast.server.cast.mdns;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.common.images.WebImage;
import com.fireflycast.server.utils.Logs;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public final class MdnsDeviceScanner extends DeviceScanner {
    private static final Logs mLogs_c = new Logs("MdnsDeviceScanner");
    private final List mCastMdnsClientList_d = new ArrayList();
    private final Map e = new HashMap();
    private final String mName_f;
    private Thread mScannerLoopThread_g;
    private boolean h;

    public MdnsDeviceScanner(Context paramContext) {
        super(paramContext);
        this.mName_f = "Cast Device";
    }

    static void scanLoop_a(final MdnsDeviceScanner paramave) {
        while (!paramave.h) {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                if (paramave.h) {
                    break;
                }
            }
            synchronized (paramave.e) {
                long l = SystemClock.elapsedRealtime();
                Iterator localIterator = paramave.e.entrySet().iterator();
                while (localIterator.hasNext()) {
                    ScannerPrivData localavi = (ScannerPrivData) ((Map.Entry) localIterator
                            .next()).getValue();
                    int i = 0;
                    if (l - localavi.mElapsedRealtime_b < 60000L) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    if (i == 0)
                        continue;
                    final CastDevice localCastDevice = localavi.mCastDevice_a;
                    // paramave.mHandler_b.post(new C_avh(paramave,
                    // localCastDevice));

                    paramave.mHandler_b.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            paramave.notifyDeviceOffline_a(localCastDevice);
                        }

                    });
                    mLogs_c.d("expired record for %s",
                            new Object[] {
                                localCastDevice
                            });
                    localIterator.remove();
                }
            }
        }

        mLogs_c.d("refreshLoop exiting", new Object[0]);
        return;
    }

    static void onScanResults_a(MdnsDeviceScanner deviceScanner, CastDeviceInfo info) {
        if (mLogs_c.isDebugEnabled()) {
            Object[] arrayOfObject1 = new Object[1];
            arrayOfObject1[0] = info.FQDN_a;
            mLogs_c.d("FQDN: %s", arrayOfObject1);
            List localList4 = info.mIpV4AddrList_b;
            if (localList4 != null) {
                Iterator localIterator4 = localList4.iterator();
                while (localIterator4.hasNext()) {
                    Inet4Address localInet4Address2 = (Inet4Address) localIterator4
                            .next();
                    mLogs_c.d("IPv4 address: %s", new Object[] {
                            localInet4Address2
                    });
                }
            }
            List localList5 = info.mIpV6AddrList_c;
            if (localList5 != null) {
                Iterator localIterator3 = localList5.iterator();
                while (localIterator3.hasNext()) {
                    Inet6Address localInet6Address = (Inet6Address) localIterator3
                            .next();
                    mLogs_c.d("IPv6 address: %s", new Object[] {
                            localInet6Address
                    });
                }
            }
            // avu localavu2 = c;
            Object[] arrayOfObject2 = new Object[1];
            arrayOfObject2[0] = info.mName_d;
            mLogs_c.d("service name: %s", arrayOfObject2);
            // avu localavu3 = c;
            Object[] arrayOfObject3 = new Object[1];
            arrayOfObject3[0] = info.mHost_f;
            mLogs_c.d("service host: %s", arrayOfObject3);
            // avu localavu4 = c;
            Object[] arrayOfObject4 = new Object[1];
            arrayOfObject4[0] = Integer.valueOf(info.mProto_g);
            mLogs_c.d("service proto: %d", arrayOfObject4);
            // avu localavu5 = c;
            Object[] arrayOfObject5 = new Object[1];
            arrayOfObject5[0] = Integer.valueOf(info.mPort_h);
            mLogs_c.d("service port: %d", arrayOfObject5);
            // avu localavu6 = c;
            Object[] arrayOfObject6 = new Object[1];
            arrayOfObject6[0] = Integer.valueOf(info.mPriority_i);
            mLogs_c.d("service priority: %d", arrayOfObject6);
            // avu localavu7 = c;
            Object[] arrayOfObject7 = new Object[1];
            arrayOfObject7[0] = Integer.valueOf(info.mWeight_j);
            mLogs_c.d("service weight: %d", arrayOfObject7);
            List localList6 = info.mTextStringList_k;
            if (localList6 != null) {
                Iterator localIterator2 = localList6.iterator();
                while (localIterator2.hasNext()) {
                    String str6 = (String) localIterator2.next();
                    mLogs_c.d("text string: %s", new Object[] {
                            str6
                    });
                }
            }
            // avu localavu8 = c;
            Object[] arrayOfObject8 = new Object[1];
            arrayOfObject8[0] = Long.valueOf(info.mTTL_l);
            mLogs_c.d("TTL: %d", arrayOfObject8);
        }
        List localList1 = info.mTextStringList_k;
        if (localList1 != null) {
            Iterator localIterator1 = localList1.iterator();
            Object localObject1 = null;
            Object localObject2 = null;
            String str1 = null;
            Object deviceId = null;
            Object localObject5;
            while (localIterator1.hasNext()) {
                String str4 = (String) localIterator1.next();
                int k = str4.indexOf('=');
                if (k > 0) {
                    String str5 = str4.substring(0, k);
                    localObject5 = str4.substring(k + 1);
                    if ("id".equalsIgnoreCase(str5))
                        deviceId = localObject5;
                    else if ("md".equalsIgnoreCase(str5))
                        str1 = ((String) localObject5).replaceAll(
                                "(Eureka|Chromekey)( Dongle)?", "CastAway");
                    else if ("ve".equalsIgnoreCase(str5))
                        localObject2 = localObject5;
                    else if (!"ic".equalsIgnoreCase(str5)) {
                        // break label1011;
                        localObject5 = localObject1;
                    } else {
                        localObject1 = localObject5;
                    }
                }
            }
            if (deviceId == null)
                return;
            if (str1 == null)
                str1 = deviceScanner.mName_f;
            final CastDevice castDevice;
            ScannerPrivData localavi;
            synchronized (deviceScanner.e) {
                List localList2 = info.mIpV4AddrList_b;
                if ((localList2 == null) || (localList2.isEmpty())) {
                    deviceScanner.e.remove(deviceId);
                    return;
                }
                Inet4Address localInet4Address1 = (Inet4Address) localList2
                        .get(0);
                ArrayList iconList = new ArrayList();
                if (localObject1 != null) {
                    String str2 = localInet4Address1.toString();
                    int i = str2.indexOf('/');
                    if (i >= 0)
                        str2 = str2.substring(i + 1);
                    iconList.add(new WebImage(Uri.parse(String.format(
                            "http://%s:8008%s", new Object[] {
                                    str2,
                                    localObject1
                            }))));
                }
                CastDeviceHelper localatr = CastDevice.a((String) deviceId,
                        localInet4Address1);
                String str3 = info.e;
                CastDevice.setFriendlyName_b(localatr.mCastDevice_a, str3);
                CastDevice.setModelName_c(localatr.mCastDevice_a, str1);
                CastDevice.setDeviceVersion_d(localatr.mCastDevice_a, (String) localObject2);
                int port = info.mPort_h;
                CastDevice.setServicePort_a(localatr.mCastDevice_a, port);
                CastDevice.setIconList_a(localatr.mCastDevice_a, iconList);
                castDevice = localatr.mCastDevice_a;
                localavi = (ScannerPrivData) deviceScanner.e.get(deviceId);
                if (localavi != null) {
                    if (castDevice.equals(localavi.mCastDevice_a)) {
                        if (!localavi.d) {
                            localavi.mElapsedRealtime_b = SystemClock.elapsedRealtime();
                        }
                        return;
                    } else {
                        deviceScanner.e.remove(deviceId);
                    }
                }

                deviceScanner.e.put(deviceId, new ScannerPrivData(deviceScanner, castDevice,
                        info.mTTL_l));
            }
            // CastDevice localCastDevice2 = localavi.mCastDevice_a; // localavi
            // will null???? need check this!!!!!!!!!!!!!!!!!!!!!!!!!
            CastDevice localCastDevice2 = null;
            if (localavi != null) {
                localCastDevice2 = localavi.mCastDevice_a;
            }
            if (localCastDevice2 != null)
                deviceScanner.notifyDeviceOffline_a(localCastDevice2);
            if (castDevice == null)
                return;
            DeviceScanner.mLogs_a.d("notifyDeviceOnline: %s",
                    new Object[] {
                        castDevice
                    });
            final List listenerList = deviceScanner.getDeviceScannerListenerList_e();
            if (listenerList == null)
                return;

            // paramave.mHandler_b.post(new C_aut(paramave, listenerList,
            // castDevice));
            deviceScanner.mHandler_b.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Iterator localIterator = listenerList.iterator();
                    while (localIterator.hasNext())
                        ((IDeviceScanListener) localIterator.next())
                                .onDeviceOnline_a(castDevice);
                }

            });
        }
    }

    public final void setDeviceOffline_a(String paramString) {
        CastDevice localCastDevice = null;
        synchronized (this.e) {
            ScannerPrivData localavi = (ScannerPrivData) this.e.get(paramString);
            if (localavi != null) {
                localavi.mElapsedRealtime_b = SystemClock.elapsedRealtime();
                localavi.d = true;
                localCastDevice = localavi.mCastDevice_a;
                if (localCastDevice != null)
                    notifyDeviceOffline_a(localCastDevice);
            }
        }
    }

    protected final void startScanInternal_a(List networkInterfaceList) {
        mLogs_c.d("startScanInternal", new Object[0]);
        if (networkInterfaceList.isEmpty()) {
            mLogs_c.w("No network interfaces to scan on!", new Object[0]);
            return;
        }
        Iterator localIterator = networkInterfaceList.iterator();
        while (localIterator.hasNext()) {
            NetworkInterface localNetworkInterface = (NetworkInterface) localIterator
                    .next();
            
            MdnsClient castMdnsClient = new MdnsClient("_googlecast._tcp.local.",localNetworkInterface) {

                @Override
                protected void onScanResults_a(CastDeviceInfo info) {
                    // TODO Auto-generated method stub
                    MdnsDeviceScanner.onScanResults_a(MdnsDeviceScanner.this, info);
                }
                
            };

            try {
                castMdnsClient.startScan_a();
                this.mCastMdnsClientList_d.add(castMdnsClient);
            } catch (Exception localIOException) { // todo
                mLogs_c.w("Couldn't start MDNS client for %s",
                        new Object[] {
                            localNetworkInterface
                        });
            }
        }
        // this.g = new Thread(new C_avg(this));
        this.mScannerLoopThread_g = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scanLoop_a(MdnsDeviceScanner.this);
            }

        });
        this.mScannerLoopThread_g.start();
    }

    protected final void stopScanInternal_c() {
        if (!this.mCastMdnsClientList_d.isEmpty()) {
            Iterator localIterator = this.mCastMdnsClientList_d.iterator();
            while (localIterator.hasNext())
                ((MdnsClient) localIterator.next()).stopScan_b();
            this.mCastMdnsClientList_d.clear();
        }
        this.h = true;

        if (this.mScannerLoopThread_g != null) {
            boolean needWait = true;
            while (needWait) {
                try {
                    this.mScannerLoopThread_g.interrupt();
                    this.mScannerLoopThread_g.join();
                    needWait = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    needWait = true;
                }
            }
        }

        this.mScannerLoopThread_g = null;
    }

    public final void onAllDevicesOffline_d() {
        synchronized (this.e) {
            boolean bool = this.e.isEmpty();
            int i = 0;
            if (!bool) {
                this.e.clear();
                i = 1;
            }
            if (i != 0) {
                final List localList = super.getDeviceScannerListenerList_e();
                if (localList != null) {
                    // this.mHandler_b.post(new C_auv(this, localList));
                    this.mHandler_b.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Iterator localIterator = localList.iterator();
                            while (localIterator.hasNext())
                                ((IDeviceScanListener) localIterator.next())
                                        .onAllDevicesOffline_a();
                        }

                    });
                }
            }
            return;
        }
    }
}
