
package com.fireflycast.server.cast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class CategoriesData {
    ArrayList mControlCategories_a;

    public CategoriesData()
    {
    }

    public CategoriesData(MediaRouteSelector selector)
    {
        if (selector == null)
            throw new IllegalArgumentException("selector must not be null");
        MediaRouteSelector.ensureControlCategories_a(selector);
        if (!MediaRouteSelector.getControlCategories_b(selector).isEmpty())
            mControlCategories_a = new ArrayList(
                    MediaRouteSelector.getControlCategories_b(selector));
    }

    public final CategoriesData addCategoryList_a(Collection categories)
    {
        if (categories == null)
            throw new IllegalArgumentException("categories must not be null");
        if (!categories.isEmpty())
        {
            Iterator iterator = categories.iterator();
            do
            {
                if (!iterator.hasNext())
                    break;
                String category = (String) iterator.next();
                if (category == null)
                    throw new IllegalArgumentException("category must not be null");
                if (mControlCategories_a == null)
                    mControlCategories_a = new ArrayList();
                if (!mControlCategories_a.contains(category))
                    mControlCategories_a.add(category);
            } while (true);
        }
        return this;
    }
}
