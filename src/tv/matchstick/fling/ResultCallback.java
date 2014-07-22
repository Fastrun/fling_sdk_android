package tv.matchstick.fling;


/**
 * An interface for receiving a Result from a PendingResult as an asynchronous callback. 
 */
public interface ResultCallback<R extends Result> {
	/**
	 * Called when result is returned
	 *
	 * It is the responsibility of each callback to release any resources associated with the result. Some result types may implement Releasable, in which case release() should be used to free the associated resources.
	 * @param result The result from the API call. May not be null. 
	 */
	public void onResult(R result);
}
