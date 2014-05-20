
package com.fireflycast.server.cast;

import android.app.PendingIntent;

import java.util.UUID;

final class TrackedItem_aws {
    public final String mItemId_a;
    public long mLoadRequestId_b;
    public long mMediaSessionId_c;
    public PendingIntent mPendingIntent_d;
    final CastRouteController_awn mCastRouteController_e;

    public TrackedItem_aws(CastRouteController_awn controller)
    {
        this(controller, -1L);
    }

    public TrackedItem_aws(CastRouteController_awn controller, long requestId)
    {
        super();
        mCastRouteController_e = controller;
        mItemId_a = (new StringBuilder("media-")).append(UUID.randomUUID().toString()).toString();
        mLoadRequestId_b = requestId;
        mMediaSessionId_c = -1L;
    }

    public final String toString()
    {
        Object aobj[] = new Object[3];
        aobj[0] = mItemId_a;
        aobj[1] = Long.valueOf(mLoadRequestId_b);
        aobj[2] = Long.valueOf(mMediaSessionId_c);
        return String.format("TrackedItem(itemId=%s, loadRequestId=%d, mediaSessionId=%d)", aobj);
    }
}
