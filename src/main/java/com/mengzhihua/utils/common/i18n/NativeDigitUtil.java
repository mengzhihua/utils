package com.mengzhihua.utils.common.i18n;


import java.text.DecimalFormatSymbols;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Convert digits using the locale numbering system ({@code ar-EG} → {@code ١٢٣٤}).
 */
public final class NativeDigitUtil {

    private NativeDigitUtil() {
    }

    public static char zeroDigit(String locale) {
        return DecimalFormatSymbols.getInstance(LocaleUtil.parse(locale)).getZeroDigit();
    }

    public static String toNative(String locale, String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        char zero = zeroDigit(locale);
        if (zero == '0') {
            return text;
        }
        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= '0' && ch <= '9') {
                builder.append((char) (zero + (ch - '0')));
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }

    public static String toLatin(String locale, String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        char zero = zeroDigit(locale);
        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch >= zero && ch <= zero + 9) {
                builder.append((char) ('0' + (ch - zero)));
            } else if (ch >= '\u0660' && ch <= '\u0669') {
                builder.append((char) ('0' + (ch - '\u0660')));
            } else if (ch >= '\u06F0' && ch <= '\u06F9') {
                builder.append((char) ('0' + (ch - '\u06F0')));
            } else {
                builder.append(ch);
            }
        }
        return builder.toString();
    }
}
