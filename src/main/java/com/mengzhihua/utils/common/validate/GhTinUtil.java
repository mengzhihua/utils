package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Ghana TIN. {@code [PCGQV]00} plus 8 alnum, check {@code sum((i+1)*n) % 11}.
 * Sample {@code C0000803561}.
 */
public final class GhTinUtil {

    private GhTinUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        return compact.matches("[PCGQV]00[A-Z0-9]{8}")
                && compact.charAt(10) == checkDigit(compact).charAt(0);
    }

    public static String checkDigit(String body) {
        String compact = normalize(body);
        if (compact.length() >= 11) {
            compact = compact.substring(0, 10);
        }
        if (compact.length() != 10 || !compact.substring(1).matches("\\d{9}")) {
            throw new IllegalArgumentException("Ghana TIN body must be prefix plus 9 digits");
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (i + 1) * (compact.charAt(i + 1) - '0');
        }
        int rem = sum % 11;
        return rem == 10 ? "X" : String.valueOf(rem);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        if (compact.length() >= 10) {
            compact = compact.substring(0, 10);
        }
        return compact + checkDigit(compact);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
