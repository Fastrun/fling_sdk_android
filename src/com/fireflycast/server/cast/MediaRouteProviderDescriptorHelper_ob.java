
package com.fireflycast.server.cast;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class MediaRouteProviderDescriptorHelper_ob {
    private final Bundle mBundle_a = new Bundle();
    private ArrayList mMediaRouteDescriptorList_b;

    public MediaRouteProviderDescriptorHelper_ob()
    {
    }

    public final MediaRouteProviderDescriptor_oa createMediaRouteProviderDescriptor_a()
    {
        if (mMediaRouteDescriptorList_b != null)
        {
            int i = mMediaRouteDescriptorList_b.size();
            ArrayList arraylist = new ArrayList(i);
            for (int j = 0; j < i; j++)
                arraylist
                        .add(((MediaRouteDescriptor_ns) mMediaRouteDescriptorList_b.get(j)).bundleData_a);

            mBundle_a.putParcelableArrayList("routes", arraylist);
        }
        return new MediaRouteProviderDescriptor_oa(mBundle_a, mMediaRouteDescriptorList_b, (byte) 0);
    }

    public final MediaRouteProviderDescriptorHelper_ob addMediaRouteDescriptors_a(
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
            MediaRouteDescriptor_ns descriptor = (MediaRouteDescriptor_ns) iterator.next();
            if (descriptor == null)
                throw new IllegalArgumentException("route must not be null");
            if (mMediaRouteDescriptorList_b == null) {
                mMediaRouteDescriptorList_b = new ArrayList();
            }

            if (mMediaRouteDescriptorList_b.contains(descriptor)) {
                throw new IllegalArgumentException("route descriptor already added");
            }

            mMediaRouteDescriptorList_b.add(descriptor);
        }

        return this;
    }
}