package com.mengzhihua.utils.common.validate;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Set;

/**
 * Indonesian NIK / KTP (16 digits {@code PPRRSSDDMMYYXXXX}).
 * Female day is {@code +40}. Sample {@code 3171011708450001}.
 */
public final class NikUtil {

    private static final Set<String> PROVINCES = Set.of(
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "21",
            "31", "32", "33", "34", "35", "36",
            "51", "52", "53",
            "61", "62", "63", "64", "65",
            "71", "72", "73", "74", "75", "76",
            "81", "82",
            "91", "92", "93", "94", "95", "96"
    );

    private NikUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{16}")
                && PROVINCES.contains(digits.substring(0, 2))
                && birthDate(digits) != null;
    }

    public static LocalDate birthDate(String value) {
        String digits = normalize(value);
        if (digits.length() < 12) {
            return null;
        }
        int day = Integer.parseInt(digits.substring(6, 8)) % 40;
        int month = Integer.parseInt(digits.substring(8, 10));
        int year = Integer.parseInt(digits.substring(10, 12));
        try {
            return LocalDate.of(year + 1900, month, day);
        } catch (DateTimeException ignored) {
            // 1900-02-29 does not exist; try 2000
        }
        try {
            return LocalDate.of(year + 2000, month, day);
        } catch (DateTimeException ignored) {
            return null;
        }
    }

    public static boolean female(String value) {
        String digits = normalize(value);
        return digits.length() >= 8 && Integer.parseInt(digits.substring(6, 8)) > 40;
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
