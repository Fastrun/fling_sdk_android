
package com.fireflycast.server.cast.media;

import android.os.Bundle;
import android.os.SystemClock;

public final class MediaItemStatusHelper_nr {
    public final Bundle mData_a = new Bundle();

    public MediaItemStatusHelper_nr(int playbackState)
    {
        a(SystemClock.elapsedRealtime());
        mData_a.putInt("playbackState", playbackState);
    }

    public final MediaItemStatus_nq createMediaItemStatus_a()
    {
        return new MediaItemStatus_nq(mData_a, (byte) 0);
    }

    public final MediaItemStatusHelper_nr a(long timestamp)
    {
        mData_a.putLong("timestamp", timestamp);
        return this;
    }

    public final MediaItemStatusHelper_nr a(Bundle extras)
    {
        mData_a.putBundle("extras", extras);
        return this;
    }
}
