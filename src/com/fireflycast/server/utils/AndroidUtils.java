
package com.fireflycast.server.utils;

import android.content.Context;
import android.content.Intent;

public final class AndroidUtils {

    public static Intent buildIntent_f(final Context context, String action)
    {
        // return (new Intent(s)).setPackage("com.fireflycast");
        return (new Intent(action)).setPackage(context.getPackageName());
    }

}
