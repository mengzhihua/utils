package com.mengzhihua.utils.common.id;


import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import com.mengzhihua.utils.common.codec.Base32Util;

/**
 * Time-Sorted ID (Hypersistence TSID style): 42-bit time + 10-bit node + 12-bit sequence.
 */
public final class TsidUtil {

    private static final long EPOCH = Instant.parse("2020-01-01T00:00:00Z").toEpochMilli();
    private static final int NODE = ThreadLocalRandom.current().nextInt(1024);
    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    private TsidUtil() {
    }

    public static long next() {
        return next(System.currentTimeMillis(), NODE, SEQUENCE.getAndIncrement() & 0xFFF);
    }

    public static long next(long unixMillis, int node, int sequence) {
        long ts = unixMillis - EPOCH;
        if (ts < 0) {
            throw new IllegalArgumentException("timestamp before TSID epoch");
        }
        return (ts << 22) | ((long) (node & 0x3FF) << 12) | (sequence & 0xFFF);
    }

    public static String nextStr() {
        return encode(next());
    }

    public static String encode(long id) {
        byte[] bytes = new byte[8];
        for (int i = 7; i >= 0; i--) {
            bytes[i] = (byte) id;
            id >>>= 8;
        }
        String crockford = Base32Util.encodeCrockford(bytes);
        return crockford.length() <= 13 ? crockford : crockford.substring(crockford.length() - 13);
    }

    public static long unixMillis(long id) {
        return (id >>> 22) + EPOCH;
    }

    public static int node(long id) {
        return (int) ((id >>> 12) & 0x3FF);
    }

    public static int sequence(long id) {
        return (int) (id & 0xFFF);
    }
}
