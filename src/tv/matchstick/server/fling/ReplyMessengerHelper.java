
package tv.matchstick.server.fling;

import android.os.Messenger;

abstract class ReplyMessengerHelper {
    public static boolean isValid(Messenger messenger)
    {
        boolean flag = false;
        if (messenger != null)
        {
            android.os.IBinder ibinder;
            try
            {
                ibinder = messenger.getBinder();
            } catch (NullPointerException nullpointerexception)
            {
                return false;
            }
            flag = false;
            if (ibinder != null)
                flag = true;
        }
        return flag;
    }
}
