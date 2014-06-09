
package com.fireflycast.server.cast;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.server.common.images.WebImage;
import com.fireflycast.server.common.internal.safeparcel.SafeParcelable;
import com.fireflycast.server.utils.ApplicationMetadataPriv;

import java.util.ArrayList;
import java.util.List;

public final class ApplicationMetadata implements SafeParcelable {
    public static final Parcelable.Creator<ApplicationMetadata> CREATOR = new ApplicationMetadataCreator();

    public String mApplicationId_a;
    String mName_b;
    List mImages_c;
    public List mNamespaces_d;
    String mSenderAppIdentifier_e;
    Uri mSenderAppLaunchUrl_f;
    private final int mVersionCode_g;

    private ApplicationMetadata()
    {
        mVersionCode_g = 1;
        mImages_c = new ArrayList();
        mNamespaces_d = new ArrayList();
    }

    public ApplicationMetadata(int versionCode, String applicationId, String name, List images,
            List namespaces, String senderAppIdentifier, Uri senderAppLaunchUrl)
    {
        mVersionCode_g = versionCode;
        mApplicationId_a = applicationId;
        mName_b = name;
        mImages_c = images;
        mNamespaces_d = namespaces;
        mSenderAppIdentifier_e = senderAppIdentifier;
        mSenderAppLaunchUrl_f = senderAppLaunchUrl;
    }

    public static ApplicationMetadataPriv getPrivateData_a(String applicationId)
    {
        ApplicationMetadata applicationmetadata = new ApplicationMetadata();
        applicationmetadata.getClass();
        return new ApplicationMetadataPriv(applicationmetadata, applicationId);
    }

    public static void setSenderAppLaunchUrl_a(ApplicationMetadata applicationmetadata, Uri uri)
    {
        applicationmetadata.mSenderAppLaunchUrl_f = uri;
    }

    public static void setName_a(ApplicationMetadata applicationmetadata, String name)
    {
        applicationmetadata.mName_b = name;
    }

    public static void setNamespaces_a(ApplicationMetadata applicationmetadata, List namespaces)
    {
        applicationmetadata.mNamespaces_d = namespaces;
    }

    public static void setSenderAppIdentifier_b(ApplicationMetadata applicationmetadata,
            String senderAppIdentifier)
    {
        applicationmetadata.mSenderAppIdentifier_e = senderAppIdentifier;
    }

    public final int getVersionCode_a()
    {
        return mVersionCode_g;
    }

    public final void addWebImage_a(WebImage webimage)
    {
        mImages_c.add(webimage);
    }

    public final String getApplicationId_b()
    {
        return mApplicationId_a;
    }

    public final String getName_c()
    {
        return mName_b;
    }

    public final String getSenderAppIdentifier_d()
    {
        return mSenderAppIdentifier_e;
    }

    public final int describeContents()
    {
        return 0;
    }

    public final Uri getSenderAppLaunchUrl_e()
    {
        return mSenderAppLaunchUrl_f;
    }

    public final List getImages_f()
    {
        return mImages_c;
    }

    public final String toString()
    {
        return mName_b;
    }

    public final void writeToParcel(Parcel out, int flags)
    {
        ApplicationMetadataCreator.buildParcel_a(this, out, flags);
    }
}
