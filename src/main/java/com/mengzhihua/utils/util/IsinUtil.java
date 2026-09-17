package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * ISO 6166 ISIN checksum (Apache Commons Validator).
 */
public final class IsinUtil {

    private IsinUtil() {
    }

    public static boolean isValid(String isin) {
        String compact = normalize(isin);
        if (compact.length() != 12 || !compact.matches("[A-Z]{2}[A-Z0-9]{9}[0-9]")) {
            return false;
        }
        StringBuilder numeric = new StringBuilder();
        for (int i = 0; i < 11; i++) {
            char c = compact.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                numeric.append(c - 'A' + 10);
            } else {
                numeric.append(c);
            }
        }
        return CheckDigitUtil.luhn(numeric.toString() + compact.charAt(11));
    }

    public static String normalize(String isin) {
        return isin == null ? "" : isin.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
