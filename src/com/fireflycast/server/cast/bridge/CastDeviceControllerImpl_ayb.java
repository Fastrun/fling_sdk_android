
package com.fireflycast.server.cast.bridge;

import com.fireflycast.server.cast.CastDeviceController_axs;
import com.fireflycast.server.cast.service.CastService;

public final class CastDeviceControllerImpl_ayb extends CastDeviceControllerStub_avq {
    final CastService mCastService_a;
    private final CastDeviceController_axs mCastDeviceController_b;

    public CastDeviceControllerImpl_ayb(CastService castservice, CastDeviceController_axs axs1)
    {
        super();
        mCastService_a = castservice;
        mCastDeviceController_b = axs1;
    }

    public final void disconnect_a()
    {
        if (!mCastDeviceController_b.isDisposed_i())
            mCastDeviceController_b.releaseReference_q();
    }

    public final void setVolume_a(double volume, double d1, boolean flag)
    {
        mCastDeviceController_b.setVolume_a(volume, d1, flag);
    }

    public final void stopApplication_a(String sessionId)
    {
        mCastDeviceController_b.stopApplication_b(sessionId);
    }

    public final void joinApplication_a(String applicationId, String sessionId)
    {
        mCastDeviceController_b.joinApplication_b(applicationId, sessionId);
    }

    public final void sendMessage_a(String namespace, String message, long id)
    {
        if (namespace == null || namespace.length() > 128)
        {
            return;
        } else
        {
            mCastDeviceController_b.sendMessage_a(namespace, message, id);
            return;
        }
    }

    public final void launchApplication_a(String applicationId, boolean relaunchIfRunning)
    {
        mCastDeviceController_b.launchApplication_a(applicationId, null, relaunchIfRunning);
    }

    public final void sendBinaryMessage_a(String s, byte abyte0[], long l)
    {
        if (s == null || s.length() > 128)
        {
            return;
        } else
        {
            mCastDeviceController_b.sendBinaryMessage_a(s, abyte0, l);
            return;
        }
    }

    public final void setMute_a(boolean flag, double d, boolean flag1)
    {
        mCastDeviceController_b.setMute_a(flag, d, flag1);
    }

    public final void leaveApplication_b()
    {
        mCastDeviceController_b.leaveApplication_j();
    }

    public final void setMessageReceivedCallbacks_b(String s)
    {
        mCastDeviceController_b.setMessageReceivedCallbacks_d(s);
    }

    public final void requestStatus_c()
    {
        mCastDeviceController_b.requestStatus_l();
    }

    public final void removeMessageReceivedCallbacks_c(String s)
    {
        mCastDeviceController_b.removeMessageReceivedCallbacks_f(s);
    }
}
