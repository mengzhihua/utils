package com.mengzhihua.utils.common.validate;


/**
 * ИНН (Russian tax identifier). 10-digit legal or 12-digit personal.
 * Samples {@code 1234567894}, {@code 7707083893}, {@code 123456789047}.
 */
public final class InnUtil {

    private static final int[] COMPANY = {2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] PERSONAL_1 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8};
    private static final int[] PERSONAL_2 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8};

    private InnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (digits.matches("\\d{10}")) {
            return companyCheck(digits.substring(0, 9)) == digits.charAt(9);
        }
        if (digits.matches("\\d{12}")) {
            return personalChecks(digits.substring(0, 10)).equals(digits.substring(10));
        }
        return false;
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (digits.length() >= 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("INN body must be 9 digits");
        }
        return companyCheck(digits);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 10) {
            return digits.substring(0, 10) + personalChecks(digits.substring(0, 10));
        }
        if (digits.length() >= 9) {
            digits = digits.substring(0, 9);
        }
        return digits + companyCheck(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static char companyCheck(String body9) {
        return weightedMod11(body9, COMPANY);
    }

    private static String personalChecks(String body10) {
        char first = weightedMod11(body10, PERSONAL_1);
        return "" + first + weightedMod11(body10 + first, PERSONAL_2);
    }

    private static char weightedMod11(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        return (char) ('0' + (sum % 11 % 10));
    }
}
