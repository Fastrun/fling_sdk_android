
package com.fireflycast.server.cast;

import android.content.ComponentName;

/**
 * @author jianminz
 */
public final class ProviderMetadata {
    final ComponentName mComponentName_a;

    ProviderMetadata(ComponentName componentname)
    {
        if (componentname == null)
        {
            throw new IllegalArgumentException("componentName must not be null");
        } else
        {
            mComponentName_a = componentname;
            return;
        }
    }

    public final String toString()
    {
        return (new StringBuilder("ProviderMetadata{ componentName="))
                .append(mComponentName_a.flattenToShortString()).append(" }").toString();
    }
}
