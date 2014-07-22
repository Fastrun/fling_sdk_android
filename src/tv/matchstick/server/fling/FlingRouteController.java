
package tv.matchstick.server.fling;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import tv.matchstick.fling.FlingDevice;
import tv.matchstick.server.cast_mirroring.JGCastService;
import tv.matchstick.server.common.images.WebImage;
import tv.matchstick.server.fling.channels.IMediaChannelHelper;
import tv.matchstick.server.fling.channels.MediaControlChannel;
import tv.matchstick.server.fling.channels.MirroringControlChannel;
import tv.matchstick.server.fling.media.MediaItemStatusHelper;
import tv.matchstick.server.fling.media.RouteController;
import tv.matchstick.server.fling.media.RouteCtrlRequestCallback;
import tv.matchstick.server.utils.C_dt;
import tv.matchstick.server.utils.IStatusRequest;
import tv.matchstick.server.utils.LOG;

import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class FlingRouteController extends RouteController implements
        IStatusRequest,
        IMediaChannelHelper {
    public final FlingDevice mFlingDevice;
    public FlingDeviceController mFlingDeviceController;
    double c;
    String d;
    public MediaRouteSession mMediaRouteSession;
    public int f;
    public String mSessionId;
    boolean h;
    public boolean i;
    JGCastService mJGCastService;
    public final FlingMediaRouteProvider mFlingMediaRouteProvider;
    private String mApplicationId;
    private boolean isRelaunchApp;
    private boolean n;
    private RemotePlaybackRequest o;
    private RemotePlaybackRequest p;
    private RemotePlaybackRequest mSyncStatusRequest;
    private PendingIntent mPendingIntent;
    private MediaControlChannel mMediaControlChannel;
    private long mLoadRequestId;
    private boolean u;
    private MirroringControlChannel mMirroringControlChannel;
    private final List w = new LinkedList();
    private TrackedItem mTrackedItem;

    public FlingRouteController(FlingMediaRouteProvider awb1, FlingDevice flingdevice) {
        super();
        mFlingMediaRouteProvider = awb1;
        mApplicationId = (String) FlingMediaRouteProvider.i.b();
        isRelaunchApp = true;
        mFlingDevice = flingdevice;
        c = 0.0D;
        f = 0;
        mLoadRequestId = -1L;
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
                Bundle httpHeaders = FlingMediaManagerHelper.getBundle(jsonobject
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
        long l1 = mFlingDeviceController.h();
        Bundle bundle = intent.getExtras();
        if (bundle.containsKey("tv.matchstick.fling.EXTRA_DEBUG_LOGGING_ENABLED")) {
            boolean flag = bundle
                    .getBoolean("tv.matchstick.fling.EXTRA_DEBUG_LOGGING_ENABLED");
            if (flag)
                l1 |= 1L;
            else
                l1 &= -2L;
            FlingMediaRouteProvider.getLogs_a().setDebugEnabled(flag);
        }
        mFlingDeviceController.setDebugLevel(l1);
    }

    private void a(TrackedItem aws1) {
        if (mTrackedItem == aws1)
            mTrackedItem = null;
        w.remove(aws1);
    }

    private void sendPlaybackStateForItem(TrackedItem item, int playbackState, Bundle bundle) {
        FlingMediaRouteProvider.getLogs_a().d("sendPlaybackStateForItem for item: %s, playbackState: %d", item, playbackState);
        if (item.mPendingIntent == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("android.media.intent.extra.ITEM_ID", item.mItemId);
        MediaItemStatusHelper nr1 = (new MediaItemStatusHelper(playbackState)).a(SystemClock
                .uptimeMillis());
        if (bundle != null)
            nr1.a(bundle);
        intent.putExtra("android.media.intent.extra.ITEM_STATUS",
                nr1.createMediaItemStatus().mBundle);
        try {
            item.mPendingIntent.send(
                    ((MediaRouteProvider) (mFlingMediaRouteProvider)).mContext, 0, intent);
            return;
        } catch (android.app.PendingIntent.CanceledException canceledexception) {
            FlingMediaRouteProvider.getLogs_a().w(canceledexception,
                    "exception while sending PendingIntent");
        }
    }

    private boolean processRemotePlaybackRequest(RemotePlaybackRequest awt1) {
        String albumTitle;
        Integer discNumber;
        Integer trackNumber;
        FlingMediaRouteProvider.getLogs_a().d("processRemotePlaybackRequest()", new Object[0]);
        Intent intent = awt1.mIntent;
        String action = intent.getAction();
        Bundle bundle = intent.getBundleExtra("tv.matchstick.fling.EXTRA_CUSTOM_DATA");
        JSONObject jsonobject;
        IllegalStateException illegalstateexception;
        LOG avu1;
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
        LOG avu2;
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
        LOG avu3;
        Object aobj2[];
        Bundle bundle6;
        JSONException jsonexception;
        JSONObject jsonobject2;
        int year;
        Calendar calendar;
        String s11;
        if (bundle != null)
            jsonobject = FlingMediaManagerHelper.getJsonObject(bundle, null);
        else
            jsonobject = null;
        FlingMediaRouteProvider.getLogs_a().d("got remote playback request; action=%s",action);
        try {
            if (!action.equals("android.media.intent.action.PLAY") || intent.getData() == null) {
                if (action.equals("android.media.intent.action.PAUSE")) {
                    flag3 = checkSession(awt1, 0);
                    if (!flag3)
                        return true;
                    try {
                        mMediaControlChannel.pause(jsonobject);
                        // } catch (IOException ioexception4) {
                    } catch (Exception ioexception4) {
                        FlingMediaRouteProvider.getLogs_a().w(ioexception4,
                                "exception while processing %s", action);
                        awt1.onRouteCtrlRequestFailed(1);
                    }
                    return true;
                }

                if (action.equals("android.media.intent.action.RESUME")) {
                    flag2 = checkSession(awt1, 0);
                    if (!flag2)
                        return true;
                    try {
                        mMediaControlChannel.play(jsonobject);
                        // } catch (IOException ioexception3) {
                    } catch (Exception ioexception3) {
                        FlingMediaRouteProvider.getLogs_a().w(ioexception3,
                                "exception while processing %s", action);
                        awt1.onRouteCtrlRequestFailed(1);
                    }
                    return true;
                }

                if (action.equals("android.media.intent.action.STOP")) {
                    flag1 = checkSession(awt1, 0);
                    if (!flag1)
                        return true;
                    try {
                        mMediaControlChannel.stop(jsonobject);
                        // } catch (IOException ioexception2) {
                    } catch (Exception ioexception2) {
                        FlingMediaRouteProvider.getLogs_a().w(ioexception2,
                                "exception while processing %s", action);
                        awt1.onRouteCtrlRequestFailed(1);
                    }
                    return true;
                }

                if (action.equals("android.media.intent.action.SEEK")) {
                    if (!checkSession(awt1, 0))
                        return true;
                    d(intent.getStringExtra("android.media.intent.extra.ITEM_ID"));
                    itemPosition = intent.getLongExtra("android.media.intent.extra.ITEM_POSITION",
                            0L);
                    try {
                        FlingMediaRouteProvider.getLogs_a().d("seeking to %d ms", itemPosition);
                        mMediaControlChannel.seekTime(this, itemPosition, jsonobject);
                        // } catch (IOException ioexception1) {
                    } catch (Exception ioexception1) {
                        FlingMediaRouteProvider.getLogs_a().w(ioexception1,
                                "exception while processing %s", action);
                        awt1.onRouteCtrlRequestFailed(1);
                    }
                    return true;
                }

                if (action.equals("android.media.intent.action.GET_STATUS")) {
                    if (!checkSession(awt1, 0))
                        return true;
                    d(intent.getStringExtra("android.media.intent.extra.ITEM_ID"));
                    if (mMediaControlChannel == null) {
                        awt1.onRouteCtrlRequestFailed(2);
                        return true;
                    }
                    bundle3 = new Bundle();
                    bundle3.putParcelable("android.media.intent.extra.ITEM_STATUS",
                            getItemStatusBundle());
                    bundle3.putParcelable("android.media.intent.extra.SESSION_STATUS",
                            createSessionStatusBundle(0));
                    awt1.onRouteCtrlRequestOk(bundle3);
                    return true;
                }

                if (action.equals("tv.matchstick.fling.ACTION_SYNC_STATUS")) {
                    if (!checkSession(awt1, 0))
                        return true;
                    avv1 = mMediaControlChannel;
                    if (avv1 == null) {
                        awt1.onRouteCtrlRequestFailed(2);
                        return true;
                    }
                    try {
                        if (mLoadRequestId == -1L)
                            mLoadRequestId = mMediaControlChannel.getStatus(this);
                        mSyncStatusRequest = awt1;
                        // } catch (IOException ioexception) {
                    } catch (Exception ioexception) {
                        mSyncStatusRequest = null;
                        FlingMediaRouteProvider.getLogs_a().w(ioexception,
                                "exception while processing %s", action);
                        awt1.onRouteCtrlRequestFailed(1);
                    }
                    return true;
                }

                if (!action.equals("android.media.intent.action.START_SESSION")) {
                    if (action.equals("android.media.intent.action.GET_SESSION_STATUS")) {
                        checkSession(awt1, 0);
                        bundle2 = new Bundle();
                        bundle2.putParcelable("android.media.intent.extra.SESSION_STATUS",
                                createSessionStatusBundle(0));
                        awt1.onRouteCtrlRequestOk(bundle2);
                        return true;
                    }
                    if (action.equals("android.media.intent.action.END_SESSION")) {
                        checkSession(awt1, 0);
                        sendPendingIntent(getSessionId(), 1);
                        mPendingIntent = null;
                        endSession();
                        bundle1 = new Bundle();
                        bundle1.putParcelable("android.media.intent.extra.SESSION_STATUS",
                                createSessionStatusBundle(1));
                        awt1.onRouteCtrlRequestOk(bundle1);
                        return true;
                    }
                    return false;

                }
                appId = intent
                        .getStringExtra("tv.matchstick.fling.EXTRA_FLING_APPLICATION_ID");
                if (!TextUtils.isEmpty(appId)) {
                    s3 = appId;

                } else {
                    s3 = (String) FlingMediaRouteProvider.i.b();
                }
                flag = intent.getBooleanExtra(
                        "tv.matchstick.fling.EXTRA_FLING_RELAUNCH_APPLICATION", true);
                n = intent
                        .getBooleanExtra(
                                "tv.matchstick.fling.EXTRA_FLING_STOP_APPLICATION_WHEN_SESSION_ENDS",
                                false);
                pendingintent = (PendingIntent) intent
                        .getParcelableExtra("android.media.intent.extra.SESSION_STATUS_UPDATE_RECEIVER");
                if (pendingintent == null) {
                    FlingMediaRouteProvider.getLogs_a().d(
                            "No status update receiver supplied to %s",
                            new Object[] {
                                action
                            });
                    return false;
                }
                a(intent);
                mPendingIntent = pendingintent;
                mApplicationId = s3;
                isRelaunchApp = flag;
                p = awt1;
                startSession(0);
                return true;

            }
            if (intent.getStringExtra("android.media.intent.extra.SESSION_ID") == null) {
                s11 = intent
                        .getStringExtra("tv.matchstick.fling.EXTRA_FLING_APPLICATION_ID");
                if (TextUtils.isEmpty(s11))
                    s11 = (String) FlingMediaRouteProvider.i.b();
                mApplicationId = s11;
            }
            if (!checkSession(awt1, 1))
                return true;
            uri = intent.getData();
            if (uri == null)
                return false;
            FlingMediaRouteProvider.getLogs_a().d("Device received play request, uri %s", uri);
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
                        aub1.putString("tv.matchstick.fling.metadata.ALBUM_TITLE",
                                albumTitle);
                    if (albumArtist != null)
                        aub1.putString("tv.matchstick.fling.metadata.ALBUM_ARTIST",
                                albumArtist);
                    if (composer != null)
                        aub1.putString("tv.matchstick.fling.metadata.COMPOSER", composer);
                    if (discNumber != null)
                        aub1.a("tv.matchstick.fling.metadata.DISC_NUMBER",
                                discNumber.intValue());
                    if (trackNumber != null)
                        aub1.a("tv.matchstick.fling.metadata.TRACK_NUMBER",
                                trackNumber.intValue());
                }
                title = bundle4.getString("android.media.metadata.TITLE");
                if (title != null)
                    aub1.putString("tv.matchstick.fling.metadata.TITLE", title);
                artist = bundle4.getString("android.media.metadata.ARTIST");
                if (artist != null)
                    aub1.putString("tv.matchstick.fling.metadata.ARTIST", artist);
                if (bundle4.containsKey("android.media.metadata.YEAR")) {
                    year = bundle4.getInt("android.media.metadata.YEAR");
                    calendar = Calendar.getInstance();
                    calendar.set(1, year);
                    aub1.a("tv.matchstick.fling.metadata.RELEASE_DATE", calendar);
                }
                if (bundle4.containsKey("android.media.metadata.ARTWORK_URI")) {
                    artworkUri = bundle4.getString("android.media.metadata.ARTWORK_URI");
                    if (!TextUtils.isEmpty(artworkUri))
                        aub1.addImage(new WebImage(Uri.parse(artworkUri)));
                }

            }
            aua1 = new MediaInfoContainer(uri.toString());
            aua1.mMediaInfo.mStreamType = 1;
            contentType = intent.getType();
            atz1 = aua1.mMediaInfo;
            if (!TextUtils.isEmpty(contentType)) {
                atz1.mContentType = contentType;
                aua1.mMediaInfo.mMediaMetadata = aub1;
                atz2 = aua1.mMediaInfo;
                if (TextUtils.isEmpty(atz2.mContentId))
                    throw new IllegalArgumentException("content ID cannot be null or empty");
                if (TextUtils.isEmpty(atz2.mContentType))
                    throw new IllegalArgumentException("content type cannot be null or empty");
                if (atz2.mStreamType == -1)
                    throw new IllegalArgumentException("a valid stream type must be specified");
                atz3 = aua1.mMediaInfo;
                bundle5 = intent.getBundleExtra("android.media.intent.extra.HTTP_HEADERS");
                if (bundle5 == null) {
                    jsonobject1 = jsonobject;

                } else {
                    try {
                        jsonobject2 = FlingMediaManagerHelper.getJsonObject(bundle5, null);
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
                    aws1 = new TrackedItem(this, mMediaControlChannel.load(this, atz3, pos,
                            jsonobject1));
                    aws1.mPendingIntent = pendingintent1;
                    w.add(aws1);

                    FlingMediaRouteProvider.getLogs_a().d("loading media with item id assigned as %s", aws1.mItemId);
                    bundle6 = new Bundle();
                    bundle6.putString("android.media.intent.extra.SESSION_ID", getSessionId());
                    bundle6.putParcelable("android.media.intent.extra.SESSION_STATUS",
                            createSessionStatusBundle(0));
                    bundle6.putString("android.media.intent.extra.ITEM_ID", aws1.mItemId);
                    bundle6.putBundle("android.media.intent.extra.ITEM_STATUS",
                            (new MediaItemStatusHelper(3)).a(SystemClock.uptimeMillis())
                                    .createMediaItemStatus().mBundle);
                    awt1.onRouteCtrlRequestOk(bundle6);
                    // } catch (IOException ioexception5) {
                } catch (Exception ioexception5) {
                    FlingMediaRouteProvider.getLogs_a().w(ioexception5,
                            "exception while processing %s",action);
                    awt1.onRouteCtrlRequestFailed(1);
                }
                return true;

            }
            throw new IllegalArgumentException("content type cannot be null or empty");
        } catch (IllegalStateException e) {
            FlingMediaRouteProvider.getLogs_a().d("can't process command; %s", e.getMessage());
            return false;
        }
    }

    private boolean checkSession(RemotePlaybackRequest awt1, int i1) {
        String sessionId = awt1.mIntent.getStringExtra("android.media.intent.extra.SESSION_ID");
        String currentSessionId = getSessionId();
        FlingMediaRouteProvider.getLogs_a().d(
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
                if (mFlingDeviceController.isConnected()) {
                    startSession(0);
                } else {
                    f = 2;
                    mSessionId = null;
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
                if (mFlingDeviceController.isConnected()) {
                    resumeSession(sessionId);
                } else {
                    f = 2;
                    mSessionId = sessionId;
                }
                return false;
            }
        }
        awt1.onRouteCtrlRequestFailed(2);
        return false;
    }

    private void resumeSession(String sessionId) {
        FlingMediaRouteProvider.getLogs_a().d("resumeSession()", new Object[0]);
        mMediaRouteSession.joinApplication(mApplicationId, sessionId);
    }

    private void d(String itemId) {
        if (mTrackedItem == null)
            throw new IllegalStateException("no current item");
        if (!mTrackedItem.mItemId.equals(itemId))
            throw new IllegalStateException("item ID does not match current item");
        else
            return;
    }

    private void startSession(int i1) {
        FlingMediaRouteProvider.getLogs_a().d("startSession()");
        
        if (i1 == 1) {
            MediaRouteSession axe1 = mMediaRouteSession;
            int j1 = ((WifiManager) ((MediaRouteProvider) (mFlingMediaRouteProvider)).mContext
                    .getSystemService("wifi"))
                    .getConnectionInfo().getIpAddress();
            axe1.startSession("674A0243",
                    (new StringBuilder()).append(j1 & 0xff).append(".").append(0xff & j1 >> 8)
                            .append(".").append(0xff & j1 >> 16).append(".")
                            .append(0xff & j1 >> 24).toString(), true);
            return;
        } else {
            mMediaRouteSession.startSession(mApplicationId, null, isRelaunchApp);
            return;
        }
    }

    private void f(int i1) {
        for (Iterator iterator = w.iterator(); iterator.hasNext(); sendPlaybackStateForItem(
                (TrackedItem) iterator.next(), i1, ((Bundle) (null))))
            ;
        w.clear();
        mTrackedItem = null;
    }

    private static final class MediaSessionStatus {

        public final Bundle mData;

        private MediaSessionStatus(Bundle bundle) {
            mData = bundle;
        }

        public MediaSessionStatus(Bundle bundle, byte byte0) {
            this(bundle);
        }

        public final String toString() {
            StringBuilder stringbuilder;
            StringBuilder stringbuilder1;
            int i;
            stringbuilder = new StringBuilder();
            stringbuilder.append("MediaSessionStatus{ ");
            stringbuilder.append("timestamp=");
            C_dt.a(SystemClock.elapsedRealtime() - mData.getLong("timestamp"), stringbuilder);
            stringbuilder.append(" ms ago");
            stringbuilder1 = stringbuilder.append(", sessionState=");
            i = mData.getInt("sessionState", 2);
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
            stringbuilder.append(", queuePaused=").append(mData.getBoolean("queuePaused"));
            stringbuilder.append(", extras=").append(mData.getBundle("extras"));
            stringbuilder.append(" }");
            return stringbuilder.toString();
        }
    }

    private static class SessionStatusBundle {

        public final Bundle data = new Bundle();

        public SessionStatusBundle(int sessionState) {
            setTimestamp(SystemClock.elapsedRealtime());
            data.putInt("sessionState", sessionState);
        }

        public final SessionStatusBundle setTimestamp(long timestamp) {
            data.putLong("timestamp", timestamp);
            return this;
        }
    }

    private Bundle createSessionStatusBundle(int sessionState) {
        SessionStatusBundle oo1 = new SessionStatusBundle(sessionState);
        MediaStatus aud1;

        boolean queuePaused;
        if (mMediaControlChannel != null
                && (aud1 = mMediaControlChannel.getMediaStatus()) != null) {
            if (aud1.mPlayerState == 3) // queue paused?
                queuePaused = true;
            else
                queuePaused = false;
        } else {
            queuePaused = false;
        }
        oo1.data.putBoolean("queuePaused", queuePaused);
        return (new MediaSessionStatus(oo1.setTimestamp(SystemClock.uptimeMillis()).data,
                (byte) 0)).mData;
    }

    private Bundle getItemStatusBundle() {
        byte byte0;
        MediaStatus aud1;
        byte0 = 5;
        aud1 = mMediaControlChannel.getMediaStatus();
        if (aud1 != null) {
            int i1;
            int j1;
            i1 = aud1.mPlayerState;
            j1 = aud1.mIdleReason;
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
            long contentDuration = mMediaControlChannel.getContentDuration();
            nr1.mData.putLong("contentDuration", contentDuration);
            long l2 = mMediaControlChannel.a();
            nr1.mData.putLong("contentPosition", l2);
            MediaItemStatusHelper nr2 = nr1.a(SystemClock.uptimeMillis());
            Bundle bundle = a(aud1.mCustomData);
            if (bundle != null)
                nr2.a(bundle);
            return nr2.createMediaItemStatus().mBundle;
        }
        FlingMediaRouteProvider.getLogs_a().d("*** media status is null!");
        return (new MediaItemStatusHelper(byte0)).createMediaItemStatus().mBundle;
    }

    public final void onRelease() {
        FlingMediaRouteProvider.getLogs_a().d("Controller released", new Object[0]);
    }

    public final void onSetVolume(int volume) {
        FlingMediaRouteProvider.getLogs_a().d("onSetVolume() volume=%d", volume);
        if (mFlingDeviceController == null) {
            return;
        }
        double d1 = (double) volume / 20D;
        try {
            mFlingDeviceController.setVolume(d1, c, false);
        } catch (IllegalStateException e) {
            FlingMediaRouteProvider.getLogs_a().d("Unable to set volume: %s", e.getMessage());
        }
    }

    public final void requestStatus(long loadRequestId, int status, JSONObject jsonobject) {
        Iterator iterator = w.iterator();
        TrackedItem aws1;
        do {
            if (!iterator.hasNext()) {
                aws1 = null;
                break;
            }
            aws1 = (TrackedItem) iterator.next();
        } while (aws1.mLoadRequestId != loadRequestId);
        if (aws1 == null) {
            if (loadRequestId != mLoadRequestId) {
                return;
            }
            FlingMediaRouteProvider.getLogs_a().d("requestStatus has completed");
            mLoadRequestId = -1L;
            long sessionId;
            Iterator iterator1;
            try {
                sessionId = mMediaControlChannel.getMediaSessionId();
                iterator1 = w.iterator();
                TrackedItem aws2;
                do {
                    if (!iterator1.hasNext()) {
                        aws2 = null;
                        break;
                    }
                    aws2 = (TrackedItem) iterator1.next();
                } while (aws2.mMediaSessionId != sessionId);
                if (mTrackedItem != null && mTrackedItem != aws2) {
                    sendPlaybackStateForItem(mTrackedItem, 4, ((Bundle) (null)));
                    a(mTrackedItem);
                }
                if (mSyncStatusRequest != null) {
                    TrackedItem aws3 = new TrackedItem(this);
                    aws3.mMediaSessionId = sessionId;
                    aws3.mPendingIntent = (PendingIntent) mSyncStatusRequest.mIntent
                            .getParcelableExtra("android.media.intent.extra.ITEM_STATUS_UPDATE_RECEIVER");
                    w.add(aws3);
                    mTrackedItem = aws3;
                }
                Iterator iterator2 = w.iterator();
                while (iterator2.hasNext()) {
                    TrackedItem aws4 = (TrackedItem) iterator2.next();
                    if (aws4.mMediaSessionId != -1L
                            && (mTrackedItem == null || aws4.mMediaSessionId < mTrackedItem.mMediaSessionId)) {
                        sendPlaybackStateForItem(aws4, 4, ((Bundle) (null)));
                        iterator2.remove();
                    }
                }
            } catch (IllegalStateException illegalstateexception) {
                f(4);
                mTrackedItem = null;
            }
            FlingMediaRouteProvider.getLogs_a().d("mSyncStatusRequest = %s, status=%d", status);
            if (mSyncStatusRequest != null) {
                if (status == 0) {
                    FlingMediaRouteProvider.getLogs_a().d(
                            "requestStatus completed; sending response");
                    Bundle bundle = new Bundle();
                    if (mTrackedItem != null) {
                        MediaStatus aud1 = mMediaControlChannel.getMediaStatus();
                        bundle.putString("android.media.intent.extra.ITEM_ID",
                                mTrackedItem.mItemId);
                        bundle.putParcelable("android.media.intent.extra.ITEM_STATUS",
                                getItemStatusBundle());
                        MediaInfo atz1 = aud1.mMedia;
                        if (atz1 != null) {
                            Bundle bundle1 = FlingMediaManagerHelper
                                    .createMetadataBundle(atz1);
                            FlingMediaRouteProvider.getLogs_a().d("adding metadata bundle: %s",
                                    new Object[] {
                                        bundle1
                                    });
                            bundle.putParcelable("android.media.intent.extra.ITEM_METADATA",
                                    bundle1);
                        }
                    }
                    mSyncStatusRequest.onRouteCtrlRequestOk(bundle);
                } else {
                    mSyncStatusRequest.onRouteCtrlRequestFailed(1);
                }
                mSyncStatusRequest = null;
            }

            return;
        }
        long mediaSessionId = mMediaControlChannel.getMediaSessionId();
        switch (status) {
            default:
            case 1:
                FlingMediaRouteProvider.getLogs_a().d("unknown status %d; sending error state", status);
                sendPlaybackStateForItem(aws1, 7, a(jsonobject));
                a(aws1);
                break;
            case 0:
                FlingMediaRouteProvider.getLogs_a().d("Load completed; mediaSessionId=%d", mediaSessionId);
                aws1.mLoadRequestId = -1L;
                aws1.mMediaSessionId = mediaSessionId;
                mTrackedItem = aws1;
                sendItemStatusUpdate();
                break;
            case 2:
                FlingMediaRouteProvider.getLogs_a().d("STATUS_CANCELED; sending error state");
                sendPlaybackStateForItem(aws1, 5, ((Bundle) (null)));
                a(aws1);
                break;
            case 3:
                FlingMediaRouteProvider.getLogs_a().d("STATUS_TIMED_OUT; sending error state");
                sendPlaybackStateForItem(aws1, 7, ((Bundle) (null)));
                a(aws1);
                break;
        }
    }

    public final void attachMediaChannel(String sessionId)
    {
        ApplicationMetadata applicationmetadata;
        applicationmetadata = mMediaRouteSession.getApplicationMetadata();
        if (p != null) {
            Bundle bundle = new Bundle();
            bundle.putString("android.media.intent.extra.SESSION_ID", sessionId);
            p.onRouteCtrlRequestOk(bundle);
            p = null;
        }
        sendPendingIntent(sessionId, 0);
        if (mApplicationId.equals(applicationmetadata.getApplicationId())) {
            FlingMediaRouteProvider.getLogs_a().d("attachMediaChannel", new Object[0]);

            // mMediaControlChannel_s = new C_awo(this);
            mMediaControlChannel = new MediaControlChannel() {
                protected final void onStatusUpdated() {
                    FlingMediaRouteProvider.getLogs_a().d("onStatusUpdated");
                    sendItemStatusUpdate();
                }

                protected final void sendItemStatusUpdate_i() {
                    sendItemStatusUpdate();
                }
            };

            mFlingDeviceController.a(mMediaControlChannel);
            if (o != null) {
                processRemotePlaybackRequest(o);
                o = null;
            }
        } else {
            if ("674A0243".equals(applicationmetadata.getApplicationId())) {
                u = true;

                mMirroringControlChannel = new MirroringControlChannel(mFlingDevice,
                        mFlingDeviceController.getTransId()) {
                    protected final void onAnswer(final String s) {
                        FlingMediaRouteProvider.getLogs_a().d("onAnswer");

                        ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler
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
                    mFlingDeviceController.a(mMirroringControlChannel);
                    mMirroringControlChannel.a();
                } catch (Exception ioexception) {
                    FlingMediaRouteProvider.getLogs_a().w(ioexception, "Failed to send offer");
                }
            }
        }
        if (mLoadRequestId != -1L || mMediaControlChannel == null)
            return;
        try {
            mLoadRequestId = mMediaControlChannel.getStatus(this);
        } catch (Exception ioexception1) {
            FlingMediaRouteProvider.getLogs_a().w(ioexception1,
                    "Exception while requesting media status");

        }
    }

    final void sendPendingIntent(String sessionId, int sessionStatus) {
        if (sessionId == null || mPendingIntent == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("android.media.intent.extra.SESSION_ID", sessionId);
        intent.putExtra("android.media.intent.extra.SESSION_STATUS",
                createSessionStatusBundle(sessionStatus));
        try {
            FlingMediaRouteProvider.getLogs_a().d(
                    "Invoking session status PendingIntent with: %s",intent);
            mPendingIntent.send(((MediaRouteProvider) (mFlingMediaRouteProvider)).mContext,
                    0, intent);
            return;
        } catch (android.app.PendingIntent.CanceledException canceledexception) {
            FlingMediaRouteProvider.getLogs_a().w(canceledexception,
                    "exception while sending PendingIntent");
        }
    }

    final void a(boolean flag) {
        if (FlingMediaRouteProvider.f(mFlingMediaRouteProvider) != null)
            FlingMediaRouteProvider.f(mFlingMediaRouteProvider).a(mFlingDevice, flag);
    }

    public final boolean onControlRequest(Intent intent, RouteCtrlRequestCallback om) {
        boolean flag;
        FlingMediaRouteProvider.getLogs_a().d("Received control request %s", intent);
        RemotePlaybackRequest awt1 = new RemotePlaybackRequest(mFlingMediaRouteProvider,
                intent, om);
        if (!intent.hasCategory("android.media.intent.category.REMOTE_PLAYBACK")) {
            boolean flag1 = intent
                    .hasCategory("tv.matchstick.fling.CATEGORY_FLING_REMOTE_PLAYBACK");
            flag = false;
            if (!flag1)
                return flag;
        }
        flag = processRemotePlaybackRequest(awt1);
        return flag;
    }

    public final void onSelect() {
        FlingMediaRouteProvider.getLogs_a().d("onSelect");
        mFlingDeviceController = FlingMediaRouteProvider.createDeviceController(
                mFlingMediaRouteProvider, this);
        mMediaRouteSession = new MediaRouteSession(mFlingDeviceController, this,
                ((MediaRouteProvider) (mFlingMediaRouteProvider)).mHandler);
    }

    public final void onUpdateVolume(int delta) {
        FlingMediaRouteProvider.getLogs_a().d("onUpdateVolume() delta=%d", delta);
        if (mFlingDeviceController == null)
            return;
        try {
            double d1 = c + (double) delta / 20D;
            mFlingDeviceController.setVolume(d1, c, false);
            return;
        } catch (IllegalStateException illegalstateexception) {
            FlingMediaRouteProvider.getLogs_a().d("Unable to update volume: %s", illegalstateexception.getMessage());
            return;
        }
    }

    public final void sendPendingIntent(String sessionId) {
        if (p != null) {
            p.onRouteCtrlRequestFailed(2);
            p = null;
        }
        sendPendingIntent(sessionId, 1);
    }

    public final void disconnectCastMirror(boolean flag) {
        if (i) {
            if (mJGCastService != null) {
                FlingMediaRouteProvider.getLogs_a().d("Destroying mirroring client",
                        new Object[0]);
                mJGCastService.disconnect();
                mJGCastService = null;
            }
            if (mMirroringControlChannel != null) {
                if (mFlingDeviceController != null)
                    mFlingDeviceController.b(mMirroringControlChannel);
                mMirroringControlChannel = null;
            }
            a(flag);
            i = false;
        }
    }

    public final void onUnselect() {
        FlingMediaRouteProvider.getLogs_a().d("onUnselect");
        endSession();
        FlingMediaRouteProvider.b(mFlingMediaRouteProvider, this);
        mFlingDeviceController = null;
    }

    public final void onApplicationDisconnected(int statusCode) {
        FlingMediaRouteProvider.getLogs_a().d("onApplicationDisconnected: statusCode=%d", statusCode);
        boolean flag = false;
        if (statusCode != 0)
            flag = true;
        disconnectCastMirror(flag);
        if (mMediaRouteSession != null) {
            mMediaRouteSession.onApplicationDisconnected(statusCode);
            sendPendingIntent(getSessionId(), 1);
        }
    }

    public final void endSession() {
        FlingMediaRouteProvider.getLogs_a().d("endSession() voluntary=%b", true);
        if (mMediaRouteSession != null) {
            mMediaRouteSession.stopSession(u | n);
            u = false;
            n = false;
        }
    }

    public final void detachMediaChannel(int i1) {
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
            FlingMediaRouteProvider.getLogs_a().d("detachMediaChannel");
            if (mMediaControlChannel != null) {
                if (mFlingDeviceController != null)
                    mFlingDeviceController.b(mMediaControlChannel);
                mMediaControlChannel = null;
            }
        }
        p = null;
    }

    final String getSessionId() {
        if (mMediaRouteSession == null)
            return null;
        else
            return mMediaRouteSession.getSessionId();
    }

    public final void startSession() {
        switch (f) {
            case 1:
                FlingMediaRouteProvider.getLogs_a().d("starting pending session for mirroring");
                startSession(1);
                break;
            case 2:
                FlingMediaRouteProvider.getLogs_a().d("starting pending session for media with session ID %s", mSessionId);
                if (mSessionId != null) {
                    resumeSession(mSessionId);
                    mSessionId = null;
                } else {
                    startSession(0);
                }
                break;
        }
        f = 0;
        return;
    }

    public final void g() {
        disconnectCastMirror(true);
        FlingMediaRouteProvider.b(mFlingMediaRouteProvider, this);
    }

    final void sendItemStatusUpdate() {
        FlingMediaRouteProvider.getLogs_a().d("sendItemStatusUpdate(); current item is %s", mTrackedItem);
        if (mTrackedItem != null) {
            PendingIntent pendingintent = mTrackedItem.mPendingIntent;
            if (pendingintent != null) {
                FlingMediaRouteProvider.getLogs_a().d("found a PendingIntent for item %s", mTrackedItem);
                Intent intent = new Intent();
                intent.putExtra("android.media.intent.extra.ITEM_ID", mTrackedItem.mItemId);
                intent.putExtra("android.media.intent.extra.ITEM_STATUS", getItemStatusBundle());
                MediaInfo atz1 = mMediaControlChannel.getMediaInfo();
                if (atz1 != null) {
                    Bundle bundle = FlingMediaManagerHelper.createMetadataBundle(atz1);
                    FlingMediaRouteProvider.getLogs_a().d("adding metadata bundle: %s", bundle.toString());
                    intent.putExtra("android.media.intent.extra.ITEM_METADATA", bundle);
                }
                try {
                    FlingMediaRouteProvider.getLogs_a().d(
                            "Invoking item status PendingIntent with: %s",intent);
                    pendingintent.send(
                            ((MediaRouteProvider) (mFlingMediaRouteProvider)).mContext, 0,
                            intent);
                } catch (android.app.PendingIntent.CanceledException canceledexception) {
                    FlingMediaRouteProvider.getLogs_a().d(canceledexception,
                            "exception while sending PendingIntent", new Object[0]);
                }
            }

            if (mMediaControlChannel.getMediaStatus().mPlayerState == 1) {
                FlingMediaRouteProvider.getLogs_a().d("player state is now IDLE; removing tracked item %s", mTrackedItem);
                a(mTrackedItem);
            }
        }
    }
}
