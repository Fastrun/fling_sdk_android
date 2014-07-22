
package tv.matchstick.server.fling;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public final class CategoriesData {
    ArrayList mControlCategories;

    public CategoriesData()
    {
    }

    public CategoriesData(MediaRouteSelector selector)
    {
        if (selector == null)
            throw new IllegalArgumentException("selector must not be null");
        MediaRouteSelector.ensureControlCategories(selector);
        if (!MediaRouteSelector.getControlCategories(selector).isEmpty())
            mControlCategories = new ArrayList(
                    MediaRouteSelector.getControlCategories(selector));
    }

    public final CategoriesData addCategoryList(Collection categories)
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
                if (mControlCategories == null)
                    mControlCategories = new ArrayList();
                if (!mControlCategories.contains(category))
                    mControlCategories.add(category);
            } while (true);
        }
        return this;
    }
}
