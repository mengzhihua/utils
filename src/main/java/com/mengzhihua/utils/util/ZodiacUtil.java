package com.mengzhihua.utils.util;

import java.time.LocalDate;
import java.time.MonthDay;

/**
 * Western constellation and Chinese zodiac.
 */
public final class ZodiacUtil {

    private static final String[] ANIMALS = {
            "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"
    };

    private ZodiacUtil() {
    }

    public static String chineseZodiac(int year) {
        int index = Math.floorMod(year - 4, 12);
        return ANIMALS[index];
    }

    public static String chineseZodiac(LocalDate date) {
        return date == null ? null : chineseZodiac(date.getYear());
    }

    public static String constellation(LocalDate date) {
        return date == null ? null : constellation(date.getMonthValue(), date.getDayOfMonth());
    }

    public static String constellation(int month, int day) {
        MonthDay md = MonthDay.of(month, day);
        if (!md.isBefore(MonthDay.of(3, 21)) && md.isBefore(MonthDay.of(4, 20))) {
            return "白羊座";
        }
        if (!md.isBefore(MonthDay.of(4, 20)) && md.isBefore(MonthDay.of(5, 21))) {
            return "金牛座";
        }
        if (!md.isBefore(MonthDay.of(5, 21)) && md.isBefore(MonthDay.of(6, 22))) {
            return "双子座";
        }
        if (!md.isBefore(MonthDay.of(6, 22)) && md.isBefore(MonthDay.of(7, 23))) {
            return "巨蟹座";
        }
        if (!md.isBefore(MonthDay.of(7, 23)) && md.isBefore(MonthDay.of(8, 23))) {
            return "狮子座";
        }
        if (!md.isBefore(MonthDay.of(8, 23)) && md.isBefore(MonthDay.of(9, 23))) {
            return "处女座";
        }
        if (!md.isBefore(MonthDay.of(9, 23)) && md.isBefore(MonthDay.of(10, 24))) {
            return "天秤座";
        }
        if (!md.isBefore(MonthDay.of(10, 24)) && md.isBefore(MonthDay.of(11, 23))) {
            return "天蝎座";
        }
        if (!md.isBefore(MonthDay.of(11, 23)) && md.isBefore(MonthDay.of(12, 22))) {
            return "射手座";
        }
        if (!md.isBefore(MonthDay.of(12, 22)) || md.isBefore(MonthDay.of(1, 20))) {
            return "摩羯座";
        }
        if (!md.isBefore(MonthDay.of(1, 20)) && md.isBefore(MonthDay.of(2, 19))) {
            return "水瓶座";
        }
        return "双鱼座";
    }
}
