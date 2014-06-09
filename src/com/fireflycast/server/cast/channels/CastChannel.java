
package com.fireflycast.server.cast.channels;

import com.fireflycast.server.utils.IMsgSender;
import com.fireflycast.server.utils.Logs;

public abstract class CastChannel {
    private IMsgSender mMsgSender_a;
    protected final Logs mLogs_j;
    public final String nameSpace_k;

    public CastChannel(String s, String tag)
    {
        nameSpace_k = s;
        mLogs_j = new Logs(tag);
    }

    public final void a(IMsgSender sender)
    {
        mMsgSender_a = sender;
        if (mMsgSender_a == null)
            d();
    }

    protected final void sendMessage_a(String s, long l, String s1)
    {
        mLogs_j.v("Sending text message: %s to: %s", new Object[] {
                s, s1
        });
        mMsgSender_a.sendMessage_a(nameSpace_k, s, l, s1);
    }

    public void onReceivedMessage_a(byte abyte0[])
    {
    }

    protected final void sendBinaryMessage_a(byte abyte0[], String transId)
    {
        mLogs_j.v("Sending binary message to: %s", new Object[] {
                transId
        });
        mMsgSender_a.sendBinaryMessage_a(nameSpace_k, abyte0, 0L, transId);
    }

    public void checkReceivedMessage_a_(String s)
    {
    }

    protected final long getId_c()
    {
        return mMsgSender_a.getId_a();
    }

    public void d()
    {
    }
}
