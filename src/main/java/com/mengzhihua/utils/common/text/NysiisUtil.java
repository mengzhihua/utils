package com.mengzhihua.utils.common.text;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Original NYSIIS (Taft 1970 / Commons Codec {@code Nysiis}, not truncated to 6).
 * {@code MILLER} → {@code MALAR}, {@code KNUTH} → {@code NATH}.
 */
public final class NysiisUtil {

    private NysiisUtil() {
    }

    public static String encode(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String word = text.toUpperCase(Locale.ROOT).replaceAll("[^A-Z]", "");
        if (word.isEmpty()) {
            return "";
        }
        if (word.startsWith("MAC")) {
            word = "MCC" + word.substring(3);
        } else if (word.startsWith("KN")) {
            word = "NN" + word.substring(2);
        } else if (word.startsWith("K")) {
            word = "C" + word.substring(1);
        } else if (word.startsWith("PH") || word.startsWith("PF")) {
            word = "FF" + word.substring(2);
        } else if (word.startsWith("SCH")) {
            word = "SSS" + word.substring(3);
        }
        if (word.endsWith("EE") || word.endsWith("IE")) {
            word = word.substring(0, word.length() - 2) + "Y";
        } else if (word.endsWith("DT") || word.endsWith("RT") || word.endsWith("RD")
                || word.endsWith("NT") || word.endsWith("ND")) {
            word = word.substring(0, word.length() - 2) + "D";
        }
        char[] chars = word.toCharArray();
        StringBuilder key = new StringBuilder(chars.length);
        key.append(chars[0]);
        for (int i = 1; i < chars.length; i++) {
            char next = i + 1 < chars.length ? chars[i + 1] : ' ';
            char next2 = i + 2 < chars.length ? chars[i + 2] : ' ';
            char[] coded = transcode(chars[i - 1], chars[i], next, next2);
            int copy = Math.min(coded.length, chars.length - i);
            System.arraycopy(coded, 0, chars, i, copy);
            if (chars[i] != chars[i - 1]) {
                key.append(chars[i]);
            }
        }
        if (key.length() > 1) {
            if (key.charAt(key.length() - 1) == 'S') {
                key.deleteCharAt(key.length() - 1);
            }
            if (key.length() > 2 && key.charAt(key.length() - 2) == 'A' && key.charAt(key.length() - 1) == 'Y') {
                key.deleteCharAt(key.length() - 2);
            }
            if (key.length() > 1 && key.charAt(key.length() - 1) == 'A') {
                key.deleteCharAt(key.length() - 1);
            }
        }
        return key.toString();
    }

    public static boolean similar(String left, String right) {
        String a = encode(left);
        return !a.isEmpty() && a.equals(encode(right));
    }

    private static char[] transcode(char prev, char current, char next, char next2) {
        if (current == 'E' && next == 'V') {
            return new char[] {'A', 'F'};
        }
        if (isVowel(current)) {
            return new char[] {'A'};
        }
        return switch (current) {
            case 'Q' -> new char[] {'G'};
            case 'Z' -> new char[] {'S'};
            case 'M' -> new char[] {'N'};
            case 'K' -> next == 'N' ? new char[] {'N', 'N'} : new char[] {'C'};
            case 'S' -> next == 'C' && next2 == 'H' ? new char[] {'S', 'S', 'S'} : new char[] {'S'};
            case 'P' -> next == 'H' ? new char[] {'F', 'F'} : new char[] {'P'};
            case 'H' -> !isVowel(prev) || !isVowel(next) ? new char[] {prev} : new char[] {'H'};
            case 'W' -> isVowel(prev) ? new char[] {prev} : new char[] {'W'};
            default -> new char[] {current};
        };
    }

    private static boolean isVowel(char c) {
        return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }
}
