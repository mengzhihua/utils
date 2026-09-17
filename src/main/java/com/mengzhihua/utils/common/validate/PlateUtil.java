package com.mengzhihua.utils.common.validate;


import com.mengzhihua.utils.common.text.RegexUtil;

/**
 * 中国机动车号牌（Hutool 风格，基于 {@link RegexUtil#PLATE}）。
 */
public final class PlateUtil {

    private PlateUtil() {
    }

    public static boolean isValid(String plate) {
        return RegexUtil.isPlate(plate);
    }

    public static boolean isNewEnergy(String plate) {
        return isValid(plate) && plate.length() == 8;
    }

    public static String province(String plate) {
        return isValid(plate) ? plate.substring(0, 1) : "";
    }
}
