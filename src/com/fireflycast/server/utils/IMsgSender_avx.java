
package com.fireflycast.server.utils;

public interface IMsgSender_avx {
    public abstract long getId_a();

    public abstract void sendMessage_a(String s, String s1, long l, String s2);

    public abstract void sendBinaryMessage_a(String s, byte abyte0[], long l, String s1);
}
