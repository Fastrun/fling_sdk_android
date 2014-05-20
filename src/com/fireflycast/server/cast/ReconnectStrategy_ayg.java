
package com.fireflycast.server.cast;

import com.fireflycast.server.utils.Logs_avu;

import android.os.SystemClock;

final class ReconnectStrategy_ayg {
    private static final Logs_avu e = new Logs_avu("ReconnectStrategy");
    private final long a;
    private final long b;
    private long c;
    private long d;

    public ReconnectStrategy_ayg()
    {
        this((byte) 0);
    }

    private ReconnectStrategy_ayg(byte byte0)
    {
        a = 3000L;
        b = 15000L;
    }

    public final void a()
    {
        long l = SystemClock.elapsedRealtime();
        c = l;
        d = l;
    }

    public final boolean b()
    {
        boolean flag;
        if (d != 0L)
            flag = true;
        else
            flag = false;
        d = 0L;
        c = 0L;
        return flag;
    }

    public final boolean wasReconnecting_c()
    {
        return d != 0L;
    }

    public final long d()
    {
        long l = -1L;
        if (d != 0L)
        {
            long l1 = SystemClock.elapsedRealtime();
            if (c == 0L)
                return 0L;
            if (l1 - d >= b)
            {
                d = 0L;
                return l;
            }
            l = a - (l1 - c);
            if (l <= 0L)
                return 0L;
        }
        return l;
    }

    public final void e()
    {
        if (d != 0L)
            c = SystemClock.elapsedRealtime();
    }
}
