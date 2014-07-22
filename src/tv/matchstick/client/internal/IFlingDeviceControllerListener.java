package tv.matchstick.client.internal;

import tv.matchstick.fling.ApplicationMetadata;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IFlingDeviceControllerListener extends IInterface {

	public abstract void onDisconnected(int statusCode)
			throws RemoteException;

	public abstract void onApplicationConnected(
			ApplicationMetadata applicationMetadata, String applicationId,
			String sessionId, boolean relaunched) throws RemoteException;

	public abstract void postApplicationConnectionResult(int paramInt)
			throws RemoteException;

	public abstract void notifyApplicationStatusOrVolumeChanged(
			String paramString, double paramDouble, boolean paramBoolean)
			throws RemoteException;

	public abstract void onMessageReceived(String namespace,
			String message) throws RemoteException;

	public abstract void receiveBinary(String paramString,
			byte[] paramArrayOfByte) throws RemoteException;

	public abstract void notifyCallback_C(int paramInt) throws RemoteException;

	public abstract void notifyCallback(int paramInt) throws RemoteException;

	public abstract void onApplicationDisconnected(int statusCode)
			throws RemoteException;

	public abstract void requestCallback(String namespace, long requestId,
			int statusCode) throws RemoteException;

	public abstract void requestCallback(String namespace, long requestId)
			throws RemoteException;

	public static abstract class Stub extends Binder implements
			IFlingDeviceControllerListener {
		public Stub() {
			attachInterface(this,
					"tv.matchstick.fling.internal.IFlingDeviceControllerListener");
		}

		public IBinder asBinder() {
			return this;
		}

		public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
				throws RemoteException {
			boolean relaunched;
			String str1;
			Object localObject;
			int k;
			String str2;
			long l;
			switch (code) {
			case 1598968902:
				reply.writeString("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				return true;
			case 1:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				int i = data.readInt();
				onDisconnected(i);
				return true;
			case 2:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				ApplicationMetadata localApplicationMetadata;
				if (0 != data.readInt())
					localApplicationMetadata = (ApplicationMetadata) ApplicationMetadata.CREATOR
							.createFromParcel(data);
				else
					localApplicationMetadata = null;
				String applicationId = data.readString();
				String sessionId = data.readString();
				relaunched = 0 != data.readInt();
				onApplicationConnected(localApplicationMetadata, applicationId, sessionId,
						relaunched);
				return true;
			case 3:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				int j = data.readInt();
				postApplicationConnectionResult(j);
				return true;
			case 4:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				str1 = data.readString();
				double d = data.readDouble();
				relaunched = 0 != data.readInt();
				notifyApplicationStatusOrVolumeChanged(str1, d, relaunched);
				return true;
			case 5:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				str1 = data.readString();
				localObject = data.readString();
				onMessageReceived(str1, (String) localObject);
				return true;
			case 6:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				str1 = data.readString();
				localObject = data.createByteArray();
				receiveBinary(str1, (byte[]) localObject);
				return true;
			case 7:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				k = data.readInt();
				notifyCallback_C(k);
				return true;
			case 8:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				k = data.readInt();
				notifyCallback(k);
				return true;
			case 9:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				k = data.readInt();
				onApplicationDisconnected(k);
				return true;
			case 10:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				str2 = data.readString();
				l = data.readLong();
				int i1 = data.readInt();
				requestCallback(str2, l, i1);
				return true;
			case 11:
				data.enforceInterface("tv.matchstick.fling.internal.IFlingDeviceControllerListener");
				str2 = data.readString();
				l = data.readLong();
				requestCallback(str2, l);
				return true;
			}
			return super.onTransact(code, data, reply, flags);
		}
	}

}
