
package com.fireflycast.server.cast.socket;

import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.fireflycast.server.utils.Logs;

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

public final class CastSocketMultiplexer {
    private static Logs mLogs_a = new Logs("CastSocketMultiplexer");
    private static CastSocketMultiplexer mInstance_b;
    private final LinkedList mRegistedCastSocketList_c = new LinkedList();
    private final LinkedList mAllCastSocketList_d = new LinkedList();
    private Selector mSelector_e;
    private volatile boolean mQuitLoop_f;
    private volatile boolean g;
    private volatile Thread mCastSocketMultiplexerThread_h;
    private final AtomicBoolean mReadyToConnect_i = new AtomicBoolean(false);
    private volatile Throwable j;
    private final Context mContext_k;
    // private final Intent mIntent_l = AndroidUtils_bln
    // .f("com.fireflycast.cast.service.SOCKET_MUX");
    private CountDownLatch mCountDownLatch_m;

    private CastSocketMultiplexer(Context context)
    {
        mContext_k = context;
    }

    public static CastSocketMultiplexer getInstance_a(Context context)
    {
        if (mInstance_b == null) {
            mInstance_b = new CastSocketMultiplexer(context);
        }

        return mInstance_b;
    }

    /*
     * static Throwable a(CastSocketMultiplexer_atv atv1, Throwable throwable) {
     * atv1.j = throwable; return throwable; }
     */

    static CountDownLatch getCountDownLatch_a(CastSocketMultiplexer atv1)
    {
        return atv1.mCountDownLatch_m;
    }

    static synchronized void processData_b(CastSocketMultiplexer multiplexer)
    {
        ArrayList errorCastSocketList = new ArrayList();

        int i1 = 0;
        while (!multiplexer.mQuitLoop_f) {
            long elapsedRealtime_l1 = SystemClock.elapsedRealtime();
            if (multiplexer.mReadyToConnect_i.getAndSet(false)) {
                synchronized (multiplexer.mAllCastSocketList_d) {
                    Iterator iterator3 = multiplexer.mAllCastSocketList_d.iterator();
                    while (iterator3.hasNext()) {
                        CastSocket castSocket = (CastSocket) iterator3.next();
                        try {
                            castSocket.startConnect_a().register(multiplexer.mSelector_e, 0).attach(castSocket);

                            // atv1.mContext_k.startService(atv1.mIntent_l);
                            multiplexer.mRegistedCastSocketList_c.add(castSocket);
                        } catch (Exception e) {
                            mLogs_a.d(e, "Error while connecting socket.", new Object[0]);
                            errorCastSocketList.add(castSocket);
                        }
                    }
                    multiplexer.mAllCastSocketList_d.clear();
                }
            }

            if (!errorCastSocketList.isEmpty())
            {
                for (Iterator iterator2 = errorCastSocketList.iterator(); iterator2.hasNext(); ((CastSocket) iterator2
                        .next()).onConnectError_i())
                    ;
                errorCastSocketList.clear();
            }

            boolean flag = false;
            synchronized (multiplexer.mRegistedCastSocketList_c) {
                Iterator iterator = multiplexer.mRegistedCastSocketList_c.iterator();

                while (iterator.hasNext())
                {
                    CastSocket castSocket = (CastSocket) iterator.next();
                    SocketChannel socketchannel = castSocket.getSocketChannel_l();
                    if (socketchannel == null
                            || socketchannel.keyFor(multiplexer.mSelector_e) == null
                            || !castSocket.checkInterestOps_a(
                                    socketchannel.keyFor(multiplexer.mSelector_e), elapsedRealtime_l1))
                    {
                        iterator.remove();
                    } else
                    {
                        boolean flag2;
                        if (castSocket.isConnecting_d() || castSocket.isDisconnecting_e()) // need
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
            if (multiplexer.mRegistedCastSocketList_c.isEmpty() && multiplexer.mAllCastSocketList_d.isEmpty())
                flag1 = true;
            else
                flag1 = false;
            // if (flag1)
            // atv1.mContext_k.stopService(atv1.mIntent_l);

            long l2;
            if (flag)
                l2 = 1000L;
            else
                l2 = 0L;

            try {
                i1 = multiplexer.mSelector_e.select(l2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (i1 != 0) {
                if (multiplexer.mQuitLoop_f) {
                    return;
                }

                Iterator iterator1 = multiplexer.mSelector_e.selectedKeys().iterator();
                while (iterator1.hasNext())
                {
                    try
                    {
                        SelectionKey selectionkey = (SelectionKey) iterator1.next();
                        CastSocket castSocket = (CastSocket) selectionkey.attachment();
                        if (selectionkey.isConnectable() && !castSocket.onConnectable_h())
                            multiplexer.mRegistedCastSocketList_c.remove(castSocket);
                        if (selectionkey.isReadable() && !castSocket.onRead_j())
                            multiplexer.mRegistedCastSocketList_c.remove(castSocket);
                        if (selectionkey.isWritable() && !castSocket.onWrite_k())
                            multiplexer.mRegistedCastSocketList_c.remove(castSocket);
                    } catch (CancelledKeyException e) {
                    }
                    iterator1.remove();
                }
            }
        }
    }

    static Logs c()
    {
        return mLogs_a;
    }

    /*
     * static boolean c(CastSocketMultiplexer_atv atv1) { atv1.g = true; return
     * true; }
     */

    static Thread d(CastSocketMultiplexer atv1)
    {
        atv1.mCastSocketMultiplexerThread_h = null;
        return null;
    }

    private void checkStatus_d()
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
        if (mCastSocketMultiplexerThread_h == null)
            throw new IllegalStateException("not started; call start()");
        else
            return;
    }

    final synchronized void init_a() throws IOException
    {
        if (mCastSocketMultiplexerThread_h != null) {
            return;
        }

        mLogs_a.d("starting multiplexer", new Object[0]);
        mCountDownLatch_m = new CountDownLatch(1);
        g = false;
        mQuitLoop_f = false;
        boolean flag = false;
        try {
            mSelector_e = Selector.open();
            // mCastSocketMultiplexerThread_h = new Thread(new C_atw(this));
            mCastSocketMultiplexerThread_h = new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try
                    {
                        mCountDownLatch_m.countDown();
                        processData_b(CastSocketMultiplexer.this);
                        return;
                    } catch (Throwable localThrowable)
                    {
                        CastSocketMultiplexer.c().e(localThrowable,
                                "Unexpected throwable in selector loop", new Object[0]);
                        j = localThrowable;
                        g = true;
                        return;
                    } finally
                    {
                        mLogs_a.d("**** selector loop thread exiting", new Object[0]);
                        mCastSocketMultiplexerThread_h = null;
                    }
                }

            });

            mCastSocketMultiplexerThread_h.setName("CastSocketMultiplexer");
            mCastSocketMultiplexerThread_h.start();

            flag = mCountDownLatch_m.await(1L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (flag) {
            return;
        }

        throw new IOException("timed out or interrupted waiting for muxer thread to start");
    }

    public final synchronized void doConnect_a(CastSocket castSocket)
    {
        try {
            checkStatus_d();
            synchronized (mAllCastSocketList_d)
            {
                mLogs_a.d("added socket", new Object[0]);
                // mContext_k.startService(mIntent_l);
                mAllCastSocketList_d.add(castSocket);
            }
            mReadyToConnect_i.set(true);
            mSelector_e.wakeup();
        } finally {

        }

        return;
    }

    public final synchronized void wakeup_b()
    {
        try {
            checkStatus_d();
            mSelector_e.wakeup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
