
package com.fireflycast.server.common.images;

import android.net.Uri;
import android.os.Parcel;

import com.fireflycast.server.common.checker.ObjEqualChecker_avo;
import com.fireflycast.server.common.internal.safeparcel.SafeParcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public final class WebImage implements SafeParcelable {
    public static final android.os.Parcelable.Creator CREATOR = new WebImageCreator_bex();
    private final int a;
    private final Uri url;
    private final int width;
    private final int height;

    public WebImage(int i, Uri uri, int j, int k)
    {
        a = i;
        url = uri;
        width = j;
        height = k;
    }

    public WebImage(Uri uri)
    {
        this(uri, 0, 0);
    }

    private WebImage(Uri uri, int i, int j)
    {
        this(1, uri, i, j);
        if (uri == null)
            throw new IllegalArgumentException("url cannot be null");
        if (i < 0 || j < 0)
            throw new IllegalArgumentException("width and height must not be negative");
        else
            return;
    }

    public WebImage(JSONObject jsonobject)
    {
        this(a(jsonobject), jsonobject.optInt("width", 0), jsonobject.optInt("height", 0));
    }

    private static Uri a(JSONObject jsonobject)
    {
        boolean flag = jsonobject.has("url");
        Uri uri = null;
        if (flag)
        {
            Uri uri1;
            try
            {
                uri1 = Uri.parse(jsonobject.getString("url"));
            } catch (JSONException jsonexception)
            {
                return null;
            }
            uri = uri1;
        }
        return uri;
    }

    public final int a()
    {
        return a;
    }

    public final Uri getUrl_b()
    {
        return url;
    }

    public final int c()
    {
        return width;
    }

    public final int d()
    {
        return height;
    }

    public final int describeContents()
    {
        return 0;
    }

    public final JSONObject e()
    {
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("url", url.toString());
            jsonobject.put("width", width);
            jsonobject.put("height", height);
        } catch (JSONException jsonexception)
        {
            return jsonobject;
        }
        return jsonobject;
    }

    public final boolean equals(Object obj)
    {
        if (this != obj)
        {
            if (obj == null || !(obj instanceof WebImage))
                return false;
            WebImage webimage = (WebImage) obj;
            if (!ObjEqualChecker_avo.isEquals_a(url, webimage.url) || width != webimage.width
                    || height != webimage.height)
                return false;
        }
        return true;
    }

    public final int hashCode()
    {
        Object aobj[] = new Object[3];
        aobj[0] = url;
        aobj[1] = Integer.valueOf(width);
        aobj[2] = Integer.valueOf(height);
        return Arrays.hashCode(aobj);
    }

    public final String toString()
    {
        Object aobj[] = new Object[3];
        aobj[0] = Integer.valueOf(width);
        aobj[1] = Integer.valueOf(height);
        aobj[2] = url.toString();
        return String.format("Image %dx%d %s", aobj);
    }

    public final void writeToParcel(Parcel parcel, int i)
    {
        WebImageCreator_bex.a(this, parcel, i);
    }
}
