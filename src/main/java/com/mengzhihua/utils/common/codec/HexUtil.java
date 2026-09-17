package com.mengzhihua.utils.common.codec;


import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

/**
 * Hex encode / decode.
 */
public final class HexUtil {

    private static final HexFormat HEX = HexFormat.of();

    private HexUtil() {
    }

    public static String encode(byte[] bytes) {
        return bytes == null ? null : HEX.formatHex(bytes);
    }

    public static String encode(String text) {
        return text == null ? null : encode(text.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(String hex) {
        if (hex == null) {
            return null;
        }
        return HEX.parseHex(hex);
    }

    public static String decodeToString(String hex) {
        byte[] bytes = decode(hex);
        return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
    }

    public static boolean isHex(String text) {
        if (text == null || text.isEmpty() || (text.length() & 1) == 1) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (Character.digit(text.charAt(i), 16) < 0) {
                return false;
            }
        }
        return true;
    }
}
