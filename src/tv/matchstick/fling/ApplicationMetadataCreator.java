package tv.matchstick.fling;

import java.util.ArrayList;

import tv.matchstick.client.common.internal.safeparcel.ParcelWriteUtil;
import tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil.SafeParcelA;
import tv.matchstick.fling.images.WebImage;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Internal Application Meta data Creator
 */
class ApplicationMetadataCreator implements
		Parcelable.Creator<ApplicationMetadata> {
	static void buildParcel(ApplicationMetadata paramApplicationMetadata,
			Parcel paramParcel, int paramInt) {
		int i = ParcelWriteUtil.p(paramParcel);
		ParcelWriteUtil.c(paramParcel, 1,
				paramApplicationMetadata.getVersionCode());
		ParcelWriteUtil.a(paramParcel, 2,
				paramApplicationMetadata.getApplicationId(), false);
		ParcelWriteUtil.a(paramParcel, 3, paramApplicationMetadata.getName(),
				false);
		ParcelWriteUtil.b(paramParcel, 4, paramApplicationMetadata.getImages(),
				false);
		ParcelWriteUtil.a(paramParcel, 5, paramApplicationMetadata.mNamespaces,
				false);
		ParcelWriteUtil.a(paramParcel, 6,
				paramApplicationMetadata.getSenderAppIdentifier(), false);
		ParcelWriteUtil.a(paramParcel, 7,
				paramApplicationMetadata.getSenderAppLaunchUrl(), paramInt,
				false);
		ParcelWriteUtil.D(paramParcel, i);
	}

	public ApplicationMetadata createFromParcel(Parcel paramParcel) {
		int i = tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
				.o(paramParcel);
		int j = 0;
		String str1 = null;
		String str2 = null;
		ArrayList images = null;
		ArrayList namespaces = null;
		String str3 = null;
		Uri localUri = null;
		while (paramParcel.dataPosition() < i) {
			int k = tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
					.readInt_n(paramParcel);
			switch (tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
					.S(k)) {
			case 1:
				j = tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
						.g(paramParcel, k);
				break;
			case 2:
				str1 = tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
						.m(paramParcel, k);
				break;
			case 3:
				str2 = tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
						.m(paramParcel, k);
				break;
			case 4:
				images = tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
						.c(paramParcel, k, WebImage.CREATOR);
				break;
			case 5:
				namespaces = tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
						.y(paramParcel, k);
				break;
			case 6:
				str3 = tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
						.m(paramParcel, k);
				break;
			case 7:
				localUri = (Uri) tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
						.a(paramParcel, k, Uri.CREATOR);
				break;
			default:
				tv.matchstick.client.common.internal.safeparcel.ParcelReadUtil
						.b(paramParcel, k);
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
