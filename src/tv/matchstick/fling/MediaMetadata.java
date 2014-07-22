package tv.matchstick.fling;

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

import tv.matchstick.client.internal.MetadataUtils;
import tv.matchstick.fling.images.WebImage;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Media meta data.
 * 
 * Metadata has a media type, an optional list of images, and a collection of metadata fields. Keys for common metadata fields are predefined as constants, but the application is free to define and use additional fields of its own.
 * <p>
 * The values of the predefined fields have predefined types. For example, a track number is an int and a creation date is a String containing an ISO-8601 representation of a date and time. Attempting to store a value of an incorrect type in a field will result in a IllegalArgumentException.
 * <p>
 * Note that the Fling protocol limits which metadata fields can be used for a given media type. When a MediaMetadata object is serialized to JSON for delivery to a Fling receiver, any predefined fields which are not supported for a given media type will not be included in the serialized form, but any application-defined fields will always be included. 
 */
public class MediaMetadata {
	/**
	 * Generic media
	 */
	public static final int MEDIA_TYPE_GENERIC = 0;

	/**
	 * Movie
	 */
	public static final int MEDIA_TYPE_MOVIE = 1;

	/**
	 * TV SHOW
	 */
	public static final int MEDIA_TYPE_TV_SHOW = 2;

	/**
	 * Music
	 */
	public static final int MEDIA_TYPE_MUSIC_TRACK = 3;

	/**
	 * Photo
	 */
	public static final int MEDIA_TYPE_PHOTO = 4;

	/**
	 * User defined
	 */
	public static final int MEDIA_TYPE_USER = 100;

	/**
	 * null
	 */
	static final int VALUE_TYPE_NULL = 0;

	/**
	 * string
	 */
	static final int VALUE_TYPE_STRING = 1;

	/**
	 * int
	 */
	static final int VALUE_TYPE_INT = 2;

	/**
	 * double
	 */
	static final int VALUE_TYPE_DOUBLE = 3;

	/**
	 * ISO_8601_STRING
	 */
	static final int VALUE_TYPE_ISO_8601_STRING = 4;

	/**
	 * values map
	 */
	private static final String[] VALUE_TYPES = { null, "String", "int",
			"double", "ISO-8601 date String" };

	/**
	 * Value checker
	 */
	private static final ValueTypesChecker mChecker = new ValueTypesChecker()
			.addKeyValue("tv.matchstick.fling.metadata.CREATION_DATE",
					"creationDateTime", VALUE_TYPE_ISO_8601_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.RELEASE_DATE",
					"releaseDate", VALUE_TYPE_ISO_8601_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.BROADCAST_DATE",
					"originalAirdate", VALUE_TYPE_ISO_8601_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.TITLE", "title",
					VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.SUBTITLE", "subtitle",
					VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.ARTIST", "artist",
					VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.ALBUM_ARTIST",
					"albumArtist", VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.ALBUM_TITLE",
					"albumName", VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.COMPOSER", "composer",
					VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.DISC_NUMBER",
					"discNumber", VALUE_TYPE_INT)
			.addKeyValue("tv.matchstick.fling.metadata.TRACK_NUMBER",
					"trackNumber", VALUE_TYPE_INT)
			.addKeyValue("tv.matchstick.fling.metadata.SEASON_NUMBER",
					"season", VALUE_TYPE_INT)
			.addKeyValue("tv.matchstick.fling.metadata.EPISODE_NUMBER",
					"episode", VALUE_TYPE_INT)
			.addKeyValue("tv.matchstick.fling.metadata.SERIES_TITLE",
					"seriesTitle", VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.STUDIO", "studio",
					VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.WIDTH", "width",
					VALUE_TYPE_INT)
			.addKeyValue("tv.matchstick.fling.metadata.HEIGHT", "height",
					VALUE_TYPE_INT)
			.addKeyValue("tv.matchstick.fling.metadata.LOCATION_NAME",
					"location", VALUE_TYPE_STRING)
			.addKeyValue("tv.matchstick.fling.metadata.LOCATION_LATITUDE",
					"latitude", VALUE_TYPE_DOUBLE)
			.addKeyValue("tv.matchstick.fling.metadata.LOCATION_LONGITUDE",
					"longitude", VALUE_TYPE_DOUBLE);

	/**
	 * web images
	 */
	private final List<WebImage> mImages;

	/**
	 * data
	 */
	private final Bundle mBundle;

	/**
	 * media type
	 */
	private int mMediaType;

	/**
	 * media keys
	 */
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

	/**
	 * Create default media data with MEDIA_TYPE_GENERIC
	 */
	public MediaMetadata() {
		this(MEDIA_TYPE_GENERIC);
	}

	/**
	 * Create the given media type data
	 * 
	 * @param mediaType
	 */
	public MediaMetadata(int mediaType) {
		this.mImages = new ArrayList<WebImage>();
		this.mBundle = new Bundle();
		this.mMediaType = mediaType;
	}

	/**
	 * Get media type
	 *
	 * @return
	 */
	public int getMediaType() {
		return this.mMediaType;
	}

	/**
	 * Clear all resources of this object
	 */
	public void clear() {
		this.mBundle.clear();
		this.mImages.clear();
	}

	/**
	 * Check whether the object contains a field with the given key.
	 *
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		return this.mBundle.containsKey(key);
	}

	/**
	 * Get a set of keys for all fields that are present in the object.
	 *
	 * @return
	 */
	public Set<String> keySet() {
		return this.mBundle.keySet();
	}

	/**
	 * Put Sting data
	 *
	 * @param key
	 * @param value
	 */
	public void putString(String key, String value) {
		checkValueType(key, VALUE_TYPE_STRING);
		this.mBundle.putString(key, value);
	}

	/**
	 * Get String data
	 *
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		checkValueType(key, VALUE_TYPE_STRING);
		return this.mBundle.getString(key);
	}

	/**
	 * Put int data
	 *
	 * @param key
	 * @param value
	 */
	public void putInt(String key, int value) {
		checkValueType(key, VALUE_TYPE_INT);
		this.mBundle.putInt(key, value);
	}

	/**
	 * Get int data
	 *
	 * @param key
	 * @return
	 */
	public int getInt(String key) {
		checkValueType(key, VALUE_TYPE_INT);
		return this.mBundle.getInt(key);
	}

	/**
	 * Put double data
	 *
	 * @param key
	 * @param value
	 */
	public void putDouble(String key, double value) {
		checkValueType(key, VALUE_TYPE_DOUBLE);
		this.mBundle.putDouble(key, value);
	}

	/**
	 * Get double data
	 *
	 * @param key
	 * @return
	 */
	public double getDouble(String key) {
		checkValueType(key, VALUE_TYPE_DOUBLE);
		return this.mBundle.getDouble(key);
	}

	/**
	 * Put date data
	 *
	 * @param key
	 * @param value
	 */
	public void putDate(String key, Calendar value) {
		checkValueType(key, VALUE_TYPE_ISO_8601_STRING);
		this.mBundle.putString(key, MetadataUtils.getDateByCalendar(value));
	}

	/**
	 * Get date data
	 *
	 * @param key
	 * @return
	 */
	public Calendar getDate(String key) {
		checkValueType(key, VALUE_TYPE_ISO_8601_STRING);
		String str = this.mBundle.getString(key);
		return ((str != null) ? MetadataUtils.getCalendarByDate(str) : null);
	}

	/**
	 * Get date data and convert it to String type
	 *
	 * @param key
	 * @return
	 */
	public String getDateAsString(String key) {
		checkValueType(key, VALUE_TYPE_ISO_8601_STRING);
		return this.mBundle.getString(key);
	}

	/**
	 * Check the specific key's value type
	 * 
	 * @param key
	 * @param valueType
	 * @throws IllegalArgumentException
	 */
	private void checkValueType(String key, int valueType)
			throws IllegalArgumentException {
		if (TextUtils.isEmpty(key))
			throw new IllegalArgumentException(
					"null and empty keys are not allowed");
		int type = mChecker.getValueTypeByBundleKey(key);
		if ((type == valueType) || (type == 0)) {
			return;
		}
		throw new IllegalArgumentException("Value for " + key + " must be a "
				+ VALUE_TYPES[valueType]);
	}

	/**
	 * Get Jsong data from meta data
	 *
	 * @return
	 */
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("metadataType", this.mMediaType);
		} catch (JSONException e) {
		}
		MetadataUtils.writeToJson(json, this.mImages);
		switch (this.mMediaType) {
		case MEDIA_TYPE_GENERIC:
			writeToJson(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.ARTIST",
					"tv.matchstick.fling.metadata.SUBTITLE",
					"tv.matchstick.fling.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_MOVIE:
			writeToJson(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.STUDIO",
					"tv.matchstick.fling.metadata.SUBTITLE",
					"tv.matchstick.fling.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_TV_SHOW:
			writeToJson(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.SERIES_TITLE",
					"tv.matchstick.fling.metadata.SEASON_NUMBER",
					"tv.matchstick.fling.metadata.EPISODE_NUMBER",
					"tv.matchstick.fling.metadata.BROADCAST_DATE" });
			break;
		case MEDIA_TYPE_MUSIC_TRACK:
			writeToJson(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.ARTIST",
					"tv.matchstick.fling.metadata.ALBUM_TITLE",
					"tv.matchstick.fling.metadata.ALBUM_ARTIST",
					"tv.matchstick.fling.metadata.COMPOSER",
					"tv.matchstick.fling.metadata.TRACK_NUMBER",
					"tv.matchstick.fling.metadata.DISC_NUMBER",
					"tv.matchstick.fling.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_PHOTO:
			writeToJson(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.ARTIST",
					"tv.matchstick.fling.metadata.LOCATION_NAME",
					"tv.matchstick.fling.metadata.LOCATION_LATITUDE",
					"tv.matchstick.fling.metadata.LOCATION_LONGITUDE",
					"tv.matchstick.fling.metadata.WIDTH",
					"tv.matchstick.fling.metadata.HEIGHT",
					"tv.matchstick.fling.metadata.CREATION_DATE" });
			break;
		default:
			writeToJson(json, new String[0]);
		}
		return json;
	}

	/**
	 * Write Json to media data
	 *
	 * @param json
	 */
	public void writeToBundle(JSONObject json) {
		clear();
		this.mMediaType = MEDIA_TYPE_GENERIC;
		try {
			this.mMediaType = json.getInt("metadataType");
		} catch (JSONException localJSONException) {
		}
		MetadataUtils.getWebImageListFromJson(this.mImages, json);
		switch (this.mMediaType) {
		case MEDIA_TYPE_GENERIC:
			writeToBundle(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.ARTIST",
					"tv.matchstick.fling.metadata.SUBTITLE",
					"tv.matchstick.fling.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_MOVIE:
			writeToBundle(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.STUDIO",
					"tv.matchstick.fling.metadata.SUBTITLE",
					"tv.matchstick.fling.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_TV_SHOW:
			writeToBundle(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.SERIES_TITLE",
					"tv.matchstick.fling.metadata.SEASON_NUMBER",
					"tv.matchstick.fling.metadata.EPISODE_NUMBER",
					"tv.matchstick.fling.metadata.BROADCAST_DATE" });
			break;
		case MEDIA_TYPE_MUSIC_TRACK:
			writeToBundle(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.ALBUM_TITLE",
					"tv.matchstick.fling.metadata.ARTIST",
					"tv.matchstick.fling.metadata.ALBUM_ARTIST",
					"tv.matchstick.fling.metadata.COMPOSER",
					"tv.matchstick.fling.metadata.TRACK_NUMBER",
					"tv.matchstick.fling.metadata.DISC_NUMBER",
					"tv.matchstick.fling.metadata.RELEASE_DATE" });
			break;
		case MEDIA_TYPE_PHOTO:
			writeToBundle(json, new String[] {
					"tv.matchstick.fling.metadata.TITLE",
					"tv.matchstick.fling.metadata.ARTIST",
					"tv.matchstick.fling.metadata.LOCATION_NAME",
					"tv.matchstick.fling.metadata.LOCATION_LATITUDE",
					"tv.matchstick.fling.metadata.LOCATION_LONGITUDE",
					"tv.matchstick.fling.metadata.WIDTH",
					"tv.matchstick.fling.metadata.HEIGHT",
					"tv.matchstick.fling.metadata.CREATION_DATE" });
			break;
		default:
			writeToBundle(json, new String[0]);
		}
	}

	/**
	 * Create Json object by keys from meida meta data
	 *
	 * @param json
	 * @param keys
	 */
	private void writeToJson(JSONObject json, String[] keys) {
		try {
			for (String key : keys) {
				if (!(this.mBundle.containsKey(key))) {
					continue;
				}
				int type = mChecker.getValueTypeByBundleKey(key);
				switch (type) {
				case VALUE_TYPE_STRING:
				case VALUE_TYPE_ISO_8601_STRING:
					json.put(mChecker.getJsonKeyByBundleKey(key),
							this.mBundle.getString(key));
					break;
				case VALUE_TYPE_INT:
					json.put(mChecker.getJsonKeyByBundleKey(key),
							this.mBundle.getInt(key));
					break;
				case VALUE_TYPE_DOUBLE:
					json.put(mChecker.getJsonKeyByBundleKey(key),
							this.mBundle.getDouble(key));
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

	/**
	 * Write Json data to media meta data by keys
	 *
	 * @param json
	 * @param keys
	 */
	private void writeToBundle(JSONObject json, String[] keys) {
		HashSet localHashSet = new HashSet(Arrays.asList(keys));
		try {
			Iterator jsonKeys = json.keys();
			while (jsonKeys.hasNext()) {
				String jsonKey = (String) jsonKeys.next();
				if ("metadataType".equals(jsonKey)) {
					continue;
				}
				String str2 = mChecker.getBundleKeyByJsonKey(jsonKey);
				if (str2 != null) {
					if (localHashSet.contains(str2))
						try {
							Object localObject1 = json.get(jsonKey);
							if (localObject1 != null) {
								switch (mChecker.getValueTypeByBundleKey(str2)) {
								case VALUE_TYPE_STRING:
									if (localObject1 instanceof String)
										this.mBundle.putString(str2,
												(String) localObject1);
									break;
								case VALUE_TYPE_ISO_8601_STRING:
									if (localObject1 instanceof String) {
										Calendar localCalendar = MetadataUtils
												.getCalendarByDate((String) localObject1);
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

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof MediaMetadata))
			return false;
		MediaMetadata localMediaMetadata = (MediaMetadata) other;
		return ((compareBundle(this.mBundle, localMediaMetadata.mBundle)) && (this.mImages
				.equals(localMediaMetadata.mImages)));
	}

	@Override
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

	/**
	 * Get Images
	 *
	 * @return
	 */
	public List<WebImage> getImages() {
		return this.mImages;
	}

	/**
	 * Check whether there're images
	 *
	 * @return
	 */
	public boolean hasImages() {
		return ((this.mImages != null) && (!(this.mImages.isEmpty())));
	}

	/**
	 * Clear images
	 */
	public void clearImages() {
		this.mImages.clear();
	}

	/**
	 * Add an image to the list of images.
	 *
	 * @param image
	 */
	public void addImage(WebImage image) {
		this.mImages.add(image);
	}

	/**
	 * Compare bundle data
	 *
	 * @param one
	 * @param other
	 * @return
	 */
	private boolean compareBundle(Bundle one, Bundle other) {
		if (one.size() != other.size())
			return false;
		Set localSet = one.keySet();
		Iterator localIterator = localSet.iterator();
		while (localIterator.hasNext()) {
			String str = (String) localIterator.next();
			Object localObject1 = one.get(str);
			Object localObject2 = other.get(str);
			if ((localObject1 instanceof Bundle)
					&& (localObject2 instanceof Bundle)
					&& (!(compareBundle((Bundle) localObject1,
							(Bundle) localObject2))))
				return false;
			if (localObject1 == null)
				if ((localObject2 != null) || (!(other.containsKey(str))))
					return false;
				else if (!(localObject1.equals(localObject2)))
					return false;
		}
		return true;
	}

	/**
	 * Value type checker utils
	 */
	private static class ValueTypesChecker {
		private final Map<String, String> bundleKeyToJsonKey = new HashMap<String, String>();
		private final Map<String, String> jsonKeyToBundleKey = new HashMap<String, String>();
		private final Map<String, Integer> bundleKeyToValueType = new HashMap<String, Integer>();

		public ValueTypesChecker addKeyValue(String bundleKey, String jsonKey,
				int valueType) {
			this.bundleKeyToJsonKey.put(bundleKey, jsonKey);
			this.jsonKeyToBundleKey.put(jsonKey, bundleKey);
			this.bundleKeyToValueType
					.put(bundleKey, Integer.valueOf(valueType));
			return this;
		}

		public String getJsonKeyByBundleKey(String key) {
			return ((String) this.bundleKeyToJsonKey.get(key));
		}

		public String getBundleKeyByJsonKey(String key) {
			return ((String) this.jsonKeyToBundleKey.get(key));
		}

		public int getValueTypeByBundleKey(String key) {
			Integer i = (Integer) this.bundleKeyToValueType.get(key);
			return ((i != null) ? i.intValue() : 0);
		}
	}

}
