package com.fireflycast.client.internal;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import com.fireflycast.cast.ApplicationMetadata;

public interface ICastDeviceControllerListener_dt extends IInterface {

	public abstract void onDisconnected_z(int statusCode)
			throws RemoteException;

	public abstract void onApplicationConnected_a(
			ApplicationMetadata applicationMetadata, String applicationId,
			String sessionId, boolean relaunched) throws RemoteException;

	public abstract void postApplicationConnectionResult_A(int paramInt)
			throws RemoteException;

	public abstract void notifyApplicationStatusOrVolumeChanged_b(
			String paramString, double paramDouble, boolean paramBoolean)
			throws RemoteException;

	public abstract void onMessageReceived_d(String paramString1,
			String paramString2) throws RemoteException;

	public abstract void receiveBinary_b(String paramString,
			byte[] paramArrayOfByte) throws RemoteException;

	public abstract void notifyCallback_C(int paramInt) throws RemoteException;

	public abstract void notifyCallback_B(int paramInt) throws RemoteException;

	public abstract void onApplicationDisconnected(int statusCode)
			throws RemoteException;

	public abstract void requestCallback_a(String namespace, long requestId,
			int statusCode) throws RemoteException;

	public abstract void requestCallback_a(String namespace, long requestId)
			throws RemoteException;

	public static abstract class Stub_a extends Binder implements
			ICastDeviceControllerListener_dt {
		public Stub_a() {
			attachInterface(this,
					"com.fireflycast.cast.internal.ICastDeviceControllerListener");
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			boolean bool;
			String str1;
			Object localObject;
			int k;
			String str2;
			long l;
			switch (code) {
			case 1598968902:
				reply.writeString("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				return true;
			case 1:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				int i = data.readInt();
				onDisconnected_z(i);
				return true;
			case 2:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				ApplicationMetadata localApplicationMetadata;
				if (0 != data.readInt())
					localApplicationMetadata = (ApplicationMetadata) ApplicationMetadata.CREATOR
							.createFromParcel(data);
				else
					localApplicationMetadata = null;
				String str3 = data.readString();
				String str4 = data.readString();
				bool = 0 != data.readInt();
				onApplicationConnected_a(localApplicationMetadata, str3, str4,
						bool);
				return true;
			case 3:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				int j = data.readInt();
				postApplicationConnectionResult_A(j);
				return true;
			case 4:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				str1 = data.readString();
				double d = data.readDouble();
				bool = 0 != data.readInt();
				notifyApplicationStatusOrVolumeChanged_b(str1, d, bool);
				return true;
			case 5:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				str1 = data.readString();
				localObject = data.readString();
				onMessageReceived_d(str1, (String) localObject);
				return true;
			case 6:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				str1 = data.readString();
				localObject = data.createByteArray();
				receiveBinary_b(str1, (byte[]) localObject);
				return true;
			case 7:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				k = data.readInt();
				notifyCallback_C(k);
				return true;
			case 8:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				k = data.readInt();
				notifyCallback_B(k);
				return true;
			case 9:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				k = data.readInt();
				onApplicationDisconnected(k);
				return true;
			case 10:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				str2 = data.readString();
				l = data.readLong();
				int i1 = data.readInt();
				requestCallback_a(str2, l, i1);
				return true;
			case 11:
				data.enforceInterface("com.fireflycast.cast.internal.ICastDeviceControllerListener");
				str2 = data.readString();
				l = data.readLong();
				requestCallback_a(str2, l);
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}
	}

}
