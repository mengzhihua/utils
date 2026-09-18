package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP {@code Clear-Site-Data} quoted directives.
 */
public final class ClearSiteDataUtil {

    private static final Pattern QUOTED = Pattern.compile("\"([^\"]*)\"");
    private static final Set<String> KNOWN = Set.of("cache", "cookies", "storage", "executioncontexts", "*");

    private ClearSiteDataUtil() {
    }

    public static List<String> parse(String header) {
        List<String> tokens = new ArrayList<>();
        if (StringUtil.isBlank(header)) {
            return tokens;
        }
        Matcher matcher = QUOTED.matcher(header);
        while (matcher.find()) {
            tokens.add(matcher.group(1).toLowerCase(Locale.ROOT));
        }
        return List.copyOf(tokens);
    }

    public static boolean has(String header, String directive) {
        return directive != null && parse(header).contains(directive.toLowerCase(Locale.ROOT));
    }

    public static boolean known(String directive) {
        return directive != null && KNOWN.contains(directive.toLowerCase(Locale.ROOT));
    }
}
