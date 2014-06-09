
package com.fireflycast.server.cast.mdns;

public final class CastDeviceInfoContainer
{
    CastDeviceInfo mCastDeviceInfo_a;

    public CastDeviceInfoContainer(String paramString)
    {
        this.mCastDeviceInfo_a = new CastDeviceInfo(paramString, (byte) 0);
    }

    public final CastDeviceInfoContainer setProto_a(int paramInt)
    {
        this.mCastDeviceInfo_a.mProto_g = paramInt;
        return this;
    }
}
