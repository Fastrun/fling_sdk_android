package com.fireflycast.cast;

import android.os.Bundle;
import android.os.Parcel;

import com.fireflycast.server.cast.mdns.CastDeviceHelper_atr;
import com.fireflycast.server.common.checker.ObjEqualChecker_avo;
import com.fireflycast.server.common.internal.safeparcel.SafeParcelable;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CastDevice implements SafeParcelable {
	public static final android.os.Parcelable.Creator CREATOR = new CastDeviceCreator_ats();
	public String mHostAddress_a;
	private final int mVersionCode_b;
	private String mDeviceId_c;
	private Inet4Address mHost_d;
	private String mFriendlyName_e;
	private String mModleName_f; // md=Chromecast
	private String mDeviceVersion_g; // ve=02
	private int mServicePort_h;
	private List mIconList_i;

	private CastDevice() {
		this(1, null, null, null, null, null, -1, ((List) (new ArrayList())));
	}

	public CastDevice(int versionCode_j, String deviceId_s,
			String hostAddress_s1, String friendlyName_s2, String modelName_s3,
			String deviceVersion_s4, int servicePort_k, List icons_list) {
		mVersionCode_b = versionCode_j;
		mDeviceId_c = deviceId_s;
		mHostAddress_a = hostAddress_s1;
		if (mHostAddress_a != null)
			try {
				InetAddress inetaddress = InetAddress.getByName(mHostAddress_a);
				if (inetaddress instanceof Inet4Address)
					mHost_d = (Inet4Address) inetaddress;
			} catch (UnknownHostException unknownhostexception) {
				mHost_d = null;
			}
		mFriendlyName_e = friendlyName_s2;
		mModleName_f = modelName_s3;
		mDeviceVersion_g = deviceVersion_s4;
		mServicePort_h = servicePort_k;
		mIconList_i = icons_list;
	}

	public static int setServicePort_a(CastDevice castdevice, int j) {
		castdevice.mServicePort_h = j;
		return j;
	}

	public static CastDeviceHelper_atr a(String deviceId,
			Inet4Address inet4address) {
		CastDevice castdevice = new CastDevice();
		castdevice.getClass();
		return new CastDeviceHelper_atr(castdevice, deviceId, inet4address);
	}

	public static String setDeviceId_a(CastDevice castdevice, String deviceID) {
		castdevice.mDeviceId_c = deviceID;
		return deviceID;
	}

	public static Inet4Address getHost_a(CastDevice castdevice) {
		return castdevice.mHost_d;
	}

	public static Inet4Address setHost_a(CastDevice castdevice,
			Inet4Address inet4address) {
		castdevice.mHost_d = inet4address;
		return inet4address;
	}

	public static List setIconList_a(CastDevice castdevice, List list) {
		castdevice.mIconList_i = list;
		return list;
	}

	public static CastDevice getFromBundle_b(Bundle bundle) {
		if (bundle == null) {
			return null;
		} else {
			bundle.setClassLoader(CastDevice.class.getClassLoader());
			return (CastDevice) bundle
					.getParcelable("com.fireflycast.cast.EXTRA_CAST_DEVICE");
		}
	}

	public static String setFriendlyName_b(CastDevice castdevice, String s) {
		castdevice.mFriendlyName_e = s;
		return s;
	}

	public static String setModelName_c(CastDevice castdevice, String s) {
		castdevice.mModleName_f = s;
		return s;
	}

	public static String setDeviceVersion_d(CastDevice castdevice, String s) {
		castdevice.mDeviceVersion_g = s;
		return s;
	}

	public final int getVersionCode_a() {
		return mVersionCode_b;
	}

	public final void putInBundle_a(Bundle bundle) {
		if (bundle == null) {
			return;
		} else {
			bundle.putParcelable("com.fireflycast.cast.EXTRA_CAST_DEVICE", this);
			return;
		}
	}

	public final String getDeviceId_b() {
		return mDeviceId_c;
	}

	public final Inet4Address getHostIp_c() {
		return mHost_d;
	}

	public final String getFriendlyName_d() {
		return mFriendlyName_e;
	}

	public int describeContents() {
		return 0;
	}

	public final String getModleName_e() {
		return mModleName_f;
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof CastDevice)) {
			return false;
		}

		CastDevice castdevice = (CastDevice) obj;

		if (mDeviceId_c == null) {
			if (castdevice.mDeviceId_c == null) {
				return true;
			} else {
				return false;
			}
		}
		if (ObjEqualChecker_avo.isEquals_a(mDeviceId_c, castdevice.mDeviceId_c)
				&& ObjEqualChecker_avo.isEquals_a(mHost_d, castdevice.mHost_d)
				&& ObjEqualChecker_avo.isEquals_a(mModleName_f,
						castdevice.mModleName_f)
				&& ObjEqualChecker_avo.isEquals_a(mFriendlyName_e,
						castdevice.mFriendlyName_e)
				&& ObjEqualChecker_avo.isEquals_a(mDeviceVersion_g,
						castdevice.mDeviceVersion_g)
				&& mServicePort_h == castdevice.mServicePort_h
				&& ObjEqualChecker_avo.isEquals_a(mIconList_i,
						castdevice.mIconList_i)) {
			return true;
		}

		return false;
	}

	public final String getDeviceVersion_f() {
		return mDeviceVersion_g;
	}

	public final int getServicePort_g() {
		return mServicePort_h;
	}

	public final List getIconList_h() {
		return Collections.unmodifiableList(mIconList_i);
	}

	public int hashCode() {
		if (mDeviceId_c == null)
			return 0;
		else
			return mDeviceId_c.hashCode();
	}

	public String toString() {
		Object aobj[] = new Object[2];
		aobj[0] = mFriendlyName_e;
		aobj[1] = mDeviceId_c;
		return String.format("\"%s\" (%s)", aobj);
	}

	public void writeToParcel(Parcel parcel, int j) {
		CastDeviceCreator_ats.buildParcel_a(this, parcel);
	}
}
