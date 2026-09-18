package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Vary} field-name list (RFC 9110).
 * Sample {@code Accept-Encoding, User-Agent}.
 */
public final class VaryUtil {

    private VaryUtil() {
    }

    public static List<String> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<String> fields = new ArrayList<>();
        for (String part : header.split(",")) {
            String field = part.trim();
            if (!field.isEmpty()) {
                fields.add(field);
            }
        }
        return List.copyOf(fields);
    }

    public static String first(String header) {
        List<String> fields = parse(header);
        return fields.isEmpty() ? "" : fields.get(0);
    }

    public static boolean isWildcard(String header) {
        List<String> fields = parse(header);
        return fields.size() == 1 && "*".equals(fields.get(0));
    }

    public static boolean has(String header, String field) {
        if (field == null) {
            return false;
        }
        if (isWildcard(header)) {
            return true;
        }
        String wanted = field.trim().toLowerCase(Locale.ROOT);
        for (String name : parse(header)) {
            if (name.toLowerCase(Locale.ROOT).equals(wanted)) {
                return true;
            }
        }
        return false;
    }
}
