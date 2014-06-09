
package com.fireflycast.server.utils;

import org.json.JSONObject;

public interface IStatusRequest {
    public abstract void requestStatus_a(long requestId, int result, JSONObject jsonobject);
}
