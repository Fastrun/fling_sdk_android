
package tv.matchstick.server.fling;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import tv.matchstick.server.common.images.WebImage;
import tv.matchstick.server.common.internal.safeparcel.SafeParcelable;
import tv.matchstick.server.utils.ApplicationMetadataPriv;

public final class ApplicationMetadata implements SafeParcelable {
    public static final Parcelable.Creator<ApplicationMetadata> CREATOR = new ApplicationMetadataCreator();

    public String mApplicationId;
    String mName;
    List mImages;
    public List mNamespaces;
    String mSenderAppIdentifier;
    Uri mSenderAppLaunchUrl;
    private final int mVersionCode;

    private ApplicationMetadata()
    {
        mVersionCode = 1;
        mImages = new ArrayList();
        mNamespaces = new ArrayList();
    }

    public ApplicationMetadata(int versionCode, String applicationId, String name, List images,
            List namespaces, String senderAppIdentifier, Uri senderAppLaunchUrl)
    {
        mVersionCode = versionCode;
        mApplicationId = applicationId;
        mName = name;
        mImages = images;
        mNamespaces = namespaces;
        mSenderAppIdentifier = senderAppIdentifier;
        mSenderAppLaunchUrl = senderAppLaunchUrl;
    }

    public static ApplicationMetadataPriv getPrivateData(String applicationId)
    {
        ApplicationMetadata applicationmetadata = new ApplicationMetadata();
        applicationmetadata.getClass();
        return new ApplicationMetadataPriv(applicationmetadata, applicationId);
    }

    public static void setSenderAppLaunchUrl(ApplicationMetadata applicationmetadata, Uri uri)
    {
        applicationmetadata.mSenderAppLaunchUrl = uri;
    }

    public static void setName(ApplicationMetadata applicationmetadata, String name)
    {
        applicationmetadata.mName = name;
    }

    public static void setNamespaces(ApplicationMetadata applicationmetadata, List namespaces)
    {
        applicationmetadata.mNamespaces = namespaces;
    }

    public static void setSenderAppIdentifier(ApplicationMetadata applicationmetadata,
            String senderAppIdentifier)
    {
        applicationmetadata.mSenderAppIdentifier = senderAppIdentifier;
    }

    public final int getVersionCode()
    {
        return mVersionCode;
    }

    public final void addWebImage(WebImage webimage)
    {
        mImages.add(webimage);
    }

    public final String getApplicationId()
    {
        return mApplicationId;
    }

    public final String getName()
    {
        return mName;
    }

    public final String getSenderAppIdentifier()
    {
        return mSenderAppIdentifier;
    }

    public final int describeContents()
    {
        return 0;
    }

    public final Uri getSenderAppLaunchUrl()
    {
        return mSenderAppLaunchUrl;
    }

    public final List getImages()
    {
        return mImages;
    }

    public final String toString()
    {
        return mName;
    }

    public final void writeToParcel(Parcel out, int flags)
    {
        ApplicationMetadataCreator.buildParcel(this, out, flags);
    }
}
