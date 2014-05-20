package com.fireflycast.cast;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.IntentSender;
import android.os.Parcel;

import com.fireflycast.client.common.api.CommonStatusCodes;
import com.fireflycast.client.common.internal.safeparcel.SafeParcelable;
import com.fireflycast.client.internal.StringBuilder_ep;

/*
 * Status.smali : OK
 */
public class Status implements Result, SafeParcelable {
	public static final Status SuccessStatus_zQ = new Status(0, null, null);
	public static final Status InterruptedStatus_zR = new Status(14, null, null);
	public static final Status TimeOutStatus_zS = new Status(15, null, null);
	public static final StatusCreator CREATOR = new StatusCreator();
	private final int mVersionCode_wj;
	private final int mStatusCode_yJ;
	private final String mStatusMessage_zT;
	private final PendingIntent mPendingIntent;

	Status(int versionCode, int statusCode, String statusMessage,
			PendingIntent pendingIntent) {
		this.mVersionCode_wj = versionCode;
		this.mStatusCode_yJ = statusCode;
		this.mStatusMessage_zT = statusMessage;
		this.mPendingIntent = pendingIntent;
	}

	public Status(int statusCode) {
		this(1, statusCode, null, null);
	}

	public Status(int statusCode, String statusMessage,
			PendingIntent pendingIntent) {
		this(1, statusCode, statusMessage, pendingIntent);
	}

	public void startResolutionForResult(Activity activity, int requestCode)
			throws IntentSender.SendIntentException {
		if (!(hasResolution())) {
			return;
		}
		activity.startIntentSenderForResult(mPendingIntent.getIntentSender(),
				requestCode, null, 0, 0, 0);
	}

	PendingIntent getPendingIntent_dE() {
		return mPendingIntent;
	}

	String getStatusMessage_dF() {
		return mStatusMessage_zT;
	}

	int getVersionCode() {
		return mVersionCode_wj;
	}

	public boolean hasResolution() {
		return (mPendingIntent != null);
	}

	public boolean isSuccess() {
		return (this.mStatusCode_yJ <= 0);
	}

	public boolean isInterrupted() {
		return (this.mStatusCode_yJ == 14);
	}

	public int getStatusCode() {
		return this.mStatusCode_yJ;
	}

	public PendingIntent getResolution() {
		return this.mPendingIntent;
	}

	public int hashCode() {
		return StringBuilder_ep.hashCode(new Object[] {
				Integer.valueOf(this.mVersionCode_wj),
				Integer.valueOf(this.mStatusCode_yJ), this.mStatusMessage_zT,
				this.mPendingIntent });
	}

	public boolean equals(Object obj) {
		if (!(obj instanceof Status))
			return false;
		Status localStatus = (Status) obj;
		return ((this.mVersionCode_wj == localStatus.mVersionCode_wj)
				&& (this.mStatusCode_yJ == localStatus.mStatusCode_yJ)
				&& (StringBuilder_ep.compare(this.mStatusMessage_zT,
						localStatus.mStatusMessage_zT)) && (StringBuilder_ep
					.compare(this.mPendingIntent, localStatus.mPendingIntent)));
	}

	private String getStatusMessage_dn() {
		if (this.mStatusMessage_zT != null) {
			return this.mStatusMessage_zT;
		}
		return CommonStatusCodes.getStatusMessage(this.mStatusCode_yJ);
	}

	public String toString() {
		return StringBuilder_ep.newStringBuilder_e(this)
				.append_a("statusCode", getStatusMessage_dn())
				.append_a("resolution", this.mPendingIntent).toString();
	}

	/*
	 * implementation of android.os.Parcelable
	 */
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		StatusCreator.buildParcel_a(this, out, flags);
	}

	/*
	 * implementation of Result
	 */
	public Status getStatus() {
		return this;
	}
}
