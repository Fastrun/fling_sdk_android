
package tv.matchstick.client.internal;

public class DoubleAndLongConverter {

    public static <T> boolean compare(T paramT1, T paramT2) {
        return (((paramT1 == null) && (paramT2 == null)) || ((paramT1 != null)
                && (paramT2 != null) && (paramT1.equals(paramT2))));
    }

    public static double long2double(long l) {
        return (l / 1000.0D);
    }

    public static long double2long(double d) {
        return (long) (d * 1000.0D);
    }
}
