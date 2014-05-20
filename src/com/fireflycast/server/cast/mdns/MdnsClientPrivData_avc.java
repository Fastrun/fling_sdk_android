
package com.fireflycast.server.cast.mdns;

import com.fireflycast.server.common.checker.EmptyChecker_bha;

public final class MdnsClientPrivData_avc
{
    final byte[] a;
    final long mCurrentTime_b;
    final int c;

    public MdnsClientPrivData_avc(byte[] paramArrayOfByte, int paramInt)
    {
        boolean bool = false;
        if (paramInt <= 0 || paramInt >= 604800) {
            bool = false;
        } else {
            bool = true;
        }

        EmptyChecker_bha.b(bool);
        this.a = paramArrayOfByte;
        this.mCurrentTime_b = System.currentTimeMillis();
        this.c = paramInt;
    }
}
