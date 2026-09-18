package com.mengzhihua.utils.common.text;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Cologne Phonetic (Kölner Phonetik / Commons Codec). {@code Müller} → {@code 657}.
 */
public final class ColognePhoneticUtil {

    private ColognePhoneticUtil() {
    }

    public static String encode(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String word = text.toUpperCase(Locale.GERMAN)
                .replace('Ä', 'A')
                .replace('Ö', 'O')
                .replace('Ü', 'U')
                .replace('ß', '8');
        StringBuilder codes = new StringBuilder();
        char last = 0;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if ((c < 'A' || c > 'Z') && c != '8') {
                continue;
            }
            if (c == 'X' && prev(word, i) != 'C' && prev(word, i) != 'K' && prev(word, i) != 'Q') {
                if (last != '4') {
                    codes.append('4');
                }
                if (last != '8') {
                    codes.append('8');
                }
                last = '8';
                continue;
            }
            char code = code(word, i);
            if (code == 0) {
                continue;
            }
            if (code != last) {
                codes.append(code);
                last = code;
            }
        }
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < codes.length(); i++) {
            char ch = codes.charAt(i);
            if (ch != '0' || i == 0) {
                out.append(ch);
            }
        }
        return out.toString();
    }

    private static char code(String word, int i) {
        char c = word.charAt(i);
        char next = i + 1 < word.length() ? word.charAt(i + 1) : 0;
        return switch (c) {
            case 'A', 'E', 'I', 'J', 'O', 'U', 'Y' -> '0';
            case 'B' -> '1';
            case 'P' -> next == 'H' ? '3' : '1';
            case 'D', 'T' -> (next == 'C' || next == 'S' || next == 'Z') ? '8' : '2';
            case 'F', 'V', 'W' -> '3';
            case 'G', 'K', 'Q' -> '4';
            case 'C' -> codeC(word, i, next);
            case 'X' -> (prev(word, i) == 'C' || prev(word, i) == 'K' || prev(word, i) == 'Q') ? '8' : '4';
            case 'L' -> '5';
            case 'M', 'N' -> '6';
            case 'R' -> '7';
            case 'S', 'Z', '8' -> '8';
            default -> 0;
        };
    }

    private static char codeC(String word, int i, char next) {
        if (i == 0) {
            return (next == 'A' || next == 'H' || next == 'K' || next == 'L'
                    || next == 'O' || next == 'Q' || next == 'R' || next == 'U' || next == 'X')
                    ? '4' : '8';
        }
        char prev = prev(word, i);
        if ((next == 'A' || next == 'H' || next == 'K' || next == 'O' || next == 'Q' || next == 'U' || next == 'X')
                && prev != 'S' && prev != 'Z') {
            return '4';
        }
        return '8';
    }

    private static char prev(String word, int i) {
        return i == 0 ? 0 : word.charAt(i - 1);
    }
}
