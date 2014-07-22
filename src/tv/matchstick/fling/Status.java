package tv.matchstick.fling;

import tv.matchstick.client.common.api.CommonStatusCodes;
import tv.matchstick.client.common.internal.safeparcel.SafeParcelable;
import tv.matchstick.client.internal.MyStringBuilder;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import android.os.Parcel;

/**
 * Represents the results of work.
 */
public class Status implements Result, SafeParcelable {
	/**
	 * Success Status
	 */
	public static final Status SuccessStatus = new Status(0, null, null);

	/**
	 * Interrupted Status
	 */
	public static final Status InterruptedStatus = new Status(14, null, null);

	/**
	 * Timeout Status
	 */
	public static final Status TimeOutStatus = new Status(15, null, null);

	/**
	 * Status Creator
	 */
	public static final StatusCreator CREATOR = new StatusCreator();

	/**
	 * Client's version code
	 */
	private final int mVersionCode;

	/**
	 * Status code
	 */
	private final int mStatusCode;

	/**
	 * Status message
	 */
	private final String mStatusMessage;

	/**
	 * Pending intent
	 */
	private final PendingIntent mPendingIntent;

	/**
	 * Fling Status constructor
	 *
	 * @param versionCode
	 * @param statusCode
	 * @param statusMessage
	 * @param pendingIntent
	 */
	Status(int versionCode, int statusCode, String statusMessage,
			PendingIntent pendingIntent) {
		this.mVersionCode = versionCode;
		this.mStatusCode = statusCode;
		this.mStatusMessage = statusMessage;
		this.mPendingIntent = pendingIntent;
	}

	/**
	 * Create status object
	 *
	 * @param statusCode
	 *            status code
	 */
	public Status(int statusCode) {
		this(1, statusCode, null, null);
	}

	/**
	 * Create Status object
	 *
	 * @param statusCode
	 *            status code
	 * @param statusMessage
	 *            status message
	 * @param pendingIntent
	 *            pending intent
	 */
	public Status(int statusCode, String statusMessage,
			PendingIntent pendingIntent) {
		this(1, statusCode, statusMessage, pendingIntent);
	}

	/**
	 * Start resolution activity
	 *
	 * @param activity
	 *            activity context
	 * @param requestCode
	 *            status code
	 * @throws IntentSender.SendIntentException
	 */
	/*
	public void startResolutionForResult(Activity activity, int requestCode)
			throws IntentSender.SendIntentException {
		if (!(hasResolution())) {
			return;
		}
		activity.startIntentSenderForResult(mPendingIntent.getIntentSender(),
				requestCode, null, 0, 0, 0);
	}
	*/

	/**
	 * Get current pending intent
	 *
	 * @return pending intent
	 */
	PendingIntent getPendingIntent() {
		return mPendingIntent;
	}

	/**
	 * Get status message
	 *
	 * @return status message
	 */
	String getStatusMessage() {
		return mStatusMessage;
	}

	/**
	 * Get version code
	 *
	 * @return version code
	 */
	int getVersionCode() {
		return mVersionCode;
	}

	/**
	 * Whether there's resolution pending intent
	 *
	 * @return
	 */
	/*
	public boolean hasResolution() {
		return (mPendingIntent != null);
	}
	*/

	/**
	 * Whether operation was successful
	 *
	 * @return status code
	 */
	public boolean isSuccess() {
		return (this.mStatusCode <= 0);
	}

	/**
	 * Whether operation was interrupted
	 *
	 * @return
	 */
	public boolean isInterrupted() {
		return (this.mStatusCode == 14);
	}

	/**
	 * Indicates the status of the operation.
	 *
	 * @return Status code
	 */
	public int getStatusCode() {
		return this.mStatusCode;
	}

	/**
	 * A pending intent to resolve the failure.
	 *
	 * @return pending intent
	 */
	/*
	public PendingIntent getResolution() {
		return this.mPendingIntent;
	}
	*/

	@Override
	public int hashCode() {
		return MyStringBuilder.hashCode(new Object[] {
				Integer.valueOf(this.mVersionCode),
				Integer.valueOf(this.mStatusCode), this.mStatusMessage,
				this.mPendingIntent });
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Status))
			return false;
		Status localStatus = (Status) obj;
		return ((this.mVersionCode == localStatus.mVersionCode)
				&& (this.mStatusCode == localStatus.mStatusCode)
				&& (MyStringBuilder.compare(this.mStatusMessage,
						localStatus.mStatusMessage)) && (MyStringBuilder
					.compare(this.mPendingIntent, localStatus.mPendingIntent)));
	}

	/**
	 * Get status message
	 *
	 * @return
	 */
	private String getStatusMessageInternal() {
		if (this.mStatusMessage != null) {
			return this.mStatusMessage;
		}
		return CommonStatusCodes.getStatusMessage(this.mStatusCode);
	}

	@Override
	public String toString() {
		return MyStringBuilder.newStringBuilder(this)
				.append("statusCode", getStatusMessageInternal())
				.append("resolution", this.mPendingIntent).toString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel out, int flags) {
		StatusCreator.buildParcel(this, out, flags);
	}

	/**
	 * Get status.
	 *
	 * implementation of Result
	 */
	@Override
	public Status getStatus() {
		return this;
	}
}
