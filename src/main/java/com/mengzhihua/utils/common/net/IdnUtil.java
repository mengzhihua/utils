package com.mengzhihua.utils.common.net;


import java.net.IDN;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Internationalized domain names via JDK {@link IDN} (Punycode).
 */
public final class IdnUtil {

    private IdnUtil() {
    }

    public static String toAscii(String domain) {
        if (StringUtil.isBlank(domain)) {
            return domain;
        }
        return IDN.toASCII(domain.trim(), IDN.ALLOW_UNASSIGNED).toLowerCase(Locale.ROOT);
    }

    public static String toUnicode(String domain) {
        if (StringUtil.isBlank(domain)) {
            return domain;
        }
        return IDN.toUnicode(domain.trim(), IDN.ALLOW_UNASSIGNED);
    }
}
