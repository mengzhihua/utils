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

    public static int[] hexToHsl(String hex) {
        int[] rgb = hexToRgb(hex);
        double r = rgb[0] / 255D;
        double g = rgb[1] / 255D;
        double b = rgb[2] / 255D;
        double max = Math.max(r, Math.max(g, b));
        double min = Math.min(r, Math.min(g, b));
        double l = (max + min) / 2;
        double h = 0;
        double s = 0;
        if (max != min) {
            double d = max - min;
            s = l > 0.5 ? d / (2 - max - min) : d / (max + min);
            if (max == r) {
                h = (g - b) / d + (g < b ? 6 : 0);
            } else if (max == g) {
                h = (b - r) / d + 2;
            } else {
                h = (r - g) / d + 4;
            }
            h *= 60;
        }
        return new int[] {(int) Math.round(h), (int) Math.round(s * 100), (int) Math.round(l * 100)};
    }

    public static String mix(String left, String right, double ratio) {
        int[] a = hexToRgb(left);
        int[] b = hexToRgb(right);
        double t = Math.max(0, Math.min(1, ratio));
        return rgbToHex(
                (int) Math.round(a[0] + (b[0] - a[0]) * t),
                (int) Math.round(a[1] + (b[1] - a[1]) * t),
                (int) Math.round(a[2] + (b[2] - a[2]) * t)
        );
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
