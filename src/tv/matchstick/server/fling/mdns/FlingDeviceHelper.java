
package tv.matchstick.server.fling.mdns;

import java.net.Inet4Address;

import tv.matchstick.fling.FlingDevice;

public final class FlingDeviceHelper {
    public final FlingDevice mFlingDevice;

    public FlingDeviceHelper(FlingDevice flingdevice, String deviceId, Inet4Address inet4address)
    {
        super();
        mFlingDevice = flingdevice;
        FlingDevice.setHost(flingdevice, inet4address);
        String address;
        if (FlingDevice.getHost(flingdevice) != null)
            address = FlingDevice.getHost(flingdevice).getHostAddress();
        else
            address = null;
        flingdevice.mHostAddress = address;
        FlingDevice.setDeviceId(flingdevice, deviceId);
    }
}
