
package com.fireflycast.server.cast.channels;

import android.text.TextUtils;

import com.fireflycast.server.utils.ApplicationInfo;
import com.fireflycast.server.utils.IStatusRequest;
import com.fireflycast.server.utils.Logs;
import com.fireflycast.server.utils.RequestTracker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ReceiverControlChannel extends CastChannel {
    public String mTransportId_a;
    public final RequestTracker mLaunchRequestTracker_b;
    public final RequestTracker mStopSessionRequestTracker_c;
    public final RequestTracker mGetStatusRequestTracker_d;
    public final RequestTracker mSetVolumeRequestTracker_e;
    public final RequestTracker mSetMuteRequestTracker_f;
    public double mLevel_g;
    public boolean mMuted_h;
    public boolean mFirst_i;
    private final String mReceiverDestinationId_l;
    private final IStatusRequest mLaunchAppResult_m;
    private final IStatusRequest mStopSessionResult_n;
    private final IStatusRequest mGetStatusResult_o;

    public ReceiverControlChannel(String receiverDestinationId)
    {
        super("urn:x-cast:com.google.cast.receiver", "ReceiverControlChannel");

        if (TextUtils.isEmpty(receiverDestinationId))
        {
            throw new IllegalArgumentException("receiverDestinationId can't be empty");
        } else
        {
            mReceiverDestinationId_l = receiverDestinationId;
            
            mLaunchRequestTracker_b = new RequestTracker(30000L); //10000L
            // m = new C_auo(this, (byte) 0);
            mLaunchAppResult_m = new IStatusRequest() {

                @Override
                public void requestStatus_a(long requestId, int result, JSONObject jsonobject) {
                    // TODO Auto-generated method stub
                    if (result != 0) {
                        onApplicationConnectionFailed_b(result);
                    }
                }

            };

            mStopSessionRequestTracker_c = new RequestTracker(3000L);

            // n = new C_auq(this, (byte) 0);
            mStopSessionResult_n = new IStatusRequest() {

                @Override
                public void requestStatus_a(long requestId, int result, JSONObject jsonobject) {
                    // TODO Auto-generated method stub
                    if (result != 0) {
                        onRequestStatus_a(result);
                    }
                }

            };

            mGetStatusRequestTracker_d = new RequestTracker(3000L);

            // o = new C_aup(this, (byte) 0);
            mGetStatusResult_o = new IStatusRequest() {

                @Override
                public void requestStatus_a(long requestId, int result, JSONObject jsonobject) {
                    // TODO Auto-generated method stub
                    if (result != 0) {
                        onStatusRequestFailed_c(result);
                    }
                }

            };

            mSetVolumeRequestTracker_e = new RequestTracker(3000L);
            mSetMuteRequestTracker_f = new RequestTracker(3000L);
            return;
        }
    }

    public final void getStatus_a()
    {
        if (mGetStatusRequestTracker_d.isRunning_b())
            return;
        long requestId = getId_c();
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("requestId", requestId);
            jsonobject.put("type", "GET_STATUS");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage_a(jsonobject.toString(), requestId, mReceiverDestinationId_l);

        mGetStatusRequestTracker_d.start_a(requestId, mGetStatusResult_o);
    }

    public final void setVolume_a(double level, double expected_level, boolean muted)
    {
        long requestId;
        JSONObject jsonobject;
        if (level < 0.0D)
            level = 0.0D;
        else if (level > 1.0D)
            level = 1.0D;
        requestId = getId_c();
        jsonobject = new JSONObject();
        try
        {
            jsonobject.put("requestId", requestId);
            jsonobject.put("type", "SET_VOLUME");
            JSONObject jsonobject1 = new JSONObject();
            jsonobject1.put("level", level);
            jsonobject.put("volume", jsonobject1);
            JSONObject jsonobject2 = new JSONObject();
            jsonobject2.put("level", expected_level);
            jsonobject2.put("muted", muted);
            jsonobject.put("expectedVolume", jsonobject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage_a(jsonobject.toString(), requestId, mReceiverDestinationId_l);

        mSetVolumeRequestTracker_e.start_a(requestId, null);
    }

    protected abstract void onRequestStatus_a(int j);

    protected abstract void onConnectToApplicationAndNotify_a(ApplicationInfo appInfo);

    protected abstract void onStatusReceived_a(ApplicationInfo atn1, double level, boolean muted);

    public final void launchApplication_a(String appId, String param)
    {
        long requestId = getId_c();
        JSONObject jsonobject = new JSONObject();
        try {
            jsonobject.put("requestId", requestId);
            jsonobject.put("type", "LAUNCH");
            jsonobject.put("appId", appId);
            if (param != null) {
                jsonobject.put("commandParameters", param);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage_a(jsonobject.toString(), requestId, mReceiverDestinationId_l);

        mLaunchRequestTracker_b.start_a(requestId, mLaunchAppResult_m);
        return;
    }

    public final void setMute_a(boolean muted, double level, boolean exp_muted)
    {
        long requestId = getId_c();
        JSONObject jsonobject = new JSONObject();
        try
        {
            jsonobject.put("requestId", requestId);
            jsonobject.put("type", "SET_VOLUME");
            JSONObject jsonobject1 = new JSONObject();
            jsonobject1.put("muted", muted);
            jsonobject.put("volume", jsonobject1);
            JSONObject jsonobject2 = new JSONObject();
            jsonobject2.put("level", level);
            jsonobject2.put("muted", exp_muted);
            jsonobject.put("expectedVolume", jsonobject2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sendMessage_a(jsonobject.toString(), requestId, mReceiverDestinationId_l);

        mSetMuteRequestTracker_f.start_a(requestId, null);
    }

    public final void checkReceivedMessage_a_(String message)
    {
        char c1;
        char c2;
        c1 = '\u07D1';
        c2 = '\r';
        this.mLogs_j.d("Received: %s", new Object[] {
                message
        });

        try {
            JSONObject jsonobject;
            String type;
            long reuestId;
            jsonobject = new JSONObject(message);
            type = jsonobject.getString("type");
            reuestId = jsonobject.optLong("requestId", -1L);
            if ("RECEIVER_STATUS".equals(type)) {
                JSONObject statusJsonObj;
                boolean hasApplications;
                statusJsonObj = jsonobject.getJSONObject("status");
                hasApplications = statusJsonObj.has("applications");
                ApplicationInfo appInfo;
                appInfo = null;
                if (hasApplications) {
                    JSONArray applications;
                    int len;
                    JSONObject firstApplication;
                    ApplicationInfo atn2;
                    try
                    {
                        applications = statusJsonObj.getJSONArray("applications");
                        len = applications.length();
                    } catch (JSONException e)
                    {
                        Logs avu1 = this.mLogs_j;
                        Object aobj[] = new Object[2];
                        aobj[0] = e.getMessage();
                        aobj[1] = message;
                        avu1.w("Message is malformed (%s); ignoring: %s", aobj);
                        return;
                    }
                    appInfo = null;
                    if (len > 0) {
                        firstApplication = applications.getJSONObject(0);
                        atn2 = new ApplicationInfo(firstApplication);
                        appInfo = atn2;
                    }
                }
                mGetStatusRequestTracker_d.onResult_a(reuestId, 0);
                if (mLaunchRequestTracker_b.onResult_a(reuestId, 0)) {
                    if (appInfo != null)
                    {
                        this.mLogs_j.d("application launch has completed", new Object[0]);
                        onConnectToApplicationAndNotify_a(appInfo);
                    }
                    return;
                }
                boolean flag1;
                boolean ignoreVolume;
                if (mTransportId_a == null || (appInfo != null)
                        && appInfo.getTransportId_c().equals(mTransportId_a)) {
                    flag1 = false;
                } else {
                    flag1 = true;
                }

                boolean flag2 = mStopSessionRequestTracker_c.onResult_a(reuestId, 0);

                if (flag1) // goto _L13; else goto _L12
                {
                    this.mLogs_j.d("application has stopped", new Object[0]);
                }
                {
                    if (!flag1 && !flag2) {

                    } else {
                        onApplicationDisconnected_b();
                    }
                    synchronized (RequestTracker.mLock_a) {
                        mSetVolumeRequestTracker_e.onResult_a(reuestId, 0);
                        mSetMuteRequestTracker_f.onResult_a(reuestId, 0);
                        if (mSetVolumeRequestTracker_e.isRunning_b() || mSetMuteRequestTracker_f.isRunning_b())
                            ignoreVolume = true;
                        else
                            ignoreVolume = false;
                    }
                    Logs avu2 = this.mLogs_j;
                    Object aobj1[] = new Object[2];
                    aobj1[0] = Long.valueOf(reuestId);
                    aobj1[1] = Boolean.valueOf(ignoreVolume);
                    avu2.d("requestId = %d, ignoreVolume = %b", aobj1);
                    if (!mFirst_i) {
                        this.mLogs_j.d("first status received, so not ignoring volume change",
                                new Object[0]);
                        mFirst_i = true;
                        ignoreVolume = false;
                    }
                    if (!statusJsonObj.has("volume") || ignoreVolume) {

                    } else {
                        JSONObject jsonobject3 = statusJsonObj.getJSONObject("volume");
                        mLevel_g = jsonobject3.getDouble("level");
                        mMuted_h = jsonobject3.getBoolean("muted");
                    }
                    onStatusReceived_a(appInfo, mLevel_g, mMuted_h);
                    return;
                }
            } else if ("LAUNCH_ERROR".equals(type)) {
                String reason;
                reason = jsonobject.getString("reason");
                if ("BAD_PARAMETER".equals(reason)) {
                    mLaunchRequestTracker_b.onResult_a(reuestId, c1);
                    return;
                } else if ("CANCELLED".equals(reason)) {
                    c1 = '\u07D2';
                } else if ("NOT_ALLOWED".equals(reason)) {
                    c1 = '\u07D3';
                } else if ("NOT_FOUND".equals(reason)) {
                    c1 = '\u07D4';
                } else if ("CAST_INIT_TIMEOUT".equals(reason)) {
                    c1 = '\017';
                } else {
                    c1 = c2;
                }
                mLaunchRequestTracker_b.onResult_a(reuestId, c1);
                return;
            } else if ("INVALID_REQUEST".equals(type)) {
                String reason = jsonobject.getString("reason");
                if ("INVALID_COMMAND".equals(reason) || "DUPLICATE_REQUEST_ID".equals(reason))
                    c2 = c1;
                mLaunchRequestTracker_b.onResult_a(reuestId, c2);
                mStopSessionRequestTracker_c.onResult_a(reuestId, c2);
                mGetStatusRequestTracker_d.onResult_a(reuestId, c2);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return;
    }

    protected abstract void onApplicationDisconnected_b();

    protected abstract void onApplicationConnectionFailed_b(int j);

    public final void stopSession_b(String sessionId)
    {
        JSONObject jsonobject;
        long requestId;
        jsonobject = new JSONObject();
        requestId = getId_c();
        try {
            jsonobject.put("requestId", requestId);
            jsonobject.put("type", "STOP");
            if (sessionId != null)
                if (!"".equals(sessionId))
                    jsonobject.put("sessionId", sessionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendMessage_a(jsonobject.toString(), requestId, mReceiverDestinationId_l);

        mStopSessionRequestTracker_c.start_a(requestId, mStopSessionResult_n);
        return;
    }

    protected abstract void onStatusRequestFailed_c(int j);

    public final void setTransportId_c(String transId)
    {
        mLogs_j.d("current transport id (in control channel) is now: %s", new Object[] {
                transId
        });
        mTransportId_a = transId;
    }
}
