
package tv.matchstick.server.fling.bridge;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class IFlingServiceBrokerStub extends Binder implements IFlingServiceBroker {

    public IFlingServiceBrokerStub()
    {
        attachInterface(this, "tv.matchstick.common.internal.IFlingServiceBroker");
    }

    public static IFlingServiceBroker asInterface(IBinder ibinder) // asInterface
    {
        if (ibinder == null)
            return null;
        android.os.IInterface iinterface = ibinder
                .queryLocalInterface("tv.matchstick.common.internal.IFlingServiceBroker");
        if (iinterface != null && (iinterface instanceof IFlingServiceBroker))
            return (IFlingServiceBroker) iinterface;
        else
            return new IFlingServiceBrokerProxy(ibinder); // FlingServiceBrokerProxy?
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
                reply.writeString("tv.matchstick.common.internal.IFlingServiceBroker");
                return true;

            case 19: // '\023'
                data.enforceInterface("tv.matchstick.common.internal.IFlingServiceBroker");
                IFlingCallbacks callbacks = IFlingCallbacksStub.a(data.readStrongBinder());// IFlingCallbacks
                int version = data.readInt(); // 4242000
                String pakcageName = data.readString(); // getContext().getPackageName()
                IBinder flingDeviceControllerListener = data.readStrongBinder(); // tv.matchstick.fling.internal.IFlingDeviceControllerListener
                Bundle bundle; // tv.matchstick.fling.EXTRA_FLING_FLAGS,
                                // last_application_id, last_session_id
                if (data.readInt() != 0)
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                else
                    bundle = null;
                initFlingService(callbacks, version, pakcageName, flingDeviceControllerListener, bundle);
                reply.writeNoException();
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
