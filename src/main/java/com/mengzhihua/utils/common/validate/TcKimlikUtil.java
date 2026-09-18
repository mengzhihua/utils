package com.mengzhihua.utils.common.validate;


/**
 * Turkish T.C. Kimlik No (11 digits). {@code d10 = (odd*7 - even) % 10},
 * {@code d11 = sum(first 10) % 10}. Sample {@code 10000000146}.
 */
public final class TcKimlikUtil {

    private TcKimlikUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("[1-9]\\d{10}") && checkDigits(digits.substring(0, 9)).equals(digits.substring(9));
    }

    public static String checkDigits(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 11) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("[1-9]\\d{8}")) {
            throw new IllegalArgumentException("TCKN body must be 9 digits");
        }
        int odd = 0;
        int even = 0;
        for (int i = 0; i < 9; i++) {
            int n = digits.charAt(i) - '0';
            if ((i & 1) == 0) {
                odd += n;
            } else {
                even += n;
            }
        }
        int d10 = (odd * 7 - even) % 10;
        int d11 = 0;
        for (int i = 0; i < 9; i++) {
            d11 += digits.charAt(i) - '0';
        }
        d11 = (d11 + d10) % 10;
        return "" + d10 + d11;
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 11) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigits(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
