package com.mengzhihua.utils.common.time;


import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Solar ↔ Chinese lunar date for 1900-01-31 .. 2100-12-31 (classic lunarInfo table).
 */
public final class LunarUtil {

    private static final String[] GAN = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private static final String[] ZHI = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    private static final String[] ANIMALS = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};
    private static final String[] MONTHS = {"正", "二", "三", "四", "五", "六", "七", "八", "九", "十", "冬", "腊"};
    private static final String[] DAYS = {
            "初一", "初二", "初三", "初四", "初五", "初六", "初七", "初八", "初九", "初十",
            "十一", "十二", "十三", "十四", "十五", "十六", "十七", "十八", "十九", "二十",
            "廿一", "廿二", "廿三", "廿四", "廿五", "廿六", "廿七", "廿八", "廿九", "三十"
    };
    private static final LocalDate BASE = LocalDate.of(1900, 1, 31);
    private static final int[] LUNAR_INFO = {
            0x04bd8, 0x04ae0, 0x0a570, 0x054d5, 0x0d260, 0x0d950, 0x16554, 0x056a0, 0x09ad0, 0x055d2,
            0x04ae0, 0x0a5b6, 0x0a4d0, 0x0d250, 0x1d255, 0x0b540, 0x0d6a0, 0x0ada2, 0x095b0, 0x14977,
            0x04970, 0x0a4b0, 0x0b4b5, 0x06a50, 0x06d40, 0x1ab54, 0x02b60, 0x09570, 0x052f2, 0x04970,
            0x06566, 0x0d4a0, 0x0ea50, 0x06e95, 0x05ad0, 0x02b60, 0x186e3, 0x092e0, 0x1c8d7, 0x0c950,
            0x0d4a0, 0x1d8a6, 0x0b550, 0x056a0, 0x1a5b4, 0x025d0, 0x092d0, 0x0d2b2, 0x0a950, 0x0b557,
            0x06ca0, 0x0b550, 0x15355, 0x04da0, 0x0a5d0, 0x14573, 0x052d0, 0x0a9a8, 0x0e950, 0x06aa0,
            0x0aea6, 0x0ab50, 0x04b60, 0x0aae4, 0x0a570, 0x05260, 0x0f263, 0x0d950, 0x05b57, 0x056a0,
            0x096d0, 0x04dd5, 0x04ad0, 0x0a4d0, 0x0d4d4, 0x0d250, 0x0d558, 0x0b540, 0x0b5a0, 0x195a6,
            0x095b0, 0x049b0, 0x0a974, 0x0a4b0, 0x0b27a, 0x06a50, 0x06d40, 0x0af46, 0x0ab60, 0x09570,
            0x04af5, 0x04970, 0x064b0, 0x074a3, 0x0ea50, 0x06b58, 0x055c0, 0x0ab60, 0x096d5, 0x092e0,
            0x0c960, 0x0d954, 0x0d4a0, 0x0da50, 0x07552, 0x056a0, 0x0abb7, 0x025d0, 0x092d0, 0x0cab5,
            0x0a950, 0x0b4a0, 0x0baa4, 0x0ad50, 0x055d9, 0x04ba0, 0x0a5b0, 0x15176, 0x052b0, 0x0a930,
            0x07954, 0x06aa0, 0x0ad50, 0x05b52, 0x04b60, 0x0a6e6, 0x0a4e0, 0x0d260, 0x0ea65, 0x0d530,
            0x05aa0, 0x076a3, 0x096d0, 0x04bd7, 0x04ad0, 0x0a4d0, 0x1d0b6, 0x0d250, 0x0d520, 0x0dd45,
            0x0b5a0, 0x056d0, 0x055b2, 0x049b0, 0x0a577, 0x0a4b0, 0x0aa50, 0x1b255,             0x06d20, 0x0ada0
    };

    private LunarUtil() {
    }

    public static Lunar of(LocalDate solar) {
        if (solar == null) {
            throw new IllegalArgumentException("date is null");
        }
        if (solar.isBefore(BASE) || solar.getYear() > 2099) {
            throw new IllegalArgumentException("lunar table covers 1900-01-31 to 2099-12-31: " + solar);
        }
        int offset = (int) ChronoUnit.DAYS.between(BASE, solar);
        int lunarYear = 1900;
        while (lunarYear < 2100) {
            int days = yearDays(lunarYear);
            if (offset < days) {
                break;
            }
            offset -= days;
            lunarYear++;
        }
        if (lunarYear >= 2100) {
            throw new IllegalArgumentException("date out of lunar table: " + solar);
        }
        int leap = leapMonth(lunarYear);
        boolean isLeap = false;
        int lunarMonth = 1;
        for (; lunarMonth < 13 && offset > 0; lunarMonth++) {
            int days;
            if (leap > 0 && lunarMonth == leap + 1 && !isLeap) {
                lunarMonth--;
                isLeap = true;
                days = leapDays(lunarYear);
            } else {
                days = monthDays(lunarYear, lunarMonth);
            }
            if (offset < days) {
                break;
            }
            offset -= days;
            if (isLeap && lunarMonth == leap + 1) {
                isLeap = false;
            }
        }
        int lunarDay = offset + 1;
        int ganIndex = Math.floorMod(lunarYear - 4, 10);
        int zhiIndex = Math.floorMod(lunarYear - 4, 12);
        return new Lunar(
                lunarYear,
                lunarMonth,
                lunarDay,
                isLeap,
                GAN[ganIndex] + ZHI[zhiIndex],
                ANIMALS[zhiIndex],
                (isLeap ? "闰" : "") + MONTHS[lunarMonth - 1] + "月",
                DAYS[lunarDay - 1]
        );
    }

    public static Lunar of(String isoDate) {
        return of(LocalDate.parse(isoDate));
    }

    private static int yearDays(int year) {
        int sum = 348;
        int info = LUNAR_INFO[year - 1900];
        for (int i = 0x8000; i > 0x8; i >>= 1) {
            if ((info & i) != 0) {
                sum++;
            }
        }
        return sum + leapDays(year);
    }

    private static int leapMonth(int year) {
        return LUNAR_INFO[year - 1900] & 0xf;
    }

    private static int leapDays(int year) {
        if (leapMonth(year) == 0) {
            return 0;
        }
        return (LUNAR_INFO[year - 1900] & 0x10000) != 0 ? 30 : 29;
    }

    private static int monthDays(int year, int month) {
        return (LUNAR_INFO[year - 1900] & (0x10000 >> month)) != 0 ? 30 : 29;
    }

    public record Lunar(int year, int month, int day, boolean leap, String ganZhi, String animal,
                        String monthName, String dayName) {
        public String display() {
            return ganZhi + "年" + monthName + dayName;
        }
    }
}
