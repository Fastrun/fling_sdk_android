
package com.fireflycast.server.cast.mdns;

import java.util.List;

final class CastDeviceInfo_avl
{
    String FQDN_a;
    List mIpV4AddrList_b;
    List mIpV6AddrList_c;
    String mName_d;
    String e;
    String mHost_f;
    int mProto_g; //1:tcp, 2: udp
    int mPort_h;
    int mPriority_i;
    int mWeight_j;
    List mTextStringList_k;
    long mTTL_l;

    private CastDeviceInfo_avl(String paramString)
    {
        this.FQDN_a = paramString;
        this.mProto_g = 0;
        this.mTTL_l = -1L;
    }

    CastDeviceInfo_avl(String paramString, byte b)
    {
        this(paramString);
    }
}
