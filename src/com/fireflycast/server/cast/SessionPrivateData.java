
package com.fireflycast.server.cast;

final class SessionPrivateData {
    String applicationId;
    String sessionId_b;
    boolean relaunchIfRunning_c;
    final MediaRouteSession mediaRouteSession_d;

    public SessionPrivateData(MediaRouteSession routeSession, String appId, String sId, boolean flag)
    {
        super();
        mediaRouteSession_d = routeSession;
        applicationId = appId;
        sessionId_b = sId;
        relaunchIfRunning_c = flag;
    }
}
