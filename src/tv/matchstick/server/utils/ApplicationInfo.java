
package tv.matchstick.server.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.server.common.checker.PlatformChecker;
import tv.matchstick.server.common.images.WebImage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ApplicationInfo {
    private static final LOG mLogs = new LOG("ApplicationInfo");
    private String appId;
    private String displayName;
    private String transportId;
    private String sessionId;
    private String statusText;
    private List namespaces;
    private final List senderApps;
    private final List appImages;

    private ApplicationInfo()
    {
        senderApps = new ArrayList();
        appImages = new ArrayList();
    }

    public ApplicationInfo(JSONObject jsonobject)
    {
        this();
        try {
            appId = jsonobject.getString("appId");
            sessionId = jsonobject.getString("sessionId");
            transportId = jsonobject.optString("transportId");
            displayName = jsonobject.optString("displayName");
            statusText = jsonobject.optString("statusText");
            if (jsonobject.has("appImages"))
            {
                JSONArray jsonarray2 = jsonobject.getJSONArray("appImages");
                int j1 = jsonarray2.length();
                int k1 = 0;
                while (k1 < j1)
                {
                    JSONObject jsonobject1 = jsonarray2.getJSONObject(k1);
                    try
                    {
                        appImages.add(new WebImage(jsonobject1));
                    } catch (IllegalArgumentException illegalargumentexception)
                    {
                        mLogs.w(illegalargumentexception, "Ignoring invalid image structure",
                                new Object[0]);
                    }
                    k1++;
                }
            }
            if (jsonobject.has("senderApps"))
            {
                JSONArray jsonarray1 = jsonobject.getJSONArray("senderApps");
                int l = jsonarray1.length();
                int i1 = 0;
                do
                {
                    if (i1 >= l)
                        break;
                    try
                    {
                        PlatformChecker aui1 = new PlatformChecker(
                                jsonarray1.getJSONObject(i1));
                        senderApps.add(aui1);
                    } catch (JSONException jsonexception)
                    {
                        LOG avu1 = mLogs;
                        Object aobj[] = new Object[1];
                        aobj[0] = jsonexception.getMessage();
                        avu1.w("Ignorning invalid sender app structure: %s", aobj);
                    }
                    i1++;
                } while (true);
            }
            if (jsonobject.has("namespaces"))
            {
                JSONArray jsonarray = jsonobject.getJSONArray("namespaces");
                int j = jsonarray.length();
                if (j > 0)
                {
                    namespaces = new ArrayList();
                    for (int k = 0; k < j; k++)
                        namespaces.add(jsonarray.getString(k));

                }
            }
        } catch (Exception e) {

        }
    }

    public final String getApplicationId()
    {
        return appId;
    }

    public final String getDisplayName()
    {
        return displayName;
    }

    public final String getTransportId()
    {
        return transportId;
    }

    public final PlatformChecker getPlatformChecker()
    {
        for (Iterator iterator = senderApps.iterator(); iterator.hasNext();)
        {
            PlatformChecker checker = (PlatformChecker) iterator.next();
            if (checker.mPlatform == 1)
                return checker;
        }

        return null;
    }

    public final String getStatusText()
    {
        return statusText;
    }

    public final List getNamespaces()
    {
        return Collections.unmodifiableList(namespaces);
    }

    public final List getAppImages()
    {
        return Collections.unmodifiableList(appImages);
    }

    public final String getSessionId()
    {
        return sessionId;
    }
}
