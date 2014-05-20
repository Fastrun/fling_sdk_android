package com.fireflycast.client.common.api;

/*
 * CommonStatusCodes.smali : OK
 */
public class CommonStatusCodes {

	public static final int SUCCESS_CACHE = -1;
	public static final int SUCCESS = 0;
	public static final int SERVICE_MISSING = 1;
	public static final int SERVICE_VERSION_UPDATE_REQUIRED = 2;
	public static final int SERVICE_DISABLED = 3;
	public static final int SIGN_IN_REQUIRED = 4;
	public static final int INVALID_ACCOUNT = 5;
	public static final int RESOLUTION_REQUIRED = 6;
	public static final int NETWORK_ERROR = 7;
	public static final int INTERNAL_ERROR = 8;
	public static final int SERVICE_INVALID = 9;
	public static final int DEVELOPER_ERROR = 10;
	public static final int LICENSE_CHECK_FAILED = 11;
	public static final int DATE_INVALID = 12;
	public static final int ERROR = 13;
	public static final int INTERRUPTED = 14;
	public static final int TIMEOUT = 15;

	public static String getStatusMessage(int statusCode) {
		switch (statusCode) {
			case -1:
				return "SUCCESS_CACHE";
			case 0:
				return "SUCCESS";
			case 1:
				return "SERVICE_MISSING";
			case 2:
				return "SERVICE_VERSION_UPDATE_REQUIRED";
			case 3:
				return "SERVICE_DISABLED";
			case 4:
				return "SIGN_IN_REQUIRED";
			case 5:
				return "INVALID_ACCOUNT";
			case 6:
				return "RESOLUTION_REQUIRED";
			case 7:
				return "NETWORK_ERROR";
			case 8:
				return "INTERNAL_ERROR";
			case 9:
				return "SERVICE_INVALID";
			case 10:
				return "DEVELOPER_ERROR";
			case 11:
				return "LICENSE_CHECK_FAILED";
			case 12:
				return "DATE_INVALID";
			case 13:
				return "ERROR";
			case 14:
				return "INTERRUPTED";
			case 15:
				return "TIMEOUT";
		}
		return "unknown status code: " + statusCode;
	}

}
