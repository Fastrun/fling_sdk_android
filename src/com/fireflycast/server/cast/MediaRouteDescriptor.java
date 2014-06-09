
package com.fireflycast.server.cast;

import android.os.Bundle;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author jianminz
 */
public final class MediaRouteDescriptor {
    final Bundle bundleData_a;
    private List mControlFilters_b;

    MediaRouteDescriptor(Bundle paramBundle, List paramList) {
        this.bundleData_a = paramBundle;
        this.mControlFilters_b = paramList;
    }

    private String getId_b() {
        return this.bundleData_a.getString("id");
    }

    private String getName_c() {
        return this.bundleData_a.getString("name");
    }

    private void getControlFilters_d() {
        if (this.mControlFilters_b == null) {
            this.mControlFilters_b = this.bundleData_a.getParcelableArrayList("controlFilters");
            if (this.mControlFilters_b == null) {
                this.mControlFilters_b = Collections.emptyList();
            }
        }
    }

    public final boolean isNoteEmpty_a() {
        getControlFilters_d();
        return (!TextUtils.isEmpty(getId_b())) && (!TextUtils.isEmpty(getName_c()))
                && (!this.mControlFilters_b.contains(null));
    }

    public final String toString() {
        StringBuilder localStringBuilder1 = new StringBuilder();
        localStringBuilder1.append("MediaRouteDescriptor{ ");
        localStringBuilder1.append("id=").append(getId_b());
        localStringBuilder1.append(", name=").append(getName_c());
        localStringBuilder1.append(", description=").append(this.bundleData_a.getString("status"));
        localStringBuilder1.append(", isEnabled=").append(
                this.bundleData_a.getBoolean("enabled", true));
        localStringBuilder1.append(", isConnecting=")
                .append(this.bundleData_a.getBoolean("connecting", false));
        StringBuilder localStringBuilder2 = localStringBuilder1.append(", controlFilters=");
        getControlFilters_d();
        localStringBuilder2.append(Arrays.toString(this.mControlFilters_b.toArray()));
        localStringBuilder1.append(", playbackType=").append(
                this.bundleData_a.getInt("playbackType", 1));
        localStringBuilder1.append(", playbackStream=").append(
                this.bundleData_a.getInt("playbackStream", -1));
        localStringBuilder1.append(", volume=").append(this.bundleData_a.getInt("volume"));
        localStringBuilder1.append(", volumeMax=").append(this.bundleData_a.getInt("volumeMax"));
        localStringBuilder1.append(", volumeHandling=").append(
                this.bundleData_a.getInt("volumeHandling", 0));
        localStringBuilder1.append(", presentationDisplayId=").append(
                this.bundleData_a.getInt("presentationDisplayId", -1));
        localStringBuilder1.append(", extras=").append(this.bundleData_a.getBundle("extras"));
        localStringBuilder1.append(", isValid=").append(isNoteEmpty_a());
        localStringBuilder1.append(" }");
        return localStringBuilder1.toString();
    }
}
