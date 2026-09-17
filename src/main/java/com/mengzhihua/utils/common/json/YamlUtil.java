package com.mengzhihua.utils.common.json;


import java.util.Map;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * YAML 与 Map / JSON 互转（Spring 自带 SnakeYAML）。
 */
public final class YamlUtil {

    private YamlUtil() {
    }

    public static String toYaml(Object value) {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setPrettyFlow(true);
        return new Yaml(options).dump(value);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Object> toMap(String yaml) {
        Object loaded = new Yaml().load(yaml);
        if (loaded instanceof Map<?, ?> map) {
            return (Map<String, Object>) map;
        }
        throw new IllegalArgumentException("yaml root is not a map");
    }

    public static String jsonToYaml(String json) {
        return toYaml(JsonUtil.toMap(json));
    }

    public static String yamlToJson(String yaml) {
        return JsonUtil.toJson(toMap(yaml));
    }
}
