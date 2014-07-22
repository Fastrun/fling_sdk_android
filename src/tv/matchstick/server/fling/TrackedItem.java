
package tv.matchstick.server.fling;

import android.app.PendingIntent;

import java.util.UUID;

final class TrackedItem {
    public final String mItemId;
    public long mLoadRequestId;
    public long mMediaSessionId;
    public PendingIntent mPendingIntent;
    final FlingRouteController mFlingRouteController;

    public TrackedItem(FlingRouteController controller)
    {
        this(controller, -1L);
    }

    public TrackedItem(FlingRouteController controller, long requestId)
    {
        super();
        mFlingRouteController = controller;
        mItemId = (new StringBuilder("media-")).append(UUID.randomUUID().toString()).toString();
        mLoadRequestId = requestId;
        mMediaSessionId = -1L;
    }

    public final String toString()
    {
        Object aobj[] = new Object[3];
        aobj[0] = mItemId;
        aobj[1] = Long.valueOf(mLoadRequestId);
        aobj[2] = Long.valueOf(mMediaSessionId);
        return String.format("TrackedItem(itemId=%s, loadRequestId=%d, mediaSessionId=%d)", aobj);
    }
}
