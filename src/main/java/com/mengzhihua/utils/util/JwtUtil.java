package com.mengzhihua.utils.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Compact HS256 JWT create / parse. Not a full JWT library — enough for internal tokens.
 */
public final class JwtUtil {

    private static final Base64.Encoder URL_ENCODER = Base64.getUrlEncoder().withoutPadding();
    private static final Base64.Decoder URL_DECODER = Base64.getUrlDecoder();
    private static final String HEADER_JSON = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

    private JwtUtil() {
    }

    public static String create(Map<String, Object> claims, String secret, Duration ttl) {
        AssertUtil.notBlank(secret, "jwt secret must not be blank");
        AssertUtil.notNull(ttl, "ttl must not be null");
        Map<String, Object> payload = new LinkedHashMap<>();
        if (claims != null) {
            payload.putAll(claims);
        }
        long now = Instant.now().getEpochSecond();
        payload.putIfAbsent("iat", now);
        payload.put("exp", now + Math.max(1, ttl.getSeconds()));
        String header = URL_ENCODER.encodeToString(HEADER_JSON.getBytes(StandardCharsets.UTF_8));
        String body = URL_ENCODER.encodeToString(JsonUtil.toJson(payload).getBytes(StandardCharsets.UTF_8));
        String signingInput = header + "." + body;
        return signingInput + "." + sign(signingInput, secret);
    }

    public static Map<String, Object> parse(String token, String secret) {
        if (!verify(token, secret)) {
            throw new IllegalArgumentException("invalid jwt");
        }
        String[] parts = token.split("\\.");
        Map<String, Object> payload = JsonUtil.toMap(new String(URL_DECODER.decode(parts[1]), StandardCharsets.UTF_8));
        Object exp = payload.get("exp");
        if (exp instanceof Number number && Instant.now().getEpochSecond() >= number.longValue()) {
            throw new IllegalArgumentException("jwt expired");
        }
        return payload;
    }

    public static boolean verify(String token, String secret) {
        if (StringUtil.isBlank(token) || StringUtil.isBlank(secret)) {
            return false;
        }
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            return false;
        }
        String expected = sign(parts[0] + "." + parts[1], secret);
        return constantTimeEquals(expected, parts[2]);
    }

    private static String sign(String signingInput, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return URL_ENCODER.encodeToString(mac.doFinal(signingInput.getBytes(StandardCharsets.UTF_8)));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("jwt sign failed", ex);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null || a.length() != b.length()) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }
}
