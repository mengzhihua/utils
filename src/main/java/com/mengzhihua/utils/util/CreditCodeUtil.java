package com.mengzhihua.utils.util;

/**
 * 统一社会信用代码（GB 32100-2015）校验与校验位计算。
 */
public final class CreditCodeUtil {

    private static final String CHARSET = "0123456789ABCDEFGHJKLMNPQRTUWXY";
    private static final int[] WEIGHTS = {1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28};

    private CreditCodeUtil() {
    }

    public static boolean isValid(String code) {
        String value = normalize(code);
        if (value.length() != 18) {
            return false;
        }
        for (int i = 0; i < 18; i++) {
            if (CHARSET.indexOf(value.charAt(i)) < 0) {
                return false;
            }
        }
        return value.charAt(17) == checkChar(value.substring(0, 17));
    }

    public static char checkChar(String body17) {
        String body = normalize(body17);
        if (body.length() != 17) {
            throw new IllegalArgumentException("credit code body must be 17 characters");
        }
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            int index = CHARSET.indexOf(body.charAt(i));
            if (index < 0) {
                throw new IllegalArgumentException("invalid credit code char: " + body.charAt(i));
            }
            sum += index * WEIGHTS[i];
        }
        int mod = 31 - (sum % 31);
        if (mod == 31) {
            mod = 0;
        }
        return CHARSET.charAt(mod);
    }

    public static String complete(String body17) {
        return normalize(body17) + checkChar(body17);
    }

    private static String normalize(String code) {
        return code == null ? "" : code.trim().toUpperCase();
    }
}
