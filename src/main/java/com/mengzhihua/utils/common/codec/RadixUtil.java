package com.mengzhihua.utils.common.codec;


import java.math.BigInteger;

import com.mengzhihua.utils.common.lang.AssertUtil;

/**
 * Convert integers between radix 2 and 62 (Hutool {@code RadixUtil} style).
 */
public final class RadixUtil {

    private static final char[] DIGITS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private RadixUtil() {
    }

    public static String toString(long value, int radix) {
        checkRadix(radix);
        if (value == 0) {
            return "0";
        }
        if (value < 0) {
            if (value == Long.MIN_VALUE) {
                throw new IllegalArgumentException("value out of range");
            }
            return "-" + toString(-value, radix);
        }
        StringBuilder builder = new StringBuilder();
        long n = value;
        while (n > 0) {
            builder.append(DIGITS[(int) (n % radix)]);
            n /= radix;
        }
        return builder.reverse().toString();
    }

    public static long parse(String text, int radix) {
        checkRadix(radix);
        AssertUtil.notBlank(text, "text must not be blank");
        String value = text.trim();
        boolean negative = value.startsWith("-");
        if (negative) {
            value = value.substring(1);
        }
        long result = 0L;
        for (int i = 0; i < value.length(); i++) {
            int digit = digit(value.charAt(i));
            if (digit < 0 || digit >= radix) {
                throw new IllegalArgumentException("invalid digit in radix " + radix + ": " + text);
            }
            result = Math.multiplyExact(result, radix);
            result = Math.addExact(result, digit);
        }
        return negative ? -result : result;
    }

    public static String convert(String text, int from, int to) {
        return toString(parse(text, from), to);
    }

    public static String encode(byte[] bytes, int radix) {
        if (bytes == null || bytes.length == 0) {
            return "0";
        }
        return new BigInteger(1, bytes).toString(Math.min(36, Math.max(2, radix)));
    }

    private static int digit(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'z') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 36;
        }
        return -1;
    }

    private static void checkRadix(int radix) {
        if (radix < 2 || radix > 62) {
            throw new IllegalArgumentException("radix must be in [2, 62]");
        }
    }
}
