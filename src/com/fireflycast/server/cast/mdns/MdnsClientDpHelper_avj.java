
package com.fireflycast.server.cast.mdns;

import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class MdnsClientDpHelper_avj {
    final byte[] mData_a;
    int b;
    int c;
    private final int mDataLen_d;
    private final Map e;

    public MdnsClientDpHelper_avj(DatagramPacket paramDatagramPacket) {
        this.mData_a = paramDatagramPacket.getData();
        this.mDataLen_d = paramDatagramPacket.getLength();
        this.b = 0;
        this.c = -1;
        this.e = new HashMap();
    }

    private int e() {
        a(1);
        byte[] arrayOfByte = this.mData_a;
        int i = this.b;
        this.b = (i + 1);
        return 0xFF & arrayOfByte[i];
    }

    public final int a() {
        int i = this.c;
        if (c < 0) {
            i = this.mDataLen_d;
        }
        return i - this.b;
    }

    final void a(int paramInt) {
        try {
            if (a() < paramInt)
                throw new EOFException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void a(byte[] paramArrayOfByte) {
        a(paramArrayOfByte.length);
        System.arraycopy(this.mData_a, this.b, paramArrayOfByte, 0,
                paramArrayOfByte.length);
        this.b += paramArrayOfByte.length;
    }

    public final int b() {
        a(2);
        byte[] arrayOfByte1 = this.mData_a;
        int i = this.b;
        this.b = (i + 1);
        int j = (0xFF & arrayOfByte1[i]) << 8;
        byte[] arrayOfByte2 = this.mData_a;
        int k = this.b;
        this.b = (k + 1);
        return j | 0xFF & arrayOfByte2[k];
    }

    public final String[] c() {
        HashMap localHashMap = new HashMap();
        ArrayList localArrayList1 = new ArrayList();
        int i;
        int j;
        while (a() > 0) {
            a(1);
            i = this.mData_a[this.b];
            if (i == 0) {
                this.b = (1 + this.b);
                break;
            } else {
                if ((i & 0xC0) == 192) {
                    j = 1;
                } else {
                    j = 0;
                }
                int k;
                List localList;
                Object localObject;
                k = this.b;
                if (j == 0) {
                    // break label233;
                    String str = d();
                    ArrayList localArrayList2 = new ArrayList();
                    localArrayList2.add(str);
                    localObject = localArrayList2;
                } else {
                    int m = (0x3F & e()) << 8 | 0xFF & e();
                    localList = (List) this.e.get(Integer.valueOf(m));
                    try {
                        if (localList == null) {
                            throw new IOException("invalid label pointer");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    localObject = localList;
                }

                localArrayList1.addAll((Collection) localObject);
                Iterator localIterator = localHashMap.keySet().iterator();
                while (localIterator.hasNext())
                    ((List) localHashMap.get((Integer) localIterator.next()))
                            .addAll((Collection) localObject);

                localHashMap.put(Integer.valueOf(k), localObject);
                if (j == 0) {
                    continue;
                } else {
                    break;
                }
            }
        }
        this.e.putAll(localHashMap);
        return (String[]) localArrayList1.toArray(new String[localArrayList1
                .size()]);
    }

    public final String d() {
        int i = e();
        a(i);
        String str = new String(this.mData_a, this.b, i, CharsetAndInetAddrInfo_avd.mCharset_a);
        this.b = (i + this.b);
        return str;
    }
}
