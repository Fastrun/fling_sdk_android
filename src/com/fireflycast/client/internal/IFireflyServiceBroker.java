package com.fireflycast.client.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.fireflycast.client.internal.IFireflyCallbacks;

/*
 * a bridge to Gms Service
 */
public interface IFireflyServiceBroker extends IInterface {
	public void initService(IFireflyCallbacks fireflyCallbacks,
			int requestVersion, String packageName, IBinder binder,
			Bundle bundle) throws RemoteException;
	
	public static abstract class Stub_a extends Binder implements
			IFireflyServiceBroker {
		public static IFireflyServiceBroker asInterface_z(
				IBinder paramIBinder) {
			if (paramIBinder == null)
				return null;
			IInterface localIInterface = paramIBinder
					.queryLocalInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
			if ((localIInterface != null)
					&& (localIInterface instanceof IFireflyServiceBroker))
				return ((IFireflyServiceBroker) localIInterface);
			return new Proxy_aimpl(paramIBinder);
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			IFireflyCallbacks localem;
			int i;
			String str1;
			Object localObject1;
			Object localObject2;
			Object localObject3;
			Object localObject4;
			switch (code) {
			case 1598968902:
				reply.writeString("com.fireflycast.common.internal.IFireflyServiceBroker");
				return true;
				
			case 19:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks.Stub_a.asInterface_y(data
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

		private static class Proxy_aimpl implements IFireflyServiceBroker {
			private IBinder ky;

			Proxy_aimpl(IBinder paramIBinder) {
				this.ky = paramIBinder;
			}

			public IBinder asBinder() {
				return this.ky;
			}

			public void initService(IFireflyCallbacks paramem,
					int paramInt, String paramString, IBinder paramIBinder,
					Bundle paramBundle) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
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
					this.ky.transact(19, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}
		}
	}

}
