package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Accept-Encoding} (RFC 9110) with q-values.
 */
public final class AcceptEncodingUtil {

    public record Encoding(String name, double q) {
    }

    private AcceptEncodingUtil() {
    }

    public static List<Encoding> parse(String header) {
        if (StringUtil.isBlank(header)) {
            return List.of();
        }
        List<Encoding> encodings = new ArrayList<>();
        for (String part : header.split(",")) {
            String item = part.trim();
            if (item.isEmpty()) {
                continue;
            }
            String name = item;
            double q = 1.0;
            int semi = item.indexOf(';');
            if (semi >= 0) {
                name = item.substring(0, semi).trim();
                String rest = item.substring(semi + 1);
                int eq = rest.toLowerCase(Locale.ROOT).indexOf("q=");
                if (eq >= 0) {
                    try {
                        q = Double.parseDouble(rest.substring(eq + 2).trim());
                    } catch (NumberFormatException ex) {
                        q = 0;
                    }
                }
            }
            if (!name.isEmpty()) {
                encodings.add(new Encoding(name.toLowerCase(Locale.ROOT), q));
            }
        }
        encodings.sort(Comparator.comparingDouble(Encoding::q).reversed());
        return List.copyOf(encodings);
    }

    public static String negotiate(String acceptEncoding, String... available) {
        List<Encoding> encodings = parse(acceptEncoding);
        if (available == null || available.length == 0) {
            return encodings.isEmpty() ? "" : encodings.get(0).name();
        }
        for (Encoding encoding : encodings) {
            if ("*".equals(encoding.name()) && encoding.q() > 0) {
                return available[0];
            }
            for (String option : available) {
                if (encoding.q() > 0 && encoding.name().equalsIgnoreCase(option)) {
                    return option;
                }
            }
        }
        return "";
    }
}
