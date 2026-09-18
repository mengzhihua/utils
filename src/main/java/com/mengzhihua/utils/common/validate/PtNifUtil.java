package com.mengzhihua.utils.common.validate;


/**
 * Portuguese NIF (Número de Identificação Fiscal). Weights {@code 9..2},
 * check {@code 0} if {@code sum % 11 < 2} else {@code 11 - (sum % 11)}.
 * Sample {@code 123456789}. Distinct from Spanish {@link NifUtil}.
 */
public final class PtNifUtil {

    private static final int[] WEIGHTS = {9, 8, 7, 6, 5, 4, 3, 2};

    private PtNifUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("[1-9]\\d{8}")) {
            return false;
        }
        return checkDigit(digits.substring(0, 8)) == digits.charAt(8);
    }

    public static char checkDigit(String body8) {
        String digits = normalize(body8);
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("Portuguese NIF body must be 8 digits");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        int rem = sum % 11;
        return (char) ('0' + (rem < 2 ? 0 : 11 - rem));
    }

    public static String complete(String body8) {
        String digits = normalize(body8);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
