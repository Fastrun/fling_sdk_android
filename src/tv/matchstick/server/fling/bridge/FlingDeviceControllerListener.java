
package tv.matchstick.server.fling.bridge;

import tv.matchstick.server.fling.ApplicationMetadata;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public final class FlingDeviceControllerListener implements IFlingDeviceControllerListener {
    private IBinder mRemoted;

    public FlingDeviceControllerListener(IBinder ibinder)
    {
        mRemoted = ibinder;
    }

    public final void onDisconnected(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted.transact(1, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void onApplicationConnected(ApplicationMetadata applicationmetadata,
            String applicationId,
            String sessionId, boolean relaunched) throws RemoteException
    {
        int i;
        Parcel parcel;
        i = 1;
        parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
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
            mRemoted.transact(2, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final void notifyApplicationStatusOrVolumeChanged(String status, double volume,
            boolean muteState) throws RemoteException
    {
        int i;
        Parcel parcel;
        i = 1;
        parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeString(status);
            parcel.writeDouble(volume);
            if (!muteState)
                i = 0;
            parcel.writeInt(i);
            mRemoted.transact(4, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void requestCallback(String namespace, long requestId) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeString(namespace);
            parcel.writeLong(requestId);
            mRemoted.transact(11, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final void requestCallback(String namespace, long requestId, int result) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeString(namespace);
            parcel.writeLong(requestId);
            parcel.writeInt(result);
            mRemoted.transact(10, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void onMessageReceived(String namespace, String message) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeString(namespace);
            parcel.writeString(message);
            mRemoted.transact(5, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void onReceiveBinary(String s, byte abyte0[]) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeString(s);
            parcel.writeByteArray(abyte0);
            mRemoted.transact(6, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final IBinder asBinder()
    {
        return mRemoted;
    }

    public final void postApplicationConnectionResult(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted.transact(3, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final void onRequestResult(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted.transact(7, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
    }

    public final void onRequestStatus(int result) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeInt(result);
            mRemoted.transact(8, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }

    public final void onApplicationDisconnected(int i) throws RemoteException
    {
        Parcel parcel = Parcel.obtain();
        try {
            parcel.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
            parcel.writeInt(i);
            mRemoted.transact(9, parcel, null, 1);
        } finally {
            parcel.recycle();
        }
        return;
    }
}
