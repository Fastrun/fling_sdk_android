
package com.fireflycast.server.utils;

import com.fireflycast.server.cast.ApplicationMetadata;

public final class ApplicationMetadataPriv_ato {
    public final ApplicationMetadata mApplicationMetadata_a;

    public ApplicationMetadataPriv_ato(ApplicationMetadata applicationmetadata, String applicationId)
    {
        super();

        mApplicationMetadata_a = applicationmetadata;
        applicationmetadata.mApplicationId_a = applicationId;
    }
}
