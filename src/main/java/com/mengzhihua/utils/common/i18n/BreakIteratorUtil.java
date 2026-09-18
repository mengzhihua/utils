package com.mengzhihua.utils.common.i18n;


import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Locale-aware word / sentence boundaries ({@link BreakIterator}).
 */
public final class BreakIteratorUtil {

    private BreakIteratorUtil() {
    }

    public static List<String> words(String locale, String text) {
        return split(BreakIterator.getWordInstance(LocaleUtil.parse(locale)), text, true);
    }

    public static List<String> sentences(String locale, String text) {
        return split(BreakIterator.getSentenceInstance(LocaleUtil.parse(locale)), text, false);
    }

    public static int wordCount(String locale, String text) {
        return words(locale, text).size();
    }

    private static List<String> split(BreakIterator iterator, String text, boolean lettersOnly) {
        if (StringUtil.isBlank(text)) {
            return List.of();
        }
        iterator.setText(text);
        List<String> parts = new ArrayList<>();
        int start = iterator.first();
        for (int end = iterator.next(); end != BreakIterator.DONE; start = end, end = iterator.next()) {
            String token = text.substring(start, end).trim();
            if (token.isEmpty()) {
                continue;
            }
            if (lettersOnly && token.chars().noneMatch(Character::isLetterOrDigit)) {
                continue;
            }
            parts.add(token);
        }
        return List.copyOf(parts);
    }
}
