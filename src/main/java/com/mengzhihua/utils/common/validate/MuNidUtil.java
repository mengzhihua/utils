package com.mengzhihua.utils.common.validate;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Locale;

/**
 * Mauritius national ID. Letter + DDMMYY + 6 digits + check (mod 17).
 * Sample {@code B150390123456A}.
 */
public final class MuNidUtil {

    static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private MuNidUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("[A-Z]\\d{12}[0-9A-Z]")) {
            return false;
        }
        try {
            birthDate(compact);
        } catch (RuntimeException ex) {
            return false;
        }
        return checkDigit(compact).equals(compact.substring(13));
    }

    public static String checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() >= 14) {
            compact = compact.substring(0, 13);
        }
        if (compact.length() != 13) {
            throw new IllegalArgumentException("Mauritius NID body must be 13 characters");
        }
        int sum = 0;
        for (int i = 0; i < 13; i++) {
            int index = ALPHABET.indexOf(compact.charAt(i));
            if (index < 0) {
                throw new IllegalArgumentException("Mauritius NID has an invalid character");
            }
            sum += (14 - i) * index;
        }
        return String.valueOf(ALPHABET.charAt(Math.floorMod(17 - sum, 17)));
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.length() >= 13) {
            compact = compact.substring(0, 13);
        }
        return compact + checkDigit(compact);
    }

    public static LocalDate birthDate(String value) {
        String compact = normalize(value);
        if (compact.length() < 7) {
            throw new IllegalArgumentException("Mauritius NID is too short");
        }
        int day = Integer.parseInt(compact.substring(1, 3));
        int month = Integer.parseInt(compact.substring(3, 5));
        int year = 2000 + Integer.parseInt(compact.substring(5, 7));
        try {
            return LocalDate.of(year, month, day);
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException("Mauritius NID has an invalid birth date", ex);
        }
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
