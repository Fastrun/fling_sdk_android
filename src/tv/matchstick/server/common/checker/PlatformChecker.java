
package tv.matchstick.server.common.checker;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

public final class PlatformChecker {
    public int mPlatform;
    public String mPackage;
    public Uri mUri;

    public PlatformChecker(JSONObject jsonobject)
    {
        try {
            int i = jsonobject.getInt("platform");
            if (i < 0 || i > 3)
            {
                throw new JSONException("Invalid value for 'platform'");
            } else
            {
                mPlatform = i;
                mPackage = jsonobject.getString("package");
                mUri = Uri.parse(jsonobject.getString("url"));
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
