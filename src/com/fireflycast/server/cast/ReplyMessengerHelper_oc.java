
package com.fireflycast.server.cast;

import android.os.Messenger;

abstract class ReplyMessengerHelper_oc {
    public static boolean isValid_a(Messenger messenger)
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
