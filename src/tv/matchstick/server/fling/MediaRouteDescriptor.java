
package tv.matchstick.server.fling;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author jianminz
 */
public final class MediaRouteDescriptor {
    final Bundle bundleData;
    private List mControlFilters;

    MediaRouteDescriptor(Bundle paramBundle, List paramList) {
        this.bundleData = paramBundle;
        this.mControlFilters = paramList;
    }

    private String getId() {
        return this.bundleData.getString("id");
    }

    private String getName() {
        return this.bundleData.getString("name");
    }

    private void getControlFilters() {
        if (this.mControlFilters == null) {
            this.mControlFilters = this.bundleData.getParcelableArrayList("controlFilters");
            if (this.mControlFilters == null) {
                this.mControlFilters = Collections.emptyList();
            }
        }
    }

    public final boolean isNoteEmpty() {
        getControlFilters();
        return (!TextUtils.isEmpty(getId())) && (!TextUtils.isEmpty(getName()))
                && (!this.mControlFilters.contains(null));
    }

    public final String toString() {
        StringBuilder localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("MediaRouteDescriptor{ ");
        localStringBuilder1.append("id=").append(getId());
        localStringBuilder1.append(", name=").append(getName());
        localStringBuilder1.append(", description=").append(this.bundleData.getString("status"));
        localStringBuilder1.append(", isEnabled=").append(
                this.bundleData.getBoolean("enabled", true));
        localStringBuilder1.append(", isConnecting=")
                .append(this.bundleData.getBoolean("connecting", false));
        StringBuilder localStringBuilder2 = localStringBuilder1.append(", controlFilters=");
        getControlFilters();
        localStringBuilder2.append(Arrays.toString(this.mControlFilters.toArray()));
        localStringBuilder1.append(", playbackType=").append(
                this.bundleData.getInt("playbackType", 1));
        localStringBuilder1.append(", playbackStream=").append(
                this.bundleData.getInt("playbackStream", -1));
        localStringBuilder1.append(", volume=").append(this.bundleData.getInt("volume"));
        localStringBuilder1.append(", volumeMax=").append(this.bundleData.getInt("volumeMax"));
        localStringBuilder1.append(", volumeHandling=").append(
                this.bundleData.getInt("volumeHandling", 0));
        localStringBuilder1.append(", presentationDisplayId=").append(
                this.bundleData.getInt("presentationDisplayId", -1));
        localStringBuilder1.append(", extras=").append(this.bundleData.getBundle("extras"));
        localStringBuilder1.append(", isValid=").append(isNoteEmpty());
        localStringBuilder1.append(" }");
        return localStringBuilder1.toString();
    }
}
