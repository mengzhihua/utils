package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Hong Kong Identity Card (Hutool {@code IdcardUtil} HK checksum).
 * Sample {@code A123456(3)} is valid.
 */
public final class HkIdUtil {

    private HkIdUtil() {
    }

    public static boolean isValid(String id) {
        String compact = normalize(id);
        if (compact.length() < 8 || compact.length() > 9) {
            return false;
        }
        char check = compact.charAt(compact.length() - 1);
        String body = compact.substring(0, compact.length() - 1);
        if (!body.matches("[A-Z]{1,2}\\d{6}")) {
            return false;
        }
        return checkDigit(body) == check;
    }

    public static char checkDigit(String body) {
        String value = body == null ? "" : body.trim().toUpperCase(Locale.ROOT);
        String padded = value.length() == 7 ? " " + value : value;
        if (padded.length() != 8) {
            throw new IllegalArgumentException("HKID body must be 1-2 letters + 6 digits");
        }
        int sum = 0;
        int weight = 9;
        for (int i = 0; i < 8; i++) {
            sum += code(padded.charAt(i)) * weight;
            weight--;
        }
        int rem = sum % 11;
        int check = (11 - rem) % 11;
        return check == 10 ? 'A' : (char) ('0' + check);
    }

    private static int code(char c) {
        if (c == ' ') {
            return 36;
        }
        if (c >= 'A' && c <= 'Z') {
            return c - 'A' + 10;
        }
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        throw new IllegalArgumentException("invalid HKID char: " + c);
    }

    public static String normalize(String id) {
        if (id == null) {
            return "";
        }
        return id.trim().toUpperCase(Locale.ROOT).replace("(", "").replace(")", "").replace(" ", "");
    }
}
