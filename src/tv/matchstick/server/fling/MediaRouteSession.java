
package tv.matchstick.server.fling;

import tv.matchstick.server.fling.channels.IMediaChannelHelper;
import tv.matchstick.server.utils.LOG;
import android.os.Handler;

public final class MediaRouteSession {
    private static final LOG log = new LOG("MediaRouteSession");
    private final FlingDeviceController mFlingDeviceController;
    private final IMediaChannelHelper mMediaChannelHelper;
    private final Handler mHandler;
    private int mState;
    private int mPendingState;
    private ApplicationMetadata mApplicationMetadata;
    private String mSessionId;
    private SessionPrivateData mSessionPrivateData;
    private boolean j;

    public MediaRouteSession(FlingDeviceController axs1, IMediaChannelHelper helper,
            Handler handler)
    {
        mFlingDeviceController = axs1;
        mMediaChannelHelper = helper;
        mHandler = handler;
        mState = 4;
        mPendingState = 0;
    }

    static IMediaChannelHelper a(MediaRouteSession axe1)
    {
        return axe1.mMediaChannelHelper;
    }

    private void launchApplication(String applicationId, String sessionId,
            boolean relaunchIfRunning)
    {
        mState = 1;
        mSessionPrivateData = null;
        mFlingDeviceController.launchApplication(applicationId, sessionId, relaunchIfRunning);
    }

    private void c()
    {
        mState = 3;
        if (j)
        {
            j = false;
            mApplicationMetadata = null;
            mFlingDeviceController.stopApplication("");
            return;
        } else
        {
            mFlingDeviceController.leaveApplication();
            return;
        }
    }

    public final synchronized ApplicationMetadata getApplicationMetadata()
    {
        return mApplicationMetadata;
    }

    final synchronized void onApplicationConnectionFailed(int statusCode)
    {
        log.d("[%d] onApplicationConnectionFailed; mPendingState=%d", hashCode(), mPendingState);
        switch (mPendingState) {
            case 0:
                mState = 4;
                
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        mMediaChannelHelper.sendPendingIntent(mSessionId);
                    }

                });
                mSessionId = null;
                break;
            case 1:
                break; // return;
            case 2:
                mPendingState = 0;
                
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        mMediaChannelHelper.sendPendingIntent(mSessionId);
                    }
                });
                launchApplication(mSessionPrivateData.applicationId,
                        mSessionPrivateData.sessionId,
                        mSessionPrivateData.relaunchIfRunning);
                break;
            case 3: // return;
                break;
            case 4:
                mState = 4;
                mPendingState = 0;
                
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

                        mMediaChannelHelper.detachMediaChannel(0);
                    }

                });

                mSessionId = null;
                break;
        }
    }

    final synchronized void onApplicationConnected(ApplicationMetadata applicationmetadata,
            String sessionId)
    {
        log.d("[%d] onApplicationConnected; mPendingState=%d", hashCode(), mPendingState);
        switch (mPendingState) {
            case 0:
                mState = 2;
                mApplicationMetadata = applicationmetadata;
                mSessionId = sessionId;

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mMediaChannelHelper.attachMediaChannel(mSessionId);
                    }

                });
                break;
            case 1:
                mState = 2;
                mPendingState = 0;
                break;
            case 2:
                c();
                break;
            case 3:
                mState = 2;
                mPendingState = 0;
                break;
            case 4:
                mPendingState = 0;
                c();
                break;
            default:
        }
    }

    public final synchronized void joinApplication(String applicationId, String sessionId)
    {
        if (mState != 4)
            throw new IllegalStateException("session is not currently stopped! state=" + mState);

        mState = 1;
        mFlingDeviceController.joinApplication(applicationId, sessionId);
    }

    public final synchronized void startSession(String applicationId, String sessionId, boolean relaunchIfRunning)
    {
        int k;
        log.d("starting session for app %s", applicationId);
        k = mState;
        switch (k) {
            case 1:
                mPendingState = 2;
                mSessionPrivateData = new SessionPrivateData(this, applicationId, sessionId, relaunchIfRunning);
                break;
            case 2:
                mPendingState = 2;
                mSessionPrivateData = new SessionPrivateData(this, applicationId, sessionId, relaunchIfRunning);
                c();
                break;
            case 3:
                mPendingState = 2;
                mSessionPrivateData = new SessionPrivateData(this, applicationId, sessionId, relaunchIfRunning);
                break;
            case 4:
                mPendingState = 0;
                launchApplication(applicationId, sessionId, relaunchIfRunning);
                break;
        }
    }

    public final synchronized void stopSession(boolean flag)
    {
        int k;
        log.d("stopping session");
        j = flag;
        k = mState;
        switch (k) {
            case 1:
                mPendingState = 4;
                break;
            case 2:
                mPendingState = 0;
                c();
                break;
            case 3:
            case 4:
                mPendingState = 0;
                break;
        }
    }

    public final synchronized String getSessionId()
    {
        return mSessionId;
    }

    final synchronized void onApplicationDisconnected(final int statusCode)
    {
        log.d("[%d] onApplicationDisconnected; mPendingState=%d", hashCode(), mPendingState);
       
        switch (mPendingState) {
            case 2:
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mMediaChannelHelper.detachMediaChannel(statusCode);
                    }

                });

                mPendingState = 0;
                launchApplication(mSessionPrivateData.applicationId,
                        mSessionPrivateData.sessionId,
                        mSessionPrivateData.relaunchIfRunning);
                break;
            case 3:
                mState = 4;
                mPendingState = 0;
                break;
            case 4:
                mPendingState = 0;
                mState = 4;

                mHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        mMediaChannelHelper.detachMediaChannel(statusCode);
                    }

                });

                mSessionId = null;
                break;
        }
    }
}
