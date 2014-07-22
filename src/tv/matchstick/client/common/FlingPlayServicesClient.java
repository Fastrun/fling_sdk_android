package tv.matchstick.client.common;

import tv.matchstick.fling.ConnectionResult;
import android.os.Bundle;

/*
 * GooglePlayServicesClient.smali : OK
 * original name : GooglePlayServicesClient
 */
public interface FlingPlayServicesClient {
	public void connect();

	public void disconnect();

	public boolean isConnected();

	public boolean isConnecting();

	public void registerConnectionCallbacks(ConnectionCallbacks callbacks);

	public boolean isConnectionCallbacksRegistered(ConnectionCallbacks callbacks);

	public void unregisterConnectionCallbacks(ConnectionCallbacks callbacks);

	public void registerConnectionFailedListener(
			OnConnectionFailedListener listener);

	public boolean isConnectionFailedListenerRegistered(
			OnConnectionFailedListener listener);

	public void unregisterConnectionFailedListener(
			OnConnectionFailedListener listener);

	public interface OnConnectionFailedListener {
		/**
		 * Called when there was an error connecting the client to the service.
		 *
		 * @param result
		 */
		public void onConnectionFailed(ConnectionResult result);
	}

	public interface ConnectionCallbacks {
		public void onConnected(Bundle paramBundle);

		public void onDisconnected();
	}

}
