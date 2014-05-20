
package com.fireflycast.cast;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.cast.images.WebImage;
import com.fireflycast.client.common.internal.safeparcel.SafeParcelable;

/*
 * ApplicationMetadata.smali : OK
 */
public final class ApplicationMetadata implements SafeParcelable {
    public static final Parcelable.Creator<ApplicationMetadata> CREATOR = new ApplicationMetadataCreator_a();
    private final int mVersionCode_wj;
    String mApplicationId_wk;
    String mName;
    List<WebImage> mImages_wl;
    List<String> mNamespaces_wm;
    String mSenderAppIdentifier_wn;
    Uri mSenderAppLaunchUrl_wo;

    ApplicationMetadata(int versionCode, String applicationId, String name,
            List<WebImage> images, List<String> namespaces,
            String senderAppIdentifier, Uri senderAppLaunchUrl) {
        this.mVersionCode_wj = versionCode;
        this.mApplicationId_wk = applicationId;
        this.mName = name;
        this.mImages_wl = images;
        this.mNamespaces_wm = namespaces;
        this.mSenderAppIdentifier_wn = senderAppIdentifier;
        this.mSenderAppLaunchUrl_wo = senderAppLaunchUrl;
    }

    private ApplicationMetadata() {
        this.mVersionCode_wj = 1;
        this.mImages_wl = new ArrayList();
        this.mNamespaces_wm = new ArrayList();
    }

    int getVersionCode() {
        return this.mVersionCode_wj;
    }

    public String getApplicationId() {
        return this.mApplicationId_wk;
    }

    public String getName() {
        return this.mName;
    }

    public boolean isNamespaceSupported(String namespace) {
        return ((this.mNamespaces_wm != null) && (this.mNamespaces_wm.contains(namespace)));
    }

    public boolean areNamespacesSupported(List<String> namespaces) {
        return ((this.mNamespaces_wm != null) && (this.mNamespaces_wm.containsAll(namespaces)));
    }

    public String getSenderAppIdentifier() {
        return this.mSenderAppIdentifier_wn;
    }

    public Uri getSenderAppLaunchUrl_cR() {
        return this.mSenderAppLaunchUrl_wo;
    }

    public List<WebImage> getImages() {
        return this.mImages_wl;
    }

    public String toString() {
        return this.mName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        ApplicationMetadataCreator_a.buildParcel_a(this, out, flags);
    }
}
