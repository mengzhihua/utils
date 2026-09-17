package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * Guava-style unsigned integer helpers.
 */
public final class UnsignedUtil {

    private UnsignedUtil() {
    }

    public static String toUnsignedString(int value) {
        return Integer.toUnsignedString(value);
    }

    public static String toUnsignedString(long value) {
        return Long.toUnsignedString(value);
    }

    public static int parseUnsignedInt(String text) {
        return Integer.parseUnsignedInt(text.trim());
    }

    public static long parseUnsignedLong(String text) {
        return Long.parseUnsignedLong(text.trim());
    }

    public static int compare(int left, int right) {
        return Integer.compareUnsigned(left, right);
    }

    public static String hex(int value) {
        return String.format(Locale.ROOT, "%08x", value);
    }
}
