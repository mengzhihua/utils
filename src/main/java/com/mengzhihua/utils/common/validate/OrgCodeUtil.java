package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * 组织机构代码 GB 11714（9 位，末位校验，10→X、11→0）。
 */
public final class OrgCodeUtil {

    private static final String CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final int[] WEIGHTS = {3, 7, 9, 10, 5, 8, 4, 2};

    private OrgCodeUtil() {
    }

    public static boolean isValid(String code) {
        String compact = normalize(code);
        if (compact.length() != 9) {
            return false;
        }
        for (int i = 0; i < 9; i++) {
            if (CHARSET.indexOf(compact.charAt(i)) < 0) {
                return false;
            }
        }
        return compact.charAt(8) == checkChar(compact.substring(0, 8));
    }

    public static char checkChar(String body8) {
        String body = normalize(body8);
        if (body.length() != 8) {
            throw new IllegalArgumentException("org code body must be 8 characters");
        }
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            int index = CHARSET.indexOf(body.charAt(i));
            if (index < 0) {
                throw new IllegalArgumentException("invalid org code char: " + body.charAt(i));
            }
            sum += index * WEIGHTS[i];
        }
        int c9 = 11 - (sum % 11);
        if (c9 == 11) {
            return '0';
        }
        if (c9 == 10) {
            return 'X';
        }
        return (char) ('0' + c9);
    }

    public static String complete(String body8) {
        String body = normalize(body8);
        return body + checkChar(body);
    }

    public static String normalize(String code) {
        return code == null ? "" : code.trim().toUpperCase(Locale.ROOT).replace("-", "").replace(" ", "");
    }
}
