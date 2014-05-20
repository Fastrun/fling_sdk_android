
package com.fireflycast.server.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class C_bmr {
    private static final Pattern a = Pattern.compile("\\\\u[0-9a-fA-F]{4}");

    public static String a(String s)
    {
        Matcher matcher;
        StringBuffer stringbuffer;
        label0:
        {
            if (!TextUtils.isEmpty(s))
            {
                matcher = a.matcher(s);
                stringbuffer = null;
                for (; matcher.find(); matcher.appendReplacement(
                        stringbuffer,
                        new String(Character.toChars(Integer.parseInt(matcher.group().substring(2),
                                16)))))
                    if (stringbuffer == null)
                        stringbuffer = new StringBuffer();

                if (stringbuffer != null)
                    break label0;
            }
            return s;
        }
        matcher.appendTail(stringbuffer);
        return stringbuffer.toString();
    }

}
