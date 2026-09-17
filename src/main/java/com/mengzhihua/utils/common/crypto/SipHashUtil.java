package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;

/**
 * SipHash-2-4 (Guava {@code Hashing.sipHash24}).
 */
public final class SipHashUtil {

    private SipHashUtil() {
    }

    public static long hash(byte[] key16, byte[] data) {
        if (key16 == null || key16.length != 16) {
            throw new IllegalArgumentException("SipHash key must be 16 bytes");
        }
        byte[] bytes = data == null ? new byte[0] : data;
        long k0 = leLong(key16, 0);
        long k1 = leLong(key16, 8);
        long v0 = 0x736f6d6570736575L ^ k0;
        long v1 = 0x646f72616e646f6dL ^ k1;
        long v2 = 0x6c7967656e657261L ^ k0;
        long v3 = 0x7465646279746573L ^ k1;
        int index = 0;
        while (index + 8 <= bytes.length) {
            long m = leLong(bytes, index);
            v3 ^= m;
            long[] s = sipRounds(v0, v1, v2, v3, 2);
            v0 = s[0] ^ m;
            v1 = s[1];
            v2 = s[2];
            v3 = s[3];
            index += 8;
        }
        long last = ((long) bytes.length) << 56;
        for (int i = 0; index + i < bytes.length; i++) {
            last |= (bytes[index + i] & 0xffL) << (8 * i);
        }
        v3 ^= last;
        long[] s = sipRounds(v0, v1, v2, v3, 2);
        v0 = s[0] ^ last;
        v1 = s[1];
        v2 = s[2] ^ 0xff;
        v3 = s[3];
        s = sipRounds(v0, v1, v2, v3, 4);
        return s[0] ^ s[1] ^ s[2] ^ s[3];
    }

    public static String hashHex(String text) {
        byte[] key = new byte[16];
        for (int i = 0; i < 16; i++) {
            key[i] = (byte) i;
        }
        return String.format(java.util.Locale.ROOT, "%016x",
                hash(key, text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8)));
    }

    private static long[] sipRounds(long v0, long v1, long v2, long v3, int times) {
        for (int i = 0; i < times; i++) {
            v0 += v1;
            v1 = Long.rotateLeft(v1, 13);
            v1 ^= v0;
            v0 = Long.rotateLeft(v0, 32);
            v2 += v3;
            v3 = Long.rotateLeft(v3, 16);
            v3 ^= v2;
            v0 += v3;
            v3 = Long.rotateLeft(v3, 21);
            v3 ^= v0;
            v2 += v1;
            v1 = Long.rotateLeft(v1, 17);
            v1 ^= v2;
            v2 = Long.rotateLeft(v2, 32);
        }
        return new long[] {v0, v1, v2, v3};
    }

    private static long leLong(byte[] data, int index) {
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
