package com.fireflycast.client.internal;

import org.json.JSONObject;

public interface RequestTrackerCallback {

	public void onTrackRequest_a(long requestId, int statusCode,
			JSONObject customData);

	public void onSignInRequired_k(long requestId);

}
