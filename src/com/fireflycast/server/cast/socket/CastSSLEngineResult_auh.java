
package com.fireflycast.server.cast.socket;

final class CastSSLEngineResult_auh {
    static final int mStatus_a[];
    static final int mHandshakeStatus_b[];

    static
    {
        mHandshakeStatus_b = new int[javax.net.ssl.SSLEngineResult.HandshakeStatus.values().length];
        try
        {
            mHandshakeStatus_b[javax.net.ssl.SSLEngineResult.HandshakeStatus.FINISHED.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror) {
        }
        try
        {
            mHandshakeStatus_b[javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING
                    .ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror1) {
        }
        try
        {
            mHandshakeStatus_b[javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_TASK.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror2) {
        }
        mStatus_a = new int[javax.net.ssl.SSLEngineResult.Status.values().length];
        try
        {
            mStatus_a[javax.net.ssl.SSLEngineResult.Status.BUFFER_UNDERFLOW.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror3) {
        }
        try
        {
            mStatus_a[javax.net.ssl.SSLEngineResult.Status.BUFFER_OVERFLOW.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror4) {
        }
        try
        {
            mStatus_a[javax.net.ssl.SSLEngineResult.Status.CLOSED.ordinal()] = 3;
        } catch (NoSuchFieldError nosuchfielderror5) {
        }
        try
        {
            mStatus_a[javax.net.ssl.SSLEngineResult.Status.OK.ordinal()] = 4;
        } catch (NoSuchFieldError nosuchfielderror6)
        {
        }
    }
}
