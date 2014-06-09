
package com.fireflycast.server.cast;

import java.util.ArrayList;
import java.util.List;

final class CastDeviceControllerHelper {
    public CastDeviceController mCastDeviceController_a;
    public boolean b;
    public boolean c;
    public boolean d;
    public final List e = new ArrayList();
    final CastMediaRouteProvider f;

    public CastDeviceControllerHelper(CastMediaRouteProvider awb)
    {
        super();

        f = awb;
        b = true;
    }

    public final boolean isEmpty_a()
    {
        return e.isEmpty();
    }
}
