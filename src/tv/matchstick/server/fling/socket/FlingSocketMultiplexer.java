
package tv.matchstick.server.fling.socket;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import java.io.IOException;
import java.nio.channels.CancelledKeyException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import tv.matchstick.server.utils.LOG;

public final class FlingSocketMultiplexer {
    private static LOG mLogs = new LOG("FlingSocketMultiplexer");
    private static FlingSocketMultiplexer mInstance;
    private final LinkedList mRegistedFlingSocketList = new LinkedList();
    private final LinkedList mAllFlingSocketList = new LinkedList();
    private Selector mSelector;
    private volatile boolean mQuitLoop;
    private volatile boolean g;
    private volatile Thread mFlingSocketMultiplexerThread;
    private final AtomicBoolean mReadyToConnect = new AtomicBoolean(false);
    private volatile Throwable j;
    private final Context mContext;

    private CountDownLatch mCountDownLatch;

    private FlingSocketMultiplexer(Context context)
    {
        mContext = context;
    }

    public static FlingSocketMultiplexer getInstance(Context context)
    {
        if (mInstance == null) {
            mInstance = new FlingSocketMultiplexer(context);
        }

        return mInstance;
    }

    static CountDownLatch getCountDownLatch(FlingSocketMultiplexer atv1)
    {
        return atv1.mCountDownLatch;
    }

    static synchronized void processData(FlingSocketMultiplexer multiplexer)
    {
        ArrayList errorFlingSocketList = new ArrayList();

        int i1 = 0;
        while (!multiplexer.mQuitLoop) {
            long elapsedRealtime_l1 = SystemClock.elapsedRealtime();
            if (multiplexer.mReadyToConnect.getAndSet(false)) {
                synchronized (multiplexer.mAllFlingSocketList) {
                    Iterator iterator3 = multiplexer.mAllFlingSocketList.iterator();
                    while (iterator3.hasNext()) {
                        FlingSocket flingSocket = (FlingSocket) iterator3.next();
                        try {
                            flingSocket.startConnecd().register(multiplexer.mSelector, 0).attach(flingSocket);

                            // atv1.mContext_k.startService(atv1.mIntent_l);
                            multiplexer.mRegistedFlingSocketList.add(flingSocket);
                        } catch (Exception e) {
                            mLogs.d(e, "Error while connecting socket.", new Object[0]);
                            errorFlingSocketList.add(flingSocket);
                        }
                    }
                    multiplexer.mAllFlingSocketList.clear();
                }
            }

            if (!errorFlingSocketList.isEmpty())
            {
                for (Iterator iterator2 = errorFlingSocketList.iterator(); iterator2.hasNext(); ((FlingSocket) iterator2
                        .next()).onConnectError())
                    ;
                errorFlingSocketList.clear();
            }

            boolean flag = false;
            synchronized (multiplexer.mRegistedFlingSocketList) {
                Iterator iterator = multiplexer.mRegistedFlingSocketList.iterator();

                while (iterator.hasNext())
                {
                    FlingSocket flingSocket = (FlingSocket) iterator.next();
                    SocketChannel socketchannel = flingSocket.getSocketChannel();
                    if (socketchannel == null
                            || socketchannel.keyFor(multiplexer.mSelector) == null
                            || !flingSocket.checkInterestOps(
                                    socketchannel.keyFor(multiplexer.mSelector), elapsedRealtime_l1))
                    {
                        iterator.remove();
                    } else
                    {
                        boolean flag2;
                        if (flingSocket.isConnecting() || flingSocket.isDisconnecting()) // need
                                                                                           // check.
                                                                                           // todo
                            flag2 = true;
                        else
                            flag2 = flag;
                        flag = flag2;
                    }
                }
            }
            boolean flag1;
            if (multiplexer.mRegistedFlingSocketList.isEmpty() && multiplexer.mAllFlingSocketList.isEmpty())
                flag1 = true;
            else
                flag1 = false;

            long l2;
            if (flag)
                l2 = 1000L;
            else
                l2 = 0L;

            try {
                i1 = multiplexer.mSelector.select(l2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (i1 != 0) {
                if (multiplexer.mQuitLoop) {
                    return;
                }

                Iterator iterator1 = multiplexer.mSelector.selectedKeys().iterator();
                while (iterator1.hasNext())
                {
                    try
                    {
                        SelectionKey selectionkey = (SelectionKey) iterator1.next();
                        FlingSocket flingSocket = (FlingSocket) selectionkey.attachment();
                        if (selectionkey.isConnectable() && !flingSocket.onConnectable())
                            multiplexer.mRegistedFlingSocketList.remove(flingSocket);
                        if (selectionkey.isReadable() && !flingSocket.onRead())
                            multiplexer.mRegistedFlingSocketList.remove(flingSocket);
                        if (selectionkey.isWritable() && !flingSocket.onWrite())
                            multiplexer.mRegistedFlingSocketList.remove(flingSocket);
                    } catch (CancelledKeyException e) {
                    }
                    iterator1.remove();
                }
            }
        }
    }

    static LOG c()
    {
        return mLogs;
    }

    static Thread d(FlingSocketMultiplexer atv1)
    {
        atv1.mFlingSocketMultiplexerThread = null;
        return null;
    }

    private void checkStatus()
    {
        if (g)
        {
            StringBuffer stringbuffer = new StringBuffer();
            stringbuffer.append("selector thread aborted due to ");
            if (j != null)
            {
                stringbuffer.append(j.getClass().getName());
                StackTraceElement astacktraceelement[] = j.getStackTrace();
                stringbuffer.append(" at ").append(astacktraceelement[0].getFileName()).append(':')
                        .append(astacktraceelement[0].getLineNumber());
            } else
            {
                stringbuffer.append("unknown condition");
            }
            throw new IllegalStateException(stringbuffer.toString());
        }
        if (mFlingSocketMultiplexerThread == null)
            throw new IllegalStateException("not started; call start()");
        else
            return;
    }

    final synchronized void init() throws IOException
    {
        if (mFlingSocketMultiplexerThread != null) {
            return;
        }

        mLogs.d("starting multiplexer", new Object[0]);
        mCountDownLatch = new CountDownLatch(1);
        g = false;
        mQuitLoop = false;
        boolean flag = false;
        try {
            mSelector = Selector.open();
            mFlingSocketMultiplexerThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try
                    {
                        mCountDownLatch.countDown();
                        processData(FlingSocketMultiplexer.this);
                        return;
                    } catch (Throwable localThrowable)
                    {
                        FlingSocketMultiplexer.c().e(localThrowable,
                                "Unexpected throwable in selector loop", new Object[0]);
                        j = localThrowable;
                        g = true;
                        return;
                    } finally
                    {
                        mLogs.d("**** selector loop thread exiting", new Object[0]);
                        mFlingSocketMultiplexerThread = null;
                    }
                }

            });

            mFlingSocketMultiplexerThread.setName("FlingSocketMultiplexer");
            mFlingSocketMultiplexerThread.start();

            flag = mCountDownLatch.await(1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (flag) {
            return;
        }

        throw new IOException("timed out or interrupted waiting for muxer thread to start");
    }

    public final synchronized void doConnect(FlingSocket flingSocket)
    {
        try {
            checkStatus();
            synchronized (mAllFlingSocketList)
            {
                mLogs.d("added socket", new Object[0]);
                // mContext_k.startService(mIntent_l);
                mAllFlingSocketList.add(flingSocket);
            }
            mReadyToConnect.set(true);
            mSelector.wakeup();
        } finally {

        }

        return;
    }

    public final synchronized void wakeup()
    {
        try {
            checkStatus();
            mSelector.wakeup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
