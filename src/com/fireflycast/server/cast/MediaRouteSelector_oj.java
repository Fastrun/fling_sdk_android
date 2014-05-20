
package com.fireflycast.server.cast;

import android.os.Bundle;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MediaRouteSelector_oj {
    public static final MediaRouteSelector_oj EMPTY = new MediaRouteSelector_oj(new Bundle(), null);
    private final Bundle mBundle_b;
    private List mControlCategories_c;

    private MediaRouteSelector_oj(Bundle bundle, List controlCategories)
    {
        mBundle_b = bundle;
        mControlCategories_c = controlCategories;
    }

    MediaRouteSelector_oj(Bundle bundle, List controlCategories, byte byte0)
    {
        this(bundle, controlCategories);
    }

    public static MediaRouteSelector_oj fromBundle_a(Bundle bundle)
    {
        if (bundle != null)
            return new MediaRouteSelector_oj(bundle, null);
        else
            return null;
    }

    static void ensureControlCategories_a(MediaRouteSelector_oj selector)
    {
        selector.ensureControlCategories_e();
    }

    static List getControlCategories_b(MediaRouteSelector_oj selector)
    {
        return selector.mControlCategories_c;
    }

    private void ensureControlCategories_e()
    {
        if (mControlCategories_c == null)
        {
            mControlCategories_c = mBundle_b.getStringArrayList("controlCategories");
            if (mControlCategories_c == null || mControlCategories_c.isEmpty())
                mControlCategories_c = Collections.emptyList();
        }
    }

    public final List getControlCategories_a()
    {
        ensureControlCategories_e();
        return mControlCategories_c;
    }

    public final boolean isEmpty_b()
    {
        ensureControlCategories_e();
        return mControlCategories_c.isEmpty();
    }

    public final boolean isValid_c()
    {
        ensureControlCategories_e();
        return !mControlCategories_c.contains(null);
    }

    public final Bundle asBundle_d()
    {
        return mBundle_b;
    }

    public final boolean equals(Object obj)
    {
        if (obj instanceof MediaRouteSelector_oj)
        {
            MediaRouteSelector_oj other = (MediaRouteSelector_oj) obj;
            ensureControlCategories_e();
            other.ensureControlCategories_e();
            return mControlCategories_c.equals(other.mControlCategories_c);
        } else
        {
            return false;
        }
    }

    public final int hashCode()
    {
        ensureControlCategories_e();
        return mControlCategories_c.hashCode();
    }

    public final String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("MediaRouteSelector{ ");
        stringbuilder.append("controlCategories=").append(
                Arrays.toString(getControlCategories_a().toArray()));
        stringbuilder.append(" }");
        return stringbuilder.toString();
    }
}
