
package tv.matchstick.server.fling;

final class SessionPrivateData {
    String applicationId;
    String sessionId;
    boolean relaunchIfRunning;
    final MediaRouteSession mediaRouteSession;

    public SessionPrivateData(MediaRouteSession routeSession, String appId, String sId, boolean flag)
    {
        super();
        mediaRouteSession = routeSession;
        applicationId = appId;
        sessionId = sId;
        relaunchIfRunning = flag;
    }
}
