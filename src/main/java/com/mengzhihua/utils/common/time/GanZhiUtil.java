package com.mengzhihua.utils.common.time;

/**
 * 天干地支 / 生肖（Hutool {@code ChineseDate}，1984=甲子鼠）。
 */
public final class GanZhiUtil {

    private static final String[] STEM = {"甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "壬", "癸"};
    private static final String[] BRANCH = {"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    private static final String[] ANIMAL = {"鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗", "猪"};

    private GanZhiUtil() {
    }

    public static String year(int year) {
        int idx = Math.floorMod(year - 1984, 60);
        return STEM[idx % 10] + BRANCH[idx % 12];
    }

    public static String animal(int year) {
        return ANIMAL[Math.floorMod(year - 1984, 12)];
    }

    public static String format(int year) {
        return year(year) + animal(year);
    }
}
