package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * ISBN-10 / ISBN-13 checksum.
 */
public final class IsbnUtil {

    private IsbnUtil() {
    }

    public static boolean isValid(String isbn) {
        String compact = normalize(isbn);
        if (compact.length() == 10) {
            return isIsbn10(compact);
        }
        if (compact.length() == 13) {
            return isIsbn13(compact);
        }
        return false;
    }

    public static String normalize(String isbn) {
        if (isbn == null) {
            return "";
        }
        return isbn.replaceAll("[^0-9Xx]", "").toUpperCase(Locale.ROOT);
    }

    private static boolean isIsbn10(String isbn) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.digit(isbn.charAt(i), 10);
            if (digit < 0) {
                return false;
            }
            sum += digit * (10 - i);
        }
        char last = isbn.charAt(9);
        int check = last == 'X' ? 10 : Character.digit(last, 10);
        if (check < 0) {
            return false;
        }
        sum += check;
        return sum % 11 == 0;
    }

    private static boolean isIsbn13(String isbn) {
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int digit = Character.digit(isbn.charAt(i), 10);
            if (digit < 0) {
                return false;
            }
            sum += digit * (i % 2 == 0 ? 1 : 3);
        }
        return sum % 10 == 0;
    }
}
