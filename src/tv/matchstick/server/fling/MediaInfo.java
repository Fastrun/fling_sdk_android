
package tv.matchstick.server.fling;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.server.common.checker.ObjEqualChecker;
import tv.matchstick.server.utils.C_bmg;

import java.util.Arrays;

public final class MediaInfo {
    public String mContentId = null;
    public int mStreamType;
    public String mContentType;
    public MediaMetadata mMediaMetadata;
    public long mDuration;
    private JSONObject mCustomData;

    public static final int STREAM_TYPE_NONE = 0;
    public static final int STREAM_TYPE_BUFFERED = 1;
    public static final int STREAM_TYPE_LIVE = 2;
    public static final int STREAM_TYPE_INVALID = -1;

    MediaInfo(String contentId_s) {
        if (TextUtils.isEmpty(contentId_s)) {
            throw new IllegalArgumentException(
                    "content ID cannot be null or empty");
        } else {
            mContentId = contentId_s;
            mStreamType = -1;
            return;
        }
    }

    MediaInfo(JSONObject jsonobject) throws JSONException {

        mContentId = jsonobject.getString("contentId");
        String streamType = jsonobject.getString("streamType");
        if ("NONE".equals(streamType))
            mStreamType = 0;
        else if ("BUFFERED".equals(streamType))
            mStreamType = 1;
        else if ("LIVE".equals(streamType))
            mStreamType = 2;
        else
            mStreamType = -1;
        mContentType = jsonobject.getString("contentType");
        if (jsonobject.has("metadata")) {
            JSONObject jsonobject1 = jsonobject.getJSONObject("metadata");
            mMediaMetadata = new MediaMetadata(
                    jsonobject1.getInt("metadataType"));
            mMediaMetadata.a(jsonobject1);
        }
        mDuration = (long) (1000D * jsonobject.optDouble("duration", 0.0D));
        mCustomData = jsonobject.optJSONObject("customData");

    }

    public String getContentId() {
        return mContentId;
    }

    void setStreamType(int streamType) throws IllegalArgumentException {
        if (streamType < -1 || streamType > 2) {
            throw new IllegalArgumentException("invalid stream type");
        } else {
            mStreamType = streamType;
            return;
        }
    }

    public int getStreamType() {
        return mStreamType;
    }

    void setContentType(String contentType) throws IllegalArgumentException {
        if (TextUtils.isEmpty(contentType)) {
            throw new IllegalArgumentException(
                    "content type cannot be null or empty");
        } else {
            mContentType = contentType;
            return;
        }
    }

    public String getContentType() {
        return mContentType;
    }

    void setMetadata(MediaMetadata mediametadata) {
        mMediaMetadata = mediametadata;
    }

    public MediaMetadata getMetadata() {
        return mMediaMetadata;
    }

    void setStreamDuration(long duration) throws IllegalArgumentException {
        if (duration < 0L) {
            throw new IllegalArgumentException(
                    "Stream duration cannot be negative");
        } else {
            mDuration = duration;
            return;
        }
    }

    public long getStreamDuration() {
        return mDuration;
    }

    void setCustomData(JSONObject jsonobject) {
        mCustomData = jsonobject;
    }

    public JSONObject getCustomData() {
        return mCustomData;
    }

    void checkValid() throws IllegalArgumentException {
        if (TextUtils.isEmpty(mContentId))
            throw new IllegalArgumentException(
                    "content ID cannot be null or empty");
        if (TextUtils.isEmpty(mContentType))
            throw new IllegalArgumentException(
                    "content type cannot be null or empty");
        if (mStreamType == -1)
            throw new IllegalArgumentException(
                    "a valid stream type must be specified");
        else
            return;
    }

    public final JSONObject buildJsonObj() {
        JSONObject jsonobject = new JSONObject();

        try {
            jsonobject.put("contentId", mContentId);
            String streamType;
            switch (mStreamType) {
                case 1:
                    streamType = "BUFFERED";
                    break;
                case 2:
                    streamType = "LIVE";
                    break;
                default:
                    streamType = "NONE";
                    break;
            }

            jsonobject.put("streamType", streamType);
            if (mContentType != null)
                jsonobject.put("contentType", mContentType);
            if (mMediaMetadata != null)
                jsonobject.put("metadata", mMediaMetadata.getMetaData());
            jsonobject.put("duration", (double) mDuration / 1000D);
            if (mCustomData != null) {
                jsonobject.put("customData", mCustomData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonobject;
    }

    public final boolean equals(Object other) {
        if (this == other)
            return true;

        if (!(other instanceof MediaInfo))
            return false;

        MediaInfo mediainfo = (MediaInfo) other;
        if ((mCustomData == null) != (mediainfo.mCustomData == null))
            return false;

        if (mCustomData != null && mediainfo.mCustomData != null
                && !C_bmg.a(mCustomData, mediainfo.mCustomData))
            return false;

        return ObjEqualChecker.isEquals(mContentId, mediainfo.mContentId)
                && mStreamType == mediainfo.mStreamType
                && ObjEqualChecker.isEquals(mContentType, mediainfo.mContentType)
                && ObjEqualChecker.isEquals(mMediaMetadata, mediainfo.mMediaMetadata)
                && mDuration == mediainfo.mDuration;
    }

    public final int hashCode() {
        Object aobj[] = new Object[6];
        aobj[0] = mContentId;
        aobj[1] = Integer.valueOf(mStreamType);
        aobj[2] = mContentType;
        aobj[3] = mMediaMetadata;
        aobj[4] = Long.valueOf(mDuration);
        aobj[5] = String.valueOf(mCustomData);
        return Arrays.hashCode(aobj);
    }

    public static class Builder {

        private final MediaInfo mMediaInfo;

        public Builder setStreamType(int streamType)
                throws IllegalArgumentException {
            mMediaInfo.setStreamType(streamType);
            return this;
        }

        public Builder setContentType(String contentType)
                throws IllegalArgumentException {
            mMediaInfo.setContentType(contentType);
            return this;
        }

        public Builder setMetadata(MediaMetadata metadata) {
            mMediaInfo.setMetadata(metadata);
            return this;
        }

        public Builder setStreamDuration(long duration)
                throws IllegalArgumentException {
            mMediaInfo.setStreamDuration(duration);
            return this;
        }

        public Builder setCustomData(JSONObject customData) {
            mMediaInfo.setCustomData(customData);
            return this;
        }

        public MediaInfo build() throws IllegalArgumentException {
            mMediaInfo.checkValid();
            return mMediaInfo;
        }

        public Builder(String contentId) throws IllegalArgumentException {
            if (TextUtils.isEmpty(contentId)) {
                throw new IllegalArgumentException("Content ID cannot be empty");
            } else {
                mMediaInfo = new MediaInfo(contentId);
                return;
            }
        }
    }

}
