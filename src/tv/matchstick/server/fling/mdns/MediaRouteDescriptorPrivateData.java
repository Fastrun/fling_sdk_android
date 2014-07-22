
package tv.matchstick.server.fling.mdns;

import android.content.IntentFilter;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class MediaRouteDescriptorPrivateData {
    public final Bundle mBundle = new Bundle();
    public ArrayList mControlIntentFilterList;

    public MediaRouteDescriptorPrivateData(String id, String name)
    {
        mBundle.putString("id", id);
        mBundle.putString("name", name);
    }

    public final MediaRouteDescriptorPrivateData addIntentFilterList(Collection collection)
    {
        if (collection == null)
            throw new IllegalArgumentException("filters must not be null");
        if (!collection.isEmpty())
        {
            Iterator iterator = collection.iterator();
            do
            {
                if (!iterator.hasNext())
                    break;
                IntentFilter intentfilter = (IntentFilter) iterator.next();
                
                if (intentfilter == null)
                    throw new IllegalArgumentException("filter must not be null");
                
                if (mControlIntentFilterList == null)
                    mControlIntentFilterList = new ArrayList();
                if (!mControlIntentFilterList.contains(intentfilter))
                    mControlIntentFilterList.add(intentfilter);
            } while (true);
        }
        return this;
    }
}
