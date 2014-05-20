
package com.fireflycast.server.cast.service.operation;

import com.fireflycast.server.cast.CastDeviceController_axs;

import java.io.IOException;

public abstract class CastOperation_aya {
    public static String TAG = "CastOperation";
    protected final CastDeviceController_axs mCastDeviceController_b;

    public CastOperation_aya(CastDeviceController_axs controller)
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
