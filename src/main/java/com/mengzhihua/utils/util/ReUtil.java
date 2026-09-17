package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Regex extract / replace helpers (Hutool {@code ReUtil} style).
 */
public final class ReUtil {

    private static final String META = ".\\[]{}()*+-?^$|";

    private ReUtil() {
    }

    public static boolean isMatch(String regex, CharSequence content) {
        return content != null && regex != null && Pattern.matches(regex, content);
    }

    public static boolean contains(String regex, CharSequence content) {
        if (content == null || regex == null) {
            return false;
        }
        return Pattern.compile(regex).matcher(content).find();
    }

    public static String get(String regex, CharSequence content, int group) {
        if (content == null || regex == null) {
            return null;
        }
        Matcher matcher = Pattern.compile(regex).matcher(content);
        return matcher.find() ? matcher.group(group) : null;
    }

    public static String getGroup0(String regex, CharSequence content) {
        return get(regex, content, 0);
    }

    public static String getGroup1(String regex, CharSequence content) {
        return get(regex, content, 1);
    }

    public static String getFirstNumber(CharSequence content) {
        return get("\\d+", content, 0);
    }

    public static List<String> findAll(String regex, CharSequence content) {
        return findAll(regex, content, 0);
    }

    public static List<String> findAll(String regex, CharSequence content, int group) {
        List<String> list = new ArrayList<>();
        if (content == null || regex == null) {
            return list;
        }
        Matcher matcher = Pattern.compile(regex).matcher(content);
        while (matcher.find()) {
            list.add(matcher.group(group));
        }
        return list;
    }

    public static String[] getAllGroups(String regex, CharSequence content) {
        if (content == null || regex == null) {
            return new String[0];
        }
        Matcher matcher = Pattern.compile(regex).matcher(content);
        if (!matcher.find()) {
            return new String[0];
        }
        String[] groups = new String[matcher.groupCount() + 1];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = matcher.group(i);
        }
        return groups;
    }

    public static int count(String regex, CharSequence content) {
        if (content == null || regex == null) {
            return 0;
        }
        Matcher matcher = Pattern.compile(regex).matcher(content);
        int n = 0;
        while (matcher.find()) {
            n++;
        }
        return n;
    }

    public static String delFirst(String regex, String content) {
        return content == null || regex == null ? content : content.replaceFirst(regex, "");
    }

    public static String delAll(String regex, String content) {
        return content == null || regex == null ? content : content.replaceAll(regex, "");
    }

    public static String replaceAll(String content, String regex, String replacement) {
        if (content == null || regex == null) {
            return content;
        }
        return content.replaceAll(regex, replacement == null ? "" : replacement);
    }

    public static String escape(CharSequence content) {
        if (content == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder(content.length());
        for (int i = 0; i < content.length(); i++) {
            char c = content.charAt(i);
            if (META.indexOf(c) >= 0) {
                builder.append('\\');
            }
            builder.append(c);
        }
        return builder.toString();
    }
}
