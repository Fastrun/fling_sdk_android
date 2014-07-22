
package tv.matchstick.server.fling;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

final class AppInfoHelper {
    final Set mAppNamespaceList;
    final Set mAppAvailabityList;

    private AppInfoHelper()
    {
        mAppNamespaceList = new HashSet();
        mAppAvailabityList = new HashSet();
    }

    AppInfoHelper(byte byte0)
    {
        this();
    }

    public final void fillNamespaceList(JSONObject jsonobject)
    {
        try {
            JSONArray jsonarray = jsonobject.getJSONObject("status").getJSONArray("applications");
            int i = 0;
            while (i < jsonarray.length()) {
                JSONArray jsonarray1 = jsonarray.getJSONObject(i).getJSONArray("namespaces");
                int j = 0;
                while (j < jsonarray1.length()) {
                    String name = jsonarray1.getJSONObject(j).getString("name");
                    mAppNamespaceList.add(name);
                    j++;
                }
                i++;
            }
        } catch (JSONException e) {
            e.printStackTrace();

            DeviceFilter.getLogs().d("No namespaces found in receiver response: %s", e.getMessage());
        }
    }

    public final void fillAppAvailabityList(JSONObject jsonobject)
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
                    mAppAvailabityList.add(appId);
            } while (true);
        } catch (JSONException jsonexception)
        {
            DeviceFilter.getLogs().d("No app availabities found in receiver response: %s", jsonexception.getMessage());
        }
    }
}
