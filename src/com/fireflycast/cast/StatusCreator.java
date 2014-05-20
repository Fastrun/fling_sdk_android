
package com.fireflycast.cast;

import android.app.PendingIntent;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a;
import com.fireflycast.client.common.internal.safeparcel.ParcelUtil_b;

/*
 * StatusCreator.smali : OK
 */
public class StatusCreator implements Parcelable.Creator<Status> {

    public static final int CONTENT_DESCRIPTION = 0;

    public Status createFromParcel(Parcel parcel) {
        int i = ParcelUtil_a.o(parcel);
        int j = 0;
        int k = 0;
        String str = null;
        PendingIntent localPendingIntent = null;
        while (parcel.dataPosition() < i) {
            int l = ParcelUtil_a.readInt_n(parcel);
            switch (ParcelUtil_a.S(l)) {
                case 1:
                    k = ParcelUtil_a.g(parcel, l);
                    break;
                case 1000:
                    j = ParcelUtil_a.g(parcel, l);
                    break;
                case 2:
                    str = ParcelUtil_a.m(parcel, l);
                    break;
                case 3:
                    localPendingIntent = (PendingIntent) ParcelUtil_a.a(parcel, l,
                            PendingIntent.CREATOR);
                    break;
                default:
                    ParcelUtil_a.b(parcel, l);
            }
        }
        if (parcel.dataPosition() != i)
            throw new ParcelUtil_a.SafeParcelA("Overread allowed size end=" + i, parcel);
        return new Status(j, k, str, localPendingIntent);
    }

    public Status[] newArray(int size) {
        return new Status[size];
    }

    static void buildParcel_a(Status paramStatus, Parcel paramParcel, int paramInt) {
        int i = ParcelUtil_b.p(paramParcel);
        ParcelUtil_b.c(paramParcel, 1, paramStatus.getStatusCode());
        ParcelUtil_b.c(paramParcel, 1000, paramStatus.getVersionCode());
        ParcelUtil_b.a(paramParcel, 2, paramStatus.getStatusMessage_dF(), false);
        ParcelUtil_b.a(paramParcel, 3, paramStatus.getPendingIntent_dE(), paramInt, false);
        ParcelUtil_b.D(paramParcel, i);
    }
}
