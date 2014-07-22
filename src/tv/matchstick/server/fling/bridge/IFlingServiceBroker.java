
package tv.matchstick.server.fling.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IFlingServiceBroker extends IInterface {
    public abstract void initFlingService(IFlingCallbacks callbacks, int version, String packageName,
            IBinder listener, Bundle bundle) throws RemoteException;
}
