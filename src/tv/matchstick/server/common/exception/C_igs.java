
package tv.matchstick.server.common.exception;

import java.io.IOException;

public final class C_igs extends IOException {
    public C_igs()
    {
        super("CodedOutputStream was writing to a flat byte array and ran out of space.");
    }
}
