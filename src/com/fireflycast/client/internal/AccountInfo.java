package com.fireflycast.client.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.os.Parcel;
import android.view.View;

import com.fireflycast.client.common.internal.safeparcel.SafeParcelable;
import com.fireflycast.client.internal.AccountInfoCreator;

public class AccountInfo {

	private final AccountInfo_a accountInfo_Bo;
	private final View mView_zp;

	public AccountInfo(String accountName,
			Collection<String> scopeUriCollection, int gravityForPopups,
			View view, String packageName) {
		accountInfo_Bo = new AccountInfo_a(accountName, scopeUriCollection,
				gravityForPopups, packageName);
		mView_zp = view;
	}

	public String getAccountName() {
		return accountInfo_Bo.getAccountName();
	}

	public String getAccountNameNotNull_dR() {
		return accountInfo_Bo.getAccountNameNotNull_dR();
	}

	public int getGravityForPopups_dS() {
		return accountInfo_Bo.getGravityForPopups_dS();
	}

	public List<String> copyScopeUriList_dT() {
		return accountInfo_Bo.copyScopeUriList_dT();
	}

	public String[] convertListToArray_dU() {
		return accountInfo_Bo.copyScopeUriList_dT().toArray(new String[0]);
	}

	public String getPackageName_dV() {
		return accountInfo_Bo.getPackageName_dV();
	}

	public View getView_dW() {
		return mView_zp;
	}

	public static final class AccountInfo_a implements SafeParcelable {
		public static final AccountInfoCreator CREATOR = new AccountInfoCreator();
		private final int mVersionCode_wj;
		private final String mAccountName_vi;
		private final List<String> mScopeUriList_Bp;
		private final int mGravityForPopups_zo;
		private final String mPackageName_zq;

		AccountInfo_a(int versionCode, String accountName,
				List<String> scopeUriList, int gravityForPopups,
				String packageName) {
			mScopeUriList_Bp = new ArrayList<String>();
			mScopeUriList_Bp.addAll(scopeUriList);

			mVersionCode_wj = versionCode;
			mAccountName_vi = accountName;

			mGravityForPopups_zo = gravityForPopups;
			mPackageName_zq = packageName;
		}

		public AccountInfo_a(String accountName,
				Collection<String> scopeUriCollection, int gravityForPopups,
				String packageName) {
			this(3, accountName, new ArrayList<String>(scopeUriCollection),
					gravityForPopups, packageName);
		}

		public String getAccountName() {
			return mAccountName_vi;
		}

		public String getAccountNameNotNull_dR() {
			return ((mAccountName_vi != null) ? mAccountName_vi
					: "<<default account>>");
		}

		public int getGravityForPopups_dS() {
			return mGravityForPopups_zo;
		}

		public String getPackageName_dV() {
			return mPackageName_zq;
		}

		public List<String> copyScopeUriList_dT() {
			return new ArrayList<String>(mScopeUriList_Bp);
		}

		public int describeContents() {
			return 0;
		}

		public void writeToParcel(Parcel out, int flags) {
			AccountInfoCreator.buildParcel_a(this, out, flags);
		}

		public int getVersionCode() {
			return mVersionCode_wj;
		}
	}

}
