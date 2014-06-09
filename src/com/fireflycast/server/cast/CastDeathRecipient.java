
package com.fireflycast.server.cast;

import android.os.IBinder.DeathRecipient;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.SparseArray;

import com.fireflycast.server.cast.media.RouteController;

final class CastDeathRecipient implements DeathRecipient {
    public final Messenger mMessenger_a;
    public final int mVersion_b;
    public DiscoveryRequest mDiscoveryRequest_c;
    final MediaRouteProviderSrv mMediaRouteProviderSrv_d;
    private final SparseArray mRouteControllerList_e = new SparseArray();

    public CastDeathRecipient(MediaRouteProviderSrv routeProvider, Messenger messenger,
            int version)
    {
        super();
        mMediaRouteProviderSrv_d = routeProvider;
        mMessenger_a = messenger;
        mVersion_b = version;
    }

    public final boolean linkToDeath_a()
    {
        try
        {
            mMessenger_a.getBinder().linkToDeath(this, 0);
        } catch (RemoteException remoteexception)
        {
            binderDied();
            return false;
        }
        return true;
    }

    public final boolean releaseRouteController_a(int controllerId)
    {
        RouteController routeController = (RouteController) mRouteControllerList_e.get(controllerId);
        if (routeController != null)
        {
            mRouteControllerList_e.remove(controllerId);
            routeController.onRelease_a();
            return true;
        } else
        {
            return false;
        }
    }

    public final boolean isBinderEquals_a(Messenger messenger)
    {
        return mMessenger_a.getBinder() == messenger.getBinder();
    }

    public final boolean checkRouteController_a(String routeId, int controllerId)
    {
        if (mRouteControllerList_e.indexOfKey(controllerId) < 0)
        {
            RouteController routeController = MediaRouteProviderSrv.getMediaRouteProvider_a(
                    mMediaRouteProviderSrv_d).getRouteController_a(routeId);
            if (routeController != null)
            {
                mRouteControllerList_e.put(controllerId, routeController);
                return true;
            }
        }
        return false;
    }

    public final boolean setDiscoveryRequest_a(DiscoveryRequest request)
    {
        if (mDiscoveryRequest_c != request
                && (mDiscoveryRequest_c == null || !mDiscoveryRequest_c.equals(request)))
        {
            mDiscoveryRequest_c = request;
            return MediaRouteProviderSrv.setDiscoveryRequest_b(mMediaRouteProviderSrv_d);
        } else
        {
            return false;
        }
    }

    public final RouteController getRouteController_b(int i)
    {
        return (RouteController) mRouteControllerList_e.get(i);
    }

    public final void onBinderDied_b()
    {
        int size = mRouteControllerList_e.size();
        for (int j = 0; j < size; j++)
            ((RouteController) mRouteControllerList_e.valueAt(j)).onRelease_a();

        mRouteControllerList_e.clear();
        mMessenger_a.getBinder().unlinkToDeath(this, 0);
        setDiscoveryRequest_a(((DiscoveryRequest) (null)));
    }

    public final void binderDied()
    {
        MediaRouteProviderSrv.getHandler_c(mMediaRouteProviderSrv_d)
                .obtainMessage(1, mMessenger_a).sendToTarget();
    }

    public final String toString()
    {
        return MediaRouteProviderSrv.getClientConnectionInfo_a(mMessenger_a);
    }
}
