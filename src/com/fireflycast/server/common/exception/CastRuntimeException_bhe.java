
package com.fireflycast.server.common.exception;

import android.os.Parcel;

public final class CastRuntimeException_bhe extends RuntimeException {
    public CastRuntimeException_bhe(String s, Parcel parcel)
    {
        super((new StringBuilder()).append(s).append(" Parcel: pos=").append(parcel.dataPosition())
                .append(" size=").append(parcel.dataSize()).toString());
    }
}
