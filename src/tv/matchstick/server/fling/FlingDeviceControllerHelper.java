
package tv.matchstick.server.fling;

import java.util.ArrayList;
import java.util.List;

final class FlingDeviceControllerHelper {
    public FlingDeviceController mFlingDeviceController;
    public boolean b;
    public boolean c;
    public boolean d;
    public final List e = new ArrayList();
    final FlingMediaRouteProvider f;

    public FlingDeviceControllerHelper(FlingMediaRouteProvider awb)
    {
        super();

        f = awb;
        b = true;
    }

    public final boolean isEmpty()
    {
        return e.isEmpty();
    }
}
