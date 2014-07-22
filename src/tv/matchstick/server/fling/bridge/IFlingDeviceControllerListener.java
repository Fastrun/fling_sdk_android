
package tv.matchstick.server.fling.bridge;

import tv.matchstick.server.fling.ApplicationMetadata;
import android.os.IInterface;
import android.os.RemoteException;

/**
 * Fling device control listener
 */
public interface IFlingDeviceControllerListener extends IInterface {
    public abstract void onDisconnected(int statusCode) throws RemoteException;

    public abstract void onApplicationConnected(ApplicationMetadata applicationmetadata,
            String applicationId, String sessionId, boolean relaunched) throws RemoteException;

    public abstract void notifyApplicationStatusOrVolumeChanged(String status, double volume,
            boolean muted) throws RemoteException;

    public abstract void requestCallback(String namespace, long requestId) throws RemoteException;

    public abstract void requestCallback(String namespace, long requestId, int result) throws RemoteException;

    public abstract void onMessageReceived(String namespace, String message) throws RemoteException;

    public abstract void onReceiveBinary(String s, byte abyte0[]) throws RemoteException;

    public abstract void postApplicationConnectionResult(int i) throws RemoteException;

    public abstract void onRequestResult(int i) throws RemoteException;

    public abstract void onRequestStatus(int result) throws RemoteException;

    public abstract void onApplicationDisconnected(int i) throws RemoteException;
}
