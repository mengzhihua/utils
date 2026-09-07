package com.mengzhihua.utils.util;

import java.util.regex.Pattern;

/**
 * Common format validators.
 */
public final class RegexUtil {

    public static final Pattern MOBILE = Pattern.compile("^1[3-9]\\d{9}$");
    public static final Pattern EMAIL = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    public static final Pattern ID_CARD = Pattern.compile("^\\d{17}[\\dXx]$");
    public static final Pattern IPV4 = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d{2}|[1-9]?\\d)$");
    public static final Pattern URL = Pattern.compile("^(https?://)[\\w.-]+(?:\\.[\\w.-]+)+(?:[/#?].*)?$", Pattern.CASE_INSENSITIVE);
    public static final Pattern USERNAME = Pattern.compile("^[A-Za-z][A-Za-z0-9_]{3,31}$");

    private RegexUtil() {
    }

    public static boolean isMatch(Pattern pattern, String value) {
        return value != null && pattern != null && pattern.matcher(value).matches();
    }

    public static boolean isMobile(String value) {
        return isMatch(MOBILE, value);
    }

    public static boolean isEmail(String value) {
        return isMatch(EMAIL, value);
    }

    public static boolean isIdCard(String value) {
        return isMatch(ID_CARD, value);
    }

    public static boolean isIpv4(String value) {
        return isMatch(IPV4, value);
    }

    public static boolean isUrl(String value) {
        return isMatch(URL, value);
    }

    public static boolean isUsername(String value) {
        return isMatch(USERNAME, value);
    }
}
