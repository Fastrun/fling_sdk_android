
package com.fireflycast.server.utils;

import android.os.SystemClock;

import org.json.JSONObject;

public final class RequestTracker_avz {
    public static final Object mLock_a = new Object();
    private static final Logs_avu mLog_f = new Logs_avu("RequestTracker");
    private long mTimeout_b;
    private long mCurrentRequestId_c;
    private long mInitTime_d;
    private IStatusRequest_avy mRequestResult_e;

    public RequestTracker_avz(long l)
    {
        mTimeout_b = l;
        mCurrentRequestId_c = -1L;
        mInitTime_d = 0L;
    }

    private void reset_c()
    {
        mCurrentRequestId_c = -1L;
        mRequestResult_e = null;
        mInitTime_d = 0L;
    }

    public final void a()
    {
        synchronized (mLock_a)
        {
            if (mCurrentRequestId_c != -1L)
                reset_c();
        }
    }

    public final void start_a(long requestId, IStatusRequest_avy avy1)
    {
        synchronized (mLock_a)
        {
            avy1 = mRequestResult_e;
            mCurrentRequestId_c = requestId;
            mRequestResult_e = avy1;
            mInitTime_d = SystemClock.elapsedRealtime();
        }
    }

    public final boolean a(long request)
    {
        boolean flag = false;
        synchronized (mLock_a) {
            if (mCurrentRequestId_c != -1L && mCurrentRequestId_c == request)
                flag = true;
            else
                flag = false;
        }

        return flag;
    }

    public final boolean onResult_a(long requestId, int result)
    {
        return onResult_a(requestId, result, null);
    }

    public final boolean onResult_a(long requestId, int result, JSONObject jsonobject)
    {
        boolean flag = true;
        IStatusRequest_avy requestResult;
        synchronized (mLock_a) {
            if (mCurrentRequestId_c == -1L || mCurrentRequestId_c != requestId) {
                requestResult = null;
                flag = false;
            } else {
                Object aobj[] = new Object[1];
                aobj[0] = Long.valueOf(mCurrentRequestId_c);
                mLog_f.d("request %d completed", aobj);
                requestResult = mRequestResult_e;
                reset_c();
            }
        }

        if (requestResult != null) {
            requestResult.requestStatus_a(requestId, result, jsonobject);
        }

        return flag;
    }

    public final boolean isRunning_b()
    {
        boolean flag;
        synchronized (mLock_a) {
            if (mCurrentRequestId_c != -1L)
                flag = true;
            else
                flag = false;

        }
        return flag;
    }

    public final boolean checkTimeout_b(long currentTiem, int result)
    {
        boolean isTimeout;
        long requestId;
        isTimeout = true;
        requestId = 0L;
        IStatusRequest_avy avy1;
        synchronized (mLock_a) {
            if (mCurrentRequestId_c == -1L || currentTiem - mInitTime_d < mTimeout_b) {
                avy1 = null;
                isTimeout = false;
            } else {
                Object aobj[] = new Object[1];
                aobj[0] = Long.valueOf(mCurrentRequestId_c);
                mLog_f.d("request %d timed out", aobj);
                requestId = mCurrentRequestId_c;
                avy1 = mRequestResult_e;
                reset_c();
            }
        }
        if (avy1 != null)
            avy1.requestStatus_a(requestId, result, null);
        return isTimeout;
    }
}
