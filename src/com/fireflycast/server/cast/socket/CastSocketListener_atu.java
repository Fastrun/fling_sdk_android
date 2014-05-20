
package com.fireflycast.server.cast.socket;

import java.nio.ByteBuffer;

public interface CastSocketListener_atu {
    public abstract void onConnected_a();

    public abstract void onConnectionFailed_a(int reason);

    public abstract void onMessageReceived_a(ByteBuffer bytebuffer);

    public abstract void onDisconnected_b(int reason);
}
