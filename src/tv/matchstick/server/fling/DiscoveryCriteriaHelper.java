
package tv.matchstick.server.fling;

import java.util.Arrays;
import java.util.Set;

import tv.matchstick.fling.FlingDevice;
import tv.matchstick.server.common.checker.ObjEqualChecker;

final class DiscoveryCriteriaHelper {
    final FlingDevice mFlingDevice;
    final Set mDiscoveryCriteriaSet;

    public DiscoveryCriteriaHelper(FlingDevice flingdevice, Set set)
    {
        mFlingDevice = flingdevice;
        mDiscoveryCriteriaSet = set;
    }

    public final boolean equals(Object obj)
    {
        boolean flag = true;
        if (obj == null || !(obj instanceof DiscoveryCriteriaHelper))
            flag = false;
        else if (obj != this)
        {
            DiscoveryCriteriaHelper helper = (DiscoveryCriteriaHelper) obj;
            if (!ObjEqualChecker.isEquals(mFlingDevice, helper.mFlingDevice)
                    || !ObjEqualChecker.isEquals(mDiscoveryCriteriaSet,
                            helper.mDiscoveryCriteriaSet))
                return false;
        }
        return flag;
    }

    public final int hashCode()
    {
        Object aobj[] = new Object[2];
        aobj[0] = mFlingDevice;
        aobj[1] = mDiscoveryCriteriaSet;
        return Arrays.hashCode(aobj);
    }
}
