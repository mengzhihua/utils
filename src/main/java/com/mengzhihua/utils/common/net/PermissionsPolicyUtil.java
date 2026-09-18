package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Permissions-Policy} directive parser.
 */
public final class PermissionsPolicyUtil {

    private static final Pattern DIRECTIVE = Pattern.compile("([A-Za-z0-9-]+)=\\(([^)]*)\\)");

    private PermissionsPolicyUtil() {
    }

    public static Map<String, List<String>> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return Map.of();
        }
        Map<String, List<String>> directives = new LinkedHashMap<>();
        Matcher matcher = DIRECTIVE.matcher(header);
        while (matcher.find()) {
            List<String> sources = new ArrayList<>();
            String body = matcher.group(2).trim();
            if (!body.isEmpty()) {
                for (String part : body.split("\\s+")) {
                    if (!part.isEmpty()) {
                        sources.add(part);
                    }
                }
            }
            directives.put(matcher.group(1).toLowerCase(Locale.ROOT), List.copyOf(sources));
        }
        return Collections.unmodifiableMap(directives);
    }

    public static List<String> allowlist(String header, String feature) {
        if (feature == null) {
            return List.of();
        }
        List<String> values = parse(header).get(feature.toLowerCase(Locale.ROOT));
        return values == null ? List.of() : values;
    }

    public static boolean disabled(String header, String feature) {
        return parse(header).containsKey(feature == null ? "" : feature.toLowerCase(Locale.ROOT))
                && allowlist(header, feature).isEmpty();
    }
}
