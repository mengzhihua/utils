package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.regex.Pattern;

/**
 * DOI (ISO 26324). Handbook example {@code 10.1000/182}.
 */
public final class DoiUtil {

    private static final Pattern DOI = Pattern.compile("^10\\.\\d{4,9}/\\S+$", Pattern.CASE_INSENSITIVE);

    private DoiUtil() {
    }

    public static boolean isValid(String value) {
        return DOI.matcher(normalize(value)).matches();
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String compact = value.trim();
        String lower = compact.toLowerCase(Locale.ROOT);
        if (lower.startsWith("https://doi.org/")) {
            compact = compact.substring("https://doi.org/".length());
        } else if (lower.startsWith("http://doi.org/")) {
            compact = compact.substring("http://doi.org/".length());
        } else if (lower.startsWith("https://dx.doi.org/")) {
            compact = compact.substring("https://dx.doi.org/".length());
        } else if (lower.startsWith("http://dx.doi.org/")) {
            compact = compact.substring("http://dx.doi.org/".length());
        } else if (lower.startsWith("doi:")) {
            compact = compact.substring(4);
        }
        return compact.trim();
    }
}
