package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Set;

/**
 * Canadian Business Number. 9-digit Luhn, optional BN15 program account.
 * Sample {@code 12302 6635 RC 0001}.
 */
public final class CaBnUtil {

    private static final Set<String> PROGRAMS = Set.of("RC", "RM", "RP", "RT");

    private CaBnUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.length() != 9 && compact.length() != 15) {
            return false;
        }
        if (!compact.substring(0, 9).matches("\\d{9}") || !CheckDigitUtil.luhn(compact.substring(0, 9))) {
            return false;
        }
        if (compact.length() == 15) {
            return PROGRAMS.contains(compact.substring(9, 11)) && compact.substring(11).matches("\\d{4}");
        }
        return true;
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        String digits = compact.replaceAll("\\D", "");
        if (digits.length() >= 8) {
            digits = digits.substring(0, 8);
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("Canadian BN body must be 8 digits");
        }
        return digits + CheckDigitUtil.luhnCheckDigit(digits);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() == 9) {
            return compact.substring(0, 5) + ' ' + compact.substring(5);
        }
        if (compact.length() == 15) {
            return compact.substring(0, 5) + ' ' + compact.substring(5, 9) + ' '
                    + compact.substring(9, 11) + ' ' + compact.substring(11);
        }
        return value == null ? "" : value.trim();
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
