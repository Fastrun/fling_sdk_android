
package com.fireflycast.server.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.server.common.exception.CastRuntimeException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public final class ParcelReader {
    public static int a(Parcel parcel)
    {
        int i1 = parcel.readInt();
        int j1 = a(parcel, i1);
        int start = parcel.dataPosition();
        if ((0xffff & i1) != 20293)
            throw new CastRuntimeException(
                    (new StringBuilder("Expected object header. Got 0x")).append(
                            Integer.toHexString(i1)).toString(), parcel);
        int end = start + j1;
        if (end < start || end > parcel.dataSize())
            throw new CastRuntimeException((new StringBuilder("Size read is invalid start="))
                    .append(start).append(" end=").append(end).toString(), parcel);
        else
            return end;
    }

    public static int a(Parcel parcel, int i1)
    {
        if ((i1 & 0xffff0000) != 0xffff0000)
            return 0xffff & i1 >> 16;
        else
            return parcel.readInt();
    }

    public static Parcelable a(Parcel parcel, int i1, android.os.Parcelable.Creator creator)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            Parcelable parcelable = (Parcelable) creator.createFromParcel(parcel);
            parcel.setDataPosition(j1 + k1);
            return parcelable;
        }
    }

    public static void a(Parcel parcel, int i1, int expectedSize)
    {
        int k1 = a(parcel, i1);
        if (k1 != expectedSize)
            throw new CastRuntimeException((new StringBuilder("Expected size "))
                    .append(expectedSize).append(" got ").append(k1).append(" (0x")
                    .append(Integer.toHexString(k1)).append(")").toString(), parcel);
        else
            return;
    }

    public static void b(Parcel parcel, int i1)
    {
        parcel.setDataPosition(a(parcel, i1) + parcel.dataPosition());
    }

    public static Object[] b(Parcel parcel, int i1, android.os.Parcelable.Creator creator)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            Object aobj[] = parcel.createTypedArray(creator);
            parcel.setDataPosition(j1 + k1);
            return aobj;
        }
    }

    public static ArrayList c(Parcel parcel, int i1, android.os.Parcelable.Creator creator)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            ArrayList arraylist = parcel.createTypedArrayList(creator);
            parcel.setDataPosition(j1 + k1);
            return arraylist;
        }
    }

    public static boolean c(Parcel parcel, int i1)
    {
        a(parcel, i1, 4);
        return parcel.readInt() != 0;
    }

    public static Boolean d(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        if (j1 == 0)
            return null;
        if (j1 != 4)
            throw new CastRuntimeException((new StringBuilder("Expected size 4"))
                    .append(" got ").append(j1).append(" (0x").append(Integer.toHexString(j1))
                    .append(")").toString(), parcel);
        boolean flag;
        if (parcel.readInt() != 0)
            flag = true;
        else
            flag = false;
        return Boolean.valueOf(flag);
    }

    public static byte e(Parcel parcel, int i1)
    {
        a(parcel, i1, 4);
        return (byte) parcel.readInt();
    }

    public static int f(Parcel parcel, int i1)
    {
        a(parcel, i1, 4);
        return parcel.readInt();
    }

    public static long g(Parcel parcel, int i1)
    {
        a(parcel, i1, 8);
        return parcel.readLong();
    }

    public static BigInteger h(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            byte abyte0[] = parcel.createByteArray();
            parcel.setDataPosition(j1 + k1);
            return new BigInteger(abyte0);
        }
    }

    public static float i(Parcel parcel, int i1)
    {
        a(parcel, i1, 4);
        return parcel.readFloat();
    }

    public static double j(Parcel parcel, int i1)
    {
        a(parcel, i1, 8);
        return parcel.readDouble();
    }

    public static BigDecimal k(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            byte abyte0[] = parcel.createByteArray();
            int l1 = parcel.readInt();
            parcel.setDataPosition(j1 + k1);
            return new BigDecimal(new BigInteger(abyte0), l1);
        }
    }

    public static String l(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            String s1 = parcel.readString();
            parcel.setDataPosition(j1 + k1);
            return s1;
        }
    }

    public static IBinder m(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            IBinder ibinder = parcel.readStrongBinder();
            parcel.setDataPosition(j1 + k1);
            return ibinder;
        }
    }

    public static Bundle n(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            Bundle bundle = parcel.readBundle();
            parcel.setDataPosition(j1 + k1);
            return bundle;
        }
    }

    public static byte[] o(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            byte abyte0[] = parcel.createByteArray();
            parcel.setDataPosition(j1 + k1);
            return abyte0;
        }
    }

    public static int[] p(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            int ai[] = parcel.createIntArray();
            parcel.setDataPosition(j1 + k1);
            return ai;
        }
    }

    public static float[] q(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            float af[] = parcel.createFloatArray();
            parcel.setDataPosition(j1 + k1);
            return af;
        }
    }

    public static BigDecimal[] r(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
            return null;
        int l1 = parcel.readInt();
        BigDecimal abigdecimal[] = new BigDecimal[l1];
        for (int i2 = 0; i2 < l1; i2++)
        {
            byte abyte0[] = parcel.createByteArray();
            int j2 = parcel.readInt();
            abigdecimal[i2] = new BigDecimal(new BigInteger(abyte0), j2);
        }

        parcel.setDataPosition(k1 + j1);
        return abigdecimal;
    }

    public static String[] s(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            String as[] = parcel.createStringArray();
            parcel.setDataPosition(j1 + k1);
            return as;
        }
    }

    public static ArrayList t(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
            return null;
        ArrayList arraylist = new ArrayList();
        int l1 = parcel.readInt();
        for (int i2 = 0; i2 < l1; i2++)
            arraylist.add(Integer.valueOf(parcel.readInt()));

        parcel.setDataPosition(k1 + j1);
        return arraylist;
    }

    public static ArrayList u(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            ArrayList arraylist = parcel.createStringArrayList();
            parcel.setDataPosition(j1 + k1);
            return arraylist;
        }
    }

    public static Parcel v(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
        {
            return null;
        } else
        {
            Parcel parcel1 = Parcel.obtain();
            parcel1.appendFrom(parcel, k1, j1);
            parcel.setDataPosition(j1 + k1);
            return parcel1;
        }
    }

    public static Parcel[] w(Parcel parcel, int i1)
    {
        int j1 = a(parcel, i1);
        int k1 = parcel.dataPosition();
        if (j1 == 0)
            return null;
        int l1 = parcel.readInt();
        Parcel aparcel[] = new Parcel[l1];
        int i2 = 0;
        while (i2 < l1)
        {
            int j2 = parcel.readInt();
            if (j2 != 0)
            {
                int k2 = parcel.dataPosition();
                Parcel parcel1 = Parcel.obtain();
                parcel1.appendFrom(parcel, k2, j2);
                aparcel[i2] = parcel1;
                parcel.setDataPosition(j2 + k2);
            } else
            {
                aparcel[i2] = null;
            }
            i2++;
        }
        parcel.setDataPosition(k1 + j1);
        return aparcel;
    }
}
