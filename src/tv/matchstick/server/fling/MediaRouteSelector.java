
package tv.matchstick.server.fling;

import android.os.Bundle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MediaRouteSelector {
    public static final MediaRouteSelector EMPTY = new MediaRouteSelector(new Bundle(), null);
    private final Bundle mBundle;
    private List mControlCategories;

    private MediaRouteSelector(Bundle bundle, List controlCategories)
    {
        mBundle = bundle;
        mControlCategories = controlCategories;
    }

    MediaRouteSelector(Bundle bundle, List controlCategories, byte byte0)
    {
        this(bundle, controlCategories);
    }

    public static MediaRouteSelector fromBundle(Bundle bundle)
    {
        if (bundle != null)
            return new MediaRouteSelector(bundle, null);
        else
            return null;
    }

    static void ensureControlCategories(MediaRouteSelector selector)
    {
        selector.ensureControlCategories();
    }

    static List getControlCategories(MediaRouteSelector selector)
    {
        return selector.mControlCategories;
    }

    private void ensureControlCategories()
    {
        if (mControlCategories == null)
        {
            mControlCategories = mBundle.getStringArrayList("controlCategories");
            if (mControlCategories == null || mControlCategories.isEmpty())
                mControlCategories = Collections.emptyList();
        }
    }

    public final List getControlCategories()
    {
        ensureControlCategories();
        return mControlCategories;
    }

    public final boolean isEmpty()
    {
        ensureControlCategories();
        return mControlCategories.isEmpty();
    }

    public final boolean isValid()
    {
        ensureControlCategories();
        return !mControlCategories.contains(null);
    }

    public final Bundle asBundle()
    {
        return mBundle;
    }

    public final boolean equals(Object obj)
    {
        if (obj instanceof MediaRouteSelector)
        {
            MediaRouteSelector other = (MediaRouteSelector) obj;
            ensureControlCategories();
            other.ensureControlCategories();
            return mControlCategories.equals(other.mControlCategories);
        } else
        {
            return false;
        }
    }

    public final int hashCode()
    {
        ensureControlCategories();
        return mControlCategories.hashCode();
    }

    public final String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("MediaRouteSelector{ ");
        stringbuilder.append("controlCategories=").append(
                Arrays.toString(getControlCategories().toArray()));
        stringbuilder.append(" }");
        return stringbuilder.toString();
    }
}
