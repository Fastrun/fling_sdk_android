
package com.fireflycast.server.cast;

import android.os.Bundle;

import com.fireflycast.server.common.images.WebImage;

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
    private static final MetadataMap mMetadataMap_b = new MetadataMap()
            .add_a("com.fireflycast.cast.metadata.CREATION_DATE",
                    "creationDateTime", 4)
            .add_a("com.fireflycast.cast.metadata.RELEASE_DATE",
                    "releaseDate", 4)
            .add_a("com.fireflycast.cast.metadata.BROADCAST_DATE",
                    "originalAirdate", 4)
            .add_a("com.fireflycast.cast.metadata.TITLE", "title", 1)
            .add_a("com.fireflycast.cast.metadata.SUBTITLE", "subtitle", 1)
            .add_a("com.fireflycast.cast.metadata.ARTIST", "artist", 1)
            .add_a("com.fireflycast.cast.metadata.ALBUM_ARTIST",
                    "albumArtist", 1)
            .add_a("com.fireflycast.cast.metadata.ALBUM_TITLE", "albumName",
                    1)
            .add_a("com.fireflycast.cast.metadata.COMPOSER", "composer", 1)
            .add_a("com.fireflycast.cast.metadata.DISC_NUMBER",
                    "discNumber", 2)
            .add_a("com.fireflycast.cast.metadata.TRACK_NUMBER",
                    "trackNumber", 2)
            .add_a("com.fireflycast.cast.metadata.SEASON_NUMBER", "season",
                    2)
            .add_a("com.fireflycast.cast.metadata.EPISODE_NUMBER",
                    "episode", 2)
            .add_a("com.fireflycast.cast.metadata.SERIES_TITLE",
                    "seriesTitle", 1)
            .add_a("com.fireflycast.cast.metadata.STUDIO", "studio", 1)
            .add_a("com.fireflycast.cast.metadata.WIDTH", "width", 2)
            .add_a("com.fireflycast.cast.metadata.HEIGHT", "height", 2)
            .add_a("com.fireflycast.cast.metadata.LOCATION_NAME",
                    "location", 1)
            .add_a("com.fireflycast.cast.metadata.LOCATION_LATITUDE",
                    "latitude", 3)
            .add_a("com.fireflycast.cast.metadata.LOCATION_LONGITUDE",
                    "longitude", 3);
    private final List mWebImageList_c = new ArrayList();
    private final Bundle mBundle_d = new Bundle();
    private int mMediaDataType_e;
    public static final String KEY_CREATION_DATE = "com.fireflycast.cast.metadata.CREATION_DATE";
    public static final String KEY_RELEASE_DATE = "com.fireflycast.cast.metadata.RELEASE_DATE";
    public static final String KEY_BROADCAST_DATE = "com.fireflycast.cast.metadata.BROADCAST_DATE";
    public static final String KEY_TITLE = "com.fireflycast.cast.metadata.TITLE";
    public static final String KEY_SUBTITLE = "com.fireflycast.cast.metadata.SUBTITLE";
    public static final String KEY_ARTIST = "com.fireflycast.cast.metadata.ARTIST";
    public static final String KEY_ALBUM_ARTIST = "com.fireflycast.cast.metadata.ALBUM_ARTIST";
    public static final String KEY_ALBUM_TITLE = "com.fireflycast.cast.metadata.ALBUM_TITLE";
    public static final String KEY_COMPOSER = "com.fireflycast.cast.metadata.COMPOSER";
    public static final String KEY_DISC_NUMBER = "com.fireflycast.cast.metadata.DISC_NUMBER";
    public static final String KEY_TRACK_NUMBER = "com.fireflycast.cast.metadata.TRACK_NUMBER";
    public static final String KEY_SEASON_NUMBER = "com.fireflycast.cast.metadata.SEASON_NUMBER";
    public static final String KEY_EPISODE_NUMBER = "com.fireflycast.cast.metadata.EPISODE_NUMBER";
    public static final String KEY_SERIES_TITLE = "com.fireflycast.cast.metadata.SERIES_TITLE";
    public static final String KEY_STUDIO = "com.fireflycast.cast.metadata.STUDIO";
    public static final String KEY_WIDTH = "com.fireflycast.cast.metadata.WIDTH";
    public static final String KEY_HEIGHT = "com.fireflycast.cast.metadata.HEIGHT";
    public static final String KEY_LOCATION_NAME = "com.fireflycast.cast.metadata.LOCATION_NAME";
    public static final String KEY_LOCATION_LATITUDE = "com.fireflycast.cast.metadata.LOCATION_LATITUDE";
    public static final String KEY_LOCATION_LONGITUDE = "com.fireflycast.cast.metadata.LOCATION_LONGITUDE";

    public MediaMetadata() {
        this(0);
    }

    public MediaMetadata(int paramInt) {
        this.mMediaDataType_e = paramInt;
    }

    public int getMediaType() {
        return mMediaDataType_e;
    }

    private void a(JSONObject paramJSONObject, String[] paramArrayOfString) {
        try {
            int i = paramArrayOfString.length;
            for (int j = 0; j < i; j++) {
                String str1;

                str1 = paramArrayOfString[j];
                if (!this.mBundle_d.containsKey(str1))
                    continue;

                switch (mMetadataMap_b.b(str1)) {
                    case 1:
                    case 4:
                        paramJSONObject.put(mMetadataMap_b.a(str1), this.mBundle_d.getString(str1));
                        break;
                    case 2:
                        paramJSONObject.put(mMetadataMap_b.a(str1), this.mBundle_d.getInt(str1));
                        break;
                    case 3:
                        paramJSONObject.put(mMetadataMap_b.a(str1), this.mBundle_d.getDouble(str1));
                }
            }
            Iterator localIterator = this.mBundle_d.keySet().iterator();
            while (localIterator.hasNext()) {
                String str2 = (String) localIterator.next();
                if (!str2.startsWith("com.google.")) {
                    Object localObject = this.mBundle_d.get(str2);
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
        int i = mMetadataMap_b.b(paramString);
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
                String str2 = (String) mMetadataMap_b.a.get(str1);
                if (str2 != null) {
                    boolean bool = localHashSet.contains(str2);
                    if (!bool) {
                        continue;
                    } else {
                        try {
                            Object localObject1 = paramJSONObject.get(str1);
                            if (localObject1 != null)
                                switch (mMetadataMap_b.b(str2)) {
                                    case 1:
                                        if ((localObject1 instanceof String))
                                            this.mBundle_d.putString(str2,
                                                    (String) localObject1);
                                        break;
                                    case 4:
                                        if (((localObject1 instanceof String))
                                                && (MetadataUtils
                                                        .a((String) localObject1) != null))
                                            this.mBundle_d.putString(str2,
                                                    (String) localObject1);
                                        break;
                                    case 2:
                                        if ((localObject1 instanceof Integer))
                                            this.mBundle_d.putInt(str2,
                                                    ((Integer) localObject1)
                                                            .intValue());
                                        break;
                                    case 3:
                                        if ((localObject1 instanceof Double)) {
                                            this.mBundle_d.putDouble(str2,
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
                            this.mBundle_d.putString(str2, (String) localObject2);
                        else if ((localObject2 instanceof Integer))
                            this.mBundle_d.putInt(str2,
                                    ((Integer) localObject2).intValue());
                        else if ((localObject2 instanceof Double))
                            this.mBundle_d.putDouble(str2,
                                    ((Double) localObject2).doubleValue());
                    }
                }
            }
        } catch (JSONException localJSONException1) {
        }
    }

    public final JSONObject a() {
        JSONObject localJSONObject = new JSONObject();
        try {
            localJSONObject.put("metadataType", this.mMediaDataType_e);
            MetadataUtils.fillImages_a(localJSONObject, this.mWebImageList_c);
            switch (this.mMediaDataType_e) {
                case 0:
                    a(localJSONObject, new String[] {
                            "com.fireflycast.cast.metadata.TITLE",
                            "com.fireflycast.cast.metadata.ARTIST",
                            "com.fireflycast.cast.metadata.SUBTITLE",
                            "com.fireflycast.cast.metadata.RELEASE_DATE"
                    });
                    return localJSONObject;
                case 1:
                    a(localJSONObject, new String[] {
                            "com.fireflycast.cast.metadata.TITLE",
                            "com.fireflycast.cast.metadata.STUDIO",
                            "com.fireflycast.cast.metadata.SUBTITLE",
                            "com.fireflycast.cast.metadata.RELEASE_DATE"
                    });
                    return localJSONObject;
                case 2:
                    a(localJSONObject, new String[] {
                            "com.fireflycast.cast.metadata.TITLE",
                            "com.fireflycast.cast.metadata.SERIES_TITLE",
                            "com.fireflycast.cast.metadata.SEASON_NUMBER",
                            "com.fireflycast.cast.metadata.EPISODE_NUMBER",
                            "com.fireflycast.cast.metadata.BROADCAST_DATE"
                    });
                    return localJSONObject;
                case 3:
                    a(localJSONObject, new String[] {
                            "com.fireflycast.cast.metadata.TITLE",
                            "com.fireflycast.cast.metadata.ARTIST",
                            "com.fireflycast.cast.metadata.ALBUM_TITLE",
                            "com.fireflycast.cast.metadata.ALBUM_ARTIST",
                            "com.fireflycast.cast.metadata.COMPOSER",
                            "com.fireflycast.cast.metadata.TRACK_NUMBER",
                            "com.fireflycast.cast.metadata.DISC_NUMBER",
                            "com.fireflycast.cast.metadata.RELEASE_DATE"
                    });
                    return localJSONObject;
                case 4:
                    a(localJSONObject,
                            new String[] {
                                    "com.fireflycast.cast.metadata.TITLE",
                                    "com.fireflycast.cast.metadata.ARTIST",
                                    "com.fireflycast.cast.metadata.LOCATION_NAME",
                                    "com.fireflycast.cast.metadata.LOCATION_LATITUDE",
                                    "com.fireflycast.cast.metadata.LOCATION_LONGITUDE",
                                    "com.fireflycast.cast.metadata.WIDTH",
                                    "com.fireflycast.cast.metadata.HEIGHT",
                                    "com.fireflycast.cast.metadata.CREATION_DATE"
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

    public final void addImage_a(WebImage paramWebImage) {
        this.mWebImageList_c.add(paramWebImage);
    }

    public final void a(String paramString, int paramInt) {
        b(paramString, 2);
        this.mBundle_d.putInt(paramString, paramInt);
    }

    public final void putString_a(String paramString1, String paramString2) {
        b(paramString1, 1);
        this.mBundle_d.putString(paramString1, paramString2);
    }

    public final void a(String paramString, Calendar paramCalendar) {
        b(paramString, 4);
        this.mBundle_d.putString(paramString, MetadataUtils.getTime_a(paramCalendar));
    }

    public final void a(JSONObject paramJSONObject) {
        this.mBundle_d.clear();
        this.mWebImageList_c.clear();
        this.mMediaDataType_e = 0;

        try {
            this.mMediaDataType_e = paramJSONObject.getInt("metadataType");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MetadataUtils.a(this.mWebImageList_c, paramJSONObject);
        switch (this.mMediaDataType_e) {
            case 0:
                b(paramJSONObject, new String[] {
                        "com.fireflycast.cast.metadata.TITLE",
                        "com.fireflycast.cast.metadata.ARTIST",
                        "com.fireflycast.cast.metadata.SUBTITLE",
                        "com.fireflycast.cast.metadata.RELEASE_DATE"
                });
                return;
            case 1:
                b(paramJSONObject, new String[] {
                        "com.fireflycast.cast.metadata.TITLE",
                        "com.fireflycast.cast.metadata.STUDIO",
                        "com.fireflycast.cast.metadata.SUBTITLE",
                        "com.fireflycast.cast.metadata.RELEASE_DATE"
                });
                return;
            case 2:
                b(paramJSONObject, new String[] {
                        "com.fireflycast.cast.metadata.TITLE",
                        "com.fireflycast.cast.metadata.SERIES_TITLE",
                        "com.fireflycast.cast.metadata.SEASON_NUMBER",
                        "com.fireflycast.cast.metadata.EPISODE_NUMBER",
                        "com.fireflycast.cast.metadata.BROADCAST_DATE"
                });
                return;
            case 3:
                b(paramJSONObject, new String[] {
                        "com.fireflycast.cast.metadata.TITLE",
                        "com.fireflycast.cast.metadata.ALBUM_TITLE",
                        "com.fireflycast.cast.metadata.ARTIST",
                        "com.fireflycast.cast.metadata.ALBUM_ARTIST",
                        "com.fireflycast.cast.metadata.COMPOSER",
                        "com.fireflycast.cast.metadata.TRACK_NUMBER",
                        "com.fireflycast.cast.metadata.DISC_NUMBER",
                        "com.fireflycast.cast.metadata.RELEASE_DATE"
                });
                return;
            case 4:
                b(paramJSONObject,
                        new String[] {
                                "com.fireflycast.cast.metadata.TITLE",
                                "com.fireflycast.cast.metadata.ARTIST",
                                "com.fireflycast.cast.metadata.LOCATION_NAME",
                                "com.fireflycast.cast.metadata.LOCATION_LATITUDE",
                                "com.fireflycast.cast.metadata.LOCATION_LONGITUDE",
                                "com.fireflycast.cast.metadata.WIDTH",
                                "com.fireflycast.cast.metadata.HEIGHT",
                                "com.fireflycast.cast.metadata.CREATION_DATE"
                        });
            default:
                b(paramJSONObject, new String[0]);
                return;

        }
    }

    public final boolean a(String paramString) {
        return this.mBundle_d.containsKey(paramString);
    }

    public final String getString_b(String paramString) {
        b(paramString, 1);
        return this.mBundle_d.getString(paramString);
    }

    public final List getImages_b() {
        return this.mWebImageList_c;
    }

    public final int c(String paramString) {
        b(paramString, 2);
        return this.mBundle_d.getInt(paramString);
    }

    public final boolean hasWebImage_c() {
        return (this.mWebImageList_c != null) && (!this.mWebImageList_c.isEmpty());
    }

    public final Calendar d(String paramString) {
        b(paramString, 4);
        String str = this.mBundle_d.getString(paramString);
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
        if ((a(this.mBundle_d, localaub.mBundle_d))
                && (this.mWebImageList_c.equals(localaub.mWebImageList_c))) {
            return true;
        }

        return false;
    }

    public final int hashCode() {
        Iterator localIterator = this.mBundle_d.keySet().iterator();
        String str;
        int i = 0;
        for (i = 17; localIterator.hasNext(); i = i * 31
                + this.mBundle_d.get(str).hashCode())
            str = (String) localIterator.next();
        return i * 31 + this.mWebImageList_c.hashCode();
    }
}
