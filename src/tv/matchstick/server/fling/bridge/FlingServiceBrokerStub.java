
package tv.matchstick.server.fling.bridge;

import android.os.Bundle;
import android.os.IBinder;

public abstract class FlingServiceBrokerStub extends IFlingServiceBrokerStub {
    public void initFlingService(IFlingCallbacks callbacks, int version, String packageName, IBinder listener,
            Bundle bundle)
    {
        throw new IllegalArgumentException("Fling service not supported");
    }
}
