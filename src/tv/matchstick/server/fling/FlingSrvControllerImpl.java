
package tv.matchstick.server.fling;

import java.util.ArrayList;
import java.util.Iterator;

import tv.matchstick.server.common.checker.ObjEqualChecker;
import tv.matchstick.server.fling.bridge.IFlingSrvController;
import tv.matchstick.server.utils.FlingStatusCodes;
import tv.matchstick.server.utils.LOG;

final class FlingSrvControllerImpl implements IFlingSrvController {
    final FlingDeviceControllerHelper mFlingDeviceControllerHelper;
    final FlingMediaRouteProvider mFlingMediaRouteProvider;

    FlingSrvControllerImpl(FlingMediaRouteProvider provider, FlingDeviceControllerHelper helper)
    {
        super();
        mFlingMediaRouteProvider = provider;
        mFlingDeviceControllerHelper = helper;
    }

    public final void onConnected()
    {
        FlingMediaRouteProvider.getLogs_a()
                .d("FlingDeviceController.Listener.onConnected");

        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!mFlingDeviceControllerHelper.isEmpty())
                {
                    mFlingDeviceControllerHelper.c = false;
                    FlingMediaRouteProvider.publishRoutes(mFlingMediaRouteProvider);
                    Iterator iterator = (new ArrayList(mFlingDeviceControllerHelper.e)).iterator();
                    while (iterator.hasNext())
                        ((FlingRouteController) iterator.next()).startSession();
                }
            }

        });
    }

    public final void onDisconnected(final int statusCode)
    {
        FlingMediaRouteProvider.getLogs_a().d("FlingDeviceController.Listener.onDisconnected: %d", statusCode);

        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mFlingDeviceControllerHelper.isEmpty())
                    return;
                mFlingDeviceControllerHelper.c = false;
                mFlingDeviceControllerHelper.d = true;
                if (statusCode == 0)
                {
                    for (Iterator iterator = (new ArrayList(mFlingDeviceControllerHelper.e)).iterator(); iterator.hasNext(); ((FlingRouteController) iterator
                            .next()).g())
                        ;
                } else
                {
                    FlingMediaRouteProvider.getLogs_a().d("MRP trying to reconnect");
                    mFlingDeviceControllerHelper.c = true;
                    mFlingDeviceControllerHelper.mFlingDeviceController.connectDevice();
                }
                FlingMediaRouteProvider.publishRoutes(mFlingMediaRouteProvider);
            }

        });

    }

    public final void onApplicationConnected(final ApplicationMetadata applicationmetadata,
            final String s,
            final String sessionId, boolean flag)
    {
        FlingMediaRouteProvider.getLogs_a().d(
                "FlingDeviceController.Listener.onApplicationConnected: sessionId=%s", sessionId);

        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!mFlingDeviceControllerHelper.isEmpty())
                {
                    Iterator iterator = (new ArrayList(mFlingDeviceControllerHelper.e)).iterator();
                    boolean flag = false;
                    while (iterator.hasNext())
                    {
                        FlingRouteController awn1 = (FlingRouteController) iterator.next();
                        FlingMediaRouteProvider.getLogs_a().d(
                                "onApplicationConnected: sessionId=%s",sessionId);
                        FlingMediaRouteProvider.getLogs_a().d("mSession = %s", awn1.mMediaRouteSession);
                        if (awn1.mMediaRouteSession != null)
                            awn1.mMediaRouteSession.onApplicationConnected(applicationmetadata,
                                    sessionId);
                        boolean flag1;
                        if (!ObjEqualChecker.isEquals(s, awn1.d))
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
                        FlingMediaRouteProvider.publishRoutes(mFlingMediaRouteProvider);
                        return;
                    }
                }

            }
        });
    }

    public final void onVolumeChanged(final String s, final double d, boolean f)
    {
        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mFlingDeviceControllerHelper.isEmpty()) {
                    return;
                }

                boolean flag = false;
                Iterator iterator = (new ArrayList(mFlingDeviceControllerHelper.e)).iterator();
                while (iterator.hasNext()) {
                    FlingRouteController awn1 = (FlingRouteController) iterator.next();
                    // String s = a;
                    boolean flag1;
                    boolean flag2;
                    boolean flag3;
                    // double d;
                    LOG avu1;
                    Object aobj[];
                    if (awn1.mFlingDeviceController != null
                            && !ObjEqualChecker.isEquals(s, awn1.d))
                    {
                        awn1.d = s;
                        flag1 = true;
                    } else
                    {
                        flag1 = false;
                    }
                    flag2 = flag | flag1;
                    // d = b;
                    if (awn1.mFlingDeviceController == null) {
                        flag3 = false;
                        flag = flag3 | flag2;
                        continue;
                    }

                    FlingMediaRouteProvider.getLogs_a().d("onVolumeChanged to %f, was %f", d, awn1.c);
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

                FlingMediaRouteProvider.publishRoutes(mFlingMediaRouteProvider);
            }
        });
    }

    public final void onRequestCallback(String namespace, long requestId)
    {
    }

    public final void onRequestCallback(String namespace, long requestId, int result)
    {
    }

    public final void notifyOnMessageReceived(String namespace, String message)
    {
    }

    public final void onReceiveBinary(String namespace, byte message[])
    {
    }

    public final void onConnectedWithoutApp()
    {
        FlingMediaRouteProvider.getLogs_a().d(
                "FlingDeviceController.Listener.onConnectedWithoutApp");

        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!mFlingDeviceControllerHelper.isEmpty())
                {
                    mFlingDeviceControllerHelper.c = false;
                    mFlingDeviceControllerHelper.d = true;
                    Iterator iterator = (new ArrayList(mFlingDeviceControllerHelper.e)).iterator();
                    while (iterator.hasNext())
                    {
                        FlingRouteController awn1 = (FlingRouteController) iterator.next();
                        awn1.startSession();
                        awn1.onApplicationDisconnected(FlingStatusCodes.APPLICATION_NOT_RUNNING); // 2005
                    }
                }
            }

        });
    }

    public final void onApplicationConnectionFailed(final int statusCode)
    {
        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!mFlingDeviceControllerHelper.isEmpty())
                {
                    Iterator iterator = (new ArrayList(mFlingDeviceControllerHelper.e)).iterator();
                    while (iterator.hasNext())
                    {
                        FlingRouteController awn1 = (FlingRouteController) iterator.next();
                        FlingMediaRouteProvider.getLogs_a().d("onApplicationConnectionFailed: statusCode=%d", statusCode);
                        awn1.h = false;
                        awn1.sendPendingIntent(awn1.getSessionId(), 1);
                        awn1.disconnectCastMirror(true);
                        if (awn1.mMediaRouteSession != null)
                            awn1.mMediaRouteSession.onApplicationConnectionFailed(statusCode);
                    }
                }
            }

        });
    }

    public final void onConnectionFailed()
    {
        FlingMediaRouteProvider.getLogs_a().d("FlingDeviceController.Listener.onConnectionFailed: %d", 7);

        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!mFlingDeviceControllerHelper.isEmpty())
                {
                    mFlingDeviceControllerHelper.c = false;
                    mFlingDeviceControllerHelper.d = true;
                    Iterator iterator = (new ArrayList(mFlingDeviceControllerHelper.e)).iterator();
                    while (iterator.hasNext())
                        ((FlingRouteController) iterator.next()).g();
                }
            }

        });
    }

    public final void onRequestStatus(int i)
    {
    }

    public final void onInvalidRequest()
    {
    }

    public final void onApplicationDisconnected(final int statusCode)
    {
        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (!mFlingDeviceControllerHelper.isEmpty())
                {
                    Iterator iterator = (new ArrayList(mFlingDeviceControllerHelper.e)).iterator();
                    while (iterator.hasNext())
                        ((FlingRouteController) iterator.next())
                                .onApplicationDisconnected(statusCode);
                }
            }

        });
    }
}
