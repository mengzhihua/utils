package com.mengzhihua.utils.common.codec;


import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Joachim Henke basE91. Round-trip for UTF-8 text.
 */
public final class Base91Util {

    private static final String ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&()*+,./:;<=>?@[]^_`{|}~\"";
    private static final int[] DECODE = new int[256];

    static {
        java.util.Arrays.fill(DECODE, -1);
        for (int i = 0; i < ALPHABET.length(); i++) {
            DECODE[ALPHABET.charAt(i)] = i;
        }
    }

    private Base91Util() {
    }

    public static String encode(String text) {
        return encode(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        StringBuilder out = new StringBuilder();
        int queue = 0;
        int nbits = 0;
        for (byte b : bytes) {
            queue |= (b & 0xff) << nbits;
            nbits += 8;
            if (nbits > 13) {
                int val = queue & 8191;
                if (val > 88) {
                    queue >>= 13;
                    nbits -= 13;
                } else {
                    val = queue & 16383;
                    queue >>= 14;
                    nbits -= 14;
                }
                out.append(ALPHABET.charAt(val % 91));
                out.append(ALPHABET.charAt(val / 91));
            }
        }
        if (nbits > 0) {
            out.append(ALPHABET.charAt(queue % 91));
            if (nbits > 7 || queue > 90) {
                out.append(ALPHABET.charAt(queue / 91));
            }
        }
        return out.toString();
    }

    public static String decodeToString(String encoded) {
        return new String(decode(encoded), StandardCharsets.UTF_8);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return new byte[0];
        }
        int queue = 0;
        int nbits = 0;
        int val = -1;
        List<Byte> out = new ArrayList<>();
        for (int i = 0; i < encoded.length(); i++) {
            int d = encoded.charAt(i) < 256 ? DECODE[encoded.charAt(i)] : -1;
            if (d == -1) {
                continue;
            }
            if (val < 0) {
                val = d;
                continue;
            }
            val += d * 91;
            queue |= val << nbits;
            nbits += (val & 8191) > 88 ? 13 : 14;
            do {
                out.add((byte) queue);
                queue >>= 8;
                nbits -= 8;
            } while (nbits > 7);
            val = -1;
        }
        if (val >= 0) {
            out.add((byte) (queue | (val << nbits)));
        }
        byte[] bytes = new byte[out.size()];
        for (int i = 0; i < out.size(); i++) {
            bytes[i] = out.get(i);
        }
        return bytes;
    }
}
