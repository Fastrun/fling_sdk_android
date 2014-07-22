
package tv.matchstick.fling.images;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.client.common.internal.safeparcel.SafeParcelable;
import tv.matchstick.client.internal.MyStringBuilder;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/*
 * WebImage.smali : OK
 */
public class WebImage implements SafeParcelable {

    public static final Parcelable.Creator<WebImage> CREATOR = new WebImageCreator();
    private final int mVersionCode;
    private final Uri mUrl;
    private final int mWidth;
    private final int mHeight;

    WebImage(int versionCode, Uri url, int width, int height) {
        this.mVersionCode = versionCode;
        this.mUrl = url;
        this.mWidth = width;
        this.mHeight = height;
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
        this(getUriFromJson(json), json.optInt("width", 0), json.optInt("height", 0));
    }

    int getVersionCode() {
        return this.mVersionCode;
    }

    private static Uri getUriFromJson(JSONObject data) {
        Uri localUri = null;
        if (data.has("url"))
            try {
                localUri = Uri.parse(data.getString("url"));
            } catch (JSONException localJSONException) {
            }
        return localUri;
    }

    public Uri getUrl() {
        return this.mUrl;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public String toString() {
        return String.format("Image %dx%d %s",
                new Object[] {
                        Integer.valueOf(this.mWidth),
                        Integer.valueOf(this.mHeight), this.mUrl.toString()
                });
    }

    public JSONObject buildJson() {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("url", this.mUrl.toString());
            localJSONObject.put("width", this.mWidth);
            localJSONObject.put("height", this.mHeight);
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
        return ((MyStringBuilder.compare(this.mUrl, localWebImage.mUrl))
                && (this.mWidth == localWebImage.mWidth) && (this.mHeight == localWebImage.mHeight));
    }

    public int hashCode() {
        return MyStringBuilder.hashCode(new Object[] {
                this.mUrl, Integer.valueOf(this.mWidth),
                Integer.valueOf(this.mHeight)
        });
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        WebImageCreator.buildParcel(this, out, flags);
    }

}
