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

    public static String toIsbn13(String isbn) {
        String compact = normalize(isbn);
        if (compact.length() == 13 && isIsbn13(compact)) {
            return compact;
        }
        if (compact.length() != 10 || !isIsbn10(compact)) {
            throw new IllegalArgumentException("invalid isbn-10: " + isbn);
        }
        String body = "978" + compact.substring(0, 9);
        return body + isbn13CheckDigit(body);
    }

    public static String toIsbn10(String isbn) {
        String compact = normalize(isbn);
        if (compact.length() == 10 && isIsbn10(compact)) {
            return compact;
        }
        if (compact.length() != 13 || !isIsbn13(compact) || !compact.startsWith("978")) {
            throw new IllegalArgumentException("isbn-13 must start with 978: " + isbn);
        }
        String body = compact.substring(3, 12);
        return body + isbn10CheckDigit(body);
    }

    private static char isbn13CheckDigit(String body12) {
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            sum += Character.digit(body12.charAt(i), 10) * (i % 2 == 0 ? 1 : 3);
        }
        return (char) ('0' + (10 - (sum % 10)) % 10);
    }

    private static char isbn10CheckDigit(String body9) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += Character.digit(body9.charAt(i), 10) * (10 - i);
        }
        int check = (11 - (sum % 11)) % 11;
        return check == 10 ? 'X' : (char) ('0' + check);
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
