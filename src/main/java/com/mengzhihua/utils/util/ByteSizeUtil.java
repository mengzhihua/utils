package com.mengzhihua.utils.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字节大小可读格式与解析。
 */
public final class ByteSizeUtil {

    private static final String[] UNITS = {"B", "KB", "MB", "GB", "TB", "PB"};
    private static final Pattern PARSE = Pattern.compile("^\\s*([0-9]+(?:\\.[0-9]+)?)\\s*([KMGTP]?B?)\\s*$", Pattern.CASE_INSENSITIVE);

    private ByteSizeUtil() {
    }

    public static String format(long bytes) {
        if (bytes < 0) {
            throw new IllegalArgumentException("bytes must be >= 0");
        }
        double value = bytes;
        int unit = 0;
        while (value >= 1024 && unit < UNITS.length - 1) {
            value /= 1024;
            unit++;
        }
        if (unit == 0) {
            return bytes + " B";
        }
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).toPlainString() + " " + UNITS[unit];
    }

    public static long parse(String text) {
        if (StringUtil.isBlank(text)) {
            throw new IllegalArgumentException("size text is blank");
        }
        Matcher matcher = PARSE.matcher(text.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid size: " + text);
        }
        BigDecimal number = new BigDecimal(matcher.group(1));
        String unit = matcher.group(2).toUpperCase(Locale.ROOT);
        int shift = switch (unit) {
            case "", "B" -> 0;
            case "K", "KB" -> 1;
            case "M", "MB" -> 2;
            case "G", "GB" -> 3;
            case "T", "TB" -> 4;
            case "P", "PB" -> 5;
            default -> throw new IllegalArgumentException("invalid size unit: " + unit);
        };
        return number.multiply(BigDecimal.valueOf(1L << (10 * shift))).setScale(0, RoundingMode.HALF_UP).longValue();
    }
}
