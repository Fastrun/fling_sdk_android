package tv.matchstick.fling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import tv.matchstick.client.common.FlingPlayServicesClient;
import tv.matchstick.client.common.api.FlingManagerImpl;
import tv.matchstick.client.internal.AccountInfo;
import tv.matchstick.client.internal.ValueChecker;
import tv.matchstick.fling.internal.Api;
import tv.matchstick.fling.internal.MatchStickApi.MatchStickApiImpl;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

/**
 * Fling Manager interface.
 *
 * Before any operation is executed, the FlingManager must be connected using the connect() method. The device is not considered connected until the onConnected(Bundle) callback has been called.
 * When your app is done using this connection, call disconnect(), even if the async result from connect() has not yet been delivered.
 * You should instantiate a client object in your Activity's onCreate(Bundle) method and then call connect() in onStart() and disconnect() in onStop(), regardless of the state. 
 */
public interface FlingManager {
	// unused
	/*
	public <A extends Api.ConnectionApi, T extends MatchStickApiImpl<? extends Result, A>> T a(
			T flingApi);
	*/
	
	/**
	 * Execute task.
	 *
	 * @param flingApi
	 * @return
	 */
	public <A extends Api.ConnectionApi, T extends MatchStickApiImpl<? extends Result, A>> T executeTask(
			T flingApi);

	/**
	 * Get connection api.
	 *
	 * @param builder
	 * @return
	 */
	public <C extends Api.ConnectionApi> C getConnectionApi(
			Api.ConnectionBuilder<C> builder);

	/*
	 * connected status: 1 - connecting, 2 - connected
	 */
	/**
	 * Connect to Fling service.
	 */
	public void connect();

	/**
	 * Disconnect from Fling service.
	 */
	public void disconnect();

	/**
	 * Reconnect to Fling service.
	 * 
	 * Closes the current connection to Fling service and creates a new connection.
	 */
	public void reconnect();

	/**
	 * Whether it's connected with Fling service
	 *
	 * @return true for connected, false for others
	 */
	public boolean isConnected();

	/**
	 * Whether it's connecting to Fling service.
	 *
	 * @return true for connecting, false for others
	 */
	public boolean isConnecting();

	/**
	 * Block connect to Fling service.
	 *
	 * @param timeout the maximum time to wait
	 * @param paramTimeUnit the time unit of the timeout argument
	 * @return one ConnectionResult object
	 */
	public ConnectionResult blockingConnect(long timeout, TimeUnit paramTimeUnit);

	/**
	 * Register Connection callback.
	 *
	 * Register a listener to receive connection events from this FlingManager.
	 *
	 * @param callbacks
	 */
	public void registerConnectionCallbacks(ConnectionCallbacks callbacks);

	/**
	 * Check whether the specific callback is already registered.
	 *
	 * If the specified listener is currently registered to receive connection events, return true
	 * 
	 * @param callbacks
	 * @return
	 */
	public boolean isConnectionCallbacksRegistered(ConnectionCallbacks callbacks);

	/**
	 * Unregister Connection Callback.
	 *
	 * Removes a connection listener from this FlingManager.
	 * @param callbacks
	 */
	public void unregisterConnectionCallbacks(ConnectionCallbacks callbacks);

	/**
	 * Register listener which will be notified when connection failed.
	 *
	 * Registers a listener to receive connection failed events from this FlingManager.
	 * @param failedListener
	 */
	public void registerConnectionFailedListener(
			OnConnectionFailedListener failedListener);

	/**
	 * Whether the specified connection failed listener is registered.
	 *
	 * If the specified listener is currently registered to receive connection failed events.
	 * @param failedListener
	 * @return
	 */
	public boolean isConnectionFailedListenerRegistered(
			OnConnectionFailedListener failedListener);

	/**
	 * Unregister connection failed listener.
	 *
	 * Removes a connection failed listener from the FlingManager.
	 *
	 * @param failedListener
	 */
	public void unregisterConnectionFailedListener(
			OnConnectionFailedListener failedListener);

	/**
	 * Helper class for FlingApi class
	 */
	public final class Builder {
		/**
		 * scope uri set
		 */
		private final Set<String> mScopeUriSet;

		/**
		 * Used context
		 */
		private final Context mContext;

		/**
		 * Api option maps
		 */
		private final Map<Api, ApiOptions> mApiOptionMap;

		/**
		 * ConnectionCallbacks set
		 */
		private final Set<ConnectionCallbacks> mCallbacksSet;

		/**
		 * Connection failed listener map
		 */
		private final Set<OnConnectionFailedListener> mFailedListenerSet;

		/**
		 * Looper
		 */
		private Looper mLooper;

		/**
		 * Gravity for popup windows
		 */
		private int mGravityForPopups;

		/**
		 * Used view
		 */
		private View mView;

		/**
		 * Package name
		 */
		private String mPackageName;

		/**
		 * Account name
		 */
		private String mAccountName;

		/**
		 * Builder Constructor
		 *
		 * @param context
		 *            used context
		 */
		public Builder(Context context) {
			this.mScopeUriSet = new HashSet<String>();
			this.mApiOptionMap = new HashMap<Api, ApiOptions>();
			this.mCallbacksSet = new HashSet<ConnectionCallbacks>();
			this.mFailedListenerSet = new HashSet<OnConnectionFailedListener>();
			this.mContext = context;
			this.mLooper = context.getMainLooper();
			this.mPackageName = context.getPackageName();
		}

		/**
		 * Build Constructor.
		 *
		 * @param context The context to use for the connection
		 * @param connectedListener The listener where the results of the asynchronous connect() call are delivered.
		 * @param connectionFailedListener the listener which will be notified if the connection attempt fails.
		 */
		public Builder(
				Context context,
				FlingManager.ConnectionCallbacks connectedListener,
				FlingManager.OnConnectionFailedListener connectionFailedListener) {
			this(context);

			ValueChecker.checkNullPointer(connectedListener,
					"Must provide a connected listener");
			mCallbacksSet.add(connectedListener);
			ValueChecker.checkNullPointer(connectionFailedListener,
					"Must provide a connection failed listener");
			mFailedListenerSet.add(connectionFailedListener);
		}

		/**
		 * Set handler.
		 * 
		 * Sets a Handler to indicate which thread to use when invoking callbacks.
		 *
		 * @param handler
		 * @return
		 */
		public Builder setHandler(Handler handler) {
			ValueChecker.checkNullPointer(handler, "Handler must not be null");
			mLooper = handler.getLooper();
			return this;
		}

		/**
		 * Add connection callback function.
		 *
		 * Registers a listener to receive connection events from FlingManager.
		 * @param callback
		 * @return
		 */
		public Builder addConnectionCallbacks(
				FlingManager.ConnectionCallbacks callback) {
			mCallbacksSet.add(callback);
			return this;
		}

		/**
		 * Add Connection Failed listener.
		 *
		 * Adds a listener to register to receive connection failed events from FlingManager.
		 *
		 * @param listener
		 * @return
		 */
		public Builder addOnConnectionFailedListener(
				FlingManager.OnConnectionFailedListener listener) {
			mFailedListenerSet.add(listener);
			return this;
		}

		/**
		 * Set popup view.
		 *
		 * @param viewForPopups
		 * @return
		 */
		public Builder setViewForPopups(View viewForPopups) {
			mView = viewForPopups;
			return this;
		}

		/**
		 * Add Api.
		 *
		 * @param api
		 * @return
		 */
		public Builder addApi(Api api) {
			return addApi(api, null);
		}

		/**
		 * Add Api.
		 *
		 * @param api
		 * @param options
		 * @return
		 */
		public Builder addApi(Api api, FlingManager.ApiOptions options) {
			mApiOptionMap.put(api, options);
			return this;
		}

		/**
		 * Set account name.
		 *
		 * @param accountName
		 * @return
		 */
		public Builder setAccountName(String accountName) {
			this.mAccountName = accountName;
			return this;
		}

		/**
		 * Use default account
		 *
		 * @return one Builder object
		 */
		public Builder useDefaultAccount() {
			return setAccountName("<<default account>>");
		}

		/**
		 * Set gravity
		 *
		 * @param gravityForPopups
		 * @return
		 */
		public Builder setGravityForPopups(int gravityForPopups) {
			mGravityForPopups = gravityForPopups;
			return this;
		}

		/**
		 * Create account info according to current data
		 *
		 * @return
		 */
		public AccountInfo getAccount() {
			return new AccountInfo(mAccountName, mScopeUriSet,
					mGravityForPopups, mView, mPackageName);
		}

		/**
		 * Create FlingManager object according to current data
		 * 
		 * @return
		 */
		public FlingManager build() {
			return new FlingManagerImpl(mContext, mLooper, getAccount(),
					mApiOptionMap, mCallbacksSet, mFailedListenerSet);
		}
	}

	/**
	 * Fling Api options
	 */
	public interface ApiOptions {
	}

	/**
	 * Interface for connection failed listener.
	 *
	 * Provides callbacks for scenarios that result in a failed attempt to connect the client to the service. 
	 */
	public interface OnConnectionFailedListener extends
			FlingPlayServicesClient.OnConnectionFailedListener {
	}

	/**
	 * Connection callback.
	 *
	 * Provides callbacks that are called when the client is connected or disconnected from the service. 
	 * <p>
	 * Most applications implement onConnected(Bundle) to start making requests. 
	 */
	public interface ConnectionCallbacks {
		/**
		 * Service disconnected.
		 *
		 * A suspension cause informing that the service has been killed.
		 */
		public static final int CAUSE_SERVICE_DISCONNECTED = 1;

		/**
		 * Network lost.
		 *
		 * A suspension cause informing you that a peer device connection was lost.
		 */
		public static final int CAUSE_NETWORK_LOST = 2;

		/**
		 * Called when connected.
		 *
		 * After calling connect(), this method will be invoked asynchronously when the connect request has successfully completed.
		 *
		 * @param connectionHint
		 */
		public void onConnected(Bundle connectionHint);

		/**
		 * Called when suspended.
		 *
		 * Called when the client is temporarily in a disconnected state.
		 * @param cause
		 */
		public void onConnectionSuspended(int cause);
	}

}
