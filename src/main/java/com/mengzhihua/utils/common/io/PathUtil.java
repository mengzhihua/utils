package com.mengzhihua.utils.common.io;


import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Path join / normalize helpers.
 */
public final class PathUtil {

    private PathUtil() {
    }

    public static String join(String first, String... more) {
        if (first == null) {
            return null;
        }
        Path path = Path.of(first);
        if (more != null) {
            for (String part : more) {
                if (StringUtil.isNotBlank(part)) {
                    path = path.resolve(part);
                }
            }
        }
        return path.normalize().toString().replace('\\', '/');
    }

    public static String normalize(String path) {
        if (path == null) {
            return null;
        }
        return Path.of(path).normalize().toString().replace('\\', '/');
    }

    public static String[] split(String path) {
        if (StringUtil.isBlank(path)) {
            return new String[0];
        }
        String[] parts = normalize(path).split("/");
        List<String> result = new ArrayList<>();
        for (String part : parts) {
            if (StringUtil.isNotEmpty(part)) {
                result.add(part);
            }
        }
        return result.toArray(String[]::new);
    }
}
