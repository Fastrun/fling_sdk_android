
package com.fireflycast.server.cast.bridge;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class IFireflyServiceBrokerStub extends Binder implements IFireflyServiceBroker {

    public IFireflyServiceBrokerStub()
    {
        attachInterface(this, "com.fireflycast.common.internal.IFireflyServiceBroker");
    }

    public static IFireflyServiceBroker asInterface_a(IBinder ibinder) // asInterface
    {
        if (ibinder == null)
            return null;
        android.os.IInterface iinterface = ibinder
                .queryLocalInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
        if (iinterface != null && (iinterface instanceof IFireflyServiceBroker))
            return (IFireflyServiceBroker) iinterface;
        else
            return new IFireflyServiceBrokerProxy(ibinder); // FireflyServiceBrokerProxy?
    }

    public IBinder asBinder()
    {
        return this;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
            throws RemoteException
    {
        switch (code)
        {
            case 1598968902:
                reply.writeString("com.fireflycast.common.internal.IFireflyServiceBroker");
                return true;

            case 19: // '\023'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks bgj1 = IFireflyCallbacksStub.a(data.readStrongBinder());// IFireflyCallbacks
                int l = data.readInt(); // 4242000
                String s2 = data.readString(); // getContext().getPackageName()
                IBinder ibinder = data.readStrongBinder(); // com.fireflycast.cast.internal.ICastDeviceControllerListener
                Bundle bundle1; // com.fireflycast.cast.EXTRA_CAST_FLAGS,
                                // last_application_id, last_session_id
                if (data.readInt() != 0)
                    bundle1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                else
                    bundle1 = null;
                initCastService_a(bgj1, l, s2, ibinder, bundle1);
                reply.writeNoException();
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
