package com.mengzhihua.utils.common.validate;


/**
 * Ukrainian EDRPOU (ЄДРПОУ). Base weights {@code 1..7} when &lt; 30M or &gt; 60M,
 * else {@code 7,1,2,3,4,5,6}; rem ≥ 10 uses weights+2. Sample {@code 14360570}.
 */
public final class EdrpouUtil {

    private static final int[] BASE = {1, 2, 3, 4, 5, 6, 7};
    private static final int[] ALT = {7, 1, 2, 3, 4, 5, 6};

    private EdrpouUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("\\d{8}") && checkDigit(digits.substring(0, 7)) == digits.charAt(7);
    }

    public static char checkDigit(String body7) {
        String digits = normalize(body7);
        if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("EDRPOU body must be 7 digits");
        }
        int[] weights = primaryWeights(digits);
        int rem = remainder(digits, weights);
        if (rem >= 10) {
            rem = remainder(digits, shifted(weights)) % 10;
        }
        return (char) ('0' + rem);
    }

    public static String complete(String body7) {
        String digits = normalize(body7);
        if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        return digits + checkDigit(digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static int[] primaryWeights(String body7) {
        char first = body7.charAt(0);
        return first >= '3' && first <= '5' ? ALT : BASE;
    }

    private static int remainder(String digits, int[] weights) {
        int sum = 0;
        for (int i = 0; i < 7; i++) {
            sum += (digits.charAt(i) - '0') * weights[i];
        }
        return sum % 11;
    }

    private static int[] shifted(int[] weights) {
        int[] next = new int[weights.length];
        for (int i = 0; i < weights.length; i++) {
            next[i] = weights[i] + 2;
        }
        return next;
    }
}
