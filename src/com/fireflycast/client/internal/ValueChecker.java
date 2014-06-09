package com.fireflycast.client.internal;


public class ValueChecker {

	public static <T> T checkNullPointer_f(T paramT) {
		if (paramT == null)
			throw new NullPointerException("null reference");
		return paramT;
	}

	public static <T> T checkNullPointer_b(T paramT, Object paramObject) {
		if (paramT == null)
			throw new NullPointerException(String.valueOf(paramObject));
		return paramT;
	}

	public static void checkTrue(boolean paramBoolean) {
		if (paramBoolean) {
			return;
		}
		throw new IllegalStateException();
	}

	public static void checkTrueWithErrorMsg(boolean paramBoolean,
			Object paramObject) {
		if (paramBoolean) {
			return;
		}
		throw new IllegalStateException(String.valueOf(paramObject));
	}

}
