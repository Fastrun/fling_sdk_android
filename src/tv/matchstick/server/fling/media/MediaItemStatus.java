
package tv.matchstick.server.fling.media;

import tv.matchstick.server.utils.C_dt;
import android.os.Bundle;
import android.os.SystemClock;

public final class MediaItemStatus {
    public final Bundle mBundle;

    private MediaItemStatus(Bundle bundle)
    {
        mBundle = bundle;
    }

    MediaItemStatus(Bundle bundle, byte byte0)
    {
        this(bundle);
    }

    public final String toString()
    {
        StringBuilder stringbuilder;
        StringBuilder stringbuilder1;
        int playbackState;
        stringbuilder = new StringBuilder();
        stringbuilder.append("MediaItemStatus{ ");
        stringbuilder.append("timestamp=");
        C_dt.a(SystemClock.elapsedRealtime() - mBundle.getLong("timestamp"), stringbuilder);
        stringbuilder.append(" ms ago");
        stringbuilder1 = stringbuilder.append(", playbackState=");
        playbackState = mBundle.getInt("playbackState", 7);
        String s;
        switch (playbackState) {
            case 0:
                s = "pending";
                break;
            case 1:
                s = "playing";
                break;
            case 2:
                s = "paused";
                break;
            case 3:
                s = "buffering";
                break;
            case 4:
                s = "finished";
                break;
            case 5:
                s = "canceled";
                break;
            case 6:
                s = "invalidated";
                break;
            case 7:
                s = "error";
                break;
            default:
                s = Integer.toString(playbackState);
                break;
        }
        stringbuilder1.append(s);
        stringbuilder.append(", contentPosition=")
                .append(mBundle.getLong("contentPosition", -1L));
        stringbuilder.append(", contentDuration=")
                .append(mBundle.getLong("contentDuration", -1L));
        stringbuilder.append(", extras=").append(mBundle.getBundle("extras"));
        stringbuilder.append(" }");

        return stringbuilder.toString();
    }
}
