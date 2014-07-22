
package tv.matchstick.server.common.exception;

import android.content.Intent;

public class C_azk extends Exception {
    private final Intent mIntent;

    public C_azk(String s, Intent intent)
    {
        super(s);
        mIntent = intent;
    }

    public final Intent b()
    {
        return new Intent(mIntent);
    }
}
