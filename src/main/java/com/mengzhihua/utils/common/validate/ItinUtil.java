package com.mengzhihua.utils.common.validate;


/**
 * U.S. ITIN. Starts with {@code 9}, group 70–99 except 89/93.
 * Sample {@code 912-90-3456}.
 */
public final class ItinUtil {

    private ItinUtil() {
    }

    public static boolean isValid(String value) {
        if (value != null && value.indexOf('-') >= 0 && !value.trim().matches("\\d{3}-\\d{2}-\\d{4}")) {
            return false;
        }
        String digits = normalize(value);
        if (!digits.matches("9\\d{8}")) {
            return false;
        }
        int group = Integer.parseInt(digits.substring(3, 5));
        return group >= 70 && group <= 99 && group != 89 && group != 93;
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 9) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 3) + '-' + digits.substring(3, 5) + '-' + digits.substring(5);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }
}
