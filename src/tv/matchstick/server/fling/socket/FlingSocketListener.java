
package tv.matchstick.server.fling.socket;

import java.nio.ByteBuffer;

public interface FlingSocketListener {
    public abstract void onConnected();

    public abstract void onConnectionFailed(int reason);

    public abstract void onMessageReceived(ByteBuffer message);

    public abstract void onDisconnected(int reason);
}
