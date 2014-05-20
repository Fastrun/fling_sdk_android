
package com.fireflycast.server.cast.bridge;

import android.os.IInterface;
import android.os.RemoteException;

public interface ICastDeviceController_avp extends IInterface {
    public abstract void disconnect_a() throws RemoteException;

    public abstract void setVolume_a(double volume_d, double d1, boolean flag)
            throws RemoteException;

    public abstract void setMessageReceivedCallbacks_b(String namespace_s) throws RemoteException;

    public abstract void joinApplication_a(String applicationId_s, String sessionId_s1)
            throws RemoteException;

    public abstract void sendMessage_a(String namespace_s, String message_s1, long id_l)
            throws RemoteException;

    public abstract void launchApplication_a(String applicationId_s, boolean relaunchIfRunning_flag)
            throws RemoteException;

    public abstract void sendBinaryMessage_a(String s, byte abyte0[], long l) throws RemoteException;

    public abstract void setMute_a(boolean mute_flag, double d, boolean flag1)
            throws RemoteException;

    public abstract void leaveApplication_b() throws RemoteException;

    public abstract void stopApplication_a(String sessionId_s) throws RemoteException;

    public abstract void requestStatus_c() throws RemoteException;

    public abstract void removeMessageReceivedCallbacks_c(String namespace_s)
            throws RemoteException;
}
