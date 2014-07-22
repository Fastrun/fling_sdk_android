
package tv.matchstick.server.fling;

import android.os.Bundle;

public final class DiscoveryRequest { // MediaRouteDiscoveryRequest
    private final Bundle mBundle;
    private MediaRouteSelector mMediaRouteSelector;

    DiscoveryRequest(Bundle bundle) {
        mBundle = bundle;
    }

    public DiscoveryRequest(MediaRouteSelector selector,
            boolean activeScan) {
        if (selector == null) {
            throw new IllegalArgumentException("selector must not be null");
        } else {
            mBundle = new Bundle();
            mMediaRouteSelector = selector;
            mBundle.putBundle("selector", selector.asBundle());
            mBundle.putBoolean("activeScan", activeScan);
            return;
        }
    }

    private void ensureSelector() {
        if (mMediaRouteSelector == null) {
            mMediaRouteSelector = MediaRouteSelector
                    .fromBundle(mBundle.getBundle("selector"));
            if (mMediaRouteSelector == null)
                mMediaRouteSelector = MediaRouteSelector.EMPTY;
        }
    }

    public final MediaRouteSelector getSelector() {
        ensureSelector();
        return mMediaRouteSelector;
    }

    public final boolean isActiveScan() {
        return mBundle.getBoolean("activeScan");
    }

    public final boolean isValid() {
        ensureSelector();
        return mMediaRouteSelector.isValid();
    }

    public final boolean equals(Object o) {
        if (o instanceof DiscoveryRequest) {
            DiscoveryRequest other = (DiscoveryRequest) o;
            return getSelector().equals(other.getSelector())
                    && isActiveScan() == other.isActiveScan();
        }

        return false;
    }

    public final int hashCode() {
        return getSelector().hashCode() ^ (isActiveScan() ? 1 : 0);
    }

    public final String toString() {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("DiscoveryRequest{ selector=").append(
                getSelector());
        stringbuilder.append(", activeScan=").append(isActiveScan());
        stringbuilder.append(", isValid=").append(isValid());
        stringbuilder.append(" }");
        return stringbuilder.toString();
    }
}
