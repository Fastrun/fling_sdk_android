
package tv.matchstick.server.fling.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

final class IFlingCallbacksProxy implements IFlingCallbacks {
    private IBinder mRemote;

    IFlingCallbacksProxy(IBinder ibinder)
    {
        mRemote = ibinder;
    }

    public final void onConnect(int i, IBinder ibinder, Bundle bundle) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();
        parcel.writeInterfaceToken("tv.matchstick.common.internal.IFlingCallbacks");
        parcel.writeInt(i);
        parcel.writeStrongBinder(ibinder);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }
        mRemote.transact(1, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();
    }

    public final IBinder asBinder()
    {
        return mRemote;
    }
}
