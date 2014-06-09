
package com.fireflycast.cast;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil;
import com.fireflycast.client.common.internal.safeparcel.ParcelWriteUtil;

/*
 * StatusCreator.smali : OK
 */
public class StatusCreator implements Parcelable.Creator<Status> {

    public static final int CONTENT_DESCRIPTION = 0;

    public Status createFromParcel(Parcel parcel) {
        int i = ParcelReadUtil.o(parcel);
        int j = 0;
        int k = 0;
        String str = null;
        PendingIntent localPendingIntent = null;
        while (parcel.dataPosition() < i) {
            int l = ParcelReadUtil.readInt_n(parcel);
            switch (ParcelReadUtil.S(l)) {
                case 1:
                    k = ParcelReadUtil.g(parcel, l);
                    break;
                case 1000:
                    j = ParcelReadUtil.g(parcel, l);
                    break;
                case 2:
                    str = ParcelReadUtil.m(parcel, l);
                    break;
                case 3:
                    localPendingIntent = (PendingIntent) ParcelReadUtil.a(parcel, l,
                            PendingIntent.CREATOR);
                    break;
                default:
                    ParcelReadUtil.b(parcel, l);
            }
        }
        if (parcel.dataPosition() != i)
            throw new ParcelReadUtil.SafeParcelA("Overread allowed size end=" + i, parcel);
        return new Status(j, k, str, localPendingIntent);
    }

    public Status[] newArray(int size) {
        return new Status[size];
    }

    static void buildParcel_a(Status paramStatus, Parcel paramParcel, int paramInt) {
        int i = ParcelWriteUtil.p(paramParcel);
        ParcelWriteUtil.c(paramParcel, 1, paramStatus.getStatusCode());
        ParcelWriteUtil.c(paramParcel, 1000, paramStatus.getVersionCode());
        ParcelWriteUtil.a(paramParcel, 2, paramStatus.getStatusMessage_dF(), false);
        ParcelWriteUtil.a(paramParcel, 3, paramStatus.getPendingIntent_dE(), paramInt, false);
        ParcelWriteUtil.D(paramParcel, i);
    }
}
