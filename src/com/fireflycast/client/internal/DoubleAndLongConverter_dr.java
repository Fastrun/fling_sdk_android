
package com.fireflycast.client.internal;

public class DoubleAndLongConverter_dr {

    public static <T> boolean compare_a(T paramT1, T paramT2) {
        return (((paramT1 == null) && (paramT2 == null)) || ((paramT1 != null)
                && (paramT2 != null) && (paramT1.equals(paramT2))));
    }

    public static double long2double_l(long l) {
        return (l / 1000.0D);
    }

    public static long double2long_b(double d) {
        return (long) (d * 1000.0D);
    }
}
