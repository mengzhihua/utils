package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HexFormat;

/**
 * RIPEMD-160 (ISO/IEC 10118-3 / Commons Codec). Empty → {@code 9c1185a5...258d31}; {@code abc} matches hashlib.
 */
public final class Ripemd160Util {

    private static final int[][] R = {
            {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15},
            {7, 4, 13, 1, 10, 6, 15, 3, 12, 0, 9, 5, 2, 14, 11, 8},
            {3, 10, 14, 4, 9, 15, 8, 1, 2, 7, 0, 6, 13, 11, 5, 12},
            {1, 9, 11, 10, 0, 8, 12, 4, 13, 3, 7, 15, 14, 5, 6, 2},
            {4, 0, 5, 9, 7, 12, 2, 10, 14, 1, 3, 8, 11, 6, 15, 13}
    };
    private static final int[][] RR = {
            {5, 14, 7, 0, 9, 2, 11, 4, 13, 6, 15, 8, 1, 10, 3, 12},
            {6, 11, 3, 7, 0, 13, 5, 10, 14, 15, 8, 12, 4, 9, 1, 2},
            {15, 5, 1, 3, 7, 14, 6, 9, 11, 8, 12, 2, 10, 0, 4, 13},
            {8, 6, 4, 1, 3, 11, 15, 0, 5, 12, 2, 13, 9, 7, 10, 14},
            {12, 15, 10, 4, 1, 5, 8, 7, 6, 2, 13, 14, 0, 3, 9, 11}
    };
    private static final int[][] S = {
            {11, 14, 15, 12, 5, 8, 7, 9, 11, 13, 14, 15, 6, 7, 9, 8},
            {7, 6, 8, 13, 11, 9, 7, 15, 7, 12, 15, 9, 11, 7, 13, 12},
            {11, 13, 6, 7, 14, 9, 13, 15, 14, 8, 13, 6, 5, 12, 7, 5},
            {11, 12, 14, 15, 14, 15, 9, 8, 9, 14, 5, 6, 8, 6, 5, 12},
            {9, 15, 5, 11, 6, 8, 13, 12, 5, 12, 13, 14, 11, 8, 5, 6}
    };
    private static final int[][] SS = {
            {8, 9, 9, 11, 13, 15, 15, 5, 7, 7, 8, 11, 14, 14, 12, 6},
            {9, 13, 15, 7, 12, 8, 9, 11, 7, 7, 12, 7, 6, 15, 13, 11},
            {9, 7, 15, 11, 8, 6, 6, 14, 12, 13, 5, 14, 13, 13, 7, 5},
            {15, 5, 8, 11, 14, 14, 6, 14, 6, 9, 12, 9, 12, 5, 15, 8},
            {8, 5, 12, 9, 12, 5, 14, 6, 8, 13, 6, 5, 15, 13, 11, 11}
    };
    private static final int[] KL = {0x00000000, 0x5A827999, 0x6ED9EBA1, 0x8F1BBCDC, 0xA953FD4E};
    private static final int[] KR = {0x50A28BE6, 0x5C4DD124, 0x6D703EF3, 0x7A6D76E9, 0x00000000};

    private Ripemd160Util() {
    }

    public static String hash(String text) {
        return hash(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String hash(byte[] data) {
        return HexFormat.of().formatHex(digest(data == null ? new byte[0] : data));
    }

    public static byte[] digest(byte[] message) {
        byte[] input = pad(message == null ? new byte[0] : message);
        int[] h = {0x67452301, 0xEFCDAB89, 0x98BADCFE, 0x10325476, 0xC3D2E1F0};
        int[] x = new int[16];
        for (int offset = 0; offset < input.length; offset += 64) {
            for (int i = 0; i < 16; i++) {
                int p = offset + i * 4;
                x[i] = (input[p] & 0xff)
                        | ((input[p + 1] & 0xff) << 8)
                        | ((input[p + 2] & 0xff) << 16)
                        | (input[p + 3] << 24);
            }
            compress(h, x);
        }
        byte[] out = new byte[20];
        for (int i = 0; i < 5; i++) {
            int w = h[i];
            out[i * 4] = (byte) w;
            out[i * 4 + 1] = (byte) (w >>> 8);
            out[i * 4 + 2] = (byte) (w >>> 16);
            out[i * 4 + 3] = (byte) (w >>> 24);
        }
        return out;
    }

    private static byte[] pad(byte[] message) {
        long bitLen = (long) message.length * 8;
        int padLen = (message.length % 64 < 56) ? 56 - message.length % 64 : 120 - message.length % 64;
        byte[] padded = Arrays.copyOf(message, message.length + padLen + 8);
        padded[message.length] = (byte) 0x80;
        for (int i = 0; i < 8; i++) {
            padded[padded.length - 8 + i] = (byte) (bitLen >>> (8 * i));
        }
        return padded;
    }

    private static void compress(int[] h, int[] x) {
        int al = h[0];
        int bl = h[1];
        int cl = h[2];
        int dl = h[3];
        int el = h[4];
        int ar = h[0];
        int br = h[1];
        int cr = h[2];
        int dr = h[3];
        int er = h[4];
        for (int j = 0; j < 80; j++) {
            int round = j / 16;
            int t = Integer.rotateLeft(al + f(round, bl, cl, dl) + x[R[round][j % 16]] + KL[round], S[round][j % 16]) + el;
            al = el;
            el = dl;
            dl = Integer.rotateLeft(cl, 10);
            cl = bl;
            bl = t;
            t = Integer.rotateLeft(ar + f(4 - round, br, cr, dr) + x[RR[round][j % 16]] + KR[round], SS[round][j % 16]) + er;
            ar = er;
            er = dr;
            dr = Integer.rotateLeft(cr, 10);
            cr = br;
            br = t;
        }
        int t = h[1] + cl + dr;
        h[1] = h[2] + dl + er;
        h[2] = h[3] + el + ar;
        h[3] = h[4] + al + br;
        h[4] = h[0] + bl + cr;
        h[0] = t;
    }

    private static int f(int round, int x, int y, int z) {
        return switch (round) {
            case 0 -> x ^ y ^ z;
            case 1 -> (x & y) | (~x & z);
            case 2 -> (x | ~y) ^ z;
            case 3 -> (x & z) | (y & ~z);
            default -> x ^ (y | ~z);
        };
    }
}
