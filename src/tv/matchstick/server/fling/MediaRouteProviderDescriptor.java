
package tv.matchstick.server.fling;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MediaRouteProviderDescriptor {
    final Bundle mRoutes;
    private List mMediaRouteDescriptorList;

    private MediaRouteProviderDescriptor(Bundle paramBundle, List paramList) {
        this.mRoutes = paramBundle;
        this.mMediaRouteDescriptorList = paramList;
    }

    public MediaRouteProviderDescriptor(Bundle paramBundle, ArrayList paramList, byte b) {
        this(paramBundle, paramList);
    }

    private List getMediaRouteDescriptorList() {
        check_b();
        return this.mMediaRouteDescriptorList;
    }

    private void check_b() {
        if (mMediaRouteDescriptorList == null)
        {
            ArrayList arraylist = mRoutes.getParcelableArrayList("routes");
            if (arraylist == null || arraylist.isEmpty())
            {
                mMediaRouteDescriptorList = Collections.emptyList();
            } else
            {
                int i = arraylist.size();
                mMediaRouteDescriptorList = new ArrayList(i);
                int j = 0;
                while (j < i)
                {
                    List list = mMediaRouteDescriptorList;
                    Bundle bundle = (Bundle) arraylist.get(j);
                    MediaRouteDescriptor ns1;
                    if (bundle != null)
                        ns1 = new MediaRouteDescriptor(bundle, null);
                    else
                        ns1 = null;
                    list.add(ns1);
                    j++;
                }
            }
        }
    }

    private boolean isValid()
    {
        check_b();
        int i = this.mMediaRouteDescriptorList.size();
        for (int j = 0; j < i; j++)
        {
            MediaRouteDescriptor localns = (MediaRouteDescriptor) this.mMediaRouteDescriptorList
                    .get(j);
            if ((localns == null) || (!localns.isNoteEmpty()))
                return false;
        }
        return true;
    }

    public final String toString()
    {
        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("MediaRouteProviderDescriptor{ ");
        localStringBuilder.append("routes=").append(
                Arrays.toString(getMediaRouteDescriptorList().toArray()));
        localStringBuilder.append(", isValid=").append(isValid());
        localStringBuilder.append(" }");
        return localStringBuilder.toString();
    }
}
