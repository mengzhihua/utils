package com.mengzhihua.utils.common.text;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Caverphone 2.0 (David Hood / Commons Codec). {@code Stevenson} → {@code STFNSN1111}.
 */
public final class CaverphoneUtil {

    private static final String TEN_1 = "1111111111";

    private CaverphoneUtil() {
    }

    public static String encode(String text) {
        if (StringUtil.isBlank(text)) {
            return TEN_1;
        }
        String word = text.toLowerCase(Locale.ENGLISH).replaceAll("[^a-z]", "");
        if (word.isEmpty()) {
            return TEN_1;
        }
        word = word.replaceAll("e$", "");
        word = word.replaceAll("^cough", "cou2f");
        word = word.replaceAll("^rough", "rou2f");
        word = word.replaceAll("^tough", "tou2f");
        word = word.replaceAll("^enough", "enou2f");
        word = word.replaceAll("^trough", "trou2f");
        word = word.replaceAll("^gn", "2n");
        word = word.replaceAll("mb$", "m2");
        word = word.replaceAll("cq", "2q");
        word = word.replaceAll("ci", "si");
        word = word.replaceAll("ce", "se");
        word = word.replaceAll("cy", "sy");
        word = word.replaceAll("tch", "2ch");
        word = word.replaceAll("c", "k");
        word = word.replaceAll("q", "k");
        word = word.replaceAll("x", "k");
        word = word.replaceAll("v", "f");
        word = word.replaceAll("dg", "2g");
        word = word.replaceAll("tio", "sio");
        word = word.replaceAll("tia", "sia");
        word = word.replaceAll("d", "t");
        word = word.replaceAll("ph", "fh");
        word = word.replaceAll("b", "p");
        word = word.replaceAll("sh", "s2");
        word = word.replaceAll("z", "s");
        word = word.replaceAll("^[aeiou]", "A");
        word = word.replaceAll("[aeiou]", "3");
        word = word.replaceAll("j", "y");
        word = word.replaceAll("^y3", "Y3");
        word = word.replaceAll("^y", "A");
        word = word.replaceAll("y", "3");
        word = word.replaceAll("3gh3", "3kh3");
        word = word.replaceAll("gh", "22");
        word = word.replaceAll("g", "k");
        word = word.replaceAll("s+", "S");
        word = word.replaceAll("t+", "T");
        word = word.replaceAll("p+", "P");
        word = word.replaceAll("k+", "K");
        word = word.replaceAll("f+", "F");
        word = word.replaceAll("m+", "M");
        word = word.replaceAll("n+", "N");
        word = word.replaceAll("w3", "W3");
        word = word.replaceAll("wh3", "Wh3");
        word = word.replaceAll("w$", "3");
        word = word.replaceAll("w", "2");
        word = word.replaceAll("^h", "A");
        word = word.replaceAll("h", "2");
        word = word.replaceAll("r3", "R3");
        word = word.replaceAll("r$", "3");
        word = word.replaceAll("r", "2");
        word = word.replaceAll("l3", "L3");
        word = word.replaceAll("l$", "3");
        word = word.replaceAll("l", "2");
        word = word.replaceAll("2", "");
        word = word.replaceAll("3$", "A");
        word = word.replaceAll("3", "");
        word = word + TEN_1;
        return word.substring(0, 10);
    }

    public static boolean similar(String left, String right) {
        return encode(left).equals(encode(right));
    }
}
