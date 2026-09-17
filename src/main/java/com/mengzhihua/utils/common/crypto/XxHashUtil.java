package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;

/**
 * xxHash32 (non-cryptographic, LZ4 / Guava-style fast hash).
 */
public final class XxHashUtil {

    private static final int P1 = 0x9E3779B1;
    private static final int P2 = 0x85EBCA77;
    private static final int P3 = 0xC2B2AE3D;
    private static final int P4 = 0x27D4EB2F;
    private static final int P5 = 0x165667B1;
    private static final long P64_1 = 0x9E3779B185EBCA87L;
    private static final long P64_2 = 0xC2B2AE3D27D4EB4FL;
    private static final long P64_3 = 0x165667B19E3779F9L;
    private static final long P64_4 = 0x85EBCA77C2B2AE63L;
    private static final long P64_5 = 0x27D4EB2F165667C5L;

    private XxHashUtil() {
    }

    public static int hash32(String text) {
        return hash32(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8), 0);
    }

    public static String hash32Hex(String text) {
        return String.format(java.util.Locale.ROOT, "%08x", hash32(text));
    }

    public static long hash64(String text) {
        return hash64(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8), 0);
    }

    public static String hash64Hex(String text) {
        return String.format(java.util.Locale.ROOT, "%016x", hash64(text));
    }

    /**
     * xxHash64, seed 0 empty digest {@code ef46db3751d8e999}.
     */
    public static long hash64(byte[] data, long seed) {
        byte[] bytes = data == null ? new byte[0] : data;
        int len = bytes.length;
        int index = 0;
        long h;
        if (len >= 32) {
            long v1 = seed + P64_1 + P64_2;
            long v2 = seed + P64_2;
            long v3 = seed;
            long v4 = seed - P64_1;
            int limit = len - 32;
            while (index <= limit) {
                v1 = round64(v1, readLong(bytes, index));
                v2 = round64(v2, readLong(bytes, index + 8));
                v3 = round64(v3, readLong(bytes, index + 16));
                v4 = round64(v4, readLong(bytes, index + 24));
                index += 32;
            }
            h = Long.rotateLeft(v1, 1) + Long.rotateLeft(v2, 7)
                    + Long.rotateLeft(v3, 12) + Long.rotateLeft(v4, 18);
            h = merge64(h, v1);
            h = merge64(h, v2);
            h = merge64(h, v3);
            h = merge64(h, v4);
        } else {
            h = seed + P64_5;
        }
        h += len;
        while (index + 8 <= len) {
            long k1 = readLong(bytes, index);
            k1 *= P64_2;
            k1 = Long.rotateLeft(k1, 31);
            k1 *= P64_1;
            h ^= k1;
            h = Long.rotateLeft(h, 27) * P64_1 + P64_4;
            index += 8;
        }
        if (index + 4 <= len) {
            h ^= Integer.toUnsignedLong(readInt(bytes, index)) * P64_1;
            h = Long.rotateLeft(h, 23) * P64_2 + P64_3;
            index += 4;
        }
        while (index < len) {
            h ^= (bytes[index] & 0xffL) * P64_5;
            h = Long.rotateLeft(h, 11) * P64_1;
            index++;
        }
        h ^= h >>> 33;
        h *= P64_2;
        h ^= h >>> 29;
        h *= P64_3;
        h ^= h >>> 32;
        return h;
    }

    public static int hash32(byte[] data, int seed) {
        byte[] bytes = data == null ? new byte[0] : data;
        int len = bytes.length;
        int index = 0;
        int h;
        if (len >= 16) {
            int v1 = seed + P1 + P2;
            int v2 = seed + P2;
            int v3 = seed;
            int v4 = seed - P1;
            int limit = len - 16;
            while (index <= limit) {
                v1 = round(v1, readInt(bytes, index));
                v2 = round(v2, readInt(bytes, index + 4));
                v3 = round(v3, readInt(bytes, index + 8));
                v4 = round(v4, readInt(bytes, index + 12));
                index += 16;
            }
            h = Integer.rotateLeft(v1, 1) + Integer.rotateLeft(v2, 7)
                    + Integer.rotateLeft(v3, 12) + Integer.rotateLeft(v4, 18);
        } else {
            h = seed + P5;
        }
        h += len;
        while (index + 4 <= len) {
            h = Integer.rotateLeft(h + readInt(bytes, index) * P3, 17) * P4;
            index += 4;
        }
        while (index < len) {
            h = Integer.rotateLeft(h + (bytes[index] & 0xff) * P5, 11) * P1;
            index++;
        }
        h ^= h >>> 15;
        h *= P2;
        h ^= h >>> 13;
        h *= P3;
        h ^= h >>> 16;
        return h;
    }

    private static int round(int acc, int input) {
        return Integer.rotateLeft(acc + input * P2, 13) * P1;
    }

    private static int readInt(byte[] data, int index) {
        return (data[index] & 0xff)
                | ((data[index + 1] & 0xff) << 8)
                | ((data[index + 2] & 0xff) << 16)
                | (data[index + 3] << 24);
    }

    private static long round64(long acc, long input) {
        acc += input * P64_2;
        acc = Long.rotateLeft(acc, 31);
        acc *= P64_1;
        return acc;
    }

    private static long merge64(long acc, long val) {
        return (acc ^ round64(0, val)) * P64_1 + P64_4;
    }

    private static long readLong(byte[] data, int index) {
        return (data[index] & 0xffL)
                | ((data[index + 1] & 0xffL) << 8)
                | ((data[index + 2] & 0xffL) << 16)
                | ((data[index + 3] & 0xffL) << 24)
                | ((data[index + 4] & 0xffL) << 32)
                | ((data[index + 5] & 0xffL) << 40)
                | ((data[index + 6] & 0xffL) << 48)
                | ((data[index + 7] & 0xffL) << 56);
    }
}
