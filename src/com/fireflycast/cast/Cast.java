package com.fireflycast.cast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;

import com.fireflycast.cast.FireflyApiClient.ApiOptions;
import com.fireflycast.cast.FireflyApiClient.ConnectionCallbacks;
import com.fireflycast.cast.FireflyApiClient.OnConnectionFailedListener;
import com.fireflycast.cast.FireflyApi.FireflyApiImpl_a;
import com.fireflycast.client.internal.AccountInfo;
import com.fireflycast.client.internal.CastClientImpl;
import com.fireflycast.client.internal.ValueChecker;

public class Cast {
	public static final int MAX_MESSAGE_LENGTH = 65536;
	public static final int MAX_NAMESPACE_LENGTH = 128;
	public static final String EXTRA_APP_NO_LONGER_RUNNING = "com.fireflycast.cast.EXTRA_APP_NO_LONGER_RUNNING";

	/*
	 * Cast$1.smali : OK
	 */
	static final Api.ConnectionBuilder_b<CastClientImpl> mConnectionBuilder_va = new Api.ConnectionBuilder_b<CastClientImpl>() {
		public int getPriority() {
			return -1;
		}

		public CastClientImpl build_b(Context context, Looper looper,
				AccountInfo account, ApiOptions options,
				ConnectionCallbacks callbacks,
				OnConnectionFailedListener failedListener) {
			ValueChecker.checkNullPointer_b(options,
					"Setting the API options is required.");
			ValueChecker.checkTrueWithErrorMsg(options instanceof CastOptions,
					"Must provide valid CastOptions!");

			CastOptions castOptions = (CastOptions) options;
			return new CastClientImpl(context, looper,
					castOptions.castDevice_wv, castOptions.loggingFlag_wx,
					castOptions.castListener_ww, callbacks, failedListener);
		}
	};
	public static final Api API = new Api(mConnectionBuilder_va);
	public static final CastApi CastApi = new CastApi.CastApiImpl_a();

	/*
	 * The main entry point for interacting with a Cast device.
	 */
	public interface CastApi {
	    public abstract String makeApplicationId(String appUrl);
	    
	    public abstract String makeApplicationId(String id, String msUrl);
	    
		public abstract void requestStatus(FireflyApiClient client) throws IOException,
				IllegalStateException;

		public abstract PendingResult<Status> sendMessage(FireflyApiClient client,
				String namespace, String message);

		public abstract PendingResult<ApplicationConnectionResult> launchApplication(
				FireflyApiClient client, String applicationId);

		public abstract PendingResult<ApplicationConnectionResult> launchApplication(
				FireflyApiClient client, String applicationId,
				boolean relaunchIfRunning);

		public abstract PendingResult<ApplicationConnectionResult> joinApplication(
				FireflyApiClient client, String applicationId,
				String sessionId);

		public abstract PendingResult<ApplicationConnectionResult> joinApplication(
				FireflyApiClient client, String applicationId);

		public abstract PendingResult<ApplicationConnectionResult> joinApplication(
				FireflyApiClient client);

		public abstract PendingResult<Status> leaveApplication(FireflyApiClient client);

		public abstract PendingResult<Status> stopApplication(FireflyApiClient client);

		public abstract PendingResult<Status> stopApplication(FireflyApiClient client,
				String sessionId);

		public abstract void setVolume(FireflyApiClient client, double volume)
				throws IOException, IllegalArgumentException,
				IllegalStateException;

		public abstract double getVolume(FireflyApiClient client)
				throws IllegalStateException;

		public abstract void setMute(FireflyApiClient client, boolean mute)
				throws IOException, IllegalStateException;

		public abstract boolean isMute(FireflyApiClient client)
				throws IllegalStateException;

		public abstract ApplicationMetadata getApplicationMetadata(
				FireflyApiClient client) throws IllegalStateException;

		public abstract String getApplicationStatus(FireflyApiClient client)
				throws IllegalStateException;

		public abstract void setMessageReceivedCallbacks(FireflyApiClient client,
				String namespace, MessageReceivedCallback callback)
				throws IOException, IllegalStateException;

		public abstract void removeMessageReceivedCallbacks(FireflyApiClient client,
				String namespace) throws IOException, IllegalArgumentException;

		/*
		 * Implementation of CastApi interface
		 */
		public static final class CastApiImpl_a implements CastApi {
		    public String makeApplicationId(String appUrl) {
                try {
                    String  encode = URLEncoder.encode(appUrl, "UTF-8");
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
		    
			public void requestStatus(FireflyApiClient client)
					throws IOException, IllegalStateException {
				try {
					client.getConnectionApi_a(Cast.mConnectionBuilder_va)
							.requestStatus_cZ();
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}

			public PendingResult<Status> sendMessage(FireflyApiClient client,
					final String namespace, final String message) {
				return client.executeTask_b(new StatusResultHandler_b() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						try {
							dq.sendMessage_a(namespace, message, this);
						} catch (IllegalArgumentException localIllegalArgumentException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<ApplicationConnectionResult> launchApplication(
					FireflyApiClient client, final String applicationId) {
				return client.executeTask_b(new ApplicationConnectionResultHandler_c() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						try {
							dq.launchApplication_a(applicationId, false, this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<ApplicationConnectionResult> launchApplication(
					FireflyApiClient client, final String applicationId,
					final boolean relaunchIfRunning) {
				return client.executeTask_b(new ApplicationConnectionResultHandler_c() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						try {
							dq.launchApplication_a(applicationId,
									relaunchIfRunning, this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<ApplicationConnectionResult> joinApplication(
					FireflyApiClient client, final String applicationId,
					final String sessionId) {
				return client.executeTask_b(new ApplicationConnectionResultHandler_c() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						try {
							dq.joinApplication_b(applicationId, sessionId, this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<ApplicationConnectionResult> joinApplication(
					FireflyApiClient client, final String applicationId) {
				return client.executeTask_b(new ApplicationConnectionResultHandler_c() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						try {
							dq.joinApplication_b(applicationId, null, this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<ApplicationConnectionResult> joinApplication(
					FireflyApiClient client) {
				return client.executeTask_b(new ApplicationConnectionResultHandler_c() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						try {
							dq.joinApplication_b(null, null, this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<Status> leaveApplication(
					FireflyApiClient client) {
				return client.executeTask_b(new StatusResultHandler_b() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						try {
							dq.leaveApplication_e(this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<Status> stopApplication(FireflyApiClient client) {
				return client.executeTask_b(new StatusResultHandler_b() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						try {
							dq.stopApplication_a("", this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public PendingResult<Status> stopApplication(
					FireflyApiClient client, final String sessionId) {
				return client.executeTask_b(new StatusResultHandler_b() {
					protected void execute_a(CastClientImpl dq)
							throws RemoteException {
						if (TextUtils.isEmpty(sessionId)) {
							notifyResult_c(CastStatusCodes.INVALID_REQUEST,
									"IllegalArgument: sessionId cannot be null or empty");
							return;
						}
						try {
							dq.stopApplication_a(sessionId, this);
						} catch (IllegalStateException localIllegalStateException) {
							notifyResult_x(CastStatusCodes.INVALID_REQUEST);
						}
					}
				});
			}

			public void setVolume(FireflyApiClient client, double volume)
					throws IOException, IllegalArgumentException,
					IllegalStateException {
				try {
					client.getConnectionApi_a(Cast.mConnectionBuilder_va)
							.setVolume_a(volume);
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}

			public double getVolume(FireflyApiClient client)
					throws IllegalStateException {
				return client.getConnectionApi_a(Cast.mConnectionBuilder_va)
						.getVolume_da();
			}

			public void setMute(FireflyApiClient client, boolean mute)
					throws IOException, IllegalStateException {
				try {
					client.getConnectionApi_a(Cast.mConnectionBuilder_va)
							.setMute_t(mute);
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}

			public boolean isMute(FireflyApiClient client)
					throws IllegalStateException {
				return client.getConnectionApi_a(Cast.mConnectionBuilder_va)
						.isMute();
			}

			public ApplicationMetadata getApplicationMetadata(
					FireflyApiClient client) throws IllegalStateException {
				return client.getConnectionApi_a(Cast.mConnectionBuilder_va)
						.getApplicationMetadata();
			}

			public String getApplicationStatus(FireflyApiClient client)
					throws IllegalStateException {
				return client.getConnectionApi_a(Cast.mConnectionBuilder_va)
						.getApplicationStatus();
			}

			public void setMessageReceivedCallbacks(FireflyApiClient client,
					String namespace, Cast.MessageReceivedCallback callbacks)
					throws IOException, IllegalStateException {
				try {
					client.getConnectionApi_a(Cast.mConnectionBuilder_va)
							.setMessageReceivedCallbacks_a(namespace, callbacks);
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}

			public void removeMessageReceivedCallbacks(FireflyApiClient client,
					String namespace) throws IOException,
					IllegalArgumentException {
				try {
					client.getConnectionApi_a(Cast.mConnectionBuilder_va)
							.removeMessageReceivedCallbacks_Q(namespace);
				} catch (RemoteException localRemoteException) {
					throw new IOException("service error");
				}
			}
		}
	}

	/*
	 * Cast$b.smali : OK
	 */
	static abstract class StatusResultHandler_b extends
			PendingResultHandler_a<Status> {
		@Override
		protected Status createResult_d(Status status) {
			// TODO Auto-generated method stub
			return status;
		}
	}

	/*
	 * Cast$c.smali : OK
	 */
	static abstract class ApplicationConnectionResultHandler_c extends
			PendingResultHandler_a<ApplicationConnectionResult> {
		@Override
		protected ApplicationConnectionResult createResult_d(final Status status) {
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

	public static abstract class PendingResultHandler_a<R extends Result>
			extends FireflyApiImpl_a<R, CastClientImpl> implements
			PendingResult<R> {
		public PendingResultHandler_a() {
			super(Cast.mConnectionBuilder_va);
		}

		public void notifyResult_x(int statusCode) {
			postResult_a(createResult_d(new Status(statusCode)));
		}

		public void notifyResult_c(int statusCode, String statusMessage) {
			postResult_a(createResult_d(new Status(statusCode, statusMessage,
					null)));
		}
	}

	public static final class CastOptions implements
			FireflyApiClient.ApiOptions {
		final CastDevice castDevice_wv;
		final Listener castListener_ww;
		private final int loggingFlag_wx;

		private CastOptions(Builder builder) {
			this.castDevice_wv = builder.mCastDevice_wy;
			this.castListener_ww = builder.mListener_wz;
			this.loggingFlag_wx = builder.mLoggingFlag_wA;
		}

		public static Builder builder(CastDevice castDevice,
				Listener castListener) {
			return new Builder(castDevice, castListener);
		}

		/*
		 * Cast$CastOptions$Builder.smali : OK
		 */
		public static final class Builder {
			CastDevice mCastDevice_wy;
			Listener mListener_wz;
			private int mLoggingFlag_wA;

			private Builder(CastDevice castDevice, Listener castListener) {
				ValueChecker.checkNullPointer_b(castDevice,
						"CastDevice parameter cannot be null");
				ValueChecker.checkNullPointer_b(castListener,
						"CastListener parameter cannot be null");
				this.mCastDevice_wy = castDevice;
				this.mListener_wz = castListener;
				this.mLoggingFlag_wA = 0;
			}

			public Builder setVerboseLoggingEnabled(boolean enabled) {
				if (enabled) {
					this.mLoggingFlag_wA |= 1;
				} else {
					this.mLoggingFlag_wA &= -2;
				}
				return this;
			}

			public CastOptions build() {
				return new CastOptions(this);
			}
		}
	}

	/*
	 * Cast$ApplicationConnectionResult.smali : OK
	 */
	public interface ApplicationConnectionResult extends Result {
		public abstract ApplicationMetadata getApplicationMetadata();

		public abstract String getApplicationStatus();

		public abstract String getSessionId();

		public abstract boolean getWasLaunched();
	}

	/*
	 * Cast$MessageReceivedCallback.smali : OK
	 */
	public interface MessageReceivedCallback {
		public abstract void onMessageReceived(CastDevice castDevice, String namespace,
				String message);
	}

	/*
	 * Cast$Listener.smali : OK
	 */
	public static abstract class Listener {
		public void onApplicationStatusChanged() {
		}

		public void onApplicationDisconnected(int statusCode) {
		}

		public void onVolumeChanged() {
		}
	}
}
