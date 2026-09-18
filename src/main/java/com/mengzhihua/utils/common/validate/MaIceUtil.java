package com.mengzhihua.utils.common.validate;


import java.math.BigInteger;

/**
 * Moroccan ICE. 15 digits, Mod 97 remainder 0 (stdnum).
 * Sample {@code 001561191000066}.
 */
public final class MaIceUtil {

    private static final BigInteger NINETY_SEVEN = BigInteger.valueOf(97);

    private MaIceUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{15}") && mod97(digits) == 0;
    }

    public static String checkDigits(String body) {
        String digits = normalize(body);
        if (digits.length() >= 15) {
            digits = digits.substring(0, 13);
        }
        if (!digits.matches("\\d{13}")) {
            throw new IllegalArgumentException("ICE body must be 13 digits");
        }
        int rem = mod97(digits + "00");
        return String.format("%02d", (97 - rem) % 97);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (isValid(digits)) {
            return digits;
        }
        if (digits.length() >= 13) {
            digits = digits.substring(0, 13);
        }
        return digits + checkDigits(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static int mod97(String digits) {
        return new BigInteger(digits).mod(NINETY_SEVEN).intValue();
    }
}
