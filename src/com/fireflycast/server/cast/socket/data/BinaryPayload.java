
package com.fireflycast.server.cast.socket.data;

import java.io.UnsupportedEncodingException;

public final class BinaryPayload {
    public static final BinaryPayload mInstance_a = new BinaryPayload(new byte[0]);
    private final byte b[];
    private volatile int c;

    private BinaryPayload(byte abyte0[])
    {
        c = 0;
        b = abyte0;
    }

    public static BinaryPayload a(String s)
    {
        BinaryPayload igp1;
        try
        {
            igp1 = new BinaryPayload(s.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            throw new RuntimeException("UTF-8 not supported?");
        }
        return igp1;
    }

    public static BinaryPayload a(byte abyte0[])
    {
        return a(abyte0, 0, abyte0.length);
    }

    public static BinaryPayload a(byte abyte0[], int i, int j)
    {
        byte abyte1[] = new byte[j];
        System.arraycopy(abyte0, i, abyte1, 0, j);
        return new BinaryPayload(abyte1);
    }

    public final int getLength_a()
    {
        return b.length;
    }

    public final void copy_b(byte abyte0[])
    {
        System.arraycopy(b, 0, abyte0, 0, b.length);
    }

    public final byte[] b()
    {
        int i = b.length;
        byte abyte0[] = new byte[i];
        System.arraycopy(b, 0, abyte0, 0, i);
        return abyte0;
    }

    public final boolean equals(Object obj)
    {
        if (obj != this)
        {
            if (!(obj instanceof BinaryPayload))
                return false;
            BinaryPayload igp1 = (BinaryPayload) obj;
            int i = b.length;
            if (i != igp1.b.length)
                return false;
            byte abyte0[] = b;
            byte abyte1[] = igp1.b;
            int j = 0;
            while (j < i)
            {
                if (abyte0[j] != abyte1[j])
                    return false;
                j++;
            }
        }
        return true;
    }

    public final int hashCode()
    {
        int i = c;
        if (i == 0)
        {
            byte abyte0[] = b;
            int j = b.length;
            int k = 0;
            int l;
            for (i = j; k < j; i = l)
            {
                l = i * 31 + abyte0[k];
                k++;
            }

            if (i == 0)
                i = 1;
            c = i;
        }
        return i;
    }
}
