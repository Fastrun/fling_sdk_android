
package com.fireflycast.server.cast;

import java.util.HashMap;
import java.util.Map;

final class MetadataMap {
    final Map a = new HashMap();
    private final Map b = new HashMap();
    private final Map c = new HashMap();

    public MetadataMap()
    {
    }

    public final MetadataMap add_a(String key, String value, int type)
    {
        b.put(key, value);
        a.put(value, key);
        c.put(key, Integer.valueOf(type));
        return this;
    }

    public final String a(String key)
    {
        return (String) b.get(key);
    }

    public final int b(String key)
    {
        Integer integer = (Integer) c.get(key);
        if (integer != null)
            return integer.intValue();
        else
            return 0;
    }
}
