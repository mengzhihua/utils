package com.mengzhihua.utils.common.net;


import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Warning} (RFC 7234, obsolete in RFC 9111).
 */
public final class WarningUtil {

    private static final Pattern VALUE = Pattern.compile(
            "^(\\d{3})\\s+(\\S+)\\s+\"([^\"]*)\"(?:\\s+\"([^\"]*)\")?$");
    private static final Set<Integer> KNOWN = Set.of(110, 111, 112, 113, 199, 214, 299);

    public record WarningValue(int code, String agent, String text, String date) {
        public boolean stale() {
            return code == 110;
        }
    }

    private WarningUtil() {
    }

    public static WarningValue parse(String header) {
        if (StringUtil.isBlank(header)) {
            throw new IllegalArgumentException("Warning is blank");
        }
        Matcher matcher = VALUE.matcher(header.trim());
        if (!matcher.matches()) {
            throw new IllegalArgumentException("invalid Warning: " + header);
        }
        return new WarningValue(
                Integer.parseInt(matcher.group(1)),
                matcher.group(2),
                matcher.group(3),
                matcher.group(4) == null ? "" : matcher.group(4));
    }

    public static boolean known(String header) {
        return KNOWN.contains(parse(header).code());
    }

    public static boolean stale(String header) {
        return parse(header).stale();
    }

    public static String codeName(int code) {
        return switch (code) {
            case 110 -> "Response is Stale";
            case 111 -> "Revalidation Failed";
            case 112 -> "Disconnected Operation";
            case 113 -> "Heuristic Expiration";
            case 199 -> "Miscellaneous Warning";
            case 214 -> "Transformation Applied";
            case 299 -> "Miscellaneous Persistent Warning";
            default -> String.valueOf(code).toLowerCase(Locale.ROOT);
        };
    }
}
