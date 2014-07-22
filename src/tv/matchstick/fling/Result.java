package tv.matchstick.fling;


/**
 * Fling Result interface.
 *
 * Represents the final result of invoking an API method in Fling Service. 
 */
public interface Result {
	/**
	 * Get result status
	 * 
	 * @return result status
	 */
	public abstract Status getStatus();
}
