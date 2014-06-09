
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

final class IFireflyServiceBrokerProxy implements IFireflyServiceBroker {
    private IBinder mRemote_a;

    IFireflyServiceBrokerProxy(IBinder ibinder)
    {
        mRemote_a = ibinder;
    }
    
    public final IBinder asBinder()
    {
        return mRemote_a;
    }

    public final void initCastService_a(IFireflyCallbacks bgj1, int i1, String s, IBinder ibinder,
            Bundle bundle) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder1 = null;
        if (bgj1 != null) {
            ibinder1 = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder1);
        parcel.writeInt(i1);
        parcel.writeString(s);
        parcel.writeStrongBinder(ibinder);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {

            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }
        mRemote_a.transact(19, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();
    }
}
