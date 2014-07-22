
package tv.matchstick.server.fling.mdns;

import tv.matchstick.fling.FlingDevice;

public interface IDeviceScanListener {
    public abstract void onAllDevicesOffline();

    public abstract void onDeviceOnline(FlingDevice device);

    public abstract void onDeviceOffline(FlingDevice device);
}
