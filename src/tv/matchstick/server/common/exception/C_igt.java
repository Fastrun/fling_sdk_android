
package tv.matchstick.server.common.exception;

import java.io.IOException;

public final class C_igt extends IOException {
    private C_igt(String s)
    {
        super(s);
    }

    public static C_igt a()
    {
        return new C_igt(
                "While parsing a protocol message, the input ended unexpectedly in the middle of a field.  This could mean either than the input has been truncated or that an embedded message misreported its own length.");
    }

    public static C_igt b()
    {
        return new C_igt(
                "CodedInputStream encountered an embedded string or message which claimed to have negative size.");
    }

    public static C_igt c()
    {
        return new C_igt("CodedInputStream encountered a malformed varint.");
    }

    public static C_igt d()
    {
        return new C_igt("Protocol message contained an invalid tag (zero).");
    }

    public static C_igt e()
    {
        return new C_igt("Protocol message end-group tag did not match expected tag.");
    }

    public static C_igt f()
    {
        return new C_igt("Protocol message tag had invalid wire type.");
    }

    public static C_igt g()
    {
        return new C_igt(
                "Protocol message had too many levels of nesting.  May be malicious.  Use CodedInputStream.setRecursionLimit() to increase the depth limit.");
    }

    public static C_igt h()
    {
        return new C_igt(
                "Protocol message was too large.  May be malicious.  Use CodedInputStream.setSizeLimit() to increase the size limit.");
    }
}
