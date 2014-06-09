
package com.fireflycast.server.cast.service;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.fireflycast.server.cast.CastDeviceController;
import com.fireflycast.server.cast.CastMediaRouteProvider;
import com.fireflycast.server.cast.MediaRouteProviderSrv;
import com.fireflycast.server.cast.MediaRouteProvider;
import com.fireflycast.server.cast.mdns.DeviceScanner;
import com.fireflycast.server.cast.service.operation.CastDeviceScannerOperation;
import com.fireflycast.server.cast.service.operation.CastOperation;
import com.fireflycast.server.utils.AndroidUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author jianminz
 */
public class CastDeviceService extends MediaRouteProviderSrv {
    public final MediaRouteProvider getInstance_a() {
        return CastMediaRouteProvider.getInstance_a(this);
    }

    // cast operations
    private static final ConcurrentLinkedQueue mCastOperationQueue = new ConcurrentLinkedQueue();

    public static void connectCast(Context context, final CastDeviceController controller)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.connectToDeviceInternal_c();
            }
        });
    }

    public static void setVolume(Context context, final CastDeviceController controller,
            final double level,
            final double expected_level, final boolean muted)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.setVolume_b(level, expected_level, muted);
            }

        });
    }

    public static void onSocketConnectionFailed(Context context,
            final CastDeviceController controller, final int socketError)
    {
        connectToDevice(context,
                new CastOperation(controller) {

                    @Override
                    public void doCast_a() throws IOException {
                        // TODO Auto-generated method stub
                        controller.onSocketConnectionFailedInternal_c(socketError);
                    }

                });
    }

    public static void stopApplication(Context context, final CastDeviceController controller,
            final String s)
    {
        connectToDevice(context,
                new CastOperation(controller) {

                    @Override
                    public void doCast_a() throws IOException {
                        // TODO Auto-generated method stub
                        controller.stopApplicationInternal_c(s);
                    }

                });
    }

    public static void joinApplication(Context context, final CastDeviceController controller,
            final String applicationId, final String sessionId)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.joinApplicationInternal_c(applicationId, sessionId);
            }

        });
    }

    public static void sendMessage(Context context, final CastDeviceController controller,
            final String namespace,
            final String message, final long id, final String transportId)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                try
                {
                    Log.e("CastDeviceService", "sendMessage: namespace:" + namespace + " message:" + message + " id:" + id + " transportId:" + transportId);
                    controller.sendMessage_a(namespace, message, id, transportId);
                    return;
                } catch (IllegalStateException e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }

        });
    }

    public static void launchApplication(Context context, final CastDeviceController controller,
            final String s,
            final String s1, final boolean flag)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.launchApplicationInternal_b(s, s1, flag);
            }

        });
    }

    public static void sendBinaryMessage(Context context, final CastDeviceController controller,
            final String namespace,
            final byte abyte0[], final long l, final String transId)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                try
                {
                    controller.sendBinaryMessage_a(namespace, abyte0, l, transId);
                    return;
                }
                catch (IllegalStateException illegalstateexception)
                {
                    Log.e(TAG, illegalstateexception.getMessage());
                }
            }

        });
    }

    public static void connectToDevice(Context context, final CastDeviceController controller,
            final ByteBuffer bytebuffer)
    {
        // connectToDevice(context, ((CastOperation_aya) (new C_ayl(axs,
        // bytebuffer))));
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.onReceivedMessage_b(bytebuffer);
            }

        });
    }

    public static void setMute(Context context, final CastDeviceController controller,
            final boolean flag,
            final double d1, final boolean flag1)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.setMute_b(flag, d1, flag1);
            }

        });
    }

    private static void connectToDevice(Context context, CastOperation operation)
    {
        mCastOperationQueue.offer(operation);
        context.startService(AndroidUtils.buildIntent_f(context,
                "com.fireflycast.cast.service.INTENT"));
    }

    public static void leaveApplication_b(Context context, final CastDeviceController controller)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.leaveApplicationInternal_k();
            }

        });
    }

    public static void onSocketDisconnected_b(Context context, final CastDeviceController controller,
            final int i)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub

                controller.onSocketDisconnectedInternal_d(i);
            }

        });
    }

    public static void setMessageReceivedCallbacks_b(Context context,
            final CastDeviceController controller,
            final String namespace)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.addNamespace_e(namespace);
            }

        });
    }

    public static void requestStatus_c(Context context, final CastDeviceController controller)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.getStatus_m();
            }

        });
    }

    public static void removeMessageReceivedCallbacks_c(Context context,
            final CastDeviceController controller, final String namespace)
    {
        connectToDevice(context,
                new CastOperation(controller) {

                    @Override
                    public void doCast_a() throws IOException {
                        // TODO Auto-generated method stub
                        controller.removeNamespace_g(namespace);
                    }

                });
    }

    public static void onSocketConnected_d(Context context, final CastDeviceController controller)
    {
        connectToDevice(context, new CastOperation(controller) {

            @Override
            public void doCast_a() throws IOException {
                // TODO Auto-generated method stub
                controller.onSocketConnectedInternal_n();
            }

        });
    }

    private static final ConcurrentLinkedQueue mDeviceScanOperationQueue_a = new ConcurrentLinkedQueue();

    public static void startScanCastDevice_a(Context context, final DeviceScanner deviceScanner)
    {
        addScanOperation_a(context,
                new CastDeviceScannerOperation(deviceScanner) {

                    @Override
                    public void act_a() {
                        // TODO Auto-generated method stub
                        deviceScanner.onAllDevicesOffline_d();
                        deviceScanner.startScan_a();
                    }

                });
    }

    private static void addScanOperation_a(Context context, CastDeviceScannerOperation operation)
    {
        mDeviceScanOperationQueue_a.offer(operation);
        context.startService(AndroidUtils
                .buildIntent_f(context, "com.fireflycast.cast.service.DEVICE_SCANNER_INTENT"));
    }

    public static void stopScanCastDevice_b(Context context, final DeviceScanner deviceScanner)
    {
        addScanOperation_a(context, new CastDeviceScannerOperation(deviceScanner) {

            @Override
            public void act_a() {
                // TODO Auto-generated method stub
                deviceScanner.stopScan_b();
            }

        });
    }

    protected void onHandleIntent(Intent intent)
    {
        if (intent == null) {
            Log.e("CastIntentService", "intent is null.ignore it!!!!");
        }

        String action = intent.getAction();
        if (action.equals("com.fireflycast.cast.service.INTENT")) {
            doCastOperations();
        } else if (action.equals("com.fireflycast.cast.service.DEVICE_SCANNER_INTENT")) {
            doDeviceScanOperations();
        } else {
            Log.e("CastService", "unknown actions!!!![" + intent + "]");
        }
    }

    private void doDeviceScanOperations() {
        CastDeviceScannerOperation operation;
        long l;
        operation = (CastDeviceScannerOperation) mDeviceScanOperationQueue_a.poll();
        if (operation == null) {
            Log.e("CastDeviceScannerIntentService", "operation missing");
            return;
        }
        l = SystemClock.elapsedRealtime();
        Log.d("CastDeviceScannerIntentService", (new StringBuilder("Starting operation: >> "))
                .append(operation.getClass().getSimpleName()).toString());

        try {
            operation.act_a();
        } catch (Exception e) {
            e.printStackTrace();
        }

        long l1 = SystemClock.elapsedRealtime() - l;
        Log.d("CastDeviceScannerIntentService",
                (new StringBuilder("Finished operation: << "))
                        .append(operation.getClass().getSimpleName()).append(" (").append(l1)
                        .append("ms elapsed)").toString());
    }

    private void doCastOperations() {
        CastOperation operation = (CastOperation) mCastOperationQueue.poll();
        if (operation == null)
        {
            Log.e("CastIntentService", "operation missing");
            return;
        }
        try
        {
            operation.doCast_a();
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            Log.e("CastIntentService", "begin call releaseReference");
            operation.releaseReference_b();
            Log.e("CastIntentService", "after call releaseReference");
        }
    }
}
