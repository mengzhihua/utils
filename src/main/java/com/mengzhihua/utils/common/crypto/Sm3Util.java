package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * GM/T 0004-2012 SM3 hash (Hutool {@code SmUtil.sm3} style, JDK only).
 */
public final class Sm3Util {

    private static final int[] IV = {
            0x7380166F, 0x4914B2B9, 0x172442D7, 0xDA8A0600,
            0xA96F30BC, 0x163138AA, 0xE38DEE4D, 0xB0FB0E4E
    };

    private Sm3Util() {
    }

    public static String hash(String text) {
        return hash(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String hash(byte[] data) {
        return HexFormat.of().formatHex(digest(data == null ? new byte[0] : data));
    }

    /**
     * HMAC-SM3 (RFC 2104, block 64).
     */
    public static String hmac(String text, String key) {
        byte[] message = text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
        byte[] secret = key == null ? new byte[0] : key.getBytes(StandardCharsets.UTF_8);
        return HexFormat.of().formatHex(hmac(message, secret));
    }

    public static byte[] hmac(byte[] message, byte[] key) {
        byte[] blockKey = key == null ? new byte[0] : key;
        if (blockKey.length > 64) {
            blockKey = digest(blockKey);
        }
        byte[] padded = new byte[64];
        System.arraycopy(blockKey, 0, padded, 0, blockKey.length);
        byte[] inner = new byte[64 + (message == null ? 0 : message.length)];
        for (int i = 0; i < 64; i++) {
            inner[i] = (byte) (padded[i] ^ 0x36);
        }
        if (message != null && message.length > 0) {
            System.arraycopy(message, 0, inner, 64, message.length);
        }
        byte[] innerHash = digest(inner);
        byte[] outer = new byte[64 + innerHash.length];
        for (int i = 0; i < 64; i++) {
            outer[i] = (byte) (padded[i] ^ 0x5c);
        }
        System.arraycopy(innerHash, 0, outer, 64, innerHash.length);
        return digest(outer);
    }

    public static byte[] digest(byte[] message) {
        byte[] padded = pad(message == null ? new byte[0] : message);
        int[] v = Arrays.copyOf(IV, 8);
        for (int offset = 0; offset < padded.length; offset += 64) {
            compress(v, padded, offset);
        }
        byte[] out = new byte[32];
        for (int i = 0; i < 8; i++) {
            out[i * 4] = (byte) (v[i] >>> 24);
            out[i * 4 + 1] = (byte) (v[i] >>> 16);
            out[i * 4 + 2] = (byte) (v[i] >>> 8);
            out[i * 4 + 3] = (byte) v[i];
        }
        return out;
    }

    private static byte[] pad(byte[] message) {
        long bitLen = (long) message.length * 8;
        int paddedLen = message.length + 1;
        while (paddedLen % 64 != 56) {
            paddedLen++;
        }
        paddedLen += 8;
        byte[] out = Arrays.copyOf(message, paddedLen);
        out[message.length] = (byte) 0x80;
        for (int i = 0; i < 8; i++) {
            out[paddedLen - 1 - i] = (byte) (bitLen >>> (8 * i));
        }
        return out;
    }

    private static void compress(int[] v, byte[] block, int offset) {
        int[] w = new int[68];
        int[] w1 = new int[64];
        for (int i = 0; i < 16; i++) {
            int p = offset + i * 4;
            w[i] = ((block[p] & 0xff) << 24)
                    | ((block[p + 1] & 0xff) << 16)
                    | ((block[p + 2] & 0xff) << 8)
                    | (block[p + 3] & 0xff);
        }
        for (int j = 16; j < 68; j++) {
            w[j] = p1(w[j - 16] ^ w[j - 9] ^ Integer.rotateLeft(w[j - 3], 15))
                    ^ Integer.rotateLeft(w[j - 13], 7)
                    ^ w[j - 6];
        }
        for (int j = 0; j < 64; j++) {
            w1[j] = w[j] ^ w[j + 4];
        }
        int a = v[0];
        int b = v[1];
        int c = v[2];
        int d = v[3];
        int e = v[4];
        int f = v[5];
        int g = v[6];
        int h = v[7];
        for (int j = 0; j < 64; j++) {
            int tj = j <= 15 ? 0x79CC4519 : 0x7A879D8A;
            int ss1 = Integer.rotateLeft(Integer.rotateLeft(a, 12) + e + Integer.rotateLeft(tj, j), 7);
            int ss2 = ss1 ^ Integer.rotateLeft(a, 12);
            int tt1 = ff(j, a, b, c) + d + ss2 + w1[j];
            int tt2 = gg(j, e, f, g) + h + ss1 + w[j];
            d = c;
            c = Integer.rotateLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = Integer.rotateLeft(f, 19);
            f = e;
            e = p0(tt2);
        }
        v[0] ^= a;
        v[1] ^= b;
        v[2] ^= c;
        v[3] ^= d;
        v[4] ^= e;
        v[5] ^= f;
        v[6] ^= g;
        v[7] ^= h;
    }

    private static int ff(int j, int x, int y, int z) {
        return j <= 15 ? x ^ y ^ z : (x & y) | (x & z) | (y & z);
    }

    private static int gg(int j, int x, int y, int z) {
        return j <= 15 ? x ^ y ^ z : (x & y) | ((~x) & z);
    }

    private static int p0(int x) {
        return x ^ Integer.rotateLeft(x, 9) ^ Integer.rotateLeft(x, 17);
    }

    private static int p1(int x) {
        return x ^ Integer.rotateLeft(x, 15) ^ Integer.rotateLeft(x, 23);
    }
}
