package com.mengzhihua.utils.common.concurrent;

/**
 * Google jump consistent hash (Guava {@code Hashing.consistentHash}).
 */
public final class JumpHashUtil {

    private JumpHashUtil() {
    }

    public static int jump(long key, int buckets) {
        if (buckets <= 0) {
            throw new IllegalArgumentException("buckets must be > 0");
        }
        long b = -1;
        long j = 0;
        while (j < buckets) {
            b = j;
            key = key * 2862933555777941757L + 1;
            j = (long) ((b + 1) * ((double) (1L << 31) / ((key >>> 33) + 1)));
        }
        return (int) b;
    }
}
