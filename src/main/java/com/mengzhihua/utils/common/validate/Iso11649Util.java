package com.mengzhihua.utils.common.validate;


import java.math.BigInteger;
import java.util.Locale;

/**
 * ISO 11649 structured creditor reference. {@code RF} + 2 check digits + up to 21 chars.
 * Sample {@code RF18 5390 0754 7034}.
 */
public final class Iso11649Util {

    private Iso11649Util() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.length() < 5 || compact.length() > 25 || !compact.startsWith("RF")) {
            return false;
        }
        if (!compact.matches("RF\\d{2}[A-Z0-9]+")) {
            return false;
        }
        return mod97(compact.substring(4) + compact.substring(0, 4)) == 1;
    }

    public static String checkDigits(String body) {
        String compact = normalize(body);
        if (compact.startsWith("RF") && compact.length() >= 4) {
            compact = compact.substring(4);
        }
        if (compact.isEmpty() || compact.length() > 21 || !compact.matches("[A-Z0-9]+")) {
            throw new IllegalArgumentException("ISO 11649 body must be 1–21 alphanumeric characters");
        }
        int check = 98 - mod97(compact + "RF00");
        return String.format(Locale.ROOT, "%02d", check);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.startsWith("RF") && compact.length() >= 4) {
            compact = compact.substring(4);
        }
        return "RF" + checkDigits(compact) + compact;
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() < 5) {
            return value == null ? "" : value.trim();
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < compact.length(); i += 4) {
            if (!out.isEmpty()) {
                out.append(' ');
            }
            out.append(compact, i, Math.min(i + 4, compact.length()));
        }
        return out.toString();
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s.,/:\\-]", "").toUpperCase(Locale.ROOT);
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
