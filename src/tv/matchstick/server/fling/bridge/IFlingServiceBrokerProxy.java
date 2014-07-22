
package tv.matchstick.server.fling.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

final class IFlingServiceBrokerProxy implements IFlingServiceBroker {
    private IBinder mRemote;

    IFlingServiceBrokerProxy(IBinder ibinder)
    {
        mRemote = ibinder;
    }
    
    public final IBinder asBinder()
    {
        return mRemote;
    }

    public final void initFlingService(IFlingCallbacks bgj1, int i1, String s, IBinder ibinder,
            Bundle bundle) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("tv.matchstick.common.internal.IFlingServiceBroker");
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
        mRemote.transact(19, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();
    }
}
