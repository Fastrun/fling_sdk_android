package com.fireflycast.cast;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.Iterator;

import android.text.TextUtils;

/*
 * CastMediaControlIntent.smali : OK
 */
public class CastMediaControlIntent {
	public static final String CATEGORY_CAST = "com.fireflycast.cast.CATEGORY_CAST";
	public static final String CATEGORY_REMOTE_PLAYBACK = "com.fireflycast.cast.CATEGORY_CAST_REMOTE_PLAYBACK";
	public static final String DEFAULT_MEDIA_RECEIVER_APPLICATION_ID = "CC1AD845";
	public static final String ACTION_SYNC_STATUS = "com.fireflycast.cast.ACTION_SYNC_STATUS";
	public static final String EXTRA_CUSTOM_DATA = "com.fireflycast.cast.EXTRA_CUSTOM_DATA";
	public static final String EXTRA_CAST_APPLICATION_ID = "com.fireflycast.cast.EXTRA_CAST_APPLICATION_ID";
	public static final String EXTRA_CAST_RELAUNCH_APPLICATION = "com.fireflycast.cast.EXTRA_CAST_RELAUNCH_APPLICATION";
	public static final String EXTRA_CAST_STOP_APPLICATION_WHEN_SESSION_ENDS = "com.fireflycast.cast.EXTRA_CAST_STOP_APPLICATION_WHEN_SESSION_ENDS";
	public static final String EXTRA_DEBUG_LOGGING_ENABLED = "com.fireflycast.cast.EXTRA_DEBUG_LOGGING_ENABLED";
	public static final String EXTRA_ERROR_CODE = "com.fireflycast.cast.EXTRA_ERROR_CODE";
	public static final int ERROR_CODE_REQUEST_FAILED = 1;
	public static final int ERROR_CODE_SESSION_START_FAILED = 2;
	public static final int ERROR_CODE_TEMPORARILY_DISCONNECTED = 3;

	public static String categoryForRemotePlayback(String applicationId)
			throws IllegalArgumentException {
		if (TextUtils.isEmpty(applicationId)) {
			throw new IllegalArgumentException(
					"applicationId cannot be null or empty");
		}
		return buildCategory_a(CATEGORY_REMOTE_PLAYBACK, applicationId, null);
	}

	public static String categoryForRemotePlayback() {
		return buildCategory_a(CATEGORY_REMOTE_PLAYBACK, null, null);
	}

	public static String categoryForCast(String applicationId)
			throws IllegalArgumentException {
		if (applicationId == null) {
			throw new IllegalArgumentException("applicationId cannot be null");
		}
		return buildCategory_a(CATEGORY_CAST, applicationId, null);
	}

	public static String categoryForCast(Collection<String> namespaces)
			throws IllegalArgumentException {
		if (namespaces == null) {
			throw new IllegalArgumentException("namespaces cannot be null");
		}
		return buildCategory_a(CATEGORY_CAST, null, namespaces);
	}

	public static String categoryForCast(String applicationId,
			Collection<String> namespaces) {
		if (applicationId == null) {
			throw new IllegalArgumentException("applicationId cannot be null");
		}
		if (namespaces == null) {
			throw new IllegalArgumentException("namespaces cannot be null");
		}
		return buildCategory_a(CATEGORY_CAST, applicationId, namespaces);
	}

	private static String buildCategory_a(String category,
			String applicationId, Collection<String> namespaces)
			throws IllegalArgumentException {
		StringBuffer sb = new StringBuffer(category);
		if (applicationId != null) {
//			if (!(applicationId.matches("[A-F0-9]+"))) {
//				throw new IllegalArgumentException("Invalid appliation ID: "
//						+ applicationId);
//			}
		    String encode = URLEncoder.encode(applicationId);
			sb.append("/").append(encode);
		}
		if (namespaces != null) {
			if (namespaces.isEmpty()) {
				throw new IllegalArgumentException(
						"Must specify at least one namespace");
			}
			Iterator<String> nss = namespaces.iterator();
			while (nss.hasNext()) {
				String namespace = (String) nss.next();
				if ((TextUtils.isEmpty(namespace))
						|| (namespace.trim().equals(""))) {
					throw new IllegalArgumentException(
							"Namespaces must not be null or empty");
				}
			}
			if (applicationId == null) {
				sb.append("/");
			}
			sb.append("/").append(TextUtils.join(",", namespaces));
		}
		return sb.toString();
	}
}
