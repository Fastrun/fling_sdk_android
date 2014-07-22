
package tv.matchstick.client.internal;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import tv.matchstick.client.internal.MessageSender;

public abstract class MediaControlChannel {

    private static final AtomicInteger xA = new AtomicInteger(0);
    protected final LogUtil mLogUtil;
    private final String mNamespace;
    private MessageSender mMessageSender;

    protected MediaControlChannel(String namespace, String logTag) {
        mNamespace = namespace;
        mLogUtil = new LogUtil(logTag);
        mLogUtil.U(String.format("instance-%d",
                new Object[] {
                    Integer.valueOf(xA.incrementAndGet())
                }));
    }

    public final String getNamespace() {
        return mNamespace;
    }

    public final void setMessageSender(MessageSender paramdw) {
        mMessageSender = paramdw;
        if (mMessageSender != null) {
            return;
        }
        clean();
    }

    protected final void sendTextMessage(String message, long requestId,
            String paramString2) throws IOException {
        mLogUtil.logv("Sending text message: %s to: %s", new Object[] {
                message, paramString2
        });
        mMessageSender.sendMessage(mNamespace, message, requestId,
                paramString2);
    }

    public void onMessageReceived(String message) {
    }

    public void trackUnSuccess(long requestId, int statusCode) {
    }

    protected final long getRequestId() {
        return mMessageSender.getRequestId();
    }

    public void clean() {
    }
}
