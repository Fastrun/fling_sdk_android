package com.fireflycast.cast;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.cast.images.WebImage;
import com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil.SafeParcelA;
import com.fireflycast.client.common.internal.safeparcel.ParcelWriteUtil;

/*
 * a.smali : OK
 */
public class ApplicationMetadataCreator implements
		Parcelable.Creator<ApplicationMetadata> {
	static void buildParcel_a(ApplicationMetadata paramApplicationMetadata,
			Parcel paramParcel, int paramInt) {
		int i = ParcelWriteUtil.p(paramParcel);
		ParcelWriteUtil.c(paramParcel, 1, paramApplicationMetadata.getVersionCode());
		ParcelWriteUtil.a(paramParcel, 2, paramApplicationMetadata.getApplicationId(), false);
		ParcelWriteUtil.a(paramParcel, 3, paramApplicationMetadata.getName(), false);
		ParcelWriteUtil.b(paramParcel, 4, paramApplicationMetadata.getImages(), false);
		ParcelWriteUtil.a(paramParcel, 5, paramApplicationMetadata.mNamespaces_wm, false);
		ParcelWriteUtil.a(paramParcel, 6, paramApplicationMetadata.getSenderAppIdentifier(),
				false);
		ParcelWriteUtil.a(paramParcel, 7,
				paramApplicationMetadata.getSenderAppLaunchUrl_cR(), paramInt,
				false);
		ParcelWriteUtil.D(paramParcel, i);
	}

	public ApplicationMetadata createFromParcel(Parcel paramParcel) {
		int i = com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil
				.o(paramParcel);
		int j = 0;
		String str1 = null;
		String str2 = null;
		ArrayList images = null;
		ArrayList namespaces = null;
		String str3 = null;
		Uri localUri = null;
		while (paramParcel.dataPosition() < i) {
			int k = com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil
					.readInt_n(paramParcel);
			switch (com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil.S(k)) {
			case 1:
				j = com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil.g(
						paramParcel, k);
				break;
			case 2:
				str1 = com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil.m(
						paramParcel, k);
				break;
			case 3:
				str2 = com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil.m(
						paramParcel, k);
				break;
			case 4:
				images = com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil
						.c(paramParcel, k, WebImage.CREATOR);
				break;
			case 5:
				namespaces = com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil
						.y(paramParcel, k);
				break;
			case 6:
				str3 = com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil.m(
						paramParcel, k);
				break;
			case 7:
				localUri = (Uri) com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil
						.a(paramParcel, k, Uri.CREATOR);
				break;
			default:
				com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil.b(
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
