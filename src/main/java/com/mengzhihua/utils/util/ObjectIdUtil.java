package com.mengzhihua.utils.util;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * MongoDB-style 12-byte ObjectId (timestamp + random + counter).
 */
public final class ObjectIdUtil {

    private static final AtomicInteger COUNTER = new AtomicInteger(ThreadLocalRandom.current().nextInt());
    private static final byte[] MACHINE = new byte[5];

    static {
        ThreadLocalRandom.current().nextBytes(MACHINE);
    }

    private ObjectIdUtil() {
    }

    public static String next() {
        ByteBuffer buffer = ByteBuffer.allocate(12);
        buffer.putInt((int) (System.currentTimeMillis() / 1000));
        buffer.put(MACHINE);
        int counter = COUNTER.getAndIncrement() & 0xffffff;
        buffer.put((byte) (counter >>> 16));
        buffer.put((byte) (counter >>> 8));
        buffer.put((byte) counter);
        return HexUtil.encode(buffer.array());
    }

    public static boolean isValid(String id) {
        return id != null && id.length() == 24 && id.matches("(?i)[0-9a-f]{24}");
    }

    public static Instant timestamp(String id) {
        if (!isValid(id)) {
            throw new IllegalArgumentException("invalid objectId: " + id);
        }
        long seconds = Integer.toUnsignedLong((int) Long.parseLong(id.substring(0, 8), 16));
        return Instant.ofEpochSecond(seconds);
    }
}
