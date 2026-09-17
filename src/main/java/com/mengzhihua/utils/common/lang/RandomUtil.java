package com.mengzhihua.utils.common.lang;


import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
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

    public static <T> T randomEle(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[nextInt(0, array.length)];
    }

    public static <T> T randomEle(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(nextInt(0, list.size()));
    }

    public static <T> List<T> randomEles(Collection<T> collection, int count) {
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<>();
        }
        if (count < 0 || count > collection.size()) {
            throw new IllegalArgumentException("count must be between 0 and collection size");
        }
        List<T> copy = new ArrayList<>(collection);
        Collections.shuffle(copy, ThreadLocalRandom.current());
        return new ArrayList<>(copy.subList(0, count));
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
