
package tv.matchstick.server.fling.bridge;

import android.os.IInterface;
import android.os.RemoteException;

public interface IFlingDeviceController extends IInterface {
    public abstract void disconnect() throws RemoteException;

    public abstract void setVolume(double volume, double d1, boolean flag)
            throws RemoteException;

    public abstract void setMessageReceivedCallbacks(String namespace) throws RemoteException;

    public abstract void joinApplication(String applicationId, String sessionId)
            throws RemoteException;

    public abstract void sendMessage(String namespace, String message, long id)
            throws RemoteException;

    public abstract void launchApplication(String applicationId, boolean relaunchIfRunning)
            throws RemoteException;

    public abstract void sendBinaryMessage(String namespace, byte message[], long requestId) throws RemoteException;

    public abstract void setMute(boolean mute_flag, double d, boolean flag1)
            throws RemoteException;

    public abstract void leaveApplication() throws RemoteException;

    public abstract void stopApplication(String sessionId) throws RemoteException;

    public abstract void requestStatus() throws RemoteException;

    public abstract void removeMessageReceivedCallbacks(String namespace)
            throws RemoteException;
}
