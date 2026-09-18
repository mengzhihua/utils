package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Alt-Svc} parser (RFC 7838).
 * Sample {@code h3=":443"; ma=86400, h2=":443"; ma=2592000}.
 */
public final class AltSvcUtil {

    public record Service(String protocol, String authority, Map<String, String> params) {
        public Service {
            protocol = protocol == null ? "" : protocol;
            authority = authority == null ? "" : authority;
            params = params == null ? Map.of() : Map.copyOf(params);
        }

        public String maxAge() {
            return params.getOrDefault("ma", "");
        }
    }

    private AltSvcUtil() {
    }

    public static boolean isClear(String header) {
        return header != null && "clear".equalsIgnoreCase(header.trim());
    }

    public static List<Service> parse(String header) {
        if (StringUtil.isBlank(header) || isClear(header)) {
            return List.of();
        }
        List<Service> services = new ArrayList<>();
        for (String part : split(header, ',')) {
            Service service = parseService(part);
            if (service != null && !service.protocol().isEmpty()) {
                services.add(service);
            }
        }
        return List.copyOf(services);
    }

    public static String firstProtocol(String header) {
        List<Service> services = parse(header);
        return services.isEmpty() ? "" : services.get(0).protocol();
    }

    public static boolean has(String header, String protocol) {
        if (protocol == null) {
            return false;
        }
        String wanted = protocol.trim().toLowerCase(Locale.ROOT);
        for (Service service : parse(header)) {
            if (service.protocol().equals(wanted)) {
                return true;
            }
        }
        return false;
    }

    private static Service parseService(String part) {
        List<String> tokens = split(part, ';');
        if (tokens.isEmpty()) {
            return null;
        }
        String head = tokens.get(0).trim();
        int eq = head.indexOf('=');
        if (eq < 0) {
            return null;
        }
        String protocol = head.substring(0, eq).trim().toLowerCase(Locale.ROOT);
        String authority = unquote(head.substring(eq + 1).trim());
        Map<String, String> params = new LinkedHashMap<>();
        for (int i = 1; i < tokens.size(); i++) {
            String token = tokens.get(i).trim();
            if (token.isEmpty()) {
                continue;
            }
            int paramEq = token.indexOf('=');
            if (paramEq < 0) {
                params.put(token.toLowerCase(Locale.ROOT), "");
                continue;
            }
            params.put(token.substring(0, paramEq).trim().toLowerCase(Locale.ROOT),
                    unquote(token.substring(paramEq + 1).trim()));
        }
        return new Service(protocol, authority, params);
    }

    private static List<String> split(String text, char separator) {
        List<String> parts = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            if (ch == '"') {
                quoted = !quoted;
                current.append(ch);
            } else if (ch == separator && !quoted) {
                parts.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        if (!current.isEmpty()) {
            parts.add(current.toString());
        }
        return parts;
    }

    private static String unquote(String value) {
        if (value.length() >= 2 && value.charAt(0) == '"' && value.charAt(value.length() - 1) == '"') {
            return value.substring(1, value.length() - 1);
        }
        return value;
    }
}
