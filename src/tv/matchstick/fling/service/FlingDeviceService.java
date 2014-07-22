package tv.matchstick.fling.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import tv.matchstick.server.fling.FlingDeviceController;
import tv.matchstick.server.fling.FlingMediaRouteProvider;
import tv.matchstick.server.fling.MediaRouteProvider;
import tv.matchstick.server.fling.MediaRouteProviderSrv;
import tv.matchstick.server.fling.mdns.DeviceScanner;
import tv.matchstick.server.fling.service.operation.FlingDeviceScannerOperation;
import tv.matchstick.server.fling.service.operation.FlingOperation;
import tv.matchstick.server.utils.AndroidUtils;

/**
 * This class is used to do several things:
 *
 * <ul>
 * <li>all fling related operations: connect, start/join/stop application,send
 * messages,etc
 * <li>fling device scan operations: start, stop device scans
 * <li>contained something for media route device found
 * </ul>
 */
public class FlingDeviceService extends MediaRouteProviderSrv {
	private static final String MEDIA_ROUTE_ACTION = "android.media.MediaRouteProviderService";

	private static final String SERVICE_ACTION_KEY = "what"; // see AndroidUtils.buildIntent
	private static final String SERVICE_ACTION_FLING = "fling"; // fling action
	private static final String SERVICE_ACTION_SCAN  = "scan";  // device scan

	public final MediaRouteProvider getInstance() {
		return FlingMediaRouteProvider.getInstance(this);
	}

	/**
	 * Fling operation queue(launch application,stop application,etc)
	 */
	private static final ConcurrentLinkedQueue<FlingOperation> mFlingOperationQueue = new ConcurrentLinkedQueue<FlingOperation>();

	/**
	 * Connect to fling device by using its IP and port
	 * 
	 * @param context
	 * @param controller
	 */
	public static void connectFlingDevice(Context context,
			final FlingDeviceController controller) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.connectToDeviceInternal();
			}
		});
	}

	/**
	 * Set fling device's volume
	 * 
	 * @param context
	 * @param controller
	 * @param level
	 * @param expected_level
	 * @param muted
	 */
	public static void setVolume(Context context,
			final FlingDeviceController controller, final double level,
			final double expected_level, final boolean muted) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.setVolume_b(level, expected_level, muted);
			}

		});
	}

	/**
	 * Do something when connecting to fling device failed by using fling socket
	 * 
	 * @param context
	 * @param controller
	 * @param socketError
	 * @see FlingSocket, FlingDeviceController
	 */
	public static void onSocketConnectionFailed(Context context,
			final FlingDeviceController controller, final int socketError) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.onSocketConnectionFailedInternal(socketError);
			}

		});
	}

	/**
	 * Stop fling application
	 * 
	 * @param context
	 * @param controller
	 * @param sessionId
	 */
	public static void stopApplication(Context context,
			final FlingDeviceController controller, final String sessionId) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.stopApplicationInternal(sessionId);
			}

		});
	}

	/**
	 * Join fling application
	 * 
	 * @param context
	 * @param controller
	 * @param applicationId
	 * @param sessionId
	 */
	public static void joinApplication(Context context,
			final FlingDeviceController controller, final String applicationId,
			final String sessionId) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.joinApplicationInternal(applicationId, sessionId);
			}

		});
	}

	/**
	 * Send Text messages to fling device
	 * 
	 * @param context
	 * @param controller
	 * @param namespace
	 * @param message
	 * @param id
	 * @param transportId
	 */
	public static void sendTextMessage(Context context,
			final FlingDeviceController controller, final String namespace,
			final String message, final long id, final String transportId) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				try {
					Log.e("FlingDeviceService", "sendMessage: namespace:"
							+ namespace + " message:" + message + " id:" + id
							+ " transportId:" + transportId);
					controller.sendTextMessage(namespace, message, id,
							transportId);
					return;
				} catch (IllegalStateException e) {
					Log.e(TAG, e.getMessage());
				}
			}

		});
	}

	/**
	 * Launch fling application
	 * 
	 * @param context
	 * @param controller
	 * @param applicationId
	 * @param param
	 * @param relaunch
	 */
	public static void launchApplication(Context context,
			final FlingDeviceController controller, final String applicationId,
			final String param, final boolean relaunch) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.launchApplicationInternal(applicationId, param,
						relaunch);
			}

		});
	}

	/**
	 * Send Binary message to fling device
	 * 
	 * @param context
	 * @param controller
	 * @param namespace
	 * @param message
	 * @param requestId
	 * @param transId
	 */
	public static void sendBinaryMessage(Context context,
			final FlingDeviceController controller, final String namespace,
			final byte message[], final long requestId, final String transId) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				try {
					controller.sendBinaryMessage(namespace, message, requestId,
							transId);
					return;
				} catch (IllegalStateException illegalstateexception) {
					Log.e(TAG, illegalstateexception.getMessage());
				}
			}

		});
	}

	/**
	 * Received message from fling device
	 * 
	 * @param context
	 * @param controller
	 * @param message
	 */
	public static void procReceivedMessage(Context context,
			final FlingDeviceController controller, final ByteBuffer message) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.onReceivedMessage(message);
			}

		});
	}

	/**
	 * Mute the fling device
	 * 
	 * @param context
	 * @param controller
	 * @param mute
	 * @param level
	 * @param isMuted
	 */
	public static void setMute(Context context,
			final FlingDeviceController controller, final boolean mute,
			final double level, final boolean isMuted) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.setMute_b(mute, level, isMuted);
			}

		});
	}

	/**
	 * Start fling service to do fling operation(launch application,etc)
	 * 
	 * @param context
	 * @param operation
	 *            fling operation
	 */
	private static void startFlingService(Context context,
			FlingOperation operation) {
		mFlingOperationQueue.offer(operation);
		context.startService(AndroidUtils.buildIntent(context,
				MEDIA_ROUTE_ACTION, SERVICE_ACTION_FLING));
	}

	/**
	 * Leave fling application
	 * 
	 * @param context
	 * @param controller
	 */
	public static void leaveApplication(Context context,
			final FlingDeviceController controller) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.leaveApplicationInternal();
			}

		});
	}

	/**
	 * Process fling socket disconnected event
	 * 
	 * @param context
	 * @param controller
	 * @param socketError
	 *            socket error code
	 */
	public static void onSocketDisconnected(Context context,
			final FlingDeviceController controller, final int socketError) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub

				controller.onSocketDisconnectedInternal(socketError);
			}

		});
	}

	/**
	 * Process message received callback. only add it into our namespace list?
	 * 
	 * @param context
	 * @param controller
	 * @param namespace
	 */
	public static void setMessageReceivedCallbacks(Context context,
			final FlingDeviceController controller, final String namespace) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.addNamespace(namespace);
			}

		});
	}

	/**
	 * Request current fling device's status
	 * 
	 * @param context
	 * @param controller
	 */
	public static void requestStatus(Context context,
			final FlingDeviceController controller) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.getStatus();
			}

		});
	}

	/**
	 * Process remove message received callback event. Just remove namespace
	 * from list?
	 * 
	 * @param context
	 * @param controller
	 * @param namespace
	 */
	public static void removeMessageReceivedCallbacks(Context context,
			final FlingDeviceController controller, final String namespace) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.removeNamespace(namespace);
			}

		});
	}

	/**
	 * Do something when connected with fling socket. (device auth,etc)
	 * 
	 * @param context
	 * @param controller
	 */
	public static void onSocketConnected(Context context,
			final FlingDeviceController controller) {
		startFlingService(context, new FlingOperation(controller) {

			@Override
			public void doFling() throws IOException {
				// TODO Auto-generated method stub
				controller.onSocketConnectedInternal();
			}

		});
	}

	/**
	 * Device scan operation queue(start scan device, stop scan device,etc)
	 */
	private static final ConcurrentLinkedQueue<FlingDeviceScannerOperation> mDeviceScanOperationQueue = new ConcurrentLinkedQueue<FlingDeviceScannerOperation>();

	/**
	 * Start scan fling device
	 * 
	 * @param context
	 * @param deviceScanner
	 */
	public static void startScanFlingDevice(Context context,
			final DeviceScanner deviceScanner) {
		addScanOperation(context,
				new FlingDeviceScannerOperation(deviceScanner) {

					@Override
					public void act() {
						// TODO Auto-generated method stub
						deviceScanner.onAllDevicesOffline();
						deviceScanner.startScan();
					}

				});
	}

	/**
	 * Stop scan fling device
	 * 
	 * @param context
	 * @param operation
	 */
	public static void stopScanFlingDevice(Context context,
			final DeviceScanner deviceScanner) {
		addScanOperation(context,
				new FlingDeviceScannerOperation(deviceScanner) {

					@Override
					public void act() {
						// TODO Auto-generated method stub
						deviceScanner.stopScan();
					}

				});
	}

	/**
	 * Add scan operation(start/stop)
	 * 
	 * @param context
	 * @param operation
	 */
	private static void addScanOperation(Context context,
			FlingDeviceScannerOperation operation) {
		mDeviceScanOperationQueue.offer(operation);
		context.startService(AndroidUtils.buildIntent(context,
				MEDIA_ROUTE_ACTION, SERVICE_ACTION_SCAN));
	}

	/**
	 * Do device scan operations
	 */
	private void doDeviceScanOperations() {
		FlingDeviceScannerOperation operation;
		long startTime;
		operation = (FlingDeviceScannerOperation) mDeviceScanOperationQueue
				.poll();
		if (operation == null) {
			Log.e("FlingDeviceScannerIntentService", "operation missing");
			return;
		}
		startTime = SystemClock.elapsedRealtime();
		Log.d("FlingDeviceScannerIntentService", "Starting operation: >> "
				+ operation.getClass().getSimpleName());

		try {
			operation.act();
		} catch (Exception e) {
			e.printStackTrace();
		}

		long elapsed = SystemClock.elapsedRealtime() - startTime;
		Log.d("FlingDeviceScannerIntentService", "Finished operation: << "
				+ operation.getClass().getSimpleName() + " (" + elapsed
				+ "ms elapsed)");
	}

	/**
	 * Process all operations in fling operation queue(launch/stop
	 * applications,etc).
	 */
	private void doFlingOperations() {
		FlingOperation operation = (FlingOperation) mFlingOperationQueue.poll();
		if (operation == null) {
			Log.e("FlingIntentService", "operation missing");
			return;
		}
		try {
			operation.doFling();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Log.e("FlingIntentService", "begin call releaseReference");
			operation.releaseReference();
			Log.e("FlingIntentService", "after call releaseReference");
		}
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if (intent == null) {
			Log.e("FlingService", "intent is null.ignore it!!!!");
            return;
		}

		String action = intent.getAction();
		if (action.equals(MEDIA_ROUTE_ACTION)) {
			String extras = (String)intent.getStringExtra(SERVICE_ACTION_KEY);

			if (extras == null) {
				Log.e("FlingDeviceService", "Media scan intent?!");
				return;
			}
			if (extras.equals(SERVICE_ACTION_FLING)) {
				doFlingOperations();
			} else if (extras.equals(SERVICE_ACTION_SCAN)) {
				doDeviceScanOperations();
			} else {
				Log.e("FlingService", "unknown actions!!!![" + intent + "]action[" + extras + "]");
			}
		}
	}
}
