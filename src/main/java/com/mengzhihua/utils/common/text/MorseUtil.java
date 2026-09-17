package com.mengzhihua.utils.common.text;


import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * ITU Morse code (Hutool {@code Morse} style).
 */
public final class MorseUtil {

    private static final String[] LETTERS = {
            ".-", "-...", "-.-.", "-..", ".", "..-.", "--.", "....", "..", ".---",
            "-.-", ".-..", "--", "-.", "---", ".--.", "--.-", ".-.", "...", "-",
            "..-", "...-", ".--", "-..-", "-.--", "--.."
    };
    private static final String[] DIGITS = {
            "-----", ".----", "..---", "...--", "....-", ".....", "-....", "--...", "---..", "----."
    };
    private static final Map<String, Character> DECODE = new HashMap<>();

    static {
        for (int i = 0; i < 26; i++) {
            DECODE.put(LETTERS[i], (char) ('A' + i));
        }
        for (int i = 0; i < 10; i++) {
            DECODE.put(DIGITS[i], (char) ('0' + i));
        }
    }

    private MorseUtil() {
    }

    public static String encode(String text) {
        if (text == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String normalized = text.toUpperCase(Locale.ROOT).trim();
        boolean firstToken = true;
        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            if (c == ' ') {
                if (!firstToken) {
                    builder.append(" / ");
                    firstToken = true;
                }
                continue;
            }
            String code = codeOf(c);
            if (code == null) {
                continue;
            }
            if (!firstToken) {
                builder.append(' ');
            }
            builder.append(code);
            firstToken = false;
        }
        return builder.toString();
    }

    public static String decode(String morse) {
        if (StringUtil.isBlank(morse)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String[] words = morse.trim().split("\\s+/\\s+");
        for (int w = 0; w < words.length; w++) {
            if (w > 0) {
                builder.append(' ');
            }
            String[] letters = words[w].trim().split("\\s+");
            for (String letter : letters) {
                Character decoded = DECODE.get(letter);
                if (decoded != null) {
                    builder.append(decoded);
                }
            }
        }
        return builder.toString();
    }

    private static String codeOf(char c) {
        if (c >= 'A' && c <= 'Z') {
            return LETTERS[c - 'A'];
        }
        if (c >= '0' && c <= '9') {
            return DIGITS[c - '0'];
        }
        return null;
    }
}
