package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Line-level LCS diff (Hutool {@code TextSimilarity} companion).
 */
public final class TextDiffUtil {

    public enum Op {
        EQUAL, INSERT, DELETE
    }

    public record Diff(Op op, String text) {
    }

    private TextDiffUtil() {
    }

    public static List<Diff> diffLines(String left, String right) {
        String[] a = lines(left);
        String[] b = lines(right);
        int n = a.length;
        int m = b.length;
        int[][] dp = new int[n + 1][m + 1];
        for (int i = n - 1; i >= 0; i--) {
            for (int j = m - 1; j >= 0; j--) {
                dp[i][j] = a[i].equals(b[j])
                        ? dp[i + 1][j + 1] + 1
                        : Math.max(dp[i + 1][j], dp[i][j + 1]);
            }
        }
        List<Diff> diffs = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < n && j < m) {
            if (a[i].equals(b[j])) {
                diffs.add(new Diff(Op.EQUAL, a[i]));
                i++;
                j++;
            } else if (dp[i + 1][j] >= dp[i][j + 1]) {
                diffs.add(new Diff(Op.DELETE, a[i]));
                i++;
            } else {
                diffs.add(new Diff(Op.INSERT, b[j]));
                j++;
            }
        }
        while (i < n) {
            diffs.add(new Diff(Op.DELETE, a[i++]));
        }
        while (j < m) {
            diffs.add(new Diff(Op.INSERT, b[j++]));
        }
        return diffs;
    }

    public static String unified(String left, String right) {
        StringBuilder builder = new StringBuilder();
        for (Diff diff : diffLines(left, right)) {
            switch (diff.op()) {
                case EQUAL -> builder.append("  ").append(diff.text()).append('\n');
                case DELETE -> builder.append("- ").append(diff.text()).append('\n');
                case INSERT -> builder.append("+ ").append(diff.text()).append('\n');
            }
        }
        return builder.toString();
    }

    public static int changedLines(String left, String right) {
        int n = 0;
        for (Diff diff : diffLines(left, right)) {
            if (diff.op() != Op.EQUAL) {
                n++;
            }
        }
        return n;
    }

    private static String[] lines(String text) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }
        return text.split("\n", -1);
    }
}
