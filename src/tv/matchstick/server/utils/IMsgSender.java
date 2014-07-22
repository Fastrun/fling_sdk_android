
package tv.matchstick.server.utils;

public interface IMsgSender {
    public abstract long getId();

    public abstract void sendMessage(String namespace, String message, long requestId, String transId);

    public abstract void sendBinaryMessage(String namespace, byte message[], long requestId, String transId);
}
