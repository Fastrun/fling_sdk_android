
package com.fireflycast.server.cast;

import android.text.TextUtils;

import com.fireflycast.server.common.checker.ObjEqualChecker;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class DiscoveryCriteria {
    public String mCategory_a;
    public String mAppid_b;
    public final Set mNamespaceList_c = new HashSet();

    public DiscoveryCriteria()
    {
    }

    public static DiscoveryCriteria getDiscoveryCriteria_a(String category)
    {
        if (!category.equals("com.fireflycast.cast.CATEGORY_CAST_REMOTE_PLAYBACK")
                && !category.equals("com.fireflycast.cast.CATEGORY_CAST")
                && !category.startsWith("com.fireflycast.cast.CATEGORY_CAST_REMOTE_PLAYBACK/")
                && !category.startsWith("com.fireflycast.cast.CATEGORY_CAST/"))
            throw new IllegalArgumentException((new StringBuilder(
                    "Invalid discovery control category: ")).append(category).toString());

        DiscoveryCriteria criteria = new DiscoveryCriteria();
        criteria.mCategory_a = category;
        String parts[] = TextUtils.split(category, "/");
        switch (parts.length)
        {
            default:
                throw new IllegalArgumentException((new StringBuilder(
                        "Could not parse criteria from control category: ")).append(category).toString());

            case 2: // '\002'
                criteria.mAppid_b = parts[1];
                // fall through

            case 1: // '\001'
                return criteria;

            case 3: // '\003'
                break;
        }
        if (!TextUtils.isEmpty(parts[1]))
            criteria.mAppid_b = parts[1];
        java.util.List list = Arrays.asList(TextUtils.split(parts[2], ","));
        checkNamespaces_a(((Collection) (list)));
        criteria.mNamespaceList_c.addAll(list);
        return criteria;
    }

    private static void checkNamespaces_a(Collection collection)
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
            if (!ObjEqualChecker.isEquals_a(mCategory_a, criteria.mCategory_a)
                    || !ObjEqualChecker.isEquals_a(mAppid_b, criteria.mAppid_b)
                    || !ObjEqualChecker.isEquals_a(mNamespaceList_c, criteria.mNamespaceList_c))
                return false;
        }
        return flag;
    }

    public final int hashCode()
    {
        Object aobj[] = new Object[3];
        aobj[0] = mCategory_a;
        aobj[1] = mAppid_b;
        aobj[2] = mNamespaceList_c;
        return Arrays.hashCode(aobj);
    }

    public final String toString()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("DiscoveryCriteria(category=").append(mCategory_a).append("; appid=")
                .append(mAppid_b)
                .append("; ns=").append(TextUtils.join(",", mNamespaceList_c)).append(")");
        return stringbuilder.toString();
    }
}
