
package com.fireflycast.server.cast.mdns;

import com.fireflycast.cast.CastDevice;

public interface IDeviceScanListener_auy {
    public abstract void onAllDevicesOffline_a();

    public abstract void onDeviceOnline_a(CastDevice castdevice);

    public abstract void onDeviceOffline_b(CastDevice castdevice);
}
