
package com.fireflycast.server.cast;

import android.content.ComponentName;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.fireflycast.server.cast.media.RouteController_nz;

/**
 * @author jianminz
 */
public abstract class MediaRouteProvider_nv {
    private static final int MSG_DELIVER_DESCRIPTOR_CHANGED = 1;
    private static final int MSG_DELIVER_DISCOVERY_REQUEST_CHANGED = 2;

    public final Context mContext_a;
    final ProviderMetadata_ny mProviderMetadata_b;

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

    DescriptorChangedListener_nw mDescriptorChangedListener_d;
    public DiscoveryRequest_nu mDiscoveryRequest_e;
    boolean mPendingDiscoveryRequestChange_f;
    public MediaRouteProviderDescriptor_oa mMediaRouteProviderDescriptor_g;
    public boolean mPendingDescriptorChange_h;

    public MediaRouteProvider_nv(Context paramContext)
    {
        this(paramContext, (byte) 0);
    }

    private MediaRouteProvider_nv(Context paramContext, byte paramByte)
    {
        if (paramContext == null)
            throw new IllegalArgumentException("context must not be null");
        this.mContext_a = paramContext;
        this.mProviderMetadata_b = new ProviderMetadata_ny(new ComponentName(paramContext,
                getClass()));
    }

    public RouteController_nz getRouteController_a(String routeId)
    {
        return null;
    }

    public void onDiscoveryRequestChanged_a(DiscoveryRequest_nu request)
    {
    }
}
