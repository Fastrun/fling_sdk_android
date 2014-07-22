
package tv.matchstick.server.utils;

import tv.matchstick.server.fling.ApplicationMetadata;

public final class ApplicationMetadataPriv {
    public final ApplicationMetadata mApplicationMetadata;

    public ApplicationMetadataPriv(ApplicationMetadata applicationmetadata, String applicationId)
    {
        super();

        mApplicationMetadata = applicationmetadata;
        applicationmetadata.mApplicationId = applicationId;
    }
}
