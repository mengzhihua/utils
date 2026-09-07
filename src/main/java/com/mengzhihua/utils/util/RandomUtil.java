package com.mengzhihua.utils.util;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Random numbers and strings.
 */
public final class RandomUtil {

    private static final String DIGITS = "0123456789";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String ALPHANUMERIC = DIGITS + LETTERS;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private RandomUtil() {
    }

    public static int nextInt(int minInclusive, int maxExclusive) {
        if (minInclusive >= maxExclusive) {
            throw new IllegalArgumentException("min must be less than max");
        }
        return ThreadLocalRandom.current().nextInt(minInclusive, maxExclusive);
    }

    public static long nextLong(long minInclusive, long maxExclusive) {
        if (minInclusive >= maxExclusive) {
            throw new IllegalArgumentException("min must be less than max");
        }
        return ThreadLocalRandom.current().nextLong(minInclusive, maxExclusive);
    }

    public static String digits(int length) {
        return random(DIGITS, length, false);
    }

    public static String letters(int length) {
        return random(LETTERS, length, false);
    }

    public static String alphanumeric(int length) {
        return random(ALPHANUMERIC, length, false);
    }

    public static String secureAlphanumeric(int length) {
        return random(ALPHANUMERIC, length, true);
    }

    private static String random(String alphabet, int length, boolean secure) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            int index = secure ? SECURE_RANDOM.nextInt(alphabet.length())
                    : ThreadLocalRandom.current().nextInt(alphabet.length());
            chars[i] = alphabet.charAt(index);
        }
        return new String(chars);
    }
}
