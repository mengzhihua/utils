package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Accept} header (RFC 9110) with q-values.
 */
public final class HttpAcceptUtil {

    private HttpAcceptUtil() {
    }

    public static List<MediaRange> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<MediaRange> ranges = new ArrayList<>();
        for (String part : header.split(",")) {
            String item = part.trim();
            if (item.isEmpty()) {
                continue;
            }
            String type = item;
            double q = 1.0;
            int semi = item.indexOf(';');
            if (semi >= 0) {
                type = item.substring(0, semi).trim();
                String rest = item.substring(semi + 1);
                for (String param : rest.split(";")) {
                    String pair = param.trim();
                    int eq = pair.indexOf('=');
                    if (eq > 0 && "q".equalsIgnoreCase(pair.substring(0, eq).trim())) {
                        try {
                            q = Double.parseDouble(pair.substring(eq + 1).trim());
                        } catch (NumberFormatException ex) {
                            q = 0;
                        }
                    }
                }
            }
            if (type.isEmpty()) {
                continue;
            }
            ranges.add(new MediaRange(type.toLowerCase(Locale.ROOT), q));
        }
        ranges.sort(Comparator.comparingDouble(MediaRange::q).reversed());
        return List.copyOf(ranges);
    }

    public static String negotiate(String accept, String... available) {
        List<MediaRange> ranges = parse(accept);
        if (available == null || available.length == 0) {
            return ranges.isEmpty() ? "" : ranges.get(0).type();
        }
        for (MediaRange range : ranges) {
            for (String option : available) {
                if (matches(range.type(), option.toLowerCase(Locale.ROOT))) {
                    return option;
                }
            }
        }
        return "";
    }

    private static boolean matches(String range, String option) {
        if ("*/*".equals(range) || range.equals(option)) {
            return true;
        }
        int slash = range.indexOf('/');
        if (slash < 0) {
            return false;
        }
        String type = range.substring(0, slash);
        String subtype = range.substring(slash + 1);
        int optionSlash = option.indexOf('/');
        if (optionSlash < 0) {
            return false;
        }
        return type.equals(option.substring(0, optionSlash)) && "*".equals(subtype);
    }

    public record MediaRange(String type, double q) {
    }
}
