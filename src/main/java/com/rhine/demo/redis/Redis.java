package com.rhine.demo.redis;

import java.util.HashMap;
import java.util.Map;

/**
 * Ä£ÄâRedis
 */
public class Redis {

    private Map<String, String> map;

    public Redis() {
        this.map = new HashMap<>();
    }

    public String get(String key) {
        return map.get(key);
    }

    public void set(String key, String value) {
        map.put(key, value);
    }
}
