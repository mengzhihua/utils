package com.mengzhihua.utils.common.validate;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * U.S. EIN / FEIN. Prefix maps to IRS campus.
 * Samples {@code 91-1144442}, {@code 04-2103594}.
 */
public final class EinUtil {

    private static final Map<String, String> CAMPUS = campusMap();

    private EinUtil() {
    }

    public static boolean isValid(String value) {
        if (value != null && value.indexOf('-') >= 0 && !value.trim().matches("\\d{2}-\\d{7}")) {
            return false;
        }
        String digits = normalize(value);
        return digits.matches("\\d{9}") && CAMPUS.containsKey(digits.substring(0, 2));
    }

    public static String campus(String value) {
        String digits = normalize(value);
        return digits.length() >= 2 ? CAMPUS.getOrDefault(digits.substring(0, 2), "") : "";
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 9) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 2) + '-' + digits.substring(2);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static Map<String, String> campusMap() {
        Map<String, String> map = new LinkedHashMap<>();
        put(map, "Andover", "10", "12");
        put(map, "Atlanta", "60", "67");
        put(map, "Austin", "50", "53");
        put(map, "Brookhaven",
                "01", "02", "03", "04", "05", "06", "11", "13", "14", "16",
                "21", "22", "23", "25", "34", "51", "52", "54", "55", "56",
                "57", "58", "59", "65");
        put(map, "Cincinnati", "30", "32", "35", "36", "37", "38", "61");
        put(map, "Fresno", "15", "24");
        put(map, "Kansas City", "40", "44");
        put(map, "Memphis", "94", "95");
        put(map, "Ogden", "80", "90");
        put(map, "Philadelphia",
                "33", "39", "41", "42", "43", "46", "48", "62", "63", "64",
                "66", "68", "71", "72", "73", "74", "75", "76", "77", "85",
                "86", "87", "88", "91", "92", "93", "98", "99");
        put(map, "Internet", "20", "26", "27", "45", "47", "81", "82", "83", "84");
        put(map, "SBA", "31");
        return Map.copyOf(map);
    }

    private static void put(Map<String, String> map, String campus, String... prefixes) {
        for (String prefix : prefixes) {
            map.put(prefix, campus);
        }
    }
}
