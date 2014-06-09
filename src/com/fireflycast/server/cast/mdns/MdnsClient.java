
package com.fireflycast.server.cast.mdns;

import android.text.TextUtils;

import com.fireflycast.server.utils.Logs;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

abstract class MdnsClient {
    private static final Logs mLogs_a = new Logs("MdnsClient");
    private final String mHostName_b;
    private MulticastSocket mMulticastSocket_c;
    private final byte[] mBuf_d = new byte[65536];
    private Thread mReceiveThread_e;
    private Thread mSendThread_f;
    private volatile boolean mStopScan_g;
    private final NetworkInterface mNetworkInterface_h;
    private final Set i = new LinkedHashSet();

    public MdnsClient(String hostName,
            NetworkInterface paramNetworkInterface) {
        this.mHostName_b = hostName;
        this.mNetworkInterface_h = paramNetworkInterface;
    }

    static void handleReceive_a(MdnsClient mdnsClient) throws IOException {
        byte[] arrayOfByte1 = new byte[4];
        byte[] arrayOfByte2 = new byte[16];
        DatagramPacket datagramPacket = new DatagramPacket(mdnsClient.mBuf_d,
                mdnsClient.mBuf_d.length);
        MdnsClientDpHelper localavj = null;
        int k = 0;
        int m = 0;
        int n = 0;
        CastDeviceInfoContainer localavm = null;
        while (!mdnsClient.mStopScan_g) {
            try {
                mdnsClient.mMulticastSocket_c.receive(datagramPacket);

                Object[] arrayOfObject = new Object[1];
                arrayOfObject[0] = Integer.valueOf(datagramPacket
                        .getLength());
                mLogs_a.d("received a packet of length %d", arrayOfObject);
                localavj = new MdnsClientDpHelper(datagramPacket);
                localavj.b();
                localavj.b();
                int j = localavj.b();
                k = localavj.b();
                m = localavj.b();
                n = localavj.b();

                if (j != 1)
                    throw new IOException("invalid response");
            } catch (IOException localIOException) {
                if (!mdnsClient.mStopScan_g) {
                    mLogs_a.d(localIOException, "while receiving packet",
                            new Object[0]);
                    continue;
                }
            }
            String str1 = TextUtils.join(".", localavj.c());
            localavj.b();
            localavj.b();
            localavm = new CastDeviceInfoContainer(str1);

            for (int i1 = 0;; i1++) {
                int i2;
                if (i1 < n + (k + m)) {
                    String[] arrayOfString = localavj.c();
                    i2 = localavj.b();
                    localavj.b();
                    localavj.a(4);
                    byte[] arrayOfByte3 = localavj.mData_a;
                    int i3 = localavj.b;
                    localavj.b = (i3 + 1);
                    long l1 = (long) (0xFF & arrayOfByte3[i3]) << 24;
                    byte[] arrayOfByte4 = localavj.mData_a;
                    int i4 = localavj.b;
                    localavj.b = (i4 + 1);
                    long l2 = l1 | (long) (0xFF & arrayOfByte4[i4]) << 16;
                    byte[] arrayOfByte5 = localavj.mData_a;
                    int i5 = localavj.b;
                    localavj.b = (i5 + 1);
                    long l3 = l2 | (long) (0xFF & arrayOfByte5[i5]) << 8;
                    byte[] arrayOfByte6 = localavj.mData_a;
                    int i6 = localavj.b;
                    localavj.b = (i6 + 1);
                    long l4 = l3 | (long) (0xFF & arrayOfByte6[i6]);
                    int i7 = localavj.b();
                    if ((i1 < k) && (l4 > 0L) && (l4 < 604800L)) {
                        int i11 = localavj.b;
                        byte[] arrayOfByte7 = Arrays.copyOfRange(
                                datagramPacket.getData(), i11, i11 + i7);
                        Set localSet = mdnsClient.i;
                        MdnsClientPrivData localavc = new MdnsClientPrivData(arrayOfByte7,
                                (int) l4);
                        localSet.add(new SoftReference(localavc));
                    }
                    CastDeviceInfo localavl1 = localavm.mCastDeviceInfo_a;
                    if ((localavl1.mTTL_l < 0L) || (l4 < localavl1.mTTL_l)) {
                        localavl1.mTTL_l = l4;
                    }
                    switch (i2) {
                        case 1:
                            localavj.a(arrayOfByte1);
                            try {
                                Inet4Address localInet4Address = (Inet4Address) InetAddress
                                        .getByAddress(arrayOfByte1);
                                CastDeviceInfo localavl4 = localavm.mCastDeviceInfo_a;
                                if (localavl4.mIpV4AddrList_b == null)
                                    localavl4.mIpV4AddrList_b = new ArrayList();
                                localavl4.mIpV4AddrList_b.add(localInet4Address);
                            } catch (UnknownHostException localUnknownHostException2) {
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            break;
                        case 12:
                            localavj.c();
                            break;
                        case 16:
                            if (i7 >= 0)
                                localavj.c = (i7 + localavj.b);
                            while (localavj.a() > 0) {
                                String str2 = localavj.d();
                                CastDeviceInfo localavl2 = localavm.mCastDeviceInfo_a;
                                if (localavl2.mTextStringList_k == null)
                                    localavl2.mTextStringList_k = new ArrayList();
                                localavl2.mTextStringList_k.add(str2);
                            }
                            localavj.c = -1;
                            break;
                        case 28:
                            localavj.a(arrayOfByte2);
                            try {
                                Inet6Address localInet6Address = (Inet6Address) InetAddress
                                        .getByAddress(arrayOfByte2);
                                CastDeviceInfo localavl3 = localavm.mCastDeviceInfo_a;
                                if (localavl3.mIpV6AddrList_c == null)
                                    localavl3.mIpV6AddrList_c = new ArrayList();
                                localavl3.mIpV6AddrList_c.add(localInet6Address);
                            } catch (UnknownHostException localUnknownHostException1) {
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 33:
                            int i8 = localavj.b();
                            localavm.mCastDeviceInfo_a.mPriority_i = i8;
                            int i9 = localavj.b();
                            localavm.mCastDeviceInfo_a.mWeight_j = i9;
                            int i10 = localavj.b();
                            localavm.mCastDeviceInfo_a.mPort_h = i10;
                            String str3 = TextUtils.join(".", localavj.c());
                            localavm.mCastDeviceInfo_a.mHost_f = str3;

                            // todo

                            if (arrayOfString.length != 4) {
                                throw new IOException(
                                        "invalid name in SRV record");
                            }

                            String str4 = arrayOfString[0];
                            localavm.mCastDeviceInfo_a.e = str4;
                            String str5 = arrayOfString[1];
                            localavm.mCastDeviceInfo_a.mName_d = str5;
                            String str6 = arrayOfString[2];
                            if (str6.equals("_tcp")) {
                                localavm.setProto_a(1);
                            } else if (str6.equals("_udp")) {
                                localavm.setProto_a(2);
                            }

                            break;
                        default:
                            localavj.a(i7);
                            localavj.b = (i7 + localavj.b);
                            break;
                    }
                } else {
                    mdnsClient.onScanResults_a(localavm.mCastDeviceInfo_a);
                    break;
                }
            }
        }
    }

    private static void stopThread_a(Thread paramThread) {
        while (true)
            try {
                paramThread.interrupt();
                paramThread.join();
                return;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }

    static void handleSend_b(MdnsClient mdnsClient) {
        int j = 1;
        int k = 1000;
        MdnsClientOutputStreamHelper localavk;
        int n;
        int i1;
        int i4;

        while (!mdnsClient.mStopScan_g) {
            try {
                String str = mdnsClient.mHostName_b;
                localavk = new MdnsClientOutputStreamHelper();
                Iterator localIterator1 = mdnsClient.i.iterator();
                n = 1024;
                while (localIterator1.hasNext()) {
                    if (n <= 1024) {
                        MdnsClientPrivData localavc1 = (MdnsClientPrivData) ((SoftReference) localIterator1
                                .next()).get();
                        if (localavc1 != null) {
                            if ((System.currentTimeMillis() - localavc1.mCurrentTime_b) / 1000L <= localavc1.c / 2.0D) {
                                i1 = 0;
                            } else {
                                i1 = 1;
                            }
                            if (i1 != 0) {
                                localIterator1.remove();
                            }
                        } else {
                            localIterator1.remove();

                        }
                    } else {
                        localIterator1.remove();
                    }

                    n--;
                } // while
                localavk.b(0);
                localavk.b(0);
                localavk.b(1);
                localavk.b(mdnsClient.i.size());
                localavk.b(0);
                localavk.b(0);
                String[] arrayOfString = str.split("\\.");
                int i2 = arrayOfString.length;
                for (int i3 = 0; i3 < i2; i3++) {
                    byte[] arrayOfByte1 = arrayOfString[i3]
                            .getBytes(CharsetAndInetAddrInfo.mCharset_a);
                    localavk.a(arrayOfByte1.length);
                    localavk.a(arrayOfByte1);
                }
                localavk.a(0);
                localavk.b(12);
                if (j != 0) {
                    i4 = 32768;
                } else {
                    i4 = 0;
                }
                localavk.b(i4 | 0x1);
                Iterator localIterator2 = mdnsClient.i.iterator();
                while (localIterator2.hasNext()) {
                    MdnsClientPrivData localavc2 = (MdnsClientPrivData) ((SoftReference) localIterator2
                            .next()).get();
                    if (localavc2 != null)
                        localavk.a(localavc2.a);
                }

                DatagramPacket datagramPacket = localavk.createDatagramPacket_a();
                byte[] arrayOfByte2 = datagramPacket.getData();
                int length = datagramPacket.getLength();
                StringBuilder localStringBuilder = new StringBuilder(
                        2 * arrayOfByte2.length);
                int i6 = 0;
                int i7 = 0;
                while (i7 < length + 0) {
                    Object[] arrayOfObject1 = new Object[1];
                    arrayOfObject1[0] = Integer
                            .valueOf(0xFF & arrayOfByte2[i7]);
                    localStringBuilder.append(String.format("%02X ",
                            arrayOfObject1));
                    i6++;
                    if (i6 % 8 == 0) {
                        localStringBuilder.append('\n');
                    }

                    i7++;
                }

                Object[] arrayOfObject2 = new Object[1];
                arrayOfObject2[0] = localStringBuilder.toString();
                mLogs_a.d("packet:\n%s", arrayOfObject2);
                mdnsClient.mMulticastSocket_c.send(datagramPacket);

            } catch (IOException e) {
                e.printStackTrace();
            }

            j = 0;

            try {
                Thread.currentThread();
                Thread.sleep(k);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int m;
            if (k >= 32000) {
                m = k;
            } else {
                m = k * 2;
                k = m;
            }
        }
    }

    public final synchronized void startScan_a() {
        try {
            MulticastSocket localMulticastSocket = this.mMulticastSocket_c;
            if (localMulticastSocket != null) {
                return;
            }

            this.mStopScan_g = false;
            this.mMulticastSocket_c = new MulticastSocket();
            this.mMulticastSocket_c.setTimeToLive(1);
            if (this.mNetworkInterface_h != null)
                this.mMulticastSocket_c.setNetworkInterface(this.mNetworkInterface_h);
            // this.mReceiveThread_e = new Thread(new C_ava(this));
            this.mReceiveThread_e = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        handleReceive_a(MdnsClient.this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            });
            this.mReceiveThread_e.start();
            // this.mSendThread_f = new Thread(new C_avb(this));
            this.mSendThread_f = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    handleSend_b(MdnsClient.this);
                }

            });
            this.mSendThread_f.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract void onScanResults_a(CastDeviceInfo paramavl);

    public final synchronized void stopScan_b() {
        try {
            MulticastSocket localMulticastSocket = this.mMulticastSocket_c;
            if (localMulticastSocket == null) {
                return;
            }

            this.mStopScan_g = true;
            this.mMulticastSocket_c.close();
            if (this.mReceiveThread_e != null) {
                stopThread_a(this.mReceiveThread_e);
                this.mReceiveThread_e = null;
            }
            if (this.mSendThread_f != null) {
                stopThread_a(this.mSendThread_f);
                this.mSendThread_f = null;
            }
            this.mMulticastSocket_c = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
