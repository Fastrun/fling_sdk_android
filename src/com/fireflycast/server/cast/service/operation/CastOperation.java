
package com.fireflycast.server.cast.service.operation;

import com.fireflycast.server.cast.CastDeviceController;

import java.io.IOException;

public abstract class CastOperation {
    public static String TAG = "CastOperation";
    protected final CastDeviceController mCastDeviceController_b;

    public CastOperation(CastDeviceController controller)
    {
        mCastDeviceController_b = controller;
        mCastDeviceController_b.p();
    }

    public abstract void doCast_a() throws IOException;

    public final void releaseReference_b()
    {
        mCastDeviceController_b.releaseReference_q();
    }

}
