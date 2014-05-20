package com.fireflycast.client.internal;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.fireflycast.client.internal.IFireflyCallbacks_em;

/*
 * a bridge to Gms Service
 */
public interface IFireflyServiceBroker_en extends IInterface {

	public void a(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString1, String paramString2,
			String[] paramArrayOfString, String paramString3, Bundle paramBundle)
			throws RemoteException;

	public void a(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void a(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString) throws RemoteException;

	public void a(IFireflyCallbacks_em fireflyCallbacks, int paramInt)
			throws RemoteException;

	public void b(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void c(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void d(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void e(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void a(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString1, String paramString2,
			String[] paramArrayOfString, String paramString3,
			IBinder paramIBinder, String paramString4, Bundle paramBundle)
			throws RemoteException;

	public void a(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString1, String paramString2,
			String[] paramArrayOfString) throws RemoteException;

	public void f(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void g(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void h(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void i(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void j(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void k(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void l(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void m(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void initService(IFireflyCallbacks_em fireflyCallbacks,
			int requestVersion, String packageName, IBinder binder,
			Bundle bundle) throws RemoteException;

	public void a(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString1, String[] paramArrayOfString,
			String paramString2, Bundle paramBundle) throws RemoteException;

	public void b(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString) throws RemoteException;

	public void c(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString) throws RemoteException;

	public void n(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString, Bundle paramBundle) throws RemoteException;

	public void d(IFireflyCallbacks_em fireflyCallbacks, int paramInt,
			String paramString) throws RemoteException;

	public static abstract class Stub_a extends Binder implements
			IFireflyServiceBroker_en {
		public static IFireflyServiceBroker_en asInterface_z(
				IBinder paramIBinder) {
			if (paramIBinder == null)
				return null;
			IInterface localIInterface = paramIBinder
					.queryLocalInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
			if ((localIInterface != null)
					&& (localIInterface instanceof IFireflyServiceBroker_en))
				return ((IFireflyServiceBroker_en) localIInterface);
			return new Proxy_aimpl(paramIBinder);
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			IFireflyCallbacks_em localem;
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
			case 1:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				localObject1 = data.readString();
				localObject2 = data.createStringArray();
				localObject3 = data.readString();
				if (0 != data.readInt())
					localObject4 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject4 = null;
				a(localem, i, str1, (String) localObject1,
						(String[]) localObject2, (String) localObject3,
						(Bundle) localObject4);
				reply.writeNoException();
				return true;
			case 2:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				a(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 3:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				a(localem, i, str1);
				reply.writeNoException();
				return true;
			case 4:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				a(localem, i);
				reply.writeNoException();
				return true;
			case 5:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				b(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 6:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				c(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 7:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				d(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 8:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				e(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 9:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				localObject1 = data.readString();
				localObject2 = data.createStringArray();
				localObject3 = data.readString();
				localObject4 = data.readStrongBinder();
				String str2 = data.readString();
				Bundle localBundle;
				if (0 != data.readInt())
					localBundle = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localBundle = null;
				a(localem, i, str1, (String) localObject1,
						(String[]) localObject2, (String) localObject3,
						(IBinder) localObject4, str2, localBundle);
				reply.writeNoException();
				return true;
			case 10:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				localObject1 = data.readString();
				localObject2 = data.createStringArray();
				a(localem, i, str1, (String) localObject1,
						(String[]) localObject2);
				reply.writeNoException();
				return true;
			case 11:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				f(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 12:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				g(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 13:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				h(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 14:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				i(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 15:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				j(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 16:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				k(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 17:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				l(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 18:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				m(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 19:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
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
			case 20:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				localObject1 = data.createStringArray();
				localObject2 = data.readString();
				if (0 != data.readInt())
					localObject3 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject3 = null;
				a(localem, i, str1, (String[]) localObject1,
						(String) localObject2, (Bundle) localObject3);
				reply.writeNoException();
				return true;
			case 21:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				b(localem, i, str1);
				reply.writeNoException();
				return true;
			case 22:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				c(localem, i, str1);
				reply.writeNoException();
				return true;
			case 23:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				if (0 != data.readInt())
					localObject1 = (Bundle) Bundle.CREATOR
							.createFromParcel(data);
				else
					localObject1 = null;
				n(localem, i, str1, (Bundle) localObject1);
				reply.writeNoException();
				return true;
			case 24:
				data.enforceInterface("com.fireflycast.common.internal.IFireflyServiceBroker");
				localem = IFireflyCallbacks_em.Stub_a.asInterface_y(data
						.readStrongBinder());
				i = data.readInt();
				str1 = data.readString();
				d(localem, i, str1);
				reply.writeNoException();
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy_aimpl implements IFireflyServiceBroker_en {
			private IBinder ky;

			Proxy_aimpl(IBinder paramIBinder) {
				this.ky = paramIBinder;
			}

			public IBinder asBinder() {
				return this.ky;
			}

			public void a(IFireflyCallbacks_em paramem, int paramInt,
					String paramString1, String paramString2,
					String[] paramArrayOfString, String paramString3,
					Bundle paramBundle) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString1);
					localParcel1.writeString(paramString2);
					localParcel1.writeStringArray(paramArrayOfString);
					localParcel1.writeString(paramString3);
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

			public void a(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(2, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void a(IFireflyCallbacks_em paramem, int paramInt,
					String paramString) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					this.ky.transact(3, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void a(IFireflyCallbacks_em paramem, int paramInt)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					this.ky.transact(4, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void b(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(5, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void c(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(6, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void d(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(7, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void e(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(8, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void a(IFireflyCallbacks_em paramem, int paramInt,
					String paramString1, String paramString2,
					String[] paramArrayOfString, String paramString3,
					IBinder paramIBinder, String paramString4,
					Bundle paramBundle) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString1);
					localParcel1.writeString(paramString2);
					localParcel1.writeStringArray(paramArrayOfString);
					localParcel1.writeString(paramString3);
					localParcel1.writeStrongBinder(paramIBinder);
					localParcel1.writeString(paramString4);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(9, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void a(IFireflyCallbacks_em paramem, int paramInt,
					String paramString1, String paramString2,
					String[] paramArrayOfString) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString1);
					localParcel1.writeString(paramString2);
					localParcel1.writeStringArray(paramArrayOfString);
					this.ky.transact(10, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void f(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(11, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void g(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(12, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void h(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(13, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void i(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(14, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void j(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(15, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void k(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(16, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void l(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(17, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void m(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(18, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void initService(IFireflyCallbacks_em paramem,
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

			public void a(IFireflyCallbacks_em paramem, int paramInt,
					String paramString1, String[] paramArrayOfString,
					String paramString2, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString1);
					localParcel1.writeStringArray(paramArrayOfString);
					localParcel1.writeString(paramString2);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(20, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void b(IFireflyCallbacks_em paramem, int paramInt,
					String paramString) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					this.ky.transact(21, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void c(IFireflyCallbacks_em paramem, int paramInt,
					String paramString) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					this.ky.transact(22, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void n(IFireflyCallbacks_em paramem, int paramInt,
					String paramString, Bundle paramBundle)
					throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					if (paramBundle != null) {
						localParcel1.writeInt(1);
						paramBundle.writeToParcel(localParcel1, 0);
					} else {
						localParcel1.writeInt(0);
					}
					this.ky.transact(23, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}

			public void d(IFireflyCallbacks_em paramem, int paramInt,
					String paramString) throws RemoteException {
				Parcel localParcel1 = Parcel.obtain();
				Parcel localParcel2 = Parcel.obtain();
				try {
					localParcel1
							.writeInterfaceToken("com.fireflycast.common.internal.IFireflyServiceBroker");
					localParcel1.writeStrongBinder((paramem != null) ? paramem
							.asBinder() : null);
					localParcel1.writeInt(paramInt);
					localParcel1.writeString(paramString);
					this.ky.transact(24, localParcel1, localParcel2, 0);
					localParcel2.readException();
				} finally {
					localParcel2.recycle();
					localParcel1.recycle();
				}
			}
		}
	}

}
