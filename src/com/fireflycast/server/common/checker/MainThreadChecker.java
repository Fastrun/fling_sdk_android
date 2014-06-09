
package com.fireflycast.server.common.checker;

import android.os.Looper;
import android.util.Log;

public final class MainThreadChecker
{

    private static final boolean a = Log.isLoggable("MediaRouter", 3);

    public static void isOnAppMainThread_a()
    {
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new IllegalStateException(
                    "The media router service must only be accessed on the application's main thread.");
        else
            return;
    }

}
