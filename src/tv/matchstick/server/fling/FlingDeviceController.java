
package tv.matchstick.server.fling;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import tv.matchstick.fling.FlingDevice;
import tv.matchstick.fling.service.FlingDeviceService;
import tv.matchstick.server.common.checker.PlatformChecker;
import tv.matchstick.server.common.exception.C_atq;
import tv.matchstick.server.common.exception.C_aue;
import tv.matchstick.server.common.images.WebImage;
import tv.matchstick.server.fling.bridge.IFlingSrvController;
import tv.matchstick.server.fling.channels.FlingChannel;
import tv.matchstick.server.fling.channels.ConnectionControlChannel;
import tv.matchstick.server.fling.channels.DeviceAuthChannel;
import tv.matchstick.server.fling.channels.HeartbeatChannel;
import tv.matchstick.server.fling.channels.ReceiverControlChannel;
import tv.matchstick.server.fling.socket.FlingSocket;
import tv.matchstick.server.fling.socket.FlingSocketListener;
import tv.matchstick.server.fling.socket.data.BinaryPayload;
import tv.matchstick.server.fling.socket.data.C_axm;
import tv.matchstick.server.utils.ApplicationInfo;
import tv.matchstick.server.utils.ApplicationMetadataPriv;
import tv.matchstick.server.utils.FlingStatusCodes;
import tv.matchstick.server.utils.IMsgSender;
import tv.matchstick.server.utils.LOG;

public final class FlingDeviceController implements FlingSocketListener {
    private static AtomicLong ID = new AtomicLong(0L);

    private final Runnable mHeartbeatRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            long time = SystemClock.elapsedRealtime();
            if (mHeartbeatChannel != null && mHeartbeatChannel.isTimeout(time))
            {
                log.d("disconnecting due to heartbeat timeout");
                onSocketError(FlingStatusCodes.TIMEOUT);//15
                return;
            }
            if (mReceiverControlChannel != null)
            {
                mReceiverControlChannel.mLaunchRequestTracker.checkTimeout(time, 15);
                mReceiverControlChannel.mStopSessionRequestTracker.checkTimeout(time, 15);
                mReceiverControlChannel.mGetStatusRequestTracker.checkTimeout(time, 15);
                mReceiverControlChannel.mSetVolumeRequestTracker.checkTimeout(time, 0);
                mReceiverControlChannel.mSetMuteRequestTracker.checkTimeout(time, 0);
            }
            mHandler.postDelayed(mHeartbeatRunnable, 1000L);
        }

    };

    private boolean mIsConnecting;
    private boolean mIsConnected;
    private boolean mDisposed;
    private int mDisconnectStatusCode;
    private long F;
    private final ReconnectStrategy mReconnectStrategy = new ReconnectStrategy();

    private final Runnable mReconnectRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (mReconnectStrategy.wasReconnecting())
            {
                log.d("in reconnect Runnable", new Object[0]);
                connectToDeviceInternal();
            }
        }

    };

    private String mLastApplicationId;
    private String mLastSessionId;
    private final LOG log = new LOG("FlingDeviceController");
    private Integer controlerId;
    private final Context mContext;
    private final Handler mHandler;
    private final FlingDevice mFlingDevice;
    private final IFlingSrvController mFlingSrvController;
    private final FlingSocket mFlingSocket;
    private AtomicLong h;

    private final IMsgSender mMsgSender = new IMsgSender() {

        @Override
        public long getId() {
            // TODO Auto-generated method stub
            return h.incrementAndGet();
        }

        @Override
        public void sendMessage(String namespace, String message, long id, String transportId) {
            // TODO Auto-generated method stub
            String transId;
            if (transportId == null)
                transId = getTransId();
            else
                transId = transportId;
            FlingDeviceService.sendTextMessage(mContext, FlingDeviceController.this, namespace,
                    message, id, transId);
        }

        @Override
        public void sendBinaryMessage(String nameSpace, byte[] message, long requestId, String transportId) {
            // TODO Auto-generated method stub
            String transId;
            if (transportId == null)
                transId = getTransId();
            else
                transId = transportId;
            FlingDeviceService.sendBinaryMessage(mContext, FlingDeviceController.this, nameSpace,
                    message, 0L, transId);

        }

    };

    private final ReceiverControlChannel mReceiverControlChannel = new ReceiverControlChannel(
            "receiver-0") {

        @Override
        protected void onRequestStatus(int j) {
            // TODO Auto-generated method stub
            mFlingSrvController.onRequestStatus(j);
        }

        @Override
        protected void onConnectToApplicationAndNotify(ApplicationInfo appInfo) {
            // TODO Auto-generated method stub
            connectToApplicationAndNotify(appInfo, true);
        }

        @Override
        protected void onStatusReceived(ApplicationInfo appInfo, double level, boolean muted) {
            // TODO Auto-generated method stub
            mLogs.d("onStatusReceived");
            mVolumeLevel = level;
            processReceiverStatus(FlingDeviceController.this, appInfo,
                    level, muted);
        }

        @Override
        protected void onApplicationDisconnected() {
            // TODO Auto-generated method stub
            int result;
            if (v)
                result = 0;
            else
                result = FlingStatusCodes.APPLICATION_NOT_RUNNING; // 2005;
            v = false;
            mLastApplicationId = null;
            mLastSessionId = null;
            onApplicationDisconnected_e(result);
        }

        @Override
        protected void onApplicationConnectionFailed(int result) {
            // TODO Auto-generated method stub
            mFlingSrvController.onApplicationConnectionFailed(result);
        }

        @Override
        protected void onStatusRequestFailed(int statusCode) {
            // TODO Auto-generated method stub
            mLogs.d("onStatusRequestFailed: statusCode=%d", statusCode);

            if (mApplicationId != null)
            {
                if (mReconnectStrategy.b())
                {
                    mLogs.d("calling Listener.onConnectedWithoutApp()");
                    mFlingSrvController.onConnectedWithoutApp();
                } else
                {
                    mFlingSrvController.onApplicationConnectionFailed(statusCode);
                }
                mApplicationId = null;
                mSessionId_y = null;
            }
        }

    };

    private final ConnectionControlChannel mConnectionControlChannel;
    private DeviceAuthChannel mDeviceAuthChannel;
    private HeartbeatChannel mHeartbeatChannel;
    private final Set mNamespaces = new HashSet();
    private final Map mFlingChannelMap = new HashMap();
    private final String p;
    private double mVolumeLevel;
    private ApplicationMetadata mApplicationMetadata;
    private String mSessionId;
    private String mStatusText;
    private boolean v;
    private boolean w;
    private String mApplicationId;
    private String mSessionId_y;
    private final long z = 10000L;

    private FlingDeviceController(Context context, Handler handler, FlingDevice device,
            String packageName, long debugLevel, IFlingSrvController axy1)
    {
        controlerId = new Integer(0);
        mContext = context;
        mHandler = handler;
        mFlingDevice = device;
        mFlingSrvController = axy1;
        setDebugLevel(debugLevel);
        mDisconnectStatusCode = 0;
        mFlingSocket = new FlingSocket(context, this);
        a(mReceiverControlChannel);
        mConnectionControlChannel = new ConnectionControlChannel(packageName);
        a(mConnectionControlChannel);
        Object aobj[] = new Object[2];
        aobj[0] = packageName;
        aobj[1] = Long.valueOf(ID.incrementAndGet());
        p = String.format("%s-%d", aobj);
    }

    static double setVolumeLevel(FlingDeviceController axs1, double volumeLevel)
    {
        axs1.mVolumeLevel = volumeLevel;
        return volumeLevel;
    }

    public static FlingDeviceController create(Context context,
            Handler handler, String packageName,
            FlingDevice device, long debugLevel, IFlingSrvController axy1)
    {
        FlingDeviceController controller = new FlingDeviceController(context, handler, device,
                packageName, debugLevel, axy1);
        controller.generateId();
        return controller;
    }

    public static FlingDeviceController create(Context context, Handler handler, String packageName,
            FlingDevice device, IFlingSrvController axy1)
    {
        return create(context, handler, packageName, device, 0L, axy1);
    }

    static IFlingSrvController getFlingSrvController(FlingDeviceController axs1)
    {
        return axs1.mFlingSrvController;
    }

    private void connectToApplicationAndNotify(ApplicationInfo applicationInfo, boolean flag)
    {
        log.d("connectToApplicationAndNotify");
        String transportId = applicationInfo.getTransportId();
        ApplicationMetadataPriv metaData;
        String displayName;
        List list;
        try
        {
            mConnectionControlChannel.connect(transportId);
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            mReceiverControlChannel.setTransportId(null);
            onSocketError(FlingStatusCodes.NETWORK_ERROR);  //7
            return;
        }
        log.d("setting current transport ID to %s", transportId);
        mReceiverControlChannel.setTransportId(transportId);
        metaData = ApplicationMetadata.getPrivateData(applicationInfo.getApplicationId());
        displayName = applicationInfo.getDisplayName();
        ApplicationMetadata.setName(metaData.mApplicationMetadata, displayName);
        list = applicationInfo.getAppImages();
        if (list != null)
        {
            WebImage webimage;
            for (Iterator iterator = list.iterator(); iterator.hasNext(); metaData.mApplicationMetadata
                    .addWebImage(webimage))
                webimage = (WebImage) iterator.next();

        }
        List list1 = applicationInfo.getNamespaces();
        ApplicationMetadata.setNamespaces(metaData.mApplicationMetadata, list1);
        PlatformChecker checker = applicationInfo.getPlatformChecker();
        if (checker != null)
        {
            String s3 = checker.mPackage;
            ApplicationMetadata.setSenderAppIdentifier(metaData.mApplicationMetadata, s3);
            android.net.Uri uri = checker.mUri;
            ApplicationMetadata.setSenderAppLaunchUrl(metaData.mApplicationMetadata, uri);
        }
        mStatusText = applicationInfo.getStatusText();
        mApplicationMetadata = metaData.mApplicationMetadata;
        mSessionId = applicationInfo.getSessionId();
        mLastApplicationId = applicationInfo.getApplicationId();
        mLastSessionId = mSessionId;
        if (mReconnectStrategy.b()) {
            mHandler.removeCallbacks(mReconnectRunnable);
            mFlingSrvController.onConnected();
        } else {
            mFlingSrvController.onApplicationConnected(mApplicationMetadata, mStatusText,
                    mSessionId,
                    flag);
        }
    }

    static void onApplicationDisconnected(FlingDeviceController axs1, int statusCode)
    {
        axs1.onApplicationDisconnected_e(statusCode);
    }

    static void a(FlingDeviceController axs1, ApplicationInfo atn1)
    {
        axs1.connectToApplicationAndNotify(atn1, true);
    }

    static void processReceiverStatus(FlingDeviceController flingDeviceController,
            ApplicationInfo applicationInfo, double volume, boolean muteState)
    {
        flingDeviceController.log.d("processReceiverStatus: applicationInfo=%s, volume=%f, muteState=%b", applicationInfo, volume, muteState);
        String statusText;
        if (applicationInfo != null)
            statusText = applicationInfo.getStatusText();
        else
            statusText = null;
        flingDeviceController.mStatusText = statusText;
        flingDeviceController.mFlingSrvController.onVolumeChanged(flingDeviceController.mStatusText, volume, muteState);
        if (flingDeviceController.mApplicationId != null)
        {
            String applicationId;
            LOG avu2;
            Object aobj1[];
            String s5;
            if (applicationInfo != null)
                applicationId = applicationInfo.getApplicationId();
            else
                applicationId = null;
            if (flingDeviceController.mApplicationId.equals("") || flingDeviceController.mApplicationId.equals(applicationId)) {
                if (flingDeviceController.mSessionId_y != null
                        && !flingDeviceController.mSessionId_y.equals(applicationInfo.getSessionId()))
                {
                    flingDeviceController.mApplicationId = null;
                    flingDeviceController.mSessionId_y = null;
                    if (flingDeviceController.w)
                    {
                        flingDeviceController.w = false;
                        flingDeviceController.launchApplicationInternal(flingDeviceController.mApplicationId, ((String) (null)), true);
                        return;
                    } else
                    {
                        flingDeviceController.mFlingSrvController
                                .onApplicationConnectionFailed(FlingStatusCodes.APPLICATION_NOT_RUNNING);// 2005
                        return;
                    }
                }
                String s3;
                if (applicationInfo != null)
                    s3 = applicationInfo.getStatusText();
                else
                    s3 = null;
                flingDeviceController.mStatusText = s3;
                if (applicationInfo != null
                        && !TextUtils.isEmpty(applicationInfo.getTransportId()))
                {
                    flingDeviceController.connectToApplicationAndNotify(applicationInfo, false);
                } else
                {
                    IFlingSrvController controller = flingDeviceController.mFlingSrvController;
                    char result;
                    if ("".equals(flingDeviceController.mApplicationId))
                        result = '\u07D5';
                    else
                        result = '\u07D4';
                    controller.onApplicationConnectionFailed(result);
                }
                flingDeviceController.mApplicationId = null;
                flingDeviceController.mSessionId_y = null;
                flingDeviceController.w = false;
                return;
            }
            flingDeviceController.log.d("application to join (%s) is NOT available!", flingDeviceController.mApplicationId);
            s5 = flingDeviceController.mApplicationId;
            flingDeviceController.mApplicationId = null;
            flingDeviceController.mSessionId_y = null;
            flingDeviceController.mLastApplicationId = null;
            flingDeviceController.mLastSessionId = null;
            if (!flingDeviceController.w) {
                if (flingDeviceController.mReconnectStrategy.b())
                {
                    flingDeviceController.mFlingSrvController.onConnectedWithoutApp();
                    return;
                } else
                {
                    flingDeviceController.log
                            .d("calling mListener.onApplicationConnectionFailed", new Object[0]);
                    flingDeviceController.mFlingSrvController
                            .onApplicationConnectionFailed(FlingStatusCodes.APPLICATION_NOT_RUNNING);// 2005
                    return;
                }
            }
            flingDeviceController.w = false;
            flingDeviceController.launchApplicationInternal(s5, ((String) (null)), true);
        } else {
            flingDeviceController.log.d(
                    "processReceiverStatus_a:ignore status messages for application id is null!");
        }
        return;
    }

    private void sendMessage(ByteBuffer bytebuffer, String namespace, long id)
            throws IOException
    {
        try
        {
            mFlingSocket.send(bytebuffer);
            mFlingSrvController.onRequestCallback(namespace, id);
            return;
        } catch (C_atq atq1)
        {
            mFlingSrvController.onRequestCallback(namespace, id,
                    FlingStatusCodes.MESSAGE_SEND_BUFFER_TOO_FULL); // 2007
            return;
        } catch (C_aue aue1)
        {
            mFlingSrvController.onRequestCallback(namespace, id, FlingStatusCodes.MESSAGE_TOO_LARGE); // 2006
        }
    }

    private void handleConnectionFailure(boolean flag)
    {
        mIsConnecting = false;
        boolean wasReconnecting = mReconnectStrategy.wasReconnecting();
        log.d("handleConnectionFailure; wasReconnecting=%b", wasReconnecting);
        if (flag)
        {
            long l1 = mReconnectStrategy.d();
            if (l1 >= 0L)
            {
                mHandler.postDelayed(mReconnectRunnable, l1);
                return;
            }
        } else
        {
            mReconnectStrategy.b();
        }
        if (wasReconnecting)
        {
            mFlingSrvController.onDisconnected(mDisconnectStatusCode);
            mDisconnectStatusCode = 0;
            return;
        } else
        {
            mFlingSrvController.onConnectionFailed();
            return;
        }
    }

    static boolean b(FlingDeviceController axs1)
    {
        return axs1.v;
    }

    static boolean c(FlingDeviceController axs1)
    {
        axs1.v = false;
        return false;
    }

    static String resetLastAppId(FlingDeviceController axs1)
    {
        axs1.mLastApplicationId = null;
        return null;
    }

    static String resetLastSessionId(FlingDeviceController axs1)
    {
        axs1.mLastSessionId = null;
        return null;
    }

    private void onApplicationDisconnected_e(int statusCode)
    {
        if (mReceiverControlChannel.mTransportId != null)
        {
            try
            {
                mConnectionControlChannel.close(mReceiverControlChannel.mTransportId);
                // } catch (IOException ioexception)
            } catch (Exception ioexception)
            {
                log.w(ioexception, "Error while leaving application");
                onSocketError(FlingStatusCodes.NETWORK_ERROR); //7
            }
            mReceiverControlChannel.setTransportId(null);
        }
        if (mApplicationMetadata != null)
        {
            mApplicationMetadata = null;
            mSessionId = null;
        }
        mFlingSrvController.onApplicationDisconnected(statusCode);
    }

    static String getApplicationId(FlingDeviceController axs1)
    {
        return axs1.mApplicationId;
    }

    private void finishDisconnecting(int socketError)
    {
        log.d("finishDisconnecting; socketError=%d, mDisconnectStatusCode=%d", socketError, mDisconnectStatusCode);
        mIsConnecting = false;
        mIsConnected = false;
        mVolumeLevel = 0.0D;
        mApplicationMetadata = null;
        mSessionId = null;
        mStatusText = null;
        mHandler.removeCallbacks(mHeartbeatRunnable);
        int disconnectStatusCode;
        if (mDisconnectStatusCode != 0)
        {
            disconnectStatusCode = mDisconnectStatusCode;
            mDisconnectStatusCode = FlingStatusCodes.SUCCESS;// 0;
        } else if (socketError == 3)
            disconnectStatusCode = FlingStatusCodes.TIMEOUT;// ;15;
        else if (socketError == 4)
            disconnectStatusCode = FlingStatusCodes.AUTHENTICATION_FAILED;// 2000;
        else
            disconnectStatusCode = FlingStatusCodes.NETWORK_ERROR;// 7;
        mReconnectStrategy.b();
        mFlingSrvController.onDisconnected(disconnectStatusCode);
    }

    static ReconnectStrategy g(FlingDeviceController axs1)
    {
        return axs1.mReconnectStrategy;
    }

    private void onSocketError(int socketError)
    {
        if (mFlingSocket.isConnected())
        {
            mDisconnectStatusCode = socketError;
            mFlingSocket.disconnect();
            return;
        } else
        {
            finishDisconnecting(socketError);
            return;
        }
    }

    static String resetApplicationId(FlingDeviceController axs1)
    {
        axs1.mApplicationId = null;
        return null;
    }

    static String resetSessionId(FlingDeviceController axs1)
    {
        axs1.mSessionId_y = null;
        return null;
    }

    static HeartbeatChannel getHeartbeatChannel(FlingDeviceController axs1)
    {
        return axs1.mHeartbeatChannel;
    }

    static LOG getLogs(FlingDeviceController axs1)
    {
        return axs1.log;
    }

    static void onSocketError_l(FlingDeviceController axs1)
    {
        axs1.onSocketError(FlingStatusCodes.TIMEOUT);//15
    }

    static ReceiverControlChannel getReceiverControlChannel(FlingDeviceController axs1)
    {
        return axs1.mReceiverControlChannel;
    }

    static Runnable getHeartbeatRunnable(FlingDeviceController axs1)
    {
        return axs1.mHeartbeatRunnable;
    }

    static Handler getHandler(FlingDeviceController axs1)
    {
        return axs1.mHandler;
    }

    static DeviceAuthChannel getDeviceAuthChannel(FlingDeviceController axs1)
    {
        return axs1.mDeviceAuthChannel;
    }

    static DeviceAuthChannel resetDeviceAuthChannel(FlingDeviceController axs1)
    {
        axs1.mDeviceAuthChannel = null;
        return null;
    }

    static void finishConnecting(FlingDeviceController flingDeviceController)
    {
        flingDeviceController.log.d("finishConnecting");
        flingDeviceController.h = new AtomicLong(0L);
        try
        {
            flingDeviceController.mConnectionControlChannel.connect("receiver-0");
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            flingDeviceController.onSocketError(FlingStatusCodes.NETWORK_ERROR);//7
            return;
        }
        flingDeviceController.mHeartbeatChannel = new HeartbeatChannel();
        flingDeviceController.a(flingDeviceController.mHeartbeatChannel);
        flingDeviceController.mHandler.postDelayed(flingDeviceController.mHeartbeatRunnable, 1000L);
        flingDeviceController.mIsConnected = true;
        flingDeviceController.mIsConnecting = false;
        if (flingDeviceController.mLastApplicationId != null && flingDeviceController.mLastSessionId != null)
        {
            flingDeviceController.w = false;
            flingDeviceController.joinApplicationInternal(flingDeviceController.mLastApplicationId, flingDeviceController.mLastSessionId);
            return;
        }
        flingDeviceController.mReconnectStrategy.b();
        flingDeviceController.mFlingSrvController.onConnected();
        try
        {
            flingDeviceController.mReceiverControlChannel.getStatus();
            return;
            // } catch (IOException ioexception1)
        } catch (Exception ioexception1)
        {
            flingDeviceController.onSocketError(FlingStatusCodes.NETWORK_ERROR); // 7
        }
    }

    static void handleConnectionFailure_s(FlingDeviceController axs1)
    {
        axs1.handleConnectionFailure(false);
    }

    static Context getContext(FlingDeviceController axs1)
    {
        return axs1.mContext;
    }

    static AtomicLong u(FlingDeviceController axs1)
    {
        return axs1.h;
    }

    public final void onConnected()
    {
        FlingDeviceService.onSocketConnected(mContext, this);
    }

    public final void setVolume(double volume, double expected_level, boolean flag)
    {
        FlingDeviceService.setVolume(mContext, this, volume, expected_level, flag);
    }

    public final void onConnectionFailed(int socketError)
    {
        log.d("onConnectionFailed; socketError=%d", socketError);
        FlingDeviceService.onSocketConnectionFailed(mContext, this, socketError);
    }

    public final void setDebugLevel(long l1)
    {
        if (F == l1)
            return;
        F = l1;
        boolean flag;
        if ((1L & F) != 0L)
            flag = true;
        else
            flag = false;
        log.setDebugEnabled(flag);
        LOG.setDebugEnabledByDefault(flag);
    }

    public final void a(FlingChannel flingChannel)
    {
        flingChannel.a(mMsgSender);
        mFlingChannelMap.put(flingChannel.nameSpace, flingChannel);
    }

    public final void setSubTag(String tag)
    {
        log.setSubTag(tag);
    }

    public final void reconnectToDevice(String lastApplicationId, String lastSessionId)
    {
        log.d("reconnectToDevice: lastApplicationId=%s, lastSessionId=%s", lastApplicationId, lastSessionId);
        mLastApplicationId = lastApplicationId;
        mLastSessionId = lastSessionId;
        connectDevice();
    }

    public final void sendMessage_a(String namespace, String message, long id)
    {
        FlingDeviceService.sendTextMessage(mContext, this, namespace, message, id,
                mReceiverControlChannel.mTransportId);
    }

    public final void sendTextMessage(String namespace, String message, long id, String transId)
    {
        if (TextUtils.isEmpty(transId))
        {
            log.w("ignoring attempt to send a text message with no destination ID",
                    new Object[0]);
            mFlingSrvController.onRequestCallback(namespace, id, FlingStatusCodes.INVALID_REQUEST); // 2001
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
                log.w(ioexception, "Error while sending message", new Object[0]);
            }
            onSocketError(FlingStatusCodes.NETWORK_ERROR); // 7
            return;
        }
        byte abyte0[] = (new C_axm()).protocolVersion(0).sourceId(p).destinationId(transId).namespace(namespace).payloadType(0).payloadMessage(message).build();
        try {
            if (abyte0.length > 0x10000)
            {
                throw new C_aue();
            } else
            {
                sendMessage(ByteBuffer.wrap(abyte0), namespace, id);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void launchApplication(String applicationId, String sessionId,
            boolean relaunchIfRunning)
    {
        FlingDeviceService.launchApplication(mContext, this, applicationId, sessionId,
                relaunchIfRunning);
    }

    public final void sendBinaryMessage(String namespace, byte message[], long requestId)
    {
        FlingDeviceService.sendBinaryMessage(mContext, this, namespace, message, requestId,
                mReceiverControlChannel.mTransportId);
    }

    public final void sendBinaryMessage(String nameSpace, byte message[], long requestId, String transId)
            throws IOException
    {
        if (TextUtils.isEmpty(transId))
        {
            log.w("ignoring attempt to send a binary message with no destination ID",
                    new Object[0]);
            mFlingSrvController.onRequestCallback(nameSpace, requestId, FlingStatusCodes.INVALID_REQUEST); // 2001
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
                log.w(ioexception, "Error while sending message");
            }
            onSocketError(FlingStatusCodes.NETWORK_ERROR); //7
            return;
        }
        sendMessage(
                ByteBuffer.wrap((new C_axm()).protocolVersion(0).sourceId(p).destinationId(transId).namespace(nameSpace).payloadType(1)
                        .payloadBinary(BinaryPayload.a(message)).build()),
                nameSpace, requestId);
        return;
    }

    public final void onMessageReceived(ByteBuffer message)
    {
        FlingDeviceService.procReceivedMessage(mContext, this, message);
    }

    public final void setMute(boolean flag, double d1, boolean flag1)
    {
        FlingDeviceService.setMute(mContext, this, flag, d1, flag1);
    }

    public final void connectDevice()
    {
        if (mReconnectStrategy.wasReconnecting())
        {
            log.d("already reconnecting; ignoring");
            return;
        } else
        {
            log.d("calling FlingMediaRouteProviderService.connectToDevice");
            FlingDeviceService.connectFlingDevice(mContext, this);
            return;
        }
    }

    public final void setVolume_b(double level, double expected_level, boolean muted)
    {
        try
        {
            mReceiverControlChannel.setVolume(level, expected_level, muted);
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            log.w(ioexception, "Error while setting volume");
        }
        onSocketError(FlingStatusCodes.NETWORK_ERROR); // 7
    }

    public final void onDisconnected(int i1)
    {
        FlingDeviceService.onSocketDisconnected(mContext, this, i1);
        if (mHeartbeatChannel != null)
        {
            b(((FlingChannel) (mHeartbeatChannel)));
            mHeartbeatChannel = null;
        }
        ReceiverControlChannel aun1 = mReceiverControlChannel;
        aun1.mTransportId = null;
        aun1.mLaunchRequestTracker.a();
        aun1.mStopSessionRequestTracker.a();
        aun1.mGetStatusRequestTracker.a();
        aun1.mSetVolumeRequestTracker.a();
        aun1.mSetMuteRequestTracker.a();
        aun1.mLevel = 0.0D;
        aun1.mMuted = false;
        aun1.mFirst = false;
    }

    public final void b(FlingChannel avn1)
    {
        avn1.a((IMsgSender) null);
        mFlingChannelMap.remove(avn1.nameSpace);
    }

    public final void stopApplication(String s1)
    {
        FlingDeviceService.stopApplication(mContext, this, s1);
    }

    public final void joinApplication(String applicationId, String sessionId)
    {
        FlingDeviceService.joinApplication(mContext, this, applicationId, sessionId);
    }

    public final void launchApplicationInternal(String appId, String param, boolean relaunch)
    {
        log.d("launchApplicationInternal() id=%s, relaunch=%b", appId, relaunch);
        if (appId == null || appId.equals(""))
        {
            mFlingSrvController
                    .onApplicationConnectionFailed(FlingStatusCodes.APPLICATION_NOT_FOUND); // 2004
            return;
        }
        if (relaunch)
        {
            try
            {
                mReceiverControlChannel.launchApplication(appId, param);
                return;
                // } catch (IOException ioexception)
            } catch (Exception ioexception)
            {
                log.w(ioexception, "Error while launching application");
            }
            onSocketError(FlingStatusCodes.NETWORK_ERROR);//7
            return;
        } else
        {
            w = true;
            joinApplicationInternal(appId, null);
            return;
        }
    }

    public final void onReceivedMessage(ByteBuffer received) {
        if (mHeartbeatChannel != null)
            mHeartbeatChannel.reset();
        C_axm axm1 = null;
        String nameSpace;
        try {
            axm1 = C_axm.a(received.array());
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
            log.d("Received an unparseable protobuf???");
        }
        nameSpace = axm1.getNamespace();
        if (TextUtils.isEmpty(nameSpace)) {
            log.d("Received a message with an empty or missing namespace");
            return;
        }
        int payloadType = axm1.getPayloadType();
        FlingChannel flingChannel = (FlingChannel) mFlingChannelMap.get(nameSpace);
        if (flingChannel != null) {
            BinaryPayload binaryPayload;
            switch (payloadType) {
                default:
                    LOG avu3 = log;
                    Object aobj2[] = new Object[1];
                    aobj2[0] = Integer.valueOf(payloadType);
                    avu3.w("Unknown payload type %s; discarding message", aobj2);
                    return;

                case 0: // '\0'
                    flingChannel.checkReceivedMessage(axm1.getMessage());
                    return;

                case 1: // '\001'
                    binaryPayload = axm1.getBinaryMessage();
                    break;
            }
            byte abyte1[] = new byte[binaryPayload.getLength()];
            binaryPayload.copy(abyte1);
            flingChannel.onReceivedMessage(abyte1);
            return;
        }
        boolean flag;
        synchronized (mNamespaces) {
            flag = mNamespaces.contains(nameSpace);
        }
        if (flag) {
            BinaryPayload igp1;
            switch (payloadType) {
                default:
                    LOG avu2 = log;
                    Object aobj1[] = new Object[1];
                    aobj1[0] = Integer.valueOf(payloadType);
                    avu2.w("Unknown payload type %s; discarding message", aobj1);
                    return;

                case 0: // '\0'
                    mFlingSrvController.notifyOnMessageReceived(nameSpace, axm1.getMessage());
                    return;

                case 1: // '\001'
                    igp1 = axm1.getBinaryMessage();
                    break;
            }
            byte message[] = new byte[igp1.getLength()];
            igp1.copy(message);
            mFlingSrvController.onReceiveBinary(nameSpace, message);
            return;
        } else {
            log.w("Ignoring message. Namespace has not been registered.", new Object[0]);
            return;
        }
    }

    public final void setMute_b(boolean mute, double level, boolean isMuted)
    {
        try
        {
            mReceiverControlChannel.setMute(mute, level, isMuted);
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            log.w(ioexception, "Error while setting mute state");
        }
        onSocketError(FlingStatusCodes.NETWORK_ERROR); // 7
    }

    public final void connectToDeviceInternal()
    {
        int socketState = mFlingSocket.getState();
        log.d("connectToDeviceInternal; socket state = %d", socketState);
        if (socketState == 1 || socketState == 2)
        {
            log.w("Redundant call to connect to device", new Object[0]);
            return;
        }
        mIsConnecting = true;
        if (!mReconnectStrategy.wasReconnecting())
            mReconnectStrategy.a();
        mReconnectStrategy.e();
        try
        {
            log.d("connecting socket now");
            mFlingSocket.connect(mFlingDevice.getIpAddress(), mFlingDevice.getServicePort());
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            log.w(ioexception, "connection exception");
        }
        handleConnectionFailure(true);
    }

    public final void onSocketConnectionFailedInternal(int socketError)
    {
        log.d("onSocketConnectionFailedInternal: socketError=%d", socketError);
        handleConnectionFailure(true);
    }

    public final void stopApplicationInternal(String sessionId)
    {
        log.d("stopApplicationInternal() sessionId=%s", sessionId);
        try
        {
            v = true;
            mReceiverControlChannel.stopSession(sessionId);
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            log.w(ioexception, "Error while stopping application");
        }
        onSocketError(FlingStatusCodes.NETWORK_ERROR); // 7
    }

    public final void joinApplicationInternal(String applicationId, String sessionId)
    {
        log.d("joinApplicationInternal(%s, %s)", applicationId, sessionId);
        if (mApplicationMetadata != null)
        {
            if (applicationId == null
                    || applicationId.equals(mApplicationMetadata.getApplicationId())
                    && (sessionId == null || sessionId.equals(mSessionId)))
            {
                log.d("already connected to requested app, so skipping join logic");
                mFlingSrvController.onApplicationConnected(mApplicationMetadata,
                        mStatusText,
                        mSessionId, false);
                return;
            }
            log.d("clearing mLastConnected* variables");
            mLastApplicationId = null;
            mLastSessionId = null;
            if (mReconnectStrategy.b())
            {
                mFlingSrvController.onConnectedWithoutApp();
                return;
            } else
            {
                mFlingSrvController
                        .onApplicationConnectionFailed(FlingStatusCodes.APPLICATION_NOT_RUNNING);// 2005
                return;
            }
        }
        if (applicationId == null)
            applicationId = "";
        mApplicationId = applicationId;
        mSessionId_y = sessionId;
        try
        {
            mReceiverControlChannel.getStatus();
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            log.w(ioexception, "Error while requesting device status for join");
        }
        onSocketError(FlingStatusCodes.NETWORK_ERROR); // 7
    }

    public final void onSocketDisconnectedInternal(int socketError)
    {
        log.d("onSocketDisconnectedInternal: socketError=%d", socketError);
        finishDisconnecting(socketError);
    }

    public final void setMessageReceivedCallbacks(String namespace)
    {
        FlingDeviceService.setMessageReceivedCallbacks(mContext, this, namespace);
    }

    public final boolean isConnected()
    {
        return mIsConnected;
    }

    public final void addNamespace(String namespace)
    {
        if (TextUtils.isEmpty(namespace))
            return;
        synchronized (mNamespaces)
        {
            mNamespaces.add(namespace);
        }
    }

    public final boolean isConnecting()
    {
        return mIsConnecting;
    }

    public final double getVolume()
    {
        return mVolumeLevel;
    }

    public final void removeMessageReceivedCallbacks(String namespace)
    {
        FlingDeviceService.removeMessageReceivedCallbacks(mContext, this, namespace);
    }

    public final String getStatusText()
    {
        return mStatusText;
    }

    public final void removeNamespace(String namespace)
    {
        if (TextUtils.isEmpty(namespace))
            return;
        synchronized (mNamespaces)
        {
            mNamespaces.remove(namespace);
        }
    }

    public final long h()
    {
        return F;
    }

    public final boolean isDisposed()
    {
        return mDisposed;
    }

    public final void leaveApplication()
    {
        FlingDeviceService.leaveApplication(mContext, this);
    }

    public final void leaveApplicationInternal()
    {
        log.d("leaveApplicationInternal()");
        if (mApplicationMetadata == null)
        {
            mFlingSrvController.onInvalidRequest();
            return;
        } else
        {
            onApplicationDisconnected_e(0);
            return;
        }
    }

    public final void requestStatus()
    {
        FlingDeviceService.requestStatus(mContext, this);
    }

    public final void getStatus()
    {
        try
        {
            mReceiverControlChannel.getStatus();
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            log.w(ioexception, "Error while stopping application");
        }

        onSocketError(FlingStatusCodes.NETWORK_ERROR); // 7
    }

    public final void onSocketConnectedInternal()
    {
        log.d("onSocketConnectedInternal");

        mDeviceAuthChannel = new DeviceAuthChannel("receiver-0",
                mFlingSocket.getPeerCertificate()) {

            @Override
            protected void verifyDevAuthResult(int result) {
                // TODO Auto-generated method stub
                b(mDeviceAuthChannel);
                mDeviceAuthChannel = null;
                if (result == 0)
                {
                    mLogs.d("authentication succeeded");
                    finishConnecting(FlingDeviceController.this);
                } else
                {
                    handleConnectionFailure_s(FlingDeviceController.this);
                }
            }

        };

        a(mDeviceAuthChannel);
        try
        {
            mDeviceAuthChannel.doDeviceAuth();
            return;
            // } catch (IOException ioexception)
        } catch (Exception ioexception)
        {
            log.w(ioexception, "Not able to authenticated Fling Device");
        }
        b(mDeviceAuthChannel);
        mDeviceAuthChannel = null;
        handleConnectionFailure(false);
    }

    public final String getTransId()
    {
        return mReceiverControlChannel.mTransportId;
    }

    public final void generateId()
    {
        synchronized (controlerId)
        {
            controlerId = Integer.valueOf(1 + controlerId.intValue());
        }
    }

    public final void releaseReference()
    {
        synchronized (controlerId) {
            boolean flag;
            if (controlerId.intValue() <= 0) {
                Object aobj[] = new Object[1];
                aobj[0] = Boolean.valueOf(mDisposed);
                log.e("unbalanced call to releaseReference(); mDisposed=%b", aobj);
                flag = false;
            } else {
                Integer integer;
                integer = Integer.valueOf(-1 + controlerId.intValue());
                controlerId = integer;
                if (integer.intValue() == 0)
                    flag = true;
                else
                    flag = false;
            }
    
            if (flag) {
                mDisposed = true;
    
                log.d("[%s] *** disposing ***", mFlingDevice);
                mReconnectStrategy.b();
                mHandler.removeCallbacks(mHeartbeatRunnable);
                mHandler.removeCallbacks(mReconnectRunnable);
                if (mFlingSocket.isConnected()) {
                    mFlingSocket.disconnect();
                }
            }
        }
    }
}
