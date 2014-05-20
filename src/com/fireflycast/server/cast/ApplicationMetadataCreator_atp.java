
package com.fireflycast.server.cast;

import android.net.Uri;
import android.os.Parcel;

import com.fireflycast.server.common.exception.CastRuntimeException_bhe;
import com.fireflycast.server.common.images.WebImage;
import com.fireflycast.server.common.internal.safeparcel.ParcelReader_bhd;
import com.fireflycast.server.common.internal.safeparcel.ParcelWritter_bhf;

public final class ApplicationMetadataCreator_atp implements android.os.Parcelable.Creator {
    public ApplicationMetadataCreator_atp()
    {
    }

    public static void buildParcel_a(ApplicationMetadata applicationmetadata, Parcel parcel, int i)
    {
        int j = ParcelWritter_bhf.a(parcel, 20293);
        ParcelWritter_bhf.b(parcel, 1, applicationmetadata.getVersionCode_a());
        ParcelWritter_bhf.a(parcel, 2, applicationmetadata.getApplicationId_b(), false);
        ParcelWritter_bhf.a(parcel, 3, applicationmetadata.getName_c(), false);
        ParcelWritter_bhf.b(parcel, 4, applicationmetadata.getImages_f(), false);
        ParcelWritter_bhf.a(parcel, 5, applicationmetadata.mNamespaces_d, false);
        ParcelWritter_bhf.a(parcel, 6, applicationmetadata.getSenderAppIdentifier_d(), false);
        ParcelWritter_bhf.a(parcel, 7, applicationmetadata.getSenderAppLaunchUrl_e(), i, false);
        ParcelWritter_bhf.b(parcel, j);
    }

    public final Object createFromParcel(Parcel parcel)
    {
        Uri senderAppLaunchUrl = null;
        int i = ParcelReader_bhd.a(parcel);
        int versionCode = 0;
        String senderAppIdentifier = null;
        java.util.ArrayList namespaces = null;
        java.util.ArrayList images = null;
        String name = null;
        String applicationId = null;
        do
            if (parcel.dataPosition() < i)
            {
                int k = parcel.readInt();
                switch (0xffff & k)
                {
                    default:
                        ParcelReader_bhd.b(parcel, k);
                        break;

                    case 1: // '\001'
                        versionCode = ParcelReader_bhd.f(parcel, k);
                        break;

                    case 2: // '\002'
                        applicationId = ParcelReader_bhd.l(parcel, k);
                        break;

                    case 3: // '\003'
                        name = ParcelReader_bhd.l(parcel, k);
                        break;

                    case 4: // '\004'
                        images = ParcelReader_bhd.c(parcel, k, WebImage.CREATOR);
                        break;

                    case 5: // '\005'
                        namespaces = ParcelReader_bhd.u(parcel, k);
                        break;

                    case 6: // '\006'
                        senderAppIdentifier = ParcelReader_bhd.l(parcel, k);
                        break;

                    case 7: // '\007'
                        senderAppLaunchUrl = (Uri) ParcelReader_bhd.a(parcel, k, Uri.CREATOR);
                        break;
                }
            } else if (parcel.dataPosition() != i)
                throw new CastRuntimeException_bhe(
                        (new StringBuilder("Overread allowed size end=")).append(i).toString(),
                        parcel);
            else
                return new ApplicationMetadata(versionCode, applicationId, name, images,
                        namespaces, senderAppIdentifier, senderAppLaunchUrl);
        while (true);
    }

    public final Object[] newArray(int i)
    {
        return new ApplicationMetadata[i];
    }
}
