package tv.matchstick.fling;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import tv.matchstick.client.internal.AccountInfo;
import tv.matchstick.client.internal.FlingClientImpl;
import tv.matchstick.client.internal.ValueChecker;
import tv.matchstick.fling.internal.Api;
import tv.matchstick.fling.internal.MatchStickApi.MatchStickApiImpl;
import tv.matchstick.fling.FlingManager.ApiOptions;
import tv.matchstick.fling.FlingManager.ConnectionCallbacks;
import tv.matchstick.fling.FlingManager.OnConnectionFailedListener;
import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;

/**
 * Which will be used by fling application to do all fling actions.
 *
 * To use the service, construct a FlingManager.Builder and pass API to addApi(Api). Once you have your FlingManager, call connect() and wait for the onConnected(Bundle) method to be called.
 * <p>
 * Device discovery on Android is performed using the Android MediaRouter APIs. The MediaRouter APIs are implemented in the Android v7 Support Library. These APIs provide a simple mechanism for discovering media destinations, such as Chromecasts, bluetooth speakers, Android-powered smart TVs, and other media playback devices; and for routing media content to and controlling playback on those endpoints. These endpoints are referred to as “media routes.”
 * <p>
 * The first step to using these APIs is to acquire the MediaRouter singleton. It is important for the application to hold on to the reference to this singleton for as long as the application will be using the MediaRouter APIs; otherwise it may get garbage collected at an inopportune time.
 * <br>
 * Next, an appropriate route selector must be constructed. The purpose of the route selector is to filter the routes down to only those that the application is interested in such as Fling devices. It is also possible to filter the routes further by supported receiver application, in the (typical) case where the sender application expects to use a specific one.
 * <br>
 * Third, a MediaRouter callback is constructed. This callback has methods that will be called by the MediaRouter whenever a route becomes available or unavailable or a route is selected by the user.
 * <br>
 */
public class Fling {
	/**
	 * Max fling message length (in bytes) supported by channel.
	 */
	public static final int MAX_MESSAGE_LENGTH = 65536;

	/**
	 * Max namespace length (in characters).
	 */
	public static final int MAX_NAMESPACE_LENGTH = 128;

	/**
	 * extra app flags.
	 *
	 * A boolean extra for the connection hint bundle passed to onConnected(Bundle) that indicates that the connection was re-established, but the receiver application that was in use at the time of the connection loss is no longer running on the receiver. 
	 */
	public static final String EXTRA_APP_NO_LONGER_RUNNING = "tv.matchstick.fling.EXTRA_APP_NO_LONGER_RUNNING";

	/**
	 * Api Connection builder.
	 */
	static final Api.ConnectionBuilder<FlingClientImpl> mConnectionBuilder = new Api.ConnectionBuilder<FlingClientImpl>() {
		public int getPriority() {
			return -1;
		}

		/**
		 * This function will create a concrete Fling client which will interact with Fling Service.
		 * Those action are , for example, connect(),disconnect(), etc.
		 */
		@Override
		public FlingClientImpl build(Context context, Looper looper,
				AccountInfo account, ApiOptions options,
				ConnectionCallbacks callbacks,
				OnConnectionFailedListener failedListener) {
			ValueChecker.checkNullPointer(options,
					"Setting the API options is required.");
			ValueChecker.checkTrueWithErrorMsg(options instanceof FlingOptions,
					"Must provide valid FlingOptions!");

			FlingOptions flingOptions = (FlingOptions) options;
			return new FlingClientImpl(context, looper, flingOptions.flingDevice,
					flingOptions.loggingFlag, flingOptions.flingListener,
					callbacks, failedListener);
		}
	};

	/**
	 * Token to pass to addApi(Api) to enable the Fling features. 
	 */
	public static final Api API = new Api(mConnectionBuilder);

	/**
	 * FlingApi instance.
	 *
	 * The instance is used to interact with a fling device. 
	 */
	public static final FlingApi FlingApi = new FlingApi.FlingApiImpl();

	/**
	 * The entry point for interacting with a Fling device.
	 */
	public interface FlingApi {

		/**
		 * Create one application ID with app's url
		 * 
		 * @param appUrl
		 *            app url
		 * @return
		 */
		public abstract String makeApplicationId(String appUrl);

		/**
		 * Create one application ID with app's id and url
		 * 
		 * @param id
		 * @param msUrl
		 * @return
		 */
		public abstract String makeApplicationId(String id, String msUrl);

		/**
		 * Request current receiver application's status
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @throws IOException If there is no active service connection.
		 * @throws IllegalStateException If an I/O error occurs while performing the request. 
		 */
		public abstract void requestStatus(FlingManager client)
				throws IOException, IllegalStateException;

		/**
		 * Send one message to the current connected receiver application.
		 * 
		 * @param client
		 *            Api apiclient with which to perform this request.
		 * @param namespace
		 *            the dest namespace. Namespace must begin with the prefix " urn:x-cast:".
		 * @param message
		 *            message which will be sent
		 * @return pending result which can be used to see whether the message has been enqueued to be sent to a Fling device. 
		 */
		public abstract PendingResult<Status> sendMessage(
				FlingManager client, String namespace, String message);

		/**
		 * Launch a receiver application
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @param applicationId The ID of the receiver application to launch.
		 * @return pending result which can be used to retrieve connection information. 
		 */
		public abstract PendingResult<ApplicationConnectionResult> launchApplication(
				FlingManager client, String applicationId);

		/**
		 * Launch a receiver application
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @param applicationId
		 *            application Id The ID of the receiver application to launch.
		 * @param relaunchIfRunning
		 *            If true, relaunch the application if it is already running.
		 * @return launch's pending result which can be used to retrieve connection information. 
		 */
		public abstract PendingResult<ApplicationConnectionResult> launchApplication(
				FlingManager client, String applicationId,
				boolean relaunchIfRunning);

		/**
		 * Join to the current running receiver application.
		 *
		 * The previous PendingResult will be canceled with the Fling.ApplicationConnectionResult's status code being CANCELED.
		 *
		 * @param client
		 *            Api client with which to perform this request.
		 * @param applicationId
		 *            application Id of the receiver application to join.
		 * @param sessionId 
		 *            The expected session ID of the receiver application, or null to connect without checking for a matching session ID
		 * @return join's pending result which can be used to retrieve connection information. 
		 */
		public abstract PendingResult<ApplicationConnectionResult> joinApplication(
				FlingManager client, String applicationId, String sessionId);

		/**
		 * Join to the current running receiver application.
		 *
		 * The previous PendingResult will be canceled with the Fling.ApplicationConnectionResult's status code being CANCELED.
		 *
		 * @param client
		 *            Api client with which to perform this request.
		 * @param applicationId
		 *            application Id of the receiver application to join.
		 * @return join's pending result which can be used to retrieve connection information. 
		 */
		public abstract PendingResult<ApplicationConnectionResult> joinApplication(
				FlingManager client, String applicationId);

		/**
		 * Join to the current running receiver application.
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @return join's pending result which can be used to retrieve connection information. 
		 */
		public abstract PendingResult<ApplicationConnectionResult> joinApplication(
				FlingManager client);

		/**
		 * Disconnect from receiver application
		 *
		 *  If there is no currently active application session, this method does nothing. If this method is called while stopApplication(GoogleApiClient) is pending, then this method does nothing. The Status's status code will be INVALID_REQUEST.
		 *  
		 * @param client
		 *            Api client with which to perform this request.
		 * @return leave's pending result which can be used to retrieve if the command was successful.
		 */
		public abstract PendingResult<Status> leaveApplication(
				FlingManager client);

		/**
		 * Stops any running receiver application(s)
		 *
		 * If this method is called while leaveApplication(FlingManager) is pending, then this method does nothing. The Status's status code will be INVALID_REQUEST.
		 *
		 * @param client
		 *            Api client with which to perform this request.
		 * @return stop's pending result which can be used to retrieve if the command was successful. 
		 */
		public abstract PendingResult<Status> stopApplication(
				FlingManager client);

		/**
		 * Stop aplicationStops the currently running receiver application, optionally doing so only if its session ID matches the supplied one.
		 *
		 * If this method is called while leaveApplication(FlingManager) is pending, then this method does nothing. The Status's status code will be INVALID_REQUEST.
		 *
		 *
		 * @param client
		 *            Api client with which to perform this request.
		 * @param sessionId
		 *            session Id of the application to stop. sessionId cannot be null or an empty string.
		 * @return stop's pending result which can be used to retrieve if the command was successful. 
		 */
		public abstract PendingResult<Status> stopApplication(
				FlingManager client, String sessionId);

		/**
		 * Set device's volume
		 *
		 * If volume is outside of the range [0.0, 1.0], then the value will be clipped.
		 *
		 * @param client
		 *            Api client with which to perform this request.
		 * @param volume
		 *            volume level, in the range [0.0, 1.0].
		 * @throws IOException If there is no active service connection.
		 * @throws IllegalArgumentException If the volume is infinity or NaN.
		 * @throws IllegalStateException If an I/O error occurs while performing the request. 
		 */
		public abstract void setVolume(FlingManager client, double volume)
				throws IOException, IllegalArgumentException,
				IllegalStateException;

		/**
		 * Get current device's volume, the range is [0.0, 1.0].
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @return
		 * @throws IllegalStateException If there is no active service connection. 
		 */
		public abstract double getVolume(FlingManager client)
				throws IllegalStateException;

		/**
		 * Set device mute state, mute or unmute.
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @param mute
		 *            mute status Whether to mute or unmute the audio.
		 * @throws IOException If there is no active service connection.
		 * @throws IllegalStateException If an I/O error occurs while performing the request. 
		 */
		public abstract void setMute(FlingManager client, boolean mute)
				throws IOException, IllegalStateException;

		/**
		 * Check device's mute status
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @return mute status
		 * @throws IllegalStateException If there is no active service connection. 
		 */
		public abstract boolean isMute(FlingManager client)
				throws IllegalStateException;

		/**
		 * Get current launched receiver application's meta data
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @return application meta data
		 * @throws IllegalStateException If there is no active service connection. 
		 */
		public abstract ApplicationMetadata getApplicationMetadata(
				FlingManager client) throws IllegalStateException;

		/**
		 * Get current launched receiver application's status
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @return Application's status
		 * @throws IllegalStateException If there is no active service connection. 
		 */
		public abstract String getApplicationStatus(FlingManager client)
				throws IllegalStateException;

		/**
		 * Set callback function when received messages on the specific namespace
		 *
		 *  The new listener will replace an existing listener for a given namespace. Messages received by the controller for the given namespace will be forwarded to this listener. The caller must have already called connect() and received onConnected(Bundle) callback.
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @param namespace
		 *            used namespace of the Fling channel. Namespaces must begin with the prefix "urn:x-cast:".
		 * @param callback
		 *            callback function
		 * @throws IOException If an I/O error occurs while performing the request.
		 * @throws IllegalStateException Thrown when the controller is not connected to a FlingDevice.
		 */
		public abstract void setMessageReceivedCallbacks(
				FlingManager client, String namespace,
				MessageReceivedCallback callback) throws IOException,
				IllegalStateException;

		/**
		 * Remove message received callback function from this controller for a given namespace.
		 * 
		 * @param client
		 *            Api client with which to perform this request.
		 * @param namespace
		 *            used namespace
		 * @throws IOException If an I/O error occurs while performing the request.
		 * @throws IllegalArgumentException If namespace is null or empty. 
		 */
		public abstract void removeMessageReceivedCallbacks(
				FlingManager client, String namespace) throws IOException,
				IllegalArgumentException;

		/**
		 * Implementation of FlingApi interface
		 */
		public static final class FlingApiImpl implements FlingApi {
			public String makeApplicationId(String appUrl) {
				try {
					String encode = URLEncoder.encode(appUrl, "UTF-8");
					StringBuffer sb = new StringBuffer();
					sb.append("app:?uri=");
					sb.append(encode);
					return sb.toString();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return appUrl;
			}

			public String makeApplicationId(String id, String msUrl) {
				try {
					String encode = URLEncoder.encode(msUrl, "UTF-8");
					StringBuffer sb = new StringBuffer();
					sb.append("app:?id=");
					sb.append(id);
					sb.append("&ms=");
					sb.append(encode);
					return sb.toString();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return id;
			}

			public void requestStatus(FlingManager client)
					throws IOException, IllegalStateException {
				try {
					client.getConnectionApi(Fling.mConnectionBuilder)
							.requestStatus();
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}

			public PendingResult<Status> sendMessage(FlingManager client,
					final String namespace, final String message) {
				return client.executeTask(new StatusResultHandler() {
					protected void execute(FlingClientImpl dq)
							throws RemoteException {
						try {
							dq.sendMessage(namespace, message, this);
						} catch (IllegalArgumentException localIllegalArgumentException) {
							notifyResult(FlingStatusCodes.INVALID_REQUEST);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult(FlingStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<ApplicationConnectionResult> launchApplication(
					FlingManager client, final String applicationId) {
				return client
						.executeTask(new ApplicationConnectionResultHandler() {
							protected void execute(FlingClientImpl dq)
									throws RemoteException {
								try {
									dq.launchApplication(applicationId, false,
											this);
								} catch (IllegalStateException localIllegalStateException) {
									notifyResult(FlingStatusCodes.INVALID_REQUEST);
								}
							}
						});
			}

			public PendingResult<ApplicationConnectionResult> launchApplication(
					FlingManager client, final String applicationId,
					final boolean relaunchIfRunning) {
				return client
						.executeTask(new ApplicationConnectionResultHandler() {
							protected void execute(FlingClientImpl dq)
									throws RemoteException {
								try {
									dq.launchApplication(applicationId,
											relaunchIfRunning, this);
								} catch (IllegalStateException localIllegalStateException) {
									notifyResult(FlingStatusCodes.INVALID_REQUEST);
								}
							}
						});
			}

			public PendingResult<ApplicationConnectionResult> joinApplication(
					FlingManager client, final String applicationId,
					final String sessionId) {
				return client
						.executeTask(new ApplicationConnectionResultHandler() {
							protected void execute(FlingClientImpl dq)
									throws RemoteException {
								try {
									dq.joinApplication(applicationId,
											sessionId, this);
								} catch (IllegalStateException localIllegalStateException) {
									notifyResult(FlingStatusCodes.INVALID_REQUEST);
								}
							}
						});
			}

			public PendingResult<ApplicationConnectionResult> joinApplication(
					FlingManager client, final String applicationId) {
				return client
						.executeTask(new ApplicationConnectionResultHandler() {
							protected void execute(FlingClientImpl dq)
									throws RemoteException {
								try {
									dq.joinApplication(applicationId, null,
											this);
								} catch (IllegalStateException localIllegalStateException) {
									notifyResult(FlingStatusCodes.INVALID_REQUEST);
								}
							}
						});
			}

			public PendingResult<ApplicationConnectionResult> joinApplication(
					FlingManager client) {
				return client
						.executeTask(new ApplicationConnectionResultHandler() {
							protected void execute(FlingClientImpl dq)
									throws RemoteException {
								try {
									dq.joinApplication(null, null, this);
								} catch (IllegalStateException localIllegalStateException) {
									notifyResult(FlingStatusCodes.INVALID_REQUEST);
								}
							}
						});
			}

			public PendingResult<Status> leaveApplication(
					FlingManager client) {
				return client.executeTask(new StatusResultHandler() {
					protected void execute(FlingClientImpl dq)
							throws RemoteException {
						try {
							dq.leaveApplication(this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult(FlingStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<Status> stopApplication(FlingManager client) {
				return client.executeTask(new StatusResultHandler() {
					protected void execute(FlingClientImpl dq)
							throws RemoteException {
						try {
							dq.stopApplication("", this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult(FlingStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<Status> stopApplication(
					FlingManager client, final String sessionId) {
				return client.executeTask(new StatusResultHandler() {
					protected void execute(FlingClientImpl dq)
							throws RemoteException {
						if (TextUtils.isEmpty(sessionId)) {
							notifyResult(FlingStatusCodes.INVALID_REQUEST,
									"IllegalArgument: sessionId cannot be null or empty");
							return;
						}
						try {
							dq.stopApplication(sessionId, this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult(FlingStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public void setVolume(FlingManager client, double volume)
					throws IOException, IllegalArgumentException,
					IllegalStateException {
				try {
					client.getConnectionApi(Fling.mConnectionBuilder).setVolume(
							volume);
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}

			public double getVolume(FlingManager client)
					throws IllegalStateException {
				return client.getConnectionApi(Fling.mConnectionBuilder)
						.getVolume();
			}

			public void setMute(FlingManager client, boolean mute)
					throws IOException, IllegalStateException {
				try {
					client.getConnectionApi(Fling.mConnectionBuilder).setMute(
							mute);
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}

			public boolean isMute(FlingManager client)
					throws IllegalStateException {
				return client.getConnectionApi(Fling.mConnectionBuilder)
						.isMute();
			}

			public ApplicationMetadata getApplicationMetadata(
					FlingManager client) throws IllegalStateException {
				return client.getConnectionApi(Fling.mConnectionBuilder)
						.getApplicationMetadata();
			}

			public String getApplicationStatus(FlingManager client)
					throws IllegalStateException {
				return client.getConnectionApi(Fling.mConnectionBuilder)
						.getApplicationStatus();
			}

			public void setMessageReceivedCallbacks(FlingManager client,
					String namespace, Fling.MessageReceivedCallback callbacks)
					throws IOException, IllegalStateException {
				try {
					client.getConnectionApi(Fling.mConnectionBuilder)
							.setMessageReceivedCallbacks(namespace, callbacks);
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}

			public void removeMessageReceivedCallbacks(FlingManager client,
					String namespace) throws IOException,
					IllegalArgumentException {
				try {
					client.getConnectionApi(Fling.mConnectionBuilder)
							.removeMessageReceivedCallbacks(namespace);
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}
		}
	}

	/**
	 * Status result handler
	 */
	static abstract class StatusResultHandler extends
			PendingResultHandler<Status> {
		/**
		 * Get Status object
		 */
		@Override
		protected Status createResult(Status status) {
			// TODO Auto-generated method stub
			return status;
		}
	}

	/**
	 * Application connection result handler
	 */
	static abstract class ApplicationConnectionResultHandler extends
			PendingResultHandler<ApplicationConnectionResult> {
		/**
		 * Create application pending result object
		 * 
		 * @status result code
		 */
		@Override
		protected ApplicationConnectionResult createResult(final Status status) {
			return new ApplicationConnectionResult() {
				public Status getStatus() {
					return status;
				}

				public boolean getWasLaunched() {
					return false;
				}

				public String getSessionId() {
					return null;
				}

				public String getApplicationStatus() {
					return null;
				}

				public ApplicationMetadata getApplicationMetadata() {
					return null;
				}
			};
		}
	}

	/**
	 * Pending result handler
	 *
	 * @param <R>
	 *            result
	 */
	static abstract class PendingResultHandler<R extends Result> extends
			MatchStickApiImpl<R, FlingClientImpl> implements PendingResult<R> {

		/**
		 * Constructor
		 */
		public PendingResultHandler() {
			super(Fling.mConnectionBuilder);
		}

		/**
		 * Notify pending results with code
		 * 
		 * @param statusCode
		 *            result status
		 */
		public void notifyResult(int statusCode) {
			postResult(createResult(new Status(statusCode)));
		}

		/**
		 * notify Pending result with code and description
		 * 
		 * @param statusCode
		 *            result status
		 * @param statusMessage
		 *            result description
		 */
		public void notifyResult(int statusCode, String statusMessage) {
			postResult(createResult(new Status(statusCode, statusMessage, null)));
		}
	}

	/**
	 * Fling options
	 * 
	 * Contained all fling options, for example, application status listener, log
	 * flags.
	 * <p>
	 * The Fling.FlingOptions.Builder is used to create an instance of Fling.FlingOptions. 
	 *
	 */
	public static final class FlingOptions implements
			FlingManager.ApiOptions {
		/**
		 * Fling device
		 */
		final FlingDevice flingDevice;

		/**
		 * Application status listener
		 */
		final Listener flingListener;

		/**
		 * Log flags
		 */
		private final int loggingFlag;

		/**
		 * Fling operation constructor
		 * 
		 * @param builder
		 */
		private FlingOptions(Builder builder) {
			this.flingDevice = builder.mFlingDevice;
			this.flingListener = builder.mListener;
			this.loggingFlag = builder.mLoggingFlag;
		}

		/**
		 * Create fling option builder 
		 * 
		 * @param flingDevice device returned from the MediaRouteProvider
		 * @param flingListener listener for Fling events
		 * @return one builder object
		 */
		public static Builder builder(FlingDevice flingDevice,
				Listener flingListener) {
			return new Builder(flingDevice, flingListener);
		}

		/**
		 * A builder to create an instance of Fling.FlingOptions to set API configuration parameters for Fling. 
		 */
		public static final class Builder {
			/**
			 * Fling device
			 */
			FlingDevice mFlingDevice;

			/**
			 * Application status's listener
			 */
			Listener mListener;

			/**
			 * Log flags
			 */
			private int mLoggingFlag;

			private Builder(FlingDevice flingDevice, Listener flingListener) {
				ValueChecker.checkNullPointer(flingDevice,
						"FlingDevice parameter cannot be null");
				ValueChecker.checkNullPointer(flingListener,
						"FlingListener parameter cannot be null");
				this.mFlingDevice = flingDevice;
				this.mListener = flingListener;
				this.mLoggingFlag = 0;
			}

			/**
			 * Enables or disables verbose logging for this Fling session.
			 *
			 * Debug only
			 * 
			 * @param enabled
			 * @return
			 */
			public Builder setVerboseLoggingEnabled(boolean enabled) {
				if (enabled) {
					this.mLoggingFlag |= 1;
				} else {
					this.mLoggingFlag &= -2;
				}
				return this;
			}

			/**
			 * Create one fling option object which will be used in connect to Fling device.
			 */
			public FlingOptions build() {
				return new FlingOptions(this);
			}
		}
	}

	/**
	 * Interface to display current application connection status.
	 *
	 * <br>
	 * When a Fling application connected to a remote receiver, this object will be returned.
	 * And it contains this fling application's meta data and current status.
	 * @see tv.matchstick.fling.ApplicationMetadata
	 */
	public interface ApplicationConnectionResult extends Result {
		/**
		 * Get current application's meta data
		 */
		public abstract ApplicationMetadata getApplicationMetadata();

		/**
		 * Get current application's status
		 * 
		 * @return application status
		 */
		public abstract String getApplicationStatus();

		/**
		 * Get current application's session ID
		 * 
		 * @return session id
		 */
		public abstract String getSessionId();

		/**
		 * Check whether current application was launched from scratch
		 * 
		 * @return
		 */
		public abstract boolean getWasLaunched();
	}

	/**
	 * Callback function when received messages from a Fling device
	 */
	public interface MessageReceivedCallback {
		/**
		 * Called when message received from a Fling device
		 * 
		 * @param flingDevice
		 *            fling device where the message comes from
		 * @param namespace
		 *            namespace of the received message
		 * @param message
		 *            received message
		 */
		public abstract void onMessageReceived(FlingDevice flingDevice,
				String namespace, String message);
	}

	/**
	 * Interface which will be notified when fling application's status is
	 * changed.
	 */
	public static abstract class Listener {
		/**
		 * Called when application's status changed
		 */
		public void onApplicationStatusChanged() {
		}

		/**
		 * Called when connection to the receiver is disconnected.such as when another client has launched a new application. 
		 * 
		 * @param statusCode status code
		 */
		public void onApplicationDisconnected(int statusCode) {
		}

		/**
		 * Called when application volume or mute status changed
		 */
		public void onVolumeChanged() {
		}
	}
}
