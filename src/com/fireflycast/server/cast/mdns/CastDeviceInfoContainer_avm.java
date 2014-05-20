
package com.fireflycast.server.cast.mdns;

public final class CastDeviceInfoContainer_avm
{
    CastDeviceInfo_avl mCastDeviceInfo_a;

    public CastDeviceInfoContainer_avm(String paramString)
    {
        this.mCastDeviceInfo_a = new CastDeviceInfo_avl(paramString, (byte) 0);
    }

    public final CastDeviceInfoContainer_avm setProto_a(int paramInt)
    {
        this.mCastDeviceInfo_a.mProto_g = paramInt;
        return this;
    }
}
