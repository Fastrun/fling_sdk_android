
package com.fireflycast.server.cast.service.operation;

import com.fireflycast.server.cast.mdns.DeviceScanner_aur;

public abstract class CastDeviceScannerOperation_axz {
    public static String a = "CastDeviceScannerOperation";
    protected final DeviceScanner_aur mDeviceScanner_b;

    public CastDeviceScannerOperation_axz(DeviceScanner_aur aur)
    {
        mDeviceScanner_b = aur;
    }

    public abstract void act_a();
}
