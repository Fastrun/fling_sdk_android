
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

final class IFireflyCallbacksProxy_bgl implements IFireflyCallbacks_bgj {
    private IBinder mRemote_a;

    IFireflyCallbacksProxy_bgl(IBinder ibinder)
    {
        mRemote_a = ibinder;
    }

    public final void onConnect_a(int i, IBinder ibinder, Bundle bundle) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyCallbacks");
        parcel.writeInt(i);
        parcel.writeStrongBinder(ibinder);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }
        mRemote_a.transact(1, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();
    }

    public final IBinder asBinder()
    {
        return mRemote_a;
    }
}
