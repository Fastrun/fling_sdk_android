
package com.fireflycast.server.cast.socket.data;

import java.io.IOException;

public final class C_axp extends C_igu {
    private boolean a;
    private int b;
    private int c;

    public C_axp()
    {
        b = 0;
        c = -1;
    }

    public final int a()
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

                case 8: // '\b'
                    int j = igq1.h();
                    a = true;
                    b = j;
                    break;
            }
        } while (true);
    }

    public final void a(C_igr igr1) throws IOException
    {
        if (a)
            igr1.a(1, b);
    }

    public final int b()
    {
        if (c < 0)
            c();
        return c;
    }

    public final int c()
    {
        boolean flag = a;
        int i = 0;
        if (flag)
            i = 0 + C_igr.d(1, b);
        c = i;
        return i;
    }
}
