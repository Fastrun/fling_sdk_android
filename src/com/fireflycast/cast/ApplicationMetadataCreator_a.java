package com.fireflycast.cast;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.cast.images.WebImage;
import com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a.SafeParcelA;
import com.fireflycast.client.common.internal.safeparcel.ParcelUtil_b;

/*
 * a.smali : OK
 */
public class ApplicationMetadataCreator_a implements
		Parcelable.Creator<ApplicationMetadata> {
	static void buildParcel_a(ApplicationMetadata paramApplicationMetadata,
			Parcel paramParcel, int paramInt) {
		int i = ParcelUtil_b.p(paramParcel);
		ParcelUtil_b.c(paramParcel, 1, paramApplicationMetadata.getVersionCode());
		ParcelUtil_b.a(paramParcel, 2, paramApplicationMetadata.getApplicationId(), false);
		ParcelUtil_b.a(paramParcel, 3, paramApplicationMetadata.getName(), false);
		ParcelUtil_b.b(paramParcel, 4, paramApplicationMetadata.getImages(), false);
		ParcelUtil_b.a(paramParcel, 5, paramApplicationMetadata.mNamespaces_wm, false);
		ParcelUtil_b.a(paramParcel, 6, paramApplicationMetadata.getSenderAppIdentifier(),
				false);
		ParcelUtil_b.a(paramParcel, 7,
				paramApplicationMetadata.getSenderAppLaunchUrl_cR(), paramInt,
				false);
		ParcelUtil_b.D(paramParcel, i);
	}

	public ApplicationMetadata createFromParcel(Parcel paramParcel) {
		int i = com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a
				.o(paramParcel);
		int j = 0;
		String str1 = null;
		String str2 = null;
		ArrayList images = null;
		ArrayList namespaces = null;
		String str3 = null;
		Uri localUri = null;
		while (paramParcel.dataPosition() < i) {
			int k = com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a
					.readInt_n(paramParcel);
			switch (com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a.S(k)) {
			case 1:
				j = com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a.g(
						paramParcel, k);
				break;
			case 2:
				str1 = com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a.m(
						paramParcel, k);
				break;
			case 3:
				str2 = com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a.m(
						paramParcel, k);
				break;
			case 4:
				images = com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a
						.c(paramParcel, k, WebImage.CREATOR);
				break;
			case 5:
				namespaces = com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a
						.y(paramParcel, k);
				break;
			case 6:
				str3 = com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a.m(
						paramParcel, k);
				break;
			case 7:
				localUri = (Uri) com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a
						.a(paramParcel, k, Uri.CREATOR);
				break;
			default:
				com.fireflycast.client.common.internal.safeparcel.ParcelUtil_a.b(
						paramParcel, k);
			}
		}
		if (paramParcel.dataPosition() != i)
			throw new SafeParcelA("Overread allowed size end=" + i, paramParcel);
		ApplicationMetadata localApplicationMetadata = new ApplicationMetadata(
				j, str1, str2, images, namespaces, str3, localUri);
		return localApplicationMetadata;

	}

	@Override
	public ApplicationMetadata[] newArray(int arg0) {
		return new ApplicationMetadata[arg0];
	}
}
