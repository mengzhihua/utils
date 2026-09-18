package com.mengzhihua.utils.common.i18n;


import java.time.LocalDate;
import java.time.chrono.JapaneseChronology;
import java.time.chrono.JapaneseDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Locale chronology helpers (ISO / Japanese imperial calendar).
 */
public final class ChronologyUtil {

    private ChronologyUtil() {
    }

    public static String iso(String locale, String isoDate) {
        Locale tag = LocaleUtil.parse(locale);
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(tag).format(date(isoDate));
    }

    public static String japanese(String isoDate) {
        JapaneseDate japanese = JapaneseDate.from(date(isoDate));
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
                .withLocale(Locale.JAPAN)
                .withChronology(JapaneseChronology.INSTANCE)
                .format(japanese);
    }

    public static String era(String isoDate) {
        return JapaneseDate.from(date(isoDate)).getEra().toString();
    }

    private static LocalDate date(String isoDate) {
        return StringUtil.isBlank(isoDate) ? LocalDate.of(2026, 9, 18) : LocalDate.parse(isoDate.trim());
    }
}
