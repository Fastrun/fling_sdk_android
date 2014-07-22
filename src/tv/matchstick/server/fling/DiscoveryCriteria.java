
package tv.matchstick.server.fling;

import android.text.TextUtils;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import tv.matchstick.server.common.checker.ObjEqualChecker;

public final class DiscoveryCriteria {
    public String mCategory;
    public String mAppid;
    public final Set mNamespaceList = new HashSet();

    public DiscoveryCriteria()
    {
    }

    public static DiscoveryCriteria getDiscoveryCriteria(String category)
    {
        if (!category.equals("tv.matchstick.fling.CATEGORY_FLING_REMOTE_PLAYBACK")
                && !category.equals("tv.matchstick.fling.CATEGORY_FLING")
                && !category.startsWith("tv.matchstick.fling.CATEGORY_FLING_REMOTE_PLAYBACK/")
                && !category.startsWith("tv.matchstick.fling.CATEGORY_FLING/"))
            throw new IllegalArgumentException((new StringBuilder(
                    "Invalid discovery control category: ")).append(category).toString());

        DiscoveryCriteria criteria = new DiscoveryCriteria();
        criteria.mCategory = category;
        String parts[] = TextUtils.split(category, "/");
        switch (parts.length)
        {
            default:
                throw new IllegalArgumentException("Could not parse criteria from control category: " + category);

            case 2: // '\002'
                criteria.mAppid = parts[1];
                // fall through

            case 1: // '\001'
                return criteria;

            case 3: // '\003'
                break;
        }
        if (!TextUtils.isEmpty(parts[1]))
            criteria.mAppid = parts[1];
        java.util.List list = Arrays.asList(TextUtils.split(parts[2], ","));
        checkNamespaces(((Collection) (list)));
        criteria.mNamespaceList.addAll(list);
        return criteria;
    }

    private static void checkNamespaces(Collection collection)
    {
        if (collection != null && collection.size() > 0)
        {
            Iterator iterator = collection.iterator();
            String namespace;
            do
            {
                if (!iterator.hasNext()) {
                    // break MISSING_BLOCK_LABEL_78;
                    return;
                }
                namespace = (String) iterator.next();
            } while (!TextUtils.isEmpty(namespace) && !namespace.trim().equals(""));
            throw new IllegalArgumentException("Namespaces must not be null or empty");
        } else
        {
            throw new IllegalArgumentException("Must specify at least one namespace");
        }
    }

    public final boolean equals(Object obj)
    {
        boolean flag = true;
        if (obj == null || !(obj instanceof DiscoveryCriteria))
            flag = false;
        else if (obj != this)
        {
            DiscoveryCriteria criteria = (DiscoveryCriteria) obj;
            if (!ObjEqualChecker.isEquals(mCategory, criteria.mCategory)
                    || !ObjEqualChecker.isEquals(mAppid, criteria.mAppid)
                    || !ObjEqualChecker.isEquals(mNamespaceList, criteria.mNamespaceList))
                return false;
        }
        return flag;
    }

    public final int hashCode()
    {
        Object aobj[] = new Object[3];
        aobj[0] = mCategory;
        aobj[1] = mAppid;
        aobj[2] = mNamespaceList;
        return Arrays.hashCode(aobj);
    }

    public final String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("DiscoveryCriteria(category=").append(mCategory).append("; appid=")
                .append(mAppid)
                .append("; ns=").append(TextUtils.join(",", mNamespaceList)).append(")");
        return stringbuilder.toString();
    }
}
