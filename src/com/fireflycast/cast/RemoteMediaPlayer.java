package com.fireflycast.cast;

import java.io.IOException;

import org.json.JSONObject;

import com.fireflycast.client.internal.CastClientImpl_dq;
import com.fireflycast.client.internal.MediaControlChannelImpl_dv;
import com.fireflycast.client.internal.MessageSender_dw;
import com.fireflycast.client.internal.RequestTrackerCallback_dx;

public class RemoteMediaPlayer implements Cast.MessageReceivedCallback {
	public static final int RESUME_STATE_UNCHANGED = 0;
	public static final int RESUME_STATE_PLAY = 1;
	public static final int RESUME_STATE_PAUSE = 2;
	public static final int STATUS_SUCCEEDED = 0;
	public static final int STATUS_FAILED = 1;
	public static final int STATUS_CANCELED = 2;
	public static final int STATUS_TIMED_OUT = 3;
	public static final int STATUS_REPLACED = 4;

	private final Object mLock_mg = new Object();

	private final MediaControlChannelImpl_dv mMediaControlChannel_xg = new MediaControlChannelImpl_dv() {
		protected void onStatusUpdated() {
			RemoteMediaPlayer.this.onStatusUpdated();
		}

		protected void onMetadataUpdated() {
			RemoteMediaPlayer.this.onMetadataUpdated();
		}
	};

	private final MessageSenderImpl_a mMessageSender_xh = new MessageSenderImpl_a();
	private OnMetadataUpdatedListener mMetadataUpdatedListener_xi;
	private OnStatusUpdatedListener mStatusUpdatedListener_xj;

	public RemoteMediaPlayer() {
		this.mMediaControlChannel_xg.setMessageSender_a(this.mMessageSender_xh);
	}

	public PendingResult<MediaChannelResult> load(FireflyApiClient apiClient,
			MediaInfo mediaInfo) {
		return load(apiClient, mediaInfo, true, 0L, null);
	}

	public PendingResult<MediaChannelResult> load(FireflyApiClient apiClient,
			MediaInfo mediaInfo, boolean autoplay) {
		return load(apiClient, mediaInfo, autoplay, 0L, null);
	}

	public PendingResult<MediaChannelResult> load(FireflyApiClient apiClient,
			MediaInfo mediaInfo, boolean autoplay, long playPosition) {
		return load(apiClient, mediaInfo, autoplay, playPosition, null);
	}

	public PendingResult<MediaChannelResult> load(
			final FireflyApiClient apiClient, final MediaInfo mediaInfo,
			final boolean autoplay, final long playPosition,
			final JSONObject customData) {
		return apiClient.executeTask_b(new MediaChannelResultHandler_b() {
			protected void execute_a(CastClientImpl_dq dq) {
				synchronized (mLock_mg) {
					mMessageSender_xh.setApiClient_b(apiClient);
					try {
						mMediaControlChannel_xg.load_a(
								this.requestTrackerCallback_xy, mediaInfo,
								autoplay, playPosition, customData);
					} catch (IOException localIOException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender_xh.setApiClient_b(null);
					}
				}
			}
		});
	}

	public PendingResult<MediaChannelResult> pause(FireflyApiClient apiClient) {
		return pause(apiClient, null);
	}

	public PendingResult<MediaChannelResult> pause(
			final FireflyApiClient apiClient, final JSONObject customData) {
		return apiClient.executeTask_b(new MediaChannelResultHandler_b() {
			protected void execute_a(CastClientImpl_dq dq) {
				synchronized (mLock_mg) {
					mMessageSender_xh.setApiClient_b(apiClient);
					try {
						mMediaControlChannel_xg.pause_a(
								this.requestTrackerCallback_xy, customData);
					} catch (IOException localIOException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender_xh.setApiClient_b(null);
					}
				}
			}
		});
	}

	public PendingResult<MediaChannelResult> stop(FireflyApiClient apiClient) {
		return stop(apiClient, null);
	}

	public PendingResult<MediaChannelResult> stop(
			final FireflyApiClient apiClient, final JSONObject customData) {
		return apiClient.executeTask_b(new MediaChannelResultHandler_b() {
			protected void execute_a(CastClientImpl_dq dq) {
				synchronized (mLock_mg) {
					mMessageSender_xh.setApiClient_b(apiClient);
					try {
						mMediaControlChannel_xg.stop_b(
								this.requestTrackerCallback_xy, customData);
					} catch (IOException localIOException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender_xh.setApiClient_b(null);
					}
				}
			}
		});
	}

	public PendingResult<MediaChannelResult> play(FireflyApiClient apiClient) {
		return play(apiClient, null);
	}

	public PendingResult<MediaChannelResult> play(
			final FireflyApiClient apiClient, final JSONObject customData) {
		return apiClient.executeTask_b(new MediaChannelResultHandler_b() {
			protected void execute_a(CastClientImpl_dq dq) {
				synchronized (mLock_mg) {
					mMessageSender_xh.setApiClient_b(apiClient);
					try {
						mMediaControlChannel_xg.play_c(
								this.requestTrackerCallback_xy, customData);
					} catch (IOException localIOException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender_xh.setApiClient_b(null);
					}
				}
			}
		});
	}

	public PendingResult<MediaChannelResult> seek(FireflyApiClient apiClient,
			long position) {
		return seek(apiClient, position, 0, null);
	}

	public PendingResult<MediaChannelResult> seek(FireflyApiClient apiClient,
			long position, int resumeState) {
		return seek(apiClient, position, resumeState, null);
	}

	public PendingResult<MediaChannelResult> seek(
			final FireflyApiClient apiClient, final long position,
			final int resumeState, final JSONObject customData) {
		return apiClient.executeTask_b(new MediaChannelResultHandler_b() {
			protected void execute_a(CastClientImpl_dq dq) {
				synchronized (mLock_mg) {
					mMessageSender_xh.setApiClient_b(apiClient);
					try {
						mMediaControlChannel_xg.seed_a(
								this.requestTrackerCallback_xy, position,
								resumeState, customData);
					} catch (IOException localIOException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender_xh.setApiClient_b(null);
					}
				}
			}
		});
	}

	public PendingResult<MediaChannelResult> setStreamVolume(
			FireflyApiClient apiClient, double volume)
			throws IllegalArgumentException {
		return setStreamVolume(apiClient, volume, null);
	}

	public PendingResult<MediaChannelResult> setStreamVolume(
			final FireflyApiClient apiClient, final double volume,
			final JSONObject customData) throws IllegalArgumentException {
		if ((Double.isInfinite(volume)) || (Double.isNaN(volume))) {
			throw new IllegalArgumentException("Volume cannot be " + volume);
		}
		return apiClient.executeTask_b(new MediaChannelResultHandler_b() {
			protected void execute_a(CastClientImpl_dq dq) {
				synchronized (mLock_mg) {
					mMessageSender_xh.setApiClient_b(apiClient);
					try {
						mMediaControlChannel_xg.setStreamVolume_a(
								this.requestTrackerCallback_xy, volume,
								customData);
					} catch (IllegalStateException localIllegalStateException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} catch (IllegalArgumentException localIllegalArgumentException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} catch (IOException localIOException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender_xh.setApiClient_b(null);
					}
				}
			}
		});
	}

	public PendingResult<MediaChannelResult> setStreamMute(
			FireflyApiClient apiClient, boolean muteState) {
		return setStreamMute(apiClient, muteState, null);
	}

	public PendingResult<MediaChannelResult> setStreamMute(
			final FireflyApiClient apiClient, final boolean muteState,
			final JSONObject customData) {
		return apiClient.executeTask_b(new MediaChannelResultHandler_b() {
			protected void execute_a(CastClientImpl_dq dq) {
				synchronized (mLock_mg) {
					mMessageSender_xh.setApiClient_b(apiClient);
					try {
						mMediaControlChannel_xg.setStreamMute_a(
								this.requestTrackerCallback_xy, muteState,
								customData);
					} catch (IllegalStateException localIllegalStateException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} catch (IOException localIOException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender_xh.setApiClient_b(null);
					}
				}
			}
		});
	}

	public PendingResult<MediaChannelResult> requestStatus(
			final FireflyApiClient apiClient) {
		return apiClient.executeTask_b(new MediaChannelResultHandler_b() {
			protected void execute_a(CastClientImpl_dq paramdq) {
				synchronized (mLock_mg) {
					mMessageSender_xh.setApiClient_b(apiClient);
					try {
						mMediaControlChannel_xg
								.requestStatus_a(this.requestTrackerCallback_xy);
					} catch (IOException localIOException) {
						postResult_a(createMediaChannelResult_j(new Status(
								STATUS_FAILED)));
					} finally {
						mMessageSender_xh.setApiClient_b(null);
					}
				}
			}
		});
	}

	public long getApproximateStreamPosition() {
		synchronized (mLock_mg) {
			return mMediaControlChannel_xg.getApproximateStreamPosition();
		}
	}

	public long getStreamDuration() {
		synchronized (mLock_mg) {
			return mMediaControlChannel_xg.getStreamDuration();
		}
	}

	public MediaStatus getMediaStatus() {
		synchronized (mLock_mg) {
			return mMediaControlChannel_xg.getMediaStatus();
		}
	}

	public MediaInfo getMediaInfo() {
		synchronized (mLock_mg) {
			return mMediaControlChannel_xg.getMediaInfo();
		}
	}

	public void setOnStatusUpdatedListener(OnStatusUpdatedListener listener) {
		mStatusUpdatedListener_xj = listener;
	}

	private void onStatusUpdated() {
		if (mStatusUpdatedListener_xj == null) {
			return;
		}
		mStatusUpdatedListener_xj.onStatusUpdated();
	}

	public void setOnMetadataUpdatedListener(OnMetadataUpdatedListener listener) {
		mMetadataUpdatedListener_xi = listener;
	}

	private void onMetadataUpdated() {
		if (mMetadataUpdatedListener_xi == null) {
			return;
		}
		mMetadataUpdatedListener_xi.onMetadataUpdated();
	}

	public String getNamespace() {
		return mMediaControlChannel_xg.getNamespace();
	}

	public void onMessageReceived(CastDevice castDevice, String namespace,
			String message) {
		mMediaControlChannel_xg.onMessageReceived_P(message);
	}

	private static abstract class MediaChannelResultHandler_b extends
			Cast.PendingResultHandler_a<MediaChannelResult> {
		RequestTrackerCallback_dx requestTrackerCallback_xy = new RequestTrackerCallback_dx() {
			public void onSignInRequired_k(long paramLong) {
				postResult_a(createMediaChannelResult_j(new Status(4))); // 4 :
																			// CommonStatusCode.SIGN_IN_REQUIRED
			}

			public void onTrackRequest_a(long paramLong, int statusCode,
					JSONObject customData) {
				postResult_a(new MediaChannelResultImpl_c(
						new Status(statusCode), customData));
			}
		};

		protected MediaChannelResult createResult_d(final Status status) {
			return new MediaChannelResult() {
				public Status getStatus() {
					return status;
				}
			};
		};

		public MediaChannelResult createMediaChannelResult_j(
				final Status paramStatus) {
			return new MediaChannelResult() {
				public Status getStatus() {
					return paramStatus;
				}
			};
		}
	}

	private static final class MediaChannelResultImpl_c implements
			MediaChannelResult {
		private final Status status_vl;
		private final JSONObject json_wP;

		MediaChannelResultImpl_c(Status paramStatus, JSONObject json) {
			this.status_vl = paramStatus;
			this.json_wP = json;
		}

		public Status getStatus() {
			return status_vl;
		}
	}

	private class MessageSenderImpl_a implements MessageSender_dw {
		private FireflyApiClient client;
		private long requestId_xv = 0L;

		public void setApiClient_b(FireflyApiClient client) {
			this.client = client;
		}

		public long getRequestId_cV() {
			return (++this.requestId_xv);
		}

		public void sendMessage_a(String namespace, String message,
				long requestId, String paramString3) throws IOException {
			if (client == null) {
				throw new IOException("No GoogleApiClient available");
			}
			Cast.CastApi.sendMessage(client, namespace, message)
					.setResultCallback(new RequestResultCallback_a(requestId));
		}

		/*
		 * RemoteMediaPlayer$a$a ---> RemoteMediaPlayer$a$aa
		 */
		private final class RequestResultCallback_a implements
				ResultCallback<Status> {
			private final long requestId_xw;

			RequestResultCallback_a(long requestId) {
				this.requestId_xw = requestId;
			}

			@Override
			public void onResult(Status status) {
				// TODO Auto-generated method stub
				if (status.isSuccess()) {
					return;
				}
				mMediaControlChannel_xg.trackUnSuccess_a(this.requestId_xw,
						status.getStatusCode());
			}
		}
	}

	public interface MediaChannelResult extends Result {
	}

	public interface OnMetadataUpdatedListener {
		public void onMetadataUpdated();
	}

	public interface OnStatusUpdatedListener {
		public void onStatusUpdated();
	}

}
