
package com.fireflycast.server.cast.socket.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import com.fireflycast.server.common.exception.C_igt;

public final class C_igq {
    private final byte a[];
    private int b;
    private int c;
    private int d;
    private final InputStream e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;
    private int k;

    public C_igq(InputStream inputstream)
    {
        h = 0x7fffffff;
        j = 64;
        k = 0x4000000;
        a = new byte[4096];
        b = 0;
        d = 0;
        e = inputstream;
    }

    C_igq(byte abyte0[], int i1, int j1)
    {
        h = 0x7fffffff;
        j = 64;
        k = 0x4000000;
        a = abyte0;
        b = i1 + j1;
        d = i1;
        e = null;
    }

    private boolean refillBuffer_a(boolean flag) throws IOException
    {
        if (d < b)
            throw new IllegalStateException("refillBuffer() called when buffer wasn't empty.");
        if (g + b == h)
            if (flag)
                throw C_igt.a();
            else
                return false;
        g = g + b;
        d = 0;
        int i1;
        if (e == null)
            i1 = -1;
        else
            i1 = e.read(a);
        b = i1;
        if (b == 0 || b < -1)
            throw new IllegalStateException((new StringBuilder(
                    "InputStream#read(byte[]) returned invalid result: ")).append(b)
                    .append("\nThe InputStream implementation is buggy.").toString());
        if (b == -1)
        {
            b = 0;
            if (flag)
                throw C_igt.a();
            else
                return false;
        }
        m();
        int j1 = g + b + c;
        if (j1 > k || j1 < 0)
            throw C_igt.h();
        else
            return true;
    }

    private void d(int i1) throws IOException
    {
        if (i1 < 0)
            throw C_igt.b();
        if (i1 + (g + d) > h)
        {
            d(h - g - d);
            throw C_igt.a();
        }
        if (i1 <= b - d)
        {
            d = i1 + d;
        } else
        {
            int j1 = b - d;
            g = g + b;
            d = 0;
            b = 0;
            int k1 = j1;
            while (k1 < i1)
            {
                int l1;
                if (e == null)
                    l1 = -1;
                else
                    l1 = (int) e.skip(i1 - k1);
                if (l1 <= 0)
                    throw C_igt.a();
                k1 += l1;
                g = l1 + g;
            }
        }
    }

    private void m()
    {
        b = b + c;
        int i1 = g + b;
        if (i1 > h)
        {
            c = i1 - h;
            b = b - c;
        } else
        {
            c = 0;
        }
    }

    public final int a() throws IOException
    {
        boolean flag;
        if (d == b && !refillBuffer_a(false))
            flag = true;
        else
            flag = false;
        if (flag)
        {
            f = 0;
            return 0;
        }
        f = h();
        if (f == 0)
            throw C_igt.d();
        else
            return f;
    }

    public final void a(int i1) throws IOException
    {
        if (f != i1)
            throw C_igt.e();
        else
            return;
    }

    public final void a(C_igu igu1) throws IOException
    {
        int i1 = h();
        if (i >= j)
            throw C_igt.g();
        if (i1 < 0)
            throw C_igt.b();
        int j1 = i1 + (g + d);
        int k1 = h;
        if (j1 > k1)
        {
            throw C_igt.a();
        } else
        {
            h = j1;
            m();
            i = 1 + i;
            igu1.a(this);
            a(0);
            i = -1 + i;
            h = k1;
            m();
            return;
        }
    }

    public final long b() throws IOException
    {
        return i();
    }

    public final boolean b(int i1) throws IOException
    {
        switch (C_igv.a(i1))
        {
            default:
                throw C_igt.f();

            case 0: // '\0'
                h();
                return true;

            case 1: // '\001'
                k();
                return true;

            case 2: // '\002'
                d(h());
                return true;

            case 3: // '\003'
                int j1;
                do
                    j1 = a();
                while (j1 != 0 && b(j1));
                a(C_igv.a(C_igv.b(i1), 4));
                return true;

            case 4: // '\004'
                return false;

            case 5: // '\005'
                j();
                return true;
        }
    }

    public final int c() throws IOException
    {
        return h();
    }

    public final byte[] c(int i1) throws IOException
    {
        if (i1 < 0)
            throw C_igt.b();
        if (i1 + (g + d) > h)
        {
            d(h - g - d);
            throw C_igt.a();
        }
        if (i1 <= b - d)
        {
            byte abyte4[] = new byte[i1];
            System.arraycopy(a, d, abyte4, 0, i1);
            d = i1 + d;
            return abyte4;
        }
        if (i1 < 4096)
        {
            byte abyte3[] = new byte[i1];
            int l3 = b - d;
            System.arraycopy(a, d, abyte3, 0, l3);
            d = b;
            refillBuffer_a(true);
            while (i1 - l3 > b)
            {
                System.arraycopy(a, 0, abyte3, l3, b);
                l3 += b;
                d = b;
                refillBuffer_a(true);
            }
            System.arraycopy(a, 0, abyte3, l3, i1 - l3);
            d = i1 - l3;
            return abyte3;
        }
        int j1 = d;
        int k1 = b;
        g = g + b;
        d = 0;
        b = 0;
        int l1 = i1 - (k1 - j1);
        Vector vector = new Vector();
        int j3;
        for (int i2 = l1; i2 > 0; i2 = j3)
        {
            byte abyte2[] = new byte[Math.min(i2, 4096)];
            int k3;
            for (int i3 = 0; i3 < abyte2.length; i3 += k3)
            {
                if (e == null)
                    k3 = -1;
                else
                    k3 = e.read(abyte2, i3, abyte2.length - i3);
                if (k3 == -1)
                    throw C_igt.a();
                g = k3 + g;
            }

            j3 = i2 - abyte2.length;
            vector.addElement(abyte2);
        }

        byte abyte0[] = new byte[i1];
        int j2 = k1 - j1;
        System.arraycopy(a, j1, abyte0, 0, j2);
        int k2 = 0;
        int l2 = j2;
        for (; k2 < vector.size(); k2++)
        {
            byte abyte1[] = (byte[]) vector.elementAt(k2);
            System.arraycopy(abyte1, 0, abyte0, l2, abyte1.length);
            l2 += abyte1.length;
        }

        return abyte0;
    }

    public final boolean d() throws IOException
    {
        return h() != 0;
    }

    public final String e() throws UnsupportedEncodingException, IOException
    {
        int i1 = h();
        if (i1 <= b - d && i1 > 0)
        {
            String s = new String(a, d, i1, "UTF-8");
            d = i1 + d;
            return s;
        } else
        {
            return new String(c(i1), "UTF-8");
        }
    }

    public final BinaryPayload f() throws IOException
    {
        int i1 = h();
        if (i1 <= b - d && i1 > 0)
        {
            BinaryPayload igp1 = BinaryPayload.a(a, d, i1);
            d = i1 + d;
            return igp1;
        } else
        {
            return BinaryPayload.a(c(i1));
        }
    }

    public final int g() throws IOException
    {
        int i1 = h();
        return i1 >>> 1 ^ -(i1 & 1);
    }

    public final int h() throws IOException
    {
        int i1 = l();
        if (i1 >= 0) {
            return i1;
        }

        int j1 = i1 & 0x7f;
        byte byte0 = l();
        if (byte0 >= 0)
            return j1 | byte0 << 7;
        int k1 = j1 | (byte0 & 0x7f) << 7;
        byte byte1 = l();
        if (byte1 >= 0)
            return k1 | byte1 << 14;
        int l1 = k1 | (byte1 & 0x7f) << 14;
        byte byte2 = l();
        if (byte2 >= 0)
            return l1 | byte2 << 21;
        int i2 = l1 | (byte2 & 0x7f) << 21;
        byte byte3 = l();
        i1 = i2 | byte3 << 28;
        if (byte3 < 0)
        {
            int j2 = 0;
            do
            {
                if (j2 >= 5) {
                    throw C_igt.c();
                }
                if (l() >= 0) {
                    return i1;
                }
                j2++;
            } while (true);
        }

        return i1;
    }

    public final long i() throws IOException
    {
        int i1 = 0;
        long l1 = 0L;
        for (; i1 < 64; i1 += 7)
        {
            byte byte0 = l();
            l1 |= (long) (byte0 & 0x7f) << i1;
            if ((byte0 & 0x80) == 0)
                return l1;
        }

        throw C_igt.c();
    }

    public final int j() throws IOException
    {
        byte byte0 = l();
        byte byte1 = l();
        byte byte2 = l();
        byte byte3 = l();
        return byte0 & 0xff | (byte1 & 0xff) << 8 | (byte2 & 0xff) << 16 | (byte3 & 0xff) << 24;
    }

    public final long k() throws IOException
    {
        int i1 = l();
        int j1 = l();
        int k1 = l();
        int l1 = l();
        int i2 = l();
        int j2 = l();
        int k2 = l();
        int l2 = l();
        return 255L & (long) i1 | (255L & (long) j1) << 8 | (255L & (long) k1) << 16
                | (255L & (long) l1) << 24 | (255L & (long) i2) << 32 | (255L & (long) j2) << 40
                | (255L & (long) k2) << 48 | (255L & (long) l2) << 56;
    }

    public final byte l() throws IOException
    {
        if (d == b)
            refillBuffer_a(true);
        byte abyte0[] = a;
        int i1 = d;
        d = i1 + 1;
        return abyte0[i1];
    }
}
