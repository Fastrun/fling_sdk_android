
package com.fireflycast.client.internal;

import android.util.Log;

public class LogUtil_du {

    private static boolean ye = false;
    private final String mTag;
    private boolean yf;
    private boolean yg;
    private String yh;

    public LogUtil_du(String tag, boolean paramBoolean) {
        this.mTag = tag;
        this.yf = paramBoolean;
        this.yg = false;
    }

    public LogUtil_du(String tag) {
        this(tag, di());
    }

    public void U(String paramString) {
        this.yh = String.format("[%s] ", new Object[] {
            paramString
        });
    }

    public boolean dg() {
        return this.yf;
    }

    public boolean dh() {
        return this.yg;
    }

    public void logv_a(String paramString, Object[] paramArrayOfObject) {
        if (!(dh()))
            return;
        Log.v(this.mTag, e(paramString, paramArrayOfObject));
    }

    public void logd_b(String paramString, Object[] paramArrayOfObject) {
        if ((!(dg())) && (!(ye)))
            return;
        Log.d(this.mTag, e(paramString, paramArrayOfObject));
    }

    public void logd_a(Throwable paramThrowable, String paramString,
            Object[] paramArrayOfObject) {
        if ((!(dg())) && (!(ye)))
            return;
        Log.d(this.mTag, e(paramString, paramArrayOfObject), paramThrowable);
    }

    public void logi_c(String paramString, Object[] paramArrayOfObject) {
        Log.i(this.mTag, e(paramString, paramArrayOfObject));
    }

    public void logw_d(String paramString, Object[] paramArrayOfObject) {
        Log.w(this.mTag, e(paramString, paramArrayOfObject));
    }

    private String e(String paramString, Object[] paramArrayOfObject) {
        String str = String.format(paramString, paramArrayOfObject);
        if (this.yh != null)
            str = this.yh + str;
        return str;
    }

    public static boolean di() {
        return ye;
    }

}
