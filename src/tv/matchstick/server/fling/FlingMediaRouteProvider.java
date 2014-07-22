
package tv.matchstick.server.fling;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tv.matchstick.fling.FlingDevice;
import tv.matchstick.fling.service.FlingDeviceService;
import tv.matchstick.server.cast_mirroring.IMirrorDeviceHelper;
import tv.matchstick.server.common.checker.MainThreadChecker;
import tv.matchstick.server.fling.mdns.DeviceScanner;
import tv.matchstick.server.fling.mdns.IDeviceScanListener;
import tv.matchstick.server.fling.mdns.MdnsDeviceScanner;
import tv.matchstick.server.fling.mdns.MediaRouteDescriptorPrivateData;
import tv.matchstick.server.fling.media.RouteController;
import tv.matchstick.server.utils.C_bcx;
import tv.matchstick.server.utils.LOG;

public class FlingMediaRouteProvider extends MediaRouteProvider {
    public static final C_bcx i = C_bcx.a("gms:cast:media:generic_player_app_id", "CC1AD845");
    public static final C_bcx j = C_bcx.a("gms:cast:media:use_tdls", true);
    private static final LOG mLogs = new LOG("FlingMediaRouteProvider");
    private static FlingMediaRouteProvider mInstance;
    private static final String mPlayActions[] = {
            "android.media.intent.action.PAUSE", "android.media.intent.action.RESUME",
            "android.media.intent.action.STOP", "android.media.intent.action.SEEK",
            "android.media.intent.action.GET_STATUS", "android.media.intent.action.START_SESSION",
            "android.media.intent.action.GET_SESSION_STATUS",
            "android.media.intent.action.END_SESSION"
    };
    private static final DiscoveryCriteria mDiscoveryCriteria;
    private final DeviceScanner mMdnsDeviceScanner;

    private final IDeviceScanListener mDeviceScannerListener = new IDeviceScanListener() {

        @Override
        public void onAllDevicesOffline() {
            // TODO Auto-generated method stub
            mLogs.d("DeviceScanner.Listener#onAllDevicesOffline");
            for (Iterator iterator = mDiscoveryCriteriaMap.entrySet().iterator(); iterator
                    .hasNext();)
            {
                DiscoveryCriteriaHelper awv1 = (DiscoveryCriteriaHelper) ((java.util.Map.Entry) iterator
                        .next()).getValue();
                if (awv1 != null)
                {
                    FlingDevice flingdevice = awv1.mFlingDevice;
                    FlingDeviceControllerHelper awu1 = (FlingDeviceControllerHelper) q
                            .get(flingdevice.getDeviceId());
                    if (awu1 != null)
                    {
                        awu1.b = false;
                        mLogs.d("device %s is in use; not removing route",flingdevice);
                    } else
                    {
                        if (r != null) {
                            r.b(flingdevice);
                        }
                        iterator.remove();
                    }
                } else
                {
                    iterator.remove();
                }
            }

            publishRoutes();
        }

        @Override
        public void onDeviceOnline(FlingDevice flingdevice) {
            // TODO Auto-generated method stub
            mLogs.d("DeviceScanner.Listener#onDeviceOnline :%s",flingdevice);
            FlingDeviceControllerHelper awu1 = (FlingDeviceControllerHelper) q.get(flingdevice
                    .getDeviceId());
            if (awu1 != null) {
                awu1.b = true;
            }
            mFlingDeviceFilter.connectOrAcceptDevice(flingdevice);
        }

        @Override
        public void onDeviceOffline(FlingDevice flingdevice) {
            // TODO Auto-generated method stub
            mLogs.d("DeviceScanner.Listener#onDeviceOffline :%s",
                    new Object[] {
                        flingdevice
                    });
            FlingDeviceControllerHelper awu1 = (FlingDeviceControllerHelper) q.get(flingdevice
                    .getDeviceId());
            if (awu1 != null)
            {
                awu1.b = false;
            } else
            {
                b(flingdevice);
                publishRoutes();
            }
        }

    };

    private final Map mDiscoveryCriteriaMap = new HashMap();
    private final Map q = new HashMap();
    private IMirrorDeviceHelper r;
    private boolean s;
    private final DeviceFilter mFlingDeviceFilter;
    private final Set mDiscoveryCriteriaHashSet = new HashSet();
    private final List mIntentFilterList = getAllIntentFilters();
    private final Map x = new HashMap();

    private static final String[] mFlingMimeTypes = {
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

    private FlingMediaRouteProvider(Context context) {
        super(context);
        x.put(Integer.valueOf(1), "Request failed");
        x.put(Integer.valueOf(2), "Failed to start a session");
        x.put(Integer.valueOf(2), "Unknown or invalid session ID");
        x.put(Integer.valueOf(3), "Disconnected from Fling Device but trying to reconnect");
        
        mFlingDeviceFilter = new DeviceFilter(context, mDiscoveryCriteriaHashSet, "gms_cast_mrp"){

            @Override
            protected void setDeviceOffline(FlingDevice flingdevice) {
                // TODO Auto-generated method stub
                mMdnsDeviceScanner.setDeviceOffline(flingdevice.getDeviceId());
            }

            @Override
            protected void onDeviceAccepted(FlingDevice flingdevice, Set set) {
                // TODO Auto-generated method stub
                
                getLogs_a().d("DeviceFilter#onDeviceAccepted: %s", new Object[] {
                        flingdevice
                });
                
                addFlingDevice(FlingMediaRouteProvider.this, flingdevice, set);
                publishRoutes();
            }
            
        };
        
        mMdnsDeviceScanner = new MdnsDeviceScanner(context);
        mMdnsDeviceScanner.addListener(mDeviceScannerListener);
        publishRoutes();
    }

    public static LOG getLogs_a() {
        return mLogs;
    }

    public static synchronized FlingMediaRouteProvider getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FlingMediaRouteProvider(context.getApplicationContext());
        }

        return mInstance;
    }

    public static FlingDeviceController createDeviceController(
            FlingMediaRouteProvider provider, FlingRouteController controller) {
        FlingDevice flingdevice = controller.mFlingDevice;
        String id = flingdevice.getDeviceId();
        FlingDeviceControllerHelper awu1 = (FlingDeviceControllerHelper) provider.q.get(id);
        if (awu1 == null) {
            awu1 = new FlingDeviceControllerHelper(provider);
            mLogs.d("creating FlingDeviceController for %s", flingdevice);
            FlingSrvControllerImpl awe1 = new FlingSrvControllerImpl(provider, awu1);
            awu1.mFlingDeviceController = FlingDeviceController.create(
                    ((MediaRouteProvider) (provider)).mContext,
                    ((MediaRouteProvider) (provider)).mHandler,
                    "gms_cast_mrp", flingdevice, awe1);
            awu1.mFlingDeviceController.setSubTag("MRP");
            awu1.c = true;
            provider.q.put(id, awu1);
            mLogs.d("Connecting controller to device", new Object[0]);
            awu1.mFlingDeviceController.connectDevice();
        }
        awu1.e.add(controller);
        return awu1.mFlingDeviceController;
    }

    public static String getFriendlyName(FlingDevice flingdevice) {
        return flingdevice.getFriendlyName();
    }

    private MediaRouteDescriptor buildRouteDescriptorForDevice(DiscoveryCriteriaHelper criteriaHelper) {
        FlingDevice flingdevice = criteriaHelper.mFlingDevice;
        Set set = criteriaHelper.mDiscoveryCriteriaSet;
        FlingDeviceControllerHelper controllerHelper = (FlingDeviceControllerHelper) q.get(flingdevice
                .getDeviceId());
        String statusText;
        boolean isConnecting;
        int volumeHandling;
        int volume;
        String status;
        if (controllerHelper != null) {
            boolean flag1 = controllerHelper.c;
            FlingDeviceController controller = controllerHelper.mFlingDeviceController;
            if (controller != null && controller.isConnected()) {
                int k1 = (int) Math.round(20D * controller.getVolume());
                statusText = controller.getStatusText();
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
            status = flingdevice.getModelName();
        else
            status = statusText;
        Bundle bundle = new Bundle();
        flingdevice.putInBundle(bundle);
        ArrayList arraylist = new ArrayList();
        IntentFilter intentfilter;
        for (Iterator iterator = set.iterator(); iterator.hasNext(); arraylist.add(intentfilter)) {
            DiscoveryCriteria criteria = (DiscoveryCriteria) iterator.next();
            intentfilter = new IntentFilter();
            String category = criteria.mCategory;
            intentfilter.addCategory(category);
            if (!isEquals(category, "android.media.intent.category.REMOTE_PLAYBACK")
                    && !isEquals(category, "tv.matchstick.fling.CATEGORY_FLING_REMOTE_PLAYBACK"))
                continue;
            for (Iterator iterator1 = mIntentFilterList.iterator(); iterator1.hasNext(); arraylist
                    .add((IntentFilter) iterator1.next()))
                ;
        }

        mLogs.d("buildRouteDescriptorForDevice: id=%s, description=%s, connecting=%b, volume=%d",
                flingdevice.getDeviceId(), flingdevice.getFriendlyName(),
                Boolean.valueOf(isConnecting),
                Integer.valueOf(volume));
        MediaRouteDescriptorPrivateData data = new MediaRouteDescriptorPrivateData(
                flingdevice.getDeviceId(), flingdevice.getFriendlyName());
        data.mBundle.putString("status", status);
        data.mBundle.putBoolean("connecting", isConnecting);
        data.mBundle.putInt("volumeHandling", volumeHandling);
        data.mBundle.putInt("volume", volume);
        data.mBundle.putInt("volumeMax", 20);
        data.mBundle.putInt("playbackType", 1);
        MediaRouteDescriptorPrivateData privateData = data.addIntentFilterList(arraylist);
        privateData.mBundle.putBundle("extras", bundle);
        if (privateData.mControlIntentFilterList != null)
            privateData.mBundle.putParcelableArrayList("controlFilters", privateData.mControlIntentFilterList);
        return new MediaRouteDescriptor(privateData.mBundle, privateData.mControlIntentFilterList);
    }

    static void publishRoutes(FlingMediaRouteProvider awb1) {
        awb1.publishRoutes();
    }

    static void a(FlingMediaRouteProvider awb1, FlingDevice flingdevice) {
        awb1.b(flingdevice);
    }

    static void addFlingDevice(FlingMediaRouteProvider routeProvider, FlingDevice flingdevice, Set set) {
        DiscoveryCriteriaHelper criteriaHelper = (DiscoveryCriteriaHelper) routeProvider.mDiscoveryCriteriaMap.get(flingdevice
                .getDeviceId());
        if (criteriaHelper != null) {
            mLogs.d("merging in criteria for existing device %s", flingdevice.getFriendlyName());
            Iterator iterator = set.iterator();
            do {
                if (!iterator.hasNext())
                    break;
                DiscoveryCriteria criteria = (DiscoveryCriteria) iterator.next();
                if (!criteriaHelper.mDiscoveryCriteriaSet.contains(criteria))
                    criteriaHelper.mDiscoveryCriteriaSet.add(criteria);
            } while (true);
        } else {
            routeProvider.mDiscoveryCriteriaMap.put(flingdevice.getDeviceId(), new DiscoveryCriteriaHelper(flingdevice, set));
            if (routeProvider.r != null)
                routeProvider.r.a(flingdevice);
        }
    }

    private static boolean isEquals(String s1, String s2) {
        return s1.equals(s2)
                || s1.startsWith(s2 + "/");
    }

    static DeviceScanner getMdnsDeviceScanner(FlingMediaRouteProvider awb1) {
        return awb1.mMdnsDeviceScanner;
    }

    private void publishRoutes() {
        ArrayList routeList = new ArrayList();
        for (Iterator iterator = mDiscoveryCriteriaMap.values().iterator(); iterator.hasNext(); routeList
                .add(buildRouteDescriptorForDevice((DiscoveryCriteriaHelper) iterator.next())))
            ;
        MediaRouteProviderDescriptor providerDescriptor = (new MediaRouteProviderDescriptorHelper())
                .addMediaRouteDescriptors(routeList).createMediaRouteProviderDescriptor();
        MainThreadChecker.isOnAppMainThread();
        if (super.mMediaRouteProviderDescriptor != providerDescriptor) {
            super.mMediaRouteProviderDescriptor = providerDescriptor;
            if (!super.mPendingDescriptorChange) {
                super.mPendingDescriptorChange = true;
                super.mHandler.sendEmptyMessage(1); //MSG_DELIVER_DESCRIPTOR_CHANGED
            }
        }

        mLogs.d("published %d routes", routeList.size());
    }

    public static void b(FlingMediaRouteProvider awb1, FlingRouteController awn1) {
        FlingDevice flingdevice = awn1.mFlingDevice;
        String id = flingdevice.getDeviceId();
        FlingDeviceControllerHelper awu1 = (FlingDeviceControllerHelper) awb1.q.get(id);
        if (awu1 != null) {
            awu1.e.remove(awn1);
            if (awu1.isEmpty()) {
                mLogs.d("disposing FlingDeviceController for %s", flingdevice);
                awu1.mFlingDeviceController.releaseReference();
                if (awu1.d)
                    awb1.mMdnsDeviceScanner.setDeviceOffline(id);
                if (!awu1.b || awu1.d) {
                    awb1.b(flingdevice);
                    awb1.publishRoutes();
                }
                awb1.q.remove(id);
            }
        }
    }

    private void b(FlingDevice flingdevice) {
        mDiscoveryCriteriaMap.remove(flingdevice.getDeviceId());
        if (r != null)
            r.b(flingdevice);
    }

    static Map c(FlingMediaRouteProvider awb1) {
        return awb1.q;
    }

    private void onDiscoveryRequestChanged() {
        boolean isStartScan, flag1, flag2;
        HashSet hashset;
        DiscoveryRequest request;
        isStartScan = true;
        hashset = new HashSet(mDiscoveryCriteriaHashSet);
        mDiscoveryCriteriaHashSet.clear();
        request = super.mDiscoveryRequest;
        if (request == null) {
            flag1 = false;
        } else {
            List list;
            int size;
            int index;
            list = request.getSelector().getControlCategories();
            size = list.size();
            index = 0;
            flag1 = false;
            while (index < size) {
                String category = (String) list.get(index);
                if (category.equals("android.media.intent.category.REMOTE_PLAYBACK")) {
                    try {
                        String s2 = (String) i.b();
                        DiscoveryCriteria aty2 = new DiscoveryCriteria();
                        aty2.mCategory = "android.media.intent.category.REMOTE_PLAYBACK";
                        aty2.mAppid = s2;
                        mDiscoveryCriteriaHashSet.add(aty2);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } finally {
                        flag2 = isStartScan;
                    }
                } else if (!category.equals("tv.matchstick.fling.CATEGORY_FLING_REMOTE_PLAYBACK")
                        && !category.startsWith("tv.matchstick.fling.CATEGORY_FLING_REMOTE_PLAYBACK/")
                        && !category.equals("tv.matchstick.fling.CATEGORY_FLING")
                        && !category.startsWith("tv.matchstick.fling.CATEGORY_FLING/")) {
                    flag2 = flag1;
                } else {
                    try {
                        DiscoveryCriteria criteria = DiscoveryCriteria.getDiscoveryCriteria(category);
                        mDiscoveryCriteriaHashSet.add(criteria);
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
            if (!mDiscoveryCriteriaHashSet.contains(mDiscoveryCriteria)) {
                mDiscoveryCriteriaHashSet.add(mDiscoveryCriteria);
            }
        } else {
            mDiscoveryCriteriaHashSet.remove(mDiscoveryCriteria);
            isStartScan = flag1;
        }

        if (hashset.equals(mDiscoveryCriteriaHashSet)) {
            isStartScan = false;
        } else {
            mFlingDeviceFilter.reset(mDiscoveryCriteriaHashSet);
        }

        if (isStartScan) {
            mLogs.d("starting the scan");
            
            FlingDeviceService.stopScanFlingDevice(super.mContext,
                    mMdnsDeviceScanner);
            FlingDeviceService.startScanFlingDevice(super.mContext,
                    mMdnsDeviceScanner);
            return;
        } else {
            mLogs.d("stopping the scan");
            
            FlingDeviceService.stopScanFlingDevice(super.mContext,
                    mMdnsDeviceScanner);
            return;
        }
    }

    static DeviceFilter getFlingDeviceFilter(FlingMediaRouteProvider awb1) {
        return awb1.mFlingDeviceFilter;
    }

    private List getAllIntentFilters() {
        int i1 = 0;
        ArrayList arraylist = new ArrayList();
        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
        intentfilter.addAction("android.media.intent.action.PLAY");
        intentfilter.addDataScheme("http");
        intentfilter.addDataScheme("https");
        // String as[] =
        // super.mContext_a.getResources().getStringArray(0x7f0e0002);
        String mimeTypes[] = mFlingMimeTypes;
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
        String as1[] = mPlayActions;
        for (int l1 = as1.length; i1 < l1; i1++) {
            String s1 = as1[i1];
            IntentFilter intentfilter2 = new IntentFilter();
            intentfilter2.addCategory("android.media.intent.category.REMOTE_PLAYBACK");
            intentfilter2.addAction(s1);
            arraylist.add(intentfilter2);
        }

        IntentFilter intentfilter1 = new IntentFilter();
        intentfilter1.addCategory("tv.matchstick.fling.CATEGORY_FLING_REMOTE_PLAYBACK");
        intentfilter1.addAction("tv.matchstick.fling.ACTION_SYNC_STATUS");
        arraylist.add(intentfilter1);
        return Collections.unmodifiableList(arraylist);
    }

    static Map e(FlingMediaRouteProvider awb1) {
        return awb1.mDiscoveryCriteriaMap;
    }

    static IMirrorDeviceHelper f(FlingMediaRouteProvider awb1) {
        return awb1.r;
    }

    static Map g(FlingMediaRouteProvider awb1) {
        return awb1.x;
    }

    public final RouteController getRouteController(String routeId) {
        DiscoveryCriteriaHelper awv1 = (DiscoveryCriteriaHelper) mDiscoveryCriteriaMap.get(routeId);
        if (awv1 == null)
            return null;
        else
            return new FlingRouteController(this, awv1.mFlingDevice);
    }

    public final void a(IMirrorDeviceHelper aww1) {
        r = aww1;
    }

    public final void onDiscoveryRequestChanged(DiscoveryRequest request) {
        mLogs.d("in onDiscoveryRequestChanged: request=%s", request);
        onDiscoveryRequestChanged();
    }

    public final void a(boolean flag) {
        if (s != flag) {
            s = flag;
            onDiscoveryRequestChanged();
        }
    }

    static {
        StringBuffer stringbuffer = new StringBuffer("tv.matchstick.fling.CATEGORY_FLING");
        if (!"674A0243".matches("[A-F0-9]+")) {
            throw new IllegalArgumentException((new StringBuilder("Invalid appliation ID: "))
                    .append("674A0243").toString());
        } else {
            stringbuffer.append("/").append("674A0243");
            mDiscoveryCriteria = DiscoveryCriteria.getDiscoveryCriteria(stringbuffer.toString());
        }
    }
}
