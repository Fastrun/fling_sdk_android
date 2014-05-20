
package com.fireflycast.server.cast.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.fireflycast.server.cast.bridge.CastConnectedClient_ayc;
import com.fireflycast.server.cast.bridge.CastServerBinder_ayf;
import com.fireflycast.server.utils.Logs_avu;

import java.util.ArrayList;
import java.util.List;

public class CastService extends Service {
    private static final Logs_avu mLogs_a = new Logs_avu("CastService");
    private Handler mHandler_b;
    private List mList_c;

    public static Logs_avu getLogs_a()
    {
        return mLogs_a;
    }

    public static List getList_a(CastService castservice)
    {
        return castservice.mList_c;
    }

    private synchronized void removeCastSrvController_a(CastConnectedClient_ayc ayc)
    {
        mList_c.remove(ayc);
    }

    public static void removeCastSrvController_a(CastService castservice,
            CastConnectedClient_ayc ayc)
    {
        castservice.removeCastSrvController_a(ayc);
    }

    public static Handler getHandler_b(CastService castservice)
    {
        return castservice.mHandler_b;
    }

    public IBinder onBind(Intent intent) {
        if (!"com.fireflycast.cast.service.BIND_CAST_DEVICE_CONTROLLER_SERVICE"
                .equals(intent.getAction())) {
            return null;
        }

        mLogs_a.d("onBind!!!!", new Object[0]);
        return (new CastServerBinder_ayf(this, (byte) 0)).asBinder();
    }

    public void onCreate() {
        mList_c = new ArrayList();
        mHandler_b = new Handler(Looper.getMainLooper());
    }

    public int onStartCommand(Intent intent, int i, int j)
    {
        return 1;
    }


}
