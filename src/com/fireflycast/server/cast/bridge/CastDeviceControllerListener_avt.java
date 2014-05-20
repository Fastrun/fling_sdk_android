
package com.fireflycast.server.cast.bridge;

import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.fireflycast.server.cast.ApplicationMetadata;

public final class CastDeviceControllerListener_avt implements ICastDeviceControllerListener_avr {
    private IBinder mRemoted_a;

    public CastDeviceControllerListener_avt(IBinder ibinder)
    {
        mRemoted_a = ibinder;
    }

    public final void onDisconnected_a(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted_a.transact(1, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void onApplicationConnected_a(ApplicationMetadata applicationmetadata,
            String applicationId,
            String sessionId, boolean relaunched) throws RemoteException
    {
        int i;
        Parcel parcel;
        i = 1;
        parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            if (applicationmetadata == null) {
                parcel.writeInt(0);
            } else {
                parcel.writeInt(1);
                applicationmetadata.writeToParcel(parcel, 0);
            }

            parcel.writeString(applicationId);
            parcel.writeString(sessionId);
            if (!relaunched)
                i = 0;
            parcel.writeInt(i);
            mRemoted_a.transact(2, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final void notifyApplicationStatusOrVolumeChanged_a(String status, double volume,
            boolean muteState) throws RemoteException
    {
        int i;
        Parcel parcel;
        i = 1;
        parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeString(status);
            parcel.writeDouble(volume);
            if (!muteState)
                i = 0;
            parcel.writeInt(i);
            mRemoted_a.transact(4, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void a(String s, long l) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeString(s);
            parcel.writeLong(l);
            mRemoted_a.transact(11, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final void a(String s, long l, int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeString(s);
            parcel.writeLong(l);
            parcel.writeInt(i);
            mRemoted_a.transact(10, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void onMessageReceived_a(String s, String s1) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeString(s);
            parcel.writeString(s1);
            mRemoted_a.transact(5, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void a(String s, byte abyte0[]) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeString(s);
            parcel.writeByteArray(abyte0);
            mRemoted_a.transact(6, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final IBinder asBinder()
    {
        return mRemoted_a;
    }

    public final void postApplicationConnectionResult_b(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted_a.transact(3, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final void onRequestResult_c(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted_a.transact(7, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void onRequestStatus_d(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted_a.transact(8, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final void onApplicationDisconnected_e(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted_a.transact(9, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }
}
