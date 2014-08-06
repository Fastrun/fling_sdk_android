
package tv.matchstick.server.fling.socket.data;

import java.io.IOException;

public abstract class C_igu {
    public C_igu()
    {
    }

    public final byte[] build()
    {
        byte abyte0[] = new byte[c()];
        a(abyte0, 0, abyte0.length);
        return abyte0;
    }

    public abstract C_igu a(C_igq igq1) throws IOException;

    public abstract void a(C_igr igr1) throws IOException;

    public final void a(byte abyte0[], int i, int j)
    {
        try
        {
            C_igr igr1 = new C_igr(abyte0, i, j);
            a(igr1);
            if (igr1.c == null)
            {
                if (igr1.a - igr1.b != 0)
                    throw new IllegalStateException("Did not write as much data as expected.");
                return;
            }
        }
        // catch (IOException ioexception)
        // {
        // throw new
        // RuntimeException("Serializing to a byte array threw an IOException (should never happen).");
        // }
        catch (Exception e) {
            e.printStackTrace();
            // throw new
            // RuntimeException("Serializing to a byte array threw an IOException (should never happen).");
        }
        throw new UnsupportedOperationException(
                "spaceLeft() can only be called on CodedOutputStreams that are writing to a flat array.");
    }

    public abstract int b();

    public final C_igu b(byte abyte0[])
    {
        return b(abyte0, 0, abyte0.length);
    }

    public final C_igu b(byte abyte0[], int i, int j)
    {
        try
        {
            C_igq igq1 = new C_igq(abyte0, i, j);
            a(igq1);
            igq1.a(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // catch (C_igt igt1)
        // {
        // throw igt1;
        // }
        // catch (IOException ioexception)
        // {
        // throw new
        // RuntimeException("Reading from a byte array threw an IOException (should never happen).");
        // }
        return this;
    }

    public abstract int c();
}
