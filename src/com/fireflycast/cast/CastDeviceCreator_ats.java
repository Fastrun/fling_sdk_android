package com.fireflycast.cast;

import android.os.Parcel;

import com.fireflycast.server.common.exception.CastRuntimeException;
import com.fireflycast.server.common.images.WebImage;
import com.fireflycast.server.common.internal.safeparcel.ParcelReader;
import com.fireflycast.server.common.internal.safeparcel.ParcelWritter;

public final class CastDeviceCreator_ats implements
		android.os.Parcelable.Creator {

	public CastDeviceCreator_ats() {
	}

	public static void buildParcel_a(CastDevice castdevice, Parcel parcel) {
		int i = ParcelWritter.a(parcel, 20293);
		ParcelWritter.b(parcel, 1, castdevice.getVersionCode_a());
		ParcelWritter.a(parcel, 2, castdevice.getDeviceId(), false);
		ParcelWritter.a(parcel, 3, castdevice.mHostAddress_a, false);
		ParcelWritter.a(parcel, 4, castdevice.getFriendlyName(), false);
		ParcelWritter.a(parcel, 5, castdevice.getModelName(), false);
		ParcelWritter.a(parcel, 6, castdevice.getDeviceVersion(), false);
		ParcelWritter.b(parcel, 7, castdevice.getServicePort());
		ParcelWritter.b(parcel, 8, castdevice.getIcons(), false);
		ParcelWritter.b(parcel, i);
	}

	public final Object createFromParcel(Parcel parcel) {
		int i = 0;
		java.util.ArrayList arraylist = null;
		int j = ParcelReader.a(parcel);
		String s = null;
		String s1 = null;
		String s2 = null;
		String s3 = null;
		String s4 = null;
		int k = 0;
		do
			if (parcel.dataPosition() < j) {
				int l = parcel.readInt();
				switch (0xffff & l) {
				default:
					ParcelReader.b(parcel, l);
					break;

				case 1: // '\001'
					k = ParcelReader.f(parcel, l);
					break;

				case 2: // '\002'
					s4 = ParcelReader.l(parcel, l);
					break;

				case 3: // '\003'
					s3 = ParcelReader.l(parcel, l);
					break;

				case 4: // '\004'
					s2 = ParcelReader.l(parcel, l);
					break;

				case 5: // '\005'
					s1 = ParcelReader.l(parcel, l);
					break;

				case 6: // '\006'
					s = ParcelReader.l(parcel, l);
					break;

				case 7: // '\007'
					i = ParcelReader.f(parcel, l);
					break;

				case 8: // '\b'
					arraylist = ParcelReader.c(parcel, l, WebImage.CREATOR);
					break;
				}
			} else if (parcel.dataPosition() != j)
				throw new CastRuntimeException((new StringBuilder(
						"Overread allowed size end=")).append(j).toString(),
						parcel);
			else
				return new CastDevice(k, s4, s3, s2, s1, s, i, arraylist);
		while (true);
	}

	public final Object[] newArray(int i) {
		return new CastDevice[i];
	}
}
