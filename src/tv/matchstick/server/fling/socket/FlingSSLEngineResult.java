
package tv.matchstick.server.fling.socket;

final class FlingSSLEngineResult {
    static final int mStatus[];
    static final int mHandshakeStatus[];

    static
    {
        mHandshakeStatus = new int[javax.net.ssl.SSLEngineResult.HandshakeStatus.values().length];
        try
        {
            mHandshakeStatus[javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
        try
        {
            mHandshakeStatus[javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING
                    .ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }
        try
        {
            mHandshakeStatus[javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_TASK.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }
        mStatus = new int[javax.net.ssl.SSLEngineResult.Status.values().length];
        try
        {
            mStatus[javax.net.ssl.SSLEngineResult.Status.BUFFER_UNDERFLOW.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror3) {
        }
        try
        {
            mStatus[javax.net.ssl.SSLEngineResult.Status.BUFFER_OVERFLOW.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror4) {
        }
        try
        {
            mStatus[javax.net.ssl.SSLEngineResult.Status.CLOSED.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror5) {
        }
        try
        {
            mStatus[javax.net.ssl.SSLEngineResult.Status.OK.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror6)
        {
        }
    }
}
