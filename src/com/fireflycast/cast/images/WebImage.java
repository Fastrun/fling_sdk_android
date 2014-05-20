
package com.fireflycast.cast.images;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.client.common.internal.safeparcel.SafeParcelable;
import com.fireflycast.client.internal.StringBuilder_ep;

/*
 * WebImage.smali : OK
 */
public class WebImage implements SafeParcelable {

    public static final Parcelable.Creator<WebImage> CREATOR = new WebImageCreator_b();
    private final int mVersionCode_wj;
    private final Uri mUrl_AR;
    private final int mWidth_w;
    private final int mHeight_v;

    WebImage(int versionCode, Uri url, int width, int height) {
        this.mVersionCode_wj = versionCode;
        this.mUrl_AR = url;
        this.mWidth_w = width;
        this.mHeight_v = height;
    }

    public WebImage(Uri url, int width, int height)
            throws IllegalArgumentException {
        this(1, url, width, height);
        if (url == null) {
            throw new IllegalArgumentException("url cannot be null");
        }
        if ((width >= 0) && (height >= 0)) {
            return;
        }
        throw new IllegalArgumentException(
                "width and height must not be negative");
    }

    public WebImage(Uri url) throws IllegalArgumentException {
        this(url, 0, 0);
    }

    public WebImage(JSONObject json) throws IllegalArgumentException {
        this(getUriFromJson_c(json), json.optInt("width", 0), json.optInt("height", 0));
    }

    int getVersionCode() {
        return this.mVersionCode_wj;
    }

    private static Uri getUriFromJson_c(JSONObject data) {
        Uri localUri = null;
        if (data.has("url"))
            try {
                localUri = Uri.parse(data.getString("url"));
            } catch (JSONException localJSONException) {
            }
        return localUri;
    }

    public Uri getUrl() {
        return this.mUrl_AR;
    }

    public int getWidth() {
        return this.mWidth_w;
    }

    public int getHeight() {
        return this.mHeight_v;
    }

    public String toString() {
        return String.format("Image %dx%d %s",
                new Object[] {
                        Integer.valueOf(this.mWidth_w),
                        Integer.valueOf(this.mHeight_v), this.mUrl_AR.toString()
                });
    }

    public JSONObject buildJson_cT() {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("url", this.mUrl_AR.toString());
            localJSONObject.put("width", this.mWidth_w);
            localJSONObject.put("height", this.mHeight_v);
        } catch (JSONException localJSONException) {
        }
        return localJSONObject;
    }

    public boolean equals(Object other) {
        if (this == other)
            return true;
        if ((other == null) || (!(other instanceof WebImage)))
            return false;
        WebImage localWebImage = (WebImage) other;
        return ((StringBuilder_ep.compare(this.mUrl_AR, localWebImage.mUrl_AR))
                && (this.mWidth_w == localWebImage.mWidth_w) && (this.mHeight_v == localWebImage.mHeight_v));
    }

    public int hashCode() {
        return StringBuilder_ep.hashCode(new Object[] {
                this.mUrl_AR, Integer.valueOf(this.mWidth_w),
                Integer.valueOf(this.mHeight_v)
        });
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        WebImageCreator_b.buildParcel_a(this, out, flags);
    }

}
