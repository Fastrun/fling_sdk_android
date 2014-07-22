package tv.matchstick.fling.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import tv.matchstick.server.fling.bridge.FlingConnectedClient;
import tv.matchstick.server.fling.bridge.FlingServiceBinder;
import tv.matchstick.server.utils.LOG;

/**
 * This service used to control all fling media actions
 * 
 * @author jim
 *
 */
public class FlingService extends Service {
	private static final LOG log = new LOG("FlingService");

	private static final String DEVICE_CONTROL_ACTION = "tv.matchstick.fling.service.FLING";

	private Handler mHandler;

	/**
	 * Contained all connect fling client.
	 */
	private List<FlingConnectedClient> mFlingClients;

	public static LOG log() {
		return log;
	}

	/**
	 * Get connected fling clients
	 * 
	 * @param flingservice
	 * @return client list
	 */
	public static List<FlingConnectedClient> getFlingClients(
			FlingService flingservice) {
		return flingservice.mFlingClients;
	}

	private synchronized void removeFlingClient(FlingConnectedClient client) {
		mFlingClients.remove(client);
	}

	/**
	 * Remove client from connected fling client list
	 * 
	 * @param client
	 *            client which will be removed
	 */
	public static void removeFlingClient(FlingService flingservice,
			FlingConnectedClient client) {
		flingservice.removeFlingClient(client);
	}

	/**
	 * Get Client main Handler
	 * 
	 * @param flingservice
	 * @return service handler
	 */
	public static Handler getHandler(FlingService flingservice) {
		return flingservice.mHandler;
	}

	@Override
	public IBinder onBind(Intent intent) {
		if (!DEVICE_CONTROL_ACTION.equals(intent.getAction())) {
			return null;
		}

		log.d("onBind!!!!");

		return (new FlingServiceBinder(this, (byte) 0)).asBinder();
	}

	@Override
	public void onCreate() {
		mFlingClients = new ArrayList<FlingConnectedClient>();
		mHandler = new Handler(Looper.getMainLooper());
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return Service.START_STICKY;
	}
}
