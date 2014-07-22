
package tv.matchstick.server.fling;

import tv.matchstick.server.fling.media.RouteController;
import android.os.IBinder.DeathRecipient;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.SparseArray;

final class FlingDeathRecipient implements DeathRecipient {
    public final Messenger mMessenger;
    public final int mVersion;
    public DiscoveryRequest mDiscoveryRequest;
    final MediaRouteProviderSrv mMediaRouteProviderSrv;
    private final SparseArray mRouteControllerList = new SparseArray();

    public FlingDeathRecipient(MediaRouteProviderSrv routeProvider, Messenger messenger,
            int version)
    {
        super();
        mMediaRouteProviderSrv = routeProvider;
        mMessenger = messenger;
        mVersion = version;
    }

    public final boolean linkToDeath()
    {
        try
        {
            mMessenger.getBinder().linkToDeath(this, 0);
        } catch (RemoteException remoteexception)
        {
            binderDied();
            return false;
        }
        return true;
    }

    public final boolean releaseRouteController(int controllerId)
    {
        RouteController routeController = (RouteController) mRouteControllerList.get(controllerId);
        if (routeController != null)
        {
            mRouteControllerList.remove(controllerId);
            routeController.onRelease();
            return true;
        } else
        {
            return false;
        }
    }

    public final boolean isBinderEquals(Messenger messenger)
    {
        return mMessenger.getBinder() == messenger.getBinder();
    }

    public final boolean checkRouteController(String routeId, int controllerId)
    {
        if (mRouteControllerList.indexOfKey(controllerId) < 0)
        {
            RouteController routeController = MediaRouteProviderSrv.getMediaRouteProvider(
                    mMediaRouteProviderSrv).getRouteController(routeId);
            if (routeController != null)
            {
                mRouteControllerList.put(controllerId, routeController);
                return true;
            }
        }
        return false;
    }

    public final boolean setDiscoveryRequest(DiscoveryRequest request)
    {
        if (mDiscoveryRequest != request
                && (mDiscoveryRequest == null || !mDiscoveryRequest.equals(request)))
        {
            mDiscoveryRequest = request;
            return MediaRouteProviderSrv.setDiscoveryRequest(mMediaRouteProviderSrv);
        } else
        {
            return false;
        }
    }

    public final RouteController getRouteController(int i)
    {
        return (RouteController) mRouteControllerList.get(i);
    }

    public final void onBinderDied()
    {
        int size = mRouteControllerList.size();
        for (int j = 0; j < size; j++)
            ((RouteController) mRouteControllerList.valueAt(j)).onRelease();

        mRouteControllerList.clear();
        mMessenger.getBinder().unlinkToDeath(this, 0);
        setDiscoveryRequest(((DiscoveryRequest) (null)));
    }

    public final void binderDied()
    {
        MediaRouteProviderSrv.getHandler(mMediaRouteProviderSrv)
                .obtainMessage(1, mMessenger).sendToTarget();
    }

    public final String toString()
    {
        return MediaRouteProviderSrv.getClientConnectionInfo(mMessenger);
    }
}
