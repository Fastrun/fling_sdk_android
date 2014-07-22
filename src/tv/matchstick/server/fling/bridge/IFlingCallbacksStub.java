
package tv.matchstick.server.fling.bridge;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class IFlingCallbacksStub extends Binder implements IFlingCallbacks {
    public IFlingCallbacksStub()
    {
        attachInterface(this, "tv.matchstick.common.internal.IFlingCallbacks");
    }

    public static IFlingCallbacks a(IBinder ibinder) // asInterface
    {
        if (ibinder == null)
            return null;
        android.os.IInterface iinterface = ibinder
                .queryLocalInterface("tv.matchstick.common.internal.IFlingCallbacks");
        if (iinterface != null && (iinterface instanceof IFlingCallbacks))
            return (IFlingCallbacks) iinterface;
        else
            return new IFlingCallbacksProxy(ibinder); // GmsCallbacksProxy?
    }

    public IBinder asBinder()
    {
        return this;
    }

    public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
            throws RemoteException
    {
        int k;
        IBinder ibinder;
        switch (code)
        {
            case 1598968902:
                reply.writeString("tv.matchstick.common.internal.IFlingCallbacks");
                return true;

            case 1: // '\001'
                data.enforceInterface("tv.matchstick.common.internal.IFlingCallbacks");
                k = data.readInt();
                ibinder = data.readStrongBinder();
                Bundle bundle;
                if (data.readInt() != 0)
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                else
                    bundle = null;
                onConnect(k, ibinder, bundle);
                reply.writeNoException();
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
