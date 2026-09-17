package com.mengzhihua.utils.common.lang;

/**
 * Character classifiers aligned with common Hutool {@code CharUtil} usage.
 */
public final class CharUtil {

    private CharUtil() {
    }

    public static boolean isAscii(char c) {
        return c < 128;
    }

    public static boolean isBlankChar(char c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a';
    }

    public static boolean isChinese(char c) {
        return c >= 0x4e00 && c <= 0x9fff;
    }

    public static boolean isEmoji(int codePoint) {
        return (codePoint >= 0x1F300 && codePoint <= 0x1F6FF)
                || (codePoint >= 0x1F900 && codePoint <= 0x1F9FF)
                || (codePoint >= 0x2600 && codePoint <= 0x27BF);
    }

    public static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isLetter(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }
}
