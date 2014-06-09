
package com.fireflycast.server.cast.mdns;

import android.os.SystemClock;

import com.fireflycast.cast.CastDevice;

final class ScannerPrivData {
    CastDevice mCastDevice_a;
    long mElapsedRealtime_b;
    long mTTl_c;
    boolean d;
    final MdnsDeviceScanner mMdnsDeviceScanner_e;

    ScannerPrivData(MdnsDeviceScanner ave, CastDevice castdevice, long ttl_l)
    {
        super();
        mMdnsDeviceScanner_e = ave;
        mCastDevice_a = castdevice;
        mTTl_c = ttl_l;
        mElapsedRealtime_b = SystemClock.elapsedRealtime();
    }
}
