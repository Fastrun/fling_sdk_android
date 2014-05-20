
package com.fireflycast.server.cast;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.fireflycast.server.cast.media.RouteController_nz;
import com.fireflycast.server.cast.media.RouteCtrlRequestCallback_om;
import com.fireflycast.server.common.checker.MainThreadChecker_ol;

import java.util.ArrayList;

public abstract class MediaRouteProviderSrv_od extends IntentService
{
    private static final boolean DEBUG = Log.isLoggable("MediaRouteProviderSrv", Log.DEBUG);
    private final ArrayList mCastDeathRecipientList_b = new ArrayList();
    private final MediaRouteProviderSrvHandler_oi mMessageTargetHandler_c = new MediaRouteProviderSrvHandler_oi(
            this);
    private final Messenger mMessenger_d;

    // private final C_og e = new C_og(this, (byte)0);
    private final Handler mBinderDiedHandler_e = new Handler() {
        public final void handleMessage(Message message)
        {
            switch (message.what)
            {
                default:
                    return;

                case 1: // '\001'
                    onBinderDied_b(MediaRouteProviderSrv_od.this,
                            (Messenger) message.obj);
                    break;
            }
        }
    };

    // private final C_oh f = new C_oh(this, (byte) 0);
    private final DescriptorChangedListener_nw mDescriptorChangedListener_f = new DescriptorChangedListener_nw() {
        public final void onDescriptorChanged_a(MediaRouteProviderDescriptor_oa descriptor)
        {
            sendDescriptorChangeEvent_a(MediaRouteProviderSrv_od.this, descriptor);
        }
    };

    private MediaRouteProvider_nv mMediaRouteProvider_g;
    private DiscoveryRequest_nu mDiscoveryRequest_h;

    public MediaRouteProviderSrv_od()
    {
        super("MediaRouteProviderSrv");
        mMessenger_d = new Messenger(mMessageTargetHandler_c);
    }

    static int getCastDeathRecipitentListIndex_a(MediaRouteProviderSrv_od mediaRouteProvider,
            Messenger messenger)
    {
        return mediaRouteProvider.getCastDeathRecipitentListIndex_c(messenger);
    }

    static String getClientConnectionInfo_a(Messenger messenger)
    {
        return getClientConnectionInfo_d(messenger);
    }

    static MediaRouteProvider_nv getMediaRouteProvider_a(MediaRouteProviderSrv_od routePrivider)
    {
        return routePrivider.mMediaRouteProvider_g;
    }

    static void sendFailureReplyMsg_a(Messenger messenger, int requestId)
    {
        if (requestId != 0)
            sendReplyMsg_a(messenger, 0, requestId, 0, null, null); // 0:SERVICE_MSG_GENERIC_FAILURE
    }

    private static void sendReplyMsg_a(Messenger messenger, int what, int requestId, int arg,
            Object obj, Bundle data)
    {
        Message message = Message.obtain();
        message.what = what;
        message.arg1 = requestId;
        message.arg2 = arg;
        message.obj = obj;
        message.setData(data);
        try
        {
            messenger.send(message);
            return;
        } catch (DeadObjectException deadobjectexception)
        {
            return;
        } catch (RemoteException remoteexception)
        {
            Log.e("MediaRouteProviderSrv", (new StringBuilder("Could not send message to "))
                    .append(getClientConnectionInfo_d(messenger)).toString(), remoteexception);
        }
    }

    static void sendReplyMsg_a(Messenger messenger, int what, int requestId, Object obj, Bundle data)
    {
        sendReplyMsg_a(messenger, what, requestId, 0, obj, data);
    }

    static void sendDescriptorChangeEvent_a(MediaRouteProviderSrv_od routeProvider,
            MediaRouteProviderDescriptor_oa descriptor)
    {
        Bundle bundle;
        int size;
        if (descriptor != null)
            bundle = descriptor.mRoutes_a;
        else
            bundle = null;
        size = routeProvider.mCastDeathRecipientList_b.size();
        for (int index = 0; index < size; index++)
        {
            CastDeathRecipient_of deathRecipient = (CastDeathRecipient_of) routeProvider.mCastDeathRecipientList_b
                    .get(index);
            sendReplyMsg_a(deathRecipient.mMessenger_a, 5, 0, 0, bundle, null); // 5:SERVICE_MSG_DESCRIPTOR_CHANGED
            if (DEBUG)
                Log.d("MediaRouteProviderSrv",
                        (new StringBuilder()).append(deathRecipient)
                                .append(": Sent descriptor change event, descriptor=")
                                .append(descriptor)
                                .toString());
        }

    }

    private boolean register_a(Messenger messenger, int requestId, int version)
    {
        if (version > 0 && getCastDeathRecipitentListIndex_c(messenger) < 0)
        {
            CastDeathRecipient_of deathRecipient = new CastDeathRecipient_of(this, messenger,
                    version);
            if (deathRecipient.linkToDeath_a())
            {
                mCastDeathRecipientList_b.add(deathRecipient);
                if (DEBUG)
                    Log.d("MediaRouteProviderSrv",
                            (new StringBuilder()).append(deathRecipient)
                                    .append(": Registered, version=")
                                    .append(version).toString());
                if (requestId != 0)
                {
                    MediaRouteProviderDescriptor_oa descriptor = mMediaRouteProvider_g.mMediaRouteProviderDescriptor_g;
                    Bundle bundle;
                    if (descriptor != null)
                        bundle = descriptor.mRoutes_a;
                    else
                        bundle = null;
                    sendReplyMsg_a(messenger, 2, requestId, 1, bundle, null); // 2:SERVICE_MSG_REGISTERED
                }
                return true;
            }
        }
        return false;
    }

    static boolean unRegister_a(MediaRouteProviderSrv_od routeProvider, Messenger messenger,
            int requestId)
    {
        int j = routeProvider.getCastDeathRecipitentListIndex_c(messenger);
        if (j >= 0)
        {
            CastDeathRecipient_of deathRecipient = (CastDeathRecipient_of) routeProvider.mCastDeathRecipientList_b
                    .remove(j);
            if (DEBUG)
                Log.d("MediaRouteProviderSrv",
                        (new StringBuilder()).append(deathRecipient).append(": Unregistered")
                                .toString());
            deathRecipient.onBinderDied_b();
            sendReplyMsg_b(messenger, requestId);
            return true;
        } else
        {
            return false;
        }
    }

    static boolean register_a(MediaRouteProviderSrv_od routeProvider, Messenger messenger,
            int requestId, int arg)
    {
        return routeProvider.register_a(messenger, requestId, arg);
    }

    static boolean setRouteVolume_a(MediaRouteProviderSrv_od routeProvider, Messenger messenger,
            int requestId, int controllerId, int volume)
    {
        CastDeathRecipient_of deathRecipient = routeProvider.getCastDeathRecipient_b(messenger);
        if (deathRecipient != null)
        {
            RouteController_nz routeController = deathRecipient.getRouteController_b(controllerId);
            if (routeController != null)
            {
                routeController.onSetVolume_a(volume);
                if (DEBUG)
                    Log.d("MediaRouteProviderSrv",
                            (new StringBuilder()).append(deathRecipient)
                                    .append(": Route volume changed, controllerId=")
                                    .append(controllerId).append(", volume=").append(volume)
                                    .toString());
                sendReplyMsg_b(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    static boolean routeControlRequest_a(final MediaRouteProviderSrv_od mediaRouteProvider,
            final Messenger messenger,
            final int requestId, final int controllerId, final Intent intent)
    {
        final CastDeathRecipient_of deathRecipient = mediaRouteProvider
                .getCastDeathRecipient_b(messenger);
        if (deathRecipient != null)
        {
            RouteController_nz routeController = deathRecipient.getRouteController_b(controllerId);
            if (routeController != null)
            {
                RouteCtrlRequestCallback_om request = null;
                if (requestId != 0) {
                    // oe1 = new MediaRouteProviderSrv_oe(od1, of1,
                    // controllerId, intent, messenger,
                    // requestId);

                    request = new RouteCtrlRequestCallback_om() {
                        public final void onResult_a(Bundle bundle)
                        {
                            if (isDebugable_b())
                                Log.d("MediaRouteProviderSrv",
                                        (new StringBuilder())
                                                .append(deathRecipient)
                                                .append(": Route control request succeeded, controllerId=")
                                                .append(controllerId)
                                                .append(", intent=").append(intent)
                                                .append(", data=").append(bundle)
                                                .toString());
                            if (mediaRouteProvider.getCastDeathRecipitentListIndex_c(messenger) >= 0)
                                sendReplyMsg_a(messenger, 3, requestId, bundle,
                                        null); // 3:SERVICE_MSG_CONTROL_REQUEST_SUCCEEDED
                        }

                        public final void onError_a(String error, Bundle bundle)
                        {
                            if (isDebugable_b())
                                Log.d("MediaRouteProviderSrv",
                                        (new StringBuilder())
                                                .append(deathRecipient)
                                                .append(": Route control request failed, controllerId=")
                                                .append(controllerId)
                                                .append(", intent=").append(intent)
                                                .append(", error=").append(error)
                                                .append(", data=").append(bundle).toString());
                            if (mediaRouteProvider.getCastDeathRecipitentListIndex_c(messenger) >= 0)
                            {
                                if (error == null) {
                                    sendReplyMsg_a(messenger, 4, requestId, bundle,
                                            null); // 4:SERVICE_MSG_CONTROL_REQUEST_FAILED
                                } else {
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("error", error);
                                    sendReplyMsg_a(messenger, 4, requestId, bundle,
                                            bundle1);
                                }
                            }
                            return;
                        }
                    };
                }
                if (routeController.onControlRequest_a(intent, request))
                {
                    if (DEBUG)
                        Log.d("MediaRouteProviderSrv",
                                (new StringBuilder()).append(deathRecipient)
                                        .append(": Route control request delivered, controllerId=")
                                        .append(controllerId).append(", intent=").append(intent)
                                        .toString());
                    return true;
                }
            }
        }
        return false;
    }

    static boolean createRouteController_a(MediaRouteProviderSrv_od routeProvider,
            Messenger messenger,
            int requestId, int controllerId, String routeId)
    {
        CastDeathRecipient_of deathRecipient = routeProvider.getCastDeathRecipient_b(messenger);
        if (deathRecipient != null && deathRecipient.checkRouteController_a(routeId, controllerId))
        {
            if (DEBUG)
                Log.d("MediaRouteProviderSrv",
                        (new StringBuilder()).append(deathRecipient)
                                .append(": Route controller created, controllerId=")
                                .append(controllerId).append(", routeId=").append(routeId)
                                .toString());
            sendReplyMsg_b(messenger, requestId);
            return true;
        } else
        {
            return false;
        }
    }

    static boolean setDiscoveryRequest_a(MediaRouteProviderSrv_od routeProvider,
            Messenger messenger,
            int requestId, DiscoveryRequest_nu request)
    {
        CastDeathRecipient_of deathRecipient = routeProvider.getCastDeathRecipient_b(messenger);
        if (deathRecipient != null)
        {
            boolean actuallyChanged = deathRecipient.setDiscoveryRequest_a(request);
            if (DEBUG)
                Log.d("MediaRouteProviderSrv",
                        (new StringBuilder()).append(deathRecipient)
                                .append(": Set discovery request, request=").append(request)
                                .append(", actuallyChanged=").append(actuallyChanged)
                                .append(", compositeDiscoveryRequest=")
                                .append(routeProvider.mDiscoveryRequest_h).toString());
            sendReplyMsg_b(messenger, requestId);
            return true;
        } else
        {
            return false;
        }
    }

    private CastDeathRecipient_of getCastDeathRecipient_b(Messenger messenger)
    {
        int index = getCastDeathRecipitentListIndex_c(messenger);
        if (index >= 0)
            return (CastDeathRecipient_of) mCastDeathRecipientList_b.get(index);
        else
            return null;
    }

    private static void sendReplyMsg_b(Messenger messenger, int requestId)
    {
        if (requestId != 0)
            sendReplyMsg_a(messenger, 1, requestId, 0, null, null); // 1:SERVICE_MSG_GENERIC_SUCCESS
    }

    static void onBinderDied_b(MediaRouteProviderSrv_od routeProvider, Messenger messenger)
    {
        int index = routeProvider.getCastDeathRecipitentListIndex_c(messenger);
        if (index >= 0)
        {
            CastDeathRecipient_of deathRecipient = (CastDeathRecipient_of) routeProvider.mCastDeathRecipientList_b
                    .remove(index);
            if (DEBUG)
                Log.d("MediaRouteProviderSrv",
                        (new StringBuilder()).append(deathRecipient).append(": Binder died")
                                .toString());
            deathRecipient.onBinderDied_b();
        }
    }

    static boolean isDebugable_b()
    {
        return DEBUG;
    }

    static boolean setDiscoveryRequest_b(MediaRouteProviderSrv_od mediaRouteProvider)
    {
        Object obj = null;
        int size = mediaRouteProvider.mCastDeathRecipientList_b.size();
        int j = 0;
        boolean flag = false;
        DiscoveryRequest_nu request = null;

        Bundle bundle;
        MediaRouteSelector_oj oj1;
        MediaRouteSelector_oj selector;
        while (j < size)
        {
            DiscoveryRequest_nu discoveryRequest = ((CastDeathRecipient_of) mediaRouteProvider.mCastDeathRecipientList_b
                    .get(j)).mDiscoveryRequest_c;
            boolean flag1;
            CategoriesData_ok ok1;
            DiscoveryRequest_nu nu3;
            if (discoveryRequest != null
                    && (!discoveryRequest.getSelector_a().isEmpty_b() || discoveryRequest
                            .isActiveScan_b()))
            {
                flag1 = flag | discoveryRequest.isActiveScan_b();
                if (request == null)
                {
                    ok1 = (CategoriesData_ok) obj;
                    nu3 = discoveryRequest;
                } else
                {

                    if (obj == null)
                        ok1 = new CategoriesData_ok(request.getSelector_a());
                    else
                        ok1 = (CategoriesData_ok) obj;
                    selector = discoveryRequest.getSelector_a();
                    if (selector == null)
                        throw new IllegalArgumentException("selector must not be null");
                    ok1.addCategoryList_a(selector.getControlCategories_a());
                    nu3 = request;
                }
            } else
            {
                flag1 = flag;
                ok1 = (CategoriesData_ok) obj;
                nu3 = request;
            }
            j++;
            request = nu3;
            obj = ok1;
            flag = flag1;
        }
        if (obj != null)
        {
            if (((CategoriesData_ok) (obj)).mControlCategories_a == null)
            {
                oj1 = MediaRouteSelector_oj.EMPTY;
            } else
            {
                bundle = new Bundle();
                bundle.putStringArrayList("controlCategories",
                        ((CategoriesData_ok) (obj)).mControlCategories_a);
                oj1 = new MediaRouteSelector_oj(bundle,
                        ((CategoriesData_ok) (obj)).mControlCategories_a,
                        (byte) 0);
            }
            request = new DiscoveryRequest_nu(oj1, flag);
        }
        if (mediaRouteProvider.mDiscoveryRequest_h == request
                || mediaRouteProvider.mDiscoveryRequest_h != null
                && mediaRouteProvider.mDiscoveryRequest_h.equals(request)) {
            return false;
        }

        mediaRouteProvider.mDiscoveryRequest_h = request;
        // nv1 = od1.g;
        MainThreadChecker_ol.isOnAppMainThread_a();
        if (mediaRouteProvider.mMediaRouteProvider_g.mDiscoveryRequest_e != request
                && (mediaRouteProvider.mMediaRouteProvider_g.mDiscoveryRequest_e == null || !mediaRouteProvider.mMediaRouteProvider_g.mDiscoveryRequest_e
                        .equals(request))) {
            mediaRouteProvider.mMediaRouteProvider_g.mDiscoveryRequest_e = request;
            if (!mediaRouteProvider.mMediaRouteProvider_g.mPendingDiscoveryRequestChange_f)
            {
                mediaRouteProvider.mMediaRouteProvider_g.mPendingDiscoveryRequestChange_f = true;
                mediaRouteProvider.mMediaRouteProvider_g.mHandler_c.sendEmptyMessage(2); // device
                                                                                         // discovery
                                                                                         // request
            }
        }

        return true;
    }

    static boolean releaseRouteController_b(MediaRouteProviderSrv_od routeProvider,
            Messenger messenger,
            int requestId, int controllerId)
    {
        CastDeathRecipient_of deathRecipient = routeProvider.getCastDeathRecipient_b(messenger);
        if (deathRecipient != null && deathRecipient.releaseRouteController_a(controllerId))
        {
            if (DEBUG)
                Log.d("MediaRouteProviderSrv",
                        (new StringBuilder()).append(deathRecipient)
                                .append(": Route controller released, controllerId=")
                                .append(controllerId).toString());
            sendReplyMsg_b(messenger, requestId);
            return true;
        } else
        {
            return false;
        }
    }

    static boolean updateRouteVolume_b(MediaRouteProviderSrv_od routeProvider, Messenger messenger,
            int requestId, int controllerId, int delta)
    {
        CastDeathRecipient_of deathRecipient = routeProvider.getCastDeathRecipient_b(messenger);
        if (deathRecipient != null)
        {
            RouteController_nz routeController = deathRecipient.getRouteController_b(controllerId);
            if (routeController != null)
            {
                routeController.onUpdateVolume_b(delta);
                if (DEBUG)
                    Log.d("MediaRouteProviderSrv",
                            (new StringBuilder()).append(deathRecipient)
                                    .append(": Route volume updated, controllerId=")
                                    .append(controllerId).append(", delta=").append(delta)
                                    .toString());
                sendReplyMsg_b(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    private int getCastDeathRecipitentListIndex_c(Messenger messenger)
    {
        int size = mCastDeathRecipientList_b.size();
        for (int j = 0; j < size; j++)
            if (((CastDeathRecipient_of) mCastDeathRecipientList_b.get(j))
                    .isBinderEquals_a(messenger))
                return j;

        return -1;
    }

    static Handler getHandler_c(MediaRouteProviderSrv_od routeProvider)
    {
        return routeProvider.mBinderDiedHandler_e;
    }

    static boolean selectRoute_c(MediaRouteProviderSrv_od mediaRouteProvider, Messenger messenger,
            int requestId, int controllerId)
    {
        CastDeathRecipient_of deathRecipient = mediaRouteProvider
                .getCastDeathRecipient_b(messenger);
        if (deathRecipient != null)
        {
            RouteController_nz routeController = deathRecipient.getRouteController_b(controllerId);
            if (routeController != null)
            {
                routeController.onSelect_b();
                if (DEBUG)
                    Log.d("MediaRouteProviderSrv",
                            (new StringBuilder()).append(deathRecipient)
                                    .append(": Route selected, controllerId=").append(controllerId)
                                    .toString());
                sendReplyMsg_b(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    private static String getClientConnectionInfo_d(Messenger messenger)
    {
        return (new StringBuilder("Client connection ")).append(messenger.getBinder().toString())
                .toString();
    }

    static boolean unselectRoute_d(MediaRouteProviderSrv_od mediaRouteProvider,
            Messenger messenger,
            int requestId, int controllerId)
    {
        CastDeathRecipient_of deathRecipient = mediaRouteProvider
                .getCastDeathRecipient_b(messenger);
        if (deathRecipient != null)
        {
            RouteController_nz routeController = deathRecipient.getRouteController_b(controllerId);
            if (routeController != null)
            {
                routeController.onUnselect_c();
                if (DEBUG)
                    Log.d("MediaRouteProviderSrv",
                            (new StringBuilder()).append(deathRecipient)
                                    .append(": Route unselected, controllerId=")
                                    .append(controllerId).toString());
                sendReplyMsg_b(messenger, requestId);
                return true;
            }
        }
        return false;
    }

    public abstract MediaRouteProvider_nv getInstance_a();

    public IBinder onBind(Intent intent)
    {
        if (intent.getAction().equals("android.media.MediaRouteProviderService"))
        {
            if (mMediaRouteProvider_g == null)
            {
                MediaRouteProvider_nv mediaRouteProvider = getInstance_a();
                if (mediaRouteProvider != null)
                {
                    String packageName = mediaRouteProvider.mProviderMetadata_b.mComponentName_a
                            .getPackageName();
                    if (!packageName.equals(getPackageName()))
                        throw new IllegalStateException(
                                (new StringBuilder(
                                        "onCreateMediaRouteProvider() returned a provider whose package name does not match the package name of the service.  A media route provider service can only export its own media route providers.  Provider package name: "))
                                        .append(packageName).append(".  Service package name: ")
                                        .append(getPackageName()).append(".").toString());
                    mMediaRouteProvider_g = mediaRouteProvider;
                    // C_oh oh1 = f;
                    MainThreadChecker_ol.isOnAppMainThread_a();
                    mMediaRouteProvider_g.mDescriptorChangedListener_d = mDescriptorChangedListener_f;
                }
            }
            if (mMediaRouteProvider_g != null)
                return mMessenger_d.getBinder();
        }
        return null;
    }

}
