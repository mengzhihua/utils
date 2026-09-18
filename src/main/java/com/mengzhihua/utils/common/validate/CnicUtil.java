package com.mengzhihua.utils.common.validate;


import java.util.Map;

/**
 * Pakistani CNIC. 13 digits, last odd=M even=F (0 invalid), province 1–7.
 * Sample {@code 34201-0891231-8}.
 */
public final class CnicUtil {

    private static final Map<Character, String> PROVINCES = Map.of(
            '1', "Khyber Pakhtunkhwa",
            '2', "FATA",
            '3', "Punjab",
            '4', "Sindh",
            '5', "Balochistan",
            '6', "Islamabad",
            '7', "Gilgit-Baltistan");

    private CnicUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1-7]\\d{10}[1-9]") && gender(digits) != null && province(digits) != null;
    }

    public static String gender(String value) {
        String digits = normalize(value);
        if (digits.isEmpty()) {
            return null;
        }
        char last = digits.charAt(digits.length() - 1);
        if ("13579".indexOf(last) >= 0) {
            return "M";
        }
        if ("2468".indexOf(last) >= 0) {
            return "F";
        }
        return null;
    }

    public static String province(String value) {
        String digits = normalize(value);
        return digits.isEmpty() ? null : PROVINCES.get(digits.charAt(0));
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 13) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 5) + '-' + digits.substring(5, 12) + '-' + digits.substring(12);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
