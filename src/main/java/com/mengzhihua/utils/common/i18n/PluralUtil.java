package com.mengzhihua.utils.common.i18n;


import java.util.Locale;

/**
 * Locale-aware plural / choice formatting via resource bundles.
 */
public final class PluralUtil {

    private PluralUtil() {
    }

    public static String items(String locale, int count) {
        return I18nUtil.get("plural.items", LocaleUtil.parse(locale), count);
    }

    public static String category(int count) {
        if (count == 0) {
            return "zero";
        }
        if (count == 1) {
            return "one";
        }
        return "other";
    }

    public static String format(String locale, String key, int count) {
        Locale tag = LocaleUtil.parse(locale);
        String resolved = String.valueOf(key == null || key.isBlank() ? "plural.items" : key);
        return I18nUtil.get(resolved, tag, count);
    }
}
