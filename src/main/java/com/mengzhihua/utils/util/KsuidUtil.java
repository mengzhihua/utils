package com.mengzhihua.utils.util;

import java.nio.ByteBuffer;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Segment KSUID (20-byte time-sortable ID, Base62).
 */
public final class KsuidUtil {

    private static final long EPOCH = 1_400_000_000L;
    private static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

    private KsuidUtil() {
    }

    public static String next() {
        return encode(System.currentTimeMillis() / 1000, randomPayload());
    }

    public static boolean isValid(String ksuid) {
        return ksuid != null && ksuid.length() == 27 && decode(ksuid) != null;
    }

    public static long timestamp(String ksuid) {
        byte[] raw = decode(ksuid);
        if (raw == null) {
            throw new IllegalArgumentException("invalid KSUID");
        }
        ByteBuffer buffer = ByteBuffer.wrap(raw);
        return Integer.toUnsignedLong(buffer.getInt()) + EPOCH;
    }

    static String encode(long epochSeconds, byte[] payload) {
        int timestamp = (int) (epochSeconds - EPOCH);
        byte[] raw = new byte[20];
        raw[0] = (byte) (timestamp >>> 24);
        raw[1] = (byte) (timestamp >>> 16);
        raw[2] = (byte) (timestamp >>> 8);
        raw[3] = (byte) timestamp;
        System.arraycopy(payload, 0, raw, 4, 16);
        return toBase62(raw);
    }

    private static byte[] randomPayload() {
        byte[] payload = new byte[16];
        ThreadLocalRandom.current().nextBytes(payload);
        return payload;
    }

    private static String toBase62(byte[] data) {
        java.math.BigInteger value = new java.math.BigInteger(1, data);
        char[] out = new char[27];
        for (int i = 26; i >= 0; i--) {
            java.math.BigInteger[] div = value.divideAndRemainder(java.math.BigInteger.valueOf(62));
            out[i] = ALPHABET[div[1].intValue()];
            value = div[0];
        }
        return new String(out);
    }

    private static byte[] decode(String ksuid) {
        if (ksuid == null || ksuid.length() != 27) {
            return null;
        }
        java.math.BigInteger value = java.math.BigInteger.ZERO;
        for (int i = 0; i < ksuid.length(); i++) {
            int index = indexOf(ksuid.charAt(i));
            if (index < 0) {
                return null;
            }
            value = value.multiply(java.math.BigInteger.valueOf(62)).add(java.math.BigInteger.valueOf(index));
        }
        byte[] raw = value.toByteArray();
        byte[] padded = new byte[20];
        int src = raw[0] == 0 ? 1 : 0;
        int length = raw.length - src;
        if (length > 20) {
            return null;
        }
        System.arraycopy(raw, src, padded, 20 - length, length);
        return padded;
    }

    private static int indexOf(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'Z') {
            return 10 + (c - 'A');
        }
        if (c >= 'a' && c <= 'z') {
            return 36 + (c - 'a');
        }
        return -1;
    }
}
