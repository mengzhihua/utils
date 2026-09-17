package com.mengzhihua.utils.util;

import java.util.Locale;

/**
 * Email local-part / domain / plus-tag parser (Commons Validator companion).
 */
public final class EmailUtil {

    private EmailUtil() {
    }

    public static boolean isValid(String email) {
        return RegexUtil.isEmail(email);
    }

    public static Parsed parse(String email) {
        if (!isValid(email)) {
            throw new IllegalArgumentException("invalid email: " + email);
        }
        int at = email.lastIndexOf('@');
        String local = email.substring(0, at);
        String domain = email.substring(at + 1).toLowerCase(Locale.ROOT);
        int plus = local.indexOf('+');
        String plusTag = plus >= 0 ? local.substring(plus + 1) : "";
        String localBase = plus >= 0 ? local.substring(0, plus) : local;
        return new Parsed(local, localBase, plusTag, domain);
    }

    public record Parsed(String local, String localBase, String plusTag, String domain) {
        public String normalized() {
            return localBase.toLowerCase(Locale.ROOT) + "@" + domain;
        }
    }
}
