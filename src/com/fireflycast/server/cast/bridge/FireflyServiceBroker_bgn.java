
package com.fireflycast.server.cast.bridge;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public abstract class FireflyServiceBroker_bgn extends Binder implements IFireflyServiceBroker_bgm {

    public FireflyServiceBroker_bgn()
    {
        attachInterface(this, "com.fireflycast.common.internal.IFireflyServiceBroker");
    }

    public static IFireflyServiceBroker_bgm a(IBinder ibinder) // asInterface
    {
        if (ibinder == null)
            return null;
        android.os.IInterface iinterface = ibinder
                .queryLocalInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
        if (iinterface != null && (iinterface instanceof IFireflyServiceBroker_bgm))
            return (IFireflyServiceBroker_bgm) iinterface;
        else
            return new IFireflyServiceBrokerProxy_bgo(ibinder); // FireflyServiceBrokerProxy?
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
                reply.writeString("com.fireflycast.common.internal.IFireflyServiceBroker");
                return true;

            case 1: // '\001'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj16 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int l7 = data.readInt();
                String s20 = data.readString();
                String s21 = data.readString();
                String as2[] = data.createStringArray();
                String s22 = data.readString();
                Bundle bundle16;
                if (data.readInt() != 0)
                    bundle16 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                else
                    bundle16 = null;
                a(bgj16, l7, s20, s21, as2, s22, bundle16);
                reply.writeNoException();
                return true;

            case 2: // '\002'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj15 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int j7 = data.readInt();
                String s19 = data.readString();
                int k7 = data.readInt();
                Bundle bundle15 = null;
                if (k7 != 0)
                    bundle15 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                b(bgj15, j7, s19, bundle15);
                reply.writeNoException();
                return true;

            case 3: // '\003'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                a(IFireflyCallbacksStub_bgk.a(data.readStrongBinder()), data.readInt(),
                        data.readString());
                reply.writeNoException();
                return true;

            case 4: // '\004'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                a(IFireflyCallbacksStub_bgk.a(data.readStrongBinder()), data.readInt());
                reply.writeNoException();
                return true;

            case 5: // '\005'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj14 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int l6 = data.readInt();
                String s18 = data.readString();
                int i7 = data.readInt();
                Bundle bundle14 = null;
                if (i7 != 0)
                    bundle14 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                c(bgj14, l6, s18, bundle14);
                reply.writeNoException();
                return true;

            case 6: // '\006'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj13 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int j6 = data.readInt();
                String s17 = data.readString();
                int k6 = data.readInt();
                Bundle bundle13 = null;
                if (k6 != 0)
                    bundle13 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                d(bgj13, j6, s17, bundle13);
                reply.writeNoException();
                return true;

            case 7: // '\007'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj12 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int l5 = data.readInt();
                String s16 = data.readString();
                int i6 = data.readInt();
                Bundle bundle12 = null;
                if (i6 != 0)
                    bundle12 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                e(bgj12, l5, s16, bundle12);
                reply.writeNoException();
                return true;

            case 8: // '\b'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj11 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int j5 = data.readInt();
                String s15 = data.readString();
                int k5 = data.readInt();
                Bundle bundle11 = null;
                if (k5 != 0)
                    bundle11 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                f(bgj11, j5, s15, bundle11);
                reply.writeNoException();
                return true;

            case 9: // '\t'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj10 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int i5 = data.readInt();
                String s11 = data.readString();
                String s12 = data.readString();
                String as1[] = data.createStringArray();
                String s13 = data.readString();
                IBinder ibinder1 = data.readStrongBinder();
                String s14 = data.readString();
                Bundle bundle10;
                if (data.readInt() != 0)
                    bundle10 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                else
                    bundle10 = null;
                a(bgj10, i5, s11, s12, as1, s13, ibinder1, s14, bundle10);
                reply.writeNoException();
                return true;

            case 10: // '\n'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                a(IFireflyCallbacksStub_bgk.a(data.readStrongBinder()), data.readInt(),
                        data.readString(),
                        data.readString(), data.createStringArray());
                reply.writeNoException();
                return true;

            case 11: // '\013'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj9 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int k4 = data.readInt();
                String s10 = data.readString();
                int l4 = data.readInt();
                Bundle bundle9 = null;
                if (l4 != 0)
                    bundle9 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                g(bgj9, k4, s10, bundle9);
                reply.writeNoException();
                return true;

            case 12: // '\f'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj8 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int i4 = data.readInt();
                String s9 = data.readString();
                int j4 = data.readInt();
                Bundle bundle8 = null;
                if (j4 != 0)
                    bundle8 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                a(bgj8, i4, s9, bundle8);
                reply.writeNoException();
                return true;

            case 13: // '\r'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj7 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int k3 = data.readInt();
                String s8 = data.readString();
                int l3 = data.readInt();
                Bundle bundle7 = null;
                if (l3 != 0)
                    bundle7 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                h(bgj7, k3, s8, bundle7);
                reply.writeNoException();
                return true;

            case 14: // '\016'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj6 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int i3 = data.readInt();
                String s7 = data.readString();
                int j3 = data.readInt();
                Bundle bundle6 = null;
                if (j3 != 0)
                    bundle6 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                i(bgj6, i3, s7, bundle6);
                reply.writeNoException();
                return true;

            case 15: // '\017'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj5 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int k2 = data.readInt();
                String s6 = data.readString();
                int l2 = data.readInt();
                Bundle bundle5 = null;
                if (l2 != 0)
                    bundle5 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                j(bgj5, k2, s6, bundle5);
                reply.writeNoException();
                return true;

            case 16: // '\020'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj4 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int i2 = data.readInt();
                String s5 = data.readString();
                int j2 = data.readInt();
                Bundle bundle4 = null;
                if (j2 != 0)
                    bundle4 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                k(bgj4, i2, s5, bundle4);
                reply.writeNoException();
                return true;

            case 17: // '\021'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj3 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int k1 = data.readInt();
                String s4 = data.readString();
                int l1 = data.readInt();
                Bundle bundle3 = null;
                if (l1 != 0)
                    bundle3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                l(bgj3, k1, s4, bundle3);
                reply.writeNoException();
                return true;

            case 18: // '\022'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj2 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int i1 = data.readInt();
                String s3 = data.readString();
                int j1 = data.readInt();
                Bundle bundle2 = null;
                if (j1 != 0)
                    bundle2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                m(bgj2, i1, s3, bundle2);
                reply.writeNoException();
                return true;

            case 19: // '\023'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj1 = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());// IFireflyCallbacks
                int l = data.readInt(); // 4242000
                String s2 = data.readString(); // getContext().getPackageName()
                IBinder ibinder = data.readStrongBinder(); // com.fireflycast.cast.internal.ICastDeviceControllerListener
                Bundle bundle1; // com.fireflycast.cast.EXTRA_CAST_FLAGS,
                                // last_application_id, last_session_id
                if (data.readInt() != 0)
                    bundle1 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                else
                    bundle1 = null;
                initCastService_a(bgj1, l, s2, ibinder, bundle1);
                reply.writeNoException();
                return true;

            case 20: // '\024'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                IFireflyCallbacks_bgj bgj = IFireflyCallbacksStub_bgk.a(data.readStrongBinder());
                int k = data.readInt();
                String s = data.readString();
                String as[] = data.createStringArray();
                String s1 = data.readString();
                Bundle bundle;
                if (data.readInt() != 0)
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
                else
                    bundle = null;
                a(bgj, k, s, as, s1, bundle);
                reply.writeNoException();
                return true;

            case 21: // '\025'
                data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
                b(IFireflyCallbacksStub_bgk.a(data.readStrongBinder()), data.readInt(),
                        data.readString());
                reply.writeNoException();
                return true;
        }

        return super.onTransact(code, data, reply, flags);
    }
}
