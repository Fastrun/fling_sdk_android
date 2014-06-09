
package com.fireflycast.client.internal;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import com.fireflycast.client.internal.MessageSender;

public abstract class MediaControlChannel {

    private static final AtomicInteger xA = new AtomicInteger(0);
    protected final LogUtil mLogUtil_xB;
    private final String mNamespace_xC;
    private MessageSender mMessageSender_xD;

    protected MediaControlChannel(String namespace, String logTag) {
        mNamespace_xC = namespace;
        mLogUtil_xB = new LogUtil(logTag);
        mLogUtil_xB.U(String.format("instance-%d",
                new Object[] {
                    Integer.valueOf(xA.incrementAndGet())
                }));
    }

    public final String getNamespace() {
        return mNamespace_xC;
    }

    public final void setMessageSender_a(MessageSender paramdw) {
        mMessageSender_xD = paramdw;
        if (mMessageSender_xD != null) {
            return;
        }
        clean_cX();
    }

    protected final void sendTextMessage_a(String message, long requestId,
            String paramString2) throws IOException {
        mLogUtil_xB.logv_a("Sending text message: %s to: %s", new Object[] {
                message, paramString2
        });
        mMessageSender_xD.sendMessage_a(mNamespace_xC, message, requestId,
                paramString2);
    }

    public void onMessageReceived_P(String message) {
    }

    public void trackUnSuccess_a(long requestId, int statusCode) {
    }

    protected final long getRequestId_cW() {
        return mMessageSender_xD.getRequestId_cV();
    }

    public void clean_cX() {
    }
}
