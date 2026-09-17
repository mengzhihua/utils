package com.mengzhihua.utils.util;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RFC 6570 URI Template Level 1 ({@code {var}} simple string expansion).
 */
public final class UriTemplateUtil {

    private static final Pattern VAR = Pattern.compile("\\{([A-Za-z0-9_]+)}");

    private UriTemplateUtil() {
    }

    public static String expand(String template, Map<String, ?> variables) {
        if (template == null) {
            return "";
        }
        Map<String, ?> vars = variables == null ? Map.of() : variables;
        Matcher matcher = VAR.matcher(template);
        StringBuilder out = new StringBuilder();
        while (matcher.find()) {
            Object value = vars.get(matcher.group(1));
            String encoded = value == null ? "" : percent(String.valueOf(value));
            matcher.appendReplacement(out, Matcher.quoteReplacement(encoded));
        }
        matcher.appendTail(out);
        return out.toString();
    }

    public static Map<String, String> match(String template, String uri) {
        if (template == null || uri == null) {
            return Map.of();
        }
        Matcher names = VAR.matcher(template);
        StringBuilder regex = new StringBuilder("^");
        int last = 0;
        java.util.List<String> keys = new java.util.ArrayList<>();
        while (names.find()) {
            regex.append(Pattern.quote(template.substring(last, names.start())));
            regex.append("([^/]+)");
            keys.add(names.group(1));
            last = names.end();
        }
        regex.append(Pattern.quote(template.substring(last))).append('$');
        Matcher matched = Pattern.compile(regex.toString()).matcher(uri);
        if (!matched.matches()) {
            return Map.of();
        }
        Map<String, String> values = new LinkedHashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            values.put(keys.get(i), matched.group(i + 1));
        }
        return values;
    }

    private static String percent(String value) {
        StringBuilder builder = new StringBuilder();
        for (byte b : value.getBytes(StandardCharsets.UTF_8)) {
            int n = b & 0xff;
            if ((n >= 'A' && n <= 'Z') || (n >= 'a' && n <= 'z') || (n >= '0' && n <= '9')
                    || n == '-' || n == '_' || n == '.' || n == '~') {
                builder.append((char) n);
            } else {
                builder.append('%').append(String.format("%02X", n));
            }
        }
        return builder.toString();
    }
}
