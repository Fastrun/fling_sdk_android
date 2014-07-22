package tv.matchstick.client.internal;

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

import tv.matchstick.fling.images.WebImage;
import android.text.TextUtils;

public class MetadataUtils {

	private static final LogUtil mLogUtil = new LogUtil(
			"MetadataUtils");
	private static final String[] yE = { "Z", "+hh", "+hhmm", "+hh:mm" };
	private static final String DEFAULT_FORMAT = "yyyyMMdd'T'HHmmss" + yE[0];

	public static void getWebImageListFromJson(List<WebImage> webImages,
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

	public static void writeToJson(JSONObject data, List<WebImage> webImages) {
		if ((webImages == null) || (webImages.isEmpty())) {
			return;
		}
		JSONArray jsonArr = new JSONArray();
		Iterator<WebImage> it = webImages.iterator();
		while (it.hasNext()) {
			WebImage image = (WebImage) it.next();
			jsonArr.put(image.buildJson());
		}
		try {
			data.put("images", jsonArr);
		} catch (JSONException localJSONException) {
		}
	}

	public static String getDateByCalendar(Calendar calendar) {
		if (calendar == null) {
			mLogUtil.logd("Calendar object cannot be null", new Object[0]);
			return null;
		}
		String format = DEFAULT_FORMAT;
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

	public static Calendar getCalendarByDate(String dateString) {
		if (TextUtils.isEmpty(dateString)) {
			mLogUtil.logd("Input string is empty or null", new Object[0]);
			return null;
		}
		String date = extractDate(dateString);
		if (TextUtils.isEmpty(date)) {
			mLogUtil.logd("Invalid date format", new Object[0]);
			return null;
		}
		String time = extractTime(dateString);
		String str3 = "yyyyMMdd";
		if (!(TextUtils.isEmpty(time))) {
			date = date + "T" + time;
			if (time.length() == "HHmmss".length())
				str3 = "yyyyMMdd'T'HHmmss";
			else
				str3 = DEFAULT_FORMAT;
		}
		Calendar localCalendar = GregorianCalendar.getInstance();
		Date localDate;
		try {
			localDate = new SimpleDateFormat(str3).parse(date);
		} catch (ParseException localParseException) {
			mLogUtil.logd("Error parsing string: %s",
					new Object[] { localParseException.getMessage() });
			return null;
		}
		localCalendar.setTime(localDate);
		return localCalendar;
	}

	private static String extractDate(String date) {
		if (TextUtils.isEmpty(date)) {
			mLogUtil.logd("Input string is empty or null", new Object[0]);
			return null;
		}
		try {
			return date.substring(0, "yyyyMMdd".length());
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			mLogUtil
					.logi("Error extracting the date: %s",
							new Object[] { localIndexOutOfBoundsException
									.getMessage() });
		}
		return null;
	}

	private static String extractTime(String date) {
		if (TextUtils.isEmpty(date)) {
			mLogUtil.logd("string is empty or null", new Object[0]);
			return null;
		}
		int i = date.indexOf(84);
		if (i++ != "yyyyMMdd".length()) {
			mLogUtil.logd("T delimeter is not found", new Object[0]);
			return null;
		}
		String str;
		try {
			str = date.substring(i);
		} catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
			mLogUtil
					.logd("Error extracting the time substring: %s",
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
