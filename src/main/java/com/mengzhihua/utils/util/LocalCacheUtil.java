package com.mengzhihua.utils.util;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Process-local TTL cache. Not a replacement for Redis.
 */
public final class LocalCacheUtil {

    private static final Map<String, Entry> CACHE = new ConcurrentHashMap<>();

    private LocalCacheUtil() {
    }

    public static void put(String key, Object value, Duration ttl) {
        AssertUtil.notBlank(key, "key must not be blank");
        long expireAt = ttl == null ? Long.MAX_VALUE : System.currentTimeMillis() + Math.max(1, ttl.toMillis());
        CACHE.put(key, new Entry(value, expireAt));
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        if (StringUtil.isBlank(key)) {
            return null;
        }
        Entry entry = CACHE.get(key);
        if (entry == null) {
            return null;
        }
        if (entry.expireAt < System.currentTimeMillis()) {
            CACHE.remove(key);
            return null;
        }
        return (T) entry.value;
    }

    public static void evict(String key) {
        CACHE.remove(key);
    }

    public static void clear() {
        CACHE.clear();
    }

    public static int size() {
        CACHE.entrySet().removeIf(item -> item.getValue().expireAt < System.currentTimeMillis());
        return CACHE.size();
    }

    private record Entry(Object value, long expireAt) {
    }
}
