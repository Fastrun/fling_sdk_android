
package tv.matchstick.server.fling;

import tv.matchstick.server.common.exception.FlingRuntimeException;
import tv.matchstick.server.common.images.WebImage;
import tv.matchstick.server.common.internal.safeparcel.ParcelReader;
import tv.matchstick.server.common.internal.safeparcel.ParcelWritter;
import android.net.Uri;
import android.os.Parcel;

final class ApplicationMetadataCreator implements android.os.Parcelable.Creator {
    public ApplicationMetadataCreator()
    {
    }

    public static void buildParcel(ApplicationMetadata applicationmetadata, Parcel parcel, int i)
    {
        int j = ParcelWritter.a(parcel, 20293);
        ParcelWritter.b(parcel, 1, applicationmetadata.getVersionCode());
        ParcelWritter.a(parcel, 2, applicationmetadata.getApplicationId(), false);
        ParcelWritter.a(parcel, 3, applicationmetadata.getName(), false);
        ParcelWritter.b(parcel, 4, applicationmetadata.getImages(), false);
        ParcelWritter.a(parcel, 5, applicationmetadata.mNamespaces, false);
        ParcelWritter.a(parcel, 6, applicationmetadata.getSenderAppIdentifier(), false);
        ParcelWritter.a(parcel, 7, applicationmetadata.getSenderAppLaunchUrl(), i, false);
        ParcelWritter.b(parcel, j);
    }

    public final Object createFromParcel(Parcel parcel)
    {
        Uri senderAppLaunchUrl = null;
        int i = ParcelReader.a(parcel);
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
                        ParcelReader.b(parcel, k);
                        break;

                    case 1: // '\001'
                        versionCode = ParcelReader.f(parcel, k);
                        break;

                    case 2: // '\002'
                        applicationId = ParcelReader.l(parcel, k);
                        break;

                    case 3: // '\003'
                        name = ParcelReader.l(parcel, k);
                        break;

                    case 4: // '\004'
                        images = ParcelReader.c(parcel, k, WebImage.CREATOR);
                        break;

                    case 5: // '\005'
                        namespaces = ParcelReader.u(parcel, k);
                        break;

                    case 6: // '\006'
                        senderAppIdentifier = ParcelReader.l(parcel, k);
                        break;

                    case 7: // '\007'
                        senderAppLaunchUrl = (Uri) ParcelReader.a(parcel, k, Uri.CREATOR);
                        break;
                }
            } else if (parcel.dataPosition() != i)
                throw new FlingRuntimeException(
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
