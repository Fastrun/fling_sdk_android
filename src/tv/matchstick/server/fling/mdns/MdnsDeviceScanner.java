
package tv.matchstick.server.fling.mdns;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.os.SystemClock;

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

import tv.matchstick.fling.FlingDevice;
import tv.matchstick.server.common.images.WebImage;
import tv.matchstick.server.utils.LOG;

public final class MdnsDeviceScanner extends DeviceScanner {
    private static final LOG mLogs = new LOG("MdnsDeviceScanner");
    private final List mFlingMdnsClientList = new ArrayList();
    private final Map e = new HashMap();
    private final String mName;
    private Thread mScannerLoopThread;
    private boolean h;

    public MdnsDeviceScanner(Context paramContext) {
        super(paramContext);
        this.mName = "Fling Device";
    }

    static void scanLoop(final MdnsDeviceScanner paramave) {
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
                    if (l - localavi.mElapsedRealtime < 60000L) {
                        i = 0;
                    } else {
                        i = 1;
                    }
                    if (i == 0)
                        continue;
                    final FlingDevice device = localavi.mFlingDevice;

                    paramave.mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            paramave.notifyDeviceOffline(device);
                        }

                    });
                    mLogs.d("expired record for %s",device);

                    localIterator.remove();
                }
            }
        }

        mLogs.d("refreshLoop exiting");
        return;
    }

    static void onScanResults(MdnsDeviceScanner deviceScanner, FlingDeviceInfo info) {
        if (mLogs.isDebugEnabled()) {
            mLogs.d("FQDN: %s", info.FQDN_a);
            List localList4 = info.mIpV4AddrList;
            if (localList4 != null) {
                Iterator localIterator4 = localList4.iterator();
                while (localIterator4.hasNext()) {
                    Inet4Address localInet4Address2 = (Inet4Address) localIterator4
                            .next();
                    mLogs.d("IPv4 address: %s", localInet4Address2);
                }
            }
            List localList5 = info.mIpV6AddrList;
            if (localList5 != null) {
                Iterator localIterator3 = localList5.iterator();
                while (localIterator3.hasNext()) {
                    Inet6Address localInet6Address = (Inet6Address) localIterator3
                            .next();
                    mLogs.d("IPv6 address: %s", localInet6Address);
                }
            }
            mLogs.d("service name: %s", info.mName);
            
            mLogs.d("service host: %s", info.mHost);
            
            mLogs.d("service proto: %d", info.mProto);
            
            mLogs.d("service port: %d", info.mPort);
            
            mLogs.d("service priority: %d", info.mPriority);
            
            mLogs.d("service weight: %d", info.mWeight);
            
            List localList6 = info.mTextStringList;
            if (localList6 != null) {
                Iterator localIterator2 = localList6.iterator();
                while (localIterator2.hasNext()) {
                    String str6 = (String) localIterator2.next();
                    mLogs.d("text string: %s", str6);
                }
            }

            mLogs.d("TTL: %d", info.mTTL);
        }
        List localList1 = info.mTextStringList;
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
                                "(Eureka|Chromekey)( Dongle)?", "Dongle");
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
                str1 = deviceScanner.mName;
            final FlingDevice device;
            ScannerPrivData localavi;
            synchronized (deviceScanner.e) {
                List localList2 = info.mIpV4AddrList;
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
                
                deviceId = deviceId + localInet4Address1.getHostAddress();
                
                FlingDeviceHelper localatr = FlingDevice.createHelper((String) deviceId,
                        localInet4Address1);
                String str3 = info.e;
                FlingDevice.setFriendlyName(localatr.mFlingDevice, str3);
                FlingDevice.setModelName(localatr.mFlingDevice, str1);
                FlingDevice.setDeviceVersion(localatr.mFlingDevice, (String) localObject2);
                int port = info.mPort;
                FlingDevice.setServicePort(localatr.mFlingDevice, port);
                FlingDevice.setIconList(localatr.mFlingDevice, iconList);
                device = localatr.mFlingDevice;
                localavi = (ScannerPrivData) deviceScanner.e.get(deviceId);
                if (localavi != null) {
                    if (device.equals(localavi.mFlingDevice)) {
                        if (!localavi.d) {
                            localavi.mElapsedRealtime = SystemClock.elapsedRealtime();
                        }
                        return;
                    } else {
                        deviceScanner.e.remove(deviceId);
                    }
                }

                deviceScanner.e.put(deviceId, new ScannerPrivData(deviceScanner, device,
                        info.mTTL));
            }
            // CastDevice localCastDevice2 = localavi.mCastDevice_a; // localavi
            // will null???? need check this!!!!!!!!!!!!!!!!!!!!!!!!!
            FlingDevice device2 = null;
            if (localavi != null) {
                device2 = localavi.mFlingDevice;
            }
            if (device2 != null)
                deviceScanner.notifyDeviceOffline(device2);
            if (device == null)
                return;
            
            DeviceScanner.mLogs.d("notifyDeviceOnline: %s", device);

            final List listenerList = deviceScanner.getDeviceScannerListenerList();
            if (listenerList == null)
                return;

            deviceScanner.mHandler.post(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    Iterator localIterator = listenerList.iterator();
                    while (localIterator.hasNext())
                        ((IDeviceScanListener) localIterator.next())
                                .onDeviceOnline(device);
                }
            });
        }
    }

    public final void setDeviceOffline(String paramString) {
        FlingDevice device = null;
        synchronized (this.e) {
            ScannerPrivData localavi = (ScannerPrivData) this.e.get(paramString);
            if (localavi != null) {
                localavi.mElapsedRealtime = SystemClock.elapsedRealtime();
                localavi.d = true;
                device = localavi.mFlingDevice;
                if (device != null)
                    notifyDeviceOffline(device);
            }
        }
    }

    protected final void startScanInternal(List networkInterfaceList) {
        mLogs.d("startScanInternal");
        if (networkInterfaceList.isEmpty()) {
            mLogs.w("No network interfaces to scan on!", new Object[0]);
            return;
        }
        Iterator localIterator = networkInterfaceList.iterator();
        while (localIterator.hasNext()) {
            NetworkInterface localNetworkInterface = (NetworkInterface) localIterator
                    .next();
            
            MdnsClient flingMdnsClient = new MdnsClient("_googlecast._tcp.local.",localNetworkInterface) {

                @Override
                protected void onScanResults(FlingDeviceInfo info) {
                    // TODO Auto-generated method stub
                    MdnsDeviceScanner.onScanResults(MdnsDeviceScanner.this, info);
                }
                
            };

            try {
                flingMdnsClient.startScan();
                this.mFlingMdnsClientList.add(flingMdnsClient);
            } catch (Exception localIOException) { // todo
                mLogs.w("Couldn't start MDNS client for %s",
                        new Object[] {
                            localNetworkInterface
                        });
            }
        }
        // this.g = new Thread(new C_avg(this));
        this.mScannerLoopThread = new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                scanLoop(MdnsDeviceScanner.this);
            }

        });
        this.mScannerLoopThread.start();
    }

    protected final void stopScanInternal() {
        if (!this.mFlingMdnsClientList.isEmpty()) {
            Iterator localIterator = this.mFlingMdnsClientList.iterator();
            while (localIterator.hasNext())
                ((MdnsClient) localIterator.next()).stopScan();
            this.mFlingMdnsClientList.clear();
        }
        this.h = true;

        if (this.mScannerLoopThread != null) {
            boolean needWait = true;
            while (needWait) {
                try {
                    this.mScannerLoopThread.interrupt();
                    this.mScannerLoopThread.join();
                    needWait = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    needWait = true;
                }
            }
        }

        this.mScannerLoopThread = null;
    }

    public final void onAllDevicesOffline() {
        synchronized (this.e) {
            boolean bool = this.e.isEmpty();
            int i = 0;
            if (!bool) {
                this.e.clear();
                i = 1;
            }
            if (i != 0) {
                final List localList = super.getDeviceScannerListenerList();
                if (localList != null) {
                    // this.mHandler_b.post(new C_auv(this, localList));
                    this.mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            Iterator localIterator = localList.iterator();
                            while (localIterator.hasNext())
                                ((IDeviceScanListener) localIterator.next())
                                        .onAllDevicesOffline();
                        }

                    });
                }
            }
            return;
        }
    }
}
