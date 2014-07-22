
package tv.matchstick.server.fling.service.operation;

import tv.matchstick.server.fling.mdns.DeviceScanner;

public abstract class FlingDeviceScannerOperation {
    public static String a = "FlingDeviceScannerOperation";
    protected final DeviceScanner mDeviceScanner;

    public FlingDeviceScannerOperation(DeviceScanner aur)
    {
        mDeviceScanner = aur;
    }

    public abstract void act();
}
