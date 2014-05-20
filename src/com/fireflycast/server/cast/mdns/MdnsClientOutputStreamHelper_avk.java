
package com.fireflycast.server.cast.mdns;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

final class MdnsClientOutputStreamHelper_avk
{
    private ByteArrayOutputStream a = new ByteArrayOutputStream(16384);
    private DataOutputStream b = new DataOutputStream(this.a);

    public final DatagramPacket createDatagramPacket_a()
    {
        try
        {
            this.b.flush();
            byte[] arrayOfByte = this.a.toByteArray();
            return new DatagramPacket(arrayOfByte, arrayOfByte.length,
                    CharsetAndInetAddrInfo_avd.mInetAddress_b, 5353);
        } catch (IOException e)
        {
            // break label7;
            e.printStackTrace();
        }

        return null;
    }

    public final void a(int paramInt)
    {
        try
        {
            this.b.writeByte(paramInt & 0xFF);
            return;
        } catch (IOException localIOException)
        {
        }
    }

    public final void a(byte[] paramArrayOfByte)
    {
        try
        {
            this.b.write(paramArrayOfByte, 0, paramArrayOfByte.length);
            return;
        } catch (IOException localIOException)
        {
        }
    }

    public final void b(int paramInt)
    {
        try
        {
            this.b.writeShort(0xFFFFF & paramInt);
            return;
        } catch (IOException localIOException)
        {
        }
    }
}
