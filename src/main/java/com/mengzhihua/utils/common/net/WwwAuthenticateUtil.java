package com.mengzhihua.utils.common.net;


import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code WWW-Authenticate} (RFC 9110). Sample
 * {@code Bearer realm="api", error="invalid_token"}.
 */
public final class WwwAuthenticateUtil {

    public record Challenge(String scheme, Map<String, String> params) {
        public String realm() {
            return params.get("realm");
        }
    }

    private static final Pattern SCHEME = Pattern.compile("^([A-Za-z][A-Za-z0-9+-]*)\\s*(.*)$");
    private static final Pattern PARAM = Pattern.compile("([A-Za-z0-9_-]+)=(\"([^\"]*)\"|[^,\\s]+)");

    private WwwAuthenticateUtil() {
    }

    public static Challenge parse(String header) {
        if (StringUtil.isBlank(header)) {
            throw new IllegalArgumentException("WWW-Authenticate is blank");
        }
        Matcher scheme = SCHEME.matcher(header.trim());
        if (!scheme.matches()) {
            throw new IllegalArgumentException("invalid WWW-Authenticate: " + header);
        }
        Map<String, String> params = new LinkedHashMap<>();
        Matcher matcher = PARAM.matcher(scheme.group(2) == null ? "" : scheme.group(2));
        while (matcher.find()) {
            String value = matcher.group(3) != null ? matcher.group(3) : matcher.group(2);
            params.put(matcher.group(1).toLowerCase(Locale.ROOT), value);
        }
        return new Challenge(scheme.group(1), Map.copyOf(params));
    }

    public static String scheme(String header) {
        return parse(header).scheme();
    }

    public static String realm(String header) {
        return parse(header).realm();
    }
}
