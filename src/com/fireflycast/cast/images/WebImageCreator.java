package com.fireflycast.cast.images;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.fireflycast.client.common.internal.safeparcel.ParcelReadUtil;

/*
 * b.smali : OK
 */
public class WebImageCreator implements Parcelable.Creator<WebImage> {

	static void buildParcel_a(WebImage paramWebImage, Parcel paramParcel,
			int paramInt) {
		int i = com.fireflycast.client.common.internal.safeparcel.ParcelWriteUtil
				.p(paramParcel);
		com.fireflycast.client.common.internal.safeparcel.ParcelWriteUtil.c(paramParcel, 1,
				paramWebImage.getVersionCode());
		com.fireflycast.client.common.internal.safeparcel.ParcelWriteUtil.a(paramParcel, 2,
				paramWebImage.getUrl(), paramInt, false);
		com.fireflycast.client.common.internal.safeparcel.ParcelWriteUtil.c(paramParcel, 3,
				paramWebImage.getWidth());
		com.fireflycast.client.common.internal.safeparcel.ParcelWriteUtil.c(paramParcel, 4,
				paramWebImage.getHeight());
		com.fireflycast.client.common.internal.safeparcel.ParcelWriteUtil.D(paramParcel, i);
	}

	@Override
	public WebImage createFromParcel(Parcel paramParcel) {
		int i = ParcelReadUtil.o(paramParcel);
		int j = 0;
		Uri localUri = null;
		int k = 0;
		int l = 0;
		while (paramParcel.dataPosition() < i) {
			int i1 = ParcelReadUtil.readInt_n(paramParcel);
			switch (ParcelReadUtil.S(i1)) {
			case 1:
				j = ParcelReadUtil.g(paramParcel, i1);
				break;
			case 2:
				localUri = (Uri) ParcelReadUtil.a(paramParcel, i1, Uri.CREATOR);
				break;
			case 3:
				k = ParcelReadUtil.g(paramParcel, i1);
				break;
			case 4:
				l = ParcelReadUtil.g(paramParcel, i1);
				break;
			default:
				ParcelReadUtil.b(paramParcel, i1);
			}
		}
		if (paramParcel.dataPosition() != i)
			throw new ParcelReadUtil.SafeParcelA("Overread allowed size end=" + i,
					paramParcel);
		WebImage localWebImage = new WebImage(j, localUri, k, l);
		return localWebImage;
	}

	@Override
	public WebImage[] newArray(int paramInt) {
		// TODO Auto-generated method stub
		return new WebImage[paramInt];
	}

}
