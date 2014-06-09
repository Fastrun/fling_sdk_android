
package com.fireflycast.server.cast;

import android.os.Bundle;

import com.fireflycast.server.common.images.WebImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

final class CastMediaManagerHelper {
    static Bundle createMetadataBundle_a(MediaInfo atz1)
    {
        Bundle bundle = new Bundle();
        bundle.putLong("android.media.metadata.DURATION", atz1.mDuration_e);
        MediaMetadata aub1 = atz1.mMediaMetadata_d;
        String s = aub1.getString_b("com.fireflycast.cast.metadata.ALBUM_ARTIST");
        if (s != null)
            bundle.putString("android.media.metadata.ALBUM_ARTIST", s);
        String s1 = aub1.getString_b("com.fireflycast.cast.metadata.ALBUM_TITLE");
        if (s1 != null)
            bundle.putString("android.media.metadata.ALBUM_TITLE", s1);
        String s2 = aub1.getString_b("com.fireflycast.cast.metadata.ARTIST");
        if (s2 == null)
            s2 = aub1.getString_b("com.fireflycast.cast.metadata.STUDIO");
        if (s2 != null)
            bundle.putString("android.media.metadata.ARTIST", s2);
        String s3 = aub1.getString_b("com.fireflycast.cast.metadata.COMPOSER");
        if (s3 != null)
            bundle.putString("android.media.metadata.COMPOSER", s3);
        if (aub1.a("com.fireflycast.cast.metadata.DISC_NUMBER"))
            bundle.putInt("android.media.metadata.DISC_NUMBER",
                    aub1.c("com.fireflycast.cast.metadata.DISC_NUMBER"));
        String s4 = aub1.getString_b("com.fireflycast.cast.metadata.TITLE");
        if (s4 != null)
            bundle.putString("android.media.metadata.TITLE", s4);
        if (aub1.a("com.fireflycast.cast.metadata.TRACK_NUMBER"))
            bundle.putInt("android.media.metadata.TRACK_NUMBER",
                    aub1.c("com.fireflycast.cast.metadata.TRACK_NUMBER"));
        Calendar calendar = aub1.d("com.fireflycast.cast.metadata.BROADCAST_DATE");
        if (calendar == null)
            calendar = aub1.d("com.fireflycast.cast.metadata.RELEASE_DATE");
        if (calendar == null)
            calendar = aub1.d("com.fireflycast.cast.metadata.CREATION_DATE");
        if (calendar != null)
            bundle.putInt("android.media.metadata.YEAR", calendar.get(1));
        if (aub1.hasWebImage_c())
            bundle.putString("android.media.metadata.ARTWORK_URI", ((WebImage) aub1.getImages_b()
                    .get(0)).getUrl()
                    .toString());
        return bundle;
    }

    static Bundle getBundle_a(JSONObject jsonobject)
    {
        Bundle bundle;
        Iterator iterator;
        bundle = new Bundle();
        iterator = jsonobject.keys();

        while (iterator.hasNext()) {
            String s = null;
            Object obj = null;
            s = (String) iterator.next();
            try {
                obj = jsonobject.get(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (obj == JSONObject.NULL)
            {
                bundle.putParcelable(s, null);
            } else if (obj instanceof String)
            {
                bundle.putString(s, (String) obj);
            } else if (obj instanceof Boolean)
            {
                bundle.putBoolean(s, ((Boolean) obj).booleanValue());
            } else if (obj instanceof Integer)
            {
                bundle.putInt(s, ((Integer) obj).intValue());
            } else if (obj instanceof Long)
            {
                bundle.putLong(s, ((Long) obj).longValue());
            } else if (obj instanceof Double)
            {
                bundle.putDouble(s, ((Double) obj).doubleValue());
            } else if (obj instanceof JSONObject)
                bundle.putBundle(s, getBundle_a((JSONObject) obj));
        }
        return bundle;
    }

    static JSONObject getJsonObject_a(Bundle bundle, Set set)
    {
        JSONObject jsonobject;
        Iterator iterator;
        jsonobject = new JSONObject();
        iterator = bundle.keySet().iterator();
        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            if (set != null && set.contains(s)) {
                continue;
            }
            Object obj = bundle.get(s);
            if (!(obj instanceof Bundle)) {
                try
                {
                    jsonobject.put(s, obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    jsonobject.put(s, getJsonObject_a((Bundle) obj, set));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonobject;
    }
}
