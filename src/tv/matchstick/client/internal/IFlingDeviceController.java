package tv.matchstick.client.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

public interface IFlingDeviceController extends IInterface {

	public void disconnect() throws RemoteException;

	public void launchApplication(String applicationId, boolean relaunchFlag)
			throws RemoteException;

	public void joinApplication(String applicationId, String sessionId)
			throws RemoteException;

	public void leaveApplication() throws RemoteException;

	public void stopApplication(String sessionId) throws RemoteException;

	public void requestStatus() throws RemoteException;

	public void setVolume(double volume, double originalVolume, boolean isMute)
			throws RemoteException;

	public void setMute(boolean mute, double volume, boolean isMute)
			throws RemoteException;

	public void sendMessage_a(String namespace, String message, long requestId)
			throws RemoteException;

	public void setMessageReceivedCallbacks(String namespace)
			throws RemoteException;

	public void removeMessageReceivedCallbacks(String namespace)
			throws RemoteException;

	/*************************************************/
	// unused
	/*************************************************/
	public void sendBinaryMessage(String namespace, byte[] message,
			long requestId) throws RemoteException;

	/*
	 * An implementation of ds interface
	 */
	public static abstract class Stub_a extends Binder implements
			IFlingDeviceController {
		public static IFlingDeviceController asInterface(IBinder obj) {
			if (obj == null)
				return null;
			IInterface local = obj
					.queryLocalInterface("tv.matchstick.fling.internal.IFlingDeviceController");
			if ((local != null) && (local instanceof IFlingDeviceController)) {
				return ((IFlingDeviceController) local);
			}
			return new Proxy(obj);
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			String str1;
			String namespace;
			Object localObject;
			long l;
			switch (code) {
			case 1598968902:
				reply.writeString("tv.matchstick.fling.internal.IFlingDeviceController");
				return true;
			case 1:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				disconnect();
				return true;
			case 2:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				str1 = data.readString();
				boolean bool2 = 0 != data.readInt();
				launchApplication(str1, bool2);
				return true;
			case 3:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				str1 = data.readString();
				String str3 = data.readString();
				joinApplication(str1, str3);
				return true;
			case 4:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				leaveApplication();
				return true;
			case 5:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				str1 = data.readString();
				stopApplication(str1);
				return true;
			case 6:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				requestStatus();
				return true;
			case 7:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				double d1 = data.readDouble();
				double d3 = data.readDouble();
				boolean bool4 = 0 != data.readInt();
				setVolume(d1, d3, bool4);
				return true;
			case 8:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				boolean bool1 = 0 != data.readInt();
				double d2 = data.readDouble();
				boolean bool3 = 0 != data.readInt();
				setMute(bool1, d2, bool3);
				return true;
			case 9:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				namespace = data.readString();
				localObject = data.readString();
				l = data.readLong();
				sendMessage_a(namespace, (String) localObject, l);
				return true;
			case 10:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				namespace = data.readString();
				localObject = data.createByteArray();
				l = data.readLong();
				sendMessage_a(namespace, (String) localObject, l);
				return true;
			case 11:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				namespace = data.readString();
				setMessageReceivedCallbacks(namespace);
				return true;
			case 12:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceController");
				namespace = data.readString();
				removeMessageReceivedCallbacks(namespace);
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}

		private static class Proxy implements IFlingDeviceController {
			private IBinder ky;

			Proxy(IBinder paramIBinder) {
				this.ky = paramIBinder;
			}

			public IBinder asBinder() {
				return this.ky;
			}

			public void disconnect() throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					this.ky.transact(1, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void launchApplication(String paramString,
					boolean paramBoolean) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					localParcel.writeString(paramString);
					localParcel.writeInt((paramBoolean) ? 1 : 0);
					this.ky.transact(2, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void joinApplication(String paramString1,
					String paramString2) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					localParcel.writeString(paramString1);
					localParcel.writeString(paramString2);
					this.ky.transact(3, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void leaveApplication() throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					this.ky.transact(4, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void stopApplication(String paramString)
					throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					localParcel.writeString(paramString);
					this.ky.transact(5, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void requestStatus() throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					this.ky.transact(6, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void setVolume(double paramDouble1, double paramDouble2,
					boolean paramBoolean) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					localParcel.writeDouble(paramDouble1);
					localParcel.writeDouble(paramDouble2);
					localParcel.writeInt((paramBoolean) ? 1 : 0);
					this.ky.transact(7, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void setMute(boolean paramBoolean1, double paramDouble,
					boolean paramBoolean2) throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
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
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					localParcel.writeString(paramString1);
					localParcel.writeString(paramString2);
					localParcel.writeLong(paramLong);
					this.ky.transact(9, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void sendBinaryMessage(String namespace,
					byte[] message, long requestId)
					throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					localParcel.writeString(namespace);
					localParcel.writeByteArray(message);
					localParcel.writeLong(requestId);
					this.ky.transact(10, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void setMessageReceivedCallbacks(String namespace)
					throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					localParcel.writeString(namespace);
					this.ky.transact(11, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}

			public void removeMessageReceivedCallbacks(String paramString)
					throws RemoteException {
				Parcel localParcel = Parcel.obtain();
				try {
					localParcel
							.writeInterfaceToken("tv.matchstick.fling.internal.IFlingDeviceController");
					localParcel.writeString(paramString);
					this.ky.transact(12, localParcel, null, 1);
				} finally {
					localParcel.recycle();
				}
			}
		}
	}

}
