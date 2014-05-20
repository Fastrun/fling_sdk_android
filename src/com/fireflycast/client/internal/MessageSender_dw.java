
package com.fireflycast.client.internal;

import java.io.IOException;

public interface MessageSender_dw {

    public void sendMessage_a(String namespace, String message, long requestId,
            String paramString3) throws IOException;

    public long getRequestId_cV();

}
