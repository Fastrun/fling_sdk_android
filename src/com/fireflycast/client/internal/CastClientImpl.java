package com.fireflycast.client.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;

import com.fireflycast.cast.ApplicationMetadata;
import com.fireflycast.cast.Cast;
import com.fireflycast.cast.Cast.ApplicationConnectionResult;
import com.fireflycast.cast.CastDevice;
import com.fireflycast.cast.CastStatusCodes;
import com.fireflycast.cast.FireflyApiClient;
import com.fireflycast.cast.FireflyApi.ResultCallback_c;
import com.fireflycast.cast.Status;

public class CastClientImpl extends
		FireflyClient<ICastDeviceController> {

	private static final LogUtil mLogUtil_xE = new LogUtil(
			"CastClientImpl");
	private static final Object mLock_xU = new Object();
	private static final Object mLock_xV = new Object();

	private final CastDevice mCastDevice_xG;
	private final Cast.Listener mCastListener_wz;
	private final Handler mHandler;
	private final ICastDeviceControllerListener mICastDeviceControllerListener_xH;
	private final Map<String, Cast.MessageReceivedCallback> mMessageReceivedCallbacksMap_xI; // key->namespace,
																								// value->MessageReceivedCallback
	private final long mCastFlags_xJ;

	private ApplicationMetadata mApplicationMetadata_xF;
	private String mApplicationStatus_xK;
	private boolean mIsMute_xf;
	private boolean mFirstStatusUpdate_xL;
	private boolean mIsConnectedDevice_xM;
	private double mVolume_xe;
	private final AtomicLong mRequestIdCreator_xN;
	private String mApplicationId_xO;
	private String mSessionId_xP;
	private Bundle mExtraMessage_xQ;
	private Map<Long, ResultCallback_c<Status>> mResultCallbackMap_xR;
	private ResultCallback_c<Cast.ApplicationConnectionResult> mResultCallback_xS;
	private ResultCallback_c<Status> mResultCallback_xT;

	public static LogUtil getLogUtil_dc() {
		return CastClientImpl.mLogUtil_xE;
	}

	public CastClientImpl(Context context, Looper looper, CastDevice device,
			long flag, Cast.Listener castListener,
			FireflyApiClient.ConnectionCallbacks callbacks,
			FireflyApiClient.OnConnectionFailedListener failedListener) {
		super(context, looper, callbacks, failedListener, null);
		this.mCastDevice_xG = device;
		this.mCastListener_wz = castListener;
		this.mCastFlags_xJ = flag;
		this.mHandler = new Handler(looper);
		this.mMessageReceivedCallbacksMap_xI = new HashMap();
		this.mIsConnectedDevice_xM = false;
		this.mApplicationMetadata_xF = null;
		this.mApplicationStatus_xK = null;
		this.mVolume_xe = 0.0D;
		this.mIsMute_xf = false;
		this.mRequestIdCreator_xN = new AtomicLong(0L);
		this.mResultCallbackMap_xR = new HashMap();

		/*
		 * implementation of ICastDeviceControllerListener_dt
		 */
		this.mICastDeviceControllerListener_xH = new ICastDeviceControllerListener.Stub_a() {
			public void onDisconnected_z(int statusCode) {
				mLogUtil_xE.logd_b(
						"ICastDeviceControllerListener.onDisconnected: %d",
						new Object[] { Integer.valueOf(statusCode) });
				mIsConnectedDevice_xM = false;
				mApplicationMetadata_xF = null;
				if (statusCode == 0) {
					return;
				}
				O(2);
			}

			public void onApplicationConnected_a(
					ApplicationMetadata applicatonMetadata,
					String applicationId, String sessionId, boolean relaunched) {
				mApplicationMetadata_xF = applicatonMetadata;
				mApplicationId_xO = applicatonMetadata.getApplicationId();
				mSessionId_xP = sessionId;
				synchronized (mLock_xU) {
					if (mResultCallback_xS != null) {
						mResultCallback_xS
								.onResult_b(new ApplicationConnectionResultImpl_a(
										new Status(0), applicatonMetadata,
										applicationId, sessionId, relaunched));
						mResultCallback_xS = null;
					}
				}
			}

			public void postApplicationConnectionResult_A(int statusCode) {
				synchronized (mLock_xU) {
					if (mResultCallback_xS != null) {
						mResultCallback_xS
								.onResult_b(new ApplicationConnectionResultImpl_a(
										new Status(statusCode)));
						mResultCallback_xS = null;
					}
				}
			}

			public void notifyCallback_B(int statusCode) {
				notifyCallback_D(statusCode);
			}

			public void notifyCallback_C(int statusCode) {
				notifyCallback_D(statusCode);
			}

			public void onApplicationDisconnected(final int statusCode) {
				mApplicationId_xO = null;
				mSessionId_xP = null;
				if (notifyCallback_D(statusCode)) {
					return;
				}
				if (mCastListener_wz == null) {
					return;
				}
				mHandler.post(new Runnable() {
					public void run() {
						if (mCastListener_wz == null) {
							return;
						}
						mCastListener_wz.onApplicationDisconnected(statusCode);
					}
				});
			}

			public void notifyApplicationStatusOrVolumeChanged_b(
					final String applicationStatus, final double volume,
					final boolean isMute) {
				mHandler.post(new Runnable() {
					public void run() {
						notifyCastListener_a(applicationStatus, volume, isMute);
					}
				});
			}

			public void onMessageReceived_d(final String namespace,
					final String message) {
				mLogUtil_xE.logd_b("Receive (type=text, ns=%s) %s",
						new Object[] { namespace, message });
				mHandler.post(new Runnable() {
					public void run() {
						Cast.MessageReceivedCallback localMessageReceivedCallback;
						synchronized (mMessageReceivedCallbacksMap_xI) {
							localMessageReceivedCallback = mMessageReceivedCallbacksMap_xI
									.get(namespace);
						}
						if (localMessageReceivedCallback != null) {
							localMessageReceivedCallback.onMessageReceived(
									mCastDevice_xG, namespace, message);
						} else {
							mLogUtil_xE
									.logd_b("Discarded message for unknown namespace '%s'",
											new Object[] { namespace });
						}
					}
				});
			}

			public void receiveBinary_b(String namespace, byte[] dataBytes) {
				mLogUtil_xE.logd_b(
						"IGNORING: Receive (type=binary, ns=%s) <%d bytes>",
						new Object[] { namespace,
								Integer.valueOf(dataBytes.length) });
			}

			public void requestCallback_a(String namespace, long requestId,
					int statusCode) {
				notifyCallback_b(requestId, statusCode);
			}

			public void requestCallback_a(String namespace, long requestId) {
				notifyCallback_b(requestId, 0);
			}

			private void notifyCallback_b(long requestId, int statusCode) {
				ResultCallback_c resultCallback = null;
				synchronized (mResultCallbackMap_xR) {
					resultCallback = (ResultCallback_c) mResultCallbackMap_xR
							.remove(Long.valueOf(requestId));
				}
				if (resultCallback == null) {
					return;
				}
				resultCallback.onResult_b(new Status(statusCode));
			}

			private boolean notifyCallback_D(int statusCode) {
				synchronized (mLock_xV) {
					if (mResultCallback_xT != null) {
						mResultCallback_xT.onResult_b(new Status(statusCode));
						mResultCallback_xT = null;
						return true;
					}
				}
				return false;
			}
		};
	}

	protected void sendMessage_a(int statusCode, IBinder binder, Bundle bundle) {
		if ((statusCode == 0) || (statusCode == 1001)) {
			mIsConnectedDevice_xM = true;
			mFirstStatusUpdate_xL = true;
		} else {
			mIsConnectedDevice_xM = false;
		}
		int i = statusCode;
		if (statusCode == 1001) {
			mExtraMessage_xQ = new Bundle();
			mExtraMessage_xQ.putBoolean("com.fireflycast.cast.EXTRA_APP_NO_LONGER_RUNNING",
					true);
			i = 0;
		}
		super.sendMessage_a(i, binder, bundle);
	}

	public void disconnect() {
		try {
			if (isConnected()) {
				synchronized (this.mMessageReceivedCallbacksMap_xI) {
					mMessageReceivedCallbacksMap_xI.clear();
				}
				getService_eb().disconnect();
			}
		} catch (RemoteException localRemoteException) {
			mLogUtil_xE.logd_b(
					"Error while disconnecting the controller interface: %s",
					new Object[] { localRemoteException.getMessage() });
		} finally {
			super.disconnect();
		}
	}

	public Bundle getBundle_cY() {
		if (mExtraMessage_xQ != null) {
			Bundle bundle = mExtraMessage_xQ;
			mExtraMessage_xQ = null;
			return bundle;
		}
		return super.getBundle_cY();
	}

	public void sendMessage_a(String namespace, String payloadMessage,
			ResultCallback_c<Status> callback) throws IllegalArgumentException,
			IllegalStateException, RemoteException {
		if (TextUtils.isEmpty(payloadMessage)) {
			throw new IllegalArgumentException(
					"The message payload cannot be null or empty");
		}
		if ((namespace == null) || (namespace.length() > 128)) {
			throw new IllegalArgumentException("Invalid namespace length");
		}
		if (payloadMessage.length() > 65536) {
			throw new IllegalArgumentException("Message exceeds maximum size");
		}
		checkConnectedDeviceThrowable_db();
		long requestId = mRequestIdCreator_xN.incrementAndGet();
		getService_eb().sendMessage_a(namespace, payloadMessage, requestId);
		mResultCallbackMap_xR.put(Long.valueOf(requestId), callback);
	}

	public void launchApplication_a(String applicationId, boolean relaunchFlag,
			ResultCallback_c<Cast.ApplicationConnectionResult> callback)
			throws IllegalStateException, RemoteException {
		setApplicationConnectionResultCallback_d(callback);
		getService_eb().launchApplication_e(applicationId, relaunchFlag);
	}

	public void joinApplication_b(String applicationId, String sessionId,
			ResultCallback_c<Cast.ApplicationConnectionResult> callback)
			throws IllegalStateException, RemoteException {
		setApplicationConnectionResultCallback_d(callback);
		getService_eb().joinApplication_e(applicationId, sessionId);
	}

	private void setApplicationConnectionResultCallback_d(
			ResultCallback_c<Cast.ApplicationConnectionResult> callback) {
		synchronized (mLock_xU) {
			if (mResultCallback_xS != null) {
				mResultCallback_xS
						.onResult_b(new ApplicationConnectionResultImpl_a(
								new Status(CastStatusCodes.CANCELED)));
			}
			mResultCallback_xS = callback;
		}
	}

	public void leaveApplication_e(ResultCallback_c<Status> callback)
			throws IllegalStateException, RemoteException {
		setStatusCallback_f(callback);
		getService_eb().leaveApplication_df();
	}

	public void stopApplication_a(String sessionId,
			ResultCallback_c<Status> paramc) throws IllegalStateException,
			RemoteException {
		setStatusCallback_f(paramc);
		getService_eb().stopApplication_R(sessionId);
	}

	private void setStatusCallback_f(ResultCallback_c<Status> callback) {
		synchronized (mLock_xV) {
			if (this.mResultCallback_xT != null) {
				callback.onResult_b(new Status(CastStatusCodes.INVALID_REQUEST));
				return;
			}
			this.mResultCallback_xT = callback;
		}
	}

	public void requestStatus_cZ() throws IllegalStateException,
			RemoteException {
		getService_eb().requestStatus_cZ();
	}

	public void setVolume_a(double volume) throws IllegalArgumentException,
			IllegalStateException, RemoteException {
		if ((Double.isInfinite(volume)) || Double.isNaN(volume)) {
			throw new IllegalArgumentException("Volume cannot be " + volume);
		}
		getService_eb().setVolume_a(volume, mVolume_xe, mIsMute_xf);
	}

	public void setMute_t(boolean mute) throws IllegalStateException,
			RemoteException {
		getService_eb().setMute_a(mute, mVolume_xe, mIsMute_xf);
	}

	public double getVolume_da() throws IllegalStateException {
		checkConnectedDeviceThrowable_db();
		return mVolume_xe;
	}

	public boolean isMute() throws IllegalStateException {
		checkConnectedDeviceThrowable_db();
		return mIsMute_xf;
	}

	public void setMessageReceivedCallbacks_a(String channelNameSpace,
			Cast.MessageReceivedCallback callback)
			throws IllegalArgumentException, IllegalStateException,
			RemoteException {
		if (TextUtils.isEmpty(channelNameSpace)) {
			throw new IllegalArgumentException(
					"Channel namespace cannot be null or empty");
		}
		removeMessageReceivedCallbacks_Q(channelNameSpace);
		if (callback == null) {
			return;
		}
		synchronized (mMessageReceivedCallbacksMap_xI) {
			mMessageReceivedCallbacksMap_xI.put(channelNameSpace, callback);
		}
		getService_eb().setMessageReceivedCallbacks_S(channelNameSpace);
	}

	public void removeMessageReceivedCallbacks_Q(String channelNameSpace)
			throws IllegalArgumentException, RemoteException {
		if (TextUtils.isEmpty(channelNameSpace)) {
			throw new IllegalArgumentException(
					"Channel namespace cannot be null or empty");
		}
		Cast.MessageReceivedCallback localMessageReceivedCallback;
		synchronized (mMessageReceivedCallbacksMap_xI) {
			localMessageReceivedCallback = mMessageReceivedCallbacksMap_xI
					.remove(channelNameSpace);
		}
		if (localMessageReceivedCallback == null)
			return;
		try {
			getService_eb().removeMessageReceivedCallbacks_T(channelNameSpace);
		} catch (IllegalStateException localIllegalStateException) {
			mLogUtil_xE.logd_a(
					localIllegalStateException,
					"Error unregistering namespace (%s): %s",
					new Object[] { channelNameSpace,
							localIllegalStateException.getMessage() });
		}
	}

	public ApplicationMetadata getApplicationMetadata()
			throws IllegalStateException {
		checkConnectedDeviceThrowable_db();
		return mApplicationMetadata_xF;
	}

	public String getApplicationStatus() throws IllegalStateException {
		checkConnectedDeviceThrowable_db();
		return mApplicationStatus_xK;
	}

	private void notifyCastListener_a(String applicationStatus, double volume,
			boolean isMute) {
		boolean hasChange = false;
		if (!(DoubleAndLongConverter.compare_a(applicationStatus,
				mApplicationStatus_xK))) {
			mApplicationStatus_xK = applicationStatus;
			hasChange = true;
		}
		if ((mCastListener_wz != null) && (hasChange || mFirstStatusUpdate_xL)) {
			mCastListener_wz.onApplicationStatusChanged();
		}
		hasChange = false;
		if (volume != mVolume_xe) {
			mVolume_xe = volume;
			hasChange = true;
		}
		if (isMute != mIsMute_xf) {
			mIsMute_xf = isMute;
			hasChange = true;
		}
		mLogUtil_xE.logd_b("hasChange=%b, mFirstStatusUpdate=%b", new Object[] {
				Boolean.valueOf(hasChange), Boolean.valueOf(mFirstStatusUpdate_xL) });
		if ((mCastListener_wz != null) && (hasChange || mFirstStatusUpdate_xL)) {
			mCastListener_wz.onVolumeChanged();
		}
		mFirstStatusUpdate_xL = false;
	}

	private void checkConnectedDeviceThrowable_db()
			throws IllegalStateException {
		if (this.mIsConnectedDevice_xM) {
			return;
		}
		throw new IllegalStateException("not connected to a device");
	}

	private static final class ApplicationConnectionResultImpl_a implements
			ApplicationConnectionResult {
		private final Status status_vl;
		private final ApplicationMetadata applicationMetadata_yb;
		private final String applicationStatus_yc;
		private final String sessionId_pz;
		private final boolean wasLaunched_yd;

		public ApplicationConnectionResultImpl_a(Status paramStatus,
				ApplicationMetadata applicationMetadata,
				String applicationStatus, String sessionId, boolean wasLaunched) {
			this.status_vl = paramStatus;
			this.applicationMetadata_yb = applicationMetadata;
			this.applicationStatus_yc = applicationStatus;
			this.sessionId_pz = sessionId;
			this.wasLaunched_yd = wasLaunched;
		}

		public ApplicationConnectionResultImpl_a(Status status) {
			this(status, null, null, null, false);
		}

		public Status getStatus() {
			return this.status_vl;
		}

		public ApplicationMetadata getApplicationMetadata() {
			return this.applicationMetadata_yb;
		}

		public String getApplicationStatus() {
			return this.applicationStatus_yc;
		}

		public String getSessionId() {
			return this.sessionId_pz;
		}

		public boolean getWasLaunched() {
			return this.wasLaunched_yd;
		}
	}

	/*******************************************************/
	// implemented from super class FmsClient_eh.class
	/*******************************************************/
	protected String getServiceName_aF() {
		return "com.fireflycast.cast.service.BIND_CAST_DEVICE_CONTROLLER_SERVICE";
	}

	protected String getInterfaceDescriptor_aG() {
		return "com.fireflycast.cast.internal.ICastDeviceController";
	}

	protected ICastDeviceController getService_p(IBinder binder) {
		return ICastDeviceController.Stub_a.asInterface_w(binder);
	}

	protected void getServiceFromBroker_a(
			IFireflyServiceBroker serviceBroker,
			IFireflyCallbackImpl_e fireflyCallback) throws RemoteException {
		mLogUtil_xE
				.logd_b("getServiceFromBroker(): mLastApplicationId=%s, mLastSessionId=%s",
						new Object[] { mApplicationId_xO, mSessionId_xP });
		Bundle bundle = new Bundle();
		mCastDevice_xG.putInBundle(bundle);
		bundle.putLong("com.fireflycast.cast.EXTRA_CAST_FLAGS", mCastFlags_xJ);
		if (mApplicationId_xO != null) {
			bundle.putString("last_application_id", mApplicationId_xO);
			if (mSessionId_xP != null) {
				bundle.putString("last_session_id", mSessionId_xP);
			}
		}
		serviceBroker.initService(fireflyCallback, 4323000, getContext()
				.getPackageName(),
				mICastDeviceControllerListener_xH.asBinder(), bundle);
	}
}
