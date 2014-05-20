
package com.fireflycast.server.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

public class Gservices_gsh {
    public static final Uri a = Uri
            .parse("content://com.google.android.gsf.gservices");
    public static final Uri b = Uri
            .parse("content://com.google.android.gsf.gservices/prefix");
    public static final Pattern c = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
    public static final Pattern d = Pattern
            .compile("^(0|false|f|off|no|n)$", 2);
    private static HashMap e;
    private static Object f;
    private static String[] g = new String[0];
    private static Context h = null;

    public static int a(ContentResolver paramContentResolver,
            String paramString, int paramInt) {
        String str = a(paramContentResolver, paramString, null);
        if (str == null) {
            return paramInt;
        }
        try {
            int i = Integer.parseInt(str);
            paramInt = i;
            return paramInt;
        } catch (NumberFormatException localNumberFormatException) {
        }
        return paramInt;
    }

    public static long a(ContentResolver paramContentResolver,
            String paramString, long paramLong) {
        String str = a(paramContentResolver, paramString, null);
        if (str == null) {
            return paramLong;
        }

        try {
            long l = Long.parseLong(str);
            paramLong = l;
            return paramLong;
        } catch (NumberFormatException localNumberFormatException) {
        }
        return paramLong;
    }

    public static Object a(ContentResolver paramContentResolver) {
        c();
        b(paramContentResolver);
        return f;
    }

    static Object a(Object paramObject) {
        f = paramObject;
        return paramObject;
    }

    public static String a(ContentResolver paramContentResolver,
            String paramString) {
        return a(paramContentResolver, paramString, null);
    }

    public static String a(ContentResolver paramContentResolver,
            String paramString1, String paramString2) {

        return paramString2;
        
//        c();
//        while (true) {
//            Object localObject2;
//            try {
//                b(paramContentResolver);
//                localObject2 = f;
//                if (e.containsKey(paramString1)) {
//                    String str2 = (String) e.get(paramString1);
//                    if (str2 != null)
//                        paramString2 = str2;
//                    return paramString2;
//                }
//                String[] arrayOfString = g;
//                int i = arrayOfString.length;
//                int j = 0;
//                while (j < i) {
//                    if (paramString1.startsWith(arrayOfString[j])) {
//                        return paramString2;
//                    }
//                    j++;
//                }
//            } finally {
//            }
//            Cursor localCursor = paramContentResolver.query(a, null, null,
//                    new String[] {
//                        paramString1
//                    }, null);
//            if (localCursor == null) {
//                e.put(paramString1, null);
//                return paramString2;
//            }
//
//            try {
//                if (!localCursor.moveToFirst()) {
//                    e.put(paramString1, null);
//                    return paramString2;
//                }
//                String str1 = localCursor.getString(1);
//                try {
//                    if (localObject2 == f)
//                        e.put(paramString1, str1);
//                    if (str1 != null)
//                        paramString2 = str1;
//                    return paramString2;
//                } finally {
//                }
//            } finally {
//                if (localCursor != null)
//                    localCursor.close();
//            }
//        }
    }

    static HashMap a() {
        return e;
    }

    public static Map a(ContentResolver paramContentResolver,
            String[] paramArrayOfString) {

        c();
        Cursor localCursor = paramContentResolver.query(b, null, null,
                paramArrayOfString, null);
        TreeMap localTreeMap = new TreeMap();
        if (localCursor == null)
            return localTreeMap;
        try {
            if (localCursor.moveToNext())
                localTreeMap.put(localCursor.getString(0),
                        localCursor.getString(1));
        } finally {
            localCursor.close();
        }
        return localTreeMap;
    }

    public static void a(Context paramContext) {
        h = paramContext.getApplicationContext();
    }

    public static boolean a(ContentResolver paramContentResolver,
            String paramString, boolean paramBoolean) {
        String str = a(paramContentResolver, paramString, null);
        if ((str == null) || (str.equals("")))
            return paramBoolean;
        if (c.matcher(str).matches())
            return true;
        if (d.matcher(str).matches())
            return false;
        Log.w("Gservices", "attempt to read gservices key " + paramString
                + " (value \"" + str + "\") as boolean");
        return paramBoolean;
    }

    private static void b(final ContentResolver resolver) {
        if (e == null) {
            e = new HashMap();
            f = new Object();
            // new C_gsi("Gservices", paramContentResolver).start();

            new Thread() {
                public final void run()
                {
                    Looper.prepare();
                    // mContentResolver_a.registerContentObserver(Gservices_gsh.a,
                    // true, new
                    // C_gsj(this, new Handler(Looper.myLooper())));

                    resolver.registerContentObserver(Gservices_gsh.a, true,
                            new ContentObserver(
                                    new Handler(Looper.myLooper())) {
                                public final void onChange(boolean flag)
                                {
                                    e.clear();
                                    f = new Object();
                                    if (g.length > 0) {
                                        Gservices_gsh.b(resolver, g);
                                    }

                                    return;
                                }
                            });
                    Looper.loop();
                }
            }.start();
        }
    }

    public static void b(ContentResolver paramContentResolver,
            String[] paramArrayOfString) {
        c();
        Map localMap = a(paramContentResolver, paramArrayOfString);
        try {
            b(paramContentResolver);
            g = paramArrayOfString;
            Iterator localIterator = localMap.entrySet().iterator();
            while (localIterator.hasNext()) {
                Map.Entry localEntry = (Map.Entry) localIterator.next();
                e.put(localEntry.getKey(), localEntry.getValue());
            }
        } finally {
        }
    }

    static String[] b() {
        return g;
    }

    private static void c() {
        if (h != null)
            h.enforceCallingOrSelfPermission(
                    "com.google.android.providers.gsf.permission.READ_GSERVICES",
                    "attempting to read gservices without permission");
    }
}
