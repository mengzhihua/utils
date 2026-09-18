package com.mengzhihua.utils.common.i18n;


import java.time.DayOfWeek;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * First day of week and minimal days in first week for a locale.
 */
public final class CalendarLocaleUtil {

    private CalendarLocaleUtil() {
    }

    public static DayOfWeek firstDayOfWeek(String locale) {
        return WeekFields.of(LocaleUtil.parse(locale)).getFirstDayOfWeek();
    }

    public static String firstDay(String locale) {
        return firstDayOfWeek(locale).name();
    }

    public static int minimalDaysInFirstWeek(String locale) {
        return WeekFields.of(LocaleUtil.parse(locale)).getMinimalDaysInFirstWeek();
    }
}
