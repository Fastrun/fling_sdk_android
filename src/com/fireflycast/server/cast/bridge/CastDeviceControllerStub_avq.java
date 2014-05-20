
package com.fireflycast.server.cast.bridge;

import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class CastDeviceControllerStub_avq extends Binder implements
        ICastDeviceController_avp {
    public CastDeviceControllerStub_avq()
    {
        attachInterface(this, "com.fireflycast.cast.internal.ICastDeviceController");
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
                reply.writeString("com.fireflycast.cast.internal.ICastDeviceController");
                return true;

            case 1: // '\001'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                disconnect_a();
                return true;

            case 2: // '\002'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                String s = data.readString();
                int l = data.readInt();
                boolean flag3 = false;
                if (l != 0)
                    flag3 = true;
                launchApplication_a(s, flag3);
                return true;

            case 3: // '\003'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                joinApplication_a(data.readString(), data.readString());
                return true;

            case 4: // '\004'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                leaveApplication_b();
                return true;

            case 5: // '\005'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                stopApplication_a(data.readString());
                return true;

            case 6: // '\006'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                requestStatus_c();
                return true;

            case 7: // '\007'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                double d1 = data.readDouble();
                double d2 = data.readDouble();
                boolean flag2;
                if (data.readInt() != 0)
                    flag2 = true;
                else
                    flag2 = false;
                setVolume_a(d1, d2, flag2);
                return true;

            case 8: // '\b'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                boolean flag = 0 != data.readInt();
                double d = data.readDouble();
                boolean flag1 = 0 != data.readInt();
                setMute_a(flag, d, flag1);
                return true;

            case 9: // '\t'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                sendMessage_a(data.readString(), data.readString(), data.readLong()); // namespace,
                                                                                      // message,
                                                                                      // id
                return true;

            case 10: // '\n'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                sendBinaryMessage_a(data.readString(), data.createByteArray(), data.readLong());
                return true;

            case 11: // '\013'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                setMessageReceivedCallbacks_b(data.readString());
                return true;

            case 12: // '\f'
                data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
                removeMessageReceivedCallbacks_c(data.readString());
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
