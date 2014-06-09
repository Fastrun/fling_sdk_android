
package com.fireflycast.server.cast;

import android.content.Intent;
import android.os.Bundle;

import com.fireflycast.server.cast.media.RouteCtrlRequestCallback;

final class RemotePlaybackRequest {
    public Intent mIntent_a;
    public RouteCtrlRequestCallback mRouteCtrlRequestCallback_b;
    final CastMediaRouteProvider mCastMediaRouteProvider_c;

    public RemotePlaybackRequest(CastMediaRouteProvider awb1, Intent intent,
            RouteCtrlRequestCallback om1)
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
                (String) CastMediaRouteProvider.g(mCastMediaRouteProvider_c).get(
                        Integer.valueOf(errorCode)), bundle);
    }

    public final void onRouteCtrlRequestOk_a(Bundle bundle)
    {
        CastMediaRouteProvider.getLogs_a().d("result bundle: %s", new Object[] {
                bundle
        });
        mRouteCtrlRequestCallback_b.onResult_a(bundle);
    }
}
