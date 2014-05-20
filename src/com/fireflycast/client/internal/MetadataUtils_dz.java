package com.fireflycast.client.internal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

import com.fireflycast.cast.images.WebImage;

public class MetadataUtils_dz {

	private static final LogUtil_du mLogUtil_xE = new LogUtil_du(
			"MetadataUtils");
	private static final String[] yE = { "Z", "+hh", "+hhmm", "+hh:mm" };
	private static final String DEFAULT_FORMAT_yF = "yyyyMMdd'T'HHmmss" + yE[0];

	public static void getWebImageListFromJson_a(List<WebImage> webImages,
			JSONObject data) {
		try {
			webImages.clear();
			JSONArray images = data.getJSONArray("images");
			int i = images.length();
			for (int j = 0; j < i; ++j) {
				JSONObject image = images.getJSONObject(j);
				try {
					webImages.add(new WebImage(image));
				} catch (IllegalArgumentException e) {
				}
			}
		} catch (JSONException e) {
		}
	}

	public static void writeToJson_a(JSONObject data, List<WebImage> webImages) {
		if ((webImages == null) || (webImages.isEmpty())) {
			return;
		}
		JSONArray jsonArr = new JSONArray();
		Iterator<WebImage> it = webImages.iterator();
		while (it.hasNext()) {
			WebImage image = (WebImage) it.next();
			jsonArr.put(image.buildJson_cT());
		}
		try {
			data.put("images", jsonArr);
		} catch (JSONException localJSONException) {
		}
	}

	public static String getDateByCalendar_a(Calendar calendar) {
		if (calendar == null) {
			mLogUtil_xE.logd_b("Calendar object cannot be null", new Object[0]);
			return null;
		}
		String format = DEFAULT_FORMAT_yF;
		if ((calendar.get(11) == 0) && (calendar.get(12) == 0)
				&& (calendar.get(13) == 0)) {
			format = "yyyyMMdd";
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		simpleDateFormat.setTimeZone(calendar.getTimeZone());
		String date = simpleDateFormat.format(calendar.getTime());
		if (date.endsWith("+0000")) {
			date = date.replace("+0000", yE[0]);
		}
		return date;
	}

	public static Calendar getCalendarByDate_V(String dateString) {
		if (TextUtils.isEmpty(dateString)) {
			mLogUtil_xE.logd_b("Input string is empty or null", new Object[0]);
			return null;
		}
		String date = extractDate_W(dateString);
		if (TextUtils.isEmpty(date)) {
			mLogUtil_xE.logd_b("Invalid date format", new Object[0]);
			return null;
		}
		String time = extractTime_X(dateString);
		String str3 = "yyyyMMdd";
		if (!(TextUtils.isEmpty(time))) {
			date = date + "T" + time;
			if (time.length() == "HHmmss".length())
				str3 = "yyyyMMdd'T'HHmmss";
			else
				str3 = DEFAULT_FORMAT_yF;
		}
		Calendar localCalendar = GregorianCalendar.getInstance();
		Date localDate;
		try {
			localDate = new SimpleDateFormat(str3).parse(date);
		} catch (ParseException localParseException) {
			mLogUtil_xE.logd_b("Error parsing string: %s",
					new Object[] { localParseException.getMessage() });
			return null;
		}
		localCalendar.setTime(localDate);
		return localCalendar;
	}

	private static String extractDate_W(String date) {
		if (TextUtils.isEmpty(date)) {
			mLogUtil_xE.logd_b("Input string is empty or null", new Object[0]);
			return null;
		}
		try {
			return date.substring(0, "yyyyMMdd".length());
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			mLogUtil_xE
					.logi_c("Error extracting the date: %s",
							new Object[] { localIndexOutOfBoundsException
									.getMessage() });
		}
		return null;
	}

	private static String extractTime_X(String date) {
		if (TextUtils.isEmpty(date)) {
			mLogUtil_xE.logd_b("string is empty or null", new Object[0]);
			return null;
		}
		int i = date.indexOf(84);
		if (i++ != "yyyyMMdd".length()) {
			mLogUtil_xE.logd_b("T delimeter is not found", new Object[0]);
			return null;
		}
		String str;
		try {
			str = date.substring(i);
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			mLogUtil_xE
					.logd_b("Error extracting the time substring: %s",
							new Object[] { localIndexOutOfBoundsException
									.getMessage() });
			return null;
		}
		if (str.length() == "HHmmss".length())
			return str;
		int j = str.charAt("HHmmss".length());
		switch (j) {
		case 90:
			if (str.length() == "HHmmss".length() + yE[0].length())
				return str.substring(0, str.length() - 1) + "+0000";
			return null;
		case 43:
		case 45:
			if (!(Y(str))) {
				break;
			}
			return str.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
		}
		return null;
	}

	private static boolean Y(String paramString) {
		int i = paramString.length();
		int j = "HHmmss".length();
		return ((i == j + yE[1].length()) || (i == j + yE[2].length()) || (i == j
				+ yE[3].length()));
	}

}
