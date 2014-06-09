
package com.fireflycast.server.cast.bridge;

import com.fireflycast.server.cast.ApplicationMetadata;

public interface ICastSrvController {
    public abstract void onConnected_a();

    public abstract void onDisconnected_a(int disconnectStatusCode);

    public abstract void onApplicationConnected_a(ApplicationMetadata applicationmetadata,
            String statusText, String sessionId, boolean flag);

    public abstract void onVolumeChanged_a(String statusText, double volume, boolean muteState);

    public abstract void a(String namespace, long id);

    public abstract void a(String namespace, long id, int result);

    public abstract void notifyOnMessageReceived_a(String s, String s1);

    public abstract void a(String s, byte abyte0[]);

    public abstract void onConnectedWithoutApp_b();

    public abstract void onApplicationConnectionFailed_b(int result);

    public abstract void onConnectionFailed_c();

    public abstract void onRequestStatus_c(int i);

    public abstract void onInvalidRequest_d();

    public abstract void onApplicationDisconnected_d(int statusCode);
}
