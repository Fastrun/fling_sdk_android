
package com.fireflycast.server.cast;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.common.checker.ObjEqualChecker_avo;

import java.util.Arrays;
import java.util.Set;

final class DiscoveryCriteriaHelper_awv {
    final CastDevice mCastDevice_a;
    final Set mDiscoveryCriteriaSet_b;

    public DiscoveryCriteriaHelper_awv(CastDevice castdevice, Set set)
    {
        mCastDevice_a = castdevice;
        mDiscoveryCriteriaSet_b = set;
    }

    public final boolean equals(Object obj)
    {
        boolean flag = true;
        if (obj == null || !(obj instanceof DiscoveryCriteriaHelper_awv))
            flag = false;
        else if (obj != this)
        {
            DiscoveryCriteriaHelper_awv helper = (DiscoveryCriteriaHelper_awv) obj;
            if (!ObjEqualChecker_avo.isEquals_a(mCastDevice_a, helper.mCastDevice_a)
                    || !ObjEqualChecker_avo.isEquals_a(mDiscoveryCriteriaSet_b,
                            helper.mDiscoveryCriteriaSet_b))
                return false;
        }
        return flag;
    }

    public final int hashCode()
    {
        Object aobj[] = new Object[2];
        aobj[0] = mCastDevice_a;
        aobj[1] = mDiscoveryCriteriaSet_b;
        return Arrays.hashCode(aobj);
    }
}
