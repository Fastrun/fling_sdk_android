package com.fireflycast.client.internal;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonComparer_fp {

	public static boolean compare_d(Object obj1, Object obj2) {
		if ((obj1 instanceof JSONObject) && (obj2 instanceof JSONObject)) {
			JSONObject json1 = (JSONObject) obj1;
			JSONObject json2 = (JSONObject) obj2;
			if (json1.length() != json2.length()) {
				return false;
			}
			Iterator json1Keys = json1.keys();
			while (json1Keys.hasNext()) {
				String key = (String) json1Keys.next();
				if (!(json2.has(key))) {
					return false;
				}
				try {
					Object tmp1 = json1.get(key);
					Object tmp2 = json2.get(key);
					if (!compare_d(tmp1, tmp2)) {
						return false;
					}
				} catch (JSONException e) {
					return false;
				}
			}
			return true;
		}
		if ((obj1 instanceof JSONArray) && (obj2 instanceof JSONArray)) {
			JSONArray jArr1 = (JSONArray) obj1;
			JSONArray jArr2 = (JSONArray) obj2;
			if (jArr1.length() != jArr2.length()) {
				return false;
			}
			for (int i = 0; i < jArr1.length(); ++i) {
				try {
					Object tmp1 = jArr1.get(i);
					Object tmp2 = jArr2.get(i);
					if (!compare_d(tmp1, tmp2)) {
						return false;
					}
				} catch (JSONException e) {
					return false;
				}
			}
			return true;
		}
		return obj1.equals(obj2);
	}
}
