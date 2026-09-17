package com.mengzhihua.utils.common.text;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Double Metaphone (Lawrence Philips / Commons Codec, max 4). {@code Smith} → {@code SM0} / {@code XMT}.
 */
public final class DoubleMetaphoneUtil {

    private static final int MAX = 4;
    private static final String VOWELS = "AEIOUY";

    private DoubleMetaphoneUtil() {
    }

    public static String encode(String text) {
        return encodeBoth(text).primary();
    }

    public static String alternate(String text) {
        return encodeBoth(text).alternate();
    }

    public static boolean similar(String left, String right) {
        Codes a = encodeBoth(left);
        Codes b = encodeBoth(right);
        if (a.primary().isEmpty() || b.primary().isEmpty()) {
            return false;
        }
        return a.primary().equals(b.primary())
                || a.primary().equals(b.alternate())
                || a.alternate().equals(b.primary())
                || (!a.alternate().isEmpty() && a.alternate().equals(b.alternate()));
    }

    public static Codes encodeBoth(String text) {
        if (StringUtil.isBlank(text)) {
            return new Codes("", "");
        }
        String word = text.toUpperCase(Locale.ROOT).replaceAll("[^A-Z]", "");
        if (word.isEmpty()) {
            return new Codes("", "");
        }
        Result result = new Result();
        int index = silentStart(word) ? 1 : 0;
        while (!result.full() && index < word.length()) {
            index = step(word, result, index);
        }
        String primary = result.primary.toString();
        String alternate = result.alternate.toString();
        if (alternate.isEmpty()) {
            alternate = primary;
        }
        return new Codes(primary, alternate);
    }

    private static boolean silentStart(String word) {
        return word.startsWith("GN") || word.startsWith("KN") || word.startsWith("PN")
                || word.startsWith("WR") || word.startsWith("PS");
    }

    private static int step(String word, Result result, int i) {
        char c = word.charAt(i);
        return switch (c) {
            case 'A', 'E', 'I', 'O', 'U', 'Y' -> {
                if (i == 0) {
                    result.append('A');
                }
                yield i + 1;
            }
            case 'B' -> {
                result.append('P');
                yield at(word, i + 1) == 'B' ? i + 2 : i + 1;
            }
            case 'C' -> handleC(word, result, i);
            case 'D' -> handleD(word, result, i);
            case 'F' -> {
                result.append('F');
                yield at(word, i + 1) == 'F' ? i + 2 : i + 1;
            }
            case 'G' -> handleG(word, result, i);
            case 'H' -> {
                if ((i == 0 || vowel(at(word, i - 1))) && vowel(at(word, i + 1))) {
                    result.append('H');
                    yield i + 2;
                }
                yield i + 1;
            }
            case 'J' -> {
                result.append('J', 'A');
                yield at(word, i + 1) == 'J' ? i + 2 : i + 1;
            }
            case 'K' -> {
                result.append('K');
                yield at(word, i + 1) == 'K' ? i + 2 : i + 1;
            }
            case 'L' -> {
                result.append('L');
                yield at(word, i + 1) == 'L' ? i + 2 : i + 1;
            }
            case 'M' -> {
                result.append('M');
                yield at(word, i + 1) == 'M' ? i + 2 : i + 1;
            }
            case 'N' -> {
                result.append('N');
                yield at(word, i + 1) == 'N' ? i + 2 : i + 1;
            }
            case 'P' -> {
                if (at(word, i + 1) == 'H') {
                    result.append('F');
                    yield i + 2;
                }
                result.append('P');
                yield at(word, i + 1) == 'P' || at(word, i + 1) == 'B' ? i + 2 : i + 1;
            }
            case 'Q' -> {
                result.append('K');
                yield at(word, i + 1) == 'Q' ? i + 2 : i + 1;
            }
            case 'R' -> {
                result.append('R');
                yield at(word, i + 1) == 'R' ? i + 2 : i + 1;
            }
            case 'S' -> handleS(word, result, i);
            case 'T' -> handleT(word, result, i);
            case 'V' -> {
                result.append('F');
                yield at(word, i + 1) == 'V' ? i + 2 : i + 1;
            }
            case 'W' -> handleW(word, result, i);
            case 'X' -> {
                if (i == 0) {
                    result.append('S');
                    yield i + 1;
                }
                result.append("KS");
                yield at(word, i + 1) == 'C' || at(word, i + 1) == 'X' ? i + 2 : i + 1;
            }
            case 'Z' -> {
                result.append('S');
                yield at(word, i + 1) == 'Z' ? i + 2 : i + 1;
            }
            default -> i + 1;
        };
    }

    private static int handleC(String word, Result result, int i) {
        if (contains(word, i, "CH")) {
            result.append('X');
            return i + 2;
        }
        if (contains(word, i, "CIA") || contains(word, i, "CIE") || contains(word, i, "CIY")) {
            result.append('X');
            return i + 3;
        }
        if (at(word, i + 1) == 'I' || at(word, i + 1) == 'E' || at(word, i + 1) == 'Y') {
            result.append('S');
            return i + 2;
        }
        if (contains(word, i, "CK") || contains(word, i, "CG") || contains(word, i, "CQ")) {
            result.append('K');
            return i + 2;
        }
        result.append('K');
        return at(word, i + 1) == 'C' ? i + 2 : i + 1;
    }

    private static int handleD(String word, Result result, int i) {
        if (contains(word, i, "DGE") || contains(word, i, "DGI") || contains(word, i, "DGY")) {
            result.append('J');
            return i + 3;
        }
        if (contains(word, i, "DT") || contains(word, i, "DD")) {
            result.append('T');
            return i + 2;
        }
        result.append('T');
        return i + 1;
    }

    private static int handleG(String word, Result result, int i) {
        if (at(word, i + 1) == 'H') {
            if (i > 0 && !vowel(at(word, i - 1))) {
                return i + 2;
            }
            if (i == 0) {
                result.append(vowel(at(word, i + 2)) ? 'K' : 'K');
                return i + 2;
            }
            result.append('K');
            return i + 2;
        }
        if (at(word, i + 1) == 'N') {
            result.append("KN", "N");
            return i + 2;
        }
        if (at(word, i + 1) == 'I' || at(word, i + 1) == 'E' || at(word, i + 1) == 'Y') {
            result.append('J');
            return i + 2;
        }
        result.append('K');
        return at(word, i + 1) == 'G' ? i + 2 : i + 1;
    }

    private static int handleS(String word, Result result, int i) {
        if (contains(word, i, "SH") || contains(word, i, "SIO") || contains(word, i, "SIA")) {
            result.append('X');
            return contains(word, i, "SH") ? i + 2 : i + 3;
        }
        if (contains(word, i, "SCH")) {
            result.append('X');
            return i + 3;
        }
        if (i == 0 && (at(word, i + 1) == 'M' || at(word, i + 1) == 'N'
                || at(word, i + 1) == 'L' || at(word, i + 1) == 'W')) {
            result.append('S', 'X');
            return i + 1;
        }
        result.append('S');
        return at(word, i + 1) == 'S' || at(word, i + 1) == 'Z' ? i + 2 : i + 1;
    }

    private static int handleT(String word, Result result, int i) {
        if (contains(word, i, "TIA") || contains(word, i, "TIO") || contains(word, i, "TCH")) {
            result.append('X');
            return contains(word, i, "TCH") ? i + 3 : i + 3;
        }
        if (contains(word, i, "TH") || contains(word, i, "TTH")) {
            result.append('0', 'T');
            return contains(word, i, "TTH") ? i + 3 : i + 2;
        }
        result.append('T');
        return at(word, i + 1) == 'T' || at(word, i + 1) == 'D' ? i + 2 : i + 1;
    }

    private static int handleW(String word, Result result, int i) {
        if (contains(word, i, "WR")) {
            result.append('R');
            return i + 2;
        }
        if (i == 0 && (vowel(at(word, i + 1)) || contains(word, i, "WH"))) {
            result.append('A', 'F');
            return contains(word, i, "WH") ? i + 2 : i + 1;
        }
        if (vowel(at(word, i + 1))) {
            result.append('F');
        }
        return i + 1;
    }

    private static boolean vowel(char c) {
        return VOWELS.indexOf(c) >= 0;
    }

    private static char at(String word, int i) {
        return i >= 0 && i < word.length() ? word.charAt(i) : 0;
    }

    private static boolean contains(String word, int i, String token) {
        return word.startsWith(token, i);
    }

    public record Codes(String primary, String alternate) {
    }

    private static final class Result {
        private final StringBuilder primary = new StringBuilder(MAX);
        private final StringBuilder alternate = new StringBuilder(MAX);

        private void append(char value) {
            append(value, value);
        }

        private void append(char primaryChar, char alternateChar) {
            if (primary.length() < MAX) {
                primary.append(primaryChar);
            }
            if (alternate.length() < MAX) {
                alternate.append(alternateChar);
            }
        }

        private void append(String value) {
            append(value, value);
        }

        private void append(String primaryValue, String alternateValue) {
            appendSlice(primary, primaryValue);
            appendSlice(alternate, alternateValue);
        }

        private static void appendSlice(StringBuilder target, String value) {
            int room = MAX - target.length();
            if (room <= 0) {
                return;
            }
            target.append(value, 0, Math.min(value.length(), room));
        }

        private boolean full() {
            return primary.length() >= MAX && alternate.length() >= MAX;
        }
    }
}
