
package com.fireflycast.server.cast;

import android.text.TextUtils;

import com.fireflycast.server.common.checker.ObjEqualChecker;
import com.fireflycast.server.utils.C_bmg;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public final class MediaInfo {
    public String mContentId_a = null;
    public int mStreamType_b;
    public String mContentType_c;
    public MediaMetadata mMediaMetadata_d;
    public long mDuration_e;
    private JSONObject mCustomData_f;

    public static final int STREAM_TYPE_NONE = 0;
    public static final int STREAM_TYPE_BUFFERED = 1;
    public static final int STREAM_TYPE_LIVE = 2;
    public static final int STREAM_TYPE_INVALID = -1;

    MediaInfo(String contentId_s) {
        if (TextUtils.isEmpty(contentId_s)) {
            throw new IllegalArgumentException(
                    "content ID cannot be null or empty");
        } else {
            mContentId_a = contentId_s;
            mStreamType_b = -1;
            return;
        }
    }

    MediaInfo(JSONObject jsonobject) throws JSONException {

        mContentId_a = jsonobject.getString("contentId");
        String streamType = jsonobject.getString("streamType");
        if ("NONE".equals(streamType))
            mStreamType_b = 0;
        else if ("BUFFERED".equals(streamType))
            mStreamType_b = 1;
        else if ("LIVE".equals(streamType))
            mStreamType_b = 2;
        else
            mStreamType_b = -1;
        mContentType_c = jsonobject.getString("contentType");
        if (jsonobject.has("metadata")) {
            JSONObject jsonobject1 = jsonobject.getJSONObject("metadata");
            mMediaMetadata_d = new MediaMetadata(
                    jsonobject1.getInt("metadataType"));
            mMediaMetadata_d.a(jsonobject1);
        }
        mDuration_e = (long) (1000D * jsonobject.optDouble("duration", 0.0D));
        mCustomData_f = jsonobject.optJSONObject("customData");

    }

    public String getContentId() {
        return mContentId_a;
    }

    void setStreamType(int streamType) throws IllegalArgumentException {
        if (streamType < -1 || streamType > 2) {
            throw new IllegalArgumentException("invalid stream type");
        } else {
            mStreamType_b = streamType;
            return;
        }
    }

    public int getStreamType() {
        return mStreamType_b;
    }

    void setContentType(String contentType) throws IllegalArgumentException {
        if (TextUtils.isEmpty(contentType)) {
            throw new IllegalArgumentException(
                    "content type cannot be null or empty");
        } else {
            mContentType_c = contentType;
            return;
        }
    }

    public String getContentType() {
        return mContentType_c;
    }

    void setMetadata_a(MediaMetadata mediametadata) {
        mMediaMetadata_d = mediametadata;
    }

    public MediaMetadata getMetadata() {
        return mMediaMetadata_d;
    }

    void setStreamDuration(long l) throws IllegalArgumentException {
        if (l < 0L) {
            throw new IllegalArgumentException(
                    "Stream duration cannot be negative");
        } else {
            mDuration_e = l;
            return;
        }
    }

    public long getStreamDuration() {
        return mDuration_e;
    }

    void setCustomData(JSONObject jsonobject) {
        mCustomData_f = jsonobject;
    }

    public JSONObject getCustomData() {
        return mCustomData_f;
    }

    void checkValid_aO() throws IllegalArgumentException {
        if (TextUtils.isEmpty(mContentId_a))
            throw new IllegalArgumentException(
                    "content ID cannot be null or empty");
        if (TextUtils.isEmpty(mContentType_c))
            throw new IllegalArgumentException(
                    "content type cannot be null or empty");
        if (mStreamType_b == -1)
            throw new IllegalArgumentException(
                    "a valid stream type must be specified");
        else
            return;
    }

    public final JSONObject buildJsonObj_a() {
        JSONObject jsonobject = new JSONObject();

        try {
            jsonobject.put("contentId", mContentId_a);
            String s;
            switch (mStreamType_b) {
                case 1:
                    s = "BUFFERED";
                    break;
                case 2:
                    s = "LIVE";
                    break;
                default:
                    s = "NONE";
                    break;
            }

            jsonobject.put("streamType", s);
            if (mContentType_c != null)
                jsonobject.put("contentType", mContentType_c);
            if (mMediaMetadata_d != null)
                jsonobject.put("metadata", mMediaMetadata_d.a());
            jsonobject.put("duration", (double) mDuration_e / 1000D);
            if (mCustomData_f != null) {
                jsonobject.put("customData", mCustomData_f);
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
        if ((mCustomData_f == null) != (mediainfo.mCustomData_f == null))
            return false;

        if (mCustomData_f != null && mediainfo.mCustomData_f != null
                && !C_bmg.a(mCustomData_f, mediainfo.mCustomData_f))
            return false;

        return ObjEqualChecker.isEquals_a(mContentId_a, mediainfo.mContentId_a)
                && mStreamType_b == mediainfo.mStreamType_b
                && ObjEqualChecker.isEquals_a(mContentType_c, mediainfo.mContentType_c)
                && ObjEqualChecker.isEquals_a(mMediaMetadata_d, mediainfo.mMediaMetadata_d)
                && mDuration_e == mediainfo.mDuration_e;
    }

    public final int hashCode() {
        Object aobj[] = new Object[6];
        aobj[0] = mContentId_a;
        aobj[1] = Integer.valueOf(mStreamType_b);
        aobj[2] = mContentType_c;
        aobj[3] = mMediaMetadata_d;
        aobj[4] = Long.valueOf(mDuration_e);
        aobj[5] = String.valueOf(mCustomData_f);
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
            mMediaInfo.setMetadata_a(metadata);
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
            mMediaInfo.checkValid_aO();
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
