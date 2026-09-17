package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Regex extract / replace helpers (Hutool {@code ReUtil} style).
 */
public final class ReUtil {

    private static final String META = ".\\[]{}()*+-?^$|";
    private static final ConcurrentHashMap<String, Pattern> CACHE = new ConcurrentHashMap<>();

    private ReUtil() {
    }

    public static Pattern compile(String regex) {
        if (regex == null) {
            throw new IllegalArgumentException("regex must not be null");
        }
        return CACHE.computeIfAbsent(regex, Pattern::compile);
    }

    public static boolean isValid(String regex) {
        if (regex == null) {
            return false;
        }
        try {
            Pattern.compile(regex);
            return true;
        } catch (PatternSyntaxException ex) {
            return false;
        }
    }

    public static boolean isMatch(String regex, CharSequence content) {
        return content != null && regex != null && Pattern.matches(regex, content);
    }

    public static boolean contains(String regex, CharSequence content) {
        if (content == null || regex == null) {
            return false;
        }
        return compile(regex).matcher(content).find();
    }

    public static String get(String regex, CharSequence content, int group) {
        if (content == null || regex == null) {
            return null;
        }
        Matcher matcher = compile(regex).matcher(content);
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
        Matcher matcher = compile(regex).matcher(content);
        while (matcher.find()) {
            list.add(matcher.group(group));
        }
        return list;
    }

    public static List<String> extract(Pattern pattern, CharSequence content) {
        List<String> list = new ArrayList<>();
        if (content == null || pattern == null) {
            return list;
        }
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            list.add(matcher.group());
        }
        return list;
    }

    public static List<String> extractMobiles(CharSequence content) {
        return extract(RegexUtil.MOBILE_FIND, content);
    }

    public static List<String> extractEmails(CharSequence content) {
        return extract(RegexUtil.EMAIL_FIND, content);
    }

    public static List<String> extractUrls(CharSequence content) {
        return extract(RegexUtil.URL_FIND, content);
    }

    public static List<String> extractIpv4(CharSequence content) {
        return extract(RegexUtil.IPV4_FIND, content);
    }

    public static List<String> extractDates(CharSequence content) {
        return extract(RegexUtil.DATE_FIND, content);
    }

    public static List<String> extractHexColors(CharSequence content) {
        return extract(RegexUtil.HEX_COLOR_FIND, content);
    }

    public static List<String> extractIdCards(CharSequence content) {
        return extract(RegexUtil.ID_CARD_FIND, content);
    }

    public static String[] getAllGroups(String regex, CharSequence content) {
        if (content == null || regex == null) {
            return new String[0];
        }
        Matcher matcher = compile(regex).matcher(content);
        if (!matcher.find()) {
            return new String[0];
        }
        String[] groups = new String[matcher.groupCount() + 1];
        for (int i = 0; i < groups.length; i++) {
            groups[i] = matcher.group(i);
        }
        return groups;
    }

    public static Map<String, String> getNamedGroups(String regex, CharSequence content) {
        Map<String, String> groups = new LinkedHashMap<>();
        if (content == null || regex == null) {
            return groups;
        }
        Matcher matcher = compile(regex).matcher(content);
        if (!matcher.find()) {
            return groups;
        }
        for (Map.Entry<String, Integer> entry : matcher.namedGroups().entrySet()) {
            groups.put(entry.getKey(), matcher.group(entry.getValue()));
        }
        return groups;
    }

    public static List<String[]> findAllGroups(String regex, CharSequence content) {
        List<String[]> list = new ArrayList<>();
        if (content == null || regex == null) {
            return list;
        }
        Matcher matcher = compile(regex).matcher(content);
        while (matcher.find()) {
            String[] groups = new String[matcher.groupCount() + 1];
            for (int i = 0; i < groups.length; i++) {
                groups[i] = matcher.group(i);
            }
            list.add(groups);
        }
        return list;
    }

    public static int count(String regex, CharSequence content) {
        if (content == null || regex == null) {
            return 0;
        }
        Matcher matcher = compile(regex).matcher(content);
        int n = 0;
        while (matcher.find()) {
            n++;
        }
        return n;
    }

    public static List<String> split(String regex, CharSequence content) {
        if (content == null) {
            return List.of();
        }
        if (regex == null || regex.isEmpty()) {
            return List.of(content.toString());
        }
        String[] parts = compile(regex).split(content.toString(), -1);
        return List.of(parts);
    }

    public static String delFirst(String regex, String content) {
        return content == null || regex == null ? content : content.replaceFirst(regex, "");
    }

    public static String delAll(String regex, String content) {
        return content == null || regex == null ? content : content.replaceAll(regex, "");
    }

    public static String replaceFirst(String content, String regex, String replacement) {
        if (content == null || regex == null) {
            return content;
        }
        return content.replaceFirst(regex, replacement == null ? "" : replacement);
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
