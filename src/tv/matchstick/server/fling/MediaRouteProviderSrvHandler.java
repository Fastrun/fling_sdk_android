
package tv.matchstick.server.fling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MediaRouteProviderSrvHandler extends Handler {

    private final WeakReference mRouteProviderRef;

    public MediaRouteProviderSrvHandler(MediaRouteProviderSrv routeProvider)
    {
        mRouteProviderRef = new WeakReference(routeProvider);
    }

    public final void handleMessage(Message message)
    {
        android.os.Messenger messenger = message.replyTo;
        if (!ReplyMessengerHelper.isValid(messenger)) {
            if (MediaRouteProviderSrv.isDebugable()) {
                Log.d("MediaRouteProviderSrv", "Ignoring message without valid reply messenger.");
            }
            return;
        }
        boolean flag = false;
        int what = message.what;
        int requestId = message.arg1;
        int arg = message.arg2;
        Object obj = message.obj;
        Bundle data = message.peekData();
        MediaRouteProviderSrv routeProvider = (MediaRouteProviderSrv) mRouteProviderRef
                .get();

        if (routeProvider != null) {
            switch (message.what) {
                case 1: // CLIENT_MSG_REGISTER
                    flag = MediaRouteProviderSrv.register(routeProvider, messenger, requestId,
                            arg);
                    break;
                case 2: // CLIENT_MSG_UNREGISTER
                    flag = MediaRouteProviderSrv.unRegister(routeProvider, messenger,
                            requestId);
                    break;
                case 3: // CLIENT_MSG_CREATE_ROUTE_CONTROLLER
                    String routeId = data.getString("routeId");
                    if (routeId == null) {
                        flag = false;
                    } else {
                        flag = MediaRouteProviderSrv.createRouteController(routeProvider,
                                messenger, requestId, arg, routeId);
                    }
                    break;
                case 4: // CLIENT_MSG_RELEASE_ROUTE_CONTROLLER
                    flag = MediaRouteProviderSrv.releaseRouteController(routeProvider,
                            messenger,
                            requestId, arg);
                    break;
                case 5: // CLIENT_MSG_SELECT_ROUTE
                    flag = MediaRouteProviderSrv.selectRoute(routeProvider, messenger,
                            requestId,
                            arg);
                    break;
                case 6: // CLIENT_MSG_UNSELECT_ROUTE
                    flag = MediaRouteProviderSrv.unselectRoute(routeProvider, messenger,
                            requestId,
                            arg);
                    break;
                case 7: // CLIENT_MSG_SET_ROUTE_VOLUME
                    int volume = data.getInt("volume", -1);
                    if (volume < 0) {
                        flag = false;
                    } else {
                        flag = MediaRouteProviderSrv.setRouteVolume(routeProvider, messenger,
                                requestId, arg, volume);
                    }
                    break;
                case 8: // CLIENT_MSG_UPDATE_ROUTE_VOLUME
                    volume = data.getInt("volume", 0);
                    if (volume == 0) {
                        flag = false;
                    } else {
                        flag = MediaRouteProviderSrv.updateRouteVolume(routeProvider,
                                messenger,
                                requestId, arg, volume);
                    }
                    break;
                case 9: // CLIENT_MSG_ROUTE_CONTROL_REQUEST
                    if (!(obj instanceof Intent)) {
                        flag = false;
                    } else {
                        flag = MediaRouteProviderSrv.routeControlRequest(routeProvider,
                                messenger,
                                requestId, arg,
                                (Intent) obj);
                    }

                    break;
                case 10: // CLIENT_MSG_SET_DISCOVERY_REQUEST
                    if (obj != null && !(obj instanceof Bundle)) {
                        flag = false;
                    } else {
                        Bundle bundle1 = (Bundle) obj;
                        DiscoveryRequest request;
                        if (bundle1 != null)
                            request = new DiscoveryRequest(bundle1);
                        else
                            request = null;
                        if (request == null || !request.isValid())
                            request = null;
                        flag = MediaRouteProviderSrv.setDiscoveryRequest(routeProvider,
                                messenger,
                                requestId, request);
                    }
                    break;
            }
        } else {
            flag = false;
        }

        if (!flag)
        {
            if (MediaRouteProviderSrv.isDebugable())
                Log.d("MediaRouteProviderSrv",
                        (new StringBuilder())
                                .append(MediaRouteProviderSrv
                                        .getClientConnectionInfo(messenger))
                                .append(": Message failed, what=").append(what)
                                .append(", requestId=")
                                .append(requestId).append(", arg=").append(arg).append(", obj=")
                                .append(obj)
                                .append(", data=").append(data).toString());
            MediaRouteProviderSrv.sendFailureReplyMsg(messenger, requestId);
        }
        return;
    }
}
