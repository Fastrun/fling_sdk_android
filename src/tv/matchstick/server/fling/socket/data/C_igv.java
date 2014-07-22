
package tv.matchstick.server.fling.socket.data;

public final class C_igv {
    static final int a = 11;
    static final int b = 12;
    static final int c = 16;
    static final int d = 26;

    static int a(int i)
    {
        return i & 7;
    }

    static int a(int i, int j)
    {
        return j | i << 3;
    }

    public static int b(int i)
    {
        return i >>> 3;
    }

}
