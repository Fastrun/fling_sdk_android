
package com.fireflycast.server.cast.socket;

import com.fireflycast.server.cast.auth.CastX509TrustManager;
import com.fireflycast.server.utils.Logs;

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

final class SSLSocketEngine {
    private static final Logs mLogs_a = new Logs("SSLSocketEngine");
    private static SSLContext mSSLContext_b;
    private SocketChannel mSocketChannel_c;
    private SocketBuf mReadAtx_d;
    private SocketBuf mWriteAtx_e;
    private ByteBuffer mReceivedByteBuffer_f;
    private ByteBuffer mWriteByteBuffer_g;
    private SSLEngine mSSLEngine_h;
    private javax.net.ssl.SSLEngineResult.HandshakeStatus mHandshakeStatus_i;
    private boolean mHandshakeFinished_j;
    private boolean mWillDisconnect_k;

    public SSLSocketEngine(SocketChannel socketchannel, SocketBuf atx1, SocketBuf atx2)
    {
        mSocketChannel_c = socketchannel;
        mReadAtx_d = atx1;
        mWriteAtx_e = atx2;
        mSSLEngine_h = mSSLContext_b.createSSLEngine();
        mSSLEngine_h.setUseClientMode(true);
        int channelBufferSize = mSSLEngine_h.getSession().getPacketBufferSize();

        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(channelBufferSize);
        mLogs_a.d("channelBufferSize = %d", aobj);
        mReceivedByteBuffer_f = ByteBuffer.allocate(channelBufferSize);
        mWriteByteBuffer_g = ByteBuffer.allocate(channelBufferSize);
        mHandshakeStatus_i = javax.net.ssl.SSLEngineResult.HandshakeStatus.NOT_HANDSHAKING;
        mHandshakeFinished_j = false;
        mWillDisconnect_k = false;
    }

    private void checkHandshakeStatus_a(SSLEngineResult sslengineresult)
    {
        mHandshakeStatus_i = sslengineresult.getHandshakeStatus();
        switch (CastSSLEngineResult.mHandshakeStatus_b[mHandshakeStatus_i.ordinal()])
        {
            default:
                return;

            case 1: // '\001' // FINISHED
            case 2: // '\002' // NOT_HANDSHAKING
                mHandshakeFinished_j = true;
                return;

            case 3: // '\003' // NEED_TASK
                do
                {
                    Runnable runnable = mSSLEngine_h.getDelegatedTask();
                    if (runnable != null)
                    {
                        runnable.run();
                    } else
                    {
                        mHandshakeStatus_i = mSSLEngine_h.getHandshakeStatus();
                        return;
                    }
                } while (true);
        }
    }

    private void writeToChannel_h() throws IOException
    {
        mWriteByteBuffer_g.flip();
        // try {
        int l = mSocketChannel_c.write(mWriteByteBuffer_g);
        if (l < 0)
        {
            mLogs_a.d("writeToChannel: throwing ClosedChannelException", new Object[0]);
            throw new ClosedChannelException();
        } else
        {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(l);
            mLogs_a.v("writeToChannel: count %s", aobj);
            mWriteByteBuffer_g.compact();
            return;
        }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

    public final boolean isHandshakeFinished_a()
    {
        return mHandshakeFinished_j;
    }

    public final void beginHandshake_b()
    {
        try {
            mSSLEngine_h.beginHandshake();
            mHandshakeStatus_i = mSSLEngine_h.getHandshakeStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void disconnect_c()
    {
        mLogs_a.v("disconnect", new Object[0]);
        mWillDisconnect_k = true;
    }

    public final int getOperationMode_d()
    {
        boolean flag;
        boolean flag1;
        int l;

        flag = true;
        if (mSSLEngine_h.isInboundDone() || mWillDisconnect_k) {
            flag1 = false;
        } else {
            if (mHandshakeStatus_i == javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_UNWRAP)
            {
                flag1 = true;
            } else
            {
                if (mReadAtx_d.e()) {
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
        if (mSSLEngine_h.isOutboundDone() || !mWillDisconnect_k
                && mHandshakeStatus_i != javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_WRAP
                && mWriteByteBuffer_g.position() <= 0 && mWriteAtx_e.e)
            flag = false;
        if (flag)
            l |= SelectionKey.OP_WRITE; // 4;
        return l;
    }

    public final void readFromChannel_e() throws IOException
    {
        boolean flag1;
        boolean error_flag;

        int readCount = 0;

        if (mSSLEngine_h.isInboundDone()) {
            throw new ClosedChannelException();
        }

        try {
            readCount = mSocketChannel_c.read(mReceivedByteBuffer_f);
            if (readCount >= 0) {
                Object[] aobj1 = new Object[1];
                aobj1[0] = Integer.valueOf(readCount);
                mLogs_a.v("readFromChannel: count %s", aobj1);
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
                mLogs_a.d("readFromChannel: throwing ClosedChannelException", new Object[0]);
                throw new ClosedChannelException();
            }
        } catch (ClosedChannelException closedchannelexception)
        {
            error_flag = true;
        }

        mReceivedByteBuffer_f.flip();

        while (true) {
            if (error_flag || mReceivedByteBuffer_f.remaining() <= 0) {
                mReceivedByteBuffer_f.clear();
                if (!error_flag) {
                    return;
                }

                mSSLEngine_h.closeInbound();
                return;
            }

            ByteBuffer destBuf[] = mReadAtx_d.b();

            SSLEngineResult sslengineresult = null;

            sslengineresult = mSSLEngine_h.unwrap(mReceivedByteBuffer_f,
                    destBuf);

            Object aobj[] = new Object[4];
            aobj[0] = sslengineresult.getStatus();
            aobj[1] = sslengineresult.getHandshakeStatus();
            aobj[2] = Integer.valueOf(sslengineresult.bytesConsumed());
            aobj[3] = Integer.valueOf(sslengineresult.bytesProduced());
            mLogs_a.v(
                    "handleRead called SSLEngine.unwrap: status=%s handshakeStatus=%s bytesConsumed=%s bytesProduced=%s",
                    aobj);
            checkHandshakeStatus_a(sslengineresult);

            int l;

            if (destBuf == mReadAtx_d.f)
                l = mReadAtx_d.f[0].position() - mReadAtx_d.c;
            else if (destBuf == mReadAtx_d.g)
                l = (mReadAtx_d.g[0].position() - mReadAtx_d.c) + mReadAtx_d.g[1].position();
            else
                throw new IllegalArgumentException();
            if (l > 0)
                mReadAtx_d.b(l);
            switch (CastSSLEngineResult.mStatus_a[sslengineresult.getStatus().ordinal()]) {
                case 1: // BUFFER_UNDERFLOW?
                    mReceivedByteBuffer_f.compact();
                    return;
                case 2: // BUFFER_OVERFLOW
                    throw new IOException("unexpected buffer overflow condition.");
                case 3: // CLOSED
                    throw new ClosedChannelException();
            }
        }
    }

    public final void writeToChannel_f() throws IOException
    {
        if (mSSLEngine_h.isOutboundDone()) {
            throw new ClosedChannelException();
        }

        if (mWriteByteBuffer_g.position() > 0)
        {
            writeToChannel_h();
            return;
        }
        do
        {
            ByteBuffer abytebuffer[] = mWriteAtx_e.a();
            if (mWriteAtx_e.e && mWillDisconnect_k)
                mSSLEngine_h.closeOutbound();
            SSLEngineResult sslengineresult = mSSLEngine_h
                    .wrap(abytebuffer, mWriteByteBuffer_g);

            Object aobj[] = new Object[4];
            aobj[0] = sslengineresult.getStatus();
            aobj[1] = sslengineresult.getHandshakeStatus();
            aobj[2] = Integer.valueOf(sslengineresult.bytesConsumed());
            aobj[3] = Integer.valueOf(sslengineresult.bytesProduced());
            mLogs_a.v(
                    "handleWrite called SSLEngine.wrap: status=%s handshakeStatus=%s bytesConsumed=%s bytesProduced=%s",
                    aobj);
            checkHandshakeStatus_a(sslengineresult);
            // C_atx atx1 = e;
            int l;
            if (abytebuffer == mWriteAtx_e.f)
                l = mWriteAtx_e.f[0].position() - mWriteAtx_e.b;
            else if (abytebuffer == mWriteAtx_e.g)
                l = (mWriteAtx_e.g[0].position() - mWriteAtx_e.b) + mWriteAtx_e.g[1].position();
            else
                throw new IllegalArgumentException();
            if (l > 0)
                mWriteAtx_e.a(l);
            switch (CastSSLEngineResult.mStatus_a[sslengineresult.getStatus().ordinal()])
            {
                default:
                    if (mWriteByteBuffer_g.position() > 0)
                        writeToChannel_h();
                    if (mWriteAtx_e.e
                            && mHandshakeStatus_i != javax.net.ssl.SSLEngineResult.HandshakeStatus.NEED_WRAP)
                        return;
                    break;

                case 1: // '\001'
                    throw new IOException("unexpected buffer underflow condition.");

                case 2: // '\002'
                    throw new IOException("unexpected buffer overflow condition.");
            }
        } while (true);
    }

    public final byte[] getPeerCertificate_g()
    {
        if (!mHandshakeFinished_j) {
            return null;
        }
        try
        {
            Certificate[] arrayOfCertificate = mSSLEngine_h.getSession().getPeerCertificates();
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
            mSSLContext_b = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException nosuchalgorithmexception) {
        }
        try
        {
            mSSLContext_b.init(null, CastX509TrustManager.a, new SecureRandom());
        } catch (KeyManagementException keymanagementexception)
        {
            mLogs_a.e(keymanagementexception, "Failed SSLContext.init.", new Object[0]);
        }
    }
}
