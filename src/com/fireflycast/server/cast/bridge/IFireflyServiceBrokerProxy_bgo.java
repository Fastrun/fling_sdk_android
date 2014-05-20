
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

final class IFireflyServiceBrokerProxy_bgo implements IFireflyServiceBroker_bgm {
    private IBinder mRemote_a;

    IFireflyServiceBrokerProxy_bgo(IBinder ibinder)
    {
        mRemote_a = ibinder;
    }

    public final void a(IFireflyCallbacks_bgj bgj1, int i1) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }
        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        mRemote_a.transact(4, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();
    }

    public final void a(IFireflyCallbacks_bgj bgj1, int i1, String s) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        mRemote_a.transact(3, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void a(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(12, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();
    }

    public final void initCastService_a(IFireflyCallbacks_bgj bgj1, int i1, String s, IBinder ibinder,
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

    public final void a(IFireflyCallbacks_bgj bgj1, int i1, String s, String s1, String as[])
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        parcel.writeString(s1);
        parcel.writeStringArray(as);
        mRemote_a.transact(10, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void a(IFireflyCallbacks_bgj bgj1, int i1, String s, String s1, String as[],
            String s2, Bundle bundle) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        parcel.writeString(s1);
        parcel.writeStringArray(as);
        parcel.writeString(s2);
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

    public final void a(IFireflyCallbacks_bgj bgj1, int i1, String s, String s1, String as[],
            String s2, IBinder ibinder,
            String s3, Bundle bundle) throws RemoteException
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
        parcel.writeString(s1);
        parcel.writeStringArray(as);
        parcel.writeString(s2);
        parcel.writeStrongBinder(ibinder);
        parcel.writeString(s3);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }
        mRemote_a.transact(9, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void a(IFireflyCallbacks_bgj bgj1, int i1, String s, String as[], String s1,
            Bundle bundle) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");

        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        parcel.writeStringArray(as);
        parcel.writeString(s1);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(20, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final IBinder asBinder()
    {
        return mRemote_a;
    }

    public final void b(IFireflyCallbacks_bgj bgj1, int i1, String s) throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        mRemote_a.transact(21, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void b(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");

        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {

            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(2, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void c(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {

            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(5, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void d(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {

            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(6, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void e(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(7, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void f(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");

        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {

            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(8, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void g(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(11, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void h(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }
        mRemote_a.transact(13, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void i(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(14, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void j(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(15, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void k(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");

        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(16, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void l(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");

        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }

        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        }
        else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(17, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }

    public final void m(IFireflyCallbacks_bgj bgj1, int i1, String s, Bundle bundle)
            throws RemoteException
    {
        Parcel parcel;
        Parcel parcel1;
        parcel = Parcel.obtain();
        parcel1 = Parcel.obtain();

        parcel.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");

        IBinder ibinder = null;
        if (bgj1 != null) {
            ibinder = bgj1.asBinder();
        }
        parcel.writeStrongBinder(ibinder);
        parcel.writeInt(i1);
        parcel.writeString(s);
        if (bundle == null) {
            parcel.writeInt(0);
        } else {
            parcel.writeInt(1);
            bundle.writeToParcel(parcel, 0);
        }

        mRemote_a.transact(18, parcel, parcel1, 0);
        parcel1.readException();

        parcel1.recycle();
        parcel.recycle();

    }
}
