package tv.matchstick.client.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tv.matchstick.client.common.internal.safeparcel.SafeParcelable;
import tv.matchstick.client.internal.AccountInfoCreator;
import android.os.Parcel;
import android.view.View;

public class AccountInfo {

	private final AccountInfo_a accountInfo;
	private final View mView;

	public AccountInfo(String accountName,
			Collection<String> scopeUriCollection, int gravityForPopups,
			View view, String packageName) {
		accountInfo = new AccountInfo_a(accountName, scopeUriCollection,
				gravityForPopups, packageName);
		mView = view;
	}

	public String getAccountName() {
		return accountInfo.getAccountName();
	}

	public String getAccountNameNotNull() {
		return accountInfo.getAccountNameNotNull();
	}

	public int getGravityForPopups() {
		return accountInfo.getGravityForPopups();
	}

	public List<String> copyScopeUriList() {
		return accountInfo.copyScopeUriList();
	}

	public String[] convertListToArray() {
		return accountInfo.copyScopeUriList().toArray(new String[0]);
	}

	public String getPackageName() {
		return accountInfo.getPackageName();
	}

	public View getView() {
		return mView;
	}

	public static final class AccountInfo_a implements SafeParcelable {
		public static final AccountInfoCreator CREATOR = new AccountInfoCreator();
		private final int mVersionCode;
		private final String mAccountName;
		private final List<String> mScopeUriList;
		private final int mGravityForPopups;
		private final String mPackageName;

		AccountInfo_a(int versionCode, String accountName,
				List<String> scopeUriList, int gravityForPopups,
				String packageName) {
			mScopeUriList = new ArrayList<String>();
			mScopeUriList.addAll(scopeUriList);

			mVersionCode = versionCode;
			mAccountName = accountName;

			mGravityForPopups = gravityForPopups;
			mPackageName = packageName;
		}

		public AccountInfo_a(String accountName,
				Collection<String> scopeUriCollection, int gravityForPopups,
				String packageName) {
			this(3, accountName, new ArrayList<String>(scopeUriCollection),
					gravityForPopups, packageName);
		}

		public String getAccountName() {
			return mAccountName;
		}

		public String getAccountNameNotNull() {
			return ((mAccountName != null) ? mAccountName
					: "<<default account>>");
		}

		public int getGravityForPopups() {
			return mGravityForPopups;
		}

		public String getPackageName() {
			return mPackageName;
		}

		public List<String> copyScopeUriList() {
			return new ArrayList<String>(mScopeUriList);
		}

		public int describeContents() {
			return 0;
		}

		public void writeToParcel(Parcel out, int flags) {
			AccountInfoCreator.buildParcel(this, out, flags);
		}

		public int getVersionCode() {
			return mVersionCode;
		}
	}

}
