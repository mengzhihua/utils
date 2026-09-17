package com.mengzhihua.utils.common.time;


import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * ISO week helpers and Chinese weekday names.
 */
public final class WeekUtil {

    private static final String[] CN = {"一", "二", "三", "四", "五", "六", "日"};
    private static final WeekFields ISO = WeekFields.ISO;

    private WeekUtil() {
    }

    public static int isoWeek(LocalDate date) {
        return date == null ? 0 : date.get(ISO.weekOfWeekBasedYear());
    }

    public static int isoWeekYear(LocalDate date) {
        return date == null ? 0 : date.get(ISO.weekBasedYear());
    }

    public static LocalDate startOfIsoWeek(LocalDate date) {
        return date == null ? null : date.with(DayOfWeek.MONDAY);
    }

    public static LocalDate endOfIsoWeek(LocalDate date) {
        return date == null ? null : date.with(DayOfWeek.SUNDAY);
    }

    public static int weekOfMonth(LocalDate date) {
        return date == null ? 0 : date.get(WeekFields.of(Locale.CHINA).weekOfMonth());
    }

    public static String chineseDayOfWeek(LocalDate date) {
        if (date == null) {
            return "";
        }
        return "星期" + CN[date.getDayOfWeek().getValue() - 1];
    }

    public static boolean isWeekend(LocalDate date) {
        return date != null && (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY);
    }

    public static String display(LocalDate date) {
        if (date == null) {
            return "";
        }
        return DateTimeUtil.format(date) + " " + chineseDayOfWeek(date)
                + " 第" + isoWeek(date) + "周";
    }
}
