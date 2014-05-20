package com.fireflycast.cast;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.fireflycast.cast.FireflyApi_a.FireflyApiImpl_a;
import com.fireflycast.client.common.FireflyPlayServicesClient;
import com.fireflycast.client.common.api.FireflyApiClientImpl_b;
import com.fireflycast.client.internal.AccountInfo_ee;
import com.fireflycast.client.internal.ValueChecker_er;

/*
 * GoogleApiClient.smali : OK
 */
public interface FireflyApiClient {
	// unused
	public <A extends Api.ConnectionApi_a, T extends FireflyApiImpl_a<? extends Result, A>> T a(
			T fireflyApi);

	public <A extends Api.ConnectionApi_a, T extends FireflyApiImpl_a<? extends Result, A>> T executeTask_b(
			T fireflyApi);

	public <C extends Api.ConnectionApi_a> C getConnectionApi_a(
			Api.ConnectionBuilder_b<C> builder);

	/*
	 * connected status: 1 - connecting, 2 - connected
	 */
	public void connect();

	public void disconnect();

	public void reconnect();

	public boolean isConnected();

	public boolean isConnecting();

	public ConnectionResult blockingConnect(long timeout, TimeUnit paramTimeUnit);

	public void registerConnectionCallbacks(ConnectionCallbacks callbacks);

	public boolean isConnectionCallbacksRegistered(ConnectionCallbacks callbacks);

	public void unregisterConnectionCallbacks(ConnectionCallbacks callbacks);

	public void registerConnectionFailedListener(
			OnConnectionFailedListener failedListener);

	public boolean isConnectionFailedListenerRegistered(
			OnConnectionFailedListener failedListener);

	public void unregisterConnectionFailedListener(
			OnConnectionFailedListener failedListener);

	/*
	 * GoogleApiClient$Builder.smali : OK
	 */
	public final class Builder {
		private final Set<String> mScopeUriSet_zn;
		private final Context mContext;
		private final Map<Api, ApiOptions> mApiOptionMap_zr;
		private final Set<ConnectionCallbacks> mCallbacksSet_zt;
		private final Set<OnConnectionFailedListener> mFailedListenerSet_zu;

		private Looper mLooper_zs;
		private int mGravityForPopups_zo;
		private View mView_zp;
		private String mPackageName_zq;
		private String mAccountName_vi;

		public Builder(Context context) {
			this.mScopeUriSet_zn = new HashSet<String>();
			this.mApiOptionMap_zr = new HashMap<Api, ApiOptions>();
			this.mCallbacksSet_zt = new HashSet<ConnectionCallbacks>();
			this.mFailedListenerSet_zu = new HashSet<OnConnectionFailedListener>();
			this.mContext = context;
			this.mLooper_zs = context.getMainLooper();
			this.mPackageName_zq = context.getPackageName();
		}

		public Builder(
				Context context,
				FireflyApiClient.ConnectionCallbacks connectedListener,
				FireflyApiClient.OnConnectionFailedListener connectionFailedListener) {
			this(context);

			ValueChecker_er.checkNullPointer_b(connectedListener,
					"Must provide a connected listener");
			mCallbacksSet_zt.add(connectedListener);
			ValueChecker_er.checkNullPointer_b(connectionFailedListener,
					"Must provide a connection failed listener");
			mFailedListenerSet_zu.add(connectionFailedListener);
		}

		public Builder setHandler(Handler handler) {
			ValueChecker_er.checkNullPointer_b(handler,
					"Handler must not be null");
			mLooper_zs = handler.getLooper();
			return this;
		}

		public Builder addConnectionCallbacks(
				FireflyApiClient.ConnectionCallbacks callback) {
			mCallbacksSet_zt.add(callback);
			return this;
		}

		public Builder addOnConnectionFailedListener(
				FireflyApiClient.OnConnectionFailedListener listener) {
			mFailedListenerSet_zu.add(listener);
			return this;
		}

		public Builder setViewForPopups(View viewForPopups) {
			mView_zp = viewForPopups;
			return this;
		}

		public Builder addApi(Api api) {
			return addApi(api, null);
		}

		public Builder addApi(Api api, FireflyApiClient.ApiOptions options) {
			mApiOptionMap_zr.put(api, options);
			return this;
		}

		public Builder setAccountName(String accountName) {
			this.mAccountName_vi = accountName;
			return this;
		}

		public Builder useDefaultAccount() {
			return setAccountName("<<default account>>");
		}

		public Builder setGravityForPopups(int gravityForPopups) {
			mGravityForPopups_zo = gravityForPopups;
			return this;
		}

		public AccountInfo_ee getAccount_dx() {
			return new AccountInfo_ee(mAccountName_vi, mScopeUriSet_zn,
					mGravityForPopups_zo, mView_zp, mPackageName_zq);
		}

		public FireflyApiClient build() {
			return new FireflyApiClientImpl_b(mContext, mLooper_zs,
					getAccount_dx(), mApiOptionMap_zr, mCallbacksSet_zt,
					mFailedListenerSet_zu);
		}
	}

	/*
	 * GoogleApiClient$ApiOptions.smali : OK
	 */
	public interface ApiOptions {
	}

	/*
	 * GoogleApiClient$ApiOptions.OnConnectionFailedListener : OK
	 */
	public interface OnConnectionFailedListener extends
			FireflyPlayServicesClient.OnConnectionFailedListener {
	}

	/*
	 * GoogleApiClient$ApiOptions.ConnectionCallbacks : OK
	 */
	public interface ConnectionCallbacks {
		public static final int CAUSE_SERVICE_DISCONNECTED = 1;
		public static final int CAUSE_NETWORK_LOST = 2;

		public void onConnected(Bundle paramBundle);

		public void onConnectionSuspended(int paramInt);
	}

}
