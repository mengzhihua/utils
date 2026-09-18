package com.mengzhihua.utils.common.i18n;


/**
 * Locale-aware ordinal numbers: {@code 1st}, {@code 第1}, {@code 1.}.
 */
public final class OrdinalUtil {

    private OrdinalUtil() {
    }

    public static String format(String locale, int value) {
        String language = LocaleUtil.parse(locale).getLanguage();
        return switch (language) {
            case "zh", "ja", "ko" -> "第" + value;
            case "de" -> value + ".";
            case "fr" -> value == 1 ? "1er" : value + "e";
            case "es" -> value + ".º";
            default -> value + englishSuffix(value);
        };
    }

    private static String englishSuffix(int value) {
        int mod100 = Math.abs(value) % 100;
        if (mod100 >= 11 && mod100 <= 13) {
            return "th";
        }
        return switch (Math.abs(value) % 10) {
            case 1 -> "st";
            case 2 -> "nd";
            case 3 -> "rd";
            default -> "th";
        };
    }
}
