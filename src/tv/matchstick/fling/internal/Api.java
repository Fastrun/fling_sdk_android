package tv.matchstick.fling.internal;

import tv.matchstick.client.internal.AccountInfo;
import tv.matchstick.fling.FlingManager;
import tv.matchstick.fling.FlingManager.ApiOptions;
import tv.matchstick.fling.FlingManager.ConnectionCallbacks;
import tv.matchstick.fling.FlingManager.OnConnectionFailedListener;
import android.content.Context;
import android.os.Looper;

/**
 * Api class
 */
public final class Api {
	private final ConnectionBuilder<?> mConnectionBuilder;

	/**
	 * Create api with connection builder
	 * 
	 * @param builder
	 *            the specific builder
	 */
	public Api(ConnectionBuilder<?> builder) {
		mConnectionBuilder = builder;
	}

	/**
	 * Get connection builder
	 * 
	 * @return builder
	 */
	public ConnectionBuilder<?> getConnectionBuilder() {
		return mConnectionBuilder;
	}

	/**
	 * Connection interface for client API
	 */
	public interface ConnectionApi {
		/**
		 * connect to device
		 */
		public void connect();

		/**
		 * disconnect to device
		 */
		public void disconnect();

		/**
		 * Get current connected status with device
		 * 
		 * @return current connect status
		 */
		public boolean isConnected();

		/**
		 * Get related Looper object
		 * 
		 * @return looper
		 */
		public Looper getLooper();
	}

	/**
	 * Connection builder
	 * 
	 * @param <T>
	 */
	public interface ConnectionBuilder<T extends ConnectionApi> {

		/**
		 * Build the specific fling object
		 * 
		 * @param context
		 *            application context
		 * @param looper
		 *            looper
		 * @param account
		 *            account information
		 * @param options
		 *            Api options
		 * @param callbacks
		 *            callback function
		 * @param failedListener
		 *            failed listener
		 * @return the specific class instance
		 */
		public T build(Context context, Looper looper, AccountInfo account,
				ApiOptions options,
				FlingManager.ConnectionCallbacks callbacks,
				FlingManager.OnConnectionFailedListener failedListener);

		/**
		 * Get related priority
		 * 
		 * @return priority
		 */
		public int getPriority();
	}

}
