
package com.fireflycast.server.cast.media;

import android.os.Bundle;
import android.os.SystemClock;

import com.fireflycast.server.utils.C_dt;

public final class MediaItemStatus {
    public final Bundle mBundle_a;

    private MediaItemStatus(Bundle bundle)
    {
        mBundle_a = bundle;
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
        C_dt.a(SystemClock.elapsedRealtime() - mBundle_a.getLong("timestamp"), stringbuilder);
        stringbuilder.append(" ms ago");
        stringbuilder1 = stringbuilder.append(", playbackState=");
        playbackState = mBundle_a.getInt("playbackState", 7);
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
                .append(mBundle_a.getLong("contentPosition", -1L));
        stringbuilder.append(", contentDuration=")
                .append(mBundle_a.getLong("contentDuration", -1L));
        stringbuilder.append(", extras=").append(mBundle_a.getBundle("extras"));
        stringbuilder.append(" }");

        return stringbuilder.toString();
    }
}
