package com.mengzhihua.utils.common.validate;


import java.util.Locale;
import java.util.Set;

/**
 * Costa Rica CPJ (Cédula de Persona Jurídica). 10 digits, class + type + sequence.
 * Sample {@code 3-101-999999}.
 */
public final class CrCpjUtil {

    private static final Set<String> CLASS_TWO = Set.of("100", "200", "300", "400");
    private static final Set<String> CLASS_THREE = Set.of(
            "002", "003", "004", "005", "006", "007", "008", "009", "010", "011", "012", "013", "014",
            "101", "102", "103", "104", "105", "106", "107", "108", "109", "110");

    private CrCpjUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{10}")) {
            return false;
        }
        char cls = digits.charAt(0);
        String type = digits.substring(1, 4);
        return switch (cls) {
            case '2' -> CLASS_TWO.contains(type);
            case '3' -> CLASS_THREE.contains(type);
            case '4' -> "000".equals(type);
            case '5' -> "001".equals(type);
            default -> false;
        };
    }

    public static String complete(String value) {
        return normalize(value);
    }

    public static String format(String value) {
        String digits = normalize(value);
        if (digits.length() != 10) {
            return value == null ? "" : value.trim();
        }
        return digits.charAt(0) + "-" + digits.substring(1, 4) + "-" + digits.substring(4);
    }

    public static String personClass(String value) {
        String digits = normalize(value);
        return digits.isEmpty() ? "" : String.valueOf(digits.charAt(0));
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
