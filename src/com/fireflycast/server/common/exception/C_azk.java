
package com.fireflycast.server.common.exception;

import android.content.Intent;

public class C_azk extends Exception {
    private final Intent mIntent_a;

    public C_azk(String s, Intent intent)
    {
        super(s);
        mIntent_a = intent;
    }

    public final Intent b()
    {
        return new Intent(mIntent_a);
    }
}
