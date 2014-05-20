
package com.fireflycast.server.cast.bridge;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class IFireflyCallbacksStub_bgk extends Binder implements IFireflyCallbacks_bgj {
    public IFireflyCallbacksStub_bgk()
    {
        attachInterface(this, "com.fireflycast.common.internal.IFireflyCallbacks");
    }

    public static IFireflyCallbacks_bgj a(IBinder ibinder) // asInterface
    {
        if (ibinder == null)
            return null;
        android.os.IInterface iinterface = ibinder
                .queryLocalInterface("com.fireflycast.common.internal.IFireflyCallbacks");
        if (iinterface != null && (iinterface instanceof IFireflyCallbacks_bgj))
            return (IFireflyCallbacks_bgj) iinterface;
        else
            return new IFireflyCallbacksProxy_bgl(ibinder); // GmsCallbacksProxy?
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
                reply.writeString("com.fireflycast.common.internal.IFireflyCallbacks");
                return true;

            case 1: // '\001'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyCallbacks");
                k = data.readInt();
                ibinder = data.readStrongBinder();
                Bundle bundle;
                if (data.readInt() != 0)
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                else
                    bundle = null;
                onConnect_a(k, ibinder, bundle);
                reply.writeNoException();
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
