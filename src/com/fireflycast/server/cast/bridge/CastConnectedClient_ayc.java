
package com.fireflycast.server.cast.bridge;

import android.os.IBinder.DeathRecipient;
import android.os.RemoteException;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.cast.ApplicationMetadata;
import com.fireflycast.server.cast.CastDeviceController_axs;
import com.fireflycast.server.cast.service.CastService;
import com.fireflycast.server.common.checker.EmptyChecker_bha;
import com.fireflycast.server.utils.CastStatusCodes;
import com.fireflycast.server.utils.Logs_avu;

public final class CastConnectedClient_ayc implements ICastSrvController_axy {
    final CastService mCastService_a;
    private CastDeviceControllerStub_avq mCastDevCtrl_b;
    private final ICastDeviceControllerListener_avr mICastDevCtlListener_c;
    private final DeathRecipient mFireflyCallbackDeathHandler_d;
    private final DeathRecipient mListenerDeathHandler_e;
    private final CastDevice mCastDevice_f;
    private String mLastAppId_g;
    private String mLastSessionId_h;
    private CastDeviceController_axs mCastDeviceCtrl_i;
    private final IFireflyCallbacks_bgj mFireflyCallbacks_j;
    private final String mPackageName_k;
    private final long mFlags_l;

    CastConnectedClient_ayc(CastService castservice, IFireflyCallbacks_bgj bgj1, CastDevice castdevice,
            String lastApplicationId_s, String lastSessionId_s1,
            ICastDeviceControllerListener_avr avr1, String packageName_s2,
            long flags_l1)
    {
        super();

        mCastService_a = castservice;

        mFireflyCallbacks_j = (IFireflyCallbacks_bgj) EmptyChecker_bha.a(bgj1);
        mCastDevice_f = castdevice;
        mLastAppId_g = lastApplicationId_s;
        mLastSessionId_h = lastSessionId_s1;
        mICastDevCtlListener_c = avr1;
        mCastDevCtrl_b = null;
        mPackageName_k = packageName_s2;
        mFlags_l = flags_l1;

        // mDeathRecipient_d = new ADeathRecipient_ayd(this, castservice);
        mFireflyCallbackDeathHandler_d = new DeathRecipient() {

            @Override
            public void binderDied() {
                // TODO Auto-generated method stub
                handleBinderDeath_a(CastConnectedClient_ayc.this);
            }

        };

        // mDeathRecipient_e = new AADeathRecipient_aye(this, castservice);
        mListenerDeathHandler_e = new DeathRecipient() {

            @Override
            public void binderDied() {
                // TODO Auto-generated method stub
                handleBinderDeath_a(CastConnectedClient_ayc.this);
            }

        };

        Logs_avu avu1;
        Object aobj[];
        try
        {
            mICastDevCtlListener_c.asBinder().linkToDeath(mListenerDeathHandler_e, 0);
        } catch (RemoteException remoteexception)
        {
            CastService.getLogs_a().e("client disconnected before listener was set", new Object[0]);
            if (!mCastDeviceCtrl_i.isDisposed_i())
                mCastDeviceCtrl_i.releaseReference_q();
        }
        avu1 = CastService.getLogs_a();
        aobj = new Object[1];
        aobj[0] = mPackageName_k;
        avu1.d("acquireDeviceController by %s", aobj);
        CastService.getLogs_a().d("createControllerStub", new Object[0]);
        mCastDeviceCtrl_i = CastDeviceController_axs.createCastDeviceController_a(mCastService_a,
                CastService.getHandler_b(mCastService_a), mPackageName_k, mCastDevice_f, mFlags_l,
                this);
        mCastDevCtrl_b = new CastDeviceControllerImpl_ayb(mCastService_a, mCastDeviceCtrl_i);
        if (mCastDeviceCtrl_i.d())
        {
            try
            {
                mFireflyCallbacks_j.onConnect_a(0, mCastDevCtrl_b.asBinder(), null);
            } catch (RemoteException remoteexception2)
            {
                CastService.getLogs_a().d("client died while brokering service", new Object[0]);
            }
            return;
        }

        if (!mCastDeviceCtrl_i.isConnecting_e())
        {
            Logs_avu avu2 = CastService.getLogs_a();
            Object aobj1[] = new Object[2];
            aobj1[0] = mLastAppId_g;
            aobj1[1] = mLastSessionId_h;
            avu2.d("reconnecting to device with applicationId=%s, sessionId=%s", aobj1);
            if (mLastAppId_g != null)
                mCastDeviceCtrl_i.reconnectToDevice_a(mLastAppId_g, mLastSessionId_h);
            else
                mCastDeviceCtrl_i.connectDevice_b();
        }

        try
        {
            mFireflyCallbacks_j.asBinder().linkToDeath(mFireflyCallbackDeathHandler_d, 0);
        } catch (RemoteException e)
        {
            CastService.getLogs_a().w("Unable to link listener reaper", new Object[0]);
        }
    }

    static void handleBinderDeath_a(CastConnectedClient_ayc connectedClient)
    {
        if (connectedClient.mCastDeviceCtrl_i != null && !connectedClient.mCastDeviceCtrl_i.isDisposed_i())
        {
            CastService.getLogs_a().w("calling releaseReference from handleBinderDeath()", new Object[0]);
            connectedClient.mCastDeviceCtrl_i.releaseReference_q();
            CastService.getLogs_a().d("Released controller.", new Object[0]);
        }
        CastService.getLogs_a().d("Removing ConnectedClient.", new Object[0]);
        CastService.removeCastSrvController_a(connectedClient.mCastService_a, connectedClient);
    }

    public final void onConnected_a()
    {
        try
        {
            mFireflyCallbacks_j.onConnect_a(0, mCastDevCtrl_b.asBinder(), null);
            CastService.getLogs_a().d("Connected to device.", new Object[0]);
        } catch (RemoteException remoteexception)
        {
            CastService.getLogs_a()
                    .d(remoteexception, "client died while brokering service", new Object[0]);
        }
    }

    public final void onDisconnected_a(int status)
    {
        Logs_avu avu1 = CastService.getLogs_a();
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(status);
        avu1.d("onDisconnected: status=%d", aobj);
        try
        {
            mICastDevCtlListener_c.onDisconnected_a(status);
        } catch (RemoteException remoteexception)
        {
            CastService.getLogs_a()
                    .d(remoteexception, "client died while brokering service", new Object[0]);
        }
        if (!mCastDeviceCtrl_i.isDisposed_i())
        {
            CastService.getLogs_a().w("calling releaseReference from ConnectedClient.onDisconnected",
                    new Object[0]);
            mCastDeviceCtrl_i.releaseReference_q();
        }
    }

    public final void onApplicationConnected_a(ApplicationMetadata applicationmetadata, String s,
            String s1, boolean flag)
    {
        mLastAppId_g = applicationmetadata.getApplicationId_b();
        mLastSessionId_h = s1;
        try
        {
            mICastDevCtlListener_c.onApplicationConnected_a(applicationmetadata, s, s1, flag);
        } catch (RemoteException e)
        {
            e.printStackTrace();
            ;
        }
    }

    public final void onVolumeChanged_a(String status, double volume, boolean muteState)
    {
        try
        {
            mICastDevCtlListener_c.notifyApplicationStatusOrVolumeChanged_a(status, volume,
                    muteState);
            return;
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public final void a(String s, long l1)
    {
        try
        {
            mICastDevCtlListener_c.a(s, l1);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public final void a(String s, long l1, int i1)
    {
        try
        {
            mICastDevCtlListener_c.a(s, l1, i1);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public final void notifyOnMessageReceived_a(String s, String s1)
    {
        try
        {
            mICastDevCtlListener_c.onMessageReceived_a(s, s1);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public final void a(String s, byte abyte0[])
    {
        try
        {
            mICastDevCtlListener_c.a(s, abyte0);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public final void onConnectedWithoutApp_b()
    {
        try
        {
            mFireflyCallbacks_j.onConnect_a(1001, mCastDevCtrl_b.asBinder(), null);
            CastService.getLogs_a().d("Connected to device without app.", new Object[0]);
        } catch (RemoteException e)
        {
            e.printStackTrace();
            CastService.getLogs_a()
                    .d(e, "client died while brokering service", new Object[0]);
        }
    }

    public final void onApplicationConnectionFailed_b(int i1)
    {
        try
        {
            mICastDevCtlListener_c.postApplicationConnectionResult_b(i1);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public final void onConnectionFailed_c()
    {
        try
        {
            mFireflyCallbacks_j.onConnect_a(7, null, null);
        } catch (RemoteException e)
        {
            e.printStackTrace();
            CastService.getLogs_a()
                    .d(e, "client died while brokering service", new Object[0]);
        }
    }

    public final void onRequestStatus_c(int i1)
    {
        try
        {
            mICastDevCtlListener_c.onRequestStatus_d(i1);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public final void onInvalidRequest_d()
    {
        try
        {
            mICastDevCtlListener_c.onRequestResult_c(CastStatusCodes.INVALID_REQUEST); // 2001
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    public final void onApplicationDisconnected_d(int i1)
    {
        try
        {
            mICastDevCtlListener_c.onApplicationDisconnected_e(i1);
        } catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }
}
