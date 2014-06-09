
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;

public abstract class FireflySrvBinder extends IFireflyServiceBrokerStub {
    public void initCastService_a(IFireflyCallbacks bgj, int i1, String s, IBinder ibinder,
            Bundle bundle)
    {
        throw new IllegalArgumentException("Cast service not supported");
    }
}
