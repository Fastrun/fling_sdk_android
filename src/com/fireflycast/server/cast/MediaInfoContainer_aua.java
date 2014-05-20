
package com.fireflycast.server.cast;

import android.text.TextUtils;

public final class MediaInfoContainer_aua {
    public final MediaInfo_atz mMediaInfo_a;

    public MediaInfoContainer_aua(String contentId)
    {
        if (TextUtils.isEmpty(contentId))
        {
            throw new IllegalArgumentException("Content ID cannot be empty");
        } else
        {
            mMediaInfo_a = new MediaInfo_atz(contentId);
            return;
        }
    }
}
