
package tv.matchstick.server.fling.socket;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLEngineResult;
import javax.net.ssl.SSLPeerUnverifiedException;

import tv.matchstick.server.fling.auth.FlingTrustManager;
import tv.matchstick.server.utils.LOG;

final class SSLSocketEngine {
    private static final LOG mLogs = new LOG("SSLSocketEngine");
    private static SSLContext mSSLContext;
    private SocketChannel mSocketChannel;
    private SocketBuf mReadSocketBuf;
    private SocketBuf mWriteSocketBuf;
    private ByteBuffer mReceivedByteBuffer;
    private ByteBuffer mWriteByteBuffer;
    private SSLEngine mSSLEngine;
    private javax.net.ssl.SSLEngineResult.HandshakeStatus mHandshakeStatus;
    private boolean mHandshakeFinished;
    private boolean mWillDisconnect;

    public SSLSocketEngine(SocketChannel socketchannel, SocketBuf atx1, SocketBuf atx2)
    {
        mSocketChannel = socketchannel;
        mReadSocketBuf = atx1;
        mWriteSocketBuf = atx2;
        mSSLEngine = mSSLContext.createSSLEngine();
        mSSLEngine.setUseClientMode(true);
        int channelBufferSize = mSSLEngine.getSession().getPacketBufferSize();

        mLogs.d("channelBufferSize = %d", channelBufferSize);
        mReceivedByteBuffer = ByteBuffer.allocate(channelBufferSize);
        mWriteByteBuffer = ByteBuffer.allocate(channelBufferSize);
        mHandshakeStatus = javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        mHandshakeFinished = false;
        mWillDisconnect = false;
    }

    private void checkHandshakeStatus(SSLEngineResult sslengineresult)
    {
        mHandshakeStatus = sslengineresult.getHandshakeStatus();
        switch (FlingSSLEngineResult.mHandshakeStatus[mHandshakeStatus.ordinal()])
        {
            default:
                return;

            case 1: // '\001' // FINISHED
            case 2: // '\002' // NOT_HANDSHAKING
                mHandshakeFinished = true;
                return;

            case 3: // '\003' // NEED_TASK
                do
                {
                    Runnable runnable = mSSLEngine.getDelegatedTask();
                    if (runnable != null)
                    {
                        runnable.run();
                    } else
                    {
                        mHandshakeStatus = mSSLEngine.getHandshakeStatus();
                        return;
                    }
                } while (true);
        }
    }

    private void writeToChannel_h() throws IOException
    {
        mWriteByteBuffer.flip();
        // try {
        int l = mSocketChannel.write(mWriteByteBuffer);
        if (l < 0)
        {
            mLogs.d("writeToChannel: throwing ClosedChannelException");
            throw new ClosedChannelException();
        } else
        {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(l);
            mLogs.v("writeToChannel: count %s", aobj);
            mWriteByteBuffer.compact();
            return;
        }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public final boolean isHandshakeFinished()
    {
        return mHandshakeFinished;
    }

    public final void beginHandshake()
    {
        try {
            mSSLEngine.beginHandshake();
            mHandshakeStatus = mSSLEngine.getHandshakeStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void disconnect()
    {
        mLogs.v("disconnect", new Object[0]);
        mWillDisconnect = true;
    }

    public final int getOperationMode()
    {
        boolean flag;
        boolean flag1;
        int l;

        flag = true;
        if (mSSLEngine.isInboundDone() || mWillDisconnect) {
            flag1 = false;
        } else {
            if (mHandshakeStatus == javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
            {
                flag1 = true;
            } else
            {
                if (mReadSocketBuf.e()) {
                    flag1 = false;
                } else {
                    flag1 = true;
                }
            }
        }

        if (flag1)
            l = ((flag) ? SelectionKey.OP_READ : 0); // ((flag) ? 1 : 0);
        else
            l = 0;
        if (mSSLEngine.isOutboundDone() || !mWillDisconnect
                && mHandshakeStatus != javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_WRAP
                && mWriteByteBuffer.position() <= 0 && mWriteSocketBuf.e)
            flag = false;
        if (flag)
            l |= SelectionKey.OP_WRITE; // 4;
        return l;
    }

    public final void readFromChannel() throws IOException
    {
        boolean flag1;
        boolean error_flag;

        int readCount = 0;

        if (mSSLEngine.isInboundDone()) {
            throw new ClosedChannelException();
        }

        try {
            readCount = mSocketChannel.read(mReceivedByteBuffer);
            if (readCount >= 0) {
                Object[] aobj1 = new Object[1];
                aobj1[0] = Integer.valueOf(readCount);
                mLogs.v("readFromChannel: count %s", aobj1);
                if (readCount > 0)
                    flag1 = true;
                else
                    flag1 = false;
                if (!flag1) {
                    return;
                } else {
                    error_flag = false;
                }
            } else {
                mLogs.d("readFromChannel: throwing ClosedChannelException");
                throw new ClosedChannelException();
            }
        } catch (ClosedChannelException closedchannelexception)
        {
            error_flag = true;
        }

        mReceivedByteBuffer.flip();

        while (true) {
            if (error_flag || mReceivedByteBuffer.remaining() <= 0) {
                mReceivedByteBuffer.clear();
                if (!error_flag) {
                    return;
                }

                mSSLEngine.closeInbound();
                return;
            }

            ByteBuffer destBuf[] = mReadSocketBuf.b();

            SSLEngineResult sslengineresult = null;

            sslengineresult = mSSLEngine.unwrap(mReceivedByteBuffer,
                    destBuf);

            Object aobj[] = new Object[4];
            aobj[0] = sslengineresult.getStatus();
            aobj[1] = sslengineresult.getHandshakeStatus();
            aobj[2] = Integer.valueOf(sslengineresult.bytesConsumed());
            aobj[3] = Integer.valueOf(sslengineresult.bytesProduced());
            mLogs.v(
                    "handleRead called SSLEngine.unwrap: status=%s handshakeStatus=%s bytesConsumed=%s bytesProduced=%s",
                    aobj);
            checkHandshakeStatus(sslengineresult);

            int l;

            if (destBuf == mReadSocketBuf.f)
                l = mReadSocketBuf.f[0].position() - mReadSocketBuf.c;
            else if (destBuf == mReadSocketBuf.g)
                l = (mReadSocketBuf.g[0].position() - mReadSocketBuf.c) + mReadSocketBuf.g[1].position();
            else
                throw new IllegalArgumentException();
            if (l > 0)
                mReadSocketBuf.b(l);
            switch (FlingSSLEngineResult.mStatus[sslengineresult.getStatus().ordinal()]) {
                case 1: // BUFFER_UNDERFLOW?
                    mReceivedByteBuffer.compact();
                    return;
                case 2: // BUFFER_OVERFLOW
                    throw new IOException("unexpected buffer overflow condition.");
                case 3: // CLOSED
                    throw new ClosedChannelException();
            }
        }
    }

    public final void writeToChannel() throws IOException
    {
        if (mSSLEngine.isOutboundDone()) {
            throw new ClosedChannelException();
        }

        if (mWriteByteBuffer.position() > 0)
        {
            writeToChannel_h();
            return;
        }
        do
        {
            ByteBuffer abytebuffer[] = mWriteSocketBuf.a();
            if (mWriteSocketBuf.e && mWillDisconnect)
                mSSLEngine.closeOutbound();
            SSLEngineResult sslengineresult = mSSLEngine
                    .wrap(abytebuffer, mWriteByteBuffer);

            Object aobj[] = new Object[4];
            aobj[0] = sslengineresult.getStatus();
            aobj[1] = sslengineresult.getHandshakeStatus();
            aobj[2] = Integer.valueOf(sslengineresult.bytesConsumed());
            aobj[3] = Integer.valueOf(sslengineresult.bytesProduced());
            mLogs.v(
                    "handleWrite called SSLEngine.wrap: status=%s handshakeStatus=%s bytesConsumed=%s bytesProduced=%s",
                    aobj);
            checkHandshakeStatus(sslengineresult);
            // C_atx atx1 = e;
            int l;
            if (abytebuffer == mWriteSocketBuf.f)
                l = mWriteSocketBuf.f[0].position() - mWriteSocketBuf.b;
            else if (abytebuffer == mWriteSocketBuf.g)
                l = (mWriteSocketBuf.g[0].position() - mWriteSocketBuf.b) + mWriteSocketBuf.g[1].position();
            else
                throw new IllegalArgumentException();
            if (l > 0)
                mWriteSocketBuf.a(l);
            switch (FlingSSLEngineResult.mStatus[sslengineresult.getStatus().ordinal()])
            {
                default:
                    if (mWriteByteBuffer.position() > 0)
                        writeToChannel_h();
                    if (mWriteSocketBuf.e
                            && mHandshakeStatus != javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_WRAP)
                        return;
                    break;

                case 1: // '\001'
                    throw new IOException("unexpected buffer underflow condition.");

                case 2: // '\002'
                    throw new IOException("unexpected buffer overflow condition.");
            }
        } while (true);
    }

    public final byte[] getPeerCertificate()
    {
        if (!mHandshakeFinished) {
            return null;
        }
        try
        {
            Certificate[] arrayOfCertificate = mSSLEngine.getSession().getPeerCertificates();
            if (arrayOfCertificate.length > 0)
            {
                return arrayOfCertificate[0].getEncoded();
            }
        } catch (CertificateEncodingException e)
        {
            e.printStackTrace();
            return null;
        } catch (SSLPeerUnverifiedException ex)
        {
            ex.printStackTrace();
        }

        return null;
    }

    static
    {
        try
        {
            mSSLContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
        }
        try
        {
            mSSLContext.init(null, FlingTrustManager.a, new SecureRandom());
        } catch (KeyManagementException keymanagementexception)
        {
            mLogs.e(keymanagementexception, "Failed SSLContext.init.", new Object[0]);
        }
    }
}
