
package com.fireflycast.server.cast.media;

import android.content.Intent;

public abstract class RouteController {
    public void onRelease_a()
    {
    }

    public void onSetVolume_a(int i)
    {
    }

    public boolean onControlRequest_a(Intent intent, RouteCtrlRequestCallback callback)
    {
        return false;
    }

    public void onSelect_b()
    {
    }

    public void onUpdateVolume_b(int delta)
    {
    }

    public void onUnselect_c()
    {
    }
}
