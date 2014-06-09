
package com.fireflycast.server.utils;

import com.fireflycast.server.cast.ApplicationMetadata;

public final class ApplicationMetadataPriv {
    public final ApplicationMetadata mApplicationMetadata_a;

    public ApplicationMetadataPriv(ApplicationMetadata applicationmetadata, String applicationId)
    {
        super();

        mApplicationMetadata_a = applicationmetadata;
        applicationmetadata.mApplicationId_a = applicationId;
    }
}
