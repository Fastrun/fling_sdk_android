
package com.fireflycast.server.cast;

final class SessionPrivateData_axj {
    String applicationId;
    String sessionId_b;
    boolean relaunchIfRunning_c;
    final MediaRouteSession_axe mediaRouteSession_d;

    public SessionPrivateData_axj(MediaRouteSession_axe routeSession, String appId, String sId, boolean flag)
    {
        super();
        mediaRouteSession_d = routeSession;
        applicationId = appId;
        sessionId_b = sId;
        relaunchIfRunning_c = flag;
    }
}
