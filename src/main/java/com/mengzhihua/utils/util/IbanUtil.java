package com.mengzhihua.utils.util;

import java.math.BigInteger;
import java.util.Locale;

/**
 * IBAN MOD-97 check (Apache Commons Validator style).
 */
public final class IbanUtil {

    private IbanUtil() {
    }

    public static boolean isValid(String iban) {
        String compact = normalize(iban);
        if (compact.length() < 15 || compact.length() > 34) {
            return false;
        }
        if (!compact.matches("[A-Z]{2}\\d{2}[A-Z0-9]+")) {
            return false;
        }
        String rearranged = compact.substring(4) + compact.substring(0, 4);
        StringBuilder numeric = new StringBuilder();
        for (int i = 0; i < rearranged.length(); i++) {
            char c = rearranged.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                numeric.append(c - 'A' + 10);
            } else {
                numeric.append(c);
            }
        }
        return new BigInteger(numeric.toString()).mod(BigInteger.valueOf(97)).intValue() == 1;
    }

    public static String normalize(String iban) {
        return iban == null ? "" : iban.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
