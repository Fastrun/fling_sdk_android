package com.fireflycast.cast;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.fireflycast.client.internal.DoubleAndLongConverter;
import com.fireflycast.client.internal.MyStringBuilder;
import com.fireflycast.client.internal.JsonComparer;

/*
 * MediaInfo.smali : OK
 */
public final class MediaInfo {
	public static final int STREAM_TYPE_NONE = 0;
	public static final int STREAM_TYPE_BUFFERED = 1;
	public static final int STREAM_TYPE_LIVE = 2;
	public static final int STREAM_TYPE_INVALID = -1;
	private final String mContentId_wK;
	private int mStreamType_wL;
	private String mContentType_wM;
	private MediaMetadata mMediaMetadata_wN;
	private long mDuration_wO;
	private JSONObject mCustomData_wP;

	MediaInfo(String contentId) throws IllegalArgumentException {
		if (TextUtils.isEmpty(contentId))
			throw new IllegalArgumentException(
					"content ID cannot be null or empty");
		this.mContentId_wK = contentId;
		this.mStreamType_wL = STREAM_TYPE_INVALID;
	}

	MediaInfo(JSONObject json) throws JSONException {
		this.mContentId_wK = json.getString("contentId");
		String str = json.getString("streamType");
		if ("NONE".equals(str)) {
			this.mStreamType_wL = STREAM_TYPE_NONE;
		} else if ("BUFFERED".equals(str)) {
			this.mStreamType_wL = STREAM_TYPE_BUFFERED;
		} else if ("LIVE".equals(str)) {
			this.mStreamType_wL = STREAM_TYPE_LIVE;
		} else {
			this.mStreamType_wL = STREAM_TYPE_INVALID;
		}
		this.mContentType_wM = json.getString("contentType");
		if (json.has("metadata")) {
			JSONObject localJSONObject = json.getJSONObject("metadata");
			int i = localJSONObject.getInt("metadataType");
			this.mMediaMetadata_wN = new MediaMetadata(i);
			this.mMediaMetadata_wN.writeToBundle_b(localJSONObject);
		}
		this.mDuration_wO = DoubleAndLongConverter.double2long_b(json.optDouble("duration", 0.0D));
		this.mCustomData_wP = json.optJSONObject("customData");
	}

	public String getContentId() {
		return this.mContentId_wK;
	}

	void setStreamType(int streamType) throws IllegalArgumentException {
		if ((streamType < -1) || (streamType > 2))
			throw new IllegalArgumentException("invalid stream type");
		this.mStreamType_wL = streamType;
	}

	public int getStreamType() {
		return this.mStreamType_wL;
	}

	void setContentType(String contentType) throws IllegalArgumentException {
		if (TextUtils.isEmpty(contentType))
			throw new IllegalArgumentException(
					"content type cannot be null or empty");
		this.mContentType_wM = contentType;
	}

	public String getContentType() {
		return this.mContentType_wM;
	}

	void setMediaMetada_a(MediaMetadata mediaMetadata) {
		this.mMediaMetadata_wN = mediaMetadata;
	}

	public MediaMetadata getMetadata() {
		return this.mMediaMetadata_wN;
	}

	void setStreamDuration_j(long duration) throws IllegalArgumentException {
		if (duration < 0L)
			throw new IllegalArgumentException(
					"Stream duration cannot be negative");
		this.mDuration_wO = duration;
	}

	public long getStreamDuration() {
		return this.mDuration_wO;
	}

	void setCustomData_a(JSONObject customData) {
		this.mCustomData_wP = customData;
	}

	public JSONObject getCustomData() {
		return this.mCustomData_wP;
	}

	void checkSelf_cS() throws IllegalArgumentException {
		if (TextUtils.isEmpty(this.mContentId_wK))
			throw new IllegalArgumentException(
					"content ID cannot be null or empty");
		if (TextUtils.isEmpty(this.mContentType_wM))
			throw new IllegalArgumentException(
					"content type cannot be null or empty");
		if (this.mStreamType_wL != -1) {
			return;
		}
		throw new IllegalArgumentException(
				"a valid stream type must be specified");
	}

	public JSONObject buildJson_cT() {
		JSONObject json = new JSONObject();
		try {
			json.put("contentId", this.mContentId_wK);
			String str;
			switch (this.mStreamType_wL) {
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
			if (this.mContentType_wM != null) {
				json.put("contentType", this.mContentType_wM);
			}
			if (this.mMediaMetadata_wN != null) {
				json.put("metadata", this.mMediaMetadata_wN.buildJson_cT());
			}
			json.put("duration", DoubleAndLongConverter.long2double_l(this.mDuration_wO));
			if (this.mCustomData_wP != null) {
				json.put("customData", this.mCustomData_wP);
			}
		} catch (JSONException e) {
		}
		return json;
	}

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (!(other instanceof MediaInfo))
			return false;
		MediaInfo mediaInfo = (MediaInfo) other;
		if (((this.mCustomData_wP == null) ? 1 : 0) != ((mediaInfo.mCustomData_wP == null) ? 1
				: 0))
			return false;
		if ((this.mCustomData_wP != null)
				&& (mediaInfo.mCustomData_wP != null)
				&& (!(JsonComparer.compare_d(this.mCustomData_wP, mediaInfo.mCustomData_wP))))
			return false;
		return ((DoubleAndLongConverter.compare_a(this.mContentId_wK, mediaInfo.mContentId_wK))
				&& (this.mStreamType_wL == mediaInfo.mStreamType_wL)
				&& (DoubleAndLongConverter.compare_a(this.mContentType_wM,
						mediaInfo.mContentType_wM))
				&& (DoubleAndLongConverter.compare_a(this.mMediaMetadata_wN,
						mediaInfo.mMediaMetadata_wN)) && (this.mDuration_wO == mediaInfo.mDuration_wO));
	}

	public int hashCode() {
		return MyStringBuilder.hashCode(new Object[] { this.mContentId_wK,
				Integer.valueOf(this.mStreamType_wL), this.mContentType_wM,
				this.mMediaMetadata_wN, Long.valueOf(this.mDuration_wO),
				String.valueOf(this.mCustomData_wP) });
	}

	/*
	 * MediaInfo$Builder.smali : OK
	 */
	public static class Builder {
		private final MediaInfo mediaInfo_wQ;

		public Builder(String contentId) throws IllegalArgumentException {
			if (TextUtils.isEmpty(contentId))
				throw new IllegalArgumentException("Content ID cannot be empty");
			this.mediaInfo_wQ = new MediaInfo(contentId);
		}

		public Builder setStreamType(int streamType)
				throws IllegalArgumentException {
			this.mediaInfo_wQ.setStreamType(streamType);
			return this;
		}

		public Builder setContentType(String contentType)
				throws IllegalArgumentException {
			this.mediaInfo_wQ.setContentType(contentType);
			return this;
		}

		public Builder setMetadata(MediaMetadata metadata) {
			this.mediaInfo_wQ.setMediaMetada_a(metadata);
			return this;
		}

		public Builder setStreamDuration(long duration)
				throws IllegalArgumentException {
			this.mediaInfo_wQ.setStreamDuration_j(duration);
			return this;
		}

		public Builder setCustomData(JSONObject customData) {
			this.mediaInfo_wQ.setCustomData_a(customData);
			return this;
		}

		public MediaInfo build() throws IllegalArgumentException {
			this.mediaInfo_wQ.checkSelf_cS();
			return this.mediaInfo_wQ;
		}
	}
}
