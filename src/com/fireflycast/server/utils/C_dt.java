
package com.fireflycast.server.utils;

import java.io.PrintWriter;

public final class C_dt {
    private static final Object a = new Object();
    private static char b[] = new char[24];

    private static int a(long l, int i)
    {
        if (b.length < 0)
            b = new char[0];
        char ac[] = b;
        if (l == 0L)
        {
            ac[0] = '0';
            return 1;
        }
        char c;
        int j;
        int k;
        int i1;
        int j1;
        int k1;
        int l1;
        int i2;
        int j2;
        boolean flag;
        int k2;
        boolean flag1;
        int l2;
        boolean flag2;
        int i3;
        if (l > 0L)
        {
            c = '+';
        } else
        {
            l = -l;
            c = '-';
        }
        j = (int) (l % 1000L);
        k = (int) Math.floor(l / 1000L);
        i1 = 0;
        if (k > 0x15180)
        {
            i1 = k / 0x15180;
            k -= 0x15180 * i1;
        }
        if (k > 3600)
        {
            int l3 = k / 3600;
            int i4 = k - l3 * 3600;
            k1 = l3;
            j1 = i4;
        } else
        {
            j1 = k;
            k1 = 0;
        }
        if (j1 > 60)
        {
            int j3 = j1 / 60;
            int k3 = j1 - j3 * 60;
            i2 = j3;
            l1 = k3;
        } else
        {
            l1 = j1;
            i2 = 0;
        }
        ac[0] = c;
        j2 = a(ac, i1, 'd', 1, false, 0);
        if (j2 != 1)
            flag = true;
        else
            flag = false;
        k2 = a(ac, k1, 'h', j2, flag, 0);
        if (k2 != 1)
            flag1 = true;
        else
            flag1 = false;
        l2 = a(ac, i2, 'm', k2, flag1, 0);
        if (l2 != 1)
            flag2 = true;
        else
            flag2 = false;
        i3 = a(ac, j, 'm', a(ac, l1, 's', l2, flag2, 0), true, 0);
        ac[i3] = 's';
        return i3 + 1;
    }

    private static int a(char ac[], int i, char c, int j, boolean flag, int k)
    {
        if (flag || i > 0)
        {
            int l;
            int i1;
            int k1;
            if (flag && k >= 3 || i > 99)
            {
                int l1 = i / 100;
                ac[j] = (char) (l1 + 48);
                l = j + 1;
                i1 = i - l1 * 100;
            } else
            {
                l = j;
                i1 = i;
            }
            if (flag && k >= 2 || i1 > 9 || j != l)
            {
                int j1 = i1 / 10;
                ac[l] = (char) (j1 + 48);
                l++;
                i1 -= j1 * 10;
            }
            ac[l] = (char) (i1 + 48);
            k1 = l + 1;
            ac[k1] = c;
            j = k1 + 1;
        }
        return j;
    }

    public static void a(long l, long l1, PrintWriter printwriter)
    {
        if (l == 0L)
        {
            printwriter.print("--");
            return;
        } else
        {
            b(l - l1, printwriter);
            return;
        }
    }

    public static void a(long l, PrintWriter printwriter)
    {
        b(l, printwriter);
    }

    public static void a(long l, StringBuilder stringbuilder)
    {
        synchronized (a)
        {
            int i = a(l, 0);
            stringbuilder.append(b, 0, i);
        }
    }

    private static void b(long l, PrintWriter printwriter)
    {
        synchronized (a)
        {
            int i = a(l, 0);
            printwriter.print(new String(b, 0, i));
        }
    }
}
