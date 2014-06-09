// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package com.fireflycast.server.cast_mirroring;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;

import java.lang.ref.WeakReference;

public class JGCastService {

    public static final int FLAG_USE_TDLS = 1;
    private static final String TAG = "JGCastService";
    private Handler mEventHandler;
    private C_aza mListener;
    private int mNativeContext;

    public JGCastService(Context context, C_aza aza) {
        mListener = aza;
        Looper looper = Looper.myLooper();
        if (looper == null)
            looper = Looper.getMainLooper();
        if (looper != null)
            mEventHandler = new JGCastServiceHandler(this, looper);
        native_setup(new WeakReference(this), context);
    }

    public static C_aza a(JGCastService jgcastservice) {
        return jgcastservice.mListener;
    }

    private final void createSink(String s, String s1, String s2, Surface surface, int i) {
        createSourceOrSink(false, s, s1, s2, surface, i);
    }

    private final void createSource(String s, String s1, String s2, int i) {
        createSourceOrSink(true, s, s1, s2, null, i);
    }

    private final native void native_finalize();

    private static final native void native_init();

    private final native void native_setup(Object obj, Context context);

    private static void postEventFromNative(Object obj, int i, int j, int k) {
        JGCastService jgcastservice = (JGCastService) ((WeakReference) obj).get();
        if (jgcastservice == null) {
            return;
        } else {
            android.os.Message message = jgcastservice.mEventHandler.obtainMessage(i, j, k);
            jgcastservice.mEventHandler.sendMessage(message);
            return;
        }
    }

    public final native void createSourceOrSink(boolean flag, String s, String s1, String s2,
            Surface surface, int i);

    public final native void disconnect();

    protected void finalize() {
        native_finalize();
    }

    public final native void release();

    static {
        System.loadLibrary("jgcastservice");
        native_init();
    }
}
