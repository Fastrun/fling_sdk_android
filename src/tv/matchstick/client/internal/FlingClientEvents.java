package tv.matchstick.client.internal;

import java.util.ArrayList;

import tv.matchstick.client.common.FlingPlayServicesClient;
import tv.matchstick.client.common.FlingPlayServicesClient.OnConnectionFailedListener;
import tv.matchstick.fling.ConnectionResult;
import tv.matchstick.fling.FlingManager;
import tv.matchstick.fling.FlingManager.ConnectionCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/*
 * original name: GmsClientEvents_ei
 */
public class FlingClientEvents {
    final ArrayList<ConnectionCallbacks> mCallbacks = new ArrayList<ConnectionCallbacks>();
    private final FmsClientEventCallback mFlingClientEventCallback;
    private final Handler mHandler;

    private ArrayList<ConnectionCallbacks> mCallbacks_BK = new ArrayList<ConnectionCallbacks>();
    private ArrayList<OnConnectionFailedListener> mFailedListeners = new ArrayList<OnConnectionFailedListener>();

    private boolean mIsNotifingFailedListener = false;
    private boolean mIsNotifingCallbacks = false;

    public FlingClientEvents(Context paramContext, Looper paramLooper, FmsClientEventCallback callback) {
        mFlingClientEventCallback = callback;
        mHandler = new FmsClientEventsHandler(paramLooper);
    }

    protected void notifyOnConnected() {
        synchronized (mCallbacks_BK) {
            notifyOnConnected(mFlingClientEventCallback.getBundle());
        }
    }

    public void notifyOnConnected(Bundle bundle) {
        synchronized (mCallbacks_BK) {
            ValueChecker.checkTrue(!mIsNotifingCallbacks);
            mHandler.removeMessages(1);
            mIsNotifingCallbacks = true;
            ValueChecker.checkTrue(mCallbacks.size() == 0);
            ArrayList<ConnectionCallbacks> localArrayList = mCallbacks_BK;
            int i = 0;
            int j = localArrayList.size();
            while ((i < j) && (mFlingClientEventCallback.canReceiveEvent())) {
                if (!mFlingClientEventCallback.isConnected()) {
                    break;
                }
                if (!mCallbacks.contains(localArrayList.get(i))) {
                    localArrayList.get(i).onConnected(bundle);
                }
                ++i;
            }
            mCallbacks.clear();
            mIsNotifingCallbacks = false;
        }
    }

    public void notifyOnConnectionSuspended(int paramInt) {
        mHandler.removeMessages(1);
        synchronized (mCallbacks_BK) {
            mIsNotifingCallbacks = true;
            ArrayList<ConnectionCallbacks> localArrayList = mCallbacks_BK;
            int i = 0;
            int j = localArrayList.size();
            while (i < j) {
                if (!mFlingClientEventCallback.canReceiveEvent()) {
                    break;
                }
                if (mCallbacks_BK.contains(localArrayList.get(i))) {
                    localArrayList.get(i).onConnectionSuspended(paramInt);
                }
                ++i;
            }
            mIsNotifingCallbacks = false;
        }
    }

    public void notifyOnConnectionFailed(ConnectionResult result) {
        mHandler.removeMessages(1);
        synchronized (mFailedListeners) {
            mIsNotifingFailedListener = true;
            ArrayList<OnConnectionFailedListener> localArrayList = mFailedListeners;
            int i = 0;
            int j = localArrayList.size();
            while (i < j) {
                if (!mFlingClientEventCallback.canReceiveEvent()) {
                    return;
                }
                if (mFailedListeners.contains(localArrayList.get(i))) {
                    localArrayList.get(i).onConnectionFailed(result);
                }
                ++i;
            }
            mIsNotifingFailedListener = false;
        }
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        ValueChecker.checkNullPointer(listener);
        synchronized (mCallbacks_BK) {
            if (mCallbacks_BK.contains(listener)) {
                Log.w("FlingClientEvents",
                        "registerConnectionCallbacks(): listener " + listener
                                + " is already registered");
            } else {
                if (mIsNotifingCallbacks) {
                    mCallbacks_BK = new ArrayList<ConnectionCallbacks>(
                            mCallbacks_BK);
                }
                mCallbacks_BK.add(listener);
            }
        }
        if (!mFlingClientEventCallback.isConnected()) {
            return;
        }
        mHandler.sendMessage(mHandler.obtainMessage(1, listener));
    }

    public boolean isConnectionCallbacksRegistered(
            FlingManager.ConnectionCallbacks listener) {
        ValueChecker.checkNullPointer(listener);
        synchronized (mCallbacks_BK) {
            return mCallbacks_BK.contains(listener);
        }
    }

    public void unregisterConnectionCallbacks(
            FlingManager.ConnectionCallbacks listener) {
        ValueChecker.checkNullPointer(listener);
        synchronized (mCallbacks_BK) {
            if (mCallbacks_BK != null) {
                if (mIsNotifingCallbacks)
                    mCallbacks_BK = new ArrayList<ConnectionCallbacks>(
                            mCallbacks_BK);
                boolean bool = mCallbacks_BK.remove(listener);
                if (!bool) {
                    Log.w("FlingClientEvents",
                            "unregisterConnectionCallbacks(): listener "
                                    + listener + " not found");
                } else if (mIsNotifingCallbacks && !(mCallbacks.contains(listener))) {
                    mCallbacks.add(listener);
                }
            }
        }
    }

    public void registerConnectionFailedListener(
            FlingPlayServicesClient.OnConnectionFailedListener listener) {
        ValueChecker.checkNullPointer(listener);
        synchronized (mFailedListeners) {
            if (mFailedListeners.contains(listener)) {
                Log.w("FlingClientEvents",
                        "registerConnectionFailedListener(): listener "
                                + listener + " is already registered");
            } else {
                if (mIsNotifingFailedListener) {
                    mFailedListeners = new ArrayList<OnConnectionFailedListener>(
                            mFailedListeners);
                }
                mFailedListeners.add(listener);
            }
        }
    }

    public boolean isConnectionFailedListenerRegistered(
            FlingPlayServicesClient.OnConnectionFailedListener listener) {
        ValueChecker.checkNullPointer(listener);
        synchronized (mFailedListeners) {
            return mFailedListeners.contains(listener);
        }
    }

    public void unregisterConnectionFailedListener(
            FlingPlayServicesClient.OnConnectionFailedListener listener) {
        ValueChecker.checkNullPointer(listener);
        synchronized (mFailedListeners) {
            if (mFailedListeners != null) {
                if (mIsNotifingFailedListener) {
                    mFailedListeners = new ArrayList<OnConnectionFailedListener>(
                            mFailedListeners);
                }
                boolean bool = mFailedListeners.remove(listener);
                if (!bool) {
                    Log.w("FlingClientEvents",
                            "unregisterConnectionFailedListener(): listener "
                                    + listener + " not found");
                }
            }
        }
    }

    public interface FmsClientEventCallback {
        public boolean canReceiveEvent();

        public boolean isConnected();

        public Bundle getBundle();
    }

    static final int NOTIFY_CALLBACK = 1;

	final class FmsClientEventsHandler extends Handler {
		public FmsClientEventsHandler(Looper paramLooper) {
			super(paramLooper);
		}

		public void handleMessage(Message msg) {
			if (msg.what == NOTIFY_CALLBACK) {
				synchronized (mCallbacks_BK) {
					if (mFlingClientEventCallback.canReceiveEvent()
							&& mFlingClientEventCallback.isConnected()
							&& mCallbacks_BK.contains(msg.obj)) {
						Bundle bundle = mFlingClientEventCallback
								.getBundle();
						FlingManager.ConnectionCallbacks cb = (FlingManager.ConnectionCallbacks) msg.obj;
						cb.onConnected(bundle);
					}
				}
				return;
			}
			Log.wtf("FlingClientEvents", "Don't know how to handle this message.");
		}
	}
}
