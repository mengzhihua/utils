package com.mengzhihua.utils.common.codec;


import java.nio.charset.StandardCharsets;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Adobe Ascii85 / Base85.
 */
public final class Base85Util {

    private Base85Util() {
    }

    public static String encode(String text) {
        return encode(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        StringBuilder builder = new StringBuilder("<~");
        int i = 0;
        while (i < bytes.length) {
            long value = 0;
            int count = 0;
            for (int j = 0; j < 4; j++) {
                value <<= 8;
                if (i < bytes.length) {
                    value |= bytes[i] & 0xffL;
                    i++;
                    count++;
                }
            }
            if (count == 4 && value == 0) {
                builder.append('z');
                continue;
            }
            char[] block = new char[5];
            for (int j = 4; j >= 0; j--) {
                block[j] = (char) (value % 85 + 33);
                value /= 85;
            }
            builder.append(block, 0, count + 1);
        }
        return builder.append("~>").toString();
    }

    public static String decodeToString(String encoded) {
        return new String(decode(encoded), StandardCharsets.UTF_8);
    }

    public static byte[] decode(String encoded) {
        if (StringUtil.isBlank(encoded)) {
            return new byte[0];
        }
        String text = encoded.trim();
        if (text.startsWith("<~")) {
            text = text.substring(2);
        }
        if (text.endsWith("~>")) {
            text = text.substring(0, text.length() - 2);
        }
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        int[] tuple = new int[5];
        int n = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isWhitespace(c)) {
                continue;
            }
            if (c == 'z') {
                if (n != 0) {
                    throw new IllegalArgumentException("invalid ascii85 z");
                }
                out.write(0);
                out.write(0);
                out.write(0);
                out.write(0);
                continue;
            }
            if (c < 33 || c > 117) {
                throw new IllegalArgumentException("invalid ascii85 character");
            }
            tuple[n++] = c - 33;
            if (n == 5) {
                writeTuple(out, tuple, 5);
                n = 0;
            }
        }
        if (n > 0) {
            for (int i = n; i < 5; i++) {
                tuple[i] = 84;
            }
            writeTuple(out, tuple, n);
        }
        return out.toByteArray();
    }

    private static void writeTuple(java.io.ByteArrayOutputStream out, int[] tuple, int count) {
        long value = 0;
        for (int i = 0; i < 5; i++) {
            value = value * 85 + tuple[i];
        }
        int bytes = count == 5 ? 4 : count - 1;
        for (int i = 3; i >= 4 - bytes; i--) {
            out.write((int) ((value >>> (8 * i)) & 0xff));
        }
    }
}
