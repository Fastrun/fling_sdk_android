
package com.fireflycast.server.cast;

import com.fireflycast.server.cast.bridge.ICastSrvController_axy;
import com.fireflycast.server.common.checker.ObjEqualChecker_avo;
import com.fireflycast.server.utils.CastStatusCodes;
import com.fireflycast.server.utils.Logs_avu;

import java.util.ArrayList;
import java.util.Iterator;

final class CastSrvControllerImpl_awe implements ICastSrvController_axy {
    final CastDeviceControllerHelper_awu a;
    final CastMediaRouteProvider_awb mCastMediaRouteProvider_b;

    CastSrvControllerImpl_awe(CastMediaRouteProvider_awb provider, CastDeviceControllerHelper_awu awu)
    {
        super();
        mCastMediaRouteProvider_b = provider;
        a = awu;
    }

    public final void onConnected_a()
    {
        CastMediaRouteProvider_awb.getLogs_a()
                .d("CastDeviceController.Listener.onConnected", new Object[0]);
        // ((C_nv) (mCastMediaRouteProvider_b)).c.post(new C_awf(this));

        ((MediaRouteProvider_nv) (mCastMediaRouteProvider_b)).mHandler_c.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!a.isEmpty_a())
                {
                    a.c = false;
                    CastMediaRouteProvider_awb.publishRoutes_a(mCastMediaRouteProvider_b);
                    Iterator iterator = (new ArrayList(a.e)).iterator();
                    while (iterator.hasNext())
                        ((CastRouteController_awn) iterator.next()).startSession_f();
                }
            }

        });
    }

    public final void onDisconnected_a(final int i)
    {
        Logs_avu avu1 = CastMediaRouteProvider_awb.getLogs_a();
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(i);
        avu1.d("CastDeviceController.Listener.onDisconnected: %d", aobj);

        // ((C_nv) (mCastMediaRouteProvider_b)).c.post(new C_awi(this, i));
        ((MediaRouteProvider_nv) (mCastMediaRouteProvider_b)).mHandler_c.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (a.isEmpty_a())
                    return;
                a.c = false;
                a.d = true;
                if (i == 0)
                {
                    for (Iterator iterator = (new ArrayList(a.e)).iterator(); iterator.hasNext(); ((CastRouteController_awn) iterator
                            .next()).g())
                        ;
                } else
                {
                    CastMediaRouteProvider_awb.getLogs_a().d("MRP trying to reconnect",
                            new Object[0]);
                    a.c = true;
                    a.mCastDeviceController_a.connectDevice_b();
                }
                CastMediaRouteProvider_awb.publishRoutes_a(mCastMediaRouteProvider_b);
            }

        });

    }

    public final void onApplicationConnected_a(final ApplicationMetadata applicationmetadata,
            final String s,
            final String sessionId, boolean flag)
    {
        CastMediaRouteProvider_awb.getLogs_a().d(
                "CastDeviceController.Listener.onApplicationConnected: sessionId=%s", new Object[] {
                    sessionId
                });
        // ((C_nv) (mCastMediaRouteProvider_b)).c.post(new C_awj(this,
        // applicationmetadata, s,
        // sessionId, flag));

        ((MediaRouteProvider_nv) (mCastMediaRouteProvider_b)).mHandler_c.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!a.isEmpty_a())
                {
                    Iterator iterator = (new ArrayList(a.e)).iterator();
                    boolean flag = false;
                    while (iterator.hasNext())
                    {
                        CastRouteController_awn awn1 = (CastRouteController_awn) iterator.next();
                        CastMediaRouteProvider_awb.getLogs_a().d(
                                "onApplicationConnected: sessionId=%s",
                                new Object[] {
                                    sessionId
                                });
                        Logs_avu avu1 = CastMediaRouteProvider_awb.getLogs_a();
                        Object aobj[] = new Object[1];
                        aobj[0] = awn1.mMediaRouteSession_e;
                        avu1.d("mSession = %s", aobj);
                        if (awn1.mMediaRouteSession_e != null)
                            awn1.mMediaRouteSession_e.onApplicationConnected_a(applicationmetadata,
                                    sessionId);
                        boolean flag1;
                        if (!ObjEqualChecker_avo.isEquals_a(s, awn1.d))
                        {
                            awn1.d = s;
                            flag1 = true;
                        } else
                        {
                            flag1 = false;
                        }
                        flag = flag1 | flag;
                    }
                    if (flag)
                    {
                        CastMediaRouteProvider_awb.publishRoutes_a(mCastMediaRouteProvider_b);
                        return;
                    }
                }

            }
        });
    }

    public final void onVolumeChanged_a(final String s, final double d, boolean f)
    {
        // ((C_nv) (mCastMediaRouteProvider_b)).c.post(new C_awm(this, s, d1));

        ((MediaRouteProvider_nv) (mCastMediaRouteProvider_b)).mHandler_c.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (a.isEmpty_a()) {
                    return;
                }

                boolean flag = false;
                Iterator iterator = (new ArrayList(a.e)).iterator();
                while (iterator.hasNext()) {
                    CastRouteController_awn awn1 = (CastRouteController_awn) iterator.next();
                    // String s = a;
                    boolean flag1;
                    boolean flag2;
                    boolean flag3;
                    // double d;
                    Logs_avu avu1;
                    Object aobj[];
                    if (awn1.mCastDeviceController_b != null
                            && !ObjEqualChecker_avo.isEquals_a(s, awn1.d))
                    {
                        awn1.d = s;
                        flag1 = true;
                    } else
                    {
                        flag1 = false;
                    }
                    flag2 = flag | flag1;
                    // d = b;
                    if (awn1.mCastDeviceController_b == null) {
                        flag3 = false;
                        flag = flag3 | flag2;
                        continue;
                    }
                    avu1 = CastMediaRouteProvider_awb.getLogs_a();
                    aobj = new Object[2];
                    aobj[0] = Double.valueOf(d);
                    aobj[1] = Double.valueOf(awn1.c);
                    avu1.d("onVolumeChanged to %f, was %f", aobj);
                    if (d == awn1.c) {
                        flag3 = false;
                        flag = flag3 | flag2;
                        continue;
                    }
                    awn1.c = d;
                    flag3 = true;
                }

                if (!flag) {
                    return;
                }

                CastMediaRouteProvider_awb.publishRoutes_a(mCastMediaRouteProvider_b);
            }
        });
    }

    public final void a(String s, long l)
    {
    }

    public final void a(String s, long l, int i)
    {
    }

    public final void notifyOnMessageReceived_a(String s, String s1)
    {
    }

    public final void a(String s, byte abyte0[])
    {
    }

    public final void onConnectedWithoutApp_b()
    {
        CastMediaRouteProvider_awb.getLogs_a().d(
                "CastDeviceController.Listener.onConnectedWithoutApp",
                new Object[0]);
        // ((C_nv) (mCastMediaRouteProvider_b)).c.post(new C_awg(this));

        ((MediaRouteProvider_nv) (mCastMediaRouteProvider_b)).mHandler_c.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!a.isEmpty_a())
                {
                    a.c = false;
                    a.d = true;
                    Iterator iterator = (new ArrayList(a.e)).iterator();
                    while (iterator.hasNext())
                    {
                        CastRouteController_awn awn1 = (CastRouteController_awn) iterator.next();
                        awn1.startSession_f();
                        awn1.onApplicationDisconnected_c(CastStatusCodes.APPLICATION_NOT_RUNNING); // 2005
                    }
                }
            }

        });
    }

    public final void onApplicationConnectionFailed_b(final int statusCode)
    {
        ((MediaRouteProvider_nv) (mCastMediaRouteProvider_b)).mHandler_c.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!a.isEmpty_a())
                {
                    Iterator iterator = (new ArrayList(a.e)).iterator();
                    while (iterator.hasNext())
                    {
                        CastRouteController_awn awn1 = (CastRouteController_awn) iterator.next();
                        Logs_avu avu1 = CastMediaRouteProvider_awb.getLogs_a();
                        Object aobj[] = new Object[1];
                        aobj[0] = Integer.valueOf(statusCode);
                        avu1.d("onApplicationConnectionFailed: statusCode=%d", aobj);
                        awn1.h = false;
                        awn1.sendPendingIntent_a(awn1.getSessionId_e(), 1);
                        awn1.disconnectCastMirror_b(true);
                        if (awn1.mMediaRouteSession_e != null)
                            awn1.mMediaRouteSession_e.onApplicationConnectionFailed_a(statusCode);
                    }
                }
            }

        });
    }

    public final void onConnectionFailed_c()
    {
        Logs_avu avu1 = CastMediaRouteProvider_awb.getLogs_a();
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(7);
        avu1.d("CastDeviceController.Listener.onConnectionFailed: %d", aobj);
        // ((C_nv) (mCastMediaRouteProvider_b)).c.post(new C_awh(this));

        ((MediaRouteProvider_nv) (mCastMediaRouteProvider_b)).mHandler_c.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!a.isEmpty_a())
                {
                    a.c = false;
                    a.d = true;
                    Iterator iterator = (new ArrayList(a.e)).iterator();
                    while (iterator.hasNext())
                        ((CastRouteController_awn) iterator.next()).g();
                }
            }

        });
    }

    public final void onRequestStatus_c(int i)
    {
    }

    public final void onInvalidRequest_d()
    {
    }

    public final void onApplicationDisconnected_d(final int statusCode)
    {
        ((MediaRouteProvider_nv) (mCastMediaRouteProvider_b)).mHandler_c.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!a.isEmpty_a())
                {
                    Iterator iterator = (new ArrayList(a.e)).iterator();
                    while (iterator.hasNext())
                        ((CastRouteController_awn) iterator.next())
                                .onApplicationDisconnected_c(statusCode);
                }
            }

        });
    }
}
