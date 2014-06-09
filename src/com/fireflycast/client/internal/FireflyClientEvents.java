package com.fireflycast.client.internal;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.fireflycast.cast.ConnectionResult;
import com.fireflycast.cast.FireflyApiClient;
import com.fireflycast.cast.FireflyApiClient.ConnectionCallbacks;
import com.fireflycast.client.common.FireflyPlayServicesClient;
import com.fireflycast.client.common.FireflyPlayServicesClient.OnConnectionFailedListener;

/*
 * original name: GmsClientEvents_ei
 */
public class FireflyClientEvents {
    final ArrayList<ConnectionCallbacks> mCallbacks_BL = new ArrayList<ConnectionCallbacks>();
    private final FmsClientEventCallback_b mFireflyClientEventCallback_BJ;
    private final Handler mHandler;

    private ArrayList<ConnectionCallbacks> mCallbacks_BK = new ArrayList<ConnectionCallbacks>();
    private ArrayList<OnConnectionFailedListener> mFailedListeners_BN = new ArrayList<OnConnectionFailedListener>();

    private boolean mIsNotifingFailedListener_BO = false;
    private boolean mIsNotifingCallbacks_BM = false;

    public FireflyClientEvents(Context paramContext, Looper paramLooper, FmsClientEventCallback_b callback) {
        mFireflyClientEventCallback_BJ = callback;
        mHandler = new FmsClientEventsHandler_a(paramLooper);
    }

    protected void notifyOnConnected_bo() {
        synchronized (mCallbacks_BK) {
            notifyOnConnected_b(mFireflyClientEventCallback_BJ.getBundle_cY());
        }
    }

    public void notifyOnConnected_b(Bundle bundle) {
        synchronized (mCallbacks_BK) {
            ValueChecker.checkTrue(!mIsNotifingCallbacks_BM);
            mHandler.removeMessages(1);
            mIsNotifingCallbacks_BM = true;
            ValueChecker.checkTrue(mCallbacks_BL.size() == 0);
            ArrayList<ConnectionCallbacks> localArrayList = mCallbacks_BK;
            int i = 0;
            int j = localArrayList.size();
            while ((i < j) && (mFireflyClientEventCallback_BJ.canReceiveEvent_dC())) {
                if (!mFireflyClientEventCallback_BJ.isConnected()) {
                    break;
                }
                if (!mCallbacks_BL.contains(localArrayList.get(i))) {
                    localArrayList.get(i).onConnected(bundle);
                }
                ++i;
            }
            mCallbacks_BL.clear();
            mIsNotifingCallbacks_BM = false;
        }
    }

    public void notifyOnConnectionSuspended_P(int paramInt) {
        mHandler.removeMessages(1);
        synchronized (mCallbacks_BK) {
            mIsNotifingCallbacks_BM = true;
            ArrayList<ConnectionCallbacks> localArrayList = mCallbacks_BK;
            int i = 0;
            int j = localArrayList.size();
            while (i < j) {
                if (!mFireflyClientEventCallback_BJ.canReceiveEvent_dC()) {
                    break;
                }
                if (mCallbacks_BK.contains(localArrayList.get(i))) {
                    localArrayList.get(i).onConnectionSuspended(paramInt);
                }
                ++i;
            }
            mIsNotifingCallbacks_BM = false;
        }
    }

    public void notifyOnConnectionFailed_a(ConnectionResult result) {
        mHandler.removeMessages(1);
        synchronized (mFailedListeners_BN) {
            mIsNotifingFailedListener_BO = true;
            ArrayList<OnConnectionFailedListener> localArrayList = mFailedListeners_BN;
            int i = 0;
            int j = localArrayList.size();
            while (i < j) {
                if (!mFireflyClientEventCallback_BJ.canReceiveEvent_dC()) {
                    return;
                }
                if (mFailedListeners_BN.contains(localArrayList.get(i))) {
                    localArrayList.get(i).onConnectionFailed(result);
                }
                ++i;
            }
            mIsNotifingFailedListener_BO = false;
        }
    }

    public void registerConnectionCallbacks(ConnectionCallbacks listener) {
        ValueChecker.checkNullPointer_f(listener);
        synchronized (mCallbacks_BK) {
            if (mCallbacks_BK.contains(listener)) {
                Log.w("FireflyClientEvents",
                        "registerConnectionCallbacks(): listener " + listener
                                + " is already registered");
            } else {
                if (mIsNotifingCallbacks_BM) {
                    mCallbacks_BK = new ArrayList<ConnectionCallbacks>(
                            mCallbacks_BK);
                }
                mCallbacks_BK.add(listener);
            }
        }
        if (!mFireflyClientEventCallback_BJ.isConnected()) {
            return;
        }
        mHandler.sendMessage(mHandler.obtainMessage(1, listener));
    }

    public boolean isConnectionCallbacksRegistered(
            FireflyApiClient.ConnectionCallbacks listener) {
        ValueChecker.checkNullPointer_f(listener);
        synchronized (mCallbacks_BK) {
            return mCallbacks_BK.contains(listener);
        }
    }

    public void unregisterConnectionCallbacks(
            FireflyApiClient.ConnectionCallbacks listener) {
        ValueChecker.checkNullPointer_f(listener);
        synchronized (mCallbacks_BK) {
            if (mCallbacks_BK != null) {
                if (mIsNotifingCallbacks_BM)
                    mCallbacks_BK = new ArrayList<ConnectionCallbacks>(
                            mCallbacks_BK);
                boolean bool = mCallbacks_BK.remove(listener);
                if (!bool) {
                    Log.w("FireflyClientEvents",
                            "unregisterConnectionCallbacks(): listener "
                                    + listener + " not found");
                } else if (mIsNotifingCallbacks_BM && !(mCallbacks_BL.contains(listener))) {
                    mCallbacks_BL.add(listener);
                }
            }
        }
    }

    public void registerConnectionFailedListener(
            FireflyPlayServicesClient.OnConnectionFailedListener listener) {
        ValueChecker.checkNullPointer_f(listener);
        synchronized (mFailedListeners_BN) {
            if (mFailedListeners_BN.contains(listener)) {
                Log.w("FireflyClientEvents",
                        "registerConnectionFailedListener(): listener "
                                + listener + " is already registered");
            } else {
                if (mIsNotifingFailedListener_BO) {
                    mFailedListeners_BN = new ArrayList<OnConnectionFailedListener>(
                            mFailedListeners_BN);
                }
                mFailedListeners_BN.add(listener);
            }
        }
    }

    public boolean isConnectionFailedListenerRegistered(
            FireflyPlayServicesClient.OnConnectionFailedListener listener) {
        ValueChecker.checkNullPointer_f(listener);
        synchronized (mFailedListeners_BN) {
            return mFailedListeners_BN.contains(listener);
        }
    }

    public void unregisterConnectionFailedListener(
            FireflyPlayServicesClient.OnConnectionFailedListener listener) {
        ValueChecker.checkNullPointer_f(listener);
        synchronized (mFailedListeners_BN) {
            if (mFailedListeners_BN != null) {
                if (mIsNotifingFailedListener_BO) {
                    mFailedListeners_BN = new ArrayList<OnConnectionFailedListener>(
                            mFailedListeners_BN);
                }
                boolean bool = mFailedListeners_BN.remove(listener);
                if (!bool) {
                    Log.w("FireflyClientEvents",
                            "unregisterConnectionFailedListener(): listener "
                                    + listener + " not found");
                }
            }
        }
    }

    public interface FmsClientEventCallback_b {
        public boolean canReceiveEvent_dC();

        public boolean isConnected();

        public Bundle getBundle_cY();
    }

    static final int NOTIFY_CALLBACK = 1;

	final class FmsClientEventsHandler_a extends Handler {
		public FmsClientEventsHandler_a(Looper paramLooper) {
			super(paramLooper);
		}

		public void handleMessage(Message msg) {
			if (msg.what == NOTIFY_CALLBACK) {
				synchronized (mCallbacks_BK) {
					if (mFireflyClientEventCallback_BJ.canReceiveEvent_dC()
							&& mFireflyClientEventCallback_BJ.isConnected()
							&& mCallbacks_BK.contains(msg.obj)) {
						Bundle bundle = mFireflyClientEventCallback_BJ
								.getBundle_cY();
						FireflyApiClient.ConnectionCallbacks cb = (FireflyApiClient.ConnectionCallbacks) msg.obj;
						cb.onConnected(bundle);
					}
				}
				return;
			}
			Log.wtf("GmsClientEvents", "Don't know how to handle this message.");
		}
	}
}
