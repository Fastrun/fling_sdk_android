
package com.fireflycast.server.cast.service.operation;

import com.fireflycast.server.cast.mdns.DeviceScanner;

public abstract class CastDeviceScannerOperation {
    public static String a = "CastDeviceScannerOperation";
    protected final DeviceScanner mDeviceScanner_b;

    public CastDeviceScannerOperation(DeviceScanner aur)
    {
        mDeviceScanner_b = aur;
    }

    public abstract void act_a();
}
