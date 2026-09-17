package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * Lawrence Philips Metaphone (Apache Commons Codec, max length 4).
 */
public final class MetaphoneUtil {

    private static final int MAX_LENGTH = 4;

    private MetaphoneUtil() {
    }

    public static String encode(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String word = text.toUpperCase(Locale.ROOT).replaceAll("[^A-Z]", "");
        if (word.isEmpty()) {
            return "";
        }
        if (word.length() > 1) {
            String prefix = word.substring(0, 2);
            if (prefix.equals("KN") || prefix.equals("GN") || prefix.equals("PN")
                    || prefix.equals("AE") || prefix.equals("WR")) {
                word = word.substring(1);
            }
        }
        if (word.charAt(0) == 'X') {
            word = "S" + word.substring(1);
        }
        StringBuilder code = new StringBuilder(MAX_LENGTH);
        int i = 0;
        char prev = 0;
        while (i < word.length() && code.length() < MAX_LENGTH) {
            char c = word.charAt(i);
            if (c == prev && c != 'C') {
                i++;
                continue;
            }
            switch (c) {
                case 'A', 'E', 'I', 'O', 'U' -> {
                    if (i == 0) {
                        code.append(c);
                    }
                }
                case 'B' -> {
                    if (!(i == word.length() - 1 && prev == 'M')) {
                        code.append('B');
                    }
                }
                case 'C' -> {
                    if (match(word, i, "CIA") || (next(word, i) == 'H' && prev != 'S')) {
                        code.append('X');
                    } else if (next(word, i) == 'I' || next(word, i) == 'E' || next(word, i) == 'Y') {
                        code.append('S');
                    } else {
                        code.append('K');
                    }
                }
                case 'D' -> {
                    if (match(word, i, "DGE") || match(word, i, "DGY") || match(word, i, "DGI")) {
                        code.append('J');
                    } else {
                        code.append('T');
                    }
                }
                case 'F', 'J', 'L', 'M', 'N', 'R' -> code.append(c);
                case 'G' -> {
                    char n = next(word, i);
                    if (n == 'H' && !vowel(next(word, i + 1)) && i + 1 != word.length() - 1) {
                        break;
                    }
                    if (n == 'N' && (i + 1 == word.length() - 1 || match(word, i, "GNED") && i + 3 == word.length() - 1)) {
                        break;
                    }
                    if ((n == 'I' || n == 'E' || n == 'Y') && prev != 'G') {
                        code.append('J');
                    } else {
                        code.append('K');
                    }
                }
                case 'H' -> {
                    if (i == 0 || vowel(prev) && vowel(next(word, i))) {
                        code.append('H');
                    }
                }
                case 'K' -> {
                    if (prev != 'C') {
                        code.append('K');
                    }
                }
                case 'P' -> code.append(next(word, i) == 'H' ? 'F' : 'P');
                case 'Q' -> code.append('K');
                case 'S' -> {
                    if (match(word, i, "SIA") || match(word, i, "SIO") || next(word, i) == 'H') {
                        code.append('X');
                    } else {
                        code.append('S');
                    }
                }
                case 'T' -> {
                    if (match(word, i, "TIA") || match(word, i, "TIO")) {
                        code.append('X');
                    } else if (next(word, i) == 'H') {
                        code.append('0');
                    } else if (!match(word, i, "TCH")) {
                        code.append('T');
                    }
                }
                case 'V' -> code.append('F');
                case 'W' -> {
                    if (i == 0 && next(word, i) == 'H') {
                        code.append('W');
                    } else if (vowel(next(word, i))) {
                        code.append('W');
                    }
                }
                case 'X' -> {
                    code.append('K');
                    if (code.length() < MAX_LENGTH) {
                        code.append('S');
                    }
                }
                case 'Y' -> {
                    if (vowel(next(word, i))) {
                        code.append('Y');
                    }
                }
                case 'Z' -> code.append('S');
                default -> {
                }
            }
            prev = c;
            i++;
        }
        return code.toString();
    }

    public static boolean similar(String left, String right) {
        String encoded = encode(left);
        return !encoded.isEmpty() && encoded.equals(encode(right));
    }

    private static boolean vowel(char c) {
        return c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U';
    }

    private static char next(String word, int i) {
        return i + 1 < word.length() ? word.charAt(i + 1) : 0;
    }

    private static boolean match(String word, int i, String token) {
        return word.startsWith(token, i);
    }
}
