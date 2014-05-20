package com.fireflycast.cast;

import java.util.concurrent.TimeUnit;

public interface PendingResult<R extends Result> {
	public R await();

	public R await(long time, TimeUnit timeUnit);

	public void setResultCallback(ResultCallback<R> resultCb);

	public void setResultCallback(ResultCallback<R> resultCb, long time,
			TimeUnit timeUnit);
}
