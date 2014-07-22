
package tv.matchstick.server.fling.socket;

import java.nio.ByteBuffer;

final class SocketBuf {
    byte a[];
    int b;
    int c;
    int d;
    boolean e;
    ByteBuffer f[];
    ByteBuffer g[];

    public SocketBuf()
    {
        a = new byte[0x20000];
        f = new ByteBuffer[1];
        g = new ByteBuffer[2];
        d = -1;
        e = true;
    }

    final void a(byte byte0)
    {
        a[c] = byte0;
        b(1);
    }

    final void a(int i)
    {
        label0:
        {
            b = i + b;
            if (b >= a.length)
                b = b - a.length;
            if (b == c)
            {
                if (d != -1)
                    break label0;
                c = 0;
                b = 0;
                d = -1;
                e = true;
            }
            return;
        }
        e = true;
    }

    public final ByteBuffer[] a()
    {
        if (b <= c)
        {
            ByteBuffer abytebuffer1[] = f;
            abytebuffer1[0] = ByteBuffer.wrap(a, b, c - b);
            return abytebuffer1;
        } else
        {
            ByteBuffer abytebuffer[] = g;
            abytebuffer[0] = ByteBuffer.wrap(a, b, a.length - b);
            abytebuffer[1] = ByteBuffer.wrap(a, 0, c);
            return abytebuffer;
        }
    }

    final void b(int i)
    {
        c = i + c;
        if (c >= a.length)
            c = c - a.length;
        e = false;
        d = -1;
    }

    public final ByteBuffer[] b()
    {
        if (c < b)
        {
            ByteBuffer abytebuffer1[] = f;
            abytebuffer1[0] = ByteBuffer.wrap(a, c, b - c);
            return abytebuffer1;
        } else
        {
            ByteBuffer abytebuffer[] = g;
            abytebuffer[0] = ByteBuffer.wrap(a, c, a.length - c);
            abytebuffer[1] = ByteBuffer.wrap(a, 0, b);
            return abytebuffer;
        }
    }

    public final int c()
    {
        if (e)
            return a.length;
        if (c < b)
            return b - c;
        else
            return (a.length - c) + b;
    }

    public final int d()
    {
        if (e)
            return 0;
        if (b < c)
            return c - b;
        else
            return (a.length - b) + c;
    }

    public final boolean e()
    {
        return !e && b == c;
    }

    final byte f()
    {
        byte byte0 = a[b];
        a(1);
        return byte0;
    }
}
