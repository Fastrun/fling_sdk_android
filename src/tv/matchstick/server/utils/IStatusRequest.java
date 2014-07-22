
package tv.matchstick.server.utils;

import org.json.JSONObject;

public interface IStatusRequest {
    public abstract void requestStatus(long requestId, int result, JSONObject jsonobject);
}
