
package tv.matchstick.server.common.exception;

import android.os.Parcel;

public final class FlingRuntimeException extends RuntimeException {
    public FlingRuntimeException(String s, Parcel parcel)
    {
        super((new StringBuilder()).append(s).append(" Parcel: pos=").append(parcel.dataPosition())
                .append(" size=").append(parcel.dataSize()).toString());
    }
}
