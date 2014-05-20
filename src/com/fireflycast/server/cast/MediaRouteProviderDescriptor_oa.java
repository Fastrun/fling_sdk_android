
package com.fireflycast.server.cast;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MediaRouteProviderDescriptor_oa {
    final Bundle mRoutes_a;
    private List mMediaRouteDescriptorList_b;

    private MediaRouteProviderDescriptor_oa(Bundle paramBundle, List paramList) {
        this.mRoutes_a = paramBundle;
        this.mMediaRouteDescriptorList_b = paramList;
    }

    public MediaRouteProviderDescriptor_oa(Bundle paramBundle, ArrayList paramList, byte b) {
        this(paramBundle, paramList);
    }

    private List getMediaRouteDescriptorList_a() {
        check_b();
        return this.mMediaRouteDescriptorList_b;
    }

    private void check_b() {
        if (mMediaRouteDescriptorList_b == null)
        {
            ArrayList arraylist = mRoutes_a.getParcelableArrayList("routes");
            if (arraylist == null || arraylist.isEmpty())
            {
                mMediaRouteDescriptorList_b = Collections.emptyList();
            } else
            {
                int i = arraylist.size();
                mMediaRouteDescriptorList_b = new ArrayList(i);
                int j = 0;
                while (j < i)
                {
                    List list = mMediaRouteDescriptorList_b;
                    Bundle bundle = (Bundle) arraylist.get(j);
                    MediaRouteDescriptor_ns ns1;
                    if (bundle != null)
                        ns1 = new MediaRouteDescriptor_ns(bundle, null);
                    else
                        ns1 = null;
                    list.add(ns1);
                    j++;
                }
            }
        }
    }

    private boolean isValid_c()
    {
        check_b();
        int i = this.mMediaRouteDescriptorList_b.size();
        for (int j = 0; j < i; j++)
        {
            MediaRouteDescriptor_ns localns = (MediaRouteDescriptor_ns) this.mMediaRouteDescriptorList_b
                    .get(j);
            if ((localns == null) || (!localns.isNoteEmpty_a()))
                return false;
        }
        return true;
    }

    public final String toString()
    {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("MediaRouteProviderDescriptor{ ");
        localStringBuilder.append("routes=").append(
                Arrays.toString(getMediaRouteDescriptorList_a().toArray()));
        localStringBuilder.append(", isValid=").append(isValid_c());
        localStringBuilder.append(" }");
        return localStringBuilder.toString();
    }
}
