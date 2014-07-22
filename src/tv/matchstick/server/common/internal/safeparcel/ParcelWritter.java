
package tv.matchstick.server.common.internal.safeparcel;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public final class ParcelWritter {
    public static int a(Parcel parcel, int i)
    {
        parcel.writeInt(0xffff0000 | i);
        parcel.writeInt(0);
        return parcel.dataPosition();
    }

    public static void a(Parcel parcel, int i, byte byte0)
    {
        a(parcel, i, 4);
        parcel.writeInt(byte0);
    }

    public static void a(Parcel parcel, int i, double d)
    {
        a(parcel, i, 8);
        parcel.writeDouble(d);
    }

    public static void a(Parcel parcel, int i, float f)
    {
        a(parcel, i, 4);
        parcel.writeFloat(f);
    }

    public static void a(Parcel parcel, int i, int j)
    {
        if (j >= 65535)
        {
            parcel.writeInt(0xffff0000 | i);
            parcel.writeInt(j);
            return;
        } else
        {
            parcel.writeInt(i | j << 16);
            return;
        }
    }

    public static void a(Parcel parcel, int i, long l)
    {
        a(parcel, i, 8);
        parcel.writeLong(l);
    }

    public static void a(Parcel parcel, int i, Bundle bundle, boolean flag)
    {
        if (bundle == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.writeBundle(bundle);
            b(parcel, j);
            return;
        }
    }

    public static void a(Parcel parcel, int i, IBinder ibinder)
    {
        if (ibinder == null)
        {
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.writeStrongBinder(ibinder);
            b(parcel, j);
            return;
        }
    }

    public static void a(Parcel parcel, int i, Parcel parcel1, boolean flag)
    {
        if (parcel1 == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.appendFrom(parcel1, 0, parcel1.dataSize());
            b(parcel, j);
            return;
        }
    }

    public static void a(Parcel parcel, int i, Parcelable parcelable, int j, boolean flag)
    {
        if (parcelable == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int k = a(parcel, i);
            parcelable.writeToParcel(parcel, j);
            b(parcel, k);
            return;
        }
    }

    public static void a(Parcel parcel, int i, Boolean boolean1)
    {
        if (boolean1 == null)
            return;
        a(parcel, i, 4);
        int j;
        if (boolean1.booleanValue())
            j = 1;
        else
            j = 0;
        parcel.writeInt(j);
    }

    public static void a(Parcel parcel, int i, String s, boolean flag)
    {
        if (s == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.writeString(s);
            b(parcel, j);
            return;
        }
    }

    public static void a(Parcel parcel, int i, List list)
    {
        if (list == null)
        {
            a(parcel, i, 0);
            return;
        }
        int j = a(parcel, i);
        int k = list.size();
        parcel.writeInt(k);
        int l = 0;
        while (l < k)
        {
            Parcel parcel1 = (Parcel) list.get(l);
            if (parcel1 != null)
            {
                parcel.writeInt(parcel1.dataSize());
                parcel.appendFrom(parcel1, 0, parcel1.dataSize());
            } else
            {
                parcel.writeInt(0);
            }
            l++;
        }
        b(parcel, j);
    }

    public static void a(Parcel parcel, int i, List list, boolean flag)
    {
        if (list == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.writeStringList(list);
            b(parcel, j);
            return;
        }
    }

    public static void a(Parcel parcel, int i, boolean flag)
    {
        a(parcel, i, 4);
        int j;
        if (flag)
            j = 1;
        else
            j = 0;
        parcel.writeInt(j);
    }

    public static void a(Parcel parcel, int i, byte abyte0[], boolean flag)
    {
        if (abyte0 == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.writeByteArray(abyte0);
            b(parcel, j);
            return;
        }
    }

    public static void a(Parcel parcel, int i, float af[], boolean flag)
    {
        if (af == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.writeFloatArray(af);
            b(parcel, j);
            return;
        }
    }

    public static void a(Parcel parcel, int i, int ai[], boolean flag)
    {
        if (ai == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.writeIntArray(ai);
            b(parcel, j);
            return;
        }
    }

    public static void a(Parcel parcel, int i, Parcelable aparcelable[], int j)
    {
        if (aparcelable == null)
            return;
        int k = a(parcel, i);
        int l = aparcelable.length;
        parcel.writeInt(l);
        int i1 = 0;
        while (i1 < l)
        {
            Parcelable parcelable = aparcelable[i1];
            if (parcelable == null)
                parcel.writeInt(0);
            else
                a(parcel, parcelable, j);
            i1++;
        }
        b(parcel, k);
    }

    public static void a(Parcel parcel, int i, String as[], boolean flag)
    {
        if (as == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        } else
        {
            int j = a(parcel, i);
            parcel.writeStringArray(as);
            b(parcel, j);
            return;
        }
    }

    private static void a(Parcel parcel, Parcelable parcelable, int i)
    {
        int j = parcel.dataPosition();
        parcel.writeInt(1);
        int k = parcel.dataPosition();
        parcelable.writeToParcel(parcel, i);
        int l = parcel.dataPosition();
        parcel.setDataPosition(j);
        parcel.writeInt(l - k);
        parcel.setDataPosition(l);
    }

    public static void a(Parcel parcel, List list)
    {
        if (list == null)
        {
            return;
        } else
        {
            int i = a(parcel, 3);
            parcel.writeList(list);
            b(parcel, i);
            return;
        }
    }

    public static void b(Parcel parcel, int i)
    {
        int j = parcel.dataPosition();
        int k = j - i;
        parcel.setDataPosition(i - 4);
        parcel.writeInt(k);
        parcel.setDataPosition(j);
    }

    public static void b(Parcel parcel, int i, int j)
    {
        a(parcel, i, 4);
        parcel.writeInt(j);
    }

    public static void b(Parcel parcel, int i, List list, boolean flag)
    {
        if (list == null)
        {
            if (flag)
                a(parcel, i, 0);
            return;
        }
        int j = a(parcel, i);
        int k = list.size();
        parcel.writeInt(k);
        int l = 0;
        while (l < k)
        {
            Parcelable parcelable = (Parcelable) list.get(l);
            if (parcelable == null)
                parcel.writeInt(0);
            else
                a(parcel, parcelable, 0);
            l++;
        }
        b(parcel, j);
    }
}
