
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.cast.service.CastService;

public final class CastServerBinder_ayf extends FireflySrvBinder_bfd {
    final CastService mCastService_a;

    private CastServerBinder_ayf(CastService castservice) {
        super();
        mCastService_a = castservice;
    }

    public CastServerBinder_ayf(CastService castservice, byte byte0) {
        this(castservice);
    }

    public final void initCastService_a(IFireflyCallbacks_bgj bgj1, int i, String packageName_s,
            IBinder ibinder, Bundle bundle) {
        CastDevice castdevice;
        String lastApplicationId;
        String lastSessionId;
        long flags;
        Object obj;

        CastService.getLogs_a().d("begin initCastService_a!", new Object[0]);
        try
        {
            castdevice = CastDevice.getFromBundle_b(bundle);
            lastApplicationId = bundle.getString("last_application_id");
            lastSessionId = bundle.getString("last_session_id");
        } catch (Exception exception) {
            CastService.getLogs_a().e(exception, "Cast device was not valid.", new Object[0]);
            try {
                bgj1.onConnect_a(10, null, null);
            } catch (RemoteException remoteexception) {
                CastService.getLogs_a().d("client died while brokering service", new Object[0]);
            }
            return;
        }
        flags = bundle.getLong("com.fireflycast.cast.EXTRA_CAST_FLAGS", 0L);
        CastService.getLogs_a().d("connecting to device with lastApplicationId=%s, lastSessionId=%s",
                new Object[] {
                        lastApplicationId, lastSessionId
                });
        if (ibinder == null) {
            obj = null;
        } else {
            android.os.IInterface iinterface = ibinder
                    .queryLocalInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            if (iinterface != null && (iinterface instanceof ICastDeviceControllerListener_avr))
                obj = (ICastDeviceControllerListener_avr) iinterface;
            else
                obj = new CastDeviceControllerListener_avt(ibinder);
        }

        CastService.getList_a(mCastService_a).add(
                new CastConnectedClient_ayc(mCastService_a, bgj1, castdevice, lastApplicationId,
                        lastSessionId, ((ICastDeviceControllerListener_avr) (obj)), packageName_s,
                        flags));

        CastService.getLogs_a().d("end initCastService_a!", new Object[0]);
    }
}
