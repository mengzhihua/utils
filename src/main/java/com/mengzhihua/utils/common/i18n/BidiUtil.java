package com.mengzhihua.utils.common.i18n;


import java.text.Bidi;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Bidirectional text helpers ({@link Bidi}).
 */
public final class BidiUtil {

    private BidiUtil() {
    }

    public static boolean rtl(String text) {
        if (StringUtil.isBlank(text)) {
            return false;
        }
        Bidi bidi = new Bidi(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
        return !bidi.baseIsLeftToRight();
    }

    public static boolean ltr(String text) {
        if (StringUtil.isBlank(text)) {
            return true;
        }
        return new Bidi(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT).isLeftToRight();
    }

    public static boolean mixed(String text) {
        if (StringUtil.isBlank(text)) {
            return false;
        }
        Bidi bidi = new Bidi(text, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
        return bidi.isMixed();
    }

    public static String direction(String text) {
        if (rtl(text)) {
            return "rtl";
        }
        if (mixed(text)) {
            return "mixed";
        }
        return "ltr";
    }

    public static boolean localeRtl(String tag) {
        String language = LocaleUtil.parse(tag).getLanguage();
        return "ar".equals(language) || "he".equals(language) || "fa".equals(language) || "ur".equals(language);
    }
}
