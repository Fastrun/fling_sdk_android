
package tv.matchstick.server.fling;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.server.common.images.WebImage;

public final class MediaMetadata {
    public static final int MEDIA_TYPE_GENERIC = 0;
    public static final int MEDIA_TYPE_MOVIE = 1;
    public static final int MEDIA_TYPE_TV_SHOW = 2;
    public static final int MEDIA_TYPE_MUSIC_TRACK = 3;
    public static final int MEDIA_TYPE_PHOTO = 4;
    public static final int MEDIA_TYPE_USER = 100;
    private static final String[] a = {
            null, "String", "int", "double",
            "ISO-8601 date String"
    };
    private static final MetadataMap mMetadataMap = new MetadataMap()
            .add("tv.matchstick.fling.metadata.CREATION_DATE",
                    "creationDateTime", 4)
            .add("tv.matchstick.fling.metadata.RELEASE_DATE",
                    "releaseDate", 4)
            .add("tv.matchstick.fling.metadata.BROADCAST_DATE",
                    "originalAirdate", 4)
            .add("tv.matchstick.fling.metadata.TITLE", "title", 1)
            .add("tv.matchstick.fling.metadata.SUBTITLE", "subtitle", 1)
            .add("tv.matchstick.fling.metadata.ARTIST", "artist", 1)
            .add("tv.matchstick.fling.metadata.ALBUM_ARTIST",
                    "albumArtist", 1)
            .add("tv.matchstick.fling.metadata.ALBUM_TITLE", "albumName",
                    1)
            .add("tv.matchstick.fling.metadata.COMPOSER", "composer", 1)
            .add("tv.matchstick.fling.metadata.DISC_NUMBER",
                    "discNumber", 2)
            .add("tv.matchstick.fling.metadata.TRACK_NUMBER",
                    "trackNumber", 2)
            .add("tv.matchstick.fling.metadata.SEASON_NUMBER", "season",
                    2)
            .add("tv.matchstick.fling.metadata.EPISODE_NUMBER",
                    "episode", 2)
            .add("tv.matchstick.fling.metadata.SERIES_TITLE",
                    "seriesTitle", 1)
            .add("tv.matchstick.fling.metadata.STUDIO", "studio", 1)
            .add("tv.matchstick.fling.metadata.WIDTH", "width", 2)
            .add("tv.matchstick.fling.metadata.HEIGHT", "height", 2)
            .add("tv.matchstick.fling.metadata.LOCATION_NAME",
                    "location", 1)
            .add("tv.matchstick.fling.metadata.LOCATION_LATITUDE",
                    "latitude", 3)
            .add("tv.matchstick.fling.metadata.LOCATION_LONGITUDE",
                    "longitude", 3);
    private final List mWebImageList = new ArrayList();
    private final Bundle mBundle = new Bundle();
    private int mMediaDataType;
    public static final String KEY_CREATION_DATE = "tv.matchstick.fling.metadata.CREATION_DATE";
    public static final String KEY_RELEASE_DATE = "tv.matchstick.fling.metadata.RELEASE_DATE";
    public static final String KEY_BROADCAST_DATE = "tv.matchstick.fling.metadata.BROADCAST_DATE";
    public static final String KEY_TITLE = "tv.matchstick.fling.metadata.TITLE";
    public static final String KEY_SUBTITLE = "tv.matchstick.fling.metadata.SUBTITLE";
    public static final String KEY_ARTIST = "tv.matchstick.fling.metadata.ARTIST";
    public static final String KEY_ALBUM_ARTIST = "tv.matchstick.fling.metadata.ALBUM_ARTIST";
    public static final String KEY_ALBUM_TITLE = "tv.matchstick.fling.metadata.ALBUM_TITLE";
    public static final String KEY_COMPOSER = "tv.matchstick.fling.metadata.COMPOSER";
    public static final String KEY_DISC_NUMBER = "tv.matchstick.fling.metadata.DISC_NUMBER";
    public static final String KEY_TRACK_NUMBER = "tv.matchstick.fling.metadata.TRACK_NUMBER";
    public static final String KEY_SEASON_NUMBER = "tv.matchstick.fling.metadata.SEASON_NUMBER";
    public static final String KEY_EPISODE_NUMBER = "tv.matchstick.fling.metadata.EPISODE_NUMBER";
    public static final String KEY_SERIES_TITLE = "tv.matchstick.fling.metadata.SERIES_TITLE";
    public static final String KEY_STUDIO = "tv.matchstick.fling.metadata.STUDIO";
    public static final String KEY_WIDTH = "tv.matchstick.fling.metadata.WIDTH";
    public static final String KEY_HEIGHT = "tv.matchstick.fling.metadata.HEIGHT";
    public static final String KEY_LOCATION_NAME = "tv.matchstick.fling.metadata.LOCATION_NAME";
    public static final String KEY_LOCATION_LATITUDE = "tv.matchstick.fling.metadata.LOCATION_LATITUDE";
    public static final String KEY_LOCATION_LONGITUDE = "tv.matchstick.fling.metadata.LOCATION_LONGITUDE";

    public MediaMetadata() {
        this(0);
    }

    public MediaMetadata(int paramInt) {
        this.mMediaDataType = paramInt;
    }

    public int getMediaType() {
        return mMediaDataType;
    }

    private void a(JSONObject paramJSONObject, String[] paramArrayOfString) {
        try {
            int i = paramArrayOfString.length;
            for (int j = 0; j < i; j++) {
                String str1;

                str1 = paramArrayOfString[j];
                if (!this.mBundle.containsKey(str1))
                    continue;

                switch (mMetadataMap.b(str1)) {
                    case 1:
                    case 4:
                        paramJSONObject.put(mMetadataMap.a(str1), this.mBundle.getString(str1));
                        break;
                    case 2:
                        paramJSONObject.put(mMetadataMap.a(str1), this.mBundle.getInt(str1));
                        break;
                    case 3:
                        paramJSONObject.put(mMetadataMap.a(str1), this.mBundle.getDouble(str1));
                }
            }
            Iterator localIterator = this.mBundle.keySet().iterator();
            while (localIterator.hasNext()) {
                String str2 = (String) localIterator.next();
                if (!str2.startsWith("com.google.")) {
                    Object localObject = this.mBundle.get(str2);
                    if ((localObject instanceof String))
                        paramJSONObject.put(str2, localObject);
                    else if ((localObject instanceof Integer))
                        paramJSONObject.put(str2, localObject);
                    else if ((localObject instanceof Double))
                        paramJSONObject.put(str2, localObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean a(Bundle paramBundle1, Bundle paramBundle2) {
        if (paramBundle1.size() != paramBundle2.size())
            return false;
        Iterator localIterator = paramBundle1.keySet().iterator();
        while (localIterator.hasNext()) {
            String str = (String) localIterator.next();
            Object localObject1 = paramBundle1.get(str);
            Object localObject2 = paramBundle2.get(str);
            if (((localObject1 instanceof Bundle))
                    && ((localObject2 instanceof Bundle))
                    && (!a((Bundle) localObject1, (Bundle) localObject2)))
                return false;
            if (localObject1 == null) {
                if ((localObject2 != null) || (!paramBundle2.containsKey(str)))
                    return false;
            } else if (!localObject1.equals(localObject2))
                return false;
        }
        return true;
    }

    private static void b(String paramString, int paramInt) {
        int i = mMetadataMap.b(paramString);
        if ((i != paramInt) && (i != 0))
            throw new IllegalArgumentException("Value for " + paramString
                    + " must be a " + a[paramInt]);
    }

    private void b(JSONObject paramJSONObject, String[] paramArrayOfString) {
        HashSet localHashSet = new HashSet(Arrays.asList(paramArrayOfString));
        try {
            Iterator localIterator = paramJSONObject.keys();
            while (localIterator.hasNext()) {
                String str1 = (String) localIterator.next();
                String str2 = (String) mMetadataMap.a.get(str1);
                if (str2 != null) {
                    boolean bool = localHashSet.contains(str2);
                    if (!bool) {
                        continue;
                    } else {
                        try {
                            Object localObject1 = paramJSONObject.get(str1);
                            if (localObject1 != null)
                                switch (mMetadataMap.b(str2)) {
                                    case 1:
                                        if ((localObject1 instanceof String))
                                            this.mBundle.putString(str2,
                                                    (String) localObject1);
                                        break;
                                    case 4:
                                        if (((localObject1 instanceof String))
                                                && (MetadataUtils
                                                        .a((String) localObject1) != null))
                                            this.mBundle.putString(str2,
                                                    (String) localObject1);
                                        break;
                                    case 2:
                                        if ((localObject1 instanceof Integer))
                                            this.mBundle.putInt(str2,
                                                    ((Integer) localObject1)
                                                            .intValue());
                                        break;
                                    case 3:
                                        if ((localObject1 instanceof Double)) {
                                            this.mBundle.putDouble(str2,
                                                    ((Double) localObject1)
                                                            .doubleValue());

                                        }
                                        break;
                                }
                        } catch (JSONException localJSONException2) {
                        }
                    }
                } else {
                    Object localObject2 = paramJSONObject.get(str1);
                    if (localObject2 != null) {
                        if ((localObject2 instanceof String))
                            this.mBundle.putString(str2, (String) localObject2);
                        else if ((localObject2 instanceof Integer))
                            this.mBundle.putInt(str2,
                                    ((Integer) localObject2).intValue());
                        else if ((localObject2 instanceof Double))
                            this.mBundle.putDouble(str2,
                                    ((Double) localObject2).doubleValue());
                    }
                }
            }
        } catch (JSONException localJSONException1) {
        }
    }

    public final JSONObject getMetaData() {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("metadataType", this.mMediaDataType);
            MetadataUtils.fillImages(localJSONObject, this.mWebImageList);
            switch (this.mMediaDataType) {
                case 0:
                    a(localJSONObject, new String[] {
                            "tv.matchstick.fling.metadata.TITLE",
                            "tv.matchstick.fling.metadata.ARTIST",
                            "tv.matchstick.fling.metadata.SUBTITLE",
                            "tv.matchstick.fling.metadata.RELEASE_DATE"
                    });
                    return localJSONObject;
                case 1:
                    a(localJSONObject, new String[] {
                            "tv.matchstick.fling.metadata.TITLE",
                            "tv.matchstick.fling.metadata.STUDIO",
                            "tv.matchstick.fling.metadata.SUBTITLE",
                            "tv.matchstick.fling.metadata.RELEASE_DATE"
                    });
                    return localJSONObject;
                case 2:
                    a(localJSONObject, new String[] {
                            "tv.matchstick.fling.metadata.TITLE",
                            "tv.matchstick.fling.metadata.SERIES_TITLE",
                            "tv.matchstick.fling.metadata.SEASON_NUMBER",
                            "tv.matchstick.fling.metadata.EPISODE_NUMBER",
                            "tv.matchstick.fling.metadata.BROADCAST_DATE"
                    });
                    return localJSONObject;
                case 3:
                    a(localJSONObject, new String[] {
                            "tv.matchstick.fling.metadata.TITLE",
                            "tv.matchstick.fling.metadata.ARTIST",
                            "tv.matchstick.fling.metadata.ALBUM_TITLE",
                            "tv.matchstick.fling.metadata.ALBUM_ARTIST",
                            "tv.matchstick.fling.metadata.COMPOSER",
                            "tv.matchstick.fling.metadata.TRACK_NUMBER",
                            "tv.matchstick.fling.metadata.DISC_NUMBER",
                            "tv.matchstick.fling.metadata.RELEASE_DATE"
                    });
                    return localJSONObject;
                case 4:
                    a(localJSONObject,
                            new String[] {
                                    "tv.matchstick.fling.metadata.TITLE",
                                    "tv.matchstick.fling.metadata.ARTIST",
                                    "tv.matchstick.fling.metadata.LOCATION_NAME",
                                    "tv.matchstick.fling.metadata.LOCATION_LATITUDE",
                                    "tv.matchstick.fling.metadata.LOCATION_LONGITUDE",
                                    "tv.matchstick.fling.metadata.WIDTH",
                                    "tv.matchstick.fling.metadata.HEIGHT",
                                    "tv.matchstick.fling.metadata.CREATION_DATE"
                            });
                default:
                    a(localJSONObject, new String[0]);
                    return localJSONObject;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public final void addImage(WebImage paramWebImage) {
        this.mWebImageList.add(paramWebImage);
    }

    public final void a(String paramString, int paramInt) {
        b(paramString, 2);
        this.mBundle.putInt(paramString, paramInt);
    }

    public final void putString(String paramString1, String paramString2) {
        b(paramString1, 1);
        this.mBundle.putString(paramString1, paramString2);
    }

    public final void a(String paramString, Calendar paramCalendar) {
        b(paramString, 4);
        this.mBundle.putString(paramString, MetadataUtils.getTime(paramCalendar));
    }

    public final void a(JSONObject paramJSONObject) {
        this.mBundle.clear();
        this.mWebImageList.clear();
        this.mMediaDataType = 0;

        try {
            this.mMediaDataType = paramJSONObject.getInt("metadataType");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MetadataUtils.a(this.mWebImageList, paramJSONObject);
        switch (this.mMediaDataType) {
            case 0:
                b(paramJSONObject, new String[] {
                        "tv.matchstick.fling.metadata.TITLE",
                        "tv.matchstick.fling.metadata.ARTIST",
                        "tv.matchstick.fling.metadata.SUBTITLE",
                        "tv.matchstick.fling.metadata.RELEASE_DATE"
                });
                return;
            case 1:
                b(paramJSONObject, new String[] {
                        "tv.matchstick.fling.metadata.TITLE",
                        "tv.matchstick.fling.metadata.STUDIO",
                        "tv.matchstick.fling.metadata.SUBTITLE",
                        "tv.matchstick.fling.metadata.RELEASE_DATE"
                });
                return;
            case 2:
                b(paramJSONObject, new String[] {
                        "tv.matchstick.fling.metadata.TITLE",
                        "tv.matchstick.fling.metadata.SERIES_TITLE",
                        "tv.matchstick.fling.metadata.SEASON_NUMBER",
                        "tv.matchstick.fling.metadata.EPISODE_NUMBER",
                        "tv.matchstick.fling.metadata.BROADCAST_DATE"
                });
                return;
            case 3:
                b(paramJSONObject, new String[] {
                        "tv.matchstick.fling.metadata.TITLE",
                        "tv.matchstick.fling.metadata.ALBUM_TITLE",
                        "tv.matchstick.fling.metadata.ARTIST",
                        "tv.matchstick.fling.metadata.ALBUM_ARTIST",
                        "tv.matchstick.fling.metadata.COMPOSER",
                        "tv.matchstick.fling.metadata.TRACK_NUMBER",
                        "tv.matchstick.fling.metadata.DISC_NUMBER",
                        "tv.matchstick.fling.metadata.RELEASE_DATE"
                });
                return;
            case 4:
                b(paramJSONObject,
                        new String[] {
                                "tv.matchstick.fling.metadata.TITLE",
                                "tv.matchstick.fling.metadata.ARTIST",
                                "tv.matchstick.fling.metadata.LOCATION_NAME",
                                "tv.matchstick.fling.metadata.LOCATION_LATITUDE",
                                "tv.matchstick.fling.metadata.LOCATION_LONGITUDE",
                                "tv.matchstick.fling.metadata.WIDTH",
                                "tv.matchstick.fling.metadata.HEIGHT",
                                "tv.matchstick.fling.metadata.CREATION_DATE"
                        });
            default:
                b(paramJSONObject, new String[0]);
                return;

        }
    }

    public final boolean a(String paramString) {
        return this.mBundle.containsKey(paramString);
    }

    public final String getString(String paramString) {
        b(paramString, 1);
        return this.mBundle.getString(paramString);
    }

    public final List getImages() {
        return this.mWebImageList;
    }

    public final int c(String paramString) {
        b(paramString, 2);
        return this.mBundle.getInt(paramString);
    }

    public final boolean hasWebImage() {
        return (this.mWebImageList != null) && (!this.mWebImageList.isEmpty());
    }

    public final Calendar d(String paramString) {
        b(paramString, 4);
        String str = this.mBundle.getString(paramString);
        if (str != null)
            return MetadataUtils.a(str);
        return null;
    }

    public final boolean equals(Object paramObject) {
        if (this == paramObject) {
            return true;
        }

        MediaMetadata localaub;
        if (!(paramObject instanceof MediaMetadata)) {
            return false;
        }

        localaub = (MediaMetadata) paramObject;
        if ((a(this.mBundle, localaub.mBundle))
                && (this.mWebImageList.equals(localaub.mWebImageList))) {
            return true;
        }

        return false;
    }

    public final int hashCode() {
        Iterator localIterator = this.mBundle.keySet().iterator();
        String str;
        int i = 0;
        for (i = 17; localIterator.hasNext(); i = i * 31
                + this.mBundle.get(str).hashCode())
            str = (String) localIterator.next();
        return i * 31 + this.mWebImageList.hashCode();
    }
}
