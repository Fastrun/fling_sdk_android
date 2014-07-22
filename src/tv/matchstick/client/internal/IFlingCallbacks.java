package tv.matchstick.client.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFlingCallbacks extends IInterface {

	public abstract void onPostInitComplete(int statusCode, IBinder binder,
			Bundle bundle) throws RemoteException;

	public static abstract class Stub_a extends Binder implements
			IFlingCallbacks {
		public Stub_a() {
			attachInterface(this,
					"tv.matchstick.common.internal.IFlingCallbacks");
		}

		public static IFlingCallbacks asInterface(IBinder paramIBinder) {
			if (paramIBinder == null)
				return null;
			IInterface localIInterface = paramIBinder
					.queryLocalInterface("tv.matchstick.common.internal.IFlingCallbacks");
			if ((localIInterface != null)
					&& (localIInterface instanceof IFlingCallbacks))
				return ((IFlingCallbacks) localIInterface);
			return new Proxy(paramIBinder);
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			switch (code) {
			case 1598968902:
				reply.writeString("tv.matchstick.common.internal.IFlingCallbacks");
				return true;
			case 1:
				data.enforceInterface("tv.matchstick.common.internal.IFlingCallbacks");
				int statusCode = data.readInt();
				IBinder binder = data.readStrongBinder();
				Bundle bundle;
				if (0 != data.readInt())
					bundle = (Bundle) Bundle.CREATOR.createFromParcel(data);
				else
					bundle = null;
				onPostInitComplete(statusCode, binder, bundle);
				reply.writeNoException();
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements IFlingCallbacks {
			private IBinder remote;

			Proxy(IBinder paramIBinder) {
				this.remote = paramIBinder;
			}

			public IBinder asBinder() {
				return this.remote;
			}

			public void onPostInitComplete(int statusCode, IBinder binder,
					Bundle bundle) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("tv.matchstick.common.internal.IFlingCallbacks");
					localParcel1.writeInt(statusCode);
					localParcel1.writeStrongBinder(binder);
					if (bundle != null) {
						localParcel1.writeInt(1);
						bundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.remote.transact(1, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}
		}
	}

}
