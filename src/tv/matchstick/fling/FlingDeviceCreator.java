package tv.matchstick.fling;

import tv.matchstick.server.common.exception.FlingRuntimeException;
import tv.matchstick.server.common.images.WebImage;
import tv.matchstick.server.common.internal.safeparcel.ParcelReader;
import tv.matchstick.server.common.internal.safeparcel.ParcelWritter;
import android.os.Parcel;

/**
 * Internal Fling device creator
 */
final class FlingDeviceCreator implements android.os.Parcelable.Creator {

	public FlingDeviceCreator() {
	}

	public static void buildParcel(FlingDevice flingdevice, Parcel parcel) {
		int i = ParcelWritter.a(parcel, 20293);
		ParcelWritter.b(parcel, 1, flingdevice.getVersionCode());
		ParcelWritter.a(parcel, 2, flingdevice.getDeviceId(), false);
		ParcelWritter.a(parcel, 3, flingdevice.mHostAddress, false);
		ParcelWritter.a(parcel, 4, flingdevice.getFriendlyName(), false);
		ParcelWritter.a(parcel, 5, flingdevice.getModelName(), false);
		ParcelWritter.a(parcel, 6, flingdevice.getDeviceVersion(), false);
		ParcelWritter.b(parcel, 7, flingdevice.getServicePort());
		ParcelWritter.b(parcel, 8, flingdevice.getIcons(), false);
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
				throw new FlingRuntimeException((new StringBuilder(
						"Overread allowed size end=")).append(j).toString(),
						parcel);
			else
				return new FlingDevice(k, s4, s3, s2, s1, s, i, arraylist);
		while (true);
	}

	public final Object[] newArray(int i) {
		return new FlingDevice[i];
	}
}
