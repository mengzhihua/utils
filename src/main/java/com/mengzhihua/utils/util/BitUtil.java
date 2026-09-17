package com.mengzhihua.utils.util;

/**
 * Bit flag helpers (Hutool {@code BitStatusUtil}).
 */
public final class BitUtil {

    private BitUtil() {
    }

    public static int set(int flags, int bit) {
        return flags | (1 << bit);
    }

    public static int unset(int flags, int bit) {
        return flags & ~(1 << bit);
    }

    public static int toggle(int flags, int bit) {
        return flags ^ (1 << bit);
    }

    public static boolean has(int flags, int bit) {
        return (flags & (1 << bit)) != 0;
    }

    public static int count(int flags) {
        return Integer.bitCount(flags);
    }
}
