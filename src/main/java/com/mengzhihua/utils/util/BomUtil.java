package com.mengzhihua.utils.util;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Byte-order mark helpers (Apache Commons IO {@code ByteOrderMark}).
 */
public final class BomUtil {

    public static final byte[] UTF_8 = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
    public static final byte[] UTF_16BE = {(byte) 0xfe, (byte) 0xff};
    public static final byte[] UTF_16LE = {(byte) 0xff, (byte) 0xfe};

    private BomUtil() {
    }

    public static boolean hasUtf8Bom(byte[] data) {
        return startsWith(data, UTF_8);
    }

    public static byte[] strip(byte[] data) {
        if (startsWith(data, UTF_8)) {
            return Arrays.copyOfRange(data, 3, data.length);
        }
        if (startsWith(data, UTF_16BE) || startsWith(data, UTF_16LE)) {
            return Arrays.copyOfRange(data, 2, data.length);
        }
        return data == null ? new byte[0] : data;
    }

    public static String stripUtf8(String text) {
        if (text == null) {
            return "";
        }
        if (!text.isEmpty() && text.charAt(0) == '\uFEFF') {
            return text.substring(1);
        }
        return text;
    }

    public static byte[] prependUtf8(byte[] data) {
        byte[] body = data == null ? new byte[0] : data;
        if (startsWith(body, UTF_8)) {
            return body;
        }
        byte[] out = new byte[3 + body.length];
        System.arraycopy(UTF_8, 0, out, 0, 3);
        System.arraycopy(body, 0, out, 3, body.length);
        return out;
    }

    public static String detect(byte[] data) {
        if (startsWith(data, UTF_8)) {
            return "UTF-8";
        }
        if (startsWith(data, UTF_16BE)) {
            return "UTF-16BE";
        }
        if (startsWith(data, UTF_16LE)) {
            return "UTF-16LE";
        }
        return null;
    }

    public static byte[] utf8(String text) {
        return (text == null ? "" : text).getBytes(StandardCharsets.UTF_8);
    }

    private static boolean startsWith(byte[] data, byte[] prefix) {
        if (data == null || data.length < prefix.length) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (data[i] != prefix[i]) {
                return false;
            }
        }
        return true;
    }
}
