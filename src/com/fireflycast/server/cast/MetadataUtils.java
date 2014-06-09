
package com.fireflycast.server.cast;

import android.text.TextUtils;

import com.fireflycast.server.common.images.WebImage;
import com.fireflycast.server.utils.Logs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public final class MetadataUtils {
    private static final Logs mLogs_a = new Logs("MetadataUtils");
    private static final String b[] = {
            "Z", "+hh", "+hhmm", "+hh:mm"
    };
    private static final String Format_c = (new StringBuilder("yyyyMMdd'T'HHmmss")).append(b[0])
            .toString();

    public static String getTime_a(Calendar calendar)
    {
        String s1;
        if (calendar == null)
        {
            mLogs_a.d("Calendar object cannot be null", new Object[0]);
            s1 = null;
        } else
        {
            String s = Format_c;
            if (calendar.get(11) == 0 && calendar.get(12) == 0 && calendar.get(13) == 0)
                s = "yyyyMMdd";
            SimpleDateFormat simpledateformat = new SimpleDateFormat(s);
            simpledateformat.setTimeZone(calendar.getTimeZone());
            s1 = simpledateformat.format(calendar.getTime());
            if (s1.endsWith("+0000"))
                return s1.replace("+0000", b[0]);
        }
        return s1;
    }

    public static Calendar a(String s)
    {
        if (TextUtils.isEmpty(s))
        {
            mLogs_a.d("Input string is empty or null", new Object[0]);
            return null;
        }
        String s1 = b(s);
        if (TextUtils.isEmpty(s1))
        {
            mLogs_a.d("Invalid date format", new Object[0]);
            return null;
        }
        String s2 = c(s);
        String s3 = "yyyyMMdd";
        if (!TextUtils.isEmpty(s2))
        {
            s1 = (new StringBuilder()).append(s1).append("T").append(s2).toString();
            Calendar calendar;
            java.util.Date date;
            if (s2.length() == 6)
                s3 = "yyyyMMdd'T'HHmmss";
            else
                s3 = Format_c;
        }
        Calendar calendar = GregorianCalendar.getInstance();
        Date date;
        try
        {
            date = (new SimpleDateFormat(s3)).parse(s1);
        } catch (ParseException parseexception)
        {
            Logs avu1 = mLogs_a;
            Object aobj[] = new Object[1];
            aobj[0] = parseexception.getMessage();
            avu1.d("Error parsing string: %s", aobj);
            return null;
        }
        calendar.setTime(date);
        return calendar;
    }

    public static void a(List list, JSONObject jsonobject)
    {
        JSONArray jsonarray = null;
        int i = 0;
        int j = 0;
        JSONObject jsonobject1;

        try
        {
            list.clear();
            jsonarray = jsonobject.getJSONArray("images");
            i = jsonarray.length();
        } catch (JSONException e)
        {
            // e.printStackTrace();
            return;
        }

        while (j < i) {
            try
            {
                jsonobject1 = jsonarray.getJSONObject(j);
                list.add(new WebImage(jsonobject1));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            j++;
        }
    }

    public static void fillImages_a(JSONObject jsonobject, List images)
    {
        JSONArray jsonarray;
        if (images == null || images.isEmpty()) {
            return;
        }
        jsonarray = new JSONArray();
        for (Iterator iterator = images.iterator(); iterator.hasNext(); jsonarray
                .put(((WebImage) iterator.next()).cT()))
            ;
        try {
            jsonobject.put("images", jsonarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return;
    }

    private static String b(String s)
    {
        if (TextUtils.isEmpty(s))
        {
            mLogs_a.d("Input string is empty or null", new Object[0]);
            return null;
        }
        String s1;
        try
        {
            s1 = s.substring(0, 8);
        } catch (IndexOutOfBoundsException indexoutofboundsexception)
        {
            Object aobj[] = new Object[1];
            aobj[0] = indexoutofboundsexception.getMessage();
            mLogs_a.i("Error extracting the date: %s", aobj);
            return null;
        }
        return s1;
    }

    private static String c(String s)
    {
        if (TextUtils.isEmpty(s)) {
            mLogs_a.d("string is empty or null", new Object[0]);
            return null;
        }

        String s1;
        int i = s.indexOf('T');
        int j = i + 1;
        if (i != 8)
        {
            mLogs_a.d("T delimeter is not found", new Object[0]);
            return null;
        }
        try
        {
            s1 = s.substring(j);
        } catch (IndexOutOfBoundsException indexoutofboundsexception)
        {
            Object aobj[] = new Object[1];
            aobj[0] = indexoutofboundsexception.getMessage();
            mLogs_a.d("Error extracting the time substring: %s", aobj);
            return null;
        }

        if (s1.length() == 6)
            return s1;

        switch (s1.charAt(6)) {
            case '+': // 43, 2b
            case '-':// 2d
                boolean flag;
                int k = s1.length();
                if (k != 6 + b[1].length() && k != 6 + b[2].length())
                {
                    int l = 6 + b[3].length();
                    flag = false;
                    if (k != l) {
                        if (!flag) {
                            return null;
                        } else {
                            return s1.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
                        }
                    }
                }
                flag = true;
                if (flag) {
                    return s1.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
                }
                break;
            case 'Z':// 5a
                if (s1.length() == 6 + b[0].length()) {
                    return (new StringBuilder()).append(s1.substring(0, -1 + s1.length()))
                            .append("+0000").toString();
                }
                break;
        }

        return null;
    }
}
