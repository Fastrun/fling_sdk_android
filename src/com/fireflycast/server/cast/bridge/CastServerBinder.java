
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.cast.service.CastService;

public final class CastServerBinder extends FireflySrvBinder {
    final CastService mCastService_a;

    private CastServerBinder(CastService castservice) {
        super();
        mCastService_a = castservice;
    }

    public CastServerBinder(CastService castservice, byte byte0) {
        this(castservice);
    }

    public final void initCastService_a(IFireflyCallbacks bgj1, int i, String packageName_s,
            IBinder ibinder, Bundle bundle) {
        CastDevice castdevice;
        String lastApplicationId;
        String lastSessionId;
        long flags;
        Object obj;

        CastService.getLogs_a().d("begin initCastService_a!", new Object[0]);
        try
        {
            castdevice = CastDevice.getFromBundle(bundle);
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
            if (iinterface != null && (iinterface instanceof ICastDeviceControllerListener))
                obj = (ICastDeviceControllerListener) iinterface;
            else
                obj = new CastDeviceControllerListener(ibinder);
        }

        CastService.getList_a(mCastService_a).add(
                new CastConnectedClient(mCastService_a, bgj1, castdevice, lastApplicationId,
                        lastSessionId, ((ICastDeviceControllerListener) (obj)), packageName_s,
                        flags));

        CastService.getLogs_a().d("end initCastService_a!", new Object[0]);
    }
}
