package com.mengzhihua.utils.common.text;

/**
 * ROT13 / ROT47 (classic obfuscation helpers).
 */
public final class RotUtil {

    private RotUtil() {
    }

    public static String rot13(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 'a' && c <= 'z') {
                builder.append((char) ('a' + (c - 'a' + 13) % 26));
            } else if (c >= 'A' && c <= 'Z') {
                builder.append((char) ('A' + (c - 'A' + 13) % 26));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }

    public static String rot47(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c >= 33 && c <= 126) {
                builder.append((char) (33 + (c - 33 + 47) % 94));
            } else {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
