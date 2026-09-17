package com.mengzhihua.utils.common.text;


import java.util.ArrayList;
import java.util.List;

/**
 * Emoji helpers (Hutool-style, Java 21 {@link Character#isEmoji(int)}).
 */
public final class EmojiUtil {

    private EmojiUtil() {
    }

    public static boolean contains(String text) {
        return count(text) > 0;
    }

    public static int count(String text) {
        return extract(text).size();
    }

    public static String remove(String text) {
        if (text == null || text.isEmpty()) {
            return text == null ? "" : text;
        }
        StringBuilder builder = new StringBuilder(text.length());
        text.codePoints().forEach(cp -> {
            if (!Character.isEmoji(cp)) {
                builder.appendCodePoint(cp);
            }
        });
        return builder.toString();
    }

    public static List<String> extract(String text) {
        List<String> emojis = new ArrayList<>();
        if (text == null || text.isEmpty()) {
            return emojis;
        }
        text.codePoints().forEach(cp -> {
            if (Character.isEmoji(cp)) {
                emojis.add(Character.toString(cp));
            }
        });
        return emojis;
    }
}
