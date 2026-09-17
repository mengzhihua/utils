package com.mengzhihua.utils.util;

/**
 * JSON Pointer access ({@code /user/name} or {@code user.name}).
 */
public final class JsonPathUtil {

    private JsonPathUtil() {
    }

    public static tools.jackson.databind.JsonNode at(String json, String path) {
        tools.jackson.databind.JsonNode tree = JsonUtil.readTree(json);
        if (tree == null) {
            return null;
        }
        return tree.at(pointer(path));
    }

    public static String getStr(String json, String path) {
        tools.jackson.databind.JsonNode node = at(json, path);
        if (node == null || node.isMissingNode() || node.isNull()) {
            return null;
        }
        return node.isValueNode() ? node.asText() : node.toString();
    }

    public static boolean exists(String json, String path) {
        tools.jackson.databind.JsonNode node = at(json, path);
        return node != null && !node.isMissingNode();
    }

    private static String pointer(String path) {
        if (StringUtil.isBlank(path) || "/".equals(path)) {
            return "";
        }
        if (path.startsWith("/")) {
            return path;
        }
        return "/" + path.replace('.', '/');
    }
}
