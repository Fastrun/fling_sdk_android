
package tv.matchstick.server.fling.media;

import android.content.Intent;

public abstract class RouteController {
    public void onRelease()
    {
    }

    public void onSetVolume(int i)
    {
    }

    public boolean onControlRequest(Intent intent, RouteCtrlRequestCallback callback)
    {
        return false;
    }

    public void onSelect()
    {
    }

    public void onUpdateVolume(int delta)
    {
    }

    public void onUnselect()
    {
    }
}
