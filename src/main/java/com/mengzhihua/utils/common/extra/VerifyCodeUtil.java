package com.mengzhihua.utils.common.extra;


import java.security.SecureRandom;

/**
 * Numeric / alphanumeric verification codes. Compare with {@link #matches} (constant-time).
 */
public final class VerifyCodeUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final char[] DIGITS = "0123456789".toCharArray();
    private static final char[] LETTERS = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789".toCharArray();

    private VerifyCodeUtil() {
    }

    public static String numeric(int length) {
        return random(length, DIGITS);
    }

    public static String alphanumeric(int length) {
        return random(length, LETTERS);
    }

    public static boolean matches(String expected, String actual) {
        if (expected == null || actual == null) {
            return false;
        }
        String left = expected.trim();
        String right = actual.trim();
        if (left.length() != right.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < left.length(); i++) {
            result |= Character.toUpperCase(left.charAt(i)) ^ Character.toUpperCase(right.charAt(i));
        }
        return result == 0;
    }

    private static String random(int length, char[] alphabet) {
        int size = Math.min(12, Math.max(4, length));
        char[] chars = new char[size];
        for (int i = 0; i < size; i++) {
            chars[i] = alphabet[RANDOM.nextInt(alphabet.length)];
        }
        return new String(chars);
    }
}
