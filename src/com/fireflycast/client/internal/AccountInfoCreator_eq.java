package com.fireflycast.client.internal;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a;
import com.fireflycast.client.common.internal.safeparcel.ParcelUtil_b;
import com.fireflycast.client.internal.AccountInfo_ee;
import com.fireflycast.client.internal.AccountInfo_ee.AccountInfo_a;

public class AccountInfoCreator_eq implements
		Parcelable.Creator<AccountInfo_ee.AccountInfo_a> {

	static void buildParcel_a(AccountInfo_ee.AccountInfo_a parama,
			Parcel paramParcel, int paramInt) {
		int i = ParcelUtil_b.p(paramParcel);
		ParcelUtil_b.a(paramParcel, 1, parama.getAccountName(), false);
		ParcelUtil_b.c(paramParcel, 1000, parama.getVersionCode());
		ParcelUtil_b.a(paramParcel, 2, parama.copyScopeUriList_dT(), false);
		ParcelUtil_b.c(paramParcel, 3, parama.getGravityForPopups_dS());
		ParcelUtil_b.a(paramParcel, 4, parama.getPackageName_dV(), false);
		ParcelUtil_b.D(paramParcel, i);
	}

	public AccountInfo_a createFromParcel(Parcel paramParcel) {
		int i = ParcelUtil_a.o(paramParcel);
		int j = 0;
		String str1 = null;
		ArrayList<String> localArrayList = null;
		int k = 0;
		String str2 = null;
		while (paramParcel.dataPosition() < i) {
			int l = ParcelUtil_a.readInt_n(paramParcel);
			switch (ParcelUtil_a.S(l)) {
			case 1:
				str1 = ParcelUtil_a.m(paramParcel, l);
				break;
			case 1000:
				j = ParcelUtil_a.g(paramParcel, l);
				break;
			case 2:
				localArrayList = ParcelUtil_a.y(paramParcel, l);
				break;
			case 3:
				k = ParcelUtil_a.g(paramParcel, l);
				break;
			case 4:
				str2 = ParcelUtil_a.m(paramParcel, l);
				break;
			default:
				ParcelUtil_a.b(paramParcel, l);
			}
		}
		if (paramParcel.dataPosition() != i) {
			throw new ParcelUtil_a.SafeParcelA("Overread allowed size end=" + i,
					paramParcel);
		}
		AccountInfo_ee.AccountInfo_a locala = new AccountInfo_ee.AccountInfo_a(
				j, str1, localArrayList, k, str2);
		return locala;
	}

	public AccountInfo_a[] newArray(int arg0) {
		return new AccountInfo_ee.AccountInfo_a[arg0];
	}

}
