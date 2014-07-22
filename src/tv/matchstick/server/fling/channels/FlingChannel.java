
package tv.matchstick.server.fling.channels;

import tv.matchstick.server.utils.IMsgSender;
import tv.matchstick.server.utils.LOG;

public abstract class FlingChannel {
    private IMsgSender mMsgSender;
    protected final LOG mLogs;
    public final String nameSpace;

    public FlingChannel(String s, String tag)
    {
        nameSpace = s;
        mLogs = new LOG(tag);
    }

    public final void a(IMsgSender sender)
    {
        mMsgSender = sender;
        if (mMsgSender == null)
            d();
    }

    protected final void sendMessage(String s, long l, String s1)
    {
        mLogs.v("Sending text message: %s to: %s", new Object[] {
                s, s1
        });
        mMsgSender.sendMessage(nameSpace, s, l, s1);
    }

    public void onReceivedMessage(byte abyte0[])
    {
    }

    protected final void sendBinaryMessage(byte message[], String transId)
    {
        mLogs.v("Sending binary message to: %s", new Object[] {
                transId
        });
        mMsgSender.sendBinaryMessage(nameSpace, message, 0L, transId);
    }

    public void checkReceivedMessage(String s)
    {
    }

    protected final long getId()
    {
        return mMsgSender.getId();
    }

    public void d()
    {
    }
}
