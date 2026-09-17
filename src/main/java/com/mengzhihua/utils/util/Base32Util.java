package com.mengzhihua.utils.util;

/**
 * RFC 4648 Base32 (no padding by default).
 */
public final class Base32Util {

    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray();
    private static final int[] DECODE = new int[128];

    static {
        java.util.Arrays.fill(DECODE, -1);
        for (int i = 0; i < ALPHABET.length; i++) {
            DECODE[ALPHABET[i]] = i;
            DECODE[Character.toLowerCase(ALPHABET[i])] = i;
        }
    }

    private Base32Util() {
    }

    public static String encode(String text) {
        return text == null ? null : encode(text.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        if (data == null) {
            return null;
        }
        if (data.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder((data.length * 8 + 4) / 5);
        int buffer = 0;
        int bits = 0;
        for (byte b : data) {
            buffer = (buffer << 8) | (b & 0xff);
            bits += 8;
            while (bits >= 5) {
                builder.append(ALPHABET[(buffer >> (bits - 5)) & 31]);
                bits -= 5;
            }
        }
        if (bits > 0) {
            builder.append(ALPHABET[(buffer << (5 - bits)) & 31]);
        }
        return builder.toString();
    }

    public static byte[] decode(String text) {
        if (text == null) {
            return null;
        }
        String compact = text.replace("=", "").replace(" ", "");
        if (compact.isEmpty()) {
            return new byte[0];
        }
        int buffer = 0;
        int bits = 0;
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        for (int i = 0; i < compact.length(); i++) {
            char c = compact.charAt(i);
            if (c >= DECODE.length || DECODE[c] < 0) {
                throw new IllegalArgumentException("invalid base32: " + text);
            }
            buffer = (buffer << 5) | DECODE[c];
            bits += 5;
            if (bits >= 8) {
                out.write((buffer >> (bits - 8)) & 0xff);
                bits -= 8;
            }
        }
        return out.toByteArray();
    }

    public static String decodeToString(String text) {
        byte[] bytes = decode(text);
        return bytes == null ? null : new String(bytes, java.nio.charset.StandardCharsets.UTF_8);
    }
}
