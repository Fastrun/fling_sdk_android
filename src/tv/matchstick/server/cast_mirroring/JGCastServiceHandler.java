// Decompiled by Jad v1.5.8g. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 

package tv.matchstick.server.cast_mirroring;

import android.os.*;

public final class JGCastServiceHandler extends Handler {

    final JGCastService a;

    public JGCastServiceHandler(JGCastService jgcastservice, Looper looper) {
        super(looper);
        a = jgcastservice;
    }

    public final void handleMessage(Message message) {
        JGCastService.a(a).a(message.what, message.arg1, message.arg2);
    }
}
