package com.mengzhihua.utils.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * In-process token-bucket rate limiter. Not a replacement for Redis / gateway limits.
 */
public final class RateLimiterUtil {

    private static final ConcurrentHashMap<String, Bucket> BUCKETS = new ConcurrentHashMap<>();

    private RateLimiterUtil() {
    }

    public static boolean tryAcquire(String key) {
        return tryAcquire(key, 1, 1);
    }

    public static boolean tryAcquire(String key, double permitsPerSecond) {
        return tryAcquire(key, Math.max(1, (int) Math.ceil(permitsPerSecond)), permitsPerSecond);
    }

    public static boolean tryAcquire(String key, int capacity, double refillPerSecond) {
        AssertUtil.notBlank(key, "key must not be blank");
        AssertUtil.isTrue(capacity > 0, "capacity must be greater than 0");
        AssertUtil.isTrue(refillPerSecond > 0, "refillPerSecond must be greater than 0");
        Bucket bucket = BUCKETS.computeIfAbsent(key, ignored -> new Bucket(capacity, refillPerSecond));
        return bucket.tryAcquire();
    }

    public static void clear() {
        BUCKETS.clear();
    }

    private static final class Bucket {
        private final int capacity;
        private final double refillPerSecond;
        private double tokens;
        private long lastNanos;

        private Bucket(int capacity, double refillPerSecond) {
            this.capacity = capacity;
            this.refillPerSecond = refillPerSecond;
            this.tokens = capacity;
            this.lastNanos = System.nanoTime();
        }

        private synchronized boolean tryAcquire() {
            long now = System.nanoTime();
            double refill = (now - lastNanos) / 1_000_000_000D * refillPerSecond;
            tokens = Math.min(capacity, tokens + refill);
            lastNanos = now;
            if (tokens < 1D) {
                return false;
            }
            tokens -= 1D;
            return true;
        }
    }
}
