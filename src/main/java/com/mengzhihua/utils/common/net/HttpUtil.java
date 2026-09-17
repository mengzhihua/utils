package com.mengzhihua.utils.common.net;


import com.mengzhihua.utils.common.json.JsonUtil;
import com.mengzhihua.utils.common.lang.AssertUtil;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

/**
 * Lightweight HTTP client wrapper around Spring {@link RestClient}.
 */
public final class HttpUtil {

    private static final RestClient CLIENT = RestClient.create();

    private HttpUtil() {
    }

    public static String get(String url) {
        AssertUtil.notBlank(url, "url must not be blank");
        return CLIENT.get()
                .uri(url)
                .retrieve()
                .body(String.class);
    }

    public static String postJson(String url, Object body) {
        AssertUtil.notBlank(url, "url must not be blank");
        return CLIENT.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(JsonUtil.toJson(body))
                .retrieve()
                .body(String.class);
    }

    public static <T> T get(String url, Class<T> responseType) {
        String json = get(url);
        return JsonUtil.fromJson(json, responseType);
    }
}
