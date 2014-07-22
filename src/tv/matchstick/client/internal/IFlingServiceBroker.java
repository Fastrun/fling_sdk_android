package tv.matchstick.client.internal;

import tv.matchstick.client.internal.IFlingCallbacks;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/**
 * a bridge to Fling Service
 */
public interface IFlingServiceBroker extends IInterface {
	public void initService(IFlingCallbacks flingCallbacks,
			int requestVersion, String packageName, IBinder binder,
			Bundle bundle) throws RemoteException;
	
	public static abstract class Stub extends Binder implements
			IFlingServiceBroker {
		public static IFlingServiceBroker asInterface(
				IBinder paramIBinder) {
			if (paramIBinder == null)
				return null;
			IInterface localIInterface = paramIBinder
					.queryLocalInterface("tv.matchstick.common.internal.IFlingServiceBroker");
			if ((localIInterface != null)
					&& (localIInterface instanceof IFlingServiceBroker))
				return ((IFlingServiceBroker) localIInterface);
			return new Proxy(paramIBinder);
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			IFlingCallbacks localem;
			int i;
			String str1;
			Object localObject1;
			Object localObject2;
			Object localObject3;
			Object localObject4;
			switch (code) {
			case 1598968902:
				reply.writeString("tv.matchstick.common.internal.IFlingServiceBroker");
				return true;
				
			case 19:
				data.enforceInterface("tv.matchstick.common.internal.IFlingServiceBroker");
				localem = IFlingCallbacks.Stub_a.asInterface(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				localObject1 = data.readStrongBinder();
				if (0 != data.readInt())
					localObject2 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject2 = null;
				initService(localem, i, str1, (IBinder) localObject1,
						(Bundle) localObject2);
				reply.writeNoException();
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements IFlingServiceBroker {
			private IBinder remoted;

			Proxy(IBinder paramIBinder) {
				this.remoted = paramIBinder;
			}

			public IBinder asBinder() {
				return this.remoted;
			}

			public void initService(IFlingCallbacks paramem,
					int paramInt, String paramString, IBinder paramIBinder,
					Bundle paramBundle) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("tv.matchstick.common.internal.IFlingServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					localParcel1.writeStrongBinder(paramIBinder);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.remoted.transact(19, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}
		}
	}

}
