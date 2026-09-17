package com.mengzhihua.utils.common.json;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;
import tools.jackson.databind.JsonNode;

/**
 * RFC 6902 JSON Patch (add / remove / replace / test / copy / move).
 */
public final class JsonPatchUtil {

    private JsonPatchUtil() {
    }

    public static String apply(String json, String patchJson) {
        Object doc = JsonUtil.mapper().convertValue(JsonUtil.readTree(json), Object.class);
        JsonNode patch = JsonUtil.readTree(patchJson);
        if (patch == null || !patch.isArray()) {
            throw new IllegalArgumentException("patch must be a JSON array");
        }
        for (JsonNode opNode : patch) {
            String op = text(opNode, "op");
            String path = text(opNode, "path");
            switch (op) {
                case "add" -> add(doc, path, javaValue(opNode.get("value")), true);
                case "replace" -> replace(doc, path, javaValue(opNode.get("value")));
                case "remove" -> remove(doc, path);
                case "test" -> {
                    if (!equalsValue(get(doc, path), javaValue(opNode.get("value")))) {
                        throw new IllegalArgumentException("json patch test failed: " + path);
                    }
                }
                case "copy" -> add(doc, path, copyOf(get(doc, text(opNode, "from"))), true);
                case "move" -> {
                    Object moved = copyOf(get(doc, text(opNode, "from")));
                    remove(doc, text(opNode, "from"));
                    add(doc, path, moved, true);
                }
                default -> throw new IllegalArgumentException("unsupported json patch op: " + op);
            }
        }
        return JsonUtil.toJson(doc);
    }

    private static Object javaValue(JsonNode node) {
        if (node == null || node.isNull() || node.isMissingNode()) {
            return null;
        }
        return JsonUtil.mapper().convertValue(node, Object.class);
    }

    @SuppressWarnings("unchecked")
    private static Object get(Object doc, String path) {
        List<String> tokens = tokens(path);
        Object current = doc;
        for (String token : tokens) {
            current = child(current, token, false);
        }
        return current;
    }

    @SuppressWarnings("unchecked")
    private static void add(Object doc, String path, Object value, boolean insert) {
        if (path == null || path.isEmpty() || "/".equals(path)) {
            throw new IllegalArgumentException("cannot replace document root");
        }
        List<String> tokens = tokens(path);
        Object parent = doc;
        for (int i = 0; i < tokens.size() - 1; i++) {
            parent = child(parent, tokens.get(i), false);
        }
        String last = tokens.get(tokens.size() - 1);
        if (parent instanceof Map<?, ?> map) {
            ((Map<String, Object>) map).put(last, value);
        } else if (parent instanceof List<?> list) {
            List<Object> items = (List<Object>) list;
            if ("-".equals(last)) {
                items.add(value);
            } else {
                int index = Integer.parseInt(last);
                if (insert) {
                    items.add(index, value);
                } else {
                    items.set(index, value);
                }
            }
        } else {
            throw new IllegalArgumentException("cannot add at " + path);
        }
    }

    @SuppressWarnings("unchecked")
    private static void replace(Object doc, String path, Object value) {
        List<String> tokens = tokens(path);
        Object parent = doc;
        for (int i = 0; i < tokens.size() - 1; i++) {
            parent = child(parent, tokens.get(i), false);
        }
        String last = tokens.get(tokens.size() - 1);
        if (parent instanceof Map<?, ?> map) {
            if (!((Map<String, Object>) map).containsKey(last)) {
                throw new IllegalArgumentException("missing path " + path);
            }
            ((Map<String, Object>) map).put(last, value);
        } else if (parent instanceof List<?> list) {
            ((List<Object>) list).set(Integer.parseInt(last), value);
        } else {
            throw new IllegalArgumentException("cannot replace " + path);
        }
    }

    @SuppressWarnings("unchecked")
    private static void remove(Object doc, String path) {
        List<String> tokens = tokens(path);
        Object parent = doc;
        for (int i = 0; i < tokens.size() - 1; i++) {
            parent = child(parent, tokens.get(i), false);
        }
        String last = tokens.get(tokens.size() - 1);
        if (parent instanceof Map<?, ?> map) {
            if (((Map<String, Object>) map).remove(last) == null && !((Map<?, ?>) map).containsKey(last)) {
                throw new IllegalArgumentException("missing path " + path);
            }
        } else if (parent instanceof List<?> list) {
            ((List<Object>) list).remove(Integer.parseInt(last));
        } else {
            throw new IllegalArgumentException("cannot remove " + path);
        }
    }

    @SuppressWarnings("unchecked")
    private static Object child(Object current, String token, boolean create) {
        if (current instanceof Map<?, ?> map) {
            Object value = ((Map<String, Object>) map).get(token);
            if (value == null && create) {
                value = new LinkedHashMap<String, Object>();
                ((Map<String, Object>) map).put(token, value);
            }
            if (value == null && !((Map<?, ?>) map).containsKey(token)) {
                throw new IllegalArgumentException("missing token " + token);
            }
            return value;
        }
        if (current instanceof List<?> list) {
            return list.get(Integer.parseInt(token));
        }
        throw new IllegalArgumentException("cannot walk token " + token);
    }

    private static List<String> tokens(String path) {
        if (path == null || path.isEmpty() || "/".equals(path)) {
            return List.of();
        }
        if (!path.startsWith("/")) {
            throw new IllegalArgumentException("json pointer must start with /");
        }
        List<String> tokens = new ArrayList<>();
        for (String raw : path.substring(1).split("/", -1)) {
            tokens.add(raw.replace("~1", "/").replace("~0", "~"));
        }
        return tokens;
    }

    private static Object copyOf(Object value) {
        return javaValue(JsonUtil.readTree(JsonUtil.toJson(value)));
    }

    private static boolean equalsValue(Object left, Object right) {
        return StringUtil.equals(JsonUtil.toJson(left), JsonUtil.toJson(right));
    }

    private static String text(JsonNode node, String field) {
        JsonNode value = node.get(field);
        if (value == null || value.isNull()) {
            throw new IllegalArgumentException("missing " + field);
        }
        return value.asText();
    }
}
