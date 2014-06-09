package com.fireflycast.cast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.text.TextUtils;

import com.fireflycast.cast.images.WebImage;
import com.fireflycast.client.internal.MetadataUtils;

/*
 * MediaMetadata.smali : OK
 */
public class MediaMetadata {

	public static final int MEDIA_TYPE_GENERIC = 0;
	public static final int MEDIA_TYPE_MOVIE = 1;
	public static final int MEDIA_TYPE_TV_SHOW = 2;
	public static final int MEDIA_TYPE_MUSIC_TRACK = 3;
	public static final int MEDIA_TYPE_PHOTO = 4;
	public static final int MEDIA_TYPE_USER = 100;

	static final int VALUE_TYPE_NULL = 0;
	static final int VALUE_TYPE_STRING = 1;
	static final int VALUE_TYPE_INT = 2;
	static final int VALUE_TYPE_DOUBLE = 3;
	static final int VALUE_TYPE_ISO_8601_STRING = 4;
	private static final String[] VALUE_TYPES_wR = { null, "String", "int",
			"double", "ISO-8601 date String" };

	private static final ValueTypesChecker_a mChecker_wS = new ValueTypesChecker_a()
			.addKeyValue_a("com.fireflycast.cast.metadata.CREATION_DATE",
					"creationDateTime", VALUE_TYPE_ISO_8601_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.RELEASE_DATE", "releaseDate",
					VALUE_TYPE_ISO_8601_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.BROADCAST_DATE",
					"originalAirdate", VALUE_TYPE_ISO_8601_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.TITLE", "title",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.SUBTITLE", "subtitle",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.ARTIST", "artist",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.ALBUM_ARTIST", "albumArtist",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.ALBUM_TITLE", "albumName",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.COMPOSER", "composer",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.DISC_NUMBER", "discNumber",
					VALUE_TYPE_INT)
			.addKeyValue_a("com.fireflycast.cast.metadata.TRACK_NUMBER", "trackNumber",
					VALUE_TYPE_INT)
			.addKeyValue_a("com.fireflycast.cast.metadata.SEASON_NUMBER", "season",
					VALUE_TYPE_INT)
			.addKeyValue_a("com.fireflycast.cast.metadata.EPISODE_NUMBER", "episode",
					VALUE_TYPE_INT)
			.addKeyValue_a("com.fireflycast.cast.metadata.SERIES_TITLE", "seriesTitle",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.STUDIO", "studio",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.WIDTH", "width", VALUE_TYPE_INT)
			.addKeyValue_a("com.fireflycast.cast.metadata.HEIGHT", "height", VALUE_TYPE_INT)
			.addKeyValue_a("com.fireflycast.cast.metadata.LOCATION_NAME", "location",
					VALUE_TYPE_STRING)
			.addKeyValue_a("com.fireflycast.cast.metadata.LOCATION_LATITUDE", "latitude",
					VALUE_TYPE_DOUBLE)
			.addKeyValue_a("com.fireflycast.cast.metadata.LOCATION_LONGITUDE", "longitude",
					VALUE_TYPE_DOUBLE);
	private final List<WebImage> mImages;
	private final Bundle mBundle;
	private int mMediaType;
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

	public MediaMetadata(int mediaType) {
		this.mImages = new ArrayList();
		this.mBundle = new Bundle();
		this.mMediaType = mediaType;
	}

	public int getMediaType() {
		return this.mMediaType;
	}

	public void clear() {
		this.mBundle.clear();
		this.mImages.clear();
	}

	public boolean containsKey(String key) {
		return this.mBundle.containsKey(key);
	}

	public Set<String> keySet() {
		return this.mBundle.keySet();
	}

	public void putString(String key, String value) {
		checkValueType_d(key, VALUE_TYPE_STRING);
		this.mBundle.putString(key, value);
	}

	public String getString(String key) {
		checkValueType_d(key, VALUE_TYPE_STRING);
		return this.mBundle.getString(key);
	}

	public void putInt(String key, int value) {
		checkValueType_d(key, VALUE_TYPE_INT);
		this.mBundle.putInt(key, value);
	}

	public int getInt(String key) {
		checkValueType_d(key, VALUE_TYPE_INT);
		return this.mBundle.getInt(key);
	}

	public void putDouble(String key, double value) {
		checkValueType_d(key, VALUE_TYPE_DOUBLE);
		this.mBundle.putDouble(key, value);
	}

	public double getDouble(String key) {
		checkValueType_d(key, VALUE_TYPE_DOUBLE);
		return this.mBundle.getDouble(key);
	}

	public void putDate(String key, Calendar value) {
		checkValueType_d(key, VALUE_TYPE_ISO_8601_STRING);
		this.mBundle
				.putString(key, MetadataUtils.getDateByCalendar_a(value));
	}

	public Calendar getDate(String key) {
		checkValueType_d(key, VALUE_TYPE_ISO_8601_STRING);
		String str = this.mBundle.getString(key);
		return ((str != null) ? MetadataUtils.getCalendarByDate_V(str)
				: null);
	}

	public String getDateAsString(String key) {
		checkValueType_d(key, VALUE_TYPE_ISO_8601_STRING);
		return this.mBundle.getString(key);
	}

	private void checkValueType_d(String key, int valueType)
			throws IllegalArgumentException {
		if (TextUtils.isEmpty(key))
			throw new IllegalArgumentException(
					"null and empty keys are not allowed");
		int type = mChecker_wS.getValueTypeByBundleKey_O(key);
		if ((type == valueType) || (type == 0)) {
			return;
		}
		throw new IllegalArgumentException("Value for " + key + " must be a "
				+ VALUE_TYPES_wR[valueType]);
	}

	public JSONObject buildJson_cT() {
		JSONObject json = new JSONObject();
		try {
			json.put("metadataType", this.mMediaType);
		} catch (JSONException e) {
		}
		MetadataUtils.writeToJson_a(json, this.mImages);
		switch (this.mMediaType) {
		case MEDIA_TYPE_GENERIC:
			writeToJson_a(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.ARTIST",
					"com.fireflycast.cast.metadata.SUBTITLE",
					"com.fireflycast.cast.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_MOVIE:
			writeToJson_a(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.STUDIO",
					"com.fireflycast.cast.metadata.SUBTITLE",
					"com.fireflycast.cast.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_TV_SHOW:
			writeToJson_a(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.SERIES_TITLE",
					"com.fireflycast.cast.metadata.SEASON_NUMBER",
					"com.fireflycast.cast.metadata.EPISODE_NUMBER",
					"com.fireflycast.cast.metadata.BROADCAST_DATE" });
			break;
		case MEDIA_TYPE_MUSIC_TRACK:
			writeToJson_a(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.ARTIST",
					"com.fireflycast.cast.metadata.ALBUM_TITLE",
					"com.fireflycast.cast.metadata.ALBUM_ARTIST",
					"com.fireflycast.cast.metadata.COMPOSER",
					"com.fireflycast.cast.metadata.TRACK_NUMBER",
					"com.fireflycast.cast.metadata.DISC_NUMBER",
					"com.fireflycast.cast.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_PHOTO:
			writeToJson_a(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.ARTIST",
					"com.fireflycast.cast.metadata.LOCATION_NAME",
					"com.fireflycast.cast.metadata.LOCATION_LATITUDE",
					"com.fireflycast.cast.metadata.LOCATION_LONGITUDE",
					"com.fireflycast.cast.metadata.WIDTH",
					"com.fireflycast.cast.metadata.HEIGHT",
					"com.fireflycast.cast.metadata.CREATION_DATE" });
			break;
		default:
			writeToJson_a(json, new String[0]);
		}
		return json;
	}

	public void writeToBundle_b(JSONObject json) {
		clear();
		this.mMediaType = MEDIA_TYPE_GENERIC;
		try {
			this.mMediaType = json.getInt("metadataType");
		} catch (JSONException localJSONException) {
		}
		MetadataUtils.getWebImageListFromJson_a(this.mImages, json);
		switch (this.mMediaType) {
		case MEDIA_TYPE_GENERIC:
			writeToBundle_b(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.ARTIST",
					"com.fireflycast.cast.metadata.SUBTITLE",
					"com.fireflycast.cast.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_MOVIE:
			writeToBundle_b(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.STUDIO",
					"com.fireflycast.cast.metadata.SUBTITLE",
					"com.fireflycast.cast.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_TV_SHOW:
			writeToBundle_b(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.SERIES_TITLE",
					"com.fireflycast.cast.metadata.SEASON_NUMBER",
					"com.fireflycast.cast.metadata.EPISODE_NUMBER",
					"com.fireflycast.cast.metadata.BROADCAST_DATE" });
			break;
		case MEDIA_TYPE_MUSIC_TRACK:
			writeToBundle_b(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.ALBUM_TITLE",
					"com.fireflycast.cast.metadata.ARTIST",
					"com.fireflycast.cast.metadata.ALBUM_ARTIST",
					"com.fireflycast.cast.metadata.COMPOSER",
					"com.fireflycast.cast.metadata.TRACK_NUMBER",
					"com.fireflycast.cast.metadata.DISC_NUMBER",
					"com.fireflycast.cast.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_PHOTO:
			writeToBundle_b(json, new String[] {
					"com.fireflycast.cast.metadata.TITLE",
					"com.fireflycast.cast.metadata.ARTIST",
					"com.fireflycast.cast.metadata.LOCATION_NAME",
					"com.fireflycast.cast.metadata.LOCATION_LATITUDE",
					"com.fireflycast.cast.metadata.LOCATION_LONGITUDE",
					"com.fireflycast.cast.metadata.WIDTH",
					"com.fireflycast.cast.metadata.HEIGHT",
					"com.fireflycast.cast.metadata.CREATION_DATE" });
			break;
		default:
			writeToBundle_b(json, new String[0]);
		}
	}

	private void writeToJson_a(JSONObject json, String[] keys) {
		try {
			for (String key : keys) {
				if (!(this.mBundle.containsKey(key))) {
					continue;
				}
				int type = mChecker_wS.getValueTypeByBundleKey_O(key);
				switch (type) {
				case VALUE_TYPE_STRING:
				case VALUE_TYPE_ISO_8601_STRING:
					json.put(mChecker_wS.getJsonKeyByBundleKey_M(key), this.mBundle.getString(key));
					break;
				case VALUE_TYPE_INT:
					json.put(mChecker_wS.getJsonKeyByBundleKey_M(key), this.mBundle.getInt(key));
					break;
				case VALUE_TYPE_DOUBLE:
					json.put(mChecker_wS.getJsonKeyByBundleKey_M(key), this.mBundle.getDouble(key));
				}
			}
			Iterator<String> bundleKeys = this.mBundle.keySet().iterator();
			while (bundleKeys.hasNext()) {
				String key = bundleKeys.next();
				if (!(key.startsWith("com.google."))) {
					Object value = this.mBundle.get(key);
					if (value instanceof String) {
						json.put(key, value);
					} else if (value instanceof Integer) {
						json.put(key, value);
					} else if (value instanceof Double) {
						json.put(key, value);
					}
				}
			}
		} catch (JSONException e) {
		}
	}

	private void writeToBundle_b(JSONObject json, String[] keys) {
		HashSet localHashSet = new HashSet(Arrays.asList(keys));
		try {
			Iterator jsonKeys = json.keys();
			while (jsonKeys.hasNext()) {
				String jsonKey = (String) jsonKeys.next();
				if ("metadataType".equals(jsonKey)) {
					continue;
				}
				String str2 = mChecker_wS.getBundleKeyByJsonKeyN(jsonKey);
				if (str2 != null) {
					if (localHashSet.contains(str2))
						try {
							Object localObject1 = json.get(jsonKey);
							if (localObject1 != null) {
								switch (mChecker_wS.getValueTypeByBundleKey_O(str2)) {
								case VALUE_TYPE_STRING:
									if (localObject1 instanceof String)
										this.mBundle.putString(str2,
												(String) localObject1);
									break;
								case VALUE_TYPE_ISO_8601_STRING:
									if (localObject1 instanceof String) {
										Calendar localCalendar = MetadataUtils
												.getCalendarByDate_V((String) localObject1);
										if (localCalendar != null)
											this.mBundle.putString(str2,
													(String) localObject1);
									}
									break;
								case VALUE_TYPE_INT:
									if (localObject1 instanceof Integer)
										this.mBundle.putInt(str2,
												((Integer) localObject1)
														.intValue());
									break;
								case VALUE_TYPE_DOUBLE:
									if (localObject1 instanceof Double)
										this.mBundle.putDouble(str2,
												((Double) localObject1)
														.doubleValue());
								}
							}
						} catch (JSONException e) {
						}
				} else {
					Object localObject2 = json.get(jsonKey);
					if (localObject2 instanceof String)
						this.mBundle.putString(jsonKey, (String) localObject2);
					else if (localObject2 instanceof Integer)
						this.mBundle.putInt(jsonKey,
								((Integer) localObject2).intValue());
					else if (localObject2 instanceof Double)
						this.mBundle.putDouble(jsonKey,
								((Double) localObject2).doubleValue());
				}
			}
		} catch (JSONException localJSONException1) {
		}
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof MediaMetadata))
			return false;
		MediaMetadata localMediaMetadata = (MediaMetadata) other;
		return ((compareBundle_a(this.mBundle, localMediaMetadata.mBundle)) && (this.mImages
				.equals(localMediaMetadata.mImages)));
	}

	public int hashCode() {
		int i = 17;
		Set localSet = this.mBundle.keySet();
		Iterator localIterator = localSet.iterator();
		while (localIterator.hasNext()) {
			String str = (String) localIterator.next();
			i = 31 * i + this.mBundle.get(str).hashCode();
		}
		i = 31 * i + this.mImages.hashCode();
		return i;
	}

	public List<WebImage> getImages() {
		return this.mImages;
	}

	public boolean hasImages() {
		return ((this.mImages != null) && (!(this.mImages.isEmpty())));
	}

	public void clearImages() {
		this.mImages.clear();
	}

	public void addImage(WebImage image) {
		this.mImages.add(image);
	}

	private boolean compareBundle_a(Bundle b1, Bundle b2) {
		if (b1.size() != b2.size())
			return false;
		Set localSet = b1.keySet();
		Iterator localIterator = localSet.iterator();
		while (localIterator.hasNext()) {
			String str = (String) localIterator.next();
			Object localObject1 = b1.get(str);
			Object localObject2 = b2.get(str);
			if ((localObject1 instanceof Bundle)
					&& (localObject2 instanceof Bundle)
					&& (!(compareBundle_a((Bundle) localObject1,
							(Bundle) localObject2))))
				return false;
			if (localObject1 == null)
				if ((localObject2 != null) || (!(b2.containsKey(str))))
					return false;
				else if (!(localObject1.equals(localObject2)))
					return false;
		}
		return true;
	}

	/*
	 * MediaMetadata\$a.smali : OK
	 */
	private static class ValueTypesChecker_a {
		private final Map<String, String> bundleKeyToJsonKey_wV = new HashMap();
		private final Map<String, String> jsonKeyToBundleKey_wW = new HashMap();
		private final Map<String, Integer> bundleKeyToValueType_wX = new HashMap();

		public ValueTypesChecker_a addKeyValue_a(String bundleKey, String jsonKey, int valueType) {
			this.bundleKeyToJsonKey_wV.put(bundleKey, jsonKey);
			this.jsonKeyToBundleKey_wW.put(jsonKey, bundleKey);
			this.bundleKeyToValueType_wX.put(bundleKey, Integer.valueOf(valueType));
			return this;
		}

		public String getJsonKeyByBundleKey_M(String key1) {
			return ((String) this.bundleKeyToJsonKey_wV.get(key1));
		}

		public String getBundleKeyByJsonKeyN(String key2) {
			return ((String) this.jsonKeyToBundleKey_wW.get(key2));
		}

		public int getValueTypeByBundleKey_O(String key) {
			Integer i = (Integer) this.bundleKeyToValueType_wX.get(key);
			return ((i != null) ? i.intValue() : 0);
		}
	}

}
