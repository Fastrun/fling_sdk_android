
package tv.matchstick.server.fling.socket.data;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import tv.matchstick.server.common.exception.C_igs;

public final class C_igr {
    final int a;
    int b;
    final OutputStream c;
    private final byte d[];

    private C_igr(OutputStream outputstream, byte abyte0[])
    {
        c = outputstream;
        d = abyte0;
        b = 0;
        a = abyte0.length;
    }

    C_igr(byte abyte0[], int i, int j)
    {
        c = null;
        d = abyte0;
        b = i;
        a = i + j;
    }

    public static int a(int i)
    {
        if (i >= 0)
            return d(i);
        else
            return 10;
    }

    public static int a(long l)
    {
        if ((-128L & l) == 0L)
            return 1;
        if ((-16384L & l) == 0L)
            return 2;
        if ((0xffffffffffe00000L & l) == 0L)
            return 3;
        if ((0xfffffffff0000000L & l) == 0L)
            return 4;
        if ((0xfffffff800000000L & l) == 0L)
            return 5;
        if ((0xfffffc0000000000L & l) == 0L)
            return 6;
        if ((0xfffe000000000000L & l) == 0L)
            return 7;
        if ((0xff00000000000000L & l) == 0L)
            return 8;
        return (0x8000000000000000L & l) != 0L ? 10 : 9;
    }

    public static int a(BinaryPayload igp1)
    {
        return d(igp1.getLength()) + igp1.getLength();
    }

    public static int a(String s)
    {
        int i;
        int j;
        try
        {
            byte abyte0[] = s.getBytes("UTF-8");
            i = d(abyte0.length);
            j = abyte0.length;
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("UTF-8 not supported.");
        }
        return j + i;
    }

    public static C_igr a(OutputStream outputstream)
    {
        return new C_igr(outputstream, new byte[4096]);
    }

    private void a(byte abyte0[]) throws IOException
    {
        int i = abyte0.length;
        if (a - b >= i)
        {
            System.arraycopy(abyte0, 0, d, b, i);
            b = i + b;
            return;
        }
        int j = a - b;
        System.arraycopy(abyte0, 0, d, b, j);
        int k = j + 0;
        int l = i - j;
        b = a;
        b();
        if (l <= a)
        {
            System.arraycopy(abyte0, k, d, 0, l);
            b = l;
            return;
        } else
        {
            try {
                c.write(abyte0, k, l);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    public static int b(int i)
    {
        return d(C_igv.a(i, 0));
    }

    public static int b(int i, BinaryPayload igp1)
    {
        return b(i) + a(igp1);
    }

    public static int b(int i, C_igu igu1)
    {
        int j = b(i);
        int k = igu1.c();
        return j + (k + d(k));
    }

    public static int b(int i, String s)
    {
        return b(i) + a(s);
    }

    private void b() throws IOException
    {
        if (c == null)
        {
            throw new C_igs();
        } else
        {
            try {
                c.write(d, 0, b);
                b = 0;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    private void b(long l) throws IOException
    {
        do
        {
            if ((-128L & l) == 0L)
            {
                a((byte) (int) l);
                return;
            }
            a((byte) (0x80 | 0x7f & (int) l));
            l >>>= 7;
        } while (true);
    }

    private void c(long l) throws IOException
    {
        a((byte) (0xff & (int) l));
        a((byte) (0xff & (int) (l >> 8)));
        a((byte) (0xff & (int) (l >> 16)));
        a((byte) (0xff & (int) (l >> 24)));
        a((byte) (0xff & (int) (l >> 32)));
        a((byte) (0xff & (int) (l >> 40)));
        a((byte) (0xff & (int) (l >> 48)));
        a((byte) (0xff & (int) (l >> 56)));
    }

    public static int d(int i)
    {
        if ((i & 0xffffff80) == 0)
            return 1;
        if ((i & 0xffffc000) == 0)
            return 2;
        if ((0xffe00000 & i) == 0)
            return 3;
        return (0xf0000000 & i) != 0 ? 5 : 4;
    }

    public static int d(int i, int j)
    {
        return b(i) + a(j);
    }

    public static int d(int i, long l)
    {
        return b(i) + a(l);
    }

    private static int e(int i)
    {
        return i << 1 ^ i >> 31;
    }

    public static int e(int i, int j)
    {
        return b(i) + d(j);
    }

    public static int e(int i, long l)
    {
        return b(i) + a(l);
    }

    public static int f(int i, int j)
    {
        return b(i) + d(e(j));
    }

    private void g(int i, int j) throws IOException
    {
        c(C_igv.a(i, j));
    }

    public final void a() throws IOException
    {
        if (c != null)
            b();
    }

    public final void a(byte byte0) throws IOException
    {
        if (b == a)
            b();
        byte abyte0[] = d;
        int i = b;
        b = i + 1;
        abyte0[i] = byte0;
    }

    public final void a(int i, double d1) throws IOException
    {
        g(i, 1);
        c(Double.doubleToLongBits(d1));
    }

    public final void a(int i, float f1) throws IOException
    {
        g(i, 5);
        int j = Float.floatToIntBits(f1);
        a((byte) (j & 0xff));
        a((byte) (0xff & j >> 8));
        a((byte) (0xff & j >> 16));
        a((byte) (0xff & j >> 24));
    }

    public final void a(int i, int j) throws IOException
    {
        g(i, 0);
        if (j >= 0)
        {
            c(j);
            return;
        } else
        {
            b(j);
            return;
        }
    }

    public final void a(int i, long l) throws IOException
    {
        g(i, 0);
        b(l);
    }

    public final void a(int i, BinaryPayload igp1) throws IOException
    {
        g(i, 2);
        byte abyte0[] = igp1.b();
        c(abyte0.length);
        a(abyte0);
    }

    public final void a(int i, C_igu igu1) throws IOException
    {
        g(i, 2);
        a(igu1);
    }

    public final void a(int i, String s) throws IOException
    {
        g(i, 2);
        try {
            byte abyte0[] = s.getBytes("UTF-8");
            c(abyte0.length);
            a(abyte0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void a(int i, boolean flag) throws IOException
    {
        g(i, 0);
        int j = 0;
        if (flag)
            j = 1;
        a((byte) j);
    }

    public final void a(C_igu igu1) throws IOException
    {
        c(igu1.b());
        igu1.a(this);
    }

    public final void b(int i, int j) throws IOException
    {
        g(i, 0);
        c(j);
    }

    public final void b(int i, long l) throws IOException
    {
        g(i, 0);
        b(l);
    }

    public final void c(int i) throws IOException
    {
        do
        {
            if ((i & 0xffffff80) == 0)
            {
                a((byte) i);
                return;
            }
            a((byte) (0x80 | i & 0x7f));
            i >>>= 7;
        } while (true);
    }

    public final void c(int i, int j) throws IOException
    {
        g(i, 0);
        c(e(j));
    }

    public final void c(int i, long l) throws IOException
    {
        g(i, 1);
        c(l);
    }
}
