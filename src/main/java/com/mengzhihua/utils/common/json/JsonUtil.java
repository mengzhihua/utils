package com.mengzhihua.utils.common.json;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.type.CollectionType;
import tools.jackson.databind.type.MapType;

/**
 * JSON serialize / deserialize helpers backed by Jackson 3 {@link JsonMapper}.
 */
public final class JsonUtil {

    private static final JsonMapper MAPPER = JsonMapper.builder()
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private JsonUtil() {
    }

    public static JsonMapper mapper() {
        return MAPPER;
    }

    public static String toJson(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof String str) {
            return str;
        }
        try {
            return MAPPER.writeValueAsString(value);
        } catch (JacksonException ex) {
            throw new IllegalArgumentException("failed to serialize json", ex);
        }
    }

    public static <T> T fromJson(String json, Class<T> type) {
        if (StringUtil.isBlank(json) || type == null) {
            return null;
        }
        try {
            return MAPPER.readValue(json, type);
        } catch (JacksonException ex) {
            throw new IllegalArgumentException("failed to parse json as " + type.getName(), ex);
        }
    }

    public static <T> List<T> toList(String json, Class<T> elementType) {
        if (StringUtil.isBlank(json) || elementType == null) {
            return new ArrayList<>();
        }
        try {
            CollectionType collectionType = MAPPER.getTypeFactory()
                    .constructCollectionType(ArrayList.class, elementType);
            List<T> list = MAPPER.readValue(json, collectionType);
            return list == null ? new ArrayList<>() : list;
        } catch (JacksonException ex) {
            throw new IllegalArgumentException("failed to parse json list", ex);
        }
    }

    public static Map<String, Object> toMap(String json) {
        if (StringUtil.isBlank(json)) {
            return new LinkedHashMap<>();
        }
        try {
            MapType mapType = MAPPER.getTypeFactory()
                    .constructMapType(LinkedHashMap.class, String.class, Object.class);
            Map<String, Object> map = MAPPER.readValue(json, mapType);
            return map == null ? new LinkedHashMap<>() : map;
        } catch (JacksonException ex) {
            throw new IllegalArgumentException("failed to parse json map", ex);
        }
    }

    public static JsonNode readTree(String json) {
        if (StringUtil.isBlank(json)) {
            return null;
        }
        try {
            return MAPPER.readTree(json);
        } catch (JacksonException ex) {
            throw new IllegalArgumentException("failed to parse json tree", ex);
        }
    }

    public static boolean isJson(String text) {
        if (StringUtil.isBlank(text)) {
            return false;
        }
        String trimmed = text.trim();
        if (!(trimmed.startsWith("{") && trimmed.endsWith("}"))
                && !(trimmed.startsWith("[") && trimmed.endsWith("]"))) {
            return false;
        }
        try {
            MAPPER.readTree(trimmed);
            return true;
        } catch (JacksonException ex) {
            return false;
        }
    }

    public static String toPrettyJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            if (value instanceof String str && isJson(str)) {
                return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(readTree(str));
            }
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JacksonException ex) {
            throw new IllegalArgumentException("failed to serialize pretty json", ex);
        }
    }
}
