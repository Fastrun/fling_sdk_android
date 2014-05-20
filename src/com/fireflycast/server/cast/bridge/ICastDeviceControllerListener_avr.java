
package com.fireflycast.server.cast.bridge;

import android.os.IInterface;
import android.os.RemoteException;

import com.fireflycast.server.cast.ApplicationMetadata;

public interface ICastDeviceControllerListener_avr extends IInterface {
    public abstract void onDisconnected_a(int i) throws RemoteException;

    public abstract void onApplicationConnected_a(ApplicationMetadata applicationmetadata,
            String applicationId, String sessionId, boolean relaunched) throws RemoteException;

    public abstract void notifyApplicationStatusOrVolumeChanged_a(String status, double volume,
            boolean muted) throws RemoteException;

    public abstract void a(String s, long l) throws RemoteException;

    public abstract void a(String s, long l, int i) throws RemoteException;

    public abstract void onMessageReceived_a(String s, String s1) throws RemoteException;

    public abstract void a(String s, byte abyte0[]) throws RemoteException;

    public abstract void postApplicationConnectionResult_b(int i) throws RemoteException;

    public abstract void onRequestResult_c(int i) throws RemoteException;

    public abstract void onRequestStatus_d(int i) throws RemoteException;

    public abstract void onApplicationDisconnected_e(int i) throws RemoteException;
}
