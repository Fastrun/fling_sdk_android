
package com.fireflycast.server.cast;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.fireflycast.server.cast.media.RouteController;

/**
 * @author jianminz
 */
public abstract class MediaRouteProvider {
    private static final int MSG_DELIVER_DESCRIPTOR_CHANGED = 1;
    private static final int MSG_DELIVER_DISCOVERY_REQUEST_CHANGED = 2;

    public final Context mContext_a;
    final ProviderMetadata mProviderMetadata_b;

    // public final C_nx c = new C_nx(this, (byte) 0);
    public final Handler mHandler_c = new Handler() {
        public final void handleMessage(Message message) {
            switch (message.what) {
                case MSG_DELIVER_DESCRIPTOR_CHANGED:
                    mPendingDescriptorChange_h = false;
                    if (mDescriptorChangedListener_d != null)
                    {
                        mDescriptorChangedListener_d
                                .onDescriptorChanged_a(mMediaRouteProviderDescriptor_g);
                        return;
                    }
                    break;
                case MSG_DELIVER_DISCOVERY_REQUEST_CHANGED: // device discovery
                                                            // request
                    mPendingDiscoveryRequestChange_f = false;
                    onDiscoveryRequestChanged_a(mDiscoveryRequest_e); // call
                                                                      // CastMediaRouteProvider_awb.a
                    break;
            }
        }
    };

    DescriptorChangedListener mDescriptorChangedListener_d;
    public DiscoveryRequest mDiscoveryRequest_e;
    boolean mPendingDiscoveryRequestChange_f;
    public MediaRouteProviderDescriptor mMediaRouteProviderDescriptor_g;
    public boolean mPendingDescriptorChange_h;

    public MediaRouteProvider(Context paramContext)
    {
        this(paramContext, (byte) 0);
    }

    private MediaRouteProvider(Context paramContext, byte paramByte)
    {
        if (paramContext == null)
            throw new IllegalArgumentException("context must not be null");
        this.mContext_a = paramContext;
        this.mProviderMetadata_b = new ProviderMetadata(new ComponentName(paramContext,
                getClass()));
    }

    public RouteController getRouteController_a(String routeId)
    {
        return null;
    }

    public void onDiscoveryRequestChanged_a(DiscoveryRequest request)
    {
    }
}
