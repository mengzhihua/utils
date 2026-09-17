package com.mengzhihua.utils.common.net;


import java.util.Locale;

/**
 * Lightweight User-Agent checks for mobile / WeChat / bots.
 */
public final class UserAgentUtil {

    private UserAgentUtil() {
    }

    public static boolean isMobile(String userAgent) {
        String ua = normalize(userAgent);
        return ua.contains("mobile") || ua.contains("android") || ua.contains("iphone") || ua.contains("ipad");
    }

    public static boolean isWeChat(String userAgent) {
        return normalize(userAgent).contains("micromessenger");
    }

    public static boolean isIos(String userAgent) {
        String ua = normalize(userAgent);
        return ua.contains("iphone") || ua.contains("ipad") || ua.contains("ipod");
    }

    public static boolean isAndroid(String userAgent) {
        return normalize(userAgent).contains("android");
    }

    public static boolean isBot(String userAgent) {
        String ua = normalize(userAgent);
        return ua.contains("bot") || ua.contains("spider") || ua.contains("crawler") || ua.contains("curl") || ua.contains("wget");
    }

    private static String normalize(String userAgent) {
        return userAgent == null ? "" : userAgent.toLowerCase(Locale.ROOT);
    }
}
