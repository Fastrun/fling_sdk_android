
package tv.matchstick.server.fling;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class MediaRouteProviderDescriptorHelper {
    private final Bundle mBundle = new Bundle();
    private ArrayList mMediaRouteDescriptorList;

    public MediaRouteProviderDescriptorHelper()
    {
    }

    public final MediaRouteProviderDescriptor createMediaRouteProviderDescriptor()
    {
        if (mMediaRouteDescriptorList != null)
        {
            int i = mMediaRouteDescriptorList.size();
            ArrayList arraylist = new ArrayList(i);
            for (int j = 0; j < i; j++)
                arraylist
                        .add(((MediaRouteDescriptor) mMediaRouteDescriptorList.get(j)).bundleData);

            mBundle.putParcelableArrayList("routes", arraylist);
        }
        return new MediaRouteProviderDescriptor(mBundle, mMediaRouteDescriptorList, (byte) 0);
    }

    public final MediaRouteProviderDescriptorHelper addMediaRouteDescriptors(
            Collection collection)
    {
        Iterator iterator;
        if (collection == null)
            throw new IllegalArgumentException("routes must not be null");
        if (collection.isEmpty()) {
            return this;
        }

        iterator = collection.iterator();
        while (iterator.hasNext()) {
            MediaRouteDescriptor descriptor = (MediaRouteDescriptor) iterator.next();
            if (descriptor == null)
                throw new IllegalArgumentException("route must not be null");
            if (mMediaRouteDescriptorList == null) {
                mMediaRouteDescriptorList = new ArrayList();
            }

            if (mMediaRouteDescriptorList.contains(descriptor)) {
                throw new IllegalArgumentException("route descriptor already added");
            }

            mMediaRouteDescriptorList.add(descriptor);
        }

        return this;
    }
}
