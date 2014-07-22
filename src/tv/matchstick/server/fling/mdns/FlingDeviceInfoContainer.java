
package tv.matchstick.server.fling.mdns;

public final class FlingDeviceInfoContainer
{
    FlingDeviceInfo mFlingDeviceInfo;

    public FlingDeviceInfoContainer(String paramString)
    {
        this.mFlingDeviceInfo = new FlingDeviceInfo(paramString, (byte) 0);
    }

    public final FlingDeviceInfoContainer setProto_a(int paramInt)
    {
        this.mFlingDeviceInfo.mProto = paramInt;
        return this;
    }
}
