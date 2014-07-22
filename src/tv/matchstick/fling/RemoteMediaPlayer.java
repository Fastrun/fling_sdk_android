package tv.matchstick.fling;

import java.io.IOException;

import org.json.JSONObject;

import tv.matchstick.client.internal.FlingClientImpl;
import tv.matchstick.client.internal.MediaControlChannelImpl;
import tv.matchstick.client.internal.MessageSender;
import tv.matchstick.client.internal.RequestTrackerCallback;

/**
 * Class for controlling a media player application running on a receiver. 
 *
 * Some operations, like loading of media or adjusting volume, can be tracked. The corresponding methods return a PendingResult for this purpose. 
 */
public class RemoteMediaPlayer implements Fling.MessageReceivedCallback {
	/**
	 * A resume state indicating that the player state should be left unchanged.
	 */
	public static final int RESUME_STATE_UNCHANGED = 0;

	/**
	 * A resume state indicating that the player should be playing, regardless of its current state.
	 */
	public static final int RESUME_STATE_PLAY = 1;

	/**
	 * A resume state indicating that the player should be paused, regardless of its current state.
	 */
	public static final int RESUME_STATE_PAUSE = 2;

	/**
	 * A status indicating that a request completed successfully.
	 */
	public static final int STATUS_SUCCEEDED = 0;

	/**
	 * A status indicating that a request failed.
	 */
	public static final int STATUS_FAILED = 1;

	/**
	 * A status indicating that a request was canceled.
	 */
	public static final int STATUS_CANCELED = 2;

	/**
	 * A status indicating that a request has timed out.
	 */
	public static final int STATUS_TIMED_OUT = 3;

	/**
	 * A status indicating that the request's progress is no longer being tracked because another request of the same type has been made before the first request completed.
	 */
	public static final int STATUS_REPLACED = 4;

	/**
	 * Media player lock object
	 */
	private final Object mLock = new Object();

	/**
	 * Media control channel implementation
	 */
	private final MediaControlChannelImpl mMediaControlChannel = new MediaControlChannelImpl() {
		/**
		 * Called when status updated
		 */
		protected void onStatusUpdated() {
			RemoteMediaPlayer.this.onStatusUpdated();
		}

		/**
		 * Called when media meta data is updated
		 */
		protected void onMetadataUpdated() {
			RemoteMediaPlayer.this.onMetadataUpdated();
		}
	};

	/**
	 * Message sender instance
	 */
	private final MessageSenderImpl mMessageSender = new MessageSenderImpl();

	/**
	 * Meta data updated listener
	 */
	private OnMetadataUpdatedListener mMetadataUpdatedListener;

	/**
	 * Status updated listener
	 */
	private OnStatusUpdatedListener mStatusUpdatedListener;

	/**
	 * Init media player
	 */
	public RemoteMediaPlayer() {
		this.mMediaControlChannel.setMessageSender(this.mMessageSender);
	}

	/**
	 * Loads and automatically starts playback of a new media item.
	 *
	 * @param apiClient with which to perform the operation.
	 * @param mediaInfo describing the media item to load.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> load(FlingManager apiClient,
			MediaInfo mediaInfo) {
		return load(apiClient, mediaInfo, true, 0L, null);
	}

	/**
	 * Loads and optionally starts playback of a new media item.
	 *
	 * @param apiClient with which to perform the operation.
	 * @param mediaInfo describing the media item to load.
	 * @param autoplay Whether playback should start immediately.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> load(FlingManager apiClient,
			MediaInfo mediaInfo, boolean autoplay) {
		return load(apiClient, mediaInfo, autoplay, 0L, null);
	}

	/**
	 * Loads and optionally starts playback of a new media item.
	 *
	 * @param apiClient with which to perform the operation.
	 * @param mediaInfo describing the media item to load.
	 * @param autoplay Whether playback should start immediately.
	 * @param playPosition Whether playback should start immediately.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> load(FlingManager apiClient,
			MediaInfo mediaInfo, boolean autoplay, long playPosition) {
		return load(apiClient, mediaInfo, autoplay, playPosition, null);
	}

	/**
	 * Loads and optionally starts playback of a new media item.
	 *
	 * This method optionally sends custom data as a JSONObject with the load request.
	 * 
	 * @param apiClient with which to perform the operation.
	 * @param mediaInfo An object describing the media item to load.
	 * @param autoplay Whether playback should start immediately.
	 * @param playPosition The initial playback position, in milliseconds from the beginning of the stream.
	 * @param customData Custom application-specific data to pass along with the request.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> load(
			final FlingManager apiClient, final MediaInfo mediaInfo,
			final boolean autoplay, final long playPosition,
			final JSONObject customData) {
		return apiClient.executeTask(new MediaChannelResultHandler() {
			protected void execute(FlingClientImpl dq) {
				synchronized (mLock) {
					mMessageSender.setApiClient(apiClient);
					try {
						mMediaControlChannel.load(this.requestTrackerCallback,
								mediaInfo, autoplay, playPosition, customData);
					} catch (IOException localIOException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender.setApiClient(null);
					}
				}
			}
		});
	}

	/**
	 * Pause media
	 *
	 * @param apiClient with which to perform the operation.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> pause(FlingManager apiClient) {
		return pause(apiClient, null);
	}

	/**
	 * Pause media
	 *
	 * @param apiClient with which to perform the operation.
	 * @param customData Custom application-specific data to pass along with the request.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> pause(
			final FlingManager apiClient, final JSONObject customData) {
		return apiClient.executeTask(new MediaChannelResultHandler() {
			protected void execute(FlingClientImpl dq) {
				synchronized (mLock) {
					mMessageSender.setApiClient(apiClient);
					try {
						mMediaControlChannel.pause(this.requestTrackerCallback,
								customData);
					} catch (IOException localIOException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender.setApiClient(null);
					}
				}
			}
		});
	}

	/**
	 * Stop media
	 *
	 * @param apiClient with which to perform the operation.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> stop(FlingManager apiClient) {
		return stop(apiClient, null);
	}

	/**
	 * Stop media
	 *
	 * @param apiClient with which to perform the operation.
	 * @param customData Custom application-specific data to pass along with the request.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> stop(
			final FlingManager apiClient, final JSONObject customData) {
		return apiClient.executeTask(new MediaChannelResultHandler() {
			protected void execute(FlingClientImpl dq) {
				synchronized (mLock) {
					mMessageSender.setApiClient(apiClient);
					try {
						mMediaControlChannel.stop(this.requestTrackerCallback,
								customData);
					} catch (IOException localIOException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender.setApiClient(null);
					}
				}
			}
		});
	}

	/**
	 * Play media
	 *
	 * @param apiClient with which to perform the operation.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> play(FlingManager apiClient) {
		return play(apiClient, null);
	}

	/**
	 * Play media
	 *
	 * @param apiClient with which to perform the operation.
	 * @param customData Custom application-specific data to pass along with the request.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> play(
			final FlingManager apiClient, final JSONObject customData) {
		return apiClient.executeTask(new MediaChannelResultHandler() {
			protected void execute(FlingClientImpl dq) {
				synchronized (mLock) {
					mMessageSender.setApiClient(apiClient);
					try {
						mMediaControlChannel.play(this.requestTrackerCallback,
								customData);
					} catch (IOException localIOException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender.setApiClient(null);
					}
				}
			}
		});
	}

	/**
	 * Seek media
	 *
	 * @param apiClient with which to perform the operation.
	 * @param position The new position, in milliseconds from the beginning of the stream.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> seek(FlingManager apiClient,
			long position) {
		return seek(apiClient, position, 0, null);
	}

	/**
	 * Seek media
	 *
	 * @param apiClient with which to perform the operation.
	 * @param position The new position, in milliseconds from the beginning of the stream.
	 * @param resumeState The action to take after the seek operation has finished.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> seek(FlingManager apiClient,
			long position, int resumeState) {
		return seek(apiClient, position, resumeState, null);
	}

	/**
	 * Seek media
	 *
	 * @param apiClient with which to perform the operation.
	 * @param position The new position, in milliseconds from the beginning of the stream.
	 * @param resumeState The action to take after the seek operation has finished.
	 * @param customData Custom application-specific data to pass along with the request.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> seek(
			final FlingManager apiClient, final long position,
			final int resumeState, final JSONObject customData) {
		return apiClient.executeTask(new MediaChannelResultHandler() {
			protected void execute(FlingClientImpl dq) {
				synchronized (mLock) {
					mMessageSender.setApiClient(apiClient);
					try {
						mMediaControlChannel.seek(this.requestTrackerCallback,
								position, resumeState, customData);
					} catch (IOException localIOException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender.setApiClient(null);
					}
				}
			}
		});
	}

	/**
	 * Set media stream volume
	 *
	 * If volume is outside of the range [0.0, 1.0], then the value will be clipped.
	 *
	 * @param apiClient with which to perform the operation.
	 * @param volume The new volume, in the range [0.0 - 1.0].
	 * @return A PendingResult which can be used to track the progress of the request.
	 * @throws IllegalArgumentException If the volume is infinity or NaN. 
	 */
	public PendingResult<MediaChannelResult> setStreamVolume(
			FlingManager apiClient, double volume)
			throws IllegalArgumentException {
		return setStreamVolume(apiClient, volume, null);
	}

	/**
	 * Set media stream volume
	 *
	 * @param apiClient with which to perform the operation.
	 * @param volume The new volume, in the range [0.0 - 1.0].
	 * @param customData Custom application-specific data to pass along with the request.
	 * @return A PendingResult which can be used to track the progress of the request.
	 * @throws IllegalArgumentException If the volume is infinity or NaN. 
	 */
	public PendingResult<MediaChannelResult> setStreamVolume(
			final FlingManager apiClient, final double volume,
			final JSONObject customData) throws IllegalArgumentException {
		if ((Double.isInfinite(volume)) || (Double.isNaN(volume))) {
			throw new IllegalArgumentException("Volume cannot be " + volume);
		}
		return apiClient.executeTask(new MediaChannelResultHandler() {
			protected void execute(FlingClientImpl dq) {
				synchronized (mLock) {
					mMessageSender.setApiClient(apiClient);
					try {
						mMediaControlChannel
								.setStreamVolume(this.requestTrackerCallback,
										volume, customData);
					} catch (IllegalStateException localIllegalStateException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} catch (IllegalArgumentException localIllegalArgumentException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} catch (IOException localIOException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender.setApiClient(null);
					}
				}
			}
		});
	}

	/**
	 * Set media stream mute status
	 *
	 * @param apiClient with which to perform the operation.
	 * @param muteState Whether the stream should be muted or unmuted.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> setStreamMute(
			FlingManager apiClient, boolean muteState) {
		return setStreamMute(apiClient, muteState, null);
	}

	/**
	 * Set media stream mute status
	 *
	 * @param apiClient with which to perform the operation.
	 * @param muteState Whether the stream should be muted or unmuted.
	 * @param customData Custom application-specific data to pass along with the request.
	 * @return A PendingResult which can be used to track the progress of the request.
	 */
	public PendingResult<MediaChannelResult> setStreamMute(
			final FlingManager apiClient, final boolean muteState,
			final JSONObject customData) {
		return apiClient.executeTask(new MediaChannelResultHandler() {
			protected void execute(FlingClientImpl dq) {
				synchronized (mLock) {
					mMessageSender.setApiClient(apiClient);
					try {
						mMediaControlChannel.setStreamMute(
								this.requestTrackerCallback, muteState,
								customData);
					} catch (IllegalStateException localIllegalStateException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} catch (IOException localIOException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender.setApiClient(null);
					}
				}
			}
		});
	}

	/**
	 * Request media status
	 *
	 * Requests updated media status information from the receiver.
	 * RemoteMediaPlayer.OnStatusUpdatedListener callback will be triggered, when the updated media status has been received. This will also update the internal state of the RemoteMediaPlayer object with the current state of the receiver, including the current session ID. This method should be called when joining an application that supports the media control namespace.
	 *
	 * @param apiClient with which to perform the operation.
	 * @return A PendingResult which can be used to track the progress of the request. 
	 */
	public PendingResult<MediaChannelResult> requestStatus(
			final FlingManager apiClient) {
		return apiClient.executeTask(new MediaChannelResultHandler() {
			protected void execute(FlingClientImpl paramdq) {
				synchronized (mLock) {
					mMessageSender.setApiClient(apiClient);
					try {
						mMediaControlChannel
								.requestStatus(this.requestTrackerCallback);
					} catch (IOException localIOException) {
						postResult(createMediaChannelResult(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender.setApiClient(null);
					}
				}
			}
		});
	}

	/**
	 * Get media approximate stream position
	 *
	 * Returns the approximate stream position as calculated from the last received stream information and the elapsed wall-time since that update.
	 *
	 * @return The approximate stream position, in milliseconds. 
	 */
	public long getApproximateStreamPosition() {
		synchronized (mLock) {
			return mMediaControlChannel.getApproximateStreamPosition();
		}
	}

	/**
	 * Get media stream duration
	 *
	 * @return media duration, in milliseconds. 
	 */
	public long getStreamDuration() {
		synchronized (mLock) {
			return mMediaControlChannel.getStreamDuration();
		}
	}

	/**
	 * Get current media status
	 *
	 * @return media status
	 */
	public MediaStatus getMediaStatus() {
		synchronized (mLock) {
			return mMediaControlChannel.getMediaStatus();
		}
	}

	/**
	 * Get current media info
	 *
	 * @return media info
	 */
	public MediaInfo getMediaInfo() {
		synchronized (mLock) {
			return mMediaControlChannel.getMediaInfo();
		}
	}

	/**
	 * Set status updated listener
	 *
	 * @param listener
	 */
	public void setOnStatusUpdatedListener(OnStatusUpdatedListener listener) {
		mStatusUpdatedListener = listener;
	}

	/**
	 * Called when current status changed to get status updates.
	 */
	private void onStatusUpdated() {
		if (mStatusUpdatedListener == null) {
			return;
		}
		mStatusUpdatedListener.onStatusUpdated();
	}

	/**
	 * Set media meta data updated listener to get meta data updates.
	 *
	 * @param listener
	 */
	public void setOnMetadataUpdatedListener(OnMetadataUpdatedListener listener) {
		mMetadataUpdatedListener = listener;
	}

	/**
	 * Called when media meta data updated
	 */
	private void onMetadataUpdated() {
		if (mMetadataUpdatedListener == null) {
			return;
		}
		mMetadataUpdatedListener.onMetadataUpdated();
	}

	/**
	 * Get current media control channel's namespace
	 *
	 * @return
	 */
	public String getNamespace() {
		return mMediaControlChannel.getNamespace();
	}

	/**
	 * Called when message received from a given device
	 *
	 * @param flingDevice from where the message originated.
	 * @param namespace The namespace of the received message.
	 * @param message The received payload for the message. 
	 */
	public void onMessageReceived(FlingDevice flingDevice, String namespace,
			String message) {
		mMediaControlChannel.onMessageReceived(message);
	}

	/**
	 * Media channel result handler
	 */
	private static abstract class MediaChannelResultHandler extends
			Fling.PendingResultHandler<MediaChannelResult> {
		/**
		 * Request tracker callback
		 */
		RequestTrackerCallback requestTrackerCallback = new RequestTrackerCallback() {
			/**
			 * Called when sign in required
			 */
			public void onSignInRequired(long paramLong) {
				postResult(createMediaChannelResult(new Status(ConnectionResult.SIGN_IN_REQUIRED))); // 4 :
																		// CommonStatusCode.SIGN_IN_REQUIRED
			}

			/**
			 * Called when there's track request
			 */
			public void onTrackRequest(long paramLong, int statusCode,
					JSONObject customData) {
				postResult(new MediaChannelResultImpl(new Status(statusCode),
						customData));
			}
		};

		/**
		 * Create result with status
		 *
		 * @param status
		 */
		protected MediaChannelResult createResult(final Status status) {
			return new MediaChannelResult() {
				public Status getStatus() {
					return status;
				}
			};
		};

		/**
		 * Create media channel result
		 *
		 * @param paramStatus
		 * @return
		 */
		public MediaChannelResult createMediaChannelResult(
				final Status paramStatus) {
			return new MediaChannelResult() {
				public Status getStatus() {
					return paramStatus;
				}
			};
		}
	}

	/**
	 * Media channel result implementation
	 */
	private static final class MediaChannelResultImpl implements
			MediaChannelResult {
		private final Status status;
		private final JSONObject json;

		MediaChannelResultImpl(Status paramStatus, JSONObject json) {
			this.status = paramStatus;
			this.json = json;
		}

		public Status getStatus() {
			return status;
		}
	}

	/**
	 * Message Sender implementation
	 */
	private class MessageSenderImpl implements MessageSender {
		/**
		 * Fling Api client
		 */
		private FlingManager client;

		/**
		 * request Id
		 */
		private long requestId = 0L;

		/**
		 * Set API client
		 *
		 * @param client
		 */
		public void setApiClient(FlingManager client) {
			this.client = client;
		}

		/**
		 * Get next request Id
		 */
		public long getRequestId() {
			return (++this.requestId);
		}

		/**
		 * Send message to device
		 *
		 * @param namespace
		 * @param message
		 * @param requestId
		 * @param paramString3
		 *
		 */
		public void sendMessage(String namespace, String message,
				long requestId, String paramString3) throws IOException {
			if (client == null) {
				throw new IOException("No FlingManager available");
			}
			Fling.FlingApi.sendMessage(client, namespace, message)
					.setResultCallback(new RequestResultCallback(requestId));
		}

		/**
		 * Request result callback
		 */
		private final class RequestResultCallback implements
				ResultCallback<Status> {
			/**
			 * request Id
			 */
			private final long reqId;

			/**
			 * Init with request Id
			 *
			 * @param requestId
			 */
			RequestResultCallback(long requestId) {
				this.reqId = requestId;
			}

			@Override
			public void onResult(Status status) {
				// TODO Auto-generated method stub
				if (status.isSuccess()) {
					return;
				}
				mMediaControlChannel.trackUnSuccess(this.reqId,
						status.getStatusCode());
			}
		}
	}

	/**
	 * Used to present a result of media command which is sent to Fling device
	 */
	public interface MediaChannelResult extends Result {
	}

	/**
	 * The listener interface for tracking media meta data changes. 
	 */
	public interface OnMetadataUpdatedListener {
		/**
		 * Called when updated media metadata is received.
		 */
		public abstract void onMetadataUpdated();
	}

	/**
	 * The listener interface for tracking player status changes. 
	 */
	public interface OnStatusUpdatedListener {
		/**
		 * Called when updated player status information is received.
		 */
		public abstract void onStatusUpdated();
	}

}
