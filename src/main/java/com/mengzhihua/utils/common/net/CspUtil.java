package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Content-Security-Policy} directive parser.
 */
public final class CspUtil {

    private CspUtil() {
    }

    public static Map<String, List<String>> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return Map.of();
        }
        Map<String, List<String>> directives = new LinkedHashMap<>();
        for (String part : header.split(";")) {
            String item = part.trim();
            if (item.isEmpty()) {
                continue;
            }
            String[] tokens = item.split("\\s+");
            String name = tokens[0].toLowerCase(Locale.ROOT);
            List<String> values = new ArrayList<>();
            for (int i = 1; i < tokens.length; i++) {
                values.add(tokens[i]);
            }
            directives.put(name, List.copyOf(values));
        }
        return Collections.unmodifiableMap(directives);
    }

    public static List<String> directive(String header, String name) {
        if (name == null) {
            return List.of();
        }
        List<String> values = parse(header).get(name.toLowerCase(Locale.ROOT));
        return values == null ? List.of() : values;
    }

    public static boolean allows(String header, String name, String source) {
        return directive(header, name).contains(source);
    }
}
