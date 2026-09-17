package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * RFC 7693 BLAKE2s-256 (Commons Codec / libsodium style, JDK only).
 * {@code ""} → {@code 69217a3079908094e11121d042354a7c1f55b6482ca1a51e1b250dfd1cd0c48d};
 * {@code "abc"} → {@code 508c5e8c327c14e2e1a72ba34eeb452f37458b209ed63a294d999b4c86675982}.
 */
public final class Blake2sUtil {

    private static final int[] IV = {
            0x6A09E667, 0xBB67AE85, 0x3C6EF372, 0xA54FF53A,
            0x510E527F, 0x9B05688C, 0x1F83D9AB, 0x5BE0CD19
    };
    private static final byte[][] SIGMA = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
            {14, 10, 4, 8, 9, 15, 13, 6, 1, 12, 0, 2, 11, 7, 5, 3},
            {11, 8, 12, 0, 5, 2, 15, 13, 10, 14, 3, 6, 7, 1, 9, 4},
            {7, 9, 3, 1, 13, 12, 11, 14, 2, 6, 5, 10, 4, 0, 15, 8},
            {9, 0, 5, 7, 2, 4, 10, 15, 14, 1, 11, 12, 6, 8, 3, 13},
            {2, 12, 6, 10, 0, 11, 8, 3, 4, 13, 7, 5, 15, 14, 1, 9},
            {12, 5, 1, 15, 14, 13, 4, 10, 0, 7, 6, 3, 9, 2, 8, 11},
            {13, 11, 7, 14, 12, 1, 3, 9, 5, 0, 15, 4, 8, 6, 2, 10},
            {6, 15, 14, 9, 11, 3, 0, 8, 12, 2, 13, 7, 1, 4, 10, 5},
            {10, 2, 8, 4, 7, 6, 1, 5, 15, 11, 9, 14, 3, 12, 13, 0}
    };

    private Blake2sUtil() {
    }

    public static String hash(String text) {
        return hash(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String hash(byte[] data) {
        return HexFormat.of().formatHex(digest(data == null ? new byte[0] : data));
    }

    public static byte[] digest(byte[] message) {
        byte[] input = message == null ? new byte[0] : message;
        int[] h = Arrays.copyOf(IV, 8);
        h[0] ^= 0x01010020;
        int offset = 0;
        while (offset + 64 < input.length) {
            compress(h, input, offset, offset + 64, false);
            offset += 64;
        }
        byte[] last = new byte[64];
        int remain = input.length - offset;
        System.arraycopy(input, offset, last, 0, remain);
        compress(h, last, 0, input.length, true);
        byte[] out = new byte[32];
        for (int i = 0; i < 8; i++) {
            out[i * 4] = (byte) h[i];
            out[i * 4 + 1] = (byte) (h[i] >>> 8);
            out[i * 4 + 2] = (byte) (h[i] >>> 16);
            out[i * 4 + 3] = (byte) (h[i] >>> 24);
        }
        return out;
    }

    private static void compress(int[] h, byte[] block, int offset, int counter, boolean last) {
        int[] v = new int[16];
        System.arraycopy(h, 0, v, 0, 8);
        System.arraycopy(IV, 0, v, 8, 8);
        v[12] ^= counter;
        v[13] ^= (int) ((counter & 0xffffffffL) >>> 32);
        if (last) {
            v[14] = ~v[14];
        }
        int[] m = new int[16];
        for (int i = 0; i < 16; i++) {
            int p = offset + i * 4;
            m[i] = (block[p] & 0xff)
                    | ((block[p + 1] & 0xff) << 8)
                    | ((block[p + 2] & 0xff) << 16)
                    | (block[p + 3] << 24);
        }
        for (int r = 0; r < 10; r++) {
            byte[] s = SIGMA[r];
            g(v, 0, 4, 8, 12, m[s[0]], m[s[1]]);
            g(v, 1, 5, 9, 13, m[s[2]], m[s[3]]);
            g(v, 2, 6, 10, 14, m[s[4]], m[s[5]]);
            g(v, 3, 7, 11, 15, m[s[6]], m[s[7]]);
            g(v, 0, 5, 10, 15, m[s[8]], m[s[9]]);
            g(v, 1, 6, 11, 12, m[s[10]], m[s[11]]);
            g(v, 2, 7, 8, 13, m[s[12]], m[s[13]]);
            g(v, 3, 4, 9, 14, m[s[14]], m[s[15]]);
        }
        for (int i = 0; i < 8; i++) {
            h[i] ^= v[i] ^ v[i + 8];
        }
    }

    private static void g(int[] v, int a, int b, int c, int d, int x, int y) {
        v[a] = v[a] + v[b] + x;
        v[d] = Integer.rotateRight(v[d] ^ v[a], 16);
        v[c] = v[c] + v[d];
        v[b] = Integer.rotateRight(v[b] ^ v[c], 12);
        v[a] = v[a] + v[b] + y;
        v[d] = Integer.rotateRight(v[d] ^ v[a], 8);
        v[c] = v[c] + v[d];
        v[b] = Integer.rotateRight(v[b] ^ v[c], 7);
    }
}
