package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * Compact numbers and English ordinals (humanize / Commons Text style).
 */
public final class HumanizeUtil {

    private HumanizeUtil() {
    }

    public static String compact(long value) {
        long abs = Math.abs(value);
        if (abs < 1_000) {
            return Long.toString(value);
        }
        String[] units = {"K", "M", "B", "T"};
        double scaled = value;
        int unit = -1;
        while (Math.abs(scaled) >= 1000 && unit + 1 < units.length) {
            scaled /= 1000D;
            unit++;
        }
        String formatted = String.format(Locale.ROOT, "%.1f", scaled);
        if (formatted.endsWith(".0")) {
            formatted = formatted.substring(0, formatted.length() - 2);
        }
        return formatted + units[unit];
    }

    public static String ordinal(int value) {
        int abs = Math.abs(value);
        int mod100 = abs % 100;
        String suffix;
        if (mod100 >= 11 && mod100 <= 13) {
            suffix = "th";
        } else {
            suffix = switch (abs % 10) {
                case 1 -> "st";
                case 2 -> "nd";
                case 3 -> "rd";
                default -> "th";
            };
        }
        return value + suffix;
    }
}
