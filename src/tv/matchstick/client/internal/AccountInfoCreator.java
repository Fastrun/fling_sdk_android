package tv.matchstick.client.internal;

import java.util.ArrayList;

import tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil;
import tv.matchstick.client.common.internal.safeparcel.ParcelWriteUtil;
import tv.matchstick.client.internal.AccountInfo;
import tv.matchstick.client.internal.AccountInfo.AccountInfo_a;
import android.os.Parcel;
import android.os.Parcelable;

class AccountInfoCreator implements
		Parcelable.Creator<AccountInfo.AccountInfo_a> {

	static void buildParcel(AccountInfo.AccountInfo_a parama,
			Parcel paramParcel, int paramInt) {
		int i = ParcelWriteUtil.p(paramParcel);
		ParcelWriteUtil.a(paramParcel, 1, parama.getAccountName(), false);
		ParcelWriteUtil.c(paramParcel, 1000, parama.getVersionCode());
		ParcelWriteUtil.a(paramParcel, 2, parama.copyScopeUriList(), false);
		ParcelWriteUtil.c(paramParcel, 3, parama.getGravityForPopups());
		ParcelWriteUtil.a(paramParcel, 4, parama.getPackageName(), false);
		ParcelWriteUtil.D(paramParcel, i);
	}

	public AccountInfo_a createFromParcel(Parcel paramParcel) {
		int i = ParcelReadUtil.o(paramParcel);
		int j = 0;
		String str1 = null;
		ArrayList<String> localArrayList = null;
		int k = 0;
		String str2 = null;
		while (paramParcel.dataPosition() < i) {
			int l = ParcelReadUtil.readInt_n(paramParcel);
			switch (ParcelReadUtil.S(l)) {
			case 1:
				str1 = ParcelReadUtil.m(paramParcel, l);
				break;
			case 1000:
				j = ParcelReadUtil.g(paramParcel, l);
				break;
			case 2:
				localArrayList = ParcelReadUtil.y(paramParcel, l);
				break;
			case 3:
				k = ParcelReadUtil.g(paramParcel, l);
				break;
			case 4:
				str2 = ParcelReadUtil.m(paramParcel, l);
				break;
			default:
				ParcelReadUtil.b(paramParcel, l);
			}
		}
		if (paramParcel.dataPosition() != i) {
			throw new ParcelReadUtil.SafeParcelA("Overread allowed size end="
					+ i, paramParcel);
		}
		AccountInfo.AccountInfo_a locala = new AccountInfo.AccountInfo_a(j,
				str1, localArrayList, k, str2);
		return locala;
	}

	public AccountInfo_a[] newArray(int arg0) {
		return new AccountInfo.AccountInfo_a[arg0];
	}

}
