
package com.fireflycast.server.cast.mdns;

import com.fireflycast.cast.CastDevice;

import java.net.Inet4Address;

public final class CastDeviceHelper {
    public final CastDevice mCastDevice_a;

    public CastDeviceHelper(CastDevice castdevice, String deviceId, Inet4Address inet4address)
    {
        super();
        mCastDevice_a = castdevice;
        CastDevice.setHost_a(castdevice, inet4address);
        String address;
        if (CastDevice.getHost_a(castdevice) != null)
            address = CastDevice.getHost_a(castdevice).getHostAddress();
        else
            address = null;
        castdevice.mHostAddress_a = address;
        CastDevice.setDeviceId_a(castdevice, deviceId);
    }
}
