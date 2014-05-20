
package com.fireflycast.server.utils;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class C_bmg {
    private static final Pattern a = Pattern.compile("\\\\.");
    private static final Pattern b = Pattern.compile("[\\\\\"/\b\f\n\r\t]");

    public static String a(String s)
    {
        if (TextUtils.isEmpty(s)) {
            return s;
        }

        Matcher matcher;
        StringBuffer stringbuffer = null;
        s = C_bmr.a(s);
        matcher = a.matcher(s);

        while (matcher.find())
        {
            if (stringbuffer == null) {
                stringbuffer = new StringBuffer();
            }
            switch (matcher.group().charAt(1))
            {
                default:
                    throw new IllegalStateException(
                            "Found an escaped character that should never be.");

                case 34: // '"'
                    matcher.appendReplacement(stringbuffer, "\"");
                    break;

                case 92: // '\\'
                    matcher.appendReplacement(stringbuffer, "\\\\");
                    break;

                case 47: // '/'
                    matcher.appendReplacement(stringbuffer, "/");
                    break;

                case 98: // 'b'
                    matcher.appendReplacement(stringbuffer, "\b");
                    break;

                case 102: // 'f'
                    matcher.appendReplacement(stringbuffer, "\f");
                    break;

                case 110: // 'n'
                    matcher.appendReplacement(stringbuffer, "\n");
                    break;

                case 114: // 'r'
                    matcher.appendReplacement(stringbuffer, "\r");
                    break;

                case 116: // 't'
                    matcher.appendReplacement(stringbuffer, "\t");
                    break;
            }
        }

        if (stringbuffer != null) {
            matcher.appendTail(stringbuffer);
            return stringbuffer.toString();
        }

        return s;
    }

    public static boolean a(Object obj, Object obj1)
    {
        JSONArray jsonarray = null;
        JSONArray jsonarray1 = null;
        int i = 0;

        boolean flag = false;
        boolean flag1 = false;
        if (!(obj instanceof JSONObject) || !(obj1 instanceof JSONObject)) {
            if (!(obj instanceof JSONArray) || !(obj1 instanceof JSONArray)) {
                return obj.equals(obj1);
            }
            jsonarray = (JSONArray) obj;
            jsonarray1 = (JSONArray) obj1;
            if (jsonarray.length() != jsonarray1.length()) {
                return false;
            }
            i = 0;
            do
            {
                if (i >= jsonarray.length())
                    break; /* Loop/switch isn't completed */
                try
                {
                    flag = a(jsonarray.get(i), jsonarray1.get(i));
                }
                // Misplaced declaration of an exception variable
                catch (JSONException e)
                {
                    return false;
                }
                if (!flag) {
                    return false;
                }
                i++;
            } while (true);
            return true;
        } else {
            JSONObject jsonobject;
            JSONObject jsonobject1;
            jsonobject = (JSONObject) obj;
            jsonobject1 = (JSONObject) obj1;
            if (jsonobject.length() == jsonobject1.length()) {
                for (Iterator iterator = jsonobject.keys(); iterator.hasNext();)
                {
                    String s = (String) iterator.next();
                    if (!jsonobject1.has(s)) {
                        return false;
                    }
                    try
                    {
                        flag1 = a(jsonobject.get(s), jsonobject1.get(s));
                    } catch (JSONException jsonexception1)
                    {
                        return false;
                    }
                    if (!flag1)
                        return false;
                }

                return true;
            }
        }

        return false;
    }

    public static String b(String s)
    {
        if (TextUtils.isEmpty(s)) {
            return s;
        }

        Matcher matcher;
        StringBuffer stringbuffer;
        matcher = b.matcher(s);
        stringbuffer = null;

        while (matcher.find())
        {
            if (stringbuffer == null)
                stringbuffer = new StringBuffer();
            switch (matcher.group().charAt(0))
            {
                case 8: // '\b'
                    matcher.appendReplacement(stringbuffer, "\\\\b");
                    break;

                case 34: // '"'
                    matcher.appendReplacement(stringbuffer, "\\\\\\\"");
                    break;

                case 92: // '\\'
                    matcher.appendReplacement(stringbuffer, "\\\\\\\\");
                    break;

                case 47: // '/'
                    matcher.appendReplacement(stringbuffer, "\\\\/");
                    break;

                case 12: // '\f'
                    matcher.appendReplacement(stringbuffer, "\\\\f");
                    break;

                case 10: // '\n'
                    matcher.appendReplacement(stringbuffer, "\\\\n");
                    break;

                case 13: // '\r'
                    matcher.appendReplacement(stringbuffer, "\\\\r");
                    break;

                case 9: // '\t'
                    matcher.appendReplacement(stringbuffer, "\\\\t");
                    break;
            }
        }

        if (stringbuffer == null) {
            return s;
        }

        matcher.appendTail(stringbuffer);
        return stringbuffer.toString();
    }
}
