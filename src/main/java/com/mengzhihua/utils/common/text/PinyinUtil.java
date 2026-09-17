package com.mengzhihua.utils.common.text;


import java.nio.charset.Charset;
import java.util.Locale;

/**
 * 汉字拼音首字母（GB2312 分区表，Hutool {@code PinyinUtil.getFirstLetter}，无额外拼音库）。
 */
public final class PinyinUtil {

    private static final Charset GB2312 = Charset.forName("GB2312");
    private static final int[][] RANGES = {
            {'A', 0xB0A1, 0xB0C4},
            {'B', 0xB0C5, 0xB2C0},
            {'C', 0xB2C1, 0xB4ED},
            {'D', 0xB4EE, 0xB6E9},
            {'E', 0xB6EA, 0xB7A1},
            {'F', 0xB7A2, 0xB8C0},
            {'G', 0xB8C1, 0xB9FD},
            {'H', 0xB9FE, 0xBBF6},
            {'J', 0xBBF7, 0xBFA5},
            {'K', 0xBFA6, 0xC0AB},
            {'L', 0xC0AC, 0xC2E7},
            {'M', 0xC2E8, 0xC4C2},
            {'N', 0xC4C3, 0xC5B5},
            {'O', 0xC5B6, 0xC5BD},
            {'P', 0xC5BE, 0xC6D9},
            {'Q', 0xC6DA, 0xC8BA},
            {'R', 0xC8BB, 0xC8F5},
            {'S', 0xC8F6, 0xCBF9},
            {'T', 0xCBFA, 0xCDD9},
            {'W', 0xCDDA, 0xCEF3},
            {'X', 0xCEF4, 0xD1B8},
            {'Y', 0xD1B9, 0xD4CF},
            {'Z', 0xD4D0, 0xD7F9}
    };

    private PinyinUtil() {
    }

    public static char firstLetter(char ch) {
        if (ch >= 'a' && ch <= 'z') {
            return Character.toUpperCase(ch);
        }
        if (ch >= 'A' && ch <= 'Z') {
            return ch;
        }
        if (ch < 0x4E00 || ch > 0x9FA5) {
            return ch;
        }
        byte[] bytes = String.valueOf(ch).getBytes(GB2312);
        if (bytes.length != 2) {
            return ch;
        }
        int gb = ((bytes[0] & 0xff) << 8) | (bytes[1] & 0xff);
        for (int[] range : RANGES) {
            if (gb >= range[1] && gb <= range[2]) {
                return (char) range[0];
            }
        }
        return ch;
    }

    public static String firstLetters(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(text.length());
        for (int i = 0; i < text.length(); i++) {
            builder.append(firstLetter(text.charAt(i)));
        }
        return builder.toString();
    }

    public static String firstLettersUpper(String text) {
        String letters = firstLetters(text);
        return letters == null ? null : letters.toUpperCase(Locale.ROOT);
    }
}
