package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Spanish NIF / NIE. Sample DNI {@code 12345678Z}, NIE {@code X1234567L}.
 */
public final class NifUtil {

    private static final String LETTERS = "TRWAGMYFPDXBNJZSQVHLCKE";

    private NifUtil() {
    }

    public static boolean isValid(String nif) {
        String compact = normalize(nif);
        if (compact.matches("\\d{8}[A-Z]")) {
            return letter(compact.substring(0, 8)) == compact.charAt(8);
        }
        if (compact.matches("[XYZ]\\d{7}[A-Z]")) {
            char prefix = compact.charAt(0);
            int mapped = prefix == 'X' ? 0 : prefix == 'Y' ? 1 : 2;
            return letter(mapped + compact.substring(1, 8)) == compact.charAt(8);
        }
        return false;
    }

    public static char letter(String body8) {
        String digits = body8 == null ? "" : body8.replaceAll("\\D", "");
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("NIF body must be 8 digits");
        }
        return LETTERS.charAt(Integer.parseInt(digits) % 23);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (compact.matches("\\d{8}")) {
            return compact + letter(compact);
        }
        if (compact.matches("[XYZ]\\d{7}")) {
            char prefix = compact.charAt(0);
            int mapped = prefix == 'X' ? 0 : prefix == 'Y' ? 1 : 2;
            return compact + letter(mapped + compact.substring(1));
        }
        throw new IllegalArgumentException("NIF/NIE body is invalid");
    }

    public static String normalize(String nif) {
        return nif == null ? "" : nif.replaceAll("[\\s.-]", "").toUpperCase(Locale.ROOT);
    }
}
