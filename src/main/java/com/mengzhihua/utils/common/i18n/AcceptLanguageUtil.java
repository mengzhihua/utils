package com.mengzhihua.utils.common.i18n;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Accept-Language} negotiator.
 */
public final class AcceptLanguageUtil {

    private AcceptLanguageUtil() {
    }

    public static List<Weighted> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<Weighted> items = new ArrayList<>();
        for (String part : header.split(",")) {
            String token = part.trim();
            if (token.isEmpty()) {
                continue;
            }
            String tag = token;
            double quality = 1.0;
            int semi = token.indexOf(';');
            if (semi >= 0) {
                tag = token.substring(0, semi).trim();
                String rest = token.substring(semi + 1);
                int q = rest.toLowerCase(Locale.ROOT).indexOf("q=");
                if (q >= 0) {
                    try {
                        quality = Double.parseDouble(rest.substring(q + 2).trim());
                    } catch (NumberFormatException ignored) {
                        quality = 0;
                    }
                }
            }
            if ("*".equals(tag)) {
                items.add(new Weighted("*", Locale.ROOT, quality));
                continue;
            }
            try {
                Locale locale = LocaleUtil.parse(tag);
                items.add(new Weighted(locale.toLanguageTag(), locale, quality));
            } catch (RuntimeException ignored) {
                // skip malformed tags
            }
        }
        items.sort(Comparator.comparingDouble(Weighted::quality).reversed());
        return List.copyOf(items);
    }

    public static String first(String header) {
        List<Weighted> items = parse(header);
        return items.isEmpty() ? "" : items.get(0).tag();
    }

    public static Locale negotiate(String header, List<Locale> supported) {
        List<Locale> candidates = supported == null || supported.isEmpty() ? LocaleUtil.SUPPORTED : supported;
        for (Weighted item : parse(header)) {
            if ("*".equals(item.tag())) {
                return candidates.get(0);
            }
            for (Locale locale : candidates) {
                if (locale.getLanguage().equals(item.locale().getLanguage())) {
                    if (locale.getCountry().isEmpty()
                            || item.locale().getCountry().isEmpty()
                            || locale.getCountry().equals(item.locale().getCountry())) {
                        return locale;
                    }
                }
            }
        }
        return LocaleUtil.defaultLocale();
    }

    public record Weighted(String tag, Locale locale, double quality) {
    }
}
