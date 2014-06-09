
package com.fireflycast.server.cast;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

import com.fireflycast.cast.CastDevice;
import com.fireflycast.server.cast.channels.IMediaChannelHelper;
import com.fireflycast.server.cast.channels.MediaControlChannel;
import com.fireflycast.server.cast.channels.MirroringControlChannel;
import com.fireflycast.server.cast.media.MediaItemStatusHelper;
import com.fireflycast.server.cast.media.RouteController;
import com.fireflycast.server.cast.media.RouteCtrlRequestCallback;
import com.fireflycast.server.cast_mirroring.JGCastService;
import com.fireflycast.server.common.images.WebImage;
import com.fireflycast.server.utils.C_dt;
import com.fireflycast.server.utils.IStatusRequest;
import com.fireflycast.server.utils.Logs;
//import com.fireflycast.server.utils.MediaSessionStatus_on;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class CastRouteController extends RouteController implements
        IStatusRequest,
        IMediaChannelHelper {
    public final CastDevice mCastDevice_a;
    public CastDeviceController mCastDeviceController_b;
    double c;
    String d;
    public MediaRouteSession mMediaRouteSession_e;
    public int f;
    public String mSessionId_g;
    boolean h;
    public boolean i;
    JGCastService mJGCastService_j;
    public final CastMediaRouteProvider mCastMediaRouteProvider_k;
    private String mApplicationId_l;
    private boolean isRelaunchApp_m;
    private boolean n;
    private RemotePlaybackRequest o;
    private RemotePlaybackRequest p;
    private RemotePlaybackRequest mSyncStatusRequest_q;
    private PendingIntent mPendingIntent_r;
    private MediaControlChannel mMediaControlChannel_s;
    private long mLoadRequestId_t;
    private boolean u;
    private MirroringControlChannel mMirroringControlChannel_v;
    private final List w = new LinkedList();
    private TrackedItem mTrackedItem_x;

    public CastRouteController(CastMediaRouteProvider awb1, CastDevice castdevice) {
        super();
        mCastMediaRouteProvider_k = awb1;
        mApplicationId_l = (String) CastMediaRouteProvider.i.b();
        isRelaunchApp_m = true;
        mCastDevice_a = castdevice;
        c = 0.0D;
        f = 0;
        mLoadRequestId_t = -1L;
    }

    private static Bundle a(JSONObject jsonobject) {
        if (jsonobject == null) {
            return null;
        }
        Bundle bundle;
        if (!jsonobject.has("httpStatus")) {
            bundle = null;
        } else {
            int httpStatus;
            try {
                httpStatus = jsonobject.getInt("httpStatus");
                bundle = new Bundle();
                bundle.putInt("android.media.status.extra.HTTP_STATUS_CODE", httpStatus);
            } catch (JSONException jsonexception1) {
                bundle = null;
            }
        }
        if (jsonobject.has("httpHeaders")) {
            try {
                Bundle httpHeaders = CastMediaManagerHelper.getBundle_a(jsonobject
                        .getJSONObject("httpHeaders"));
                if (bundle == null)
                    bundle = new Bundle();
                bundle.putBundle("android.media.status.extra.HTTP_RESPONSE_HEADERS", httpHeaders);
            } catch (JSONException jsonexception) {
            }
        }
        return bundle;
    }

    private void a(Intent intent) {
        long l1 = mCastDeviceController_b.h();
        Bundle bundle = intent.getExtras();
        if (bundle.containsKey("com.fireflycast.cast.EXTRA_DEBUG_LOGGING_ENABLED")) {
            boolean flag = bundle
                    .getBoolean("com.fireflycast.cast.EXTRA_DEBUG_LOGGING_ENABLED");
            if (flag)
                l1 |= 1L;
            else
                l1 &= -2L;
            CastMediaRouteProvider.getLogs_a().setDebugEnabled_a(flag);
        }
        mCastDeviceController_b.setDebugLevel_a(l1);
    }

    private void a(TrackedItem aws1) {
        if (mTrackedItem_x == aws1)
            mTrackedItem_x = null;
        w.remove(aws1);
    }

    private void sendPlaybackStateForItem_a(TrackedItem item, int playbackState, Bundle bundle) {
        Logs avu1 = CastMediaRouteProvider.getLogs_a();
        Object aobj[] = new Object[2];
        aobj[0] = item;
        aobj[1] = Integer.valueOf(playbackState);
        avu1.d("sendPlaybackStateForItem for item: %s, playbackState: %d", aobj);
        if (item.mPendingIntent_d == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("android.media.intent.extra.ITEM_ID", item.mItemId_a);
        MediaItemStatusHelper nr1 = (new MediaItemStatusHelper(playbackState)).a(SystemClock
                .uptimeMillis());
        if (bundle != null)
            nr1.a(bundle);
        intent.putExtra("android.media.intent.extra.ITEM_STATUS",
                nr1.createMediaItemStatus_a().mBundle_a);
        try {
            item.mPendingIntent_d.send(
                    ((MediaRouteProvider) (mCastMediaRouteProvider_k)).mContext_a, 0, intent);
            return;
        } catch (android.app.PendingIntent.CanceledException canceledexception) {
            CastMediaRouteProvider.getLogs_a().d(canceledexception,
                    "exception while sending PendingIntent", new Object[0]);
        }
    }

    private boolean processRemotePlaybackRequest_a(RemotePlaybackRequest awt1) {
        String albumTitle;
        Integer discNumber;
        Integer trackNumber;
        CastMediaRouteProvider.getLogs_a().d("processRemotePlaybackRequest()", new Object[0]);
        Intent intent = awt1.mIntent_a;
        String action = intent.getAction();
        Bundle bundle = intent.getBundleExtra("com.fireflycast.cast.EXTRA_CUSTOM_DATA");
        JSONObject jsonobject;
        IllegalStateException illegalstateexception;
        Logs avu1;
        Object aobj[];
        Bundle bundle1;
        Bundle bundle2;
        String appId;
        String s3;
        boolean flag;
        PendingIntent pendingintent;
        MediaControlChannel avv1;
        Bundle bundle3;
        long itemPosition;
        Logs avu2;
        Object aobj1[];
        boolean flag1;
        boolean flag2;
        boolean flag3;
        Uri uri;
        Bundle bundle4;
        MediaMetadata aub1;
        String albumArtist;
        String composer;
        String title;
        String artist;
        String artworkUri;
        MediaInfoContainer aua1;
        String contentType;
        MediaInfo atz1;
        MediaInfo atz2;
        MediaInfo atz3;
        Bundle bundle5;
        JSONObject jsonobject1;
        long pos;
        PendingIntent pendingintent1;
        TrackedItem aws1;
        Logs avu3;
        Object aobj2[];
        Bundle bundle6;
        JSONException jsonexception;
        JSONObject jsonobject2;
        int year;
        Calendar calendar;
        String s11;
        if (bundle != null)
            jsonobject = CastMediaManagerHelper.getJsonObject_a(bundle, null);
        else
            jsonobject = null;
        CastMediaRouteProvider.getLogs_a().d("got remote playback request; action=%s",
                new Object[] {
                    action
                });
        try {
            if (!action.equals("android.media.intent.action.PLAY") || intent.getData() == null) {
                if (action.equals("android.media.intent.action.PAUSE")) {
                    flag3 = checkSession_a(awt1, 0);
                    if (!flag3)
                        return true;
                    try {
                        mMediaControlChannel_s.pause_a(jsonobject);
                        // } catch (IOException ioexception4) {
                    } catch (Exception ioexception4) {
                        CastMediaRouteProvider.getLogs_a().w(ioexception4,
                                "exception while processing %s", new Object[] {
                                    action
                                });
                        awt1.onRouteCtrlRequestFailed_a(1);
                    }
                    return true;
                }

                if (action.equals("android.media.intent.action.RESUME")) {
                    flag2 = checkSession_a(awt1, 0);
                    if (!flag2)
                        return true;
                    try {
                        mMediaControlChannel_s.play_c(jsonobject);
                        // } catch (IOException ioexception3) {
                    } catch (Exception ioexception3) {
                        CastMediaRouteProvider.getLogs_a().w(ioexception3,
                                "exception while processing %s", new Object[] {
                                    action
                                });
                        awt1.onRouteCtrlRequestFailed_a(1);
                    }
                    return true;
                }

                if (action.equals("android.media.intent.action.STOP")) {
                    flag1 = checkSession_a(awt1, 0);
                    if (!flag1)
                        return true;
                    try {
                        mMediaControlChannel_s.stop_b(jsonobject);
                        // } catch (IOException ioexception2) {
                    } catch (Exception ioexception2) {
                        CastMediaRouteProvider.getLogs_a().w(ioexception2,
                                "exception while processing %s", new Object[] {
                                    action
                                });
                        awt1.onRouteCtrlRequestFailed_a(1);
                    }
                    return true;
                }

                if (action.equals("android.media.intent.action.SEEK")) {
                    if (!checkSession_a(awt1, 0))
                        return true;
                    d(intent.getStringExtra("android.media.intent.extra.ITEM_ID"));
                    itemPosition = intent.getLongExtra("android.media.intent.extra.ITEM_POSITION",
                            0L);
                    try {
                        avu2 = CastMediaRouteProvider.getLogs_a();
                        aobj1 = new Object[1];
                        aobj1[0] = Long.valueOf(itemPosition);
                        avu2.d("seeking to %d ms", aobj1);
                        mMediaControlChannel_s.seekTime_a(this, itemPosition, jsonobject);
                        // } catch (IOException ioexception1) {
                    } catch (Exception ioexception1) {
                        CastMediaRouteProvider.getLogs_a().w(ioexception1,
                                "exception while processing %s", new Object[] {
                                    action
                                });
                        awt1.onRouteCtrlRequestFailed_a(1);
                    }
                    return true;
                }

                if (action.equals("android.media.intent.action.GET_STATUS")) {
                    if (!checkSession_a(awt1, 0))
                        return true;
                    d(intent.getStringExtra("android.media.intent.extra.ITEM_ID"));
                    if (mMediaControlChannel_s == null) {
                        awt1.onRouteCtrlRequestFailed_a(2);
                        return true;
                    }
                    bundle3 = new Bundle();
                    bundle3.putParcelable("android.media.intent.extra.ITEM_STATUS",
                            getItemStatusBundle_i());
                    bundle3.putParcelable("android.media.intent.extra.SESSION_STATUS",
                            createSessionStatusBundle_g(0));
                    awt1.onRouteCtrlRequestOk_a(bundle3);
                    return true;
                }

                if (action.equals("com.fireflycast.cast.ACTION_SYNC_STATUS")) {
                    if (!checkSession_a(awt1, 0))
                        return true;
                    avv1 = mMediaControlChannel_s;
                    if (avv1 == null) {
                        awt1.onRouteCtrlRequestFailed_a(2);
                        return true;
                    }
                    try {
                        if (mLoadRequestId_t == -1L)
                            mLoadRequestId_t = mMediaControlChannel_s.getStatus_a(this);
                        mSyncStatusRequest_q = awt1;
                        // } catch (IOException ioexception) {
                    } catch (Exception ioexception) {
                        mSyncStatusRequest_q = null;
                        CastMediaRouteProvider.getLogs_a().w(ioexception,
                                "exception while processing %s", new Object[] {
                                    action
                                });
                        awt1.onRouteCtrlRequestFailed_a(1);
                    }
                    return true;
                }

                if (!action.equals("android.media.intent.action.START_SESSION")) {
                    if (action.equals("android.media.intent.action.GET_SESSION_STATUS")) {
                        checkSession_a(awt1, 0);
                        bundle2 = new Bundle();
                        bundle2.putParcelable("android.media.intent.extra.SESSION_STATUS",
                                createSessionStatusBundle_g(0));
                        awt1.onRouteCtrlRequestOk_a(bundle2);
                        return true;
                    }
                    if (action.equals("android.media.intent.action.END_SESSION")) {
                        checkSession_a(awt1, 0);
                        sendPendingIntent_a(getSessionId_e(), 1);
                        mPendingIntent_r = null;
                        endSession_d();
                        bundle1 = new Bundle();
                        bundle1.putParcelable("android.media.intent.extra.SESSION_STATUS",
                                createSessionStatusBundle_g(1));
                        awt1.onRouteCtrlRequestOk_a(bundle1);
                        return true;
                    }
                    return false;

                }
                appId = intent
                        .getStringExtra("com.fireflycast.cast.EXTRA_CAST_APPLICATION_ID");
                if (!TextUtils.isEmpty(appId)) {
                    s3 = appId;

                } else {
                    s3 = (String) CastMediaRouteProvider.i.b();
                }
                flag = intent.getBooleanExtra(
                        "com.fireflycast.cast.EXTRA_CAST_RELAUNCH_APPLICATION", true);
                n = intent
                        .getBooleanExtra(
                                "com.fireflycast.cast.EXTRA_CAST_STOP_APPLICATION_WHEN_SESSION_ENDS",
                                false);
                pendingintent = (PendingIntent) intent
                        .getParcelableExtra("android.media.intent.extra.SESSION_STATUS_UPDATE_RECEIVER");
                if (pendingintent == null) {
                    CastMediaRouteProvider.getLogs_a().d(
                            "No status update receiver supplied to %s",
                            new Object[] {
                                action
                            });
                    return false;
                }
                a(intent);
                mPendingIntent_r = pendingintent;
                mApplicationId_l = s3;
                isRelaunchApp_m = flag;
                p = awt1;
                startSession_e(0);
                return true;

            }
            if (intent.getStringExtra("android.media.intent.extra.SESSION_ID") == null) {
                s11 = intent
                        .getStringExtra("com.fireflycast.cast.EXTRA_CAST_APPLICATION_ID");
                if (TextUtils.isEmpty(s11))
                    s11 = (String) CastMediaRouteProvider.i.b();
                mApplicationId_l = s11;
            }
            if (!checkSession_a(awt1, 1))
                return true;
            uri = intent.getData();
            if (uri == null)
                return false;
            CastMediaRouteProvider.getLogs_a().d("Device received play request, uri %s",
                    new Object[] {
                        uri
                    });
            a(intent);
            bundle4 = intent.getBundleExtra("android.media.intent.extra.ITEM_METADATA");
            aub1 = null;
            if (bundle4 != null) {
                albumTitle = bundle4.getString("android.media.metadata.ALBUM_TITLE");
                albumArtist = bundle4.getString("android.media.metadata.ALBUM_ARTIST");
                composer = bundle4.getString("android.media.metadata.COMPOSER");
                if (!bundle4.containsKey("android.media.metadata.DISC_NUMBER")) {
                    discNumber = null;

                } else {
                    discNumber = Integer.valueOf(bundle4
                            .getInt("android.media.metadata.DISC_NUMBER"));
                }
                if (!bundle4.containsKey("android.media.metadata.TRACK_NUMBER")) {
                    trackNumber = null;

                } else {
                    trackNumber = Integer.valueOf(bundle4
                            .getInt("android.media.metadata.TRACK_NUMBER"));
                }
                if (albumTitle == null && discNumber == null && trackNumber == null) {
                    aub1 = new MediaMetadata(0);

                } else {
                    aub1 = new MediaMetadata(3);
                    if (albumTitle != null)
                        aub1.putString_a("com.fireflycast.cast.metadata.ALBUM_TITLE",
                                albumTitle);
                    if (albumArtist != null)
                        aub1.putString_a("com.fireflycast.cast.metadata.ALBUM_ARTIST",
                                albumArtist);
                    if (composer != null)
                        aub1.putString_a("com.fireflycast.cast.metadata.COMPOSER", composer);
                    if (discNumber != null)
                        aub1.a("com.fireflycast.cast.metadata.DISC_NUMBER",
                                discNumber.intValue());
                    if (trackNumber != null)
                        aub1.a("com.fireflycast.cast.metadata.TRACK_NUMBER",
                                trackNumber.intValue());
                }
                title = bundle4.getString("android.media.metadata.TITLE");
                if (title != null)
                    aub1.putString_a("com.fireflycast.cast.metadata.TITLE", title);
                artist = bundle4.getString("android.media.metadata.ARTIST");
                if (artist != null)
                    aub1.putString_a("com.fireflycast.cast.metadata.ARTIST", artist);
                if (bundle4.containsKey("android.media.metadata.YEAR")) {
                    year = bundle4.getInt("android.media.metadata.YEAR");
                    calendar = Calendar.getInstance();
                    calendar.set(1, year);
                    aub1.a("com.fireflycast.cast.metadata.RELEASE_DATE", calendar);
                }
                if (bundle4.containsKey("android.media.metadata.ARTWORK_URI")) {
                    artworkUri = bundle4.getString("android.media.metadata.ARTWORK_URI");
                    if (!TextUtils.isEmpty(artworkUri))
                        aub1.addImage_a(new WebImage(Uri.parse(artworkUri)));
                }

            }
            aua1 = new MediaInfoContainer(uri.toString());
            aua1.mMediaInfo_a.mStreamType_b = 1;
            contentType = intent.getType();
            atz1 = aua1.mMediaInfo_a;
            if (!TextUtils.isEmpty(contentType)) {
                atz1.mContentType_c = contentType;
                aua1.mMediaInfo_a.mMediaMetadata_d = aub1;
                atz2 = aua1.mMediaInfo_a;
                if (TextUtils.isEmpty(atz2.mContentId_a))
                    throw new IllegalArgumentException("content ID cannot be null or empty");
                if (TextUtils.isEmpty(atz2.mContentType_c))
                    throw new IllegalArgumentException("content type cannot be null or empty");
                if (atz2.mStreamType_b == -1)
                    throw new IllegalArgumentException("a valid stream type must be specified");
                atz3 = aua1.mMediaInfo_a;
                bundle5 = intent.getBundleExtra("android.media.intent.extra.HTTP_HEADERS");
                if (bundle5 == null) {
                    jsonobject1 = jsonobject;

                } else {
                    try {
                        jsonobject2 = CastMediaManagerHelper.getJsonObject_a(bundle5, null);
                        if (jsonobject == null) {
                            jsonobject = new JSONObject();
                        }
                        jsonobject.put("httpHeaders", jsonobject2);
                    } catch (JSONException e) {
                    }
                    jsonobject1 = jsonobject;
                }
                pos = intent.getLongExtra("android.media.intent.extra.ITEM_POSITION", 0L);
                pendingintent1 = (PendingIntent) intent
                        .getParcelableExtra("android.media.intent.extra.ITEM_STATUS_UPDATE_RECEIVER");
                try {
                    aws1 = new TrackedItem(this, mMediaControlChannel_s.load_a(this, atz3, pos,
                            jsonobject1));
                    aws1.mPendingIntent_d = pendingintent1;
                    w.add(aws1);
                    avu3 = CastMediaRouteProvider.getLogs_a();
                    aobj2 = new Object[1];
                    aobj2[0] = aws1.mItemId_a;
                    avu3.d("loading media with item id assigned as %s", aobj2);
                    bundle6 = new Bundle();
                    bundle6.putString("android.media.intent.extra.SESSION_ID", getSessionId_e());
                    bundle6.putParcelable("android.media.intent.extra.SESSION_STATUS",
                            createSessionStatusBundle_g(0));
                    bundle6.putString("android.media.intent.extra.ITEM_ID", aws1.mItemId_a);
                    bundle6.putBundle("android.media.intent.extra.ITEM_STATUS",
                            (new MediaItemStatusHelper(3)).a(SystemClock.uptimeMillis())
                                    .createMediaItemStatus_a().mBundle_a);
                    awt1.onRouteCtrlRequestOk_a(bundle6);
                    // } catch (IOException ioexception5) {
                } catch (Exception ioexception5) {
                    CastMediaRouteProvider.getLogs_a().w(ioexception5,
                            "exception while processing %s",
                            new Object[] {
                                action
                            });
                    awt1.onRouteCtrlRequestFailed_a(1);
                }
                return true;

            }
            throw new IllegalArgumentException("content type cannot be null or empty");
        } catch (IllegalStateException e) {
            avu1 = CastMediaRouteProvider.getLogs_a();
            aobj = new Object[1];
            aobj[0] = e.getMessage();
            avu1.d("can't process command; %s", aobj);
            return false;
        }
    }

    private boolean checkSession_a(RemotePlaybackRequest awt1, int i1) {
        String sessionId = awt1.mIntent_a.getStringExtra("android.media.intent.extra.SESSION_ID");
        String currentSessionId = getSessionId_e();
        CastMediaRouteProvider.getLogs_a().d(
                "checkSession() sessionId=%s, currentSessionId=%s",
                new Object[] {
                        sessionId, currentSessionId
                });
        if (TextUtils.isEmpty(sessionId)) {
            if (h && currentSessionId != null) {
                h = false;
                return true;
            }
            if (i1 == 1) {
                o = awt1;
                h = true;
                if (mCastDeviceController_b.d()) {
                    startSession_e(0);
                } else {
                    f = 2;
                    mSessionId_g = null;
                }
                return false;
            }
        } else {
            if (sessionId.equals(currentSessionId)) {
                h = false;
                return true;
            }
            if (currentSessionId == null) {
                o = awt1;
                if (mCastDeviceController_b.d()) {
                    resumeSession_c(sessionId);
                } else {
                    f = 2;
                    mSessionId_g = sessionId;
                }
                return false;
            }
        }
        awt1.onRouteCtrlRequestFailed_a(2);
        return false;
    }

    private void resumeSession_c(String sessionId) {
        CastMediaRouteProvider.getLogs_a().d("resumeSession()", new Object[0]);
        mMediaRouteSession_e.joinApplication_a(mApplicationId_l, sessionId);
    }

    private void d(String itemId) {
        if (mTrackedItem_x == null)
            throw new IllegalStateException("no current item");
        if (!mTrackedItem_x.mItemId_a.equals(itemId))
            throw new IllegalStateException("item ID does not match current item");
        else
            return;
    }

    private void startSession_e(int i1) {
        CastMediaRouteProvider.getLogs_a().d("startSession()", new Object[0]);
        if (i1 == 1) {
            MediaRouteSession axe1 = mMediaRouteSession_e;
            int j1 = ((WifiManager) ((MediaRouteProvider) (mCastMediaRouteProvider_k)).mContext_a
                    .getSystemService("wifi"))
                    .getConnectionInfo().getIpAddress();
            axe1.startSession_a("674A0243",
                    (new StringBuilder()).append(j1 & 0xff).append(".").append(0xff & j1 >> 8)
                            .append(".").append(0xff & j1 >> 16).append(".")
                            .append(0xff & j1 >> 24).toString(), true);
            return;
        } else {
            mMediaRouteSession_e.startSession_a(mApplicationId_l, null, isRelaunchApp_m);
            return;
        }
    }

    private void f(int i1) {
        for (Iterator iterator = w.iterator(); iterator.hasNext(); sendPlaybackStateForItem_a(
                (TrackedItem) iterator.next(), i1, ((Bundle) (null))))
            ;
        w.clear();
        mTrackedItem_x = null;
    }

    private static final class MediaSessionStatus_on {

        public final Bundle mData_a;

        private MediaSessionStatus_on(Bundle bundle) {
            mData_a = bundle;
        }

        public MediaSessionStatus_on(Bundle bundle, byte byte0) {
            this(bundle);
        }

        public final String toString() {
            StringBuilder stringbuilder;
            StringBuilder stringbuilder1;
            int i;
            stringbuilder = new StringBuilder();
            stringbuilder.append("MediaSessionStatus{ ");
            stringbuilder.append("timestamp=");
            C_dt.a(SystemClock.elapsedRealtime() - mData_a.getLong("timestamp"), stringbuilder);
            stringbuilder.append(" ms ago");
            stringbuilder1 = stringbuilder.append(", sessionState=");
            i = mData_a.getInt("sessionState", 2);
            String s;
            switch (i) {
                case 0:
                    s = "active";
                    break;
                case 1:
                    s = "ended";
                    break;
                case 2:
                    s = "invalidated";
                    break;
                default:
                    s = Integer.toString(i);
            }
            stringbuilder1.append(s);
            stringbuilder.append(", queuePaused=").append(mData_a.getBoolean("queuePaused"));
            stringbuilder.append(", extras=").append(mData_a.getBundle("extras"));
            stringbuilder.append(" }");
            return stringbuilder.toString();
        }
    }

    private static class SessionStatusBundle_oo {

        public final Bundle data = new Bundle();

        public SessionStatusBundle_oo(int sessionState) {
            setTimestamp_a(SystemClock.elapsedRealtime());
            data.putInt("sessionState", sessionState);
        }

        public final SessionStatusBundle_oo setTimestamp_a(long timestamp) {
            data.putLong("timestamp", timestamp);
            return this;
        }
    }

    private Bundle createSessionStatusBundle_g(int sessionState) {
        SessionStatusBundle_oo oo1 = new SessionStatusBundle_oo(sessionState);
        MediaStatus aud1;

        boolean queuePaused;
        if (mMediaControlChannel_s != null
                && (aud1 = mMediaControlChannel_s.getMediaStatus_e()) != null) {
            if (aud1.mPlayerState_d == 3) // queue paused?
                queuePaused = true;
            else
                queuePaused = false;
        } else {
            queuePaused = false;
        }
        oo1.data.putBoolean("queuePaused", queuePaused);
        return (new MediaSessionStatus_on(oo1.setTimestamp_a(SystemClock.uptimeMillis()).data,
                (byte) 0)).mData_a;
    }

    private Bundle getItemStatusBundle_i() {
        byte byte0;
        MediaStatus aud1;
        byte0 = 5;
        aud1 = mMediaControlChannel_s.getMediaStatus_e();
        if (aud1 != null) {
            int i1;
            int j1;
            i1 = aud1.mPlayerState_d;
            j1 = aud1.mIdleReason_e;
            switch (i1) {
                case 1:
                    switch (j1) {
                        default:
                            byte0 = 7;
                            break;

                        case 4: // '\004'
                            byte0 = 7;
                            break;

                        case 1: // '\001'
                            byte0 = 4;
                            break;

                        case 3: // '\003'
                            byte0 = 6;
                            break;

                        case 2: // '\002'
                            break;
                    }
                    break;
                case 2:
                    byte0 = 1;
                    break;
                case 3:
                    byte0 = 2;
                    break;
                case 4:
                    byte0 = 3;
                    break;
                default:
                    byte0 = 7;
                    break;
            }
            MediaItemStatusHelper nr1 = new MediaItemStatusHelper(byte0);
            long contentDuration = mMediaControlChannel_s.getContentDuration_b();
            nr1.mData_a.putLong("contentDuration", contentDuration);
            long l2 = mMediaControlChannel_s.a();
            nr1.mData_a.putLong("contentPosition", l2);
            MediaItemStatusHelper nr2 = nr1.a(SystemClock.uptimeMillis());
            Bundle bundle = a(aud1.mCustomData_g);
            if (bundle != null)
                nr2.a(bundle);
            return nr2.createMediaItemStatus_a().mBundle_a;
        }
        CastMediaRouteProvider.getLogs_a().d("*** media status is null!", new Object[0]);
        return (new MediaItemStatusHelper(byte0)).createMediaItemStatus_a().mBundle_a;
    }

    public final void onRelease_a() {
        CastMediaRouteProvider.getLogs_a().d("Controller released", new Object[0]);
    }

    public final void onSetVolume_a(int i1) {
        Logs avu1 = CastMediaRouteProvider.getLogs_a();
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(i1);
        avu1.d("onSetVolume() volume=%d", aobj);
        if (mCastDeviceController_b == null) {
            return;
        }
        double d1 = (double) i1 / 20D;
        try {
            mCastDeviceController_b.setVolume_a(d1, c, false);
        } catch (IllegalStateException e) {
            Logs avu2 = CastMediaRouteProvider.getLogs_a();
            Object aobj1[] = new Object[1];
            aobj1[0] = e.getMessage();
            avu2.d("Unable to set volume: %s", aobj1);
        }
    }

    public final void requestStatus_a(long loadRequestId, int status, JSONObject jsonobject) {
        Iterator iterator = w.iterator();
        TrackedItem aws1;
        do {
            if (!iterator.hasNext()) {
                aws1 = null;
                break;
            }
            aws1 = (TrackedItem) iterator.next();
        } while (aws1.mLoadRequestId_b != loadRequestId);
        if (aws1 == null) {
            if (loadRequestId != mLoadRequestId_t) {
                return;
            }
            CastMediaRouteProvider.getLogs_a().d("requestStatus has completed", new Object[0]);
            mLoadRequestId_t = -1L;
            long sessionId;
            Iterator iterator1;
            try {
                sessionId = mMediaControlChannel_s.getMediaSessionId_g();
                iterator1 = w.iterator();
                TrackedItem aws2;
                do {
                    if (!iterator1.hasNext()) {
                        aws2 = null;
                        break;
                    }
                    aws2 = (TrackedItem) iterator1.next();
                } while (aws2.mMediaSessionId_c != sessionId);
                if (mTrackedItem_x != null && mTrackedItem_x != aws2) {
                    sendPlaybackStateForItem_a(mTrackedItem_x, 4, ((Bundle) (null)));
                    a(mTrackedItem_x);
                }
                if (mSyncStatusRequest_q != null) {
                    TrackedItem aws3 = new TrackedItem(this);
                    aws3.mMediaSessionId_c = sessionId;
                    aws3.mPendingIntent_d = (PendingIntent) mSyncStatusRequest_q.mIntent_a
                            .getParcelableExtra("android.media.intent.extra.ITEM_STATUS_UPDATE_RECEIVER");
                    w.add(aws3);
                    mTrackedItem_x = aws3;
                }
                Iterator iterator2 = w.iterator();
                while (iterator2.hasNext()) {
                    TrackedItem aws4 = (TrackedItem) iterator2.next();
                    if (aws4.mMediaSessionId_c != -1L
                            && (mTrackedItem_x == null || aws4.mMediaSessionId_c < mTrackedItem_x.mMediaSessionId_c)) {
                        sendPlaybackStateForItem_a(aws4, 4, ((Bundle) (null)));
                        iterator2.remove();
                    }
                }
            } catch (IllegalStateException illegalstateexception) {
                f(4);
                mTrackedItem_x = null;
            }
            Logs avu1 = CastMediaRouteProvider.getLogs_a();
            Object aobj[] = new Object[2];
            aobj[0] = mSyncStatusRequest_q;
            aobj[1] = Integer.valueOf(status);
            avu1.d("mSyncStatusRequest = %s, status=%d", aobj);
            if (mSyncStatusRequest_q != null) {
                if (status == 0) {
                    CastMediaRouteProvider.getLogs_a().d(
                            "requestStatus completed; sending response",
                            new Object[0]);
                    Bundle bundle = new Bundle();
                    if (mTrackedItem_x != null) {
                        MediaStatus aud1 = mMediaControlChannel_s.getMediaStatus_e();
                        bundle.putString("android.media.intent.extra.ITEM_ID",
                                mTrackedItem_x.mItemId_a);
                        bundle.putParcelable("android.media.intent.extra.ITEM_STATUS",
                                getItemStatusBundle_i());
                        MediaInfo atz1 = aud1.mMedia_b;
                        if (atz1 != null) {
                            Bundle bundle1 = CastMediaManagerHelper
                                    .createMetadataBundle_a(atz1);
                            CastMediaRouteProvider.getLogs_a().d("adding metadata bundle: %s",
                                    new Object[] {
                                        bundle1
                                    });
                            bundle.putParcelable("android.media.intent.extra.ITEM_METADATA",
                                    bundle1);
                        }
                    }
                    mSyncStatusRequest_q.onRouteCtrlRequestOk_a(bundle);
                } else {
                    mSyncStatusRequest_q.onRouteCtrlRequestFailed_a(1);
                }
                mSyncStatusRequest_q = null;
            }

            return;
        }
        long mediaSessionId = mMediaControlChannel_s.getMediaSessionId_g();
        switch (status) {
            default:
            case 1:
                Logs avu3 = CastMediaRouteProvider.getLogs_a();
                Object aobj2[] = new Object[1];
                aobj2[0] = Integer.valueOf(status);
                avu3.d("unknown status %d; sending error state", aobj2);
                sendPlaybackStateForItem_a(aws1, 7, a(jsonobject));
                a(aws1);
                break;
            case 0:
                Logs avu2 = CastMediaRouteProvider.getLogs_a();
                Object aobj1[] = new Object[1];
                aobj1[0] = Long.valueOf(mediaSessionId);
                avu2.d("Load completed; mediaSessionId=%d", aobj1);
                aws1.mLoadRequestId_b = -1L;
                aws1.mMediaSessionId_c = mediaSessionId;
                mTrackedItem_x = aws1;
                sendItemStatusUpdate_h();
                break;
            case 2:
                CastMediaRouteProvider.getLogs_a().d("STATUS_CANCELED; sending error state",
                        new Object[0]);
                sendPlaybackStateForItem_a(aws1, 5, ((Bundle) (null)));
                a(aws1);
                break;
            case 3:
                CastMediaRouteProvider.getLogs_a().d("STATUS_TIMED_OUT; sending error state",
                        new Object[0]);
                sendPlaybackStateForItem_a(aws1, 7, ((Bundle) (null)));
                a(aws1);
                break;
        }
    }

    public final void attachMediaChannel_a(String sessionId)
    {
        ApplicationMetadata applicationmetadata;
        applicationmetadata = mMediaRouteSession_e.getApplicationMetadata_a();
        if (p != null) {
            Bundle bundle = new Bundle();
            bundle.putString("android.media.intent.extra.SESSION_ID", sessionId);
            p.onRouteCtrlRequestOk_a(bundle);
            p = null;
        }
        sendPendingIntent_a(sessionId, 0);
        if (mApplicationId_l.equals(applicationmetadata.getApplicationId_b())) {
            CastMediaRouteProvider.getLogs_a().d("attachMediaChannel", new Object[0]);

            // mMediaControlChannel_s = new C_awo(this);
            mMediaControlChannel_s = new MediaControlChannel() {
                protected final void onStatusUpdated_h() {
                    CastMediaRouteProvider.getLogs_a().d("onStatusUpdated", new Object[0]);
                    sendItemStatusUpdate_h();
                }

                protected final void sendItemStatusUpdate_i() {
                    sendItemStatusUpdate_h();
                }
            };

            mCastDeviceController_b.a(mMediaControlChannel_s);
            if (o != null) {
                processRemotePlaybackRequest_a(o);
                o = null;
            }
        } else {
            if ("674A0243".equals(applicationmetadata.getApplicationId_b())) {
                u = true;

                // mMirroringControlChannel_v = new C_awp(this, mCastDevice_a,
                // mCastDeviceController_b.getTransId_o());
                mMirroringControlChannel_v = new MirroringControlChannel(mCastDevice_a,
                        mCastDeviceController_b.getTransId_o()) {
                    protected final void onAnswer_b(final String s) {
                        CastMediaRouteProvider.getLogs_a().d("onAnswer", new Object[0]);
                        // ((C_nv) (a.mCastMediaRouteProvider_k)).c.post(new
                        // C_awq(this, s));
                        ((MediaRouteProvider) (mCastMediaRouteProvider_k)).mHandler_c
                                .post(new Runnable() {

                                    @Override
                                    public void run() {
                                        // TODO Auto-generated method stub
                                        // todo
                                    }

                                });
                    }
                };

                try {
                    mCastDeviceController_b.a(mMirroringControlChannel_v);
                    mMirroringControlChannel_v.a();
                } catch (Exception ioexception) {
                    CastMediaRouteProvider.getLogs_a().d(ioexception, "Failed to send offer",
                            new Object[0]);
                }
            }
        }
        if (mLoadRequestId_t != -1L || mMediaControlChannel_s == null)
            return;
        try {
            mLoadRequestId_t = mMediaControlChannel_s.getStatus_a(this);
        } catch (Exception ioexception1) {
            CastMediaRouteProvider.getLogs_a().w(ioexception1,
                    "Exception while requesting media status", new Object[0]);

        }
    }

    final void sendPendingIntent_a(String sessionId, int sessionStatus) {
        if (sessionId == null || mPendingIntent_r == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("android.media.intent.extra.SESSION_ID", sessionId);
        intent.putExtra("android.media.intent.extra.SESSION_STATUS",
                createSessionStatusBundle_g(sessionStatus));
        try {
            CastMediaRouteProvider.getLogs_a().d(
                    "Invoking session status PendingIntent with: %s",
                    new Object[] {
                        intent
                    });
            mPendingIntent_r.send(((MediaRouteProvider) (mCastMediaRouteProvider_k)).mContext_a,
                    0, intent);
            return;
        } catch (android.app.PendingIntent.CanceledException canceledexception) {
            CastMediaRouteProvider.getLogs_a().d(canceledexception,
                    "exception while sending PendingIntent", new Object[0]);
        }
    }

    final void a(boolean flag) {
        if (CastMediaRouteProvider.f(mCastMediaRouteProvider_k) != null)
            CastMediaRouteProvider.f(mCastMediaRouteProvider_k).a(mCastDevice_a, flag);
    }

    public final boolean onControlRequest_a(Intent intent, RouteCtrlRequestCallback om) {
        boolean flag;
        CastMediaRouteProvider.getLogs_a().d("Received control request %s", new Object[] {
                intent
        });
        RemotePlaybackRequest awt1 = new RemotePlaybackRequest(mCastMediaRouteProvider_k,
                intent, om);
        if (!intent.hasCategory("android.media.intent.category.REMOTE_PLAYBACK")) {
            boolean flag1 = intent
                    .hasCategory("com.fireflycast.cast.CATEGORY_CAST_REMOTE_PLAYBACK");
            flag = false;
            if (!flag1)
                return flag;
        }
        flag = processRemotePlaybackRequest_a(awt1);
        return flag;
    }

    public final void onSelect_b() {
        CastMediaRouteProvider.getLogs_a().d("onSelect", new Object[0]);
        mCastDeviceController_b = CastMediaRouteProvider.createDeviceController_a(
                mCastMediaRouteProvider_k, this);
        mMediaRouteSession_e = new MediaRouteSession(mCastDeviceController_b, this,
                ((MediaRouteProvider) (mCastMediaRouteProvider_k)).mHandler_c);
    }

    public final void onUpdateVolume_b(int delta) {
        Logs avu1 = CastMediaRouteProvider.getLogs_a();
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(delta);
        avu1.d("onUpdateVolume() delta=%d", aobj);
        if (mCastDeviceController_b == null)
            return;
        try {
            double d1 = c + (double) delta / 20D;
            mCastDeviceController_b.setVolume_a(d1, c, false);
            return;
        } catch (IllegalStateException illegalstateexception) {
            Logs avu2 = CastMediaRouteProvider.getLogs_a();
            Object aobj1[] = new Object[1];
            aobj1[0] = illegalstateexception.getMessage();
            avu2.d("Unable to update volume: %s", aobj1);
            return;
        }
    }

    public final void sendPendingIntent_b(String sessionId) {
        if (p != null) {
            p.onRouteCtrlRequestFailed_a(2);
            p = null;
        }
        sendPendingIntent_a(sessionId, 1);
    }

    public final void disconnectCastMirror_b(boolean flag) {
        if (i) {
            if (mJGCastService_j != null) {
                CastMediaRouteProvider.getLogs_a().d("Destroying mirroring client",
                        new Object[0]);
                mJGCastService_j.disconnect();
                mJGCastService_j = null;
            }
            if (mMirroringControlChannel_v != null) {
                if (mCastDeviceController_b != null)
                    mCastDeviceController_b.b(mMirroringControlChannel_v);
                mMirroringControlChannel_v = null;
            }
            a(flag);
            i = false;
        }
    }

    public final void onUnselect_c() {
        CastMediaRouteProvider.getLogs_a().d("onUnselect", new Object[0]);
        endSession_d();
        CastMediaRouteProvider.b(mCastMediaRouteProvider_k, this);
        mCastDeviceController_b = null;
    }

    public final void onApplicationDisconnected_c(int statusCode) {
        Logs avu1 = CastMediaRouteProvider.getLogs_a();
        Object aobj[] = new Object[1];
        aobj[0] = Integer.valueOf(statusCode);
        avu1.d("onApplicationDisconnected: statusCode=%d", aobj);
        boolean flag = false;
        if (statusCode != 0)
            flag = true;
        disconnectCastMirror_b(flag);
        if (mMediaRouteSession_e != null) {
            mMediaRouteSession_e.onApplicationDisconnected_b(statusCode);
            sendPendingIntent_a(getSessionId_e(), 1);
        }
    }

    public final void endSession_d() {
        Logs avu1 = CastMediaRouteProvider.getLogs_a();
        Object aobj[] = new Object[1];
        aobj[0] = Boolean.valueOf(true);
        avu1.d("endSession() voluntary=%b", aobj);
        if (mMediaRouteSession_e != null) {
            mMediaRouteSession_e.stopSession_a(u | n);
            u = false;
            n = false;
        }
    }

    public final void detachMediaChannel_d(int i1) {
        boolean flag;
        byte byte0;
        if (i1 == 0)
            flag = true;
        else
            flag = false;
        if (flag)
            byte0 = 5;
        else
            byte0 = 6;
        f(byte0);
        if (!i) {
            CastMediaRouteProvider.getLogs_a().d("detachMediaChannel", new Object[0]);
            if (mMediaControlChannel_s != null) {
                if (mCastDeviceController_b != null)
                    mCastDeviceController_b.b(mMediaControlChannel_s);
                mMediaControlChannel_s = null;
            }
        }
        p = null;
    }

    final String getSessionId_e() {
        if (mMediaRouteSession_e == null)
            return null;
        else
            return mMediaRouteSession_e.getSessionId_b();
    }

    public final void startSession_f() {
        switch (f) {
            case 1:
                CastMediaRouteProvider.getLogs_a().d("starting pending session for mirroring",
                        new Object[0]);
                startSession_e(1);
                break;
            case 2:
                Logs avu1 = CastMediaRouteProvider.getLogs_a();
                Object aobj[] = new Object[1];
                aobj[0] = mSessionId_g;
                avu1.d("starting pending session for media with session ID %s", aobj);
                if (mSessionId_g != null) {
                    resumeSession_c(mSessionId_g);
                    mSessionId_g = null;
                } else {
                    startSession_e(0);
                }
                break;
        }
        f = 0;
        return;
    }

    public final void g() {
        disconnectCastMirror_b(true);
        CastMediaRouteProvider.b(mCastMediaRouteProvider_k, this);
    }

    final void sendItemStatusUpdate_h() {
        Logs avu1 = CastMediaRouteProvider.getLogs_a();
        Object aobj[] = new Object[1];
        aobj[0] = mTrackedItem_x;
        avu1.d("sendItemStatusUpdate(); current item is %s", aobj);
        if (mTrackedItem_x != null) {
            PendingIntent pendingintent = mTrackedItem_x.mPendingIntent_d;
            if (pendingintent != null) {
                Logs avu3 = CastMediaRouteProvider.getLogs_a();
                Object aobj2[] = new Object[1];
                aobj2[0] = mTrackedItem_x;
                avu3.d("found a PendingIntent for item %s", aobj2);
                Intent intent = new Intent();
                intent.putExtra("android.media.intent.extra.ITEM_ID", mTrackedItem_x.mItemId_a);
                intent.putExtra("android.media.intent.extra.ITEM_STATUS", getItemStatusBundle_i());
                MediaInfo atz1 = mMediaControlChannel_s.getMediaInfo_f();
                if (atz1 != null) {
                    Bundle bundle = CastMediaManagerHelper.createMetadataBundle_a(atz1);
                    Logs avu4 = CastMediaRouteProvider.getLogs_a();
                    Object aobj3[] = new Object[1];
                    aobj3[0] = bundle.toString();
                    avu4.d("adding metadata bundle: %s", aobj3);
                    intent.putExtra("android.media.intent.extra.ITEM_METADATA", bundle);
                }
                try {
                    CastMediaRouteProvider.getLogs_a().d(
                            "Invoking item status PendingIntent with: %s",
                            new Object[] {
                                intent
                            });
                    pendingintent.send(
                            ((MediaRouteProvider) (mCastMediaRouteProvider_k)).mContext_a, 0,
                            intent);
                } catch (android.app.PendingIntent.CanceledException canceledexception) {
                    CastMediaRouteProvider.getLogs_a().d(canceledexception,
                            "exception while sending PendingIntent", new Object[0]);
                }
            }
            Logs avu2;
            Object aobj1[];
            if (mMediaControlChannel_s.getMediaStatus_e().mPlayerState_d == 1) {
                avu2 = CastMediaRouteProvider.getLogs_a();
                aobj1 = new Object[1];
                aobj1[0] = mTrackedItem_x;
                avu2.d("player state is now IDLE; removing tracked item %s", aobj1);
                a(mTrackedItem_x);
            }
        }
    }
}
