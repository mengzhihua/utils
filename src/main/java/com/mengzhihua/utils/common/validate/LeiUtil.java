package com.mengzhihua.utils.common.validate;


import java.math.BigInteger;
import java.util.Locale;

/**
 * ISO 17442 Legal Entity Identifier (ISO 7064 MOD 97-10). Sample {@code 5493001KJTIIGC8Y1R12}.
 */
public final class LeiUtil {

    private LeiUtil() {
    }

    public static boolean isValid(String lei) {
        String compact = normalize(lei);
        if (!compact.matches("[A-Z0-9]{18}\\d{2}")) {
            return false;
        }
        return mod97(compact) == 1;
    }

    public static String checkDigits(String body18) {
        String body = normalize(body18);
        if (!body.matches("[A-Z0-9]{18}")) {
            throw new IllegalArgumentException("LEI body must be 18 alphanumeric characters");
        }
        int check = 98 - mod97(body + "00");
        return String.format(Locale.ROOT, "%02d", check);
    }

    public static String complete(String body18) {
        String body = normalize(body18);
        return body + checkDigits(body);
    }

    public static String normalize(String lei) {
        return lei == null ? "" : lei.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
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
