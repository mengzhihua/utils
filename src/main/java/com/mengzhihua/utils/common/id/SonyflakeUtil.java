package com.mengzhihua.utils.common.id;


import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Sonyflake (sony/sonyflake): 39-bit 10ms time + 8-bit sequence + 16-bit machine.
 * Epoch {@code 2014-09-01T00:00:00Z}. Layout from LSB: sequence, machine, elapsed.
 */
public final class SonyflakeUtil {

    private static final long EPOCH = Instant.parse("2014-09-01T00:00:00Z").toEpochMilli();
    private static final int MACHINE = ThreadLocalRandom.current().nextInt(1 << 16);
    private static final AtomicInteger SEQUENCE = new AtomicInteger();

    private SonyflakeUtil() {
    }

    public static long next() {
        return next(System.currentTimeMillis(), MACHINE, SEQUENCE.getAndIncrement() & 0xFF);
    }

    public static long next(long unixMillis, int machine, int sequence) {
        long elapsed = (unixMillis - EPOCH) / 10;
        if (elapsed < 0) {
            throw new IllegalArgumentException("timestamp before Sonyflake epoch");
        }
        return (elapsed << 24) | ((long) (machine & 0xFFFF) << 8) | (sequence & 0xFF);
    }

    public static long unixMillis(long id) {
        return ((id >>> 24) * 10) + EPOCH;
    }

    public static int machine(long id) {
        return (int) ((id >>> 8) & 0xFFFF);
    }

    public static int sequence(long id) {
        return (int) (id & 0xFF);
    }

    public static long epochMillis() {
        return EPOCH;
    }
}
