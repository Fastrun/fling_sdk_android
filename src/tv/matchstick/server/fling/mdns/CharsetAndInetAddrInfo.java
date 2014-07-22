
package tv.matchstick.server.fling.mdns;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;

final class CharsetAndInetAddrInfo {
    public static final Charset mCharset;
    public static final InetAddress mInetAddress;

    static {
        Charset charSet = null;
        try {
            charSet = Charset.forName("UTF-8");
        } catch (IllegalCharsetNameException e) {
            e.printStackTrace();
        } catch (UnsupportedCharsetException ex) {
            ex.printStackTrace();
        }

        mCharset = charSet;

        InetAddress address = null;
        try {
            address = InetAddress.getByName("224.0.0.251");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        mInetAddress = address;
    }
}
