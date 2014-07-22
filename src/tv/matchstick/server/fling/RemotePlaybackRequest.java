
package tv.matchstick.server.fling;

import tv.matchstick.server.fling.media.RouteCtrlRequestCallback;
import android.content.Intent;
import android.os.Bundle;

final class RemotePlaybackRequest {
    public Intent mIntent;
    public RouteCtrlRequestCallback mRouteCtrlRequestCallback;
    final FlingMediaRouteProvider mFlingMediaRouteProvider;

    public RemotePlaybackRequest(FlingMediaRouteProvider awb1, Intent intent,
            RouteCtrlRequestCallback om1)
    {
        super();
        mFlingMediaRouteProvider = awb1;
        mIntent = intent;
        mRouteCtrlRequestCallback = om1;
    }

    public final void onRouteCtrlRequestFailed(int errorCode)
    {
        Bundle bundle = new Bundle();
        bundle.putInt("tv.matchstick.fling.EXTRA_ERROR_CODE", errorCode);
        mRouteCtrlRequestCallback.onError(
                (String) FlingMediaRouteProvider.g(mFlingMediaRouteProvider).get(
                        Integer.valueOf(errorCode)), bundle);
    }

    public final void onRouteCtrlRequestOk(Bundle bundle)
    {
        FlingMediaRouteProvider.getLogs_a().d("result bundle: %s", bundle);
        mRouteCtrlRequestCallback.onResult(bundle);
    }
}
