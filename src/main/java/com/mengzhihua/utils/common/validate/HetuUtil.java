package com.mengzhihua.utils.common.validate;


import java.time.LocalDate;
import java.util.Locale;

/**
 * Finnish henkilötunnus. Sample {@code 131052-308T}.
 */
public final class HetuUtil {

    private static final String CHECK = "0123456789ABCDEFHJKLMNPRSTUVWXY";

    private HetuUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (!compact.matches("\\d{6}[-+A-GU-Y]\\d{3}[0-9A-Z]")) {
            return false;
        }
        return checkChar(compact.substring(0, 6) + compact.substring(7, 10)) == compact.charAt(10);
    }

    public static char checkChar(String body9) {
        String digits = body9 == null ? "" : body9.replaceAll("\\D", "");
        if (!digits.matches("\\d{9}")) {
            throw new IllegalArgumentException("HETU body must be 9 digits");
        }
        return CHECK.charAt(Integer.parseInt(digits) % 31);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (compact.matches("\\d{6}[-+A-FU-Y]\\d{3}")) {
            return compact + checkChar(compact.substring(0, 6) + compact.substring(7));
        }
        if (compact.matches("\\d{9}")) {
            return compact.substring(0, 6) + "-" + compact.substring(6) + checkChar(compact);
        }
        throw new IllegalArgumentException("HETU body is invalid");
    }

    public static boolean female(String value) {
        String compact = normalize(value);
        if (compact.length() < 10) {
            throw new IllegalArgumentException("HETU is too short");
        }
        return (compact.charAt(9) - '0') % 2 == 0;
    }

    public static LocalDate birthDate(String value) {
        String compact = normalize(value);
        if (compact.length() < 7) {
            throw new IllegalArgumentException("HETU is too short");
        }
        int day = Integer.parseInt(compact.substring(0, 2));
        int month = Integer.parseInt(compact.substring(2, 4));
        int yy = Integer.parseInt(compact.substring(4, 6));
        char sign = compact.charAt(6);
        int century = switch (sign) {
            case '+' -> 1800;
            case '-', 'Y', 'X', 'W', 'V', 'U' -> 1900;
            default -> 2000;
        };
        return LocalDate.of(century + yy, month, day);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s.]", "").toUpperCase(Locale.ROOT);
    }
}
