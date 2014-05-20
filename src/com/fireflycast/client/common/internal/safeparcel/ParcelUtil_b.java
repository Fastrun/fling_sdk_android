
package com.fireflycast.client.common.internal.safeparcel;

import java.util.List;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * OK
 */
public class ParcelUtil_b {
    private static void b(Parcel paramParcel, int paramInt1, int paramInt2) {
        if (paramInt2 >= 65535) {
            paramParcel.writeInt(0xFFFF0000 | paramInt1);
            paramParcel.writeInt(paramInt2);
        } else {
            paramParcel.writeInt(paramInt2 << 16 | paramInt1);
        }
    }

    private static int writeInt_B(Parcel paramParcel, int paramInt) {
        paramParcel.writeInt(0xFFFF0000 | paramInt);
        paramParcel.writeInt(0);
        return paramParcel.dataPosition();
    }

    private static void writeInt_C(Parcel paramParcel, int paramInt) {
        int i = paramParcel.dataPosition();
        int j = i - paramInt;
        paramParcel.setDataPosition(paramInt - 4);
        paramParcel.writeInt(j);
        paramParcel.setDataPosition(i);
    }

    public static int p(Parcel paramParcel) {
        return writeInt_B(paramParcel, 20293);
    }

    public static void D(Parcel paramParcel, int paramInt) {
        writeInt_C(paramParcel, paramInt);
    }

    public static void a(Parcel paramParcel, int paramInt, boolean paramBoolean) {
        b(paramParcel, paramInt, 4);
        paramParcel.writeInt((paramBoolean) ? 1 : 0);
    }

    public static void a(Parcel paramParcel, int paramInt,
            Boolean paramBoolean, boolean paramBoolean1) {
        if (paramBoolean == null) {
            if (paramBoolean1)
                b(paramParcel, paramInt, 0);
            return;
        }
        b(paramParcel, paramInt, 4);
        paramParcel.writeInt((paramBoolean.booleanValue()) ? 1 : 0);
    }

    public static void a(Parcel paramParcel, int paramInt, byte paramByte) {
        b(paramParcel, paramInt, 4);
        paramParcel.writeInt(paramByte);
    }

    public static void a(Parcel paramParcel, int paramInt, short paramShort) {
        b(paramParcel, paramInt, 4);
        paramParcel.writeInt(paramShort);
    }

    public static void c(Parcel paramParcel, int paramInt1, int paramInt2) {
        b(paramParcel, paramInt1, 4);
        paramParcel.writeInt(paramInt2);
    }

    public static void a(Parcel paramParcel, int paramInt, long paramLong) {
        b(paramParcel, paramInt, 8);
        paramParcel.writeLong(paramLong);
    }

    public static void a(Parcel paramParcel, int paramInt, float paramFloat) {
        b(paramParcel, paramInt, 4);
        paramParcel.writeFloat(paramFloat);
    }

    public static void a(Parcel paramParcel, int paramInt, double paramDouble) {
        b(paramParcel, paramInt, 8);
        paramParcel.writeDouble(paramDouble);
    }

    public static void a(Parcel paramParcel, int paramInt, String paramString,
            boolean paramBoolean) {
        if (paramString == null) {
            if (paramBoolean)
                b(paramParcel, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt);
        paramParcel.writeString(paramString);
        writeInt_C(paramParcel, i);
    }

    public static void a(Parcel paramParcel, int paramInt,
            IBinder paramIBinder, boolean paramBoolean) {
        if (paramIBinder == null) {
            if (paramBoolean)
                b(paramParcel, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt);
        paramParcel.writeStrongBinder(paramIBinder);
        writeInt_C(paramParcel, i);
    }

    public static void a(Parcel paramParcel, int paramInt1,
            Parcelable paramParcelable, int paramInt2, boolean paramBoolean) {
        if (paramParcelable == null) {
            if (paramBoolean)
                b(paramParcel, paramInt1, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt1);
        paramParcelable.writeToParcel(paramParcel, paramInt2);
        writeInt_C(paramParcel, i);
    }

    public static void a(Parcel paramParcel, int paramInt, Bundle paramBundle,
            boolean paramBoolean) {
        if (paramBundle == null) {
            if (paramBoolean)
                b(paramParcel, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt);
        paramParcel.writeBundle(paramBundle);
        writeInt_C(paramParcel, i);
    }

    public static void a(Parcel paramParcel, int paramInt,
            byte[] paramArrayOfByte, boolean paramBoolean) {
        if (paramArrayOfByte == null) {
            if (paramBoolean)
                b(paramParcel, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt);
        paramParcel.writeByteArray(paramArrayOfByte);
        writeInt_C(paramParcel, i);
    }

    public static void a(Parcel paramParcel, int paramInt,
            String[] paramArrayOfString, boolean paramBoolean) {
        if (paramArrayOfString == null) {
            if (paramBoolean)
                b(paramParcel, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt);
        paramParcel.writeStringArray(paramArrayOfString);
        writeInt_C(paramParcel, i);
    }

    public static void a(Parcel paramParcel, int paramInt,
            List<String> paramList, boolean paramBoolean) {
        if (paramList == null) {
            if (paramBoolean)
                b(paramParcel, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt);
        paramParcel.writeStringList(paramList);
        writeInt_C(paramParcel, i);
    }

    public static <T extends Parcelable> void a(Parcel paramParcel,
            int paramInt1, T[] paramArrayOfT, int paramInt2,
            boolean paramBoolean) {
        if (paramArrayOfT == null) {
            if (paramBoolean)
                b(paramParcel, paramInt1, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt1);
        int j = paramArrayOfT.length;
        paramParcel.writeInt(j);
        for (int k = 0; k < j; ++k) {
            T t = paramArrayOfT[k];
            if (t == null)
                paramParcel.writeInt(0);
            else
                a(paramParcel, t, paramInt2);
        }
        writeInt_C(paramParcel, i);
    }

    public static <T extends Parcelable> void b(Parcel paramParcel,
            int paramInt, List<T> paramList, boolean paramBoolean) {
        if (paramList == null) {
            if (paramBoolean)
                b(paramParcel, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt);
        int j = paramList.size();
        paramParcel.writeInt(j);
        for (int k = 0; k < j; ++k) {
            Parcelable localParcelable = (Parcelable) paramList.get(k);
            if (localParcelable == null)
                paramParcel.writeInt(0);
            else
                a(paramParcel, localParcelable, 0);
        }
        writeInt_C(paramParcel, i);
    }

    private static <T extends Parcelable> void a(Parcel paramParcel, T paramT,
            int paramInt) {
        int i = paramParcel.dataPosition();
        paramParcel.writeInt(1);
        int j = paramParcel.dataPosition();
        paramT.writeToParcel(paramParcel, paramInt);
        int k = paramParcel.dataPosition();
        paramParcel.setDataPosition(i);
        paramParcel.writeInt(k - j);
        paramParcel.setDataPosition(k);
    }

    public static void a(Parcel paramParcel1, int paramInt,
            Parcel paramParcel2, boolean paramBoolean) {
        if (paramParcel2 == null) {
            if (paramBoolean)
                b(paramParcel1, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel1, paramInt);
        paramParcel1.appendFrom(paramParcel2, 0, paramParcel2.dataSize());
        writeInt_C(paramParcel1, i);
    }

    public static void writeList_c(Parcel paramParcel, int paramInt, List paramList,
            boolean paramBoolean) {
        if (paramList == null) {
            if (paramBoolean)
                b(paramParcel, paramInt, 0);
            return;
        }
        int i = writeInt_B(paramParcel, paramInt);
        paramParcel.writeList(paramList);
        writeInt_C(paramParcel, i);
    }
}
