package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * SHAKE128 / SHAKE256 (FIPS 202). Empty SHAKE128-256 → {@code 7f9c2ba4...fa66ef26}.
 */
public final class ShakeUtil {

    private static final int[] RHO = new int[25];
    private static final long[] RC = {
            0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL, 0x8000000080008000L,
            0x000000000000808bL, 0x0000000080000001L, 0x8000000080008081L, 0x8000000000008009L,
            0x000000000000008aL, 0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
            0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L, 0x8000000000008003L,
            0x8000000000008002L, 0x8000000000000080L, 0x000000000000800aL, 0x800000008000000aL,
            0x8000000080008081L, 0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L
    };

    static {
        int x = 1;
        int y = 0;
        for (int t = 0; t < 24; t++) {
            RHO[index(x, y)] = ((t + 1) * (t + 2) / 2) % 64;
            int nx = y;
            y = Math.floorMod(2 * x + 3 * y, 5);
            x = nx;
        }
    }

    private ShakeUtil() {
    }

    public static String shake128(String text) {
        return HexFormat.of().formatHex(shake128(bytes(text), 32));
    }

    public static String shake256(String text) {
        return HexFormat.of().formatHex(shake256(bytes(text), 64));
    }

    public static byte[] shake128(byte[] message, int outputLength) {
        return xof(message, 168, outputLength);
    }

    public static byte[] shake256(byte[] message, int outputLength) {
        return xof(message, 136, outputLength);
    }

    private static byte[] bytes(String text) {
        return text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] xof(byte[] message, int rate, int outputLength) {
        byte[] input = message == null ? new byte[0] : message;
        if (outputLength < 0) {
            throw new IllegalArgumentException("output length must be >= 0");
        }
        long[] state = new long[25];
        int offset = 0;
        while (offset + rate <= input.length) {
            xor(state, input, offset, rate);
            keccakf(state);
            offset += rate;
        }
        byte[] block = new byte[rate];
        System.arraycopy(input, offset, block, 0, input.length - offset);
        block[input.length - offset] ^= 0x1F;
        block[rate - 1] ^= 0x80;
        xor(state, block, 0, rate);
        keccakf(state);
        byte[] out = new byte[outputLength];
        int produced = 0;
        while (produced < outputLength) {
            int take = Math.min(rate, outputLength - produced);
            extract(state, out, produced, take);
            produced += take;
            if (produced < outputLength) {
                keccakf(state);
            }
        }
        return out;
    }

    private static void xor(long[] state, byte[] data, int offset, int length) {
        for (int i = 0; i < length; i++) {
            int lane = i / 8;
            int shift = (i % 8) * 8;
            state[lane] ^= (data[offset + i] & 0xffL) << shift;
        }
    }

    private static void extract(long[] state, byte[] out, int dest, int length) {
        for (int i = 0; i < length; i++) {
            int lane = i / 8;
            int shift = (i % 8) * 8;
            out[dest + i] = (byte) (state[lane] >>> shift);
        }
    }

    private static void keccakf(long[] a) {
        long[] b = new long[25];
        long[] c = new long[5];
        long[] d = new long[5];
        for (int round = 0; round < 24; round++) {
            for (int x = 0; x < 5; x++) {
                c[x] = a[index(x, 0)] ^ a[index(x, 1)] ^ a[index(x, 2)] ^ a[index(x, 3)] ^ a[index(x, 4)];
            }
            for (int x = 0; x < 5; x++) {
                d[x] = c[mod5(x - 1)] ^ Long.rotateLeft(c[mod5(x + 1)], 1);
            }
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    a[index(x, y)] ^= d[x];
                }
            }
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    b[index(y, 2 * x + 3 * y)] = Long.rotateLeft(a[index(x, y)], RHO[index(x, y)]);
                }
            }
            for (int x = 0; x < 5; x++) {
                for (int y = 0; y < 5; y++) {
                    a[index(x, y)] = b[index(x, y)] ^ (~b[index(x + 1, y)] & b[index(x + 2, y)]);
                }
            }
            a[0] ^= RC[round];
        }
    }

    private static int index(int x, int y) {
        return mod5(x) + 5 * mod5(y);
    }

    private static int mod5(int value) {
        return Math.floorMod(value, 5);
    }
}
