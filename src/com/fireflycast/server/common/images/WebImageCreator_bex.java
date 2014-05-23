
package com.fireflycast.server.common.images;

import android.net.Uri;
import android.os.Parcel;

import com.fireflycast.server.common.exception.CastRuntimeException_bhe;
import com.fireflycast.server.common.internal.safeparcel.ParcelReader_bhd;
import com.fireflycast.server.common.internal.safeparcel.ParcelWritter_bhf;

public final class WebImageCreator_bex implements android.os.Parcelable.Creator {
    public WebImageCreator_bex()
    {
    }

    public static void a(WebImage webimage, Parcel parcel, int i)
    {
        int j = ParcelWritter_bhf.a(parcel, 20293);
        ParcelWritter_bhf.b(parcel, 1, webimage.getVersionCode());
        ParcelWritter_bhf.a(parcel, 2, webimage.getUrl(), i, false);
        ParcelWritter_bhf.b(parcel, 3, webimage.getWidth());
        ParcelWritter_bhf.b(parcel, 4, webimage.getHeight());
        ParcelWritter_bhf.b(parcel, j);
    }

    public final Object createFromParcel(Parcel parcel)
    {
        int i = ParcelReader_bhd.a(parcel);
        int j = 0;
        Uri uri = null;
        int k = 0;
        int l = 0;
        do
            if (parcel.dataPosition() < i)
            {
                int i1 = parcel.readInt();
                switch (0xffff & i1)
                {
                    default:
                        ParcelReader_bhd.b(parcel, i1);
                        break;

                    case 1: // '\001'
                        k = ParcelReader_bhd.f(parcel, i1);
                        break;

                    case 2: // '\002'
                        uri = (Uri) ParcelReader_bhd.a(parcel, i1, Uri.CREATOR);
                        break;

                    case 3: // '\003'
                        j = ParcelReader_bhd.f(parcel, i1);
                        break;

                    case 4: // '\004'
                        l = ParcelReader_bhd.f(parcel, i1);
                        break;
                }
            } else if (parcel.dataPosition() != i)
                throw new CastRuntimeException_bhe(
                        (new StringBuilder("Overread allowed size end=")).append(i).toString(),
                        parcel);
            else
                return new WebImage(k, uri, j, l);
        while (true);
    }

    public final Object[] newArray(int i)
    {
        return new WebImage[i];
    }
}
