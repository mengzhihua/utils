package com.mengzhihua.utils.common.id;


import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * CUID2-style collision-resistant IDs (paralleldrive/cuid2).
 */
public final class Cuid2Util {

    private static final AtomicLong COUNTER = new AtomicLong(ThreadLocalRandom.current().nextLong());

    private Cuid2Util() {
    }

    public static String next() {
        return next(24);
    }

    public static String next(int length) {
        if (length < 2 || length > 32) {
            throw new IllegalArgumentException("length must be 2-32");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            ByteBuffer buffer = ByteBuffer.allocate(24);
            buffer.putLong(System.currentTimeMillis());
            buffer.putLong(COUNTER.incrementAndGet());
            buffer.putLong(ThreadLocalRandom.current().nextLong());
            digest.update(buffer.array());
            digest.update(Long.toHexString(ProcessHandle.current().pid()).getBytes(StandardCharsets.UTF_8));
            byte[] hash = digest.digest();
            String encoded = new BigInteger(1, hash).toString(36);
            int first = (hash[0] & 0xff) % 26;
            String body = encoded.length() >= length - 1
                    ? encoded.substring(0, length - 1)
                    : (encoded + "00000000000000000000000000000000").substring(0, length - 1);
            return (char) ('a' + first) + body;
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 not available", ex);
        }
    }

    public static boolean isValid(String id) {
        return id != null && id.length() >= 2 && id.length() <= 32 && id.matches("[a-z][0-9a-z]+");
    }
}
