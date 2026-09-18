package com.mengzhihua.utils.common.net;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.mengzhihua.utils.common.json.JsonUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import tools.jackson.databind.JsonNode;

/**
 * HTTP {@code Report-To} JSON array of reporting groups.
 */
public final class ReportToUtil {

    private ReportToUtil() {
    }

    public static List<Map<String, Object>> parse(String header) {
        if (StringUtil.isBlank(header) || !JsonUtil.isJson(header)) {
            return List.of();
        }
        try {
            JsonNode tree = JsonUtil.readTree(header.trim());
            if (tree == null || !tree.isArray()) {
                return List.of();
            }
            List<Map<String, Object>> groups = new ArrayList<>();
            for (JsonNode item : tree) {
                groups.add(JsonUtil.mapper().convertValue(item, Map.class));
            }
            return Collections.unmodifiableList(groups);
        } catch (RuntimeException ex) {
            return List.of();
        }
    }

    public static String firstGroup(String header) {
        List<Map<String, Object>> groups = parse(header);
        if (groups.isEmpty()) {
            return null;
        }
        Object value = groups.get(0).get("group");
        return value == null ? null : String.valueOf(value);
    }

    public static Long firstMaxAge(String header) {
        List<Map<String, Object>> groups = parse(header);
        if (groups.isEmpty()) {
            return null;
        }
        Object value = groups.get(0).get("max_age");
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value == null) {
            return null;
        }
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    public static boolean hasGroup(String header, String group) {
        if (group == null) {
            return false;
        }
        return parse(header).stream().anyMatch(item -> group.equals(String.valueOf(item.get("group"))));
    }
}
