
package tv.matchstick.server.common.checker;

public final class ObjEqualChecker {
    public static boolean isEquals(Object obj, Object obj1) {
        return obj == null && obj1 == null || obj != null && obj1 != null && obj.equals(obj1);
    }
}
