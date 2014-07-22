
package tv.matchstick.client.internal;

import java.io.IOException;

public interface MessageSender {

    public void sendMessage(String namespace, String message, long requestId,
            String paramString3) throws IOException;

    public long getRequestId();

}
