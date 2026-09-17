package com.mengzhihua.utils.common.net;


import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * BCP 47 language tag helpers.
 */
public final class LanguageTagUtil {

    private LanguageTagUtil() {
    }

    public static Parsed parse(String tag) {
        if (StringUtil.isBlank(tag)) {
            throw new IllegalArgumentException("language tag is blank");
        }
        String normalized = tag.trim().replace('_', '-');
        String[] parts = normalized.split("-");
        String language = parts[0].toLowerCase(Locale.ROOT);
        String script = "";
        String region = "";
        for (int i = 1; i < parts.length; i++) {
            String part = parts[i];
            if (part.length() == 4 && script.isEmpty()) {
                script = Character.toUpperCase(part.charAt(0)) + part.substring(1).toLowerCase(Locale.ROOT);
            } else if ((part.length() == 2 || part.length() == 3) && region.isEmpty()) {
                region = part.toUpperCase(Locale.ROOT);
            }
        }
        return new Parsed(language, script, region, toLanguageTag(language, script, region));
    }

    public static String toLanguageTag(String language, String script, String region) {
        StringBuilder builder = new StringBuilder(language == null ? "" : language.toLowerCase(Locale.ROOT));
        if (StringUtil.isNotBlank(script)) {
            builder.append('-').append(script);
        }
        if (StringUtil.isNotBlank(region)) {
            builder.append('-').append(region.toUpperCase(Locale.ROOT));
        }
        return builder.toString();
    }

    public record Parsed(String language, String script, String region, String tag) {
    }
}
