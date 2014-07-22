
package tv.matchstick.server.utils;

import android.os.SystemClock;

import org.json.JSONObject;

public final class RequestTracker {
    public static final Object mLock_a = new Object();
    private static final LOG mLog_f = new LOG("RequestTracker");
    private long mTimeout;
    private long mCurrentRequestId;
    private long mInitTime;
    private IStatusRequest mRequestResult;

    public RequestTracker(long l)
    {
        mTimeout = l;
        mCurrentRequestId = -1L;
        mInitTime = 0L;
    }

    private void reset()
    {
        mCurrentRequestId = -1L;
        mRequestResult = null;
        mInitTime = 0L;
    }

    public final void a()
    {
        synchronized (mLock_a)
        {
            if (mCurrentRequestId != -1L)
                reset();
        }
    }

    public final void start(long requestId, IStatusRequest avy1)
    {
        synchronized (mLock_a)
        {
            avy1 = mRequestResult;
            mCurrentRequestId = requestId;
            mRequestResult = avy1;
            mInitTime = SystemClock.elapsedRealtime();
        }
    }

    public final boolean a(long request)
    {
        boolean flag = false;
        synchronized (mLock_a) {
            if (mCurrentRequestId != -1L && mCurrentRequestId == request)
                flag = true;
            else
                flag = false;
        }

        return flag;
    }

    public final boolean onResult(long requestId, int result)
    {
        return onResult(requestId, result, null);
    }

    public final boolean onResult(long requestId, int result, JSONObject jsonobject)
    {
        boolean flag = true;
        IStatusRequest requestResult;
        synchronized (mLock_a) {
            if (mCurrentRequestId == -1L || mCurrentRequestId != requestId) {
                requestResult = null;
                flag = false;
            } else {
                Object aobj[] = new Object[1];
                aobj[0] = Long.valueOf(mCurrentRequestId);
                mLog_f.d("request %d completed", aobj);
                requestResult = mRequestResult;
                reset();
            }
        }

        if (requestResult != null) {
            requestResult.requestStatus(requestId, result, jsonobject);
        }

        return flag;
    }

    public final boolean isRunning()
    {
        boolean flag;
        synchronized (mLock_a) {
            if (mCurrentRequestId != -1L)
                flag = true;
            else
                flag = false;

        }
        return flag;
    }

    public final boolean checkTimeout(long currentTiem, int result)
    {
        boolean isTimeout;
        long requestId;
        isTimeout = true;
        requestId = 0L;
        IStatusRequest avy1;
        synchronized (mLock_a) {
            if (mCurrentRequestId == -1L || currentTiem - mInitTime < mTimeout) {
                avy1 = null;
                isTimeout = false;
            } else {
                Object aobj[] = new Object[1];
                aobj[0] = Long.valueOf(mCurrentRequestId);
                mLog_f.d("request %d timed out", aobj);
                requestId = mCurrentRequestId;
                avy1 = mRequestResult;
                reset();
            }
        }
        if (avy1 != null)
            avy1.requestStatus(requestId, result, null);
        return isTimeout;
    }
}
