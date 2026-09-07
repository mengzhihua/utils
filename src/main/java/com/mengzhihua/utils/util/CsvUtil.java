package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Minimal CSV encode / decode (RFC4180-ish, UTF-8 text).
 */
public final class CsvUtil {

    private CsvUtil() {
    }

    public static String toCsv(List<String> headers, List<List<String>> rows) {
        StringBuilder builder = new StringBuilder();
        if (headers != null) {
            builder.append(joinLine(headers)).append('\n');
        }
        if (rows != null) {
            for (List<String> row : rows) {
                builder.append(joinLine(row)).append('\n');
            }
        }
        return builder.toString();
    }

    public static String toCsv(List<Map<String, Object>> records) {
        if (CollectionUtil.isEmpty(records)) {
            return "";
        }
        List<String> headers = new ArrayList<>(records.get(0).keySet());
        List<List<String>> rows = new ArrayList<>();
        for (Map<String, Object> record : records) {
            List<String> row = new ArrayList<>();
            for (String header : headers) {
                row.add(ConvertUtil.toStr(record.get(header), ""));
            }
            rows.add(row);
        }
        return toCsv(headers, rows);
    }

    public static List<List<String>> parse(String csv) {
        List<List<String>> rows = new ArrayList<>();
        if (StringUtil.isBlank(csv)) {
            return rows;
        }
        String text = csv.replace("\r\n", "\n").replace('\r', '\n');
        if (text.endsWith("\n")) {
            text = text.substring(0, text.length() - 1);
        }
        List<String> current = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (quoted) {
                if (c == '"') {
                    if (i + 1 < text.length() && text.charAt(i + 1) == '"') {
                        field.append('"');
                        i++;
                    } else {
                        quoted = false;
                    }
                } else {
                    field.append(c);
                }
            } else if (c == '"') {
                quoted = true;
            } else if (c == ',') {
                current.add(field.toString());
                field.setLength(0);
            } else if (c == '\n') {
                current.add(field.toString());
                field.setLength(0);
                rows.add(current);
                current = new ArrayList<>();
            } else {
                field.append(c);
            }
        }
        current.add(field.toString());
        rows.add(current);
        return rows;
    }

    private static String joinLine(List<String> values) {
        List<String> escaped = new ArrayList<>();
        if (values != null) {
            for (String value : values) {
                escaped.add(escape(value));
            }
        }
        return String.join(",", escaped);
    }

    private static String escape(String value) {
        String text = value == null ? "" : value;
        if (text.contains(",") || text.contains("\"") || text.contains("\n") || text.contains("\r")) {
            return "\"" + text.replace("\"", "\"\"") + "\"";
        }
        return text;
    }
}
