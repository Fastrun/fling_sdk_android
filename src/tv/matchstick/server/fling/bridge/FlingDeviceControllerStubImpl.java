
package tv.matchstick.server.fling.bridge;

import tv.matchstick.fling.service.FlingService;
import tv.matchstick.server.fling.FlingDeviceController;

public final class FlingDeviceControllerStubImpl extends FlingDeviceControllerStub {
    final FlingService mFlingService;
    private final FlingDeviceController mFlingDeviceController;

    public FlingDeviceControllerStubImpl(FlingService service, FlingDeviceController axs1)
    {
        super();
        mFlingService = service;
        mFlingDeviceController = axs1;
    }

    public final void disconnect()
    {
        if (!mFlingDeviceController.isDisposed())
            mFlingDeviceController.releaseReference();
    }

    public final void setVolume(double volume, double d1, boolean flag)
    {
        mFlingDeviceController.setVolume(volume, d1, flag);
    }

    public final void stopApplication(String sessionId)
    {
        mFlingDeviceController.stopApplication(sessionId);
    }

    public final void joinApplication(String applicationId, String sessionId)
    {
        mFlingDeviceController.joinApplication(applicationId, sessionId);
    }

    public final void sendMessage(String namespace, String message, long id)
    {
        if (namespace == null || namespace.length() > 128)
        {
            return;
        } else
        {
            mFlingDeviceController.sendMessage_a(namespace, message, id);
            return;
        }
    }

    public final void launchApplication(String applicationId, boolean relaunchIfRunning)
    {
        mFlingDeviceController.launchApplication(applicationId, null, relaunchIfRunning);
    }

    public final void sendBinaryMessage(String namespace, byte message[], long requestId)
    {
        if (namespace == null || namespace.length() > 128)
        {
            return;
        } else
        {
            mFlingDeviceController.sendBinaryMessage(namespace, message, requestId);
            return;
        }
    }

    public final void setMute(boolean flag, double d, boolean flag1)
    {
        mFlingDeviceController.setMute(flag, d, flag1);
    }

    public final void leaveApplication()
    {
        mFlingDeviceController.leaveApplication();
    }

    public final void setMessageReceivedCallbacks(String namespace)
    {
        mFlingDeviceController.setMessageReceivedCallbacks(namespace);
    }

    public final void requestStatus()
    {
        mFlingDeviceController.requestStatus();
    }

    public final void removeMessageReceivedCallbacks(String s)
    {
        mFlingDeviceController.removeMessageReceivedCallbacks(s);
    }
}
