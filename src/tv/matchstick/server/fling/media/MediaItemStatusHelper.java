
package tv.matchstick.server.fling.media;

import android.os.Bundle;
import android.os.SystemClock;

public final class MediaItemStatusHelper {
    public final Bundle mData = new Bundle();

    public MediaItemStatusHelper(int playbackState)
    {
        a(SystemClock.elapsedRealtime());
        mData.putInt("playbackState", playbackState);
    }

    public final MediaItemStatus createMediaItemStatus()
    {
        return new MediaItemStatus(mData, (byte) 0);
    }

    public final MediaItemStatusHelper a(long timestamp)
    {
        mData.putLong("timestamp", timestamp);
        return this;
    }

    public final MediaItemStatusHelper a(Bundle extras)
    {
        mData.putBundle("extras", extras);
        return this;
    }
}
