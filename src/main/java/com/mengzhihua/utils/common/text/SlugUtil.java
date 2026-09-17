package com.mengzhihua.utils.common.text;


import java.text.Normalizer;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * URL slug from mixed Latin / CJK text.
 */
public final class SlugUtil {

    private SlugUtil() {
    }

    public static String of(String text) {
        if (StringUtil.isBlank(text)) {
            return "";
        }
        String normalized = Normalizer.normalize(text.trim(), Normalizer.Form.NFKD);
        StringBuilder builder = new StringBuilder(normalized.length());
        for (int i = 0; i < normalized.length(); i++) {
            char c = normalized.charAt(i);
            if (Character.getType(c) == Character.NON_SPACING_MARK) {
                continue;
            }
            if (Character.isLetterOrDigit(c) || isCjk(c)) {
                builder.append(Character.toLowerCase(c));
            } else {
                builder.append('-');
            }
        }
        return builder.toString().replaceAll("-{2,}", "-").replaceAll("^-|-$", "");
    }

    private static boolean isCjk(char c) {
        return c >= 0x4e00 && c <= 0x9fff;
    }
}
