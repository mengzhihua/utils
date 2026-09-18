package com.mengzhihua.utils.common.i18n;


import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.net.LanguageTagUtil;

/**
 * Locale parse / display helpers. Tags accept {@code zh-CN} or {@code zh_CN}.
 */
public final class LocaleUtil {

    public static final List<Locale> SUPPORTED = List.of(
            Locale.ENGLISH,
            Locale.US,
            Locale.SIMPLIFIED_CHINESE,
            Locale.TRADITIONAL_CHINESE,
            Locale.JAPANESE,
            Locale.GERMANY,
            Locale.FRANCE,
            Locale.KOREA);

    private LocaleUtil() {
    }

    public static Locale parse(String tag) {
        if (StringUtil.isBlank(tag)) {
            return Locale.ENGLISH;
        }
        LanguageTagUtil.Parsed parsed = LanguageTagUtil.parse(tag);
        Locale.Builder builder = new Locale.Builder().setLanguage(parsed.language());
        if (StringUtil.isNotBlank(parsed.script())) {
            builder.setScript(parsed.script());
        }
        if (StringUtil.isNotBlank(parsed.region())) {
            builder.setRegion(parsed.region());
        }
        return builder.build();
    }

    public static String toTag(Locale locale) {
        if (locale == null) {
            return "en";
        }
        return locale.toLanguageTag();
    }

    public static String displayName(String tag, String inLocale) {
        Locale locale = parse(tag);
        Locale view = parse(inLocale);
        return locale.getDisplayName(view);
    }

    public static String languageName(String tag, String inLocale) {
        return parse(tag).getDisplayLanguage(parse(inLocale));
    }

    public static String countryName(String tag, String inLocale) {
        return parse(tag).getDisplayCountry(parse(inLocale));
    }

    public static boolean supported(String tag) {
        Locale locale = parse(tag);
        for (Locale item : SUPPORTED) {
            if (item.getLanguage().equals(locale.getLanguage())) {
                return true;
            }
        }
        return false;
    }

    public static List<String> supportedTags() {
        Set<String> tags = new LinkedHashSet<>();
        for (Locale locale : SUPPORTED) {
            tags.add(locale.toLanguageTag());
        }
        return new ArrayList<>(tags);
    }

    public static Locale defaultLocale() {
        return Locale.ENGLISH;
    }
}
