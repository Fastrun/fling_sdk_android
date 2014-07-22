
package tv.matchstick.server.fling.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IFlingCallbacks extends IInterface {
    public abstract void onConnect(int status, IBinder ibinder, Bundle bundle)
            throws RemoteException;
}
