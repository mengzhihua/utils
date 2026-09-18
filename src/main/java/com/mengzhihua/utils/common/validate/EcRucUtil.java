package com.mengzhihua.utils.common.validate;


/**
 * Ecuador RUC (Registro Único de Contribuyentes). 13 digits.
 * Sample {@code 1792060346-001}.
 */
public final class EcRucUtil {

    private static final int[] PUBLIC = {3, 2, 7, 6, 5, 4, 3, 2, 1};
    private static final int[] JURIDICAL = {4, 3, 2, 7, 6, 5, 4, 3, 2, 1};

    private EcRucUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{13}") || !EcCiUtil.validProvince(digits.substring(0, 2))) {
            return false;
        }
        char type = digits.charAt(2);
        if (type < '6') {
            return natural(digits);
        }
        if (type == '6') {
            return publicRuc(digits) || natural(digits);
        }
        if (type == '9') {
            return publicRuc(digits) || juridical(digits);
        }
        return false;
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() == 10) {
            String candidate = digits + "001";
            if (isValid(candidate)) {
                return candidate;
            }
        }
        return digits;
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 13) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 10) + '-' + digits.substring(10);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static boolean natural(String digits) {
        return !digits.endsWith("000") && EcCiUtil.isValid(digits.substring(0, 10));
    }

    private static boolean publicRuc(String digits) {
        return !digits.endsWith("0000") && weighted(digits.substring(0, 9), PUBLIC) == 0;
    }

    private static boolean juridical(String digits) {
        return !digits.endsWith("000") && weighted(digits.substring(0, 10), JURIDICAL) == 0;
    }

    private static int weighted(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += weights[i] * (digits.charAt(i) - '0');
        }
        return sum % 11;
    }
}
