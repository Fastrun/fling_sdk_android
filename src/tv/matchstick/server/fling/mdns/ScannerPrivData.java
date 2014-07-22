
package tv.matchstick.server.fling.mdns;

import tv.matchstick.fling.FlingDevice;
import android.os.SystemClock;

final class ScannerPrivData {
    FlingDevice mFlingDevice;
    long mElapsedRealtime;
    long mTTl;
    boolean d;
    final MdnsDeviceScanner mMdnsDeviceScanner;

    ScannerPrivData(MdnsDeviceScanner ave, FlingDevice device, long ttl)
    {
        super();
        mMdnsDeviceScanner = ave;
        mFlingDevice = device;
        mTTl = ttl;
        mElapsedRealtime = SystemClock.elapsedRealtime();
    }
}
