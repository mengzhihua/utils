package com.mengzhihua.utils.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Pattern;

/**
 * Base64 / Base64URL / MIME (Apache Commons Codec / Hutool subset).
 */
public final class Base64Util {

    private static final Pattern BASE64 = Pattern.compile("^[A-Za-z0-9+/]*={0,2}$");
    private static final Pattern BASE64_URL = Pattern.compile("^[A-Za-z0-9\\-_]*={0,2}$");

    private Base64Util() {
    }

    public static String encode(String text) {
        return text == null ? null : encode(text.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] bytes) {
        return bytes == null ? null : Base64.getEncoder().encodeToString(bytes);
    }

    public static String encodeUrl(String text) {
        return text == null ? null : Base64.getUrlEncoder().withoutPadding()
                .encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    public static byte[] decode(String text) {
        if (text == null) {
            return null;
        }
        String compact = text.replaceAll("\\s+", "");
        return compact.contains("-") || compact.contains("_")
                ? Base64.getUrlDecoder().decode(compact)
                : Base64.getDecoder().decode(compact);
    }

    public static String decodeToString(String text) {
        byte[] bytes = decode(text);
        return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
    }

    public static boolean isBase64(String text) {
        if (StringUtil.isBlank(text)) {
            return false;
        }
        String compact = text.replaceAll("\\s+", "");
        if ((compact.length() & 3) != 0) {
            return false;
        }
        return BASE64.matcher(compact).matches() || BASE64_URL.matcher(compact).matches();
    }
}
