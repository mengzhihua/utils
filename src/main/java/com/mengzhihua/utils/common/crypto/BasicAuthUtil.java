package com.mengzhihua.utils.common.crypto;


import java.nio.charset.StandardCharsets;
import java.util.Base64;

import com.mengzhihua.utils.common.lang.AssertUtil;
import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * HTTP Basic auth header helpers.
 */
public final class BasicAuthUtil {

    private BasicAuthUtil() {
    }

    public static String header(String username, String password) {
        AssertUtil.notBlank(username, "username must not be blank");
        String token = Base64.getEncoder().encodeToString(
                (username + ":" + (password == null ? "" : password)).getBytes(StandardCharsets.UTF_8));
        return "Basic " + token;
    }

    public static String[] parse(String header) {
        if (StringUtil.isBlank(header) || !header.regionMatches(true, 0, "Basic ", 0, 6)) {
            throw new IllegalArgumentException("invalid basic auth header");
        }
        String decoded = new String(Base64.getDecoder().decode(header.substring(6).trim()), StandardCharsets.UTF_8);
        int colon = decoded.indexOf(':');
        if (colon < 0) {
            return new String[] {decoded, ""};
        }
        return new String[] {decoded.substring(0, colon), decoded.substring(colon + 1)};
    }
}
