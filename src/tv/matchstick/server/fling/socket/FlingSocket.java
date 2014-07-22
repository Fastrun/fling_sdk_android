
package tv.matchstick.server.fling.socket;

import android.content.Context;
import android.os.SystemClock;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import javax.net.ssl.SSLException;

import tv.matchstick.server.common.exception.C_atq;
import tv.matchstick.server.common.exception.C_auf;
import tv.matchstick.server.fling.socket.data.C_axm;
import tv.matchstick.server.utils.LOG;

public final class FlingSocket {
    private static final LOG mLogs = new LOG("FlingSocket");
    private static final boolean DEBUG = false;
    private final FlingSocketListener mSocketListener;
    private SocketChannel mSocketChannel;
    private SocketBuf mReadSocketBuf;
    private SocketBuf mWriteSocketBuf;
    private final FlingSocketMultiplexer mFlingSocketMultiplexer;
    private final int g;
    private int mSocketStatus;
    private InetSocketAddress mInetSocketAddress;
    private long mBeginConnectTime;
    private long mTimeoutTime;
    private long mDisconnectTime;
    private long m;
    private boolean n;
    private SSLSocketEngine mSSLSocketEngine;

    public FlingSocket(Context context, FlingSocketListener listener_atu1)
    {
        if (listener_atu1 == null)
        {
            throw new IllegalArgumentException("listener cannot be null");
        } else {
            mSocketListener = listener_atu1;
            mSocketStatus = 0;
            g = 0x1fffc;
            mFlingSocketMultiplexer = FlingSocketMultiplexer.getInstance(context);
            mSSLSocketEngine = null;
            return;
        }
    }

    private void doTeardown(int reason_i1)
    {
        mLogs.d("doTeardown with reason=%d", reason_i1);
        
        mSSLSocketEngine = null;
        if (mSocketChannel != null)
        {
            boolean flag;
            try
            {
                mSocketChannel.close();
            } catch (IOException ioexception) {
            }
            mSocketChannel = null;
        }
        mReadSocketBuf = null;
        mWriteSocketBuf = null;
        boolean flag = false;
        if (mSocketStatus == 1) // connecting
            flag = true;
        else
            flag = false;
        mSocketStatus = 0;
        mDisconnectTime = 0L;
        mBeginConnectTime = 0L;
        n = true;
        if (flag)
        {
            mSocketListener.onConnectionFailed(reason_i1);
            return;
        } else
        {
            mSocketListener.onDisconnected(reason_i1);
            return;
        }
    }

    private synchronized void connect_b(Inet4Address hostAddr, int port)
    {
        try {
            mFlingSocketMultiplexer.init();
            mLogs.d("Connecting to %s:%d", hostAddr, port);
            mInetSocketAddress = new InetSocketAddress(hostAddr, port);
            mTimeoutTime = 50000L;// 5000L;
            m = 2000L;
            mFlingSocketMultiplexer.doConnect(this);
            mSocketStatus = 1; // connecting
            n = false;

            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRead() throws IOException
    {
        boolean flag = false;
        Long long1;

        while (true) {
            if (mReadSocketBuf.e) {
                flag = false;
            } else {
                mReadSocketBuf.d = mReadSocketBuf.b;
                if (mReadSocketBuf.d() < 4) {
                    long1 = null;
                } else {
                    // cond_2
                    long1 = Long.valueOf((long) (0xff & mReadSocketBuf.f()) << 24
                            | (long) (0xff & mReadSocketBuf.f()) << 16
                            | (long) (0xff & mReadSocketBuf.f()) << 8 | (long) (0xff & mReadSocketBuf.f()));
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
                    if ((long) mReadSocketBuf.d() >= long1.longValue()) {
                        // cond_5
                        byte abyte0[] = new byte[(int) long1.longValue()];
                        // C_atx atx5 = d;
                        int i1 = abyte0.length;
                        if (mReadSocketBuf.d() >= i1)
                        {
                            int j1;
                            int k1;
                            int l1;
                            if (mReadSocketBuf.e)
                                j1 = 0;
                            else if (mReadSocketBuf.b < mReadSocketBuf.c)
                                j1 = mReadSocketBuf.c - mReadSocketBuf.b;
                            else
                                j1 = mReadSocketBuf.a.length - mReadSocketBuf.b;
                            k1 = Math.min(i1, j1);
                            System.arraycopy(mReadSocketBuf.a, mReadSocketBuf.b, abyte0, 0, k1);
                            mReadSocketBuf.a(k1);
                            l1 = i1 - k1;
                            if (l1 > 0)
                            {
                                int i2 = k1 + 0;
                                System.arraycopy(mReadSocketBuf.a, mReadSocketBuf.b, abyte0, i2, l1);
                                mReadSocketBuf.a(l1);
                            }
                        }
                        // cond_6
                        payload("received", ByteBuffer.wrap(abyte0));
                        mSocketListener.onMessageReceived(ByteBuffer.wrap(abyte0));
                        continue;
                    } else {
                        flag = true;
                    }
                }
            }

            if (flag) {
                if (mReadSocketBuf.d != -1)
                {
                    if (mReadSocketBuf.b != mReadSocketBuf.d)
                    {
                        mReadSocketBuf.b = mReadSocketBuf.d;
                        mReadSocketBuf.e = false;
                    }
                    mReadSocketBuf.d = -1;
                }
                return;
            } else {
                mReadSocketBuf.d = -1;
                if (!mReadSocketBuf.e) {
                    return;
                }

                mReadSocketBuf.c = 0;
                mReadSocketBuf.b = 0;
                return;
            }
        }
    }

    private void payload(String tag, ByteBuffer bytebuffer) {
        if (DEBUG) {
            C_axm axm1 = C_axm.a(bytebuffer.array());
            android.util.Log.d("FlingSocket", tag);
            android.util.Log.d("FlingSocket", axm1.payload());
        }
    }

    final synchronized SocketChannel startConnecd() throws IOException
    {
        mLogs.d("startConnect");

        // try {
        mBeginConnectTime = SystemClock.elapsedRealtime();
        mSocketChannel = SocketChannel.open();
        mSocketChannel.configureBlocking(false);
        mReadSocketBuf = new SocketBuf();
        mWriteSocketBuf = new SocketBuf();
        mSSLSocketEngine = new SSLSocketEngine(mSocketChannel,
                mReadSocketBuf, mWriteSocketBuf);
        if (mSocketChannel.connect(mInetSocketAddress))
        {
            if (mSSLSocketEngine == null) {
                mSocketStatus = 2;
                mSocketListener.onConnected();
                return mSocketChannel;
            }
            mSSLSocketEngine.beginHandshake();
        }
        // } catch (Exception e) {
        // e.printStackTrace();
        // }

        return mSocketChannel;
    }

    public final void connect(Inet4Address hostAddress, int port)
    {
        connect_b(hostAddress, port);
    }

    public final synchronized void send(ByteBuffer bytebuffer) throws IOException
    {
        if (mSocketStatus != 2)
            throw new IllegalStateException("not connected; state=" + this.mSocketStatus);

        if (bytebuffer == null)
            throw new IllegalArgumentException("message cannot be null");

        // try {
        if (this.mWriteSocketBuf.c() < 4 + bytebuffer.remaining())
            throw new C_atq();
        // } catch (Exception e) {
        // e.printStackTrace();
        // return;
        // }

        long remainingCount = bytebuffer.remaining();
        if (mWriteSocketBuf.c() >= 4)
        {
            if ((remainingCount < 0L) || (remainingCount > 0xffffffffL))
                throw new C_auf(remainingCount + " is not a valid uint32 value");
            mWriteSocketBuf.a((byte) (int) (0xFF & remainingCount >> 24));
            mWriteSocketBuf.a((byte) (int) (0xFF & remainingCount >> 16));
            mWriteSocketBuf.a((byte) (int) (0xFF & remainingCount >> 8));
            mWriteSocketBuf.a((byte) (int) (remainingCount & 0xFF));
        }

        byte[] sendBuf = bytebuffer.array();
        int pos = bytebuffer.position();
        int remain = bytebuffer.remaining();
        int writeCount = 0;
        if (mWriteSocketBuf.c() >= remain)
        {
            if (!mWriteSocketBuf.e) {
                if (mWriteSocketBuf.c < mWriteSocketBuf.b)
                {
                    writeCount = mWriteSocketBuf.b - mWriteSocketBuf.c;
                }
                else
                {
                    writeCount = mWriteSocketBuf.a.length - mWriteSocketBuf.c;
                }
            } else {
                writeCount = mWriteSocketBuf.a.length;
            }
        } else {
            this.mFlingSocketMultiplexer.wakeup();
            return;
        }

        int actualWriteCount = Math.min(remain, writeCount);
        System.arraycopy(sendBuf, pos, mWriteSocketBuf.a, mWriteSocketBuf.c, actualWriteCount);
        mWriteSocketBuf.b(actualWriteCount);
        int currentPos = pos + actualWriteCount;
        int remainedCount = remain - actualWriteCount;
        if (remainedCount > 0)
        {
            System.arraycopy(sendBuf, currentPos, mWriteSocketBuf.a, mWriteSocketBuf.c, remainedCount);
            mWriteSocketBuf.b(remainedCount);
        }
        payload("send", bytebuffer);
        this.mFlingSocketMultiplexer.wakeup();
    }

    final synchronized boolean checkInterestOps(SelectionKey selectionkey, long elapsedRealtime_l1)
    {
        boolean ok = false;
        if (n) {
            mLogs.w("Socket is no longer connected", new Object[0]);
            n = false;
            return false;
        }

        int mode = 0;
        switch (mSocketStatus) {
            case 1: // conncting?
                if (elapsedRealtime_l1 - this.mBeginConnectTime >= this.mTimeoutTime)
                {
                    doTeardown(3);
                } else {
                    if (mSocketChannel.isConnected()) {
                        if (mSSLSocketEngine != null) {
                            mode = 0 | mSSLSocketEngine.getOperationMode();
                        }
                    } else {
                        mode = SelectionKey.OP_CONNECT;// 8;
                    }
                    selectionkey.interestOps(mode);
                    ok = true;
                }
                break;
            case 2: // connected
                if (mSSLSocketEngine != null)
                {
                    mode = 0 | mSSLSocketEngine.getOperationMode();
                } else {
                    boolean flag1 = mReadSocketBuf.e();
                    mode = 0;
                    if (!flag1) {
                        mode = SelectionKey.OP_READ; // 1;
                    }
                    if (!mWriteSocketBuf.e) {
                        mode |= SelectionKey.OP_WRITE; // 4;
                    }
                }
                selectionkey.interestOps(mode);
                ok = true;
                break;
            case 3:// disconnecting?
                if (elapsedRealtime_l1 - mDisconnectTime < m) {
                    if (mSSLSocketEngine != null) {
                        mode = 0 | mSSLSocketEngine.getOperationMode();
                    } else {
                        if (mWriteSocketBuf.e) {
                            doTeardown(0);
                            ok = false;
                            break;
                        }

                        mode = SelectionKey.OP_WRITE;// 4;
                    }

                    selectionkey.interestOps(mode);
                    ok = true;
                } else {
                    doTeardown(0);
                    ok = false;
                }

                break;
        }

        return ok;
    }

    public final synchronized void disconnect()
    {
        mSocketStatus = 3;
        mDisconnectTime = SystemClock.elapsedRealtime();
        if (mSSLSocketEngine != null) {
            mSSLSocketEngine.disconnect();
        }
        mFlingSocketMultiplexer.wakeup();
    }

    public final synchronized boolean isConnected()
    {
        return (mSocketStatus == 2);
    }

    public final synchronized boolean isConnecting()
    {
        return (mSocketStatus == 1);
    }

    public final synchronized boolean isDisconnecting()
    {
        boolean flag = false;
        if (mSocketStatus == 3) {
            flag = true;
        }

        return flag;
    }

    public final synchronized int getState()
    {
        return mSocketStatus;
    }

    public final synchronized byte[] getPeerCertificate()
    {
        if (mSSLSocketEngine == null) {
            return null;
        }

        return mSSLSocketEngine.getPeerCertificate();
    }

    final synchronized boolean onConnectable()
    {
        boolean flag = true;

        mLogs.d("onConnectable", new Object[0]);
        try {
            mSocketChannel.finishConnect();

            if (mSSLSocketEngine != null) {
                mSSLSocketEngine.beginHandshake();

                return true;
            }
            mSocketStatus = 2;
            mSocketListener.onConnected();
        } catch (SSLException e) {
            mLogs.d(e, "exception in onConnectable", new Object[0]);
            doTeardown(4);
            flag = false;
        } catch (IOException ex) {
            mLogs.d(ex, "exception in onConnectable", new Object[0]);
            doTeardown(2);
            flag = false;
        }

        return flag;
    }

    final synchronized void onConnectError()
    {
        mLogs.d("onConnectError");
        try {
            doTeardown(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    final synchronized boolean onRead()
    {
        boolean flag = true;
        try {
            if (mSSLSocketEngine == null) {
                // cond_1
                if (!mReadSocketBuf.e()) {
                    int i1 = (int) mSocketChannel.read(mReadSocketBuf.b());
                    if (i1 <= 0) {
                        throw new ClosedChannelException();
                    }

                    mReadSocketBuf.b(i1);
                }
            } else {
                mSSLSocketEngine.readFromChannel();
                if (mSocketStatus == 1 && mSSLSocketEngine.isHandshakeFinished()) {
                    mSocketStatus = 2;
                    mSocketListener.onConnected();
                }
            }

            handleRead();
        } catch (ClosedChannelException e) {
            mLogs.w(e, "ClosedChannelException when state was %d", mSocketStatus);
            doTeardown(1);
            flag = false;
        } catch (SSLException ee) {

            mLogs.w(ee, "SSLException encountered. Tearing down the socket.");
            doTeardown(4);
            flag = false;
        } catch (IOException ex) {
            mLogs.w(ex, "IOException encountered. Tearing down the socket.");
            doTeardown(2);
            flag = false;
        }

        return flag;

    }

    final synchronized boolean onWrite()
    {
        boolean flag = false;

        try {
            if (mSSLSocketEngine == null) {
                if (!mWriteSocketBuf.e) {

                    int i1 = (int) mSocketChannel.write(mWriteSocketBuf.a());
                    if (i1 <= 0) {
                        // cond_2
                        throw new ClosedChannelException();
                    }
                    mWriteSocketBuf.a(i1);
                }
            } else {
                mSSLSocketEngine.writeToChannel();
                if (mSocketStatus == 1 && mSSLSocketEngine.isHandshakeFinished())
                {
                    mSocketStatus = 2;
                    mSocketListener.onConnected();
                }
            }

            // cond_0
            if (!mWriteSocketBuf.e || mSocketStatus != 3) {
                return true;
            }

            doTeardown(0);
        } catch (ClosedChannelException e) {
            mLogs.w(e, "ClosedChannelException when state was %d", mSocketStatus);
            doTeardown(1);
        } catch (SSLException ex) {
            mLogs.w(ex, "SSLException encountered. Tearing down the socket.");
            doTeardown(4);
            flag = false;
        } catch (IOException eio) {
            mLogs.w(eio, "IOException encountered. Tearing down the socket.");
            doTeardown(2);
            flag = false;
        }

        return flag;
    }

    final synchronized SocketChannel getSocketChannel()
    {
        return mSocketChannel;
    }
}
