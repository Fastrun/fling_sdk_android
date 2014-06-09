
package com.fireflycast.server.cast.media;

import android.os.Bundle;
import android.os.SystemClock;

public final class MediaItemStatusHelper {
    public final Bundle mData_a = new Bundle();

    public MediaItemStatusHelper(int playbackState)
    {
        a(SystemClock.elapsedRealtime());
        mData_a.putInt("playbackState", playbackState);
    }

    public final MediaItemStatus createMediaItemStatus_a()
    {
        return new MediaItemStatus(mData_a, (byte) 0);
    }

    public final MediaItemStatusHelper a(long timestamp)
    {
        mData_a.putLong("timestamp", timestamp);
        return this;
    }

    public final MediaItemStatusHelper a(Bundle extras)
    {
        mData_a.putBundle("extras", extras);
        return this;
    }
}
