
package com.fireflycast.server.utils;

import com.fireflycast.server.common.checker.PlatformChecker_aui;
import com.fireflycast.server.common.images.WebImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class ApplicationInfo_atn {
    private static final Logs_avu a = new Logs_avu("ApplicationInfo");
    private String appId_b;
    private String displayName_c;
    private String transportId_d;
    private String sessionId_e;
    private String statusText_f;
    private List namespaces_g;
    private final List senderApps_h;
    private final List appImages_i;

    private ApplicationInfo_atn()
    {
        senderApps_h = new ArrayList();
        appImages_i = new ArrayList();
    }

    public ApplicationInfo_atn(JSONObject jsonobject)
    {
        this();
        try {
            appId_b = jsonobject.getString("appId");
            sessionId_e = jsonobject.getString("sessionId");
            transportId_d = jsonobject.optString("transportId");
            displayName_c = jsonobject.optString("displayName");
            statusText_f = jsonobject.optString("statusText");
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
                        appImages_i.add(new WebImage(jsonobject1));
                    } catch (IllegalArgumentException illegalargumentexception)
                    {
                        a.w(illegalargumentexception, "Ignoring invalid image structure",
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
                        PlatformChecker_aui aui1 = new PlatformChecker_aui(
                                jsonarray1.getJSONObject(i1));
                        senderApps_h.add(aui1);
                    } catch (JSONException jsonexception)
                    {
                        Logs_avu avu1 = a;
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
                    namespaces_g = new ArrayList();
                    for (int k = 0; k < j; k++)
                        namespaces_g.add(jsonarray.getString(k));

                }
            }
        } catch (Exception e) {

        }
    }

    public final String getApplicationId_a()
    {
        return appId_b;
    }

    public final String getDisplayName_b()
    {
        return displayName_c;
    }

    public final String getTransportId_c()
    {
        return transportId_d;
    }

    public final PlatformChecker_aui getPlatformChecker_d()
    {
        for (Iterator iterator = senderApps_h.iterator(); iterator.hasNext();)
        {
            PlatformChecker_aui checker = (PlatformChecker_aui) iterator.next();
            if (checker.mPlatform_a == 1)
                return checker;
        }

        return null;
    }

    public final String getStatusText_e()
    {
        return statusText_f;
    }

    public final List getNamespaces_f()
    {
        return Collections.unmodifiableList(namespaces_g);
    }

    public final List getAppImages_g()
    {
        return Collections.unmodifiableList(appImages_i);
    }

    public final String getSessionId_h()
    {
        return sessionId_e;
    }
}
