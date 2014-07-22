
package tv.matchstick.server.fling;

import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.server.common.images.WebImage;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

final class FlingMediaManagerHelper {
    static Bundle createMetadataBundle(MediaInfo mediaInfo)
    {
        Bundle bundle = new Bundle();
        bundle.putLong("android.media.metadata.DURATION", mediaInfo.mDuration);
        MediaMetadata aub1 = mediaInfo.mMediaMetadata;
        String s = aub1.getString("tv.matchstick.fling.metadata.ALBUM_ARTIST");
        if (s != null)
            bundle.putString("android.media.metadata.ALBUM_ARTIST", s);
        String s1 = aub1.getString("tv.matchstick.fling.metadata.ALBUM_TITLE");
        if (s1 != null)
            bundle.putString("android.media.metadata.ALBUM_TITLE", s1);
        String s2 = aub1.getString("tv.matchstick.fling.metadata.ARTIST");
        if (s2 == null)
            s2 = aub1.getString("tv.matchstick.fling.metadata.STUDIO");
        if (s2 != null)
            bundle.putString("android.media.metadata.ARTIST", s2);
        String s3 = aub1.getString("tv.matchstick.fling.metadata.COMPOSER");
        if (s3 != null)
            bundle.putString("android.media.metadata.COMPOSER", s3);
        if (aub1.a("tv.matchstick.fling.metadata.DISC_NUMBER"))
            bundle.putInt("android.media.metadata.DISC_NUMBER",
                    aub1.c("tv.matchstick.fling.metadata.DISC_NUMBER"));
        String s4 = aub1.getString("tv.matchstick.fling.metadata.TITLE");
        if (s4 != null)
            bundle.putString("android.media.metadata.TITLE", s4);
        if (aub1.a("tv.matchstick.fling.metadata.TRACK_NUMBER"))
            bundle.putInt("android.media.metadata.TRACK_NUMBER",
                    aub1.c("tv.matchstick.fling.metadata.TRACK_NUMBER"));
        Calendar calendar = aub1.d("tv.matchstick.fling.metadata.BROADCAST_DATE");
        if (calendar == null)
            calendar = aub1.d("tv.matchstick.fling.metadata.RELEASE_DATE");
        if (calendar == null)
            calendar = aub1.d("tv.matchstick.fling.metadata.CREATION_DATE");
        if (calendar != null)
            bundle.putInt("android.media.metadata.YEAR", calendar.get(1));
        if (aub1.hasWebImage())
            bundle.putString("android.media.metadata.ARTWORK_URI", ((WebImage) aub1.getImages()
                    .get(0)).getUrl()
                    .toString());
        return bundle;
    }

    static Bundle getBundle(JSONObject jsonobject)
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
                bundle.putBundle(s, getBundle((JSONObject) obj));
        }
        return bundle;
    }

    static JSONObject getJsonObject(Bundle bundle, Set set)
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
                    jsonobject.put(s, getJsonObject((Bundle) obj, set));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        return jsonobject;
    }
}
