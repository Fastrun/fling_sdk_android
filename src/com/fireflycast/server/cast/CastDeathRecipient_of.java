
package com.fireflycast.server.cast;

import android.os.IBinder.DeathRecipient;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.SparseArray;

import com.fireflycast.server.cast.media.RouteController_nz;

final class CastDeathRecipient_of implements DeathRecipient {
    public final Messenger mMessenger_a;
    public final int mVersion_b;
    public DiscoveryRequest_nu mDiscoveryRequest_c;
    final MediaRouteProviderSrv_od mMediaRouteProviderSrv_d;
    private final SparseArray mRouteControllerList_e = new SparseArray();

    public CastDeathRecipient_of(MediaRouteProviderSrv_od routeProvider, Messenger messenger,
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
        RouteController_nz routeController = (RouteController_nz) mRouteControllerList_e.get(controllerId);
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
            RouteController_nz routeController = MediaRouteProviderSrv_od.getMediaRouteProvider_a(
                    mMediaRouteProviderSrv_d).getRouteController_a(routeId);
            if (routeController != null)
            {
                mRouteControllerList_e.put(controllerId, routeController);
                return true;
            }
        }
        return false;
    }

    public final boolean setDiscoveryRequest_a(DiscoveryRequest_nu request)
    {
        if (mDiscoveryRequest_c != request
                && (mDiscoveryRequest_c == null || !mDiscoveryRequest_c.equals(request)))
        {
            mDiscoveryRequest_c = request;
            return MediaRouteProviderSrv_od.setDiscoveryRequest_b(mMediaRouteProviderSrv_d);
        } else
        {
            return false;
        }
    }

    public final RouteController_nz getRouteController_b(int i)
    {
        return (RouteController_nz) mRouteControllerList_e.get(i);
    }

    public final void onBinderDied_b()
    {
        int size = mRouteControllerList_e.size();
        for (int j = 0; j < size; j++)
            ((RouteController_nz) mRouteControllerList_e.valueAt(j)).onRelease_a();

        mRouteControllerList_e.clear();
        mMessenger_a.getBinder().unlinkToDeath(this, 0);
        setDiscoveryRequest_a(((DiscoveryRequest_nu) (null)));
    }

    public final void binderDied()
    {
        MediaRouteProviderSrv_od.getHandler_c(mMediaRouteProviderSrv_d)
                .obtainMessage(1, mMessenger_a).sendToTarget();
    }

    public final String toString()
    {
        return MediaRouteProviderSrv_od.getClientConnectionInfo_a(mMessenger_a);
    }
}
