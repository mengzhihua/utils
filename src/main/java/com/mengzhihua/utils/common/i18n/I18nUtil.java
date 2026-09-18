package com.mengzhihua.utils.common.i18n;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * UTF-8 resource bundle lookup with {@link MessageFormat} placeholders.
 * Basename {@code i18n/messages}.
 */
public final class I18nUtil {

    public static final String BASENAME = "i18n/messages";

    private static final ResourceBundle.Control UTF8 = new Utf8Control();

    private I18nUtil() {
    }

    public static String get(String key) {
        return get(key, LocaleUtil.defaultLocale());
    }

    public static String get(String key, String locale, Object... args) {
        return get(key, LocaleUtil.parse(locale), args);
    }

    public static String get(String key, Locale locale, Object... args) {
        if (StringUtil.isBlank(key)) {
            return "";
        }
        Locale resolved = locale == null ? LocaleUtil.defaultLocale() : locale;
        String pattern = bundle(resolved).getString(key);
        if (args == null || args.length == 0) {
            return pattern;
        }
        MessageFormat format = new MessageFormat(pattern, resolved);
        return format.format(args);
    }

    public static String getOrDefault(String key, Locale locale, String fallback, Object... args) {
        try {
            return get(key, locale, args);
        } catch (Exception ex) {
            if (args == null || args.length == 0 || fallback == null) {
                return fallback == null ? key : fallback;
            }
            return new MessageFormat(fallback, locale == null ? LocaleUtil.defaultLocale() : locale).format(args);
        }
    }

    public static boolean contains(String key, String locale) {
        try {
            bundle(LocaleUtil.parse(locale)).getString(key);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static List<String> keys(Locale locale) {
        List<String> keys = new ArrayList<>(Collections.list(bundle(locale).getKeys()));
        keys.sort(String::compareTo);
        return List.copyOf(keys);
    }

    public static List<String> availableLanguages() {
        return List.of("en", "zh", "ja", "de", "fr", "ko", "es");
    }

    public static ResourceBundle bundle(Locale locale) {
        return ResourceBundle.getBundle(BASENAME, locale == null ? LocaleUtil.defaultLocale() : locale,
                I18nUtil.class.getClassLoader(), UTF8);
    }

    private static final class Utf8Control extends ResourceBundle.Control {
        @Override
        public List<String> getFormats(String baseName) {
            return FORMAT_PROPERTIES;
        }

        @Override
        public Locale getFallbackLocale(String baseName, Locale locale) {
            return Locale.ENGLISH.equals(locale) ? null : Locale.ENGLISH;
        }

        @Override
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
                boolean reload) throws IOException {
            if (!FORMAT_PROPERTIES.contains(format)) {
                return null;
            }
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                if (stream == null) {
                    return null;
                }
                try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                    return new PropertyResourceBundle(reader);
                }
            }
        }
    }
}
