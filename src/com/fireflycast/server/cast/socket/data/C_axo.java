
package com.fireflycast.server.cast.socket.data;

import java.io.IOException;

public final class C_axo extends C_igu {
    private int a;

    public C_axo()
    {
        a = -1;
    }

    public final C_igu a(C_igq igq1) throws IOException
    {
        int i = 0;
        do {
            i = igq1.a();
            switch (i) {
                case 0:
                    return this;
            }
        } while (igq1.b(i));

        return this;
    }

    public final void a(C_igr igr)
    {
    }

    public final int b()
    {
        if (a < 0)
            a = 0;
        return a;
    }

    public final int c()
    {
        a = 0;
        return 0;
    }
}
