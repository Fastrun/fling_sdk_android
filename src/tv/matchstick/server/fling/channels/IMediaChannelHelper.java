
package tv.matchstick.server.fling.channels;

public interface IMediaChannelHelper {
    public abstract void attachMediaChannel(String sessionId);

    public abstract void sendPendingIntent(String sessionId);

    public abstract void detachMediaChannel(int statusCode);
}
