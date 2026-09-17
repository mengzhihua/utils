package com.mengzhihua.utils.common.net;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.mengzhihua.utils.common.lang.AssertUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Cookie read / write helpers.
 */
public final class CookieUtil {

    private CookieUtil() {
    }

    public static String get(HttpServletRequest request, String name) {
        if (request == null || StringUtil.isBlank(name) || request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public static void set(HttpServletResponse response, String name, String value, int maxAgeSeconds) {
        set(response, name, value, maxAgeSeconds, "/", true);
    }

    public static void set(HttpServletResponse response, String name, String value, int maxAgeSeconds, String path, boolean httpOnly) {
        AssertUtil.notNull(response, "response must not be null");
        Cookie cookie = new Cookie(name, value == null ? "" : value);
        cookie.setMaxAge(maxAgeSeconds);
        cookie.setPath(StringUtil.defaultIfBlank(path, "/"));
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }

    public static void delete(HttpServletResponse response, String name) {
        set(response, name, "", 0);
    }
}
