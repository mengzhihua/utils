package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Costa Rica CPF (Cédula de Persona Física). Format {@code 0P-TTTT-AAAA}.
 * Sample {@code 3-0455-0175} → {@code 0304550175}.
 */
public final class CrCpfUtil {

    private CrCpfUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        return digits.matches("0\\d{9}");
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            return value == null ? "" : value.trim();
        }
        return digits.substring(0, 2) + '-' + digits.substring(2, 6) + '-' + digits.substring(6);
    }

    public static String normalize(String value) {
        if (value == null) {
            return "";
        }
        String text = value.replaceAll("\\s+", "").toUpperCase(Locale.ROOT);
        String[] parts = text.split("-", -1);
        if (parts.length == 3) {
            text = pad(parts[0], 2) + pad(parts[1], 4) + pad(parts[2], 4);
        } else {
            text = text.replaceAll("\\D", "");
        }
        if (text.length() == 9) {
            return "0" + text;
        }
        return text;
    }

    private static String pad(String part, int width) {
        String digits = part == null ? "" : part.replaceAll("\\D", "");
        if (digits.length() >= width) {
            return digits;
        }
        return "0".repeat(width - digits.length()) + digits;
    }
}
