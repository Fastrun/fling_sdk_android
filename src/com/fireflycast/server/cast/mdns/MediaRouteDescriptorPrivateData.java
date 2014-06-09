
package com.fireflycast.server.cast.mdns;

import android.content.IntentFilter;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class MediaRouteDescriptorPrivateData {
    public final Bundle mBundle_a = new Bundle();
    public ArrayList mControlIntentFilterList_b;

    public MediaRouteDescriptorPrivateData(String id, String name)
    {
        mBundle_a.putString("id", id);
        mBundle_a.putString("name", name);
    }

    public final MediaRouteDescriptorPrivateData addIntentFilterList_a(Collection collection)
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
                if (mControlIntentFilterList_b == null)
                    mControlIntentFilterList_b = new ArrayList();
                if (!mControlIntentFilterList_b.contains(intentfilter))
                    mControlIntentFilterList_b.add(intentfilter);
            } while (true);
        }
        return this;
    }
}
