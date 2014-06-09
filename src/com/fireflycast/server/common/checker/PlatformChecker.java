
package com.fireflycast.server.common.checker;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

public final class PlatformChecker {
    public int mPlatform_a;
    public String mPackage_b;
    public Uri mUri_c;

    public PlatformChecker(JSONObject jsonobject)
    {
        try {
            int i = jsonobject.getInt("platform");
            if (i < 0 || i > 3)
            {
                throw new JSONException("Invalid value for 'platform'");
            } else
            {
                mPlatform_a = i;
                mPackage_b = jsonobject.getString("package");
                mUri_c = Uri.parse(jsonobject.getString("url"));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
