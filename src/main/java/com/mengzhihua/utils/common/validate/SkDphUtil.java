package com.mengzhihua.utils.common.validate;


import java.math.BigInteger;
import java.util.Locale;
import java.util.Set;

/**
 * Slovak IČ DPH (VAT). 10 digits, {@code n % 11 == 0}, third digit in
 * {@code 2,3,4,7,8,9}. Sample {@code SK 202 274 96 19}. Does not treat RČ as VAT.
 */
public final class SkDphUtil {

    private static final Set<Character> THIRD = Set.of('2', '3', '4', '7', '8', '9');

    private SkDphUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("[1-9]\\d{9}") || !THIRD.contains(compact.charAt(2))) {
            return false;
        }
        return new BigInteger(compact).mod(BigInteger.valueOf(11)).intValue() == 0;
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() == 10) {
            return "SK " + compact.substring(0, 3) + ' ' + compact.substring(3, 6) + ' '
                    + compact.substring(6, 8) + ' ' + compact.substring(8);
        }
        return value == null ? "" : value.trim();
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("SK")) {
            compact = compact.substring(2);
        }
        return compact;
    }
}
