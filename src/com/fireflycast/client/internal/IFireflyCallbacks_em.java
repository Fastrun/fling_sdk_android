package com.fireflycast.client.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFireflyCallbacks_em extends IInterface {

	public abstract void onPostInitComplete_b(int statusCode, IBinder binder,
			Bundle bundle) throws RemoteException;

	public static abstract class Stub_a extends Binder implements
			IFireflyCallbacks_em {
		public Stub_a() {
			attachInterface(this,
					"com.fireflycast.common.internal.IFireflyCallbacks");
		}

		public static IFireflyCallbacks_em asInterface_y(IBinder paramIBinder) {
			if (paramIBinder == null)
				return null;
			IInterface localIInterface = paramIBinder
					.queryLocalInterface("com.fireflycast.common.internal.IFireflyCallbacks");
			if ((localIInterface != null)
					&& (localIInterface instanceof IFireflyCallbacks_em))
				return ((IFireflyCallbacks_em) localIInterface);
			return new Proxy_aimpl(paramIBinder);
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			switch (code) {
			case 1598968902:
				reply.writeString("com.fireflycast.common.internal.IFireflyCallbacks");
				return true;
			case 1:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyCallbacks");
				int i = data.readInt();
				IBinder localIBinder = data.readStrongBinder();
				Bundle localBundle;
				if (0 != data.readInt())
					localBundle = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localBundle = null;
				onPostInitComplete_b(i, localIBinder, localBundle);
				reply.writeNoException();
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy_aimpl implements IFireflyCallbacks_em {
			private IBinder ky;

			Proxy_aimpl(IBinder paramIBinder) {
				this.ky = paramIBinder;
			}

			public IBinder asBinder() {
				return this.ky;
			}

			public void onPostInitComplete_b(int paramInt,
					IBinder paramIBinder, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyCallbacks");
					localParcel1.writeInt(paramInt);
					localParcel1.writeStrongBinder(paramIBinder);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(1, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}
		}
	}

}
