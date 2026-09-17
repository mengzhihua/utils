package com.mengzhihua.utils.common.concurrent;


import java.time.Duration;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import com.mengzhihua.utils.common.lang.AssertUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Process-local TTL cache with a size cap. Not a replacement for Redis.
 */
public final class LocalCacheUtil {

    private static final int MAX_SIZE = 10_000;
    private static final Map<String, Entry> CACHE = new ConcurrentHashMap<>();

    private LocalCacheUtil() {
    }

    public static void put(String key, Object value, Duration ttl) {
        AssertUtil.notBlank(key, "key must not be blank");
        long expireAt = ttl == null ? Long.MAX_VALUE : System.currentTimeMillis() + Math.max(1, ttl.toMillis());
        CACHE.put(key, new Entry(value, expireAt));
        evictIfNeeded();
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
            CACHE.remove(key, entry);
            return null;
        }
        return (T) entry.value;
    }

    public static <T> T getOrLoad(String key, Duration ttl, Supplier<T> loader) {
        T cached = get(key);
        if (cached != null) {
            return cached;
        }
        AssertUtil.notNull(loader, "loader must not be null");
        return KeyedLockUtil.supply("local-cache:" + key, () -> {
            T again = get(key);
            if (again != null) {
                return again;
            }
            T value = loader.get();
            if (value != null) {
                put(key, value, ttl);
            }
            return value;
        });
    }

    public static void evict(String key) {
        CACHE.remove(key);
    }

    public static void clear() {
        CACHE.clear();
    }

    public static int size() {
        purgeExpired();
        return CACHE.size();
    }

    private static void purgeExpired() {
        long now = System.currentTimeMillis();
        CACHE.entrySet().removeIf(item -> item.getValue().expireAt < now);
    }

    private static void evictIfNeeded() {
        if (CACHE.size() <= MAX_SIZE) {
            return;
        }
        purgeExpired();
        int overflow = CACHE.size() - MAX_SIZE;
        if (overflow <= 0) {
            return;
        }
        CACHE.entrySet().stream()
                .sorted(Comparator.comparingLong(item -> item.getValue().expireAt))
                .limit(overflow)
                .map(Map.Entry::getKey)
                .forEach(CACHE::remove);
    }

    private record Entry(Object value, long expireAt) {
    }
}
