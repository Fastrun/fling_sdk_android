package tv.matchstick.server.fling.bridge;

import tv.matchstick.server.fling.ApplicationMetadata;

public interface IFlingSrvController {
	public abstract void onConnected();

	public abstract void onDisconnected(int disconnectStatusCode);

	public abstract void onApplicationConnected(
			ApplicationMetadata applicationmetadata, String statusText,
			String sessionId, boolean flag);

	public abstract void onVolumeChanged(String statusText, double volume,
			boolean muteState);

	public abstract void onRequestCallback(String namespace, long id);

	public abstract void onRequestCallback(String namespace, long requestId,
			int result);

	public abstract void notifyOnMessageReceived(String namespace,
			String message);

	public abstract void onReceiveBinary(String namespace, byte message[]);

	public abstract void onConnectedWithoutApp();

	public abstract void onApplicationConnectionFailed(int result);

	public abstract void onConnectionFailed();

	public abstract void onRequestStatus(int result);

	public abstract void onInvalidRequest();

	public abstract void onApplicationDisconnected(int statusCode);
}
