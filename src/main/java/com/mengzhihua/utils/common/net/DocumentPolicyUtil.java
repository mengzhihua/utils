package com.mengzhihua.utils.common.net;


import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Document-Policy} dictionary parser.
 */
public final class DocumentPolicyUtil {

    private static final Pattern DIRECTIVE = Pattern.compile("([A-Za-z0-9-]+)(?:=([^,]+))?");

    private DocumentPolicyUtil() {
    }

    public static Map<String, String> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return Map.of();
        }
        Map<String, String> directives = new LinkedHashMap<>();
        Matcher matcher = DIRECTIVE.matcher(header);
        while (matcher.find()) {
            String name = matcher.group(1).toLowerCase(Locale.ROOT);
            String value = matcher.group(2) == null ? "" : matcher.group(2).trim();
            directives.put(name, value);
        }
        return Collections.unmodifiableMap(directives);
    }

    public static String first(String header) {
        Map<String, String> directives = parse(header);
        return directives.isEmpty() ? "" : directives.keySet().iterator().next();
    }

    public static String value(String header, String name) {
        if (name == null) {
            return "";
        }
        String found = parse(header).get(name.toLowerCase(Locale.ROOT));
        return found == null ? "" : found;
    }

    public static boolean has(String header, String name) {
        return name != null && parse(header).containsKey(name.toLowerCase(Locale.ROOT));
    }
}
