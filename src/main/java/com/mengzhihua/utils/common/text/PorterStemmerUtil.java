package com.mengzhihua.utils.common.text;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Porter stemmer (1980 / Commons Text). {@code relational} → {@code relat}.
 */
public final class PorterStemmerUtil {

    private PorterStemmerUtil() {
    }

    public static String stem(String word) {
        if (StringUtil.isBlank(word)) {
            return "";
        }
        char[] letters = word.toLowerCase(Locale.ROOT).replaceAll("[^a-z]", "").toCharArray();
        if (letters.length <= 2) {
            return new String(letters);
        }
        return new Engine(letters).stem();
    }

    private static final class Engine {
        private char[] b;
        private int k;
        private int j;

        private Engine(char[] b) {
            this.b = b;
            this.k = b.length - 1;
        }

        private String stem() {
            if (k > 1) {
                step1();
                step2();
                step3();
                step4();
                step5();
                step6();
            }
            return new String(b, 0, k + 1);
        }

        private boolean cons(int i) {
            return switch (b[i]) {
                case 'a', 'e', 'i', 'o', 'u' -> false;
                case 'y' -> i == 0 || !cons(i - 1);
                default -> true;
            };
        }

        private int m() {
            int n = 0;
            int i = 0;
            while (true) {
                if (i > j) {
                    return n;
                }
                if (!cons(i)) {
                    break;
                }
                i++;
            }
            i++;
            while (true) {
                while (true) {
                    if (i > j) {
                        return n;
                    }
                    if (cons(i)) {
                        break;
                    }
                    i++;
                }
                i++;
                n++;
                while (true) {
                    if (i > j) {
                        return n;
                    }
                    if (!cons(i)) {
                        break;
                    }
                    i++;
                }
                i++;
            }
        }

        private boolean vowelInStem() {
            for (int i = 0; i <= j; i++) {
                if (!cons(i)) {
                    return true;
                }
            }
            return false;
        }

        private boolean doubleC(int i) {
            return i >= 1 && b[i] == b[i - 1] && cons(i);
        }

        private boolean cvc(int i) {
            if (i < 2 || !cons(i) || cons(i - 1) || !cons(i - 2)) {
                return false;
            }
            char ch = b[i];
            return ch != 'w' && ch != 'x' && ch != 'y';
        }

        private boolean ends(String s) {
            int length = s.length();
            int offset = k - length + 1;
            if (offset < 0) {
                return false;
            }
            for (int i = 0; i < length; i++) {
                if (b[offset + i] != s.charAt(i)) {
                    return false;
                }
            }
            j = k - length;
            return true;
        }

        private void setTo(String s) {
            int length = s.length();
            int offset = j + 1;
            for (int i = 0; i < length; i++) {
                b[offset + i] = s.charAt(i);
            }
            k = j + length;
        }

        private void r(String s) {
            if (m() > 0) {
                setTo(s);
            }
        }

        private void step1() {
            if (b[k] == 's') {
                if (ends("sses")) {
                    k -= 2;
                } else if (ends("ies")) {
                    setTo("i");
                } else if (b[k - 1] != 's') {
                    k--;
                }
            }
            if (ends("eed")) {
                if (m() > 0) {
                    k--;
                }
            } else if ((ends("ed") || ends("ing")) && vowelInStem()) {
                k = j;
                if (ends("at")) {
                    setTo("ate");
                } else if (ends("bl")) {
                    setTo("ble");
                } else if (ends("iz")) {
                    setTo("ize");
                } else if (doubleC(k)) {
                    k--;
                    char ch = b[k];
                    if (ch == 'l' || ch == 's' || ch == 'z') {
                        k++;
                    }
                } else if (m() == 1 && cvc(k)) {
                    setTo("e");
                }
            }
        }

        private void step2() {
            if (ends("y") && vowelInStem()) {
                b[k] = 'i';
            }
        }

        private void step3() {
            if (k == 0) {
                return;
            }
            switch (b[k - 1]) {
                case 'a' -> {
                    if (ends("ational")) {
                        r("ate");
                    } else if (ends("tional")) {
                        r("tion");
                    }
                }
                case 'c' -> {
                    if (ends("enci")) {
                        r("ence");
                    } else if (ends("anci")) {
                        r("ance");
                    }
                }
                case 'e' -> {
                    if (ends("izer")) {
                        r("ize");
                    }
                }
                case 'l' -> {
                    if (ends("bli")) {
                        r("ble");
                    } else if (ends("alli")) {
                        r("al");
                    } else if (ends("entli")) {
                        r("ent");
                    } else if (ends("eli")) {
                        r("e");
                    } else if (ends("ousli")) {
                        r("ous");
                    }
                }
                case 'o' -> {
                    if (ends("ization")) {
                        r("ize");
                    } else if (ends("ation")) {
                        r("ate");
                    } else if (ends("ator")) {
                        r("ate");
                    }
                }
                case 's' -> {
                    if (ends("alism")) {
                        r("al");
                    } else if (ends("iveness")) {
                        r("ive");
                    } else if (ends("fulness")) {
                        r("ful");
                    } else if (ends("ousness")) {
                        r("ous");
                    }
                }
                case 't' -> {
                    if (ends("aliti")) {
                        r("al");
                    } else if (ends("iviti")) {
                        r("ive");
                    } else if (ends("biliti")) {
                        r("ble");
                    }
                }
                case 'g' -> {
                    if (ends("logi")) {
                        r("log");
                    }
                }
                default -> {
                }
            }
        }

        private void step4() {
            switch (b[k]) {
                case 'e' -> {
                    if (ends("icate")) {
                        r("ic");
                    } else if (ends("ative")) {
                        r("");
                    } else if (ends("alize")) {
                        r("al");
                    }
                }
                case 'i' -> {
                    if (ends("iciti")) {
                        r("ic");
                    }
                }
                case 'l' -> {
                    if (ends("ical")) {
                        r("ic");
                    } else if (ends("ful")) {
                        r("");
                    }
                }
                case 's' -> {
                    if (ends("ness")) {
                        r("");
                    }
                }
                default -> {
                }
            }
        }

        private void step5() {
            if (k == 0) {
                return;
            }
            switch (b[k - 1]) {
                case 'a' -> {
                    if (ends("al")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'c' -> {
                    if (ends("ance") || ends("ence")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'e' -> {
                    if (ends("er")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'i' -> {
                    if (ends("ic")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'l' -> {
                    if (ends("able") || ends("ible")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'n' -> {
                    if (ends("ant") || ends("ement") || ends("ment") || ends("ent")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'o' -> {
                    if (ends("ion") && j >= 0 && (b[j] == 's' || b[j] == 't')) {
                        /* fall through */
                    } else if (ends("ou")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 's' -> {
                    if (ends("ism")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 't' -> {
                    if (ends("ate") || ends("iti")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'u' -> {
                    if (ends("ous")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'v' -> {
                    if (ends("ive")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                case 'z' -> {
                    if (ends("ize")) {
                        /* fall through */
                    } else {
                        return;
                    }
                }
                default -> {
                    return;
                }
            }
            if (m() > 1) {
                k = j;
            }
        }

        private void step6() {
            j = k;
            if (b[k] == 'e') {
                int a = m();
                if (a > 1 || a == 1 && !cvc(k - 1)) {
                    k--;
                }
            }
            if (b[k] == 'l' && doubleC(k) && m() > 1) {
                k--;
            }
        }
    }
}
