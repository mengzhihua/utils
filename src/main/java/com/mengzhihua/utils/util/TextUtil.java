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

    /**
     * Jaro-Winkler similarity in {@code [0, 1]} (Apache Commons Text).
     */
    public static double jaroWinkler(String left, String right) {
        String a = left == null ? "" : left;
        String b = right == null ? "" : right;
        double jaro = jaro(a, b);
        int prefix = 0;
        int limit = Math.min(4, Math.min(a.length(), b.length()));
        while (prefix < limit && a.charAt(prefix) == b.charAt(prefix)) {
            prefix++;
        }
        return jaro + prefix * 0.1 * (1 - jaro);
    }

    public static double jaro(String left, String right) {
        String a = left == null ? "" : left;
        String b = right == null ? "" : right;
        if (a.equals(b)) {
            return 1D;
        }
        if (a.isEmpty() || b.isEmpty()) {
            return 0D;
        }
        int matchDistance = Math.max(a.length(), b.length()) / 2 - 1;
        boolean[] aMatch = new boolean[a.length()];
        boolean[] bMatch = new boolean[b.length()];
        int matches = 0;
        for (int i = 0; i < a.length(); i++) {
            int from = Math.max(0, i - matchDistance);
            int to = Math.min(i + matchDistance + 1, b.length());
            for (int j = from; j < to; j++) {
                if (bMatch[j] || a.charAt(i) != b.charAt(j)) {
                    continue;
                }
                aMatch[i] = true;
                bMatch[j] = true;
                matches++;
                break;
            }
        }
        if (matches == 0) {
            return 0D;
        }
        int transpositions = 0;
        int k = 0;
        for (int i = 0; i < a.length(); i++) {
            if (!aMatch[i]) {
                continue;
            }
            while (!bMatch[k]) {
                k++;
            }
            if (a.charAt(i) != b.charAt(k)) {
                transpositions++;
            }
            k++;
        }
        double m = matches;
        return (m / a.length() + m / b.length() + (m - transpositions / 2.0) / m) / 3.0;
    }

    public static int hamming(String left, String right) {
        String a = left == null ? "" : left;
        String b = right == null ? "" : right;
        if (a.length() != b.length()) {
            throw new IllegalArgumentException("hamming requires equal length");
        }
        int n = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                n++;
            }
        }
        return n;
    }

    public static String longestCommonSubsequence(String left, String right) {
        String a = left == null ? "" : left;
        String b = right == null ? "" : right;
        int n = a.length();
        int m = b.length();
        int[][] dp = new int[n + 1][m + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = m - 1; j >= 0; j--) {
                dp[i][j] = a.charAt(i) == b.charAt(j)
                        ? dp[i + 1][j + 1] + 1
                        : Math.max(dp[i + 1][j], dp[i][j + 1]);
            }
        }
        StringBuilder builder = new StringBuilder();
        int i = 0;
        int j = 0;
        while (i < n && j < m) {
            if (a.charAt(i) == b.charAt(j)) {
                builder.append(a.charAt(i));
                i++;
                j++;
            } else if (dp[i + 1][j] >= dp[i][j + 1]) {
                i++;
            } else {
                j++;
            }
        }
        return builder.toString();
    }

    public static double jaccard(String left, String right) {
        java.util.Set<String> a = shingles(left);
        java.util.Set<String> b = shingles(right);
        if (a.isEmpty() && b.isEmpty()) {
            return 1D;
        }
        java.util.Set<String> union = new java.util.HashSet<>(a);
        union.addAll(b);
        java.util.Set<String> inter = new java.util.HashSet<>(a);
        inter.retainAll(b);
        return inter.size() / (double) union.size();
    }

    private static java.util.Set<String> shingles(String text) {
        String value = text == null ? "" : text;
        java.util.Set<String> set = new java.util.LinkedHashSet<>();
        if (value.length() < 2) {
            if (!value.isEmpty()) {
                set.add(value);
            }
            return set;
        }
        for (int i = 0; i < value.length() - 1; i++) {
            set.add(value.substring(i, i + 2));
        }
        return set;
    }
}
