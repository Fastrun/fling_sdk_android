
package com.fireflycast.server.cast;

import com.fireflycast.server.utils.Logs_avu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

final class AppInfoHelper_axa {
    final Set mAppNamespaceList_a;
    final Set mAppAvailabityList_b;

    private AppInfoHelper_axa()
    {
        mAppNamespaceList_a = new HashSet();
        mAppAvailabityList_b = new HashSet();
    }

    AppInfoHelper_axa(byte byte0)
    {
        this();
    }

    public final void fillNamespaceList_a(JSONObject jsonobject)
    {
        try {
            JSONArray jsonarray = jsonobject.getJSONObject("status").getJSONArray("applications");
            int i = 0;
            while (i < jsonarray.length()) {
                JSONArray jsonarray1 = jsonarray.getJSONObject(i).getJSONArray("namespaces");
                int j = 0;
                while (j < jsonarray1.length()) {
                    String name = jsonarray1.getJSONObject(j).getString("name");
                    mAppNamespaceList_a.add(name);
                    j++;
                }
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();

            Logs_avu avu1 = DeviceFilter_awz.getLogs_b();
            Object aobj[] = new Object[1];
            aobj[0] = e.getMessage();
            avu1.d("No namespaces found in receiver response: %s", aobj);
        }
    }

    public final void fillAppAvailabityList_b(JSONObject jsonobject)
    {
        try
        {
            JSONObject jsonobject1 = jsonobject.getJSONObject("availability");
            Iterator iterator = jsonobject1.keys();
            do
            {
                if (!iterator.hasNext())
                    break;
                String appId = (String) iterator.next();
                if ("APP_AVAILABLE".equals(jsonobject1.optString(appId)))
                    mAppAvailabityList_b.add(appId);
            } while (true);
        } catch (JSONException jsonexception)
        {
            Logs_avu avu1 = DeviceFilter_awz.getLogs_b();
            Object aobj[] = new Object[1];
            aobj[0] = jsonexception.getMessage();
            avu1.d("No app availabities found in receiver response: %s", aobj);
        }
    }
}
