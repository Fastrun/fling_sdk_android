
package tv.matchstick.server.utils;

import android.util.Log;

public final class LOG {
    private static boolean mDefaultDebugEnabled = true;
    private String mTag;
    private boolean mDebugEnabled = true;
    private boolean d = true;

    public LOG(String tag) {
        this(tag, mDefaultDebugEnabled);
    }

    private LOG(String tag, boolean debugEnabled) {
        mTag = tag;
        mDebugEnabled = debugEnabled;
    }

    public static void setDebugEnabledByDefault(boolean flag) {
        mDefaultDebugEnabled = flag;
    }

    public final void setSubTag(String tag) {
        mTag = mTag + '_' + tag;
    }

    public final void v(String message, Object args[]) {
        // if (d)
        Log.v(mTag, String.format(message, args));
    }

    public final void d(Throwable t, String s, Object aobj[]) {
        // if (c || a)
        Log.d(mTag, String.format(s, aobj), t);
    }

    public final void setDebugEnabled(boolean flag) {
        mDebugEnabled = flag;
    }

    public final boolean isDebugEnabled() {
        // return c;
        return true;
    }

    public final void d(String message, Object... args) {
        // if (c || a)
        Log.d(mTag, String.format(message, args));
    }

    public final void w(Throwable t, String message, Object... args) {
        Log.w(mTag, String.format(message, args), t);
    }

    public final void i(String message, Object args[]) {
        Log.i(mTag, String.format(message, args));
    }

    public final void e(Throwable t, String message, Object args[]) {
        Log.e(mTag, String.format(message, args), t);
    }

    public final void w(String message, Object args[]) {
        Log.w(mTag, String.format(message, args));
    }

    public final void e(String message, Object args[]) {
        Log.e(mTag, String.format(message, args));
    }
}
