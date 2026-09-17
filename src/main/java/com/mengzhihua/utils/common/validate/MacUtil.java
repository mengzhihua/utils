package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.regex.Pattern;

/**
 * MAC address validate / normalize.
 */
public final class MacUtil {

    private static final Pattern MAC = Pattern.compile("(?i)^([0-9a-f]{2}[:-]){5}[0-9a-f]{2}$");

    private MacUtil() {
    }

    public static boolean isValid(String mac) {
        return mac != null && MAC.matcher(mac.trim()).matches();
    }

    public static String normalize(String mac) {
        if (!isValid(mac)) {
            throw new IllegalArgumentException("invalid mac: " + mac);
        }
        String hex = mac.replace(":", "").replace("-", "").toLowerCase(Locale.ROOT);
        StringBuilder builder = new StringBuilder(17);
        for (int i = 0; i < 12; i += 2) {
            if (i > 0) {
                builder.append(':');
            }
            builder.append(hex, i, i + 2);
        }
        return builder.toString();
    }
}
