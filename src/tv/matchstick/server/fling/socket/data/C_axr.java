
package tv.matchstick.server.fling.socket.data;

import java.io.IOException;

public final class C_axr extends C_igu {
    private boolean a;
    private C_axo b;
    private boolean c;
    private C_axq d;
    private boolean e;
    private C_axp f;
    private int g;

    public C_axr()
    {
        b = null;
        d = null;
        f = null;
        g = -1;
    }

    public static C_axr a(byte abyte0[])
    {
        return (C_axr) (new C_axr()).b(abyte0);
    }

    public final C_axo a()
    {
        return b;
    }

    public final C_axr a(C_axo axo1)
    {
        if (axo1 == null)
        {
            throw new NullPointerException();
        } else
        {
            a = true;
            b = axo1;
            return this;
        }
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
                    C_axo axo1 = new C_axo();
                    igq1.a(axo1);
                    a(axo1);
                    break;

                case 18: // '\022'
                    C_axq axq1 = new C_axq();
                    igq1.a(axq1);
                    c = true;
                    d = axq1;
                    break;

                case 26: // '\032'
                    C_axp axp1 = new C_axp();
                    igq1.a(axp1);
                    e = true;
                    f = axp1;
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
        if (e)
            igr1.a(3, f);
    }

    public final int b()
    {
        if (g < 0)
            c();
        return g;
    }

    public final int c()
    {
        boolean flag = a;
        int i = 0;
        if (flag)
            i = 0 + C_igr.b(1, b);
        if (c)
            i += C_igr.b(2, d);
        if (e)
            i += C_igr.b(3, f);
        g = i;
        return i;
    }

    public final C_axq d()
    {
        return d;
    }

    public final C_axp e()
    {
        return f;
    }
}
