package tv.matchstick.server.fling.bridge;

import tv.matchstick.fling.FlingDevice;
import tv.matchstick.fling.service.FlingService;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.IInterface;

/**
 * Fling Service's binder
 */
public final class FlingServiceBinder extends FlingServiceBrokerStub {
	final FlingService mFlingService;

	private FlingServiceBinder(FlingService flingservice) {
		super();
		mFlingService = flingservice;
	}

	public FlingServiceBinder(FlingService flingservice, byte dummy) {
		this(flingservice);
	}

	/**
	 * Create one fling client
	 * 
	 * @callbacks callback function
	 * @version fling client's versions
	 * @packageName package name
	 * @listener fling device control listener
	 * @bundle data
	 */
	public final void initFlingService(IFlingCallbacks callbacks, int version,
			String packageName, IBinder listener, Bundle bundle) {
		FlingDevice flingdevice;
		String lastApplicationId;
		String lastSessionId;
		long flags;
		IFlingDeviceControllerListener controlListener;

		FlingService.log().d("begin initFlingService!");
		try {
			flingdevice = FlingDevice.getFromBundle(bundle);
			lastApplicationId = bundle.getString("last_application_id");
			lastSessionId = bundle.getString("last_session_id");
		} catch (Exception exception) {
			FlingService.log().e(exception, "Fling device was not valid.",
					new Object[0]);
			try {
				callbacks.onConnect(10, null, null);
			} catch (RemoteException remoteexception) {
				FlingService.log().d("client died while brokering service");
			}
			return;
		}
		flags = bundle.getLong("tv.matchstick.fling.EXTRA_FLING_FLAGS", 0L);
		FlingService
				.log()
				.d("connecting to device with lastApplicationId=%s, lastSessionId=%s",
						lastApplicationId, lastSessionId);
		if (listener == null) {
			controlListener = null;
		} else {
			IInterface iinterface = listener
					.queryLocalInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
			if (iinterface != null
					&& (iinterface instanceof IFlingDeviceControllerListener))
				controlListener = (IFlingDeviceControllerListener) iinterface;
			else
				controlListener = new FlingDeviceControllerListener(listener);
		}

		/**
		 * Add one fling client to fling service's client list
		 */
		FlingService.getFlingClients(mFlingService).add(
				new FlingConnectedClient(mFlingService, callbacks, flingdevice,
						lastApplicationId, lastSessionId, controlListener,
						packageName, flags));

		FlingService.log().d("end initFlingService!");
	}
}
