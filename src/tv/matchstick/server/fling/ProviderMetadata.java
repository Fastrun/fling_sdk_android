
package tv.matchstick.server.fling;

import android.content.ComponentName;

/**
 * @author jianminz
 */
public final class ProviderMetadata {
    final ComponentName mComponentName;

    ProviderMetadata(ComponentName componentname)
    {
        if (componentname == null)
        {
            throw new IllegalArgumentException("componentName must not be null");
        } else
        {
            mComponentName = componentname;
            return;
        }
    }

    public final String toString()
    {
        return (new StringBuilder("ProviderMetadata{ componentName="))
                .append(mComponentName.flattenToShortString()).append(" }").toString();
    }
}
