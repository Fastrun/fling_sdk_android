
package com.fireflycast.server.cast.mdns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

final class CharsetAndInetAddrInfo_avd {
    public static final Charset mCharset_a;
    public static final InetAddress mInetAddress_b;

    static {
        Charset charSet = null;
        try {
            charSet = Charset.forName("UTF-8");
        } catch (IllegalCharsetNameException e) {
            e.printStackTrace();
        } catch (UnsupportedCharsetException ex) {
            ex.printStackTrace();
        }

        mCharset_a = charSet;

        InetAddress address = null;
        try {
            address = InetAddress.getByName("224.0.0.251");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mInetAddress_b = address;
    }
}
