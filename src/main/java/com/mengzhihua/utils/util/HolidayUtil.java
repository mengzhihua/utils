package com.mengzhihua.utils.util;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * 中国法定节假日（固定公历 + 农历春节/端午/中秋，Hutool 日历风格）。
 */
public final class HolidayUtil {

    private HolidayUtil() {
    }

    public static boolean isHoliday(LocalDate date) {
        return name(date) != null;
    }

    public static String name(LocalDate date) {
        if (date == null) {
            return null;
        }
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();
        if (month == 1 && day == 1) {
            return "元旦";
        }
        if (month == 5 && day == 1) {
            return "劳动节";
        }
        if (month == 10 && day >= 1 && day <= 3) {
            return "国庆节";
        }
        if (month == 4 && day == qingmingDay(date.getYear())) {
            return "清明节";
        }
        if (date.getYear() >= 1900 && date.getYear() <= 2099) {
            LunarUtil.Lunar next = LunarUtil.of(date.plusDays(1));
            if (!next.leap() && next.month() == 1 && next.day() == 1) {
                return "除夕";
            }
            LunarUtil.Lunar lunar = LunarUtil.of(date);
            if (!lunar.leap() && lunar.month() == 1 && lunar.day() <= 3) {
                return "春节";
            }
            if (!lunar.leap() && lunar.month() == 5 && lunar.day() == 5) {
                return "端午节";
            }
            if (!lunar.leap() && lunar.month() == 8 && lunar.day() == 15) {
                return "中秋节";
            }
        }
        return null;
    }

    public static List<String> yearHolidays(int year) {
        List<String> names = new ArrayList<>();
        LocalDate cursor = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        while (!cursor.isAfter(end)) {
            String holiday = name(cursor);
            if (holiday != null) {
                names.add(cursor + " " + holiday);
            }
            cursor = cursor.plusDays(1);
        }
        return names;
    }

    static int qingmingDay(int year) {
        int y = year % 100;
        return (int) (y * 0.2422 + 4.81) - y / 4;
    }
}
