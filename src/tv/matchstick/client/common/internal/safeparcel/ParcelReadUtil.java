
package tv.matchstick.client.common.internal.safeparcel;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * OK
 */
public class ParcelReadUtil {

    public static int readInt_n(Parcel paramParcel) {
        return paramParcel.readInt();
    }

    public static int S(int paramInt) {
        return (paramInt & 0xFFFF);
    }

    public static int a(Parcel paramParcel, int paramInt) {
        if ((paramInt & 0xFFFF0000) != -65536)
            return (paramInt >> 16 & 0xFFFF);
        return paramParcel.readInt();
    }

    public static void b(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        paramParcel.setDataPosition(paramParcel.dataPosition() + i);
    }

    private static void a(Parcel paramParcel, int paramInt1, int paramInt2) {
        int i = a(paramParcel, paramInt1);
        if (i == paramInt2)
            return;
        throw new SafeParcelA("Expected size " + paramInt2 + " got " + i
                + " (0x" + Integer.toHexString(i) + ")", paramParcel);
    }

    private static void a(Parcel paramParcel, int paramInt1, int paramInt2,
            int paramInt3) {
        if (paramInt2 == paramInt3)
            return;
        throw new SafeParcelA("Expected size " + paramInt3 + " got "
                + paramInt2 + " (0x" + Integer.toHexString(paramInt2) + ")",
                paramParcel);
    }

    public static int o(Parcel paramParcel) {
        int i = readInt_n(paramParcel);
        int j = a(paramParcel, i);
        int k = paramParcel.dataPosition();
        if (S(i) != 20293)
            throw new SafeParcelA("Expected object header. Got 0x"
                    + Integer.toHexString(i), paramParcel);
        int l = k + j;
        if ((l < k) || (l > paramParcel.dataSize()))
            throw new SafeParcelA("Size read is invalid start=" + k + " end="
                    + l, paramParcel);
        return l;
    }

    public static boolean c(Parcel paramParcel, int paramInt) {
        a(paramParcel, paramInt, 4);
        return (paramParcel.readInt() != 0);
    }

    public static Boolean d(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        if (i == 0)
            return null;
        a(paramParcel, paramInt, i, 4);
        return Boolean.valueOf(paramParcel.readInt() != 0);
    }

    public static byte e(Parcel paramParcel, int paramInt) {
        a(paramParcel, paramInt, 4);
        return (byte) paramParcel.readInt();
    }

    public static short f(Parcel paramParcel, int paramInt) {
        a(paramParcel, paramInt, 4);
        return (short) paramParcel.readInt();
    }

    public static int g(Parcel paramParcel, int paramInt) {
        a(paramParcel, paramInt, 4);
        return paramParcel.readInt();
    }

    public static long h(Parcel paramParcel, int paramInt) {
        a(paramParcel, paramInt, 8);
        return paramParcel.readLong();
    }

    public static BigInteger i(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        byte[] arrayOfByte = paramParcel.createByteArray();
        paramParcel.setDataPosition(j + i);
        return new BigInteger(arrayOfByte);
    }

    public static float j(Parcel paramParcel, int paramInt) {
        a(paramParcel, paramInt, 4);
        return paramParcel.readFloat();
    }

    public static double k(Parcel paramParcel, int paramInt) {
        a(paramParcel, paramInt, 8);
        return paramParcel.readDouble();
    }

    public static BigDecimal l(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        byte[] arrayOfByte = paramParcel.createByteArray();
        int k = paramParcel.readInt();
        paramParcel.setDataPosition(j + i);
        return new BigDecimal(new BigInteger(arrayOfByte), k);
    }

    public static String m(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        String str = paramParcel.readString();
        paramParcel.setDataPosition(j + i);
        return str;
    }

    public static IBinder n(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        IBinder localIBinder = paramParcel.readStrongBinder();
        paramParcel.setDataPosition(j + i);
        return localIBinder;
    }

    public static <T extends Parcelable> T a(Parcel paramParcel, int paramInt,
            Parcelable.Creator<T> paramCreator) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        T localParcelable = paramCreator.createFromParcel(paramParcel);
        paramParcel.setDataPosition(j + i);

        return localParcelable;
    }

    public static Bundle o(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        Bundle localBundle = paramParcel.readBundle();
        paramParcel.setDataPosition(j + i);
        return localBundle;
    }

    public static byte[] p(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        byte[] arrayOfByte = paramParcel.createByteArray();
        paramParcel.setDataPosition(j + i);
        return arrayOfByte;
    }

    public static boolean[] q(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        boolean[] arrayOfBoolean = paramParcel.createBooleanArray();
        paramParcel.setDataPosition(j + i);
        return arrayOfBoolean;
    }

    public static int[] r(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        int[] arrayOfInt = paramParcel.createIntArray();
        paramParcel.setDataPosition(j + i);
        return arrayOfInt;
    }

    public static long[] s(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        long[] arrayOfLong = paramParcel.createLongArray();
        paramParcel.setDataPosition(j + i);
        return arrayOfLong;
    }

    public static BigInteger[] t(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        int k = paramParcel.readInt();
        BigInteger[] arrayOfBigInteger = new BigInteger[k];
        for (int l = 0; l < k; ++l)
            arrayOfBigInteger[l] = new BigInteger(paramParcel.createByteArray());
        paramParcel.setDataPosition(j + i);
        return arrayOfBigInteger;
    }

    public static float[] u(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        float[] arrayOfFloat = paramParcel.createFloatArray();
        paramParcel.setDataPosition(j + i);
        return arrayOfFloat;
    }

    public static double[] v(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        double[] arrayOfDouble = paramParcel.createDoubleArray();
        paramParcel.setDataPosition(j + i);
        return arrayOfDouble;
    }

    public static BigDecimal[] w(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        int k = paramParcel.readInt();
        BigDecimal[] arrayOfBigDecimal = new BigDecimal[k];
        for (int l = 0; l < k; ++l) {
            byte[] arrayOfByte = paramParcel.createByteArray();
            int i1 = paramParcel.readInt();
            arrayOfBigDecimal[l] = new BigDecimal(new BigInteger(arrayOfByte),
                    i1);
        }
        paramParcel.setDataPosition(j + i);
        return arrayOfBigDecimal;
    }

    public static String[] x(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        String[] arrayOfString = paramParcel.createStringArray();
        paramParcel.setDataPosition(j + i);
        return arrayOfString;
    }

    public static ArrayList<String> y(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        ArrayList<String> localArrayList = paramParcel.createStringArrayList();
        paramParcel.setDataPosition(j + i);
        return localArrayList;
    }

    public static <T> T[] b(Parcel paramParcel, int paramInt,
            Parcelable.Creator<T> paramCreator) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        T[] arrayOfObject = paramParcel.createTypedArray(paramCreator);
        paramParcel.setDataPosition(j + i);
        return arrayOfObject;
    }

    public static <T> ArrayList<T> c(Parcel paramParcel, int paramInt,
            Parcelable.Creator<T> paramCreator) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        ArrayList<T> localArrayList = paramParcel
                .createTypedArrayList(paramCreator);
        paramParcel.setDataPosition(j + i);
        return localArrayList;
    }

    public static Parcel z(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        Parcel localParcel = Parcel.obtain();
        localParcel.appendFrom(paramParcel, j, i);
        paramParcel.setDataPosition(j + i);
        return localParcel;
    }

    public static Parcel[] A(Parcel paramParcel, int paramInt) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return null;
        int k = paramParcel.readInt();
        Parcel[] arrayOfParcel = new Parcel[k];
        for (int l = 0; l < k; ++l) {
            int i1 = paramParcel.readInt();
            if (i1 != 0) {
                int i2 = paramParcel.dataPosition();
                Parcel localParcel = Parcel.obtain();
                localParcel.appendFrom(paramParcel, i2, i1);
                arrayOfParcel[l] = localParcel;
                paramParcel.setDataPosition(i2 + i1);
            } else {
                arrayOfParcel[l] = null;
            }
        }
        paramParcel.setDataPosition(j + i);
        return arrayOfParcel;
    }

    public static void a(Parcel paramParcel, int paramInt, List paramList,
            ClassLoader paramClassLoader) {
        int i = a(paramParcel, paramInt);
        int j = paramParcel.dataPosition();
        if (i == 0)
            return;
        paramParcel.readList(paramList, paramClassLoader);
        paramParcel.setDataPosition(j + i);
    }

    public static class SafeParcelA extends RuntimeException {
        public SafeParcelA(String paramString, Parcel paramParcel) {
            super(paramString + " Parcel: pos=" + paramParcel.dataPosition()
                    + " size=" + paramParcel.dataSize());
        }
    }

}
