package com.mengzhihua.utils.util;

import java.nio.charset.StandardCharsets;

/**
 * RFC 2045 Quoted-Printable (Apache Commons Codec).
 */
public final class QuotedPrintableUtil {

    private QuotedPrintableUtil() {
    }

    public static String encode(String text) {
        return encode(text == null ? new byte[0] : text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] data) {
        byte[] bytes = data == null ? new byte[0] : data;
        StringBuilder builder = new StringBuilder(bytes.length * 3);
        int line = 0;
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i] & 0xff;
            boolean encode = b > 126 || b < 32 || b == '=' || (b == ' ' || b == '\t') && nextIsEnd(bytes, i);
            String token;
            if (encode) {
                token = String.format("=%02X", b);
            } else {
                token = String.valueOf((char) b);
            }
            if (line + token.length() >= 76) {
                builder.append("=\r\n");
                line = 0;
            }
            builder.append(token);
            line += token.length();
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
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(encoded.length());
        for (int i = 0; i < encoded.length(); i++) {
            char c = encoded.charAt(i);
            if (c == '=' && i + 1 < encoded.length() && encoded.charAt(i + 1) == '\r') {
                i++;
                if (i + 1 < encoded.length() && encoded.charAt(i + 1) == '\n') {
                    i++;
                }
                continue;
            }
            if (c == '=' && i + 1 < encoded.length() && encoded.charAt(i + 1) == '\n') {
                i++;
                continue;
            }
            if (c == '=' && i + 2 < encoded.length()) {
                int hi = Character.digit(encoded.charAt(i + 1), 16);
                int lo = Character.digit(encoded.charAt(i + 2), 16);
                if (hi >= 0 && lo >= 0) {
                    out.write((hi << 4) | lo);
                    i += 2;
                    continue;
                }
            }
            out.write(c);
        }
        return out.toByteArray();
    }

    private static boolean nextIsEnd(byte[] bytes, int i) {
        return i + 1 == bytes.length;
    }
}
