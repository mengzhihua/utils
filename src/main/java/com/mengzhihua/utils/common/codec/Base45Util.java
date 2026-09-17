package com.mengzhihua.utils.common.codec;


import java.nio.charset.StandardCharsets;

/**
 * RFC 9285 Base45 (QR / EU DCC style).
 */
public final class Base45Util {

    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ $%*+-./:";
    private static final int[] DECODE = new int[128];

    static {
        java.util.Arrays.fill(DECODE, -1);
        for (int i = 0; i < ALPHABET.length(); i++) {
            DECODE[ALPHABET.charAt(i)] = i;
        }
    }

    private Base45Util() {
    }

    public static String encode(String text) {
        return encode(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        StringBuilder builder = new StringBuilder();
        int i = 0;
        while (i < bytes.length) {
            if (i + 1 < bytes.length) {
                int value = ((bytes[i] & 0xff) << 8) | (bytes[i + 1] & 0xff);
                int c = value % 45;
                value /= 45;
                int d = value % 45;
                int e = value / 45;
                builder.append(ALPHABET.charAt(c)).append(ALPHABET.charAt(d)).append(ALPHABET.charAt(e));
                i += 2;
            } else {
                int value = bytes[i] & 0xff;
                builder.append(ALPHABET.charAt(value % 45)).append(ALPHABET.charAt(value / 45));
                i++;
            }
        }
        return builder.toString();
    }

    public static String decodeToString(String encoded) {
        return new String(decode(encoded), StandardCharsets.UTF_8);
    }

    public static byte[] decode(String encoded) {
        if (encoded == null || encoded.isEmpty()) {
            return new byte[0];
        }
        if (encoded.length() % 3 == 1) {
            throw new IllegalArgumentException("invalid base45 length");
        }
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        int i = 0;
        while (i < encoded.length()) {
            if (i + 2 < encoded.length()) {
                int c = value(encoded.charAt(i));
                int d = value(encoded.charAt(i + 1));
                int e = value(encoded.charAt(i + 2));
                int value = c + d * 45 + e * 45 * 45;
                out.write((value >>> 8) & 0xff);
                out.write(value & 0xff);
                i += 3;
            } else {
                int c = value(encoded.charAt(i));
                int d = value(encoded.charAt(i + 1));
                int value = c + d * 45;
                out.write(value);
                i += 2;
            }
        }
        return out.toByteArray();
    }

    private static int value(char c) {
        int v = c < 128 ? DECODE[c] : -1;
        if (v < 0) {
            throw new IllegalArgumentException("invalid base45 character: " + c);
        }
        return v;
    }
}
