
package tv.matchstick.server.fling;

import android.text.TextUtils;

public final class MediaInfoContainer {
    public final MediaInfo mMediaInfo;

    public MediaInfoContainer(String contentId)
    {
        if (TextUtils.isEmpty(contentId))
        {
            throw new IllegalArgumentException("Content ID cannot be empty");
        } else
        {
            mMediaInfo = new MediaInfo(contentId);
            return;
        }
    }
}
