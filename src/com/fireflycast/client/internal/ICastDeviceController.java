package com.fireflycast.client.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public interface ICastDeviceController extends IInterface {

	public void disconnect() throws RemoteException;

	public void launchApplication_e(String applicationId, boolean relaunchFlag)
			throws RemoteException;

	public void joinApplication_e(String applicationId, String sessionId)
			throws RemoteException;

	public void leaveApplication_df() throws RemoteException;

	public void stopApplication_R(String sessionId) throws RemoteException;

	public void requestStatus_cZ() throws RemoteException;

	public void setVolume_a(double volume, double originalVolume, boolean isMute)
			throws RemoteException;

	public void setMute_a(boolean mute, double volume, boolean isMute)
			throws RemoteException;

	public void sendMessage_a(String namespace, String message, long requestId)
			throws RemoteException;

	public void setMessageReceivedCallbacks_S(String namespace)
			throws RemoteException;

	public void removeMessageReceivedCallbacks_T(String namespace)
			throws RemoteException;

	/*************************************************/
	// unused
	/*************************************************/
	public void sendBinaryMessage_a(String namespace, byte[] message,
			long requestId) throws RemoteException;

	/*
	 * An implementation of ds interface
	 */
	public static abstract class Stub_a extends Binder implements
			ICastDeviceController {
		public static ICastDeviceController asInterface_w(IBinder obj) {
			if (obj == null)
				return null;
			IInterface local = obj
					.queryLocalInterface("com.fireflycast.cast.internal.ICastDeviceController");
			if ((local != null) && (local instanceof ICastDeviceController)) {
				return ((ICastDeviceController) local);
			}
			return new Proxy_a(obj);
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			String str1;
			String str2;
			Object localObject;
			long l;
			switch (code) {
			case 1598968902:
				reply.writeString("com.fireflycast.cast.internal.ICastDeviceController");
				return true;
			case 1:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				disconnect();
				return true;
			case 2:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				str1 = data.readString();
				boolean bool2 = 0 != data.readInt();
				launchApplication_e(str1, bool2);
				return true;
			case 3:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				str1 = data.readString();
				String str3 = data.readString();
				joinApplication_e(str1, str3);
				return true;
			case 4:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				leaveApplication_df();
				return true;
			case 5:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				str1 = data.readString();
				stopApplication_R(str1);
				return true;
			case 6:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				requestStatus_cZ();
				return true;
			case 7:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				double d1 = data.readDouble();
				double d3 = data.readDouble();
				boolean bool4 = 0 != data.readInt();
				setVolume_a(d1, d3, bool4);
				return true;
			case 8:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				boolean bool1 = 0 != data.readInt();
				double d2 = data.readDouble();
				boolean bool3 = 0 != data.readInt();
				setMute_a(bool1, d2, bool3);
				return true;
			case 9:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				str2 = data.readString();
				localObject = data.readString();
				l = data.readLong();
				sendMessage_a(str2, (String) localObject, l);
				return true;
			case 10:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				str2 = data.readString();
				localObject = data.createByteArray();
				l = data.readLong();
				sendMessage_a(str2, (String) localObject, l);
				return true;
			case 11:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				str2 = data.readString();
				setMessageReceivedCallbacks_S(str2);
				return true;
			case 12:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceController");
				str2 = data.readString();
				removeMessageReceivedCallbacks_T(str2);
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy_a implements ICastDeviceController {
			private IBinder ky;

			Proxy_a(IBinder paramIBinder) {
				this.ky = paramIBinder;
			}

			public IBinder asBinder() {
				return this.ky;
			}

			public void disconnect() throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					this.ky.transact(1, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void launchApplication_e(String paramString,
					boolean paramBoolean) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeString(paramString);
					localParcel.writeInt((paramBoolean) ? 1 : 0);
					this.ky.transact(2, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void joinApplication_e(String paramString1,
					String paramString2) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeString(paramString1);
					localParcel.writeString(paramString2);
					this.ky.transact(3, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void leaveApplication_df() throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					this.ky.transact(4, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void stopApplication_R(String paramString)
					throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeString(paramString);
					this.ky.transact(5, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void requestStatus_cZ() throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					this.ky.transact(6, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void setVolume_a(double paramDouble1, double paramDouble2,
					boolean paramBoolean) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeDouble(paramDouble1);
					localParcel.writeDouble(paramDouble2);
					localParcel.writeInt((paramBoolean) ? 1 : 0);
					this.ky.transact(7, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void setMute_a(boolean paramBoolean1, double paramDouble,
					boolean paramBoolean2) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeInt((paramBoolean1) ? 1 : 0);
					localParcel.writeDouble(paramDouble);
					localParcel.writeInt((paramBoolean2) ? 1 : 0);
					this.ky.transact(8, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void sendMessage_a(String paramString1, String paramString2,
					long paramLong) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeString(paramString1);
					localParcel.writeString(paramString2);
					localParcel.writeLong(paramLong);
					this.ky.transact(9, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void sendBinaryMessage_a(String paramString,
					byte[] paramArrayOfByte, long paramLong)
					throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeString(paramString);
					localParcel.writeByteArray(paramArrayOfByte);
					localParcel.writeLong(paramLong);
					this.ky.transact(10, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void setMessageReceivedCallbacks_S(String paramString)
					throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeString(paramString);
					this.ky.transact(11, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void removeMessageReceivedCallbacks_T(String paramString)
					throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("com.fireflycast.cast.internal.ICastDeviceController");
					localParcel.writeString(paramString);
					this.ky.transact(12, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}
		}
	}

}
