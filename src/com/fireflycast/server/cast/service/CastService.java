
package com.fireflycast.server.cast.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import com.fireflycast.server.cast.bridge.CastConnectedClient;
import com.fireflycast.server.cast.bridge.CastServerBinder;
import com.fireflycast.server.utils.Logs;

import java.util.ArrayList;
import java.util.List;

public class CastService extends Service {
    private static final Logs mLogs_a = new Logs("CastService");
    private Handler mHandler_b;
    private List mList_c;

    public static Logs getLogs_a()
    {
        return mLogs_a;
    }

    public static List getList_a(CastService castservice)
    {
        return castservice.mList_c;
    }

    private synchronized void removeCastSrvController_a(CastConnectedClient ayc)
    {
        mList_c.remove(ayc);
    }

    public static void removeCastSrvController_a(CastService castservice,
            CastConnectedClient ayc)
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
        return (new CastServerBinder(this, (byte) 0)).asBinder();
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
