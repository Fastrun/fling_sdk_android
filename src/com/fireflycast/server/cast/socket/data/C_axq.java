
package com.fireflycast.server.cast.socket.data;

import java.io.IOException;

public final class C_axq extends C_igu {
    private boolean a;
    private BinaryPayload_igp b;
    private boolean c;
    private BinaryPayload_igp d;
    private int e;

    public C_axq()
    {
        b = BinaryPayload_igp.mInstance_a;
        d = BinaryPayload_igp.mInstance_a;
        e = -1;
    }

    public final BinaryPayload_igp a()
    {
        return b;
    }

    public final C_igu a(C_igq igq1) throws IOException
    {
        do
        {
            int i = igq1.a();
            switch (i)
            {
                default:
                    if (igq1.b(i))
                        continue;
                    // fall through

                case 0: // '\0'
                    return this;

                case 10: // '\n'
                    BinaryPayload_igp igp2 = igq1.f();
                    a = true;
                    b = igp2;
                    break;

                case 18: // '\022'
                    BinaryPayload_igp igp1 = igq1.f();
                    c = true;
                    d = igp1;
                    break;
            }
        } while (true);
    }

    public final void a(C_igr igr1) throws IOException
    {
        if (a)
            igr1.a(1, b);
        if (c)
            igr1.a(2, d);
    }

    public final int b()
    {
        if (e < 0)
            c();
        return e;
    }

    public final int c()
    {
        boolean flag = a;
        int i = 0;
        if (flag)
            i = 0 + C_igr.b(1, b);
        if (c)
            i += C_igr.b(2, d);
        e = i;
        return i;
    }

    public final BinaryPayload_igp d()
    {
        return d;
    }
}
