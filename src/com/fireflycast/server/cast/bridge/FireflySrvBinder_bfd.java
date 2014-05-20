
package com.fireflycast.server.cast.bridge;

import android.os.Bundle;
import android.os.IBinder;

public abstract class FireflySrvBinder_bfd extends FireflyServiceBroker_bgn {
    public FireflySrvBinder_bfd()
    {
    }

    public void a(IFireflyCallbacks_bgj bgj, int i1)
    {
        throw new IllegalArgumentException("Wallet service not supported");
    }

    public void a(IFireflyCallbacks_bgj bgj, int i1, String s)
    {
        throw new IllegalArgumentException("AppDataSearch service not supported.");
    }

    public void a(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("AdMob service not supported");
    }

    public void initCastService_a(IFireflyCallbacks_bgj bgj, int i1, String s, IBinder ibinder,
            Bundle bundle)
    {
        throw new IllegalArgumentException("Cast service not supported");
    }

    public void a(IFireflyCallbacks_bgj bgj, int i1, String s, String s1, String as[])
    {
        throw new IllegalArgumentException("AppState service not supported.");
    }

    public void a(IFireflyCallbacks_bgj bgj, int i1, String s, String s1, String as[], String s2,
            Bundle bundle)
    {
        throw new IllegalArgumentException("Plus service not supported.");
    }

    public void a(IFireflyCallbacks_bgj bgj, int i1, String s, String s1, String as[], String s2,
            IBinder ibinder,
            String s3, Bundle bundle)
    {
        throw new IllegalArgumentException("Games service not supported.");
    }

    public void a(IFireflyCallbacks_bgj bgj, int i1, String s, String as[], String s1, Bundle bundle)
    {
        throw new IllegalArgumentException("Drive service not supported.");
    }

    public void b(IFireflyCallbacks_bgj bgj, int i1, String s)
    {
        throw new IllegalArgumentException("LightweightAppDataSearch service not supported.");
    }

    public void b(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("Panorama service not supported.");
    }

    public void c(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("People service not supported");
    }

    public void d(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("Reporting service not supported");
    }

    public final void e(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("Location service not supported");
    }

    public void f(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("Google location manager service not supported");
    }

    public void g(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("PlayLog service not supported");
    }

    public void h(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("DroidGuard service not supported");
    }

    public final void i(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("Lockbox sevice not supported");
    }

    public final void j(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("CastMirroring service not supported");
    }

    public void k(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("Network quality sevice not supported");
    }

    public void l(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("Google identity service not supported");
    }

    public void m(IFireflyCallbacks_bgj bgj, int i1, String s, Bundle bundle)
    {
        throw new IllegalArgumentException("Google feedback service not supported");
    }
}
