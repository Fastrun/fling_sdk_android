
package com.fireflycast.server.common.checker;

public final class ObjEqualChecker_avo {
    public static boolean isEquals_a(Object obj, Object obj1) {
        return obj == null && obj1 == null || obj != null && obj1 != null && obj.equals(obj1);
    }
}
