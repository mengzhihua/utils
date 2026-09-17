package com.mengzhihua.utils.util;

/**
 * 文本相似度（Levenshtein）与空白规范化。
 */
public final class TextUtil {

    private TextUtil() {
    }

    public static int levenshtein(String left, String right) {
        String a = left == null ? "" : left;
        String b = right == null ? "" : right;
        int[] prev = new int[b.length() + 1];
        int[] curr = new int[b.length() + 1];
        for (int j = 0; j <= b.length(); j++) {
            prev[j] = j;
        }
        for (int i = 1; i <= a.length(); i++) {
            curr[0] = i;
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] swap = prev;
            prev = curr;
            curr = swap;
        }
        return prev[b.length()];
    }

    public static double similarity(String left, String right) {
        String a = left == null ? "" : left;
        String b = right == null ? "" : right;
        int max = Math.max(a.length(), b.length());
        if (max == 0) {
            return 1D;
        }
        return 1D - (levenshtein(a, b) / (double) max);
    }

    public static String collapseWhitespace(String text) {
        if (text == null) {
            return null;
        }
        return text.trim().replaceAll("\\s+", " ");
    }
}
