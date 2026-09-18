package com.mengzhihua.utils.common.text;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Match Rating Approach (Western Airlines / Commons Codec). {@code Smith} → {@code SMTH}.
 */
public final class MatchRatingUtil {

    private static final String[] DOUBLES = {
            "BB", "CC", "DD", "FF", "GG", "HH", "JJ", "KK", "LL", "MM", "NN",
            "PP", "QQ", "RR", "SS", "TT", "VV", "WW", "XX", "YY", "ZZ"
    };

    private MatchRatingUtil() {
    }

    public static String encode(String name) {
        if (StringUtil.isBlank(name) || name.trim().length() == 1) {
            return "";
        }
        String cleaned = clean(name);
        if (cleaned.isEmpty()) {
            return "";
        }
        cleaned = removeVowels(cleaned);
        if (cleaned.isEmpty()) {
            return "";
        }
        cleaned = removeDoubles(cleaned);
        return first3Last3(cleaned);
    }

    public static boolean similar(String left, String right) {
        if (StringUtil.isBlank(left) || StringUtil.isBlank(right) || left.trim().length() == 1 || right.trim().length() == 1) {
            return false;
        }
        if (left.equalsIgnoreCase(right)) {
            return true;
        }
        String a = encode(left);
        String b = encode(right);
        if (a.isEmpty() || b.isEmpty() || Math.abs(a.length() - b.length()) >= 3) {
            return false;
        }
        int minRating = minRating(a.length() + b.length());
        return leftoverScore(a, b) >= minRating;
    }

    private static String clean(String name) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (c >= 'a' && c <= 'z') {
                c = Character.toUpperCase(c);
            }
            if (c >= 'A' && c <= 'Z') {
                out.append(c);
            }
        }
        return out.toString();
    }

    private static String removeVowels(String name) {
        String first = name.substring(0, 1);
        String rest = name.replace("A", "").replace("E", "").replace("I", "").replace("O", "").replace("U", "");
        return isVowel(first) ? first + rest : rest;
    }

    private static boolean isVowel(String letter) {
        return "AEIOU".contains(letter);
    }

    private static String removeDoubles(String name) {
        String replaced = name;
        for (String pair : DOUBLES) {
            replaced = replaced.replace(pair, pair.substring(0, 1));
        }
        return replaced;
    }

    private static String first3Last3(String name) {
        if (name.length() <= 6) {
            return name;
        }
        return name.substring(0, 3) + name.substring(name.length() - 3);
    }

    private static int minRating(int sumLength) {
        if (sumLength <= 4) {
            return 5;
        }
        if (sumLength <= 7) {
            return 4;
        }
        if (sumLength <= 11) {
            return 3;
        }
        if (sumLength == 12) {
            return 2;
        }
        return 1;
    }

    private static int leftoverScore(String name1, String name2) {
        char[] a = name1.toCharArray();
        char[] b = name2.toCharArray();
        int lastA = a.length - 1;
        int lastB = b.length - 1;
        for (int i = 0; i < a.length && i <= lastB; i++) {
            if (name1.charAt(i) == name2.charAt(i)) {
                a[i] = ' ';
                b[i] = ' ';
            }
            if (name1.charAt(lastA - i) == name2.charAt(lastB - i)) {
                a[lastA - i] = ' ';
                b[lastB - i] = ' ';
            }
        }
        String left = new String(a).replace(" ", "");
        String right = new String(b).replace(" ", "");
        int longer = Math.max(left.length(), right.length());
        return Math.abs(6 - longer);
    }
}
