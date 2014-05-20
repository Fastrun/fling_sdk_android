
package com.fireflycast.server.cast;

import android.os.Handler;

import com.fireflycast.server.cast.channels.IMediaChannelHelper_axf;
import com.fireflycast.server.utils.Logs_avu;

public final class MediaRouteSession_axe {
    private static final Logs_avu mLogs_a = new Logs_avu("MediaRouteSession");
    private final CastDeviceController_axs mCastDeviceController_b;
    private final IMediaChannelHelper_axf mMediaChannelHelper_c;
    private final Handler mHandler_d;
    private int mState_e;
    private int mPendingState_f;
    private ApplicationMetadata mApplicationMetadata_g;
    private String mSessionId_h;
    private SessionPrivateData_axj mSessionPrivateData_i;
    private boolean j;

    public MediaRouteSession_axe(CastDeviceController_axs axs1, IMediaChannelHelper_axf helper,
            Handler handler)
    {
        mCastDeviceController_b = axs1;
        mMediaChannelHelper_c = helper;
        mHandler_d = handler;
        mState_e = 4;
        mPendingState_f = 0;
    }

    static IMediaChannelHelper_axf a(MediaRouteSession_axe axe1)
    {
        return axe1.mMediaChannelHelper_c;
    }

    private void launchApplication_b(String applicationId, String sessionId,
            boolean relaunchIfRunning)
    {
        mState_e = 1;
        mSessionPrivateData_i = null;
        mCastDeviceController_b.launchApplication_a(applicationId, sessionId, relaunchIfRunning);
    }

    private void c()
    {
        mState_e = 3;
        if (j)
        {
            j = false;
            mApplicationMetadata_g = null;
            mCastDeviceController_b.stopApplication_b("");
            return;
        } else
        {
            mCastDeviceController_b.leaveApplication_j();
            return;
        }
    }

    public final synchronized ApplicationMetadata getApplicationMetadata_a()
    {
        return mApplicationMetadata_g;
    }

    final synchronized void onApplicationConnectionFailed_a(int statusCode)
    {
        int l;

        Object aobj[] = new Object[2];
        aobj[0] = Integer.valueOf(hashCode());
        aobj[1] = Integer.valueOf(mPendingState_f);
        mLogs_a.d("[%d] onApplicationConnectionFailed; mPendingState=%d", aobj);
        switch (mPendingState_f) {
            case 0:
                mState_e = 4;
                // mHandler_d.post(new C_axh(this, mSessionId_h, statusCode));
                mHandler_d.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        mMediaChannelHelper_c.sendPendingIntent_b(mSessionId_h);
                    }

                });
                mSessionId_h = null;
                break;
            case 1:
                break; // return;
            case 2:
                mPendingState_f = 0;
                // mHandler_d.post(new C_axh(this, mSessionId_h, statusCode));
                mHandler_d.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        mMediaChannelHelper_c.sendPendingIntent_b(mSessionId_h);
                    }
                });
                launchApplication_b(mSessionPrivateData_i.applicationId,
                        mSessionPrivateData_i.sessionId_b,
                        mSessionPrivateData_i.relaunchIfRunning_c);
                break;
            case 3: // return;
                break;
            case 4:
                mState_e = 4;
                mPendingState_f = 0;
                // mHandler_d.post(new C_axg(this, mSessionId_h, 0));
                mHandler_d.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // C_axf axf1 = c;
                        mMediaChannelHelper_c.detachMediaChannel_d(0);
                    }

                });

                mSessionId_h = null;
                break;
        }
    }

    final synchronized void onApplicationConnected_a(ApplicationMetadata applicationmetadata,
            String sessionId)
    {
        Object aobj[] = new Object[2];
        aobj[0] = Integer.valueOf(hashCode());
        aobj[1] = Integer.valueOf(mPendingState_f);
        mLogs_a.d("[%d] onApplicationConnected; mPendingState=%d", aobj);
        switch (mPendingState_f) {
            case 0:
                mState_e = 2;
                mApplicationMetadata_g = applicationmetadata;
                mSessionId_h = sessionId;
                // mHandler_d.post(new C_axi(this, mSessionId_h));

                mHandler_d.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mMediaChannelHelper_c.attachMediaChannel_a(mSessionId_h);
                    }

                });
                break;
            case 1:
                mState_e = 2;
                mPendingState_f = 0;
                break;
            case 2:
                c();
                break;
            case 3:
                mState_e = 2;
                mPendingState_f = 0;
                break;
            case 4:
                mPendingState_f = 0;
                c();
                break;
            default:
        }
    }

    public final synchronized void joinApplication_a(String applicationId, String sessionId)
    {
        if (mState_e != 4)
            throw new IllegalStateException((new StringBuilder(
                    "session is not currently stopped! state=")).append(mState_e).toString());
        mState_e = 1;
        mCastDeviceController_b.joinApplication_b(applicationId, sessionId);
    }

    public final synchronized void startSession_a(String applicationId, String sessionId, boolean relaunchIfRunning)
    {
        int k;
        mLogs_a.d("starting session for app %s", new Object[] {
                applicationId
        });
        k = mState_e;
        switch (k) {
            case 1:
                mPendingState_f = 2;
                mSessionPrivateData_i = new SessionPrivateData_axj(this, applicationId, sessionId, relaunchIfRunning);
                break;
            case 2:
                mPendingState_f = 2;
                mSessionPrivateData_i = new SessionPrivateData_axj(this, applicationId, sessionId, relaunchIfRunning);
                c();
                break;
            case 3:
                mPendingState_f = 2;
                mSessionPrivateData_i = new SessionPrivateData_axj(this, applicationId, sessionId, relaunchIfRunning);
                break;
            case 4:
                mPendingState_f = 0;
                launchApplication_b(applicationId, sessionId, relaunchIfRunning);
                break;
        }
    }

    public final synchronized void stopSession_a(boolean flag)
    {
        int k;
        mLogs_a.d("stopping session", new Object[0]);
        j = flag;
        k = mState_e;
        switch (k) {
            case 1:
                mPendingState_f = 4;
                break;
            case 2:
                mPendingState_f = 0;
                c();
                break;
            case 3:
            case 4:
                mPendingState_f = 0;
                break;
        }
    }

    public final synchronized String getSessionId_b()
    {
        return mSessionId_h;
    }

    final synchronized void onApplicationDisconnected_b(final int statusCode)
    {
        Object aobj[] = new Object[2];
        aobj[0] = Integer.valueOf(hashCode());
        aobj[1] = Integer.valueOf(mPendingState_f);
        mLogs_a.d("[%d] onApplicationDisconnected; mPendingState=%d", aobj);
        switch (mPendingState_f) {
            case 2:
                // mHandler_d.post(new C_axg(this, mSessionId_h, k));
                mHandler_d.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mMediaChannelHelper_c.detachMediaChannel_d(statusCode);
                    }

                });

                mPendingState_f = 0;
                launchApplication_b(mSessionPrivateData_i.applicationId,
                        mSessionPrivateData_i.sessionId_b,
                        mSessionPrivateData_i.relaunchIfRunning_c);
                break;
            case 3:
                mState_e = 4;
                mPendingState_f = 0;
                break;
            case 4:
                mPendingState_f = 0;
                mState_e = 4;

                // mHandler_d.post(new C_axg(this, mSessionId_h, k));
                mHandler_d.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mMediaChannelHelper_c.detachMediaChannel_d(statusCode);
                    }

                });

                mSessionId_h = null;
                break;
        }
    }
}
