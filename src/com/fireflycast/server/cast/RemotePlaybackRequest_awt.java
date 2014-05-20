
package com.fireflycast.server.cast;

import android.content.Intent;
import android.os.Bundle;

import com.fireflycast.server.cast.media.RouteCtrlRequestCallback_om;

final class RemotePlaybackRequest_awt {
    public Intent mIntent_a;
    public RouteCtrlRequestCallback_om mRouteCtrlRequestCallback_b;
    final CastMediaRouteProvider_awb mCastMediaRouteProvider_c;

    public RemotePlaybackRequest_awt(CastMediaRouteProvider_awb awb1, Intent intent,
            RouteCtrlRequestCallback_om om1)
    {
        super();
        mCastMediaRouteProvider_c = awb1;
        mIntent_a = intent;
        mRouteCtrlRequestCallback_b = om1;
    }

    public final void onRouteCtrlRequestFailed_a(int errorCode)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("com.fireflycast.cast.EXTRA_ERROR_CODE", errorCode);
        mRouteCtrlRequestCallback_b.onError_a(
                (String) CastMediaRouteProvider_awb.g(mCastMediaRouteProvider_c).get(
                        Integer.valueOf(errorCode)), bundle);
    }

    public final void onRouteCtrlRequestOk_a(Bundle bundle)
    {
        CastMediaRouteProvider_awb.getLogs_a().d("result bundle: %s", new Object[] {
                bundle
        });
        mRouteCtrlRequestCallback_b.onResult_a(bundle);
    }
}
