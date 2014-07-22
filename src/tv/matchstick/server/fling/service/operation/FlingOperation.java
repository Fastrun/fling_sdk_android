
package tv.matchstick.server.fling.service.operation;

import java.io.IOException;

import tv.matchstick.server.fling.FlingDeviceController;

public abstract class FlingOperation {
    public static String TAG = "FlingOperation";
    protected final FlingDeviceController mFlingDeviceController;

    public FlingOperation(FlingDeviceController controller)
    {
        mFlingDeviceController = controller;
        mFlingDeviceController.generateId();
    }

    public abstract void doFling() throws IOException;

    public final void releaseReference()
    {
        mFlingDeviceController.releaseReference();
    }

}
