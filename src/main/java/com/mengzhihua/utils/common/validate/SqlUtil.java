package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Set;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Guards against SQL injection in dynamic ORDER BY / column names.
 */
public final class SqlUtil {

    private static final Set<String> DIRECTIONS = Set.of("ASC", "DESC");

    private SqlUtil() {
    }

    /**
     * Allows letters, digits, underscore, comma and dot only.
     */
    public static String escapeOrderBy(String orderBy) {
        if (StringUtil.isBlank(orderBy)) {
            return "";
        }
        if (!orderBy.matches("[A-Za-z0-9_,.\\s]+")) {
            throw new IllegalArgumentException("illegal order by clause");
        }
        return orderBy.trim();
    }

    public static String column(String name) {
        if (StringUtil.isBlank(name) || !name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
            throw new IllegalArgumentException("illegal column name");
        }
        return name;
    }

    public static String direction(String direction) {
        String normalized = StringUtil.defaultIfBlank(direction, "ASC").trim().toUpperCase(Locale.ROOT);
        if (!DIRECTIONS.contains(normalized)) {
            throw new IllegalArgumentException("illegal sort direction");
        }
        return normalized;
    }
}
