package com.mengzhihua.utils.common.id;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * 邀请码 / 短码：正整数与 Base62 互转。
 */
public final class ShortCodeUtil {

    private static final char[] ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final int BASE = ALPHABET.length;

    private ShortCodeUtil() {
    }

    public static String encode(long id) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be >= 0");
        }
        if (id == 0) {
            return "0";
        }
        StringBuilder builder = new StringBuilder();
        long value = id;
        while (value > 0) {
            builder.append(ALPHABET[(int) (value % BASE)]);
            value /= BASE;
        }
        return builder.reverse().toString();
    }

    public static long decode(String code) {
        if (StringUtil.isBlank(code)) {
            throw new IllegalArgumentException("code is blank");
        }
        long value = 0;
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            int index = indexOf(c);
            if (index < 0) {
                throw new IllegalArgumentException("invalid short code char: " + c);
            }
            value = value * BASE + index;
        }
        return value;
    }

    private static int indexOf(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'z') {
            return 10 + (c - 'a');
        }
        if (c >= 'A' && c <= 'Z') {
            return 36 + (c - 'A');
        }
        return -1;
    }
}
