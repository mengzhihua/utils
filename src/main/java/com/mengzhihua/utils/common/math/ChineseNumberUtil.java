package com.mengzhihua.utils.common.math;


import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Arabic numbers to simplified Chinese, including RMB amount in uppercase.
 */
public final class ChineseNumberUtil {

    private static final char[] DIGITS = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};
    private static final char[] MONEY = {'零', '壹', '贰', '叁', '肆', '伍', '陆', '柒', '捌', '玖'};
    private static final String[] UNITS = {"", "十", "百", "千"};
    private static final String[] MONEY_UNITS = {"", "拾", "佰", "仟"};
    private static final String[] BIG_UNITS = {"", "万", "亿"};

    private ChineseNumberUtil() {
    }

    public static String toChinese(long number) {
        if (number == 0) {
            return "零";
        }
        if (number < 0) {
            return "负" + toChinese(-number);
        }
        return formatSection(number, DIGITS, UNITS, false);
    }

    public static String toRmb(Object amount) {
        BigDecimal value = NumberUtil.toBigDecimal(amount).setScale(2, RoundingMode.HALF_UP);
        if (value.compareTo(BigDecimal.ZERO) == 0) {
            return "零元整";
        }
        boolean negative = value.signum() < 0;
        value = value.abs();
        long yuan = value.longValue();
        int cents = value.remainder(BigDecimal.ONE).movePointRight(2).intValue();
        StringBuilder builder = new StringBuilder();
        if (negative) {
            builder.append("负");
        }
        if (yuan > 0) {
            builder.append(formatSection(yuan, MONEY, MONEY_UNITS, true)).append("元");
        }
        int jiao = cents / 10;
        int fen = cents % 10;
        if (cents == 0) {
            builder.append("整");
        } else {
            if (jiao > 0) {
                builder.append(MONEY[jiao]).append("角");
            } else if (yuan > 0) {
                builder.append("零");
            }
            if (fen > 0) {
                builder.append(MONEY[fen]).append("分");
            }
        }
        return builder.toString();
    }

    private static String formatSection(long number, char[] digits, String[] smallUnits, boolean money) {
        StringBuilder builder = new StringBuilder();
        String num = Long.toString(number);
        int zeroCount = 0;
        int length = num.length();
        for (int i = 0; i < length; i++) {
            int digit = num.charAt(i) - '0';
            int pos = length - i - 1;
            int unitIndex = pos % 4;
            int bigIndex = pos / 4;
            if (digit == 0) {
                zeroCount++;
            } else {
                if (zeroCount > 0) {
                    builder.append(digits[0]);
                }
                zeroCount = 0;
                if (!money && digit == 1 && unitIndex == 1 && builder.isEmpty()) {
                    builder.append(smallUnits[1]);
                } else {
                    builder.append(digits[digit]).append(smallUnits[unitIndex]);
                }
            }
            if (unitIndex == 0 && bigIndex > 0 && (zeroCount < 4 || builder.isEmpty())) {
                if (!builder.isEmpty() && !endsWithBigUnit(builder)) {
                    builder.append(BIG_UNITS[bigIndex]);
                }
                zeroCount = 0;
            }
        }
        return builder.toString();
    }

    private static boolean endsWithBigUnit(StringBuilder builder) {
        String text = builder.toString();
        return text.endsWith("万") || text.endsWith("亿");
    }
}
