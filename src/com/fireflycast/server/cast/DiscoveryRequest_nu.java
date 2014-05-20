
package com.fireflycast.server.cast;

import android.os.Bundle;

public final class DiscoveryRequest_nu { // MediaRouteDiscoveryRequest
    private final Bundle mBundle_a;
    private MediaRouteSelector_oj mMediaRouteSelector_b;

    DiscoveryRequest_nu(Bundle bundle) {
        mBundle_a = bundle;
    }

    public DiscoveryRequest_nu(MediaRouteSelector_oj selector,
            boolean activeScan) {
        if (selector == null) {
            throw new IllegalArgumentException("selector must not be null");
        } else {
            mBundle_a = new Bundle();
            mMediaRouteSelector_b = selector;
            mBundle_a.putBundle("selector", selector.asBundle_d());
            mBundle_a.putBoolean("activeScan", activeScan);
            return;
        }
    }

    private void ensureSelector_d() {
        if (mMediaRouteSelector_b == null) {
            mMediaRouteSelector_b = MediaRouteSelector_oj
                    .fromBundle_a(mBundle_a.getBundle("selector"));
            if (mMediaRouteSelector_b == null)
                mMediaRouteSelector_b = MediaRouteSelector_oj.EMPTY;
        }
    }

    public final MediaRouteSelector_oj getSelector_a() {
        ensureSelector_d();
        return mMediaRouteSelector_b;
    }

    public final boolean isActiveScan_b() {
        return mBundle_a.getBoolean("activeScan");
    }

    public final boolean isValid_c() {
        ensureSelector_d();
        return mMediaRouteSelector_b.isValid_c();
    }

    public final boolean equals(Object o) {
        if (o instanceof DiscoveryRequest_nu) {
            DiscoveryRequest_nu other = (DiscoveryRequest_nu) o;
            return getSelector_a().equals(other.getSelector_a())
                    && isActiveScan_b() == other.isActiveScan_b();
        }

        return false;
    }

    public final int hashCode() {
        return getSelector_a().hashCode() ^ (isActiveScan_b() ? 1 : 0);
    }

    public final String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("DiscoveryRequest{ selector=").append(
                getSelector_a());
        stringbuilder.append(", activeScan=").append(isActiveScan_b());
        stringbuilder.append(", isValid=").append(isValid_c());
        stringbuilder.append(" }");
        return stringbuilder.toString();
    }
}