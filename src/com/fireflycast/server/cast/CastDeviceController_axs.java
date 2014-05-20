
package com.fireflycast.server.cast;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.cast.bridge.ICastSrvController_axy;
import com.fireflycast.server.cast.channels.CastChannel_avn;
import com.fireflycast.server.cast.channels.ConnectionControlChannel_auj;
import com.fireflycast.server.cast.channels.DeviceAuthChannel_auk;
import com.fireflycast.server.cast.channels.HeartbeatChannel_aul;
import com.fireflycast.server.cast.channels.ReceiverControlChannel_aun;
import com.fireflycast.server.cast.service.CastDeviceService;
import com.fireflycast.server.cast.socket.CastSocketListener_atu;
import com.fireflycast.server.cast.socket.CastSocket_att;
import com.fireflycast.server.cast.socket.data.BinaryPayload_igp;
import com.fireflycast.server.cast.socket.data.C_axm;
import com.fireflycast.server.common.checker.PlatformChecker_aui;
import com.fireflycast.server.common.exception.C_atq;
import com.fireflycast.server.common.exception.C_aue;
import com.fireflycast.server.common.images.WebImage;
import com.fireflycast.server.utils.ApplicationInfo_atn;
import com.fireflycast.server.utils.ApplicationMetadataPriv_ato;
import com.fireflycast.server.utils.CastStatusCodes;
import com.fireflycast.server.utils.IMsgSender_avx;
import com.fireflycast.server.utils.Logs_avu;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public final class CastDeviceController_axs implements CastSocketListener_atu {
    private static AtomicLong ID_r = new AtomicLong(0L);

    // private final Runnable A = new C_axu(this);
    private final Runnable mHeartbeatRunnable_A = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            long time = SystemClock.elapsedRealtime();
            if (mHeartbeatChannel_m != null && mHeartbeatChannel_m.isTimeout_a(time))
            {
                mLogs_a.d("disconnecting due to heartbeat timeout", new Object[0]);
                onSocketError_g(CastStatusCodes.TIMEOUT);//15
                return;
            }
            if (mReceiverCtrlChannel != null)
            {
                mReceiverCtrlChannel.mLaunchRequestTracker_b.checkTimeout_b(time, 15);
                mReceiverCtrlChannel.mStopSessionRequestTracker_c.checkTimeout_b(time, 15);
                mReceiverCtrlChannel.mGetStatusRequestTracker_d.checkTimeout_b(time, 15);
                mReceiverCtrlChannel.mSetVolumeRequestTracker_e.checkTimeout_b(time, 0);
                mReceiverCtrlChannel.mSetMuteRequestTracker_f.checkTimeout_b(time, 0);
            }
            mHandler.postDelayed(mHeartbeatRunnable_A, 1000L);
        }

    };

    private boolean mIsConnecting_B;
    private boolean C;
    private boolean mDisposed_D;
    private int mDisconnectStatusCode_E;
    private long F;
    private final ReconnectStrategy_ayg mReconnectStrategy_G = new ReconnectStrategy_ayg();

    // private final Runnable mReconnectRunnable_H = new
    // ReconnectRunnable_axv(this);

    private final Runnable mReconnectRunnable_H = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (mReconnectStrategy_G.wasReconnecting_c())
            {
                mLogs_a.d("in reconnect Runnable", new Object[0]);
                connectToDeviceInternal_c();
            }
        }

    };

    private String mLastApplicationId_I;
    private String mLastSessionId_J;
    private final Logs_avu mLogs_a = new Logs_avu("CastDeviceController");
    private Integer b;
    private final Context mContext;
    private final Handler mHandler;
    private final CastDevice mCastDevice;
    private final ICastSrvController_axy mCastSrvController_f;
    private final CastSocket_att mCastSocket_g;
    private AtomicLong h;

    // private final C_axx i = new C_axx(this, (byte) 0);
    private final IMsgSender_avx mMsgSender_i = new IMsgSender_avx() {

        @Override
        public long getId_a() {
            // TODO Auto-generated method stub
            return h.incrementAndGet();
        }

        @Override
        public void sendMessage_a(String namespace, String message, long id, String transportId) {
            // TODO Auto-generated method stub
            String transId;
            if (transportId == null)
                transId = getTransId_o();
            else
                transId = transportId;
            CastDeviceService.sendMessage(mContext, CastDeviceController_axs.this, namespace,
                    message, id, transId);
        }

        @Override
        public void sendBinaryMessage_a(String nameSpace, byte[] abyte0, long l, String transportId) {
            // TODO Auto-generated method stub
            String transId;
            if (transportId == null)
                transId = getTransId_o();
            else
                transId = transportId;
            CastDeviceService.sendBinaryMessage(mContext, CastDeviceController_axs.this, nameSpace,
                    abyte0, 0L, transId);

        }

    };

    // private final ReceiverControlChannel_aun mReceiverCtrlChannel = new
    // CastReceiverControlChannel_axt(this, "receiver-0");

    private final ReceiverControlChannel_aun mReceiverCtrlChannel = new ReceiverControlChannel_aun(
            "receiver-0") {

        @Override
        protected void onRequestStatus_a(int j) {
            // TODO Auto-generated method stub
            mCastSrvController_f.onRequestStatus_c(j);
        }

        @Override
        protected void onConnectToApplicationAndNotify_a(ApplicationInfo_atn appInfo) {
            // TODO Auto-generated method stub
            connectToApplicationAndNotify_a(appInfo, true);
        }

        @Override
        protected void onStatusReceived_a(ApplicationInfo_atn appInfo, double level, boolean muted) {
            // TODO Auto-generated method stub
            mLogs_j.d("onStatusReceived", new Object[0]);
            mVolumeLevel_q = level;
            processReceiverStatus_a(CastDeviceController_axs.this, appInfo,
                    level, muted);
        }

        @Override
        protected void onApplicationDisconnected_b() {
            // TODO Auto-generated method stub
            int result;
            if (v)
                result = 0;
            else
                result = CastStatusCodes.APPLICATION_NOT_RUNNING; // 2005;
            v = false;
            mLastApplicationId_I = null;
            mLastSessionId_J = null;
            onApplicationDisconnected_e(result);
        }

        @Override
        protected void onApplicationConnectionFailed_b(int result) {
            // TODO Auto-generated method stub
            mCastSrvController_f.onApplicationConnectionFailed_b(result);
        }

        @Override
        protected void onStatusRequestFailed_c(int statusCode) {
            // TODO Auto-generated method stub
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(statusCode);
            mLogs_j.d("onStatusRequestFailed: statusCode=%d", aobj);

            if (mApplicationId_x != null)
            {
                if (mReconnectStrategy_G.b())
                {
                    mLogs_j.d("calling Listener.onConnectedWithoutApp()", new Object[0]);
                    mCastSrvController_f.onConnectedWithoutApp_b();
                } else
                {
                    mCastSrvController_f.onApplicationConnectionFailed_b(statusCode);
                }
                mApplicationId_x = null;
                mSessionId_y = null;
            }
        }

    };

    private final ConnectionControlChannel_auj mConnectionControlChannel_k;
    private DeviceAuthChannel_auk mDeviceAuthChannel_l;
    private HeartbeatChannel_aul mHeartbeatChannel_m;
    private final Set mNamespaces_n = new HashSet();
    private final Map mCastChannelMap_o = new HashMap();
    private final String p;
    private double mVolumeLevel_q;
    private ApplicationMetadata mApplicationMetadata_s;
    private String mSessionId_t;
    private String mStatusText_u;
    private boolean v;
    private boolean w;
    private String mApplicationId_x;
    private String mSessionId_y;
    private final long z = 10000L;

    private CastDeviceController_axs(Context context, Handler handler, CastDevice castdevice,
            String packageName_s1, long debugLevel, ICastSrvController_axy axy1)
    {
        b = new Integer(0);
        mContext = context;
        mHandler = handler;
        mCastDevice = castdevice;
        mCastSrvController_f = axy1;
        setDebugLevel_a(debugLevel);
        mDisconnectStatusCode_E = 0;
        mCastSocket_g = new CastSocket_att(context, this);
        a(mReceiverCtrlChannel);
        mConnectionControlChannel_k = new ConnectionControlChannel_auj(packageName_s1);
        a(mConnectionControlChannel_k);
        Object aobj[] = new Object[2];
        aobj[0] = packageName_s1;
        aobj[1] = Long.valueOf(ID_r.incrementAndGet());
        p = String.format("%s-%d", aobj);
    }

    static double setVolumeLevel_a(CastDeviceController_axs axs1, double volumeLevel)
    {
        axs1.mVolumeLevel_q = volumeLevel;
        return volumeLevel;
    }

    public static CastDeviceController_axs createCastDeviceController_a(Context context,
            Handler handler, String packageName,
            CastDevice castdevice, long debugLevel, ICastSrvController_axy axy1)
    {
        CastDeviceController_axs controller = new CastDeviceController_axs(context, handler, castdevice,
                packageName, debugLevel, axy1);
        controller.p();
        return controller;
    }

    public static CastDeviceController_axs createCastDeviceController_a(Context context, Handler handler, String packageName,
            CastDevice castdevice, ICastSrvController_axy axy1)
    {
        return createCastDeviceController_a(context, handler, packageName, castdevice, 0L, axy1);
    }

    static ICastSrvController_axy getCastSrvController_a(CastDeviceController_axs axs1)
    {
        return axs1.mCastSrvController_f;
    }

    private void connectToApplicationAndNotify_a(ApplicationInfo_atn applicationInfo, boolean flag)
    {
        mLogs_a.d("connectToApplicationAndNotify", new Object[0]);
        String transportId = applicationInfo.getTransportId_c();
        ApplicationMetadataPriv_ato metaData;
        String displayName;
        List list;
        try
        {
            mConnectionControlChannel_k.connect_a(transportId);
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mReceiverCtrlChannel.setTransportId_c(null);
            onSocketError_g(CastStatusCodes.NETWORK_ERROR);  //7
            return;
        }
        mLogs_a.d("setting current transport ID to %s", new Object[] {
                transportId
        });
        mReceiverCtrlChannel.setTransportId_c(transportId);
        metaData = ApplicationMetadata.getPrivateData_a(applicationInfo.getApplicationId_a());
        displayName = applicationInfo.getDisplayName_b();
        ApplicationMetadata.setName_a(metaData.mApplicationMetadata_a, displayName);
        list = applicationInfo.getAppImages_g();
        if (list != null)
        {
            WebImage webimage;
            for (Iterator iterator = list.iterator(); iterator.hasNext(); metaData.mApplicationMetadata_a
                    .addWebImage_a(webimage))
                webimage = (WebImage) iterator.next();

        }
        List list1 = applicationInfo.getNamespaces_f();
        ApplicationMetadata.setNamespaces_a(metaData.mApplicationMetadata_a, list1);
        PlatformChecker_aui checker = applicationInfo.getPlatformChecker_d();
        if (checker != null)
        {
            String s3 = checker.mPackage_b;
            ApplicationMetadata.setSenderAppIdentifier_b(metaData.mApplicationMetadata_a, s3);
            android.net.Uri uri = checker.mUri_c;
            ApplicationMetadata.setSenderAppLaunchUrl_a(metaData.mApplicationMetadata_a, uri);
        }
        mStatusText_u = applicationInfo.getStatusText_e();
        mApplicationMetadata_s = metaData.mApplicationMetadata_a;
        mSessionId_t = applicationInfo.getSessionId_h();
        mLastApplicationId_I = applicationInfo.getApplicationId_a();
        mLastSessionId_J = mSessionId_t;
        if (mReconnectStrategy_G.b()) {
            mHandler.removeCallbacks(mReconnectRunnable_H);
            mCastSrvController_f.onConnected_a();
        } else {
            mCastSrvController_f.onApplicationConnected_a(mApplicationMetadata_s, mStatusText_u,
                    mSessionId_t,
                    flag);
        }
    }

    static void onApplicationDisconnected_a(CastDeviceController_axs axs1, int statusCode)
    {
        axs1.onApplicationDisconnected_e(statusCode);
    }

    static void a(CastDeviceController_axs axs1, ApplicationInfo_atn atn1)
    {
        axs1.connectToApplicationAndNotify_a(atn1, true);
    }

    static void processReceiverStatus_a(CastDeviceController_axs castDeviceController,
            ApplicationInfo_atn applicationInfo, double volume, boolean muteState)
    {
        Logs_avu avu1 = castDeviceController.mLogs_a;
        Object aobj[] = new Object[3];
        aobj[0] = applicationInfo;
        aobj[1] = Double.valueOf(volume);
        aobj[2] = Boolean.valueOf(muteState);
        avu1.d("processReceiverStatus: applicationInfo=%s, volume=%f, muteState=%b", aobj);
        String statusText;
        if (applicationInfo != null)
            statusText = applicationInfo.getStatusText_e();
        else
            statusText = null;
        castDeviceController.mStatusText_u = statusText;
        castDeviceController.mCastSrvController_f.onVolumeChanged_a(castDeviceController.mStatusText_u, volume, muteState);
        if (castDeviceController.mApplicationId_x != null)
        {
            String applicationId;
            Logs_avu avu2;
            Object aobj1[];
            String s5;
            if (applicationInfo != null)
                applicationId = applicationInfo.getApplicationId_a();
            else
                applicationId = null;
            if (castDeviceController.mApplicationId_x.equals("") || castDeviceController.mApplicationId_x.equals(applicationId)) {
                if (castDeviceController.mSessionId_y != null
                        && !castDeviceController.mSessionId_y.equals(applicationInfo.getSessionId_h()))
                {
                    //String s4 = castDeviceController.mApplicationId_x;
                    castDeviceController.mApplicationId_x = null;
                    castDeviceController.mSessionId_y = null;
                    if (castDeviceController.w)
                    {
                        castDeviceController.w = false;
                        castDeviceController.launchApplicationInternal_b(castDeviceController.mApplicationId_x, ((String) (null)), true);
                        return;
                    } else
                    {
                        castDeviceController.mCastSrvController_f
                                .onApplicationConnectionFailed_b(CastStatusCodes.APPLICATION_NOT_RUNNING);// 2005
                        return;
                    }
                }
                String s3;
                if (applicationInfo != null)
                    s3 = applicationInfo.getStatusText_e();
                else
                    s3 = null;
                castDeviceController.mStatusText_u = s3;
                if (applicationInfo != null
                        && !TextUtils.isEmpty(applicationInfo.getTransportId_c()))
                {
                    castDeviceController.connectToApplicationAndNotify_a(applicationInfo, false);
                } else
                {
                    ICastSrvController_axy controller = castDeviceController.mCastSrvController_f;
                    char result;
                    if ("".equals(castDeviceController.mApplicationId_x))
                        result = '\u07D5';
                    else
                        result = '\u07D4';
                    controller.onApplicationConnectionFailed_b(result);
                }
                castDeviceController.mApplicationId_x = null;
                castDeviceController.mSessionId_y = null;
                castDeviceController.w = false;
                return;
            }
            avu2 = castDeviceController.mLogs_a;
            aobj1 = new Object[1];
            aobj1[0] = castDeviceController.mApplicationId_x;
            avu2.d("application to join (%s) is NOT available!", aobj1);
            s5 = castDeviceController.mApplicationId_x;
            castDeviceController.mApplicationId_x = null;
            castDeviceController.mSessionId_y = null;
            castDeviceController.mLastApplicationId_I = null;
            castDeviceController.mLastSessionId_J = null;
            if (!castDeviceController.w) {
                if (castDeviceController.mReconnectStrategy_G.b())
                {
                    castDeviceController.mCastSrvController_f.onConnectedWithoutApp_b();
                    return;
                } else
                {
                    castDeviceController.mLogs_a
                            .d("calling mListener.onApplicationConnectionFailed", new Object[0]);
                    castDeviceController.mCastSrvController_f
                            .onApplicationConnectionFailed_b(CastStatusCodes.APPLICATION_NOT_RUNNING);// 2005
                    return;
                }
            }
            castDeviceController.w = false;
            castDeviceController.launchApplicationInternal_b(s5, ((String) (null)), true);
        } else {
            castDeviceController.mLogs_a.d(
                    "processReceiverStatus_a:ignore status messages for application id is null!",
                    new Object[0]);
        }
        return;
    }

    private void sendMessage_a(ByteBuffer bytebuffer, String namespace_s1, long id_l1)
            throws IOException
    {
        try
        {
            mCastSocket_g.send_a(bytebuffer);
            mCastSrvController_f.a(namespace_s1, id_l1);
            return;
        } catch (C_atq atq1)
        {
            mCastSrvController_f.a(namespace_s1, id_l1,
                    CastStatusCodes.MESSAGE_SEND_BUFFER_TOO_FULL); // 2007
            return;
        } catch (C_aue aue1)
        {
            mCastSrvController_f.a(namespace_s1, id_l1, CastStatusCodes.MESSAGE_TOO_LARGE); // 2006
        }
    }

    private void handleConnectionFailure_a(boolean flag)
    {
        mIsConnecting_B = false;
        boolean wasReconnecting = mReconnectStrategy_G.wasReconnecting_c();
        Logs_avu avu1 = mLogs_a;
        Object aobj[] = new Object[1];
        aobj[0] = Boolean.valueOf(wasReconnecting);
        avu1.d("handleConnectionFailure; wasReconnecting=%b", aobj);
        if (flag)
        {
            long l1 = mReconnectStrategy_G.d();
            if (l1 >= 0L)
            {
                mHandler.postDelayed(mReconnectRunnable_H, l1);
                return;
            }
        } else
        {
            mReconnectStrategy_G.b();
        }
        if (wasReconnecting)
        {
            mCastSrvController_f.onDisconnected_a(mDisconnectStatusCode_E);
            mDisconnectStatusCode_E = 0;
            return;
        } else
        {
            mCastSrvController_f.onConnectionFailed_c();
            return;
        }
    }

    static boolean b(CastDeviceController_axs axs1)
    {
        return axs1.v;
    }

    static boolean c(CastDeviceController_axs axs1)
    {
        axs1.v = false;
        return false;
    }

    static String resetAppId_d(CastDeviceController_axs axs1)
    {
        axs1.mLastApplicationId_I = null;
        return null;
    }

    static String resetLastSessionId_e(CastDeviceController_axs axs1)
    {
        axs1.mLastSessionId_J = null;
        return null;
    }

    private void onApplicationDisconnected_e(int statusCode)
    {
        if (mReceiverCtrlChannel.mTransportId_a != null)
        {
            try
            {
                mConnectionControlChannel_k.close_b(mReceiverCtrlChannel.mTransportId_a);
                // } catch (IOException ioexception)
            } catch (Exception ioexception)
            {
                mLogs_a.w(ioexception, "Error while leaving application", new Object[0]);
                onSocketError_g(CastStatusCodes.NETWORK_ERROR); //7
            }
            mReceiverCtrlChannel.setTransportId_c(null);
        }
        if (mApplicationMetadata_s != null)
        {
            mApplicationMetadata_s = null;
            mSessionId_t = null;
        }
        mCastSrvController_f.onApplicationDisconnected_d(statusCode);
    }

    static String getApplicationId_f(CastDeviceController_axs axs1)
    {
        return axs1.mApplicationId_x;
    }

    private void finishDisconnecting_f(int socketError)
    {
        Logs_avu avu1 = mLogs_a;
        Object aobj[] = new Object[2];
        aobj[0] = Integer.valueOf(socketError);
        aobj[1] = Integer.valueOf(mDisconnectStatusCode_E);
        avu1.d("finishDisconnecting; socketError=%d, mDisconnectStatusCode=%d", aobj);
        mIsConnecting_B = false;
        C = false;
        mVolumeLevel_q = 0.0D;
        mApplicationMetadata_s = null;
        mSessionId_t = null;
        mStatusText_u = null;
        mHandler.removeCallbacks(mHeartbeatRunnable_A);
        int disconnectStatusCode;
        if (mDisconnectStatusCode_E != 0)
        {
            disconnectStatusCode = mDisconnectStatusCode_E;
            mDisconnectStatusCode_E = CastStatusCodes.SUCCESS;// 0;
        } else if (socketError == 3)
            disconnectStatusCode = CastStatusCodes.TIMEOUT;// ;15;
        else if (socketError == 4)
            disconnectStatusCode = CastStatusCodes.AUTHENTICATION_FAILED;// 2000;
        else
            disconnectStatusCode = CastStatusCodes.NETWORK_ERROR;// 7;
        mReconnectStrategy_G.b();
        mCastSrvController_f.onDisconnected_a(disconnectStatusCode);
    }

    static ReconnectStrategy_ayg g(CastDeviceController_axs axs1)
    {
        return axs1.mReconnectStrategy_G;
    }

    private void onSocketError_g(int socketError)
    {
        if (mCastSocket_g.isConnected_c())
        {
            mDisconnectStatusCode_E = socketError;
            mCastSocket_g.disconnect_b();
            return;
        } else
        {
            finishDisconnecting_f(socketError);
            return;
        }
    }

    static String resetApplicationId_h(CastDeviceController_axs axs1)
    {
        axs1.mApplicationId_x = null;
        return null;
    }

    static String resetSessionId_i(CastDeviceController_axs axs1)
    {
        axs1.mSessionId_y = null;
        return null;
    }

    static HeartbeatChannel_aul getHeartbeatChannel_j(CastDeviceController_axs axs1)
    {
        return axs1.mHeartbeatChannel_m;
    }

    static Logs_avu getLogs_k(CastDeviceController_axs axs1)
    {
        return axs1.mLogs_a;
    }

    static void onSocketError_l(CastDeviceController_axs axs1)
    {
        axs1.onSocketError_g(CastStatusCodes.TIMEOUT);//15
    }

    static ReceiverControlChannel_aun getReceiverControlChannel_m(CastDeviceController_axs axs1)
    {
        return axs1.mReceiverCtrlChannel;
    }

    static Runnable getHeartbeatRunnable_n(CastDeviceController_axs axs1)
    {
        return axs1.mHeartbeatRunnable_A;
    }

    static Handler getHandler_o(CastDeviceController_axs axs1)
    {
        return axs1.mHandler;
    }

    static DeviceAuthChannel_auk getDeviceAuthChannel_p(CastDeviceController_axs axs1)
    {
        return axs1.mDeviceAuthChannel_l;
    }

    static DeviceAuthChannel_auk resetDeviceAuthChannel_q(CastDeviceController_axs axs1)
    {
        axs1.mDeviceAuthChannel_l = null;
        return null;
    }

    static void finishConnecting_r(CastDeviceController_axs castDeviceController)
    {
        castDeviceController.mLogs_a.d("finishConnecting", new Object[0]);
        castDeviceController.h = new AtomicLong(0L);
        try
        {
            castDeviceController.mConnectionControlChannel_k.connect_a("receiver-0");
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            castDeviceController.onSocketError_g(CastStatusCodes.NETWORK_ERROR);//7
            return;
        }
        castDeviceController.mHeartbeatChannel_m = new HeartbeatChannel_aul();
        castDeviceController.a(castDeviceController.mHeartbeatChannel_m);
        castDeviceController.mHandler.postDelayed(castDeviceController.mHeartbeatRunnable_A, 1000L);
        castDeviceController.C = true;
        castDeviceController.mIsConnecting_B = false;
        if (castDeviceController.mLastApplicationId_I != null && castDeviceController.mLastSessionId_J != null)
        {
            castDeviceController.w = false;
            castDeviceController.joinApplicationInternal_c(castDeviceController.mLastApplicationId_I, castDeviceController.mLastSessionId_J);
            return;
        }
        castDeviceController.mReconnectStrategy_G.b();
        castDeviceController.mCastSrvController_f.onConnected_a();
        try
        {
            castDeviceController.mReceiverCtrlChannel.getStatus_a();
            return;
            // } catch (IOException ioexception1)
        } catch (Exception ioexception1)
        {
            castDeviceController.onSocketError_g(CastStatusCodes.NETWORK_ERROR); // 7
        }
    }

    static void handleConnectionFailure_s(CastDeviceController_axs axs1)
    {
        axs1.handleConnectionFailure_a(false);
    }

    static Context getContext_t(CastDeviceController_axs axs1)
    {
        return axs1.mContext;
    }

    static AtomicLong u(CastDeviceController_axs axs1)
    {
        return axs1.h;
    }

    public final void onConnected_a()
    {
        CastDeviceService.onSocketConnected_d(mContext, this);
    }

    public final void setVolume_a(double volume, double expected_level, boolean flag)
    {
        CastDeviceService.setVolume(mContext, this, volume, expected_level, flag);
    }

    public final void onConnectionFailed_a(int socketError)
    {
        Logs_avu avu1 = mLogs_a;
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(socketError);
        avu1.d("onConnectionFailed; socketError=%d", aobj);
        CastDeviceService.onSocketConnectionFailed(mContext, this, socketError);
    }

    public final void setDebugLevel_a(long l1)
    {
        if (F == l1)
            return;
        F = l1;
        boolean flag;
        if ((1L & F) != 0L)
            flag = true;
        else
            flag = false;
        mLogs_a.setDebugEnabled_a(flag);
        Logs_avu.setDebugEnabledByDefault(flag);
    }

    public final void a(CastChannel_avn castChannel)
    {
        castChannel.a(mMsgSender_i);
        mCastChannelMap_o.put(castChannel.nameSpace_k, castChannel);
    }

    public final void setSubTag_a(String s1)
    {
        mLogs_a.setSubTag(s1);
    }

    public final void reconnectToDevice_a(String lastApplicationId, String lastSessionId)
    {
        mLogs_a.d("reconnectToDevice: lastApplicationId=%s, lastSessionId=%s", new Object[] {
                lastApplicationId, lastSessionId
        });
        mLastApplicationId_I = lastApplicationId;
        mLastSessionId_J = lastSessionId;
        connectDevice_b();
    }

    public final void sendMessage_a(String namespace, String message, long id)
    {
        CastDeviceService.sendMessage(mContext, this, namespace, message, id,
                mReceiverCtrlChannel.mTransportId_a);
    }

    public final void sendMessage_a(String namespace, String message, long id, String transId)
    {
        if (TextUtils.isEmpty(transId))
        {
            mLogs_a.w("ignoring attempt to send a text message with no destination ID",
                    new Object[0]);
            mCastSrvController_f.a(namespace, id, CastStatusCodes.INVALID_REQUEST); // 2001
            return;
        }
        if (transId == null)
        {
            try
            {
                throw new IllegalStateException("The application has not launched yet.");
                // } catch (IOException ioexception)
            } catch (Exception ioexception)
            {
                mLogs_a.w(ioexception, "Error while sending message", new Object[0]);
            }
            onSocketError_g(CastStatusCodes.NETWORK_ERROR); // 7
            return;
        }
        byte abyte0[] = (new C_axm()).a(0).a(p).b(transId).c(namespace).b(0).d(message).K();
        try {
            if (abyte0.length > 0x10000)
            {
                throw new C_aue();
            } else
            {
                sendMessage_a(ByteBuffer.wrap(abyte0), namespace, id);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void launchApplication_a(String applicationId, String sessionId,
            boolean relaunchIfRunning)
    {
        CastDeviceService.launchApplication(mContext, this, applicationId, sessionId,
                relaunchIfRunning);
    }

    public final void sendBinaryMessage_a(String s1, byte abyte0[], long l1)
    {
        CastDeviceService.sendBinaryMessage(mContext, this, s1, abyte0, l1,
                mReceiverCtrlChannel.mTransportId_a);
    }

    public final void sendBinaryMessage_a(String nameSpace, byte abyte0[], long l1, String transId)
            throws IOException
    {
        if (TextUtils.isEmpty(transId))
        {
            mLogs_a.w("ignoring attempt to send a binary message with no destination ID",
                    new Object[0]);
            mCastSrvController_f.a(nameSpace, l1, CastStatusCodes.INVALID_REQUEST); // 2001
            return;
        }
        if (transId == null)
        {
            try
            {
                throw new IllegalStateException("The application has not launched yet.");
                // } catch (IOException ioexception)
            } catch (Exception ioexception)
            {
                mLogs_a.w(ioexception, "Error while sending message", new Object[0]);
            }
            onSocketError_g(CastStatusCodes.NETWORK_ERROR); //7
            return;
        }
        sendMessage_a(
                ByteBuffer.wrap((new C_axm()).a(0).a(p).b(transId).c(nameSpace).b(1)
                        .a(BinaryPayload_igp.a(abyte0)).K()),
                nameSpace, l1);
        return;
    }

    public final void onMessageReceived_a(ByteBuffer bytebuffer)
    {
        CastDeviceService.connectToDevice(mContext, this, bytebuffer);
    }

    public final void setMute_a(boolean flag, double d1, boolean flag1)
    {
        CastDeviceService.setMute(mContext, this, flag, d1, flag1);
    }

    public final void connectDevice_b()
    {
        if (mReconnectStrategy_G.wasReconnecting_c())
        {
            mLogs_a.d("already reconnecting; ignoring", new Object[0]);
            return;
        } else
        {
            mLogs_a.d("calling CastMediaRouteProviderService.connectToDevice", new Object[0]);
            CastDeviceService.connectCast(mContext, this);
            return;
        }
    }

    public final void setVolume_b(double level, double expected_level, boolean muted)
    {
        try
        {
            mReceiverCtrlChannel.setVolume_a(level, expected_level, muted);
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mLogs_a.w(ioexception, "Error while setting volume", new Object[0]);
        }
        onSocketError_g(CastStatusCodes.NETWORK_ERROR); // 7
    }

    public final void onDisconnected_b(int i1)
    {
        CastDeviceService.onSocketDisconnected_b(mContext, this, i1);
        if (mHeartbeatChannel_m != null)
        {
            b(((CastChannel_avn) (mHeartbeatChannel_m)));
            mHeartbeatChannel_m = null;
        }
        ReceiverControlChannel_aun aun1 = mReceiverCtrlChannel;
        aun1.mTransportId_a = null;
        aun1.mLaunchRequestTracker_b.a();
        aun1.mStopSessionRequestTracker_c.a();
        aun1.mGetStatusRequestTracker_d.a();
        aun1.mSetVolumeRequestTracker_e.a();
        aun1.mSetMuteRequestTracker_f.a();
        aun1.mLevel_g = 0.0D;
        aun1.mMuted_h = false;
        aun1.mFirst_i = false;
    }

    public final void b(CastChannel_avn avn1)
    {
        avn1.a((IMsgSender_avx) null);
        mCastChannelMap_o.remove(avn1.nameSpace_k);
    }

    public final void stopApplication_b(String s1)
    {
        CastDeviceService.stopApplication(mContext, this, s1);
    }

    public final void joinApplication_b(String applicationId, String sessionId)
    {
        CastDeviceService.joinApplication(mContext, this, applicationId, sessionId);
    }

    public final void launchApplicationInternal_b(String appId, String param, boolean relaunch)
    {
        Logs_avu avu1 = mLogs_a;
        Object aobj[] = new Object[2];
        aobj[0] = appId;
        aobj[1] = Boolean.valueOf(relaunch);
        avu1.d("launchApplicationInternal() id=%s, relaunch=%b", aobj);
        if (appId == null || appId.equals(""))
        {
            mCastSrvController_f
                    .onApplicationConnectionFailed_b(CastStatusCodes.APPLICATION_NOT_FOUND); // 2004
            return;
        }
        if (relaunch)
        {
            try
            {
                mReceiverCtrlChannel.launchApplication_a(appId, param);
                return;
                // } catch (IOException ioexception)
            } catch (Exception ioexception)
            {
                mLogs_a.w(ioexception, "Error while launching application", new Object[0]);
            }
            onSocketError_g(CastStatusCodes.NETWORK_ERROR);//7
            return;
        } else
        {
            w = true;
            joinApplicationInternal_c(appId, null);
            return;
        }
    }

    public final void onReceivedMessage_b(ByteBuffer bytebuffer) {
        if (mHeartbeatChannel_m != null)
            mHeartbeatChannel_m.reset_a();
        C_axm axm1 = null;
        String nameSpace;
        try {
            axm1 = C_axm.a(bytebuffer.array());
        }
        // catch (C_igt igt1)
        // {
        // Logs_avu avu1 = a;
        // Object aobj[] = new Object[1];
        // aobj[0] = igt1.getMessage();
        // avu1.b("Received an unparseable protobuf: %s", aobj);
        // return;
        // }
        catch (Exception e) {
            e.printStackTrace();
            mLogs_a.d("Received an unparseable protobuf???");
        }
        nameSpace = axm1.getNamespace_a();
        if (TextUtils.isEmpty(nameSpace)) {
            mLogs_a.d("Received a message with an empty or missing namespace", new Object[0]);
            return;
        }
        int payloadType = axm1.getPayloadType_d();
        CastChannel_avn castChannel = (CastChannel_avn) mCastChannelMap_o.get(nameSpace);
        if (castChannel != null) {
            BinaryPayload_igp binaryPayload;
            switch (payloadType) {
                default:
                    Logs_avu avu3 = mLogs_a;
                    Object aobj2[] = new Object[1];
                    aobj2[0] = Integer.valueOf(payloadType);
                    avu3.w("Unknown payload type %s; discarding message", aobj2);
                    return;

                case 0: // '\0'
                    castChannel.checkReceivedMessage_a_(axm1.getMessage_e());
                    return;

                case 1: // '\001'
                    binaryPayload = axm1.getBinaryMessage_f();
                    break;
            }
            byte abyte1[] = new byte[binaryPayload.getLength_a()];
            binaryPayload.copy_b(abyte1);
            castChannel.onReceivedMessage_a(abyte1);
            return;
        }
        boolean flag;
        synchronized (mNamespaces_n) {
            flag = mNamespaces_n.contains(nameSpace);
        }
        if (flag) {
            BinaryPayload_igp igp1;
            switch (payloadType) {
                default:
                    Logs_avu avu2 = mLogs_a;
                    Object aobj1[] = new Object[1];
                    aobj1[0] = Integer.valueOf(payloadType);
                    avu2.w("Unknown payload type %s; discarding message", aobj1);
                    return;

                case 0: // '\0'
                    mCastSrvController_f.notifyOnMessageReceived_a(nameSpace, axm1.getMessage_e());
                    return;

                case 1: // '\001'
                    igp1 = axm1.getBinaryMessage_f();
                    break;
            }
            byte abyte0[] = new byte[igp1.getLength_a()];
            igp1.copy_b(abyte0);
            mCastSrvController_f.a(nameSpace, abyte0);
            return;
        } else {
            mLogs_a.w("Ignoring message. Namespace has not been registered.", new Object[0]);
            return;
        }
    }

    public final void setMute_b(boolean mute, double level, boolean exp_muted)
    {
        try
        {
            mReceiverCtrlChannel.setMute_a(mute, level, exp_muted);
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mLogs_a.w(ioexception, "Error while setting mute state", new Object[0]);
        }
        onSocketError_g(CastStatusCodes.NETWORK_ERROR); // 7
    }

    public final void connectToDeviceInternal_c()
    {
        int socketState = mCastSocket_g.getState_f();
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(socketState);
        mLogs_a.d("connectToDeviceInternal; socket state = %d", aobj);
        if (socketState == 1 || socketState == 2)
        {
            mLogs_a.w("Redundant call to connect to device", new Object[0]);
            return;
        }
        mIsConnecting_B = true;
        if (!mReconnectStrategy_G.wasReconnecting_c())
            mReconnectStrategy_G.a();
        mReconnectStrategy_G.e();
        try
        {
            mLogs_a.d("connecting socket now", new Object[0]);
            mCastSocket_g.connect_a(mCastDevice.getHostIp_c(), mCastDevice.getServicePort_g());
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mLogs_a.d(ioexception, "connection exception", new Object[0]);
        }
        handleConnectionFailure_a(true);
    }

    public final void onSocketConnectionFailedInternal_c(int socketError)
    {
        Logs_avu avu1 = mLogs_a;
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(socketError);
        avu1.d("onSocketConnectionFailedInternal: socketError=%d", aobj);
        handleConnectionFailure_a(true);
    }

    public final void stopApplicationInternal_c(String sessionId)
    {
        mLogs_a.d("stopApplicationInternal() sessionId=%s", new Object[] {
                sessionId
        });
        try
        {
            v = true;
            mReceiverCtrlChannel.stopSession_b(sessionId);
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mLogs_a.w(ioexception, "Error while stopping application", new Object[0]);
        }
        onSocketError_g(CastStatusCodes.NETWORK_ERROR); // 7
    }

    public final void joinApplicationInternal_c(String applicationId, String sessionId)
    {
        mLogs_a.d("joinApplicationInternal(%s, %s)", new Object[] {
                applicationId, sessionId
        });
        if (mApplicationMetadata_s != null)
        {
            if (applicationId == null
                    || applicationId.equals(mApplicationMetadata_s.getApplicationId_b())
                    && (sessionId == null || sessionId.equals(mSessionId_t)))
            {
                mLogs_a.d("already connected to requested app, so skipping join logic",
                        new Object[0]);
                mCastSrvController_f.onApplicationConnected_a(mApplicationMetadata_s,
                        mStatusText_u,
                        mSessionId_t, false);
                return;
            }
            mLogs_a.d("clearing mLastConnected* variables", new Object[0]);
            mLastApplicationId_I = null;
            mLastSessionId_J = null;
            if (mReconnectStrategy_G.b())
            {
                mCastSrvController_f.onConnectedWithoutApp_b();
                return;
            } else
            {
                mCastSrvController_f
                        .onApplicationConnectionFailed_b(CastStatusCodes.APPLICATION_NOT_RUNNING);// 2005
                return;
            }
        }
        if (applicationId == null)
            applicationId = "";
        mApplicationId_x = applicationId;
        mSessionId_y = sessionId;
        try
        {
            mReceiverCtrlChannel.getStatus_a();
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mLogs_a.w(ioexception, "Error while requesting device status for join", new Object[0]);
        }
        onSocketError_g(CastStatusCodes.NETWORK_ERROR); // 7
    }

    public final void onSocketDisconnectedInternal_d(int socketError)
    {
        Logs_avu avu1 = mLogs_a;
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(socketError);
        avu1.d("onSocketDisconnectedInternal: socketError=%d", aobj);
        finishDisconnecting_f(socketError);
    }

    public final void setMessageReceivedCallbacks_d(String namespace)
    {
        CastDeviceService.setMessageReceivedCallbacks_b(mContext, this, namespace);
    }

    public final boolean d()
    {
        return C;
    }

    public final void addNamespace_e(String namespace)
    {
        if (TextUtils.isEmpty(namespace))
            return;
        synchronized (mNamespaces_n)
        {
            mNamespaces_n.add(namespace);
        }
    }

    public final boolean isConnecting_e()
    {
        return mIsConnecting_B;
    }

    public final double getVolume_f()
    {
        return mVolumeLevel_q;
    }

    public final void removeMessageReceivedCallbacks_f(String namespace)
    {
        CastDeviceService.removeMessageReceivedCallbacks_c(mContext, this, namespace);
    }

    public final String getStatusText_g()
    {
        return mStatusText_u;
    }

    public final void removeNamespace_g(String namespace)
    {
        if (TextUtils.isEmpty(namespace))
            return;
        synchronized (mNamespaces_n)
        {
            mNamespaces_n.remove(namespace);
        }
    }

    public final long h()
    {
        return F;
    }

    public final boolean isDisposed_i()
    {
        return mDisposed_D;
    }

    public final void leaveApplication_j()
    {
        CastDeviceService.leaveApplication_b(mContext, this);
    }

    public final void leaveApplicationInternal_k()
    {
        mLogs_a.d("leaveApplicationInternal()", new Object[0]);
        if (mApplicationMetadata_s == null)
        {
            mCastSrvController_f.onInvalidRequest_d();
            return;
        } else
        {
            onApplicationDisconnected_e(0);
            return;
        }
    }

    public final void requestStatus_l()
    {
        CastDeviceService.requestStatus_c(mContext, this);
    }

    public final void getStatus_m()
    {
        try
        {
            mReceiverCtrlChannel.getStatus_a();
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mLogs_a.w(ioexception, "Error while stopping application", new Object[0]);
        }

        onSocketError_g(CastStatusCodes.NETWORK_ERROR); // 7
    }

    public final void onSocketConnectedInternal_n()
    {
        mLogs_a.d("onSocketConnectedInternal", new Object[0]);

        /*
         * mDeviceAuthChannel_l = new CastDeviceAuthChannel_axw(this,
         * "receiver-0", mCastSocket_g.getPeerCertificate_g());
         */
        mDeviceAuthChannel_l = new DeviceAuthChannel_auk("receiver-0",
                mCastSocket_g.getPeerCertificate_g()) {

            @Override
            protected void verifyDevAuthResult_a(int result) {
                // TODO Auto-generated method stub
                b(mDeviceAuthChannel_l);
                mDeviceAuthChannel_l = null;
                if (result == 0)
                {
                    mLogs_j.d("authentication succeeded", new Object[0]);
                    finishConnecting_r(CastDeviceController_axs.this);
                } else
                {
                    handleConnectionFailure_s(CastDeviceController_axs.this);
                }
            }

        };

        a(mDeviceAuthChannel_l);
        try
        {
            mDeviceAuthChannel_l.doDeviceAuth_a();
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mLogs_a.w(ioexception, "Not able to authenticated Cast Device", new Object[0]);
        }
        b(mDeviceAuthChannel_l);
        mDeviceAuthChannel_l = null;
        handleConnectionFailure_a(false);
    }

    public final String getTransId_o()
    {
        return mReceiverCtrlChannel.mTransportId_a;
    }

    public final void p()
    {
        synchronized (b)
        {
            b = Integer.valueOf(1 + b.intValue());
        }
    }

    public final void releaseReference_q()
    {
        boolean flag;
        if (b.intValue() <= 0) {
            Logs_avu avu1 = mLogs_a;
            Object aobj[] = new Object[1];
            aobj[0] = Boolean.valueOf(mDisposed_D);
            avu1.e("unbalanced call to releaseReference(); mDisposed=%b", aobj);
            flag = false;
        } else {
            Integer integer;
            integer = Integer.valueOf(-1 + b.intValue());
            b = integer;
            if (integer.intValue() == 0)
                flag = true;
            else
                flag = false;
        }

        if (flag) {
            mDisposed_D = true;

            Object aobj1[] = new Object[1];
            aobj1[0] = mCastDevice;
            mLogs_a.d("[%s] *** disposing ***", aobj1);
            mReconnectStrategy_G.b();
            mHandler.removeCallbacks(mHeartbeatRunnable_A);
            mHandler.removeCallbacks(mReconnectRunnable_H);
            if (mCastSocket_g.isConnected_c()) {
                mCastSocket_g.disconnect_b();
            }
        }
    }
}
