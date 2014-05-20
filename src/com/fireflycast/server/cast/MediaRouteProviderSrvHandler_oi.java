
package com.fireflycast.server.cast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.ref.WeakReference;

public class MediaRouteProviderSrvHandler_oi extends Handler {

    private final WeakReference mRouteProviderRef_a;

    public MediaRouteProviderSrvHandler_oi(MediaRouteProviderSrv_od routeProvider)
    {
        mRouteProviderRef_a = new WeakReference(routeProvider);
    }

    public final void handleMessage(Message message)
    {
        android.os.Messenger messenger = message.replyTo;
        if (!ReplyMessengerHelper_oc.isValid_a(messenger)) {
            if (MediaRouteProviderSrv_od.isDebugable_b()) {
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
        MediaRouteProviderSrv_od routeProvider = (MediaRouteProviderSrv_od) mRouteProviderRef_a
                .get();

        if (routeProvider != null) {
            switch (message.what) {
                case 1: // CLIENT_MSG_REGISTER
                    flag = MediaRouteProviderSrv_od.register_a(routeProvider, messenger, requestId,
                            arg);
                    break;
                case 2: // CLIENT_MSG_UNREGISTER
                    flag = MediaRouteProviderSrv_od.unRegister_a(routeProvider, messenger,
                            requestId);
                    break;
                case 3: // CLIENT_MSG_CREATE_ROUTE_CONTROLLER
                    String routeId = data.getString("routeId");
                    if (routeId == null) {
                        flag = false;
                    } else {
                        flag = MediaRouteProviderSrv_od.createRouteController_a(routeProvider,
                                messenger, requestId, arg, routeId);
                    }
                    break;
                case 4: // CLIENT_MSG_RELEASE_ROUTE_CONTROLLER
                    flag = MediaRouteProviderSrv_od.releaseRouteController_b(routeProvider,
                            messenger,
                            requestId, arg);
                    break;
                case 5: // CLIENT_MSG_SELECT_ROUTE
                    flag = MediaRouteProviderSrv_od.selectRoute_c(routeProvider, messenger,
                            requestId,
                            arg);
                    break;
                case 6: // CLIENT_MSG_UNSELECT_ROUTE
                    flag = MediaRouteProviderSrv_od.unselectRoute_d(routeProvider, messenger,
                            requestId,
                            arg);
                    break;
                case 7: // CLIENT_MSG_SET_ROUTE_VOLUME
                    int volume = data.getInt("volume", -1);
                    if (volume < 0) {
                        flag = false;
                    } else {
                        flag = MediaRouteProviderSrv_od.setRouteVolume_a(routeProvider, messenger,
                                requestId, arg, volume);
                    }
                    break;
                case 8: // CLIENT_MSG_UPDATE_ROUTE_VOLUME
                    volume = data.getInt("volume", 0);
                    if (volume == 0) {
                        flag = false;
                    } else {
                        flag = MediaRouteProviderSrv_od.updateRouteVolume_b(routeProvider,
                                messenger,
                                requestId, arg, volume);
                    }
                    break;
                case 9: // CLIENT_MSG_ROUTE_CONTROL_REQUEST
                    if (!(obj instanceof Intent)) {
                        flag = false;
                    } else {
                        flag = MediaRouteProviderSrv_od.routeControlRequest_a(routeProvider,
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
                        DiscoveryRequest_nu request;
                        if (bundle1 != null)
                            request = new DiscoveryRequest_nu(bundle1);
                        else
                            request = null;
                        if (request == null || !request.isValid_c())
                            request = null;
                        flag = MediaRouteProviderSrv_od.setDiscoveryRequest_a(routeProvider,
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
            if (MediaRouteProviderSrv_od.isDebugable_b())
                Log.d("MediaRouteProviderSrv",
                        (new StringBuilder())
                                .append(MediaRouteProviderSrv_od
                                        .getClientConnectionInfo_a(messenger))
                                .append(": Message failed, what=").append(what)
                                .append(", requestId=")
                                .append(requestId).append(", arg=").append(arg).append(", obj=")
                                .append(obj)
                                .append(", data=").append(data).toString());
            MediaRouteProviderSrv_od.sendFailureReplyMsg_a(messenger, requestId);
        }
        return;
    }
}
