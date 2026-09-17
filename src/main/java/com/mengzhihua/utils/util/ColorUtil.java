package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * HEX / RGB helpers and relative luminance.
 */
public final class ColorUtil {

    private ColorUtil() {
    }

    public static int[] hexToRgb(String hex) {
        String value = normalize(hex);
        int n = Integer.parseInt(value, 16);
        return new int[] {(n >> 16) & 255, (n >> 8) & 255, n & 255};
    }

    public static String rgbToHex(int r, int g, int b) {
        return String.format(Locale.ROOT, "#%02X%02X%02X", clamp(r), clamp(g), clamp(b));
    }

    public static double luminance(String hex) {
        int[] rgb = hexToRgb(hex);
        return (0.2126 * channel(rgb[0]) + 0.7152 * channel(rgb[1]) + 0.0722 * channel(rgb[2]));
    }

    public static boolean isDark(String hex) {
        return luminance(hex) < 0.5;
    }

    private static String normalize(String hex) {
        String value = hex == null ? "" : hex.trim();
        if (value.startsWith("#")) {
            value = value.substring(1);
        }
        if (value.length() == 3) {
            value = "" + value.charAt(0) + value.charAt(0) + value.charAt(1) + value.charAt(1)
                    + value.charAt(2) + value.charAt(2);
        }
        if (!value.matches("(?i)[0-9a-f]{6}")) {
            throw new IllegalArgumentException("invalid hex color: " + hex);
        }
        return value;
    }

    private static int clamp(int value) {
        return Math.max(0, Math.min(255, value));
    }

    private static double channel(int value) {
        double s = value / 255D;
        return s <= 0.03928 ? s / 12.92 : Math.pow((s + 0.055) / 1.055, 2.4);
    }
}
