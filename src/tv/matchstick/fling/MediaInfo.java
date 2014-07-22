package tv.matchstick.fling;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.client.internal.DoubleAndLongConverter;
import tv.matchstick.client.internal.JsonComparer;
import tv.matchstick.client.internal.MyStringBuilder;
import android.text.TextUtils;

/**
 * Contain all Media informations.
 *
 * Use MediaInfo.Builder to build an instance of this class. MediaInfo is used by RemoteMediaPlayer to load media on the receiver application.
 */
public final class MediaInfo {
	/**
	 * A stream type of "none".
	 */
	public static final int STREAM_TYPE_NONE = 0;

	/**
	 * Buffered stream type
	 */
	public static final int STREAM_TYPE_BUFFERED = 1;

	/**
	 * Live stream type
	 */
	public static final int STREAM_TYPE_LIVE = 2;

	/**
	 * Invalid stream type
	 */
	public static final int STREAM_TYPE_INVALID = -1;

	/**
	 * content id
	 */
	private final String mContentId;

	/**
	 * stream type
	 */
	private int mStreamType;

	/**
	 * content type
	 */
	private String mContentType;

	/**
	 * media meta data
	 */
	private MediaMetadata mMediaMetadata;

	/**
	 * media duration
	 */
	private long mDuration;

	/**
	 * custom data
	 */
	private JSONObject mCustomData;

	/**
	 * Create MediaInfo object with content
	 *
	 * @param contentId
	 * @throws IllegalArgumentException
	 */
	MediaInfo(String contentId) throws IllegalArgumentException {
		if (TextUtils.isEmpty(contentId))
			throw new IllegalArgumentException(
					"content ID cannot be null or empty");
		this.mContentId = contentId;
		this.mStreamType = STREAM_TYPE_INVALID;
	}

	/**
	 * Create MediaInfo with json object
	 *
	 * @param json
	 * @throws JSONException
	 */
	MediaInfo(JSONObject json) throws JSONException {
		this.mContentId = json.getString("contentId");
		String str = json.getString("streamType");
		if ("NONE".equals(str)) {
			this.mStreamType = STREAM_TYPE_NONE;
		} else if ("BUFFERED".equals(str)) {
			this.mStreamType = STREAM_TYPE_BUFFERED;
		} else if ("LIVE".equals(str)) {
			this.mStreamType = STREAM_TYPE_LIVE;
		} else {
			this.mStreamType = STREAM_TYPE_INVALID;
		}
		this.mContentType = json.getString("contentType");
		if (json.has("metadata")) {
			JSONObject localJSONObject = json.getJSONObject("metadata");
			int i = localJSONObject.getInt("metadataType");
			this.mMediaMetadata = new MediaMetadata(i);
			this.mMediaMetadata.writeToBundle(localJSONObject);
		}
		this.mDuration = DoubleAndLongConverter.double2long(json.optDouble(
				"duration", 0.0D));
		this.mCustomData = json.optJSONObject("customData");
	}

	/**
	 * Get content Id
	 *
	 * @return content Id
	 */
	public String getContentId() {
		return this.mContentId;
	}

	/**
	 * Set stream type
	 *
	 * @param streamType
	 * @throws IllegalArgumentException
	 */
	void setStreamType(int streamType) throws IllegalArgumentException {
		if ((streamType < -1) || (streamType > 2))
			throw new IllegalArgumentException("invalid stream type");
		this.mStreamType = streamType;
	}

	/**
	 * Get stream type.
	 *
	 * @return
	 */
	public int getStreamType() {
		return this.mStreamType;
	}

	/**
	 * Set content type
	 *
	 * @param contentType
	 * @throws IllegalArgumentException
	 */
	void setContentType(String contentType) throws IllegalArgumentException {
		if (TextUtils.isEmpty(contentType))
			throw new IllegalArgumentException(
					"content type cannot be null or empty");
		this.mContentType = contentType;
	}

	/**
	 * Get content (MIME) type
	 *
	 * @return
	 */
	public String getContentType() {
		return this.mContentType;
	}

	/**
	 * Set media meta data.
	 *
	 * @param mediaMetadata
	 */
	void setMediaMetada(MediaMetadata mediaMetadata) {
		this.mMediaMetadata = mediaMetadata;
	}

	/**
	 * Get media meta data.
	 *
	 * @return
	 */
	public MediaMetadata getMetadata() {
		return this.mMediaMetadata;
	}

	/**
	 * Set media stream duration.
	 *
	 * @param duration
	 * @throws IllegalArgumentException
	 */
	void setStreamDuration(long duration) throws IllegalArgumentException {
		if (duration < 0L)
			throw new IllegalArgumentException(
					"Stream duration cannot be negative");
		this.mDuration = duration;
	}

	/**
	 * Get Media stream duration,in milliseconds.
	 *
	 * @return
	 */
	public long getStreamDuration() {
		return this.mDuration;
	}

	/**
	 * Set custom data.
	 *
	 * @param customData
	 */
	void setCustomData(JSONObject customData) {
		this.mCustomData = customData;
	}

	/**
	 * Get custom data.
	 *
	 * @return
	 */
	public JSONObject getCustomData() {
		return this.mCustomData;
	}

	/**
	 * Check whether self's content is valid.
	 *
	 * @throws IllegalArgumentException
	 */
	void checkSelf() throws IllegalArgumentException {
		if (TextUtils.isEmpty(this.mContentId))
			throw new IllegalArgumentException(
					"content ID cannot be null or empty");
		if (TextUtils.isEmpty(this.mContentType))
			throw new IllegalArgumentException(
					"content type cannot be null or empty");
		if (this.mStreamType != -1) {
			return;
		}
		throw new IllegalArgumentException(
				"a valid stream type must be specified");
	}

	/**
	 * Build media info Json.
	 *
	 * @return
	 */
	public JSONObject buildJson() {
		JSONObject json = new JSONObject();
		try {
			json.put("contentId", this.mContentId);
			String str;
			switch (this.mStreamType) {
			case STREAM_TYPE_BUFFERED:
				str = "BUFFERED";
				break;
			case STREAM_TYPE_LIVE:
				str = "LIVE";
				break;
			case STREAM_TYPE_NONE:
			default:
				str = "NONE";
			}
			json.put("streamType", str);
			if (this.mContentType != null) {
				json.put("contentType", this.mContentType);
			}
			if (this.mMediaMetadata != null) {
				json.put("metadata", this.mMediaMetadata.buildJson());
			}
			json.put("duration",
					DoubleAndLongConverter.long2double(this.mDuration));
			if (this.mCustomData != null) {
				json.put("customData", this.mCustomData);
			}
		} catch (JSONException e) {
		}
		return json;
	}

	@Override
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof MediaInfo))
			return false;
		MediaInfo mediaInfo = (MediaInfo) other;
		if (((this.mCustomData == null) ? 1 : 0) != ((mediaInfo.mCustomData == null) ? 1
				: 0))
			return false;
		if ((this.mCustomData != null)
				&& (mediaInfo.mCustomData != null)
				&& (!(JsonComparer.compare(this.mCustomData,
						mediaInfo.mCustomData))))
			return false;
		return ((DoubleAndLongConverter.compare(this.mContentId,
				mediaInfo.mContentId))
				&& (this.mStreamType == mediaInfo.mStreamType)
				&& (DoubleAndLongConverter.compare(this.mContentType,
						mediaInfo.mContentType))
				&& (DoubleAndLongConverter.compare(this.mMediaMetadata,
						mediaInfo.mMediaMetadata)) && (this.mDuration == mediaInfo.mDuration));
	}

	@Override
	public int hashCode() {
		return MyStringBuilder.hashCode(new Object[] { this.mContentId,
				Integer.valueOf(this.mStreamType), this.mContentType,
				this.mMediaMetadata, Long.valueOf(this.mDuration),
				String.valueOf(this.mCustomData) });
	}

	/**
	 * MediaInfo builder, used to set all staffs of MediaInfo.
	 *
	 * MediaInfo is used by RemoteMediaPlayer to load media on the receiver application. 
	 */
	public static class Builder {
		private final MediaInfo mediaInfo;

		/**
		 * Create one MediaInfo object with the given content Id
		 *
		 * @param contentId
		 * @throws IllegalArgumentException
		 */
		public Builder(String contentId) throws IllegalArgumentException {
			if (TextUtils.isEmpty(contentId))
				throw new IllegalArgumentException("Content ID cannot be empty");
			this.mediaInfo = new MediaInfo(contentId);
		}

		/**
		 * Set stream type
		 *
		 * @param streamType
		 * @return
		 * @throws IllegalArgumentException
		 */
		public Builder setStreamType(int streamType)
				throws IllegalArgumentException {
			this.mediaInfo.setStreamType(streamType);
			return this;
		}

		/**
		 * Set content type
		 *
		 * @param contentType
		 * @return
		 * @throws IllegalArgumentException
		 */
		public Builder setContentType(String contentType)
				throws IllegalArgumentException {
			this.mediaInfo.setContentType(contentType);
			return this;
		}

		/**
		 * Set meta data
		 *
		 * @param metadata
		 * @return
		 */
		public Builder setMetadata(MediaMetadata metadata) {
			this.mediaInfo.setMediaMetada(metadata);
			return this;
		}

		/**
		 * Set media stream duration,in milliseconds.
		 *
		 * @param duration
		 * @return
		 * @throws IllegalArgumentException
		 */
		public Builder setStreamDuration(long duration)
				throws IllegalArgumentException {
			this.mediaInfo.setStreamDuration(duration);
			return this;
		}

		/**
		 * Set custom data
		 *
		 * @param customData
		 * @return
		 */
		public Builder setCustomData(JSONObject customData) {
			this.mediaInfo.setCustomData(customData);
			return this;
		}

		/**
		 * Check and return the valid MediaInfo object
		 *
		 * @return MediaInfo object
		 * @throws IllegalArgumentException
		 */
		public MediaInfo build() throws IllegalArgumentException {
			this.mediaInfo.checkSelf();
			return this.mediaInfo;
		}
	}
}
