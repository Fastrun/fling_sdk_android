package tv.matchstick.fling;

import java.util.concurrent.TimeUnit;

/**
 * This interface used for fling request wait and callback process.
 *
 * Represents a pending result from calling an API method in Fling service. The final result object from a PendingResult is of type R, which can be retrieved in one of two ways.
 * via blocking calls to await(), or await(long, TimeUnit), or
 * via a callback by passing in an object implementing interface ResultCallback to setResultCallback(ResultCallback). 
 * After the result has been retrieved using await() or delivered to the result callback, it is an error to attempt to retrieve the result again. It is the responsibility of the caller or callback receiver to release any resources associated with the returned result. Some result types may implement Releasable, in which case release() should be used to free the associated resources. 
 *
 * @param <R>
 */
public interface PendingResult<R extends Result> {
	/**
	 * Block wait.
	 *
	 * Blocks until the task is completed.
	 *
	 * @return
	 */
	public R await();

	/**
	 * Blocks until the task is completed or has timed out waiting for the result.
	 *
	 * @param time
	 * @param timeUnit
	 * @return
	 */
	public R await(long time, TimeUnit timeUnit);

	/**
	 * Set the callback here if you want the result to be delivered via a callback when the result is ready or has timed out waiting for the result.
	 *
	 * @param resultCb
	 */
	public void setResultCallback(ResultCallback<R> resultCb);

	/**
	 * Set the callback here if you want the result to be delivered via a callback when the result is ready.
	 *
	 * The returned result object can have an additional failure mode of TIMEOUT. 
	 * @param resultCb
	 * @param time
	 * @param timeUnit
	 */
	public void setResultCallback(ResultCallback<R> resultCb, long time,
			TimeUnit timeUnit);
}
