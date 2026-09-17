package com.mengzhihua.utils.common.text;


import java.text.Normalizer;

/**
 * Accent / diacritic helpers (Apache Commons Lang {@code StringUtils.stripAccents}).
 */
public final class AccentUtil {

    private AccentUtil() {
    }

    public static String strip(String text) {
        if (text == null || text.isEmpty()) {
            return text == null ? "" : text;
        }
        String normalized = Normalizer.normalize(text, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}+", "");
    }

    public static boolean isNormalized(String text) {
        return text != null && text.equals(Normalizer.normalize(text, Normalizer.Form.NFC));
    }

    public static String nfc(String text) {
        return text == null ? "" : Normalizer.normalize(text, Normalizer.Form.NFC);
    }
}
