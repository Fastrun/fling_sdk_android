
package com.fireflycast.server.cast;

import android.text.TextUtils;

public final class MediaInfoContainer {
    public final MediaInfo mMediaInfo_a;

    public MediaInfoContainer(String contentId)
    {
        if (TextUtils.isEmpty(contentId))
        {
            throw new IllegalArgumentException("Content ID cannot be empty");
        } else
        {
            mMediaInfo_a = new MediaInfo(contentId);
            return;
        }
    }
}
