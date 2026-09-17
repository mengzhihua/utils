package com.mengzhihua.utils.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;

/**
 * Mainland China 18-digit ID card checksum, birthday and gender.
 */
public final class IdCardUtil {

    private static final int[] WEIGHTS = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static final char[] CHECK_CODES = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private static final DateTimeFormatter BIRTHDAY = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Map<String, String> PROVINCES = Map.ofEntries(
            Map.entry("11", "北京"), Map.entry("12", "天津"), Map.entry("13", "河北"),
            Map.entry("14", "山西"), Map.entry("15", "内蒙古"), Map.entry("21", "辽宁"),
            Map.entry("22", "吉林"), Map.entry("23", "黑龙江"), Map.entry("31", "上海"),
            Map.entry("32", "江苏"), Map.entry("33", "浙江"), Map.entry("34", "安徽"),
            Map.entry("35", "福建"), Map.entry("36", "江西"), Map.entry("37", "山东"),
            Map.entry("41", "河南"), Map.entry("42", "湖北"), Map.entry("43", "湖南"),
            Map.entry("44", "广东"), Map.entry("45", "广西"), Map.entry("46", "海南"),
            Map.entry("50", "重庆"), Map.entry("51", "四川"), Map.entry("52", "贵州"),
            Map.entry("53", "云南"), Map.entry("54", "西藏"), Map.entry("61", "陕西"),
            Map.entry("62", "甘肃"), Map.entry("63", "青海"), Map.entry("64", "宁夏"),
            Map.entry("65", "新疆")
    );

    private IdCardUtil() {
    }

    public static boolean isValid(String idCard) {
        String normalized = to18(idCard);
        if (!RegexUtil.isIdCard(normalized)) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Character.digit(normalized.charAt(i), 10) * WEIGHTS[i];
        }
        char expected = CHECK_CODES[sum % 11];
        if (normalized.charAt(17) != expected) {
            return false;
        }
        return parseBirthday(normalized) != null;
    }

    public static LocalDate getBirthday(String idCard) {
        return isValid(idCard) ? parseBirthday(to18(idCard)) : null;
    }

    /**
     * @return {@code M} male, {@code F} female, or {@code null}
     */
    public static String getGender(String idCard) {
        if (!isValid(idCard)) {
            return null;
        }
        int code = Character.digit(to18(idCard).charAt(16), 10);
        return code % 2 == 0 ? "F" : "M";
    }

    public static int getAge(String idCard) {
        LocalDate birthday = getBirthday(idCard);
        return birthday == null ? 0 : DateTimeUtil.age(birthday);
    }

    public static String getProvince(String idCard) {
        if (!RegexUtil.isIdCard(idCard)) {
            return null;
        }
        return PROVINCES.get(idCard.substring(0, 2));
    }

    public static String convert15To18(String id15) {
        if (id15 != null && id15.length() == 18) {
            return id15.toUpperCase(Locale.ROOT);
        }
        if (id15 == null || id15.length() != 15 || !id15.chars().allMatch(Character::isDigit)) {
            throw new IllegalArgumentException("invalid 15-digit id card: " + id15);
        }
        String body = id15.substring(0, 6) + "19" + id15.substring(6);
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += Character.digit(body.charAt(i), 10) * WEIGHTS[i];
        }
        return body + CHECK_CODES[sum % 11];
    }

    private static String to18(String idCard) {
        if (idCard != null && idCard.length() == 15 && idCard.chars().allMatch(Character::isDigit)) {
            return convert15To18(idCard);
        }
        return idCard == null ? null : idCard.toUpperCase(Locale.ROOT);
    }

    private static LocalDate parseBirthday(String idCard) {
        try {
            return LocalDate.parse(idCard.substring(6, 14), BIRTHDAY);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}
