package com.mengzhihua.utils.common.validate;


/**
 * Korean business registration number (사업자등록번호). Weights
 * {@code 1,3,7,1,3,7,1,3,5} plus {@code floor(d8*5/10)}. Sample {@code 120-81-47521}.
 */
public final class KrBrnUtil {

    private static final int[] WEIGHTS = {1, 3, 7, 1, 3, 7, 1, 3, 5};

    private KrBrnUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{10}") && checkDigit(digits.substring(0, 9)) == digits.charAt(9);
    }

    public static char checkDigit(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("BRN body must be 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (digits.charAt(i) - '0') * WEIGHTS[i];
        }
        sum += (digits.charAt(8) - '0') * 5 / 10;
        return (char) ('0' + ((10 - (sum % 10)) % 10));
    }

    public static String complete(String body9) {
        String digits = normalize(body9);
        if (digits.length() == 10) {
            digits = digits.substring(0, 9);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
