
package com.fireflycast.server.cast;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.cast.mdns.DeviceScanner;
import com.fireflycast.server.cast.mdns.IDeviceScanListener;
import com.fireflycast.server.cast.mdns.MdnsDeviceScanner;
import com.fireflycast.server.cast.mdns.MediaRouteDescriptorPrivateData;
import com.fireflycast.server.cast.media.RouteController;
import com.fireflycast.server.cast.service.CastDeviceService;
import com.fireflycast.server.cast_mirroring.IMirrorDeviceHelper;
import com.fireflycast.server.common.checker.MainThreadChecker;
import com.fireflycast.server.utils.C_bcx;
import com.fireflycast.server.utils.Logs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CastMediaRouteProvider extends MediaRouteProvider {
    public static final C_bcx i = C_bcx.a("gms:cast:media:generic_player_app_id", "CC1AD845");
    public static final C_bcx j = C_bcx.a("gms:cast:media:use_tdls", true);
    private static final Logs mLogs_k = new Logs("CastMediaRouteProvider");
    private static CastMediaRouteProvider mInstance_l;
    private static final String mPlayActions_m[] = {
            "android.media.intent.action.PAUSE", "android.media.intent.action.RESUME",
            "android.media.intent.action.STOP", "android.media.intent.action.SEEK",
            "android.media.intent.action.GET_STATUS", "android.media.intent.action.START_SESSION",
            "android.media.intent.action.GET_SESSION_STATUS",
            "android.media.intent.action.END_SESSION"
    };
    private static final DiscoveryCriteria mDiscoveryCriteria_w;
    private final DeviceScanner mMdnsDeviceScanner_n;

    // private final IDeviceScanListener_auy mDeviceScannerListener_o = new
    // CastDeviceScanListener_awd(
    // this);
    private final IDeviceScanListener mDeviceScannerListener_o = new IDeviceScanListener() {

        @Override
        public void onAllDevicesOffline_a() {
            // TODO Auto-generated method stub
            mLogs_k.d("DeviceScanner.Listener#onAllDevicesOffline",
                    new Object[0]);
            for (Iterator iterator = mDiscoveryCriteriaMap_p.entrySet().iterator(); iterator
                    .hasNext();)
            {
                DiscoveryCriteriaHelper awv1 = (DiscoveryCriteriaHelper) ((java.util.Map.Entry) iterator
                        .next()).getValue();
                if (awv1 != null)
                {
                    CastDevice castdevice = awv1.mCastDevice_a;
                    CastDeviceControllerHelper awu1 = (CastDeviceControllerHelper) q
                            .get(castdevice.getDeviceId());
                    if (awu1 != null)
                    {
                        awu1.b = false;
                        mLogs_k.d("device %s is in use; not removing route",
                                new Object[] {
                                    castdevice
                                });
                    } else
                    {
                        if (r != null) {
                            r.b(castdevice);
                        }
                        iterator.remove();
                    }
                } else
                {
                    iterator.remove();
                }
            }

            publishRoutes_b();
        }

        @Override
        public void onDeviceOnline_a(CastDevice castdevice) {
            // TODO Auto-generated method stub
            mLogs_k.d("DeviceScanner.Listener#onDeviceOnline :%s",
                    new Object[] {
                        castdevice
                    });
            CastDeviceControllerHelper awu1 = (CastDeviceControllerHelper) q.get(castdevice
                    .getDeviceId());
            if (awu1 != null) {
                awu1.b = true;
            }
            mCastDeviceFilter_t.connectOrAcceptDevice_b(castdevice);
        }

        @Override
        public void onDeviceOffline_b(CastDevice castdevice) {
            // TODO Auto-generated method stub
            mLogs_k.d("DeviceScanner.Listener#onDeviceOffline :%s",
                    new Object[] {
                        castdevice
                    });
            CastDeviceControllerHelper awu1 = (CastDeviceControllerHelper) q.get(castdevice
                    .getDeviceId());
            if (awu1 != null)
            {
                awu1.b = false;
            } else
            {
                b(castdevice);
                publishRoutes_b();
            }
        }

    };

    private final Map mDiscoveryCriteriaMap_p = new HashMap();
    private final Map q = new HashMap();
    private IMirrorDeviceHelper r;
    private boolean s;
    private final DeviceFilter mCastDeviceFilter_t;
    private final Set mDiscoveryCriteriaHashSet_u = new HashSet();
    private final List mIntentFilterList_v = getAllIntentFilters_d();
    private final Map x = new HashMap();

    private static final String[] mCastMimeTypes = {
            "image/jpeg",
            "image/pjpeg",
            "image/jpg",
            "image/webp",
            "image/png",
            "image/gif",
            "image/bmp",
            "image/vnd.microsoft.icon",
            "image/x-icon",
            "image/x-xbitmap",
            "audio/wav",
            "audio/x-wav",
            "audio/mp3",
            "audio/x-mp3",
            "audio/x-m4a",
            "audio/mpeg",
            "audio/webm",
            "video/mp4",
            "video/x-m4v",
            "video/mp2t",
            "video/webm"

    };

    private CastMediaRouteProvider(Context context) {
        super(context);
        x.put(Integer.valueOf(1), "Request failed");
        x.put(Integer.valueOf(2), "Failed to start a session");
        x.put(Integer.valueOf(2), "Unknown or invalid session ID");
        x.put(Integer.valueOf(3), "Disconnected from Cast Device but trying to reconnect");
        
        /*
        mCastDeviceFilter_t = new CastDeviceFilter_awc(this, context, mDiscoveryCriteriaHashSet_u,
                "gms_cast_mrp");
        */
        mCastDeviceFilter_t = new DeviceFilter(context, mDiscoveryCriteriaHashSet_u, "gms_cast_mrp"){

            @Override
            protected void setDeviceOffline_a(CastDevice castdevice) {
                // TODO Auto-generated method stub
                mMdnsDeviceScanner_n.setDeviceOffline_a(castdevice.getDeviceId());
            }

            @Override
            protected void onDeviceAccepted_a(CastDevice castdevice, Set set) {
                // TODO Auto-generated method stub
                
                getLogs_a().d("DeviceFilter#onDeviceAccepted: %s", new Object[] {
                        castdevice
                });
                
                addCastDevice_a(CastMediaRouteProvider.this, castdevice, set);
                publishRoutes_b();
            }
            
        };
        
        mMdnsDeviceScanner_n = new MdnsDeviceScanner(context);
        mMdnsDeviceScanner_n.addListener_a(mDeviceScannerListener_o);
        publishRoutes_b();
    }

    public static Logs getLogs_a() {
        return mLogs_k;
    }

    public static synchronized CastMediaRouteProvider getInstance_a(Context context) {
        if (mInstance_l == null) {
            mInstance_l = new CastMediaRouteProvider(context.getApplicationContext());
        }

        return mInstance_l;
    }

    public static CastDeviceController createDeviceController_a(
            CastMediaRouteProvider provider, CastRouteController controller) {
        CastDevice castdevice = controller.mCastDevice_a;
        String id = castdevice.getDeviceId();
        CastDeviceControllerHelper awu1 = (CastDeviceControllerHelper) provider.q.get(id);
        if (awu1 == null) {
            awu1 = new CastDeviceControllerHelper(provider);
            mLogs_k.d("creating CastDeviceController for %s", new Object[] {
                    castdevice
            });
            CastSrvControllerImpl awe1 = new CastSrvControllerImpl(provider, awu1);
            awu1.mCastDeviceController_a = CastDeviceController.createCastDeviceController_a(
                    ((MediaRouteProvider) (provider)).mContext_a,
                    ((MediaRouteProvider) (provider)).mHandler_c,
                    "gms_cast_mrp", castdevice, awe1);
            awu1.mCastDeviceController_a.setSubTag_a("MRP");
            awu1.c = true;
            provider.q.put(id, awu1);
            mLogs_k.d("Connecting controller to device", new Object[0]);
            awu1.mCastDeviceController_a.connectDevice_b();
        }
        awu1.e.add(controller);
        return awu1.mCastDeviceController_a;
    }

    public static String getFriendlyName_a(CastDevice castdevice) {
        return castdevice.getFriendlyName();
    }

    private MediaRouteDescriptor buildRouteDescriptorForDevice_a(DiscoveryCriteriaHelper criteriaHelper) {
        CastDevice castdevice = criteriaHelper.mCastDevice_a;
        Set set = criteriaHelper.mDiscoveryCriteriaSet_b;
        CastDeviceControllerHelper controllerHelper = (CastDeviceControllerHelper) q.get(castdevice
                .getDeviceId());
        String statusText;
        boolean isConnecting;
        int volumeHandling;
        int volume;
        String status;
        if (controllerHelper != null) {
            boolean flag1 = controllerHelper.c;
            CastDeviceController controller = controllerHelper.mCastDeviceController_a;
            if (controller != null && controller.d()) {
                int k1 = (int) Math.round(20D * controller.getVolume_f());
                statusText = controller.getStatusText_g();
                volume = k1;
                isConnecting = flag1;
                volumeHandling = 1;
            } else {
                isConnecting = flag1;
                statusText = null;
                volumeHandling = 0;
                volume = 0;
            }
        } else {
            statusText = null;
            isConnecting = false;
            volumeHandling = 0;
            volume = 0;
        }
        if (TextUtils.isEmpty(statusText))
            status = castdevice.getModelName();
        else
            status = statusText;
        Bundle bundle = new Bundle();
        castdevice.putInBundle(bundle);
        ArrayList arraylist = new ArrayList();
        IntentFilter intentfilter;
        for (Iterator iterator = set.iterator(); iterator.hasNext(); arraylist.add(intentfilter)) {
            DiscoveryCriteria criteria = (DiscoveryCriteria) iterator.next();
            intentfilter = new IntentFilter();
            String category = criteria.mCategory_a;
            intentfilter.addCategory(category);
            if (!isEquals_a(category, "android.media.intent.category.REMOTE_PLAYBACK")
                    && !isEquals_a(category, "com.fireflycast.cast.CATEGORY_CAST_REMOTE_PLAYBACK"))
                continue;
            for (Iterator iterator1 = mIntentFilterList_v.iterator(); iterator1.hasNext(); arraylist
                    .add((IntentFilter) iterator1.next()))
                ;
        }

        mLogs_k.d("buildRouteDescriptorForDevice: id=%s, description=%s, connecting=%b, volume=%d",
                castdevice.getDeviceId(), castdevice.getFriendlyName(),
                Boolean.valueOf(isConnecting),
                Integer.valueOf(volume));
        MediaRouteDescriptorPrivateData data = new MediaRouteDescriptorPrivateData(
                castdevice.getDeviceId(), castdevice.getFriendlyName());
        data.mBundle_a.putString("status", status);
        data.mBundle_a.putBoolean("connecting", isConnecting);
        data.mBundle_a.putInt("volumeHandling", volumeHandling);
        data.mBundle_a.putInt("volume", volume);
        data.mBundle_a.putInt("volumeMax", 20);
        data.mBundle_a.putInt("playbackType", 1);
        MediaRouteDescriptorPrivateData privateData = data.addIntentFilterList_a(arraylist);
        privateData.mBundle_a.putBundle("extras", bundle);
        if (privateData.mControlIntentFilterList_b != null)
            privateData.mBundle_a.putParcelableArrayList("controlFilters", privateData.mControlIntentFilterList_b);
        return new MediaRouteDescriptor(privateData.mBundle_a, privateData.mControlIntentFilterList_b);
    }

    static void publishRoutes_a(CastMediaRouteProvider awb1) {
        awb1.publishRoutes_b();
    }

    static void a(CastMediaRouteProvider awb1, CastDevice castdevice) {
        awb1.b(castdevice);
    }

    static void addCastDevice_a(CastMediaRouteProvider routeProvider, CastDevice castdevice, Set set) {
        DiscoveryCriteriaHelper criteriaHelper = (DiscoveryCriteriaHelper) routeProvider.mDiscoveryCriteriaMap_p.get(castdevice
                .getDeviceId());
        if (criteriaHelper != null) {
            Object aobj[] = new Object[1];
            aobj[0] = castdevice.getFriendlyName();
            mLogs_k.d("merging in criteria for existing device %s", aobj);
            Iterator iterator = set.iterator();
            do {
                if (!iterator.hasNext())
                    break;
                DiscoveryCriteria criteria = (DiscoveryCriteria) iterator.next();
                if (!criteriaHelper.mDiscoveryCriteriaSet_b.contains(criteria))
                    criteriaHelper.mDiscoveryCriteriaSet_b.add(criteria);
            } while (true);
        } else {
            routeProvider.mDiscoveryCriteriaMap_p.put(castdevice.getDeviceId(), new DiscoveryCriteriaHelper(castdevice, set));
            if (routeProvider.r != null)
                routeProvider.r.a(castdevice);
        }
    }

    private static boolean isEquals_a(String s1, String s2) {
        return s1.equals(s2)
                || s1.startsWith((new StringBuilder()).append(s2).append("/").toString());
    }

    static DeviceScanner getMdnsDeviceScanner_b(CastMediaRouteProvider awb1) {
        return awb1.mMdnsDeviceScanner_n;
    }

    private void publishRoutes_b() {
        ArrayList routeList = new ArrayList();
        for (Iterator iterator = mDiscoveryCriteriaMap_p.values().iterator(); iterator.hasNext(); routeList
                .add(buildRouteDescriptorForDevice_a((DiscoveryCriteriaHelper) iterator.next())))
            ;
        MediaRouteProviderDescriptor providerDescriptor = (new MediaRouteProviderDescriptorHelper())
                .addMediaRouteDescriptors_a(routeList).createMediaRouteProviderDescriptor_a();
        MainThreadChecker.isOnAppMainThread_a();
        if (super.mMediaRouteProviderDescriptor_g != providerDescriptor) {
            super.mMediaRouteProviderDescriptor_g = providerDescriptor;
            if (!super.mPendingDescriptorChange_h) {
                super.mPendingDescriptorChange_h = true;
                super.mHandler_c.sendEmptyMessage(1); //MSG_DELIVER_DESCRIPTOR_CHANGED
            }
        }
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(routeList.size());
        mLogs_k.d("published %d routes", aobj);
    }

    public static void b(CastMediaRouteProvider awb1, CastRouteController awn1) {
        CastDevice castdevice = awn1.mCastDevice_a;
        String id = castdevice.getDeviceId();
        CastDeviceControllerHelper awu1 = (CastDeviceControllerHelper) awb1.q.get(id);
        if (awu1 != null) {
            awu1.e.remove(awn1);
            if (awu1.isEmpty_a()) {
                mLogs_k.d("disposing CastDeviceController for %s", new Object[] {
                        castdevice
                });
                awu1.mCastDeviceController_a.releaseReference_q();
                if (awu1.d)
                    awb1.mMdnsDeviceScanner_n.setDeviceOffline_a(id);
                if (!awu1.b || awu1.d) {
                    awb1.b(castdevice);
                    awb1.publishRoutes_b();
                }
                awb1.q.remove(id);
            }
        }
    }

    private void b(CastDevice castdevice) {
        mDiscoveryCriteriaMap_p.remove(castdevice.getDeviceId());
        if (r != null)
            r.b(castdevice);
    }

    static Map c(CastMediaRouteProvider awb1) {
        return awb1.q;
    }

    private void onDiscoveryRequestChanged_c() {
        boolean isStartScan, flag1, flag2;
        HashSet hashset;
        DiscoveryRequest request;
        isStartScan = true;
        hashset = new HashSet(mDiscoveryCriteriaHashSet_u);
        mDiscoveryCriteriaHashSet_u.clear();
        request = super.mDiscoveryRequest_e;
        if (request == null) {
            flag1 = false;
        } else {
            List list;
            int size;
            int index;
            list = request.getSelector_a().getControlCategories_a();
            size = list.size();
            index = 0;
            flag1 = false;
            while (index < size) {
                String category = (String) list.get(index);
                if (category.equals("android.media.intent.category.REMOTE_PLAYBACK")) {
                    try {
                        String s2 = (String) i.b();
                        DiscoveryCriteria aty2 = new DiscoveryCriteria();
                        aty2.mCategory_a = "android.media.intent.category.REMOTE_PLAYBACK";
                        aty2.mAppid_b = s2;
                        mDiscoveryCriteriaHashSet_u.add(aty2);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } finally {
                        flag2 = isStartScan;
                    }
                } else if (!category.equals("com.fireflycast.cast.CATEGORY_CAST_REMOTE_PLAYBACK")
                        && !category.startsWith("com.fireflycast.cast.CATEGORY_CAST_REMOTE_PLAYBACK/")
                        && !category.equals("com.fireflycast.cast.CATEGORY_CAST")
                        && !category.startsWith("com.fireflycast.cast.CATEGORY_CAST/")) {
                    flag2 = flag1;
                } else {
                    try {
                        DiscoveryCriteria criteria = DiscoveryCriteria.getDiscoveryCriteria_a(category);
                        mDiscoveryCriteriaHashSet_u.add(criteria);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } finally {
                        flag2 = isStartScan;
                    }
                }

                index++;
                flag1 = flag2;
            }
        }
        if (s) {
            if (!mDiscoveryCriteriaHashSet_u.contains(mDiscoveryCriteria_w)) {
                mDiscoveryCriteriaHashSet_u.add(mDiscoveryCriteria_w);
            }
        } else {
            mDiscoveryCriteriaHashSet_u.remove(mDiscoveryCriteria_w);
            isStartScan = flag1;
        }

        if (hashset.equals(mDiscoveryCriteriaHashSet_u)) {
            isStartScan = false;
        } else {
            mCastDeviceFilter_t.reset_a(mDiscoveryCriteriaHashSet_u);
        }

        if (isStartScan) {
            mLogs_k.d("starting the scan", new Object[0]);
            CastDeviceService.stopScanCastDevice_b(super.mContext_a,
                    mMdnsDeviceScanner_n);
            CastDeviceService.startScanCastDevice_a(super.mContext_a,
                    mMdnsDeviceScanner_n);
            return;
        } else {
            mLogs_k.d("stopping the scan", new Object[0]);
            CastDeviceService.stopScanCastDevice_b(super.mContext_a,
                    mMdnsDeviceScanner_n);
            return;
        }
    }

    static DeviceFilter getCastDeviceFilter_d(CastMediaRouteProvider awb1) {
        return awb1.mCastDeviceFilter_t;
    }

    private List getAllIntentFilters_d() {
        int i1 = 0;
        ArrayList arraylist = new ArrayList();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
        intentfilter.addAction("android.media.intent.action.PLAY");
        intentfilter.addDataScheme("http");
        intentfilter.addDataScheme("https");
        // String as[] =
        // super.mContext_a.getResources().getStringArray(0x7f0e0002);
        String mimeTypes[] = mCastMimeTypes;
        int j1 = mimeTypes.length;
        int k1 = 0;
        while (k1 < j1) {
            String s2 = mimeTypes[k1];
            try {
                intentfilter.addDataType(s2);
            } catch (android.content.IntentFilter.MalformedMimeTypeException malformedmimetypeexception) {
                throw new RuntimeException(malformedmimetypeexception);
            }
            k1++;
        }
        arraylist.add(intentfilter);
        String as1[] = mPlayActions_m;
        for (int l1 = as1.length; i1 < l1; i1++) {
            String s1 = as1[i1];
            IntentFilter intentfilter2 = new IntentFilter();
            intentfilter2.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
            intentfilter2.addAction(s1);
            arraylist.add(intentfilter2);
        }

        IntentFilter intentfilter1 = new IntentFilter();
        intentfilter1.addCategory("com.fireflycast.cast.CATEGORY_CAST_REMOTE_PLAYBACK");
        intentfilter1.addAction("com.fireflycast.cast.ACTION_SYNC_STATUS");
        arraylist.add(intentfilter1);
        return Collections.unmodifiableList(arraylist);
    }

    static Map e(CastMediaRouteProvider awb1) {
        return awb1.mDiscoveryCriteriaMap_p;
    }

    static IMirrorDeviceHelper f(CastMediaRouteProvider awb1) {
        return awb1.r;
    }

    static Map g(CastMediaRouteProvider awb1) {
        return awb1.x;
    }

    public final RouteController getRouteController_a(String routeId) {
        DiscoveryCriteriaHelper awv1 = (DiscoveryCriteriaHelper) mDiscoveryCriteriaMap_p.get(routeId);
        if (awv1 == null)
            return null;
        else
            return new CastRouteController(this, awv1.mCastDevice_a);
    }

    public final void a(IMirrorDeviceHelper aww1) {
        r = aww1;
    }

    public final void onDiscoveryRequestChanged_a(DiscoveryRequest request) {
        mLogs_k.d("in onDiscoveryRequestChanged: request=%s", new Object[] {
                request
        });
        onDiscoveryRequestChanged_c();
    }

    public final void a(boolean flag) {
        if (s != flag) {
            s = flag;
            onDiscoveryRequestChanged_c();
        }
    }

    static {
        StringBuffer stringbuffer = new StringBuffer("com.fireflycast.cast.CATEGORY_CAST");
        if (!"674A0243".matches("[A-F0-9]+")) {
            throw new IllegalArgumentException((new StringBuilder("Invalid appliation ID: "))
                    .append("674A0243").toString());
        } else {
            stringbuffer.append("/").append("674A0243");
            mDiscoveryCriteria_w = DiscoveryCriteria.getDiscoveryCriteria_a(stringbuffer.toString());
        }
    }
}
