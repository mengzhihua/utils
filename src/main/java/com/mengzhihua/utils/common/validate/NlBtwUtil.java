package com.mengzhihua.utils.common.validate;


import java.math.BigInteger;
import java.util.Locale;

/**
 * Dutch btw-identificatienummer. 9 digits + {@code B} + 2 digits.
 * Valid if the first 9 pass RSIN/elfproef (leading zeros allowed), or
 * {@code NL} + number passes ISO 7064 MOD 97-10.
 * Sample {@code NL004495445B01}.
 */
public final class NlBtwUtil {

    private NlBtwUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("\\d{9}B\\d{2}")) {
            return false;
        }
        if (Long.parseLong(compact.substring(0, 9)) <= 0 || Integer.parseInt(compact.substring(10)) <= 0) {
            return false;
        }
        return elfproef(compact.substring(0, 9)) || mod97("NL" + compact) == 1;
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() == 12) {
            return "NL" + compact;
        }
        return value == null ? "" : value.trim();
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
        if (compact.startsWith("NL")) {
            compact = compact.substring(2);
        }
        int marker = compact.lastIndexOf('B');
        if (marker > 0 && compact.length() - marker == 3 && compact.substring(0, marker).matches("\\d+")) {
            String body = compact.substring(0, marker);
            if (body.length() < 9) {
                body = "0".repeat(9 - body.length()) + body;
            }
            compact = body + compact.substring(marker);
        }
        return compact;
    }

    /** Same 11-proef as BSN/RSIN, but leading zeros (except all-zero) are allowed. */
    private static boolean elfproef(String digits) {
        if (!digits.matches("\\d{9}")) {
            return false;
        }
        int[] weights = {9, 8, 7, 6, 5, 4, 3, 2, -1};
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        return sum != 0 && sum % 11 == 0;
    }

    private static int mod97(String compact) {
        StringBuilder numeric = new StringBuilder();
        for (int i = 0; i < compact.length(); i++) {
            char c = compact.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                numeric.append(c - 'A' + 10);
            } else {
                numeric.append(c);
            }
        }
        return new BigInteger(numeric.toString()).mod(BigInteger.valueOf(97)).intValue();
    }
}
