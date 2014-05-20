
package com.fireflycast.server.utils;

import android.content.Context;
import android.os.Binder;

public abstract class C_bcx {
    //static C_bdd a = null;
    private static final Object d = new Object();
    protected final String mKey_b;
    protected final Object mDefaultVal_c;
    private Object e;

    protected C_bcx(String key, Object defaultVal)
    {
        e = null;
        mKey_b = key;
        mDefaultVal_c = defaultVal;
    }

    public static C_bcx a(String key, String defaultAppId)
    {
        // return new C_bdc(s, s1);
        return new C_bcx(key, defaultAppId) {

            @Override
            protected Object a() {
                // TODO Auto-generated method stub
                // return a.a(mKey_b, (String) mDefaultVal_c);
                return mDefaultVal_c;
            }

        };
    }

    public static C_bcx a(String s, boolean flag)
    {
        // return new C_bcy(s, Boolean.valueOf(flag));
        return new C_bcx(s, Boolean.valueOf(flag)) {

            @Override
            protected Object a() {
                // TODO Auto-generated method stub
                // return a.a(mKey_b, (Boolean) mDefaultVal_c);
                return (Boolean) mDefaultVal_c;
            }

        };
    }

    public static void a(Context context)
    {
        /*
        synchronized (d)
        {
            if (a == null)
                a = new C_bde(context.getContentResolver());
        }
        */
    }

    protected abstract Object a();

    public final Object b()
    {
        if (e != null)
        {
            return e;
        } else
        {
            // String = b;
            return a();
        }
    }

    public final Object c()
    {
        Object obj = b();
        long l;
        try {
            l = Binder.clearCallingIdentity();
            Binder.restoreCallingIdentity(l);
        } catch (Exception e) {
            e.printStackTrace();
            // Binder.restoreCallingIdentity(l);
        }
        return obj;
    }

    public final String d()
    {
        return mKey_b;
    }
}
