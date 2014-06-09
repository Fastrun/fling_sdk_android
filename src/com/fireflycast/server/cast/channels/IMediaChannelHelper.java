
package com.fireflycast.server.cast.channels;

public interface IMediaChannelHelper {
    public abstract void attachMediaChannel_a(String sessionId);

    public abstract void sendPendingIntent_b(String sessionId);

    public abstract void detachMediaChannel_d(int statusCode);
}
