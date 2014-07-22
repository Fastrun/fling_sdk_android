package tv.matchstick.client.internal;

import org.json.JSONObject;

public interface RequestTrackerCallback {

	public void onTrackRequest(long requestId, int statusCode,
			JSONObject customData);

	public void onSignInRequired(long requestId);

}
