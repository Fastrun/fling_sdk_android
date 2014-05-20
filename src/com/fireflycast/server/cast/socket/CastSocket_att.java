
package com.fireflycast.server.cast.socket;

import android.content.Context;
import android.os.SystemClock;

import com.fireflycast.server.cast.socket.data.C_axm;
import com.fireflycast.server.common.exception.C_atq;
import com.fireflycast.server.common.exception.C_auf;
import com.fireflycast.server.utils.Logs_avu;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import javax.net.ssl.SSLException;

public final class CastSocket_att {
    private static final Logs_avu mLogs_a = new Logs_avu("CastSocket");
    private static final boolean DEBUG = false;
    private final CastSocketListener_atu mSocketListener_b;
    private SocketChannel mSocketChannel_c;
    private SocketBuf_atx mReadAtx_d;
    private SocketBuf_atx mWriteAtx_e;
    private final CastSocketMultiplexer_atv mCastSocketMultiplexer_f;
    private final int g;
    private int mSocketStatus_h;
    private InetSocketAddress mInetSocketAddress_i;
    private long mBeginConnectTime_j;
    private long mTimeoutTime_k;
    private long mDisconnectTime_l;
    private long m;
    private boolean n;
    private SSLSocketEngine_aug mSSLSocketEngine_o;

    public CastSocket_att(Context context, CastSocketListener_atu listener_atu1)
    {
        if (listener_atu1 == null)
        {
            throw new IllegalArgumentException("listener cannot be null");
        } else {
            mSocketListener_b = listener_atu1;
            mSocketStatus_h = 0;
            g = 0x1fffc;
            mCastSocketMultiplexer_f = CastSocketMultiplexer_atv.getInstance_a(context);
            mSSLSocketEngine_o = null;
            return;
        }
    }

    private void doTeardown_a(int reason_i1)
    {
        Logs_avu avu1 = mLogs_a;
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(reason_i1);
        avu1.d("doTeardown with reason=%d", aobj);
        mSSLSocketEngine_o = null;
        if (mSocketChannel_c != null)
        {
            boolean flag;
            try
            {
                mSocketChannel_c.close();
            } catch (IOException ioexception) {
            }
            mSocketChannel_c = null;
        }
        mReadAtx_d = null;
        mWriteAtx_e = null;
        boolean flag = false;
        if (mSocketStatus_h == 1) // connecting
            flag = true;
        else
            flag = false;
        mSocketStatus_h = 0;
        mDisconnectTime_l = 0L;
        mBeginConnectTime_j = 0L;
        n = true;
        if (flag)
        {
            mSocketListener_b.onConnectionFailed_a(reason_i1);
            return;
        } else
        {
            mSocketListener_b.onDisconnected_b(reason_i1);
            return;
        }
    }

    private synchronized void connect_b(Inet4Address hostAddr, int port)
    {
        try {
            mCastSocketMultiplexer_f.init_a();

            Object aobj[] = new Object[2];
            aobj[0] = hostAddr;
            aobj[1] = Integer.valueOf(port);
            mLogs_a.d("Connecting to %s:%d", aobj);
            mInetSocketAddress_i = new InetSocketAddress(hostAddr, port);
            mTimeoutTime_k = 50000L;// 5000L;
            m = 2000L;
            mCastSocketMultiplexer_f.doConnect_a(this);
            mSocketStatus_h = 1; // connecting
            n = false;

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRead_m() throws IOException
    {
        boolean flag = false;
        Long long1;

        while (true) {
            if (mReadAtx_d.e) {
                flag = false;
            } else {
                mReadAtx_d.d = mReadAtx_d.b;
                if (mReadAtx_d.d() < 4) {
                    long1 = null;
                } else {
                    // cond_2
                    long1 = Long.valueOf((long) (0xff & mReadAtx_d.f()) << 24
                            | (long) (0xff & mReadAtx_d.f()) << 16
                            | (long) (0xff & mReadAtx_d.f()) << 8 | (long) (0xff & mReadAtx_d.f()));
                }
                // :goto_1
                if (long1 == null)
                {
                    flag = true;
                } else
                {
                    if (long1.longValue() > (long) g) {
                        throw new IOException("invalid message size received");
                    }

                    // cond_4
                    if ((long) mReadAtx_d.d() >= long1.longValue()) {
                        // cond_5
                        byte abyte0[] = new byte[(int) long1.longValue()];
                        // C_atx atx5 = d;
                        int i1 = abyte0.length;
                        if (mReadAtx_d.d() >= i1)
                        {
                            int j1;
                            int k1;
                            int l1;
                            if (mReadAtx_d.e)
                                j1 = 0;
                            else if (mReadAtx_d.b < mReadAtx_d.c)
                                j1 = mReadAtx_d.c - mReadAtx_d.b;
                            else
                                j1 = mReadAtx_d.a.length - mReadAtx_d.b;
                            k1 = Math.min(i1, j1);
                            System.arraycopy(mReadAtx_d.a, mReadAtx_d.b, abyte0, 0, k1);
                            mReadAtx_d.a(k1);
                            l1 = i1 - k1;
                            if (l1 > 0)
                            {
                                int i2 = k1 + 0;
                                System.arraycopy(mReadAtx_d.a, mReadAtx_d.b, abyte0, i2, l1);
                                mReadAtx_d.a(l1);
                            }
                        }
                        // cond_6
                        payload("received", ByteBuffer.wrap(abyte0));
                        mSocketListener_b.onMessageReceived_a(ByteBuffer.wrap(abyte0));
                        continue;
                    } else {
                        flag = true;
                    }
                }
            }

            if (flag) {
                if (mReadAtx_d.d != -1)
                {
                    if (mReadAtx_d.b != mReadAtx_d.d)
                    {
                        mReadAtx_d.b = mReadAtx_d.d;
                        mReadAtx_d.e = false;
                    }
                    mReadAtx_d.d = -1;
                }
                return;
            } else {
                mReadAtx_d.d = -1;
                if (!mReadAtx_d.e) {
                    return;
                }

                mReadAtx_d.c = 0;
                mReadAtx_d.b = 0;
                return;
            }
        }
    }

    private void payload(String tag, ByteBuffer bytebuffer) {
        if (DEBUG) {
            C_axm axm1 = C_axm.a(bytebuffer.array());
            android.util.Log.d("CastSocket", tag);
            android.util.Log.d("CastSocket", axm1.payload());
        }
    }

    final synchronized SocketChannel startConnect_a() throws IOException
    {
        mLogs_a.d("startConnect", new Object[0]);

        // try {
        mBeginConnectTime_j = SystemClock.elapsedRealtime();
        mSocketChannel_c = SocketChannel.open();
        mSocketChannel_c.configureBlocking(false);
        mReadAtx_d = new SocketBuf_atx();
        mWriteAtx_e = new SocketBuf_atx();
        mSSLSocketEngine_o = new SSLSocketEngine_aug(mSocketChannel_c,
                mReadAtx_d, mWriteAtx_e);
        if (mSocketChannel_c.connect(mInetSocketAddress_i))
        {
            if (mSSLSocketEngine_o == null) {
                mSocketStatus_h = 2;
                mSocketListener_b.onConnected_a();
                return mSocketChannel_c;
            }
            mSSLSocketEngine_o.beginHandshake_b();
        }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        return mSocketChannel_c;
    }

    public final void connect_a(Inet4Address hostAddress, int port)
    {
        connect_b(hostAddress, port);
    }

    public final synchronized void send_a(ByteBuffer bytebuffer) throws IOException
    {
        if (mSocketStatus_h != 2)
            throw new IllegalStateException("not connected; state=" + this.mSocketStatus_h);

        if (bytebuffer == null)
            throw new IllegalArgumentException("message cannot be null");

        // try {
        if (this.mWriteAtx_e.c() < 4 + bytebuffer.remaining())
            throw new C_atq();
        // } catch (Exception e) {
        // e.printStackTrace();
        // return;
        // }

        long remainingCount = bytebuffer.remaining();
        if (mWriteAtx_e.c() >= 4)
        {
            if ((remainingCount < 0L) || (remainingCount > 0xffffffffL))
                throw new C_auf(remainingCount + " is not a valid uint32 value");
            mWriteAtx_e.a((byte) (int) (0xFF & remainingCount >> 24));
            mWriteAtx_e.a((byte) (int) (0xFF & remainingCount >> 16));
            mWriteAtx_e.a((byte) (int) (0xFF & remainingCount >> 8));
            mWriteAtx_e.a((byte) (int) (remainingCount & 0xFF));
        }

        byte[] sendBuf = bytebuffer.array();
        int pos = bytebuffer.position();
        int remain = bytebuffer.remaining();
        int writeCount = 0;
        if (mWriteAtx_e.c() >= remain)
        {
            if (!mWriteAtx_e.e) {
                if (mWriteAtx_e.c < mWriteAtx_e.b)
                {
                    writeCount = mWriteAtx_e.b - mWriteAtx_e.c;
                }
                else
                {
                    writeCount = mWriteAtx_e.a.length - mWriteAtx_e.c;
                }
            } else {
                writeCount = mWriteAtx_e.a.length;
            }
        } else {
            this.mCastSocketMultiplexer_f.wakeup_b();
            return;
        }

        int actualWriteCount = Math.min(remain, writeCount);
        System.arraycopy(sendBuf, pos, mWriteAtx_e.a, mWriteAtx_e.c, actualWriteCount);
        mWriteAtx_e.b(actualWriteCount);
        int currentPos = pos + actualWriteCount;
        int remainedCount = remain - actualWriteCount;
        if (remainedCount > 0)
        {
            System.arraycopy(sendBuf, currentPos, mWriteAtx_e.a, mWriteAtx_e.c, remainedCount);
            mWriteAtx_e.b(remainedCount);
        }
        payload("send", bytebuffer);
        this.mCastSocketMultiplexer_f.wakeup_b();
    }

    final synchronized boolean checkInterestOps_a(SelectionKey selectionkey, long elapsedRealtime_l1)
    {
        boolean ok = false;
        if (n) {
            mLogs_a.w("Socket is no longer connected", new Object[0]);
            n = false;
            return false;
        }

        int mode = 0;
        switch (mSocketStatus_h) {
            case 1: // conncting?
                if (elapsedRealtime_l1 - this.mBeginConnectTime_j >= this.mTimeoutTime_k)
                {
                    doTeardown_a(3);
                } else {
                    if (mSocketChannel_c.isConnected()) {
                        if (mSSLSocketEngine_o != null) {
                            mode = 0 | mSSLSocketEngine_o.getOperationMode_d();
                        }
                    } else {
                        mode = SelectionKey.OP_CONNECT;// 8;
                    }
                    selectionkey.interestOps(mode);
                    ok = true;
                }
                break;
            case 2: // connected
                if (mSSLSocketEngine_o != null)
                {
                    mode = 0 | mSSLSocketEngine_o.getOperationMode_d();
                } else {
                    boolean flag1 = mReadAtx_d.e();
                    mode = 0;
                    if (!flag1) {
                        mode = SelectionKey.OP_READ; // 1;
                    }
                    if (!mWriteAtx_e.e) {
                        mode |= SelectionKey.OP_WRITE; // 4;
                    }
                }
                selectionkey.interestOps(mode);
                ok = true;
                break;
            case 3:// disconnecting?
                if (elapsedRealtime_l1 - mDisconnectTime_l < m) {
                    if (mSSLSocketEngine_o != null) {
                        mode = 0 | mSSLSocketEngine_o.getOperationMode_d();
                    } else {
                        if (mWriteAtx_e.e) {
                            doTeardown_a(0);
                            ok = false;
                            break;
                        }

                        mode = SelectionKey.OP_WRITE;// 4;
                    }

                    selectionkey.interestOps(mode);
                    ok = true;
                } else {
                    doTeardown_a(0);
                    ok = false;
                }

                break;
        }

        return ok;
    }

    public final synchronized void disconnect_b()
    {
        mSocketStatus_h = 3;
        mDisconnectTime_l = SystemClock.elapsedRealtime();
        if (mSSLSocketEngine_o != null) {
            mSSLSocketEngine_o.disconnect_c();
        }
        mCastSocketMultiplexer_f.wakeup_b();
    }

    public final synchronized boolean isConnected_c()
    {
        return (mSocketStatus_h == 2);
    }

    public final synchronized boolean isConnecting_d()
    {
        return (mSocketStatus_h == 1);
    }

    public final synchronized boolean isDisconnecting_e()
    {
        boolean flag = false;
        if (mSocketStatus_h == 3) {
            flag = true;
        }

        return flag;
    }

    public final synchronized int getState_f()
    {
        return mSocketStatus_h;
    }

    public final synchronized byte[] getPeerCertificate_g()
    {
        if (mSSLSocketEngine_o == null) {
            return null;
        }

        return mSSLSocketEngine_o.getPeerCertificate_g();
    }

    final synchronized boolean onConnectable_h()
    {
        boolean flag = true;

        mLogs_a.d("onConnectable", new Object[0]);
        try {
            mSocketChannel_c.finishConnect();

            if (mSSLSocketEngine_o != null) {
                mSSLSocketEngine_o.beginHandshake_b();

                return true;
            }
            mSocketStatus_h = 2;
            mSocketListener_b.onConnected_a();
        } catch (SSLException e) {
            mLogs_a.d(e, "exception in onConnectable", new Object[0]);
            doTeardown_a(4);
            flag = false;
        } catch (IOException ex) {
            mLogs_a.d(ex, "exception in onConnectable", new Object[0]);
            doTeardown_a(2);
            flag = false;
        }

        return flag;
    }

    final synchronized void onConnectError_i()
    {
        mLogs_a.d("onConnectError", new Object[0]);
        try {
            doTeardown_a(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final synchronized boolean onRead_j()
    {
        boolean flag = true;
        try {
            if (mSSLSocketEngine_o == null) {
                // cond_1
                if (!mReadAtx_d.e()) {
                    int i1 = (int) mSocketChannel_c.read(mReadAtx_d.b());
                    if (i1 <= 0) {
                        throw new ClosedChannelException();
                    }

                    mReadAtx_d.b(i1);
                }
            } else {
                mSSLSocketEngine_o.readFromChannel_e();
                if (mSocketStatus_h == 1 && mSSLSocketEngine_o.isHandshakeFinished_a()) {
                    mSocketStatus_h = 2;
                    mSocketListener_b.onConnected_a();
                }
            }

            handleRead_m();
        } catch (ClosedChannelException e) {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(mSocketStatus_h);
            mLogs_a.d(e, "ClosedChannelException when state was %d", aobj);
            doTeardown_a(1);
            flag = false;
        } catch (SSLException ee) {

            mLogs_a.d(ee, "SSLException encountered. Tearing down the socket.", new Object[0]);
            doTeardown_a(4);
            flag = false;
        } catch (IOException ex) {
            mLogs_a.d(ex, "IOException encountered. Tearing down the socket.", new Object[0]);
            doTeardown_a(2);
            flag = false;
        }

        return flag;

    }

    final synchronized boolean onWrite_k()
    {
        boolean flag = false;

        try {
            if (mSSLSocketEngine_o == null) {
                if (!mWriteAtx_e.e) {

                    int i1 = (int) mSocketChannel_c.write(mWriteAtx_e.a());
                    if (i1 <= 0) {
                        // cond_2
                        throw new ClosedChannelException();
                    }
                    mWriteAtx_e.a(i1);
                }
            } else {
                mSSLSocketEngine_o.writeToChannel_f();
                if (mSocketStatus_h == 1 && mSSLSocketEngine_o.isHandshakeFinished_a())
                {
                    mSocketStatus_h = 2;
                    mSocketListener_b.onConnected_a();
                }
            }

            // cond_0
            if (!mWriteAtx_e.e || mSocketStatus_h != 3) {
                return true;
            }

            doTeardown_a(0);
        } catch (ClosedChannelException e) {
            Object aobj[] = new Object[1];
            aobj[0] = Integer.valueOf(mSocketStatus_h);
            mLogs_a.d(e, "ClosedChannelException when state was %d", aobj);
            doTeardown_a(1);
        } catch (SSLException ex) {
            mLogs_a.d(ex, "SSLException encountered. Tearing down the socket.", new Object[0]);
            doTeardown_a(4);
            flag = false;
        } catch (IOException eio) {
            mLogs_a.d(eio, "IOException encountered. Tearing down the socket.", new Object[0]);
            doTeardown_a(2);
            flag = false;
        }

        return flag;
    }

    final synchronized SocketChannel getSocketChannel_l()
    {
        return mSocketChannel_c;
    }
}
