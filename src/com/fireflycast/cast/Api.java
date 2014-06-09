package com.fireflycast.cast;

import android.content.Context;
import android.os.Looper;

import com.fireflycast.cast.FireflyApiClient.ApiOptions;
import com.fireflycast.client.internal.AccountInfo;

/*
 * Api.smali : OK
 */
public final class Api {
	private final ConnectionBuilder_b<?> mConnectionBuilder_za; // Cast.mConnectionManagerCreator_va

	public Api(ConnectionBuilder_b<?> builder) {
		mConnectionBuilder_za = builder;
	}

	public ConnectionBuilder_b<?> getConnectionBuilder_dp() {
		return mConnectionBuilder_za;
	}

	/* Api$a.smali: OK */
	public interface ConnectionApi_a {
		public void connect();

		public void disconnect();

		public boolean isConnected();

		public Looper getLooper();
	}

	/* Api$b.smali: OK */
	public interface ConnectionBuilder_b<T extends ConnectionApi_a> {
		public T build_b(Context context, Looper looper,
				AccountInfo account, ApiOptions options,
				FireflyApiClient.ConnectionCallbacks callbacks,
				FireflyApiClient.OnConnectionFailedListener failedListener);

		public int getPriority();
	}

}
