
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;

public interface IFireflyServiceBroker_bgm extends IInterface {
    public abstract void a(IFireflyCallbacks_bgj bgj, int i1) throws RemoteException;

    public abstract void a(IFireflyCallbacks_bgj bgj, int i1, String s) throws RemoteException;

    public abstract void a(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void initCastService_a(IFireflyCallbacks_bgj bgj, int i1, String s,
            IBinder ibinder, Bundle bundle) throws RemoteException;

    public abstract void a(IFireflyCallbacks_bgj bgj, int i1, String s, String s1, String as[])
            throws RemoteException;

    public abstract void a(IFireflyCallbacks_bgj bgj, int i1, String s, String s1, String as[],
            String s2, Bundle bundle) throws RemoteException;

    public abstract void a(IFireflyCallbacks_bgj bgj, int i1, String s, String s1, String as[],
            String s2, IBinder ibinder,
            String s3, Bundle bundle) throws RemoteException;

    public abstract void a(IFireflyCallbacks_bgj bgj, int i1, String s, String as[], String s1,
            Bundle bundle) throws RemoteException;

    public abstract void b(IFireflyCallbacks_bgj bgj, int i1, String s) throws RemoteException;

    public abstract void b(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void c(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void d(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void e(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void f(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void g(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void h(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void i(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void j(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void k(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void l(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;

    public abstract void m(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
            throws RemoteException;
}
