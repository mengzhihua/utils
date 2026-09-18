package com.mengzhihua.utils.common.net;


import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Reporting-Endpoints}.
 */
public final class ReportingEndpointsUtil {

    private static final Pattern ENTRY = Pattern.compile("([A-Za-z0-9_-]+)\\s*=\\s*\"([^\"]*)\"");

    private ReportingEndpointsUtil() {
    }

    public static Map<String, String> parse(String header) {
        Map<String, String> endpoints = new LinkedHashMap<>();
        if (StringUtil.isBlank(header)) {
            return Map.of();
        }
        Matcher matcher = ENTRY.matcher(header);
        while (matcher.find()) {
            endpoints.put(matcher.group(1).toLowerCase(Locale.ROOT), matcher.group(2));
        }
        return Map.copyOf(endpoints);
    }

    public static String firstName(String header) {
        if (StringUtil.isBlank(header)) {
            return "";
        }
        Matcher matcher = ENTRY.matcher(header);
        return matcher.find() ? matcher.group(1).toLowerCase(Locale.ROOT) : "";
    }

    public static String firstUrl(String header) {
        if (StringUtil.isBlank(header)) {
            return "";
        }
        Matcher matcher = ENTRY.matcher(header);
        return matcher.find() ? matcher.group(2) : "";
    }

    public static boolean has(String header, String name) {
        return name != null && parse(header).containsKey(name.toLowerCase(Locale.ROOT));
    }
}
