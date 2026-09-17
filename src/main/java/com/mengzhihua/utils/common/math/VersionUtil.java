package com.mengzhihua.utils.common.math;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Semantic-ish version compare for {@code 1.2.10} style strings.
 */
public final class VersionUtil {

    private VersionUtil() {
    }

    /**
     * @return negative if {@code left < right}, 0 if equal, positive if {@code left > right}
     */
    public static int compare(String left, String right) {
        int[] a = split(left);
        int[] b = split(right);
        int len = Math.max(a.length, b.length);
        for (int i = 0; i < len; i++) {
            int av = i < a.length ? a[i] : 0;
            int bv = i < b.length ? b[i] : 0;
            if (av != bv) {
                return Integer.compare(av, bv);
            }
        }
        return 0;
    }

    public static boolean isGreater(String left, String right) {
        return compare(left, right) > 0;
    }

    public static boolean isCompatible(String current, String required) {
        return compare(current, required) >= 0;
    }

    private static int[] split(String version) {
        if (StringUtil.isBlank(version)) {
            return new int[]{0};
        }
        String[] parts = version.trim().split("[.+\\-]");
        int[] numbers = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            String digits = parts[i].replaceAll("\\D", "");
            numbers[i] = digits.isEmpty() ? 0 : Integer.parseInt(digits);
        }
        return numbers;
    }
}
