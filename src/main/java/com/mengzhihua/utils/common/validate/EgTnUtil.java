package com.mengzhihua.utils.common.validate;


/**
 * Egypt Tax Registration Number (الرقم الضريبي). 9 digits, Arabic-Indic accepted.
 * Sample {@code 100-531-385} / {@code ٣٣١-١٠٥-٢٦٨}.
 */
public final class EgTnUtil {

    private EgTnUtil() {
    }

    public static boolean isValid(String value) {
        return normalize(value).matches("\\d{9}");
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 9) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 3) + '-' + digits.substring(3, 6) + '-' + digits.substring(6);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char ch = value.charAt(i);
            if (ch >= '0' && ch <= '9') {
                builder.append(ch);
            } else if (ch >= '\u0660' && ch <= '\u0669') {
                builder.append((char) ('0' + (ch - '\u0660')));
            } else if (ch >= '\u06F0' && ch <= '\u06F9') {
                builder.append((char) ('0' + (ch - '\u06F0')));
            }
        }
        return builder.toString();
    }
}
