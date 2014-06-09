
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IFireflyServiceBroker extends IInterface {
    public abstract void initCastService_a(IFireflyCallbacks bgj, int i1, String s,
            IBinder ibinder, Bundle bundle) throws RemoteException;
}
