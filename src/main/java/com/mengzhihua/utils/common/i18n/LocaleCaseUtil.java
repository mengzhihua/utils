package com.mengzhihua.utils.common.i18n;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Locale-aware case conversion (Turkish {@code i/İ} vs root {@code i/I}).
 */
public final class LocaleCaseUtil {

    private LocaleCaseUtil() {
    }

    public static String upper(String locale, String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        return text.toUpperCase(LocaleUtil.parse(locale));
    }

    public static String lower(String locale, String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        return text.toLowerCase(LocaleUtil.parse(locale));
    }

    public static String title(String locale, String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        Locale tag = LocaleUtil.parse(locale);
        String[] parts = text.trim().split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            if (part.isEmpty()) {
                continue;
            }
            builder.append(part.substring(0, 1).toUpperCase(tag));
            if (part.length() > 1) {
                builder.append(part.substring(1).toLowerCase(tag));
            }
        }
        return builder.toString();
    }
}
