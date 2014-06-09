
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IFireflyCallbacks extends IInterface {
    public abstract void onConnect_a(int status, IBinder ibinder, Bundle bundle)
            throws RemoteException;
}
