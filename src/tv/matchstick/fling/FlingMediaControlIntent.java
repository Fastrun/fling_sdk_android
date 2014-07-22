package tv.matchstick.fling;

import java.util.Collection;
import java.util.Iterator;

import android.text.TextUtils;

/**
 * Fling media control intent.
 *
 * Intent constants for use with the Fling MediaRouteProvider. <br>
 * This class also contains utility methods for creating a control category for discovering Fling media routes that support a specific app and/or set of namespaces, to be used with MediaRouteSelector. 
 */
public class FlingMediaControlIntent {
	/**
	 * Fling category
	 */
	public static final String CATEGORY_FLING = "tv.matchstick.fling.CATEGORY_FLING";

	/**
	 * Remote media playback category
	 */
	public static final String CATEGORY_REMOTE_PLAYBACK = "tv.matchstick.fling.CATEGORY_FLING_REMOTE_PLAYBACK";

	/**
	 * Default media player receiver application id
	 */
	public static final String DEFAULT_MEDIA_RECEIVER_APPLICATION_ID = "CC1AD845";

	/**
	 * Sync status action.
	 *
	 * A Fling extension action for requesting the current media status when the current item ID is not known to the client application.
	 */
	public static final String ACTION_SYNC_STATUS = "tv.matchstick.fling.ACTION_SYNC_STATUS";

	/**
	 * Extra data for custom data.
	 *
	 * The extra that contains a compact JSON string of custom data to pass with a media request.
	 */
	public static final String EXTRA_CUSTOM_DATA = "tv.matchstick.fling.EXTRA_CUSTOM_DATA";

	/**
	 * Extra data for application Id.
	 *
	 * The extra that contains the ID of the application to launch for an ACTION_START_SESSION request.
	 */
	public static final String EXTRA_FLING_APPLICATION_ID = "tv.matchstick.fling.EXTRA_FLING_APPLICATION_ID";

	/**
	 * Extra data for relaunch application.
	 *
	 * The extra that indicates whether the application should be relaunched if it is already running (the default behavior) or whether an attempt should be made to join the application first.
	 */
	public static final String EXTRA_FLING_RELAUNCH_APPLICATION = "tv.matchstick.fling.EXTRA_FLING_RELAUNCH_APPLICATION";

	/**
	 * Extra data for stop application when end session.
	 *
	 * The extra that indicates that the receiver application should be stopped when the session ends.
	 */
	public static final String EXTRA_FLING_STOP_APPLICATION_WHEN_SESSION_ENDS = "tv.matchstick.fling.EXTRA_FLING_STOP_APPLICATION_WHEN_SESSION_ENDS";

	/**
	 * Extra data for debug log enabled.
	 *
	 * The extra that indicates whether debug logging should be enabled for the Fling session.
	 */
	public static final String EXTRA_DEBUG_LOGGING_ENABLED = "tv.matchstick.fling.EXTRA_DEBUG_LOGGING_ENABLED";

	/**
	 * Extra error code.
	 *
	 * An error bundle extra for the error code.
	 */
	public static final String EXTRA_ERROR_CODE = "tv.matchstick.fling.EXTRA_ERROR_CODE";

	/**
	 * Fling request failed.
	 */
	public static final int ERROR_CODE_REQUEST_FAILED = 1;

	/**
	 * Session start failed.
	 *
	 * An error code indicating that the request could not be processed because the session could not be started.
	 */
	public static final int ERROR_CODE_SESSION_START_FAILED = 2;

	/**
	 * temporarily disconnected.
	 *
	 * An error code indicating that the connection to the Fling device has been lost, but the system is actively trying to re-establish the connection.
	 */
	public static final int ERROR_CODE_TEMPORARILY_DISCONNECTED = 3;

	/**
	 * Get media remote playback category.
	 *
	 * Returns a custom control category for discovering Fling devices which support the default Android remote playback actions using the specified Fling player.
	 * 
	 * @param applicationId
	 *            application's id
	 * @return media remote playback category
	 * @throws IllegalArgumentException
	 */
	public static String categoryForRemotePlayback(String applicationId)
			throws IllegalArgumentException {
		if (TextUtils.isEmpty(applicationId)) {
			throw new IllegalArgumentException(
					"applicationId cannot be null or empty");
		}
		return buildCategory(CATEGORY_REMOTE_PLAYBACK, applicationId, null);
	}

	/**
	 * Get default remote media playback category.
	 *
	 * Returns a custom control category for discovering Fling devices which support the Default Media Receiver.
	 * 
	 * @return media remote playback category
	 */
	public static String categoryForRemotePlayback() {
		return buildCategory(CATEGORY_REMOTE_PLAYBACK, null, null);
	}

	/**
	 * Get fling category with application Id.
	 *
	 * Returns a custom control category for discovering Fling devices that support running the specified app, independent of whether the app is running or not.
	 * 
	 * @param applicationId
	 *            application's Id
	 * @return fling category
	 * @throws IllegalArgumentException
	 */
	public static String categoryForFling(String applicationId)
			throws IllegalArgumentException {
		if (applicationId == null) {
			throw new IllegalArgumentException("applicationId cannot be null");
		}
		return buildCategory(CATEGORY_FLING, applicationId, null);
	}

	/**
	 * Get fling category with specific namespace.
	 *
	 * Returns a custom control category for discovering Fling devices currently running an application which supports the specified namespaces.
	 * 
	 * @param namespaces
	 *            namespace list
	 * @return fling category
	 * @throws IllegalArgumentException
	 */
	public static String categoryForFling(Collection<String> namespaces)
			throws IllegalArgumentException {
		if (namespaces == null) {
			throw new IllegalArgumentException("namespaces cannot be null");
		}
		return buildCategory(CATEGORY_FLING, null, namespaces);
	}

	/**
	 * Get Fling category with specific application and it's namespace list.
	 *
	 * Returns a custom control category for discovering Fling devices meeting both application ID and namespace restrictions.
	 * 
	 * @param applicationId
	 *            application Id
	 * @param namespaces
	 *            namespace list
	 * @return fling category
	 */
	public static String categoryForFling(String applicationId,
			Collection<String> namespaces) {
		if (applicationId == null) {
			throw new IllegalArgumentException("applicationId cannot be null");
		}
		if (namespaces == null) {
			throw new IllegalArgumentException("namespaces cannot be null");
		}
		return buildCategory(CATEGORY_FLING, applicationId, namespaces);
	}

	/**
	 * Build category with category, application id and namespace list.
	 * 
	 * @param category
	 *            the specific category
	 * @param applicationId
	 *            application Id
	 * @param namespaces
	 *            namespace list
	 * @return category
	 * @throws IllegalArgumentException
	 */
	private static String buildCategory(String category, String applicationId,
			Collection<String> namespaces) throws IllegalArgumentException {
		StringBuffer sb = new StringBuffer(category);
		if (applicationId != null) {
			// if (!(applicationId.matches("[A-F0-9]+"))) {
			// throw new IllegalArgumentException("Invalid appliation ID: "
			// + applicationId);
			// }
			sb.append("/").append(applicationId);
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
