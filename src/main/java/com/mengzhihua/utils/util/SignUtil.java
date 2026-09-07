package com.mengzhihua.utils.util;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 开放接口常用签名：参数按 key 排序后拼接，再 MD5 / HMAC-SHA256。
 */
public final class SignUtil {

    private SignUtil() {
    }

    public static String canonical(Map<String, ?> params, String secret) {
        TreeMap<String, String> sorted = new TreeMap<>();
        if (params != null) {
            params.forEach((key, value) -> {
                if (StringUtil.isBlank(key) || "sign".equalsIgnoreCase(key) || value == null) {
                    return;
                }
                String text = String.valueOf(value);
                if (StringUtil.isNotBlank(text)) {
                    sorted.put(key, text);
                }
            });
        }
        String body = sorted.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
        if (StringUtil.isBlank(secret)) {
            return body;
        }
        return body.isEmpty() ? "secret=" + secret : body + "&secret=" + secret;
    }

    public static String md5(Map<String, ?> params, String secret) {
        return EncryptUtil.md5(canonical(params, secret));
    }

    public static String hmacSha256(Map<String, ?> params, String secret) {
        return EncryptUtil.hmacSha256(canonical(params, null), secret);
    }
}
