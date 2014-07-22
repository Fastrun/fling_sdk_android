
package tv.matchstick.server.fling.bridge;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class FlingDeviceControllerStub extends Binder implements
        IFlingDeviceController {
    public FlingDeviceControllerStub()
    {
        attachInterface(this, "tv.matchstick.fling.internal.IFlingDeviceController");
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
                reply.writeString("tv.matchstick.fling.internal.IFlingDeviceController");
                return true;

            case 1: // '\001'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                disconnect();
                return true;

            case 2: // '\002'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                String s = data.readString();
                int l = data.readInt();
                boolean flag3 = false;
                if (l != 0)
                    flag3 = true;
                launchApplication(s, flag3);
                return true;

            case 3: // '\003'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                joinApplication(data.readString(), data.readString());
                return true;

            case 4: // '\004'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                leaveApplication();
                return true;

            case 5: // '\005'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                stopApplication(data.readString());
                return true;

            case 6: // '\006'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                requestStatus();
                return true;

            case 7: // '\007'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                double d1 = data.readDouble();
                double d2 = data.readDouble();
                boolean flag2;
                if (data.readInt() != 0)
                    flag2 = true;
                else
                    flag2 = false;
                setVolume(d1, d2, flag2);
                return true;

            case 8: // '\b'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                boolean flag = 0 != data.readInt();
                double d = data.readDouble();
                boolean flag1 = 0 != data.readInt();
                setMute(flag, d, flag1);
                return true;

            case 9: // '\t'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                sendMessage(data.readString(), data.readString(), data.readLong()); // namespace,
                                                                                      // message,
                                                                                      // id
                return true;

            case 10: // '\n'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                sendBinaryMessage(data.readString(), data.createByteArray(), data.readLong());
                return true;

            case 11: // '\013'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                setMessageReceivedCallbacks(data.readString());
                return true;

            case 12: // '\f'
                data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
                removeMessageReceivedCallbacks(data.readString());
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
