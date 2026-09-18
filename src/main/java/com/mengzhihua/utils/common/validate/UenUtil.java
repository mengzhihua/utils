package com.mengzhihua.utils.common.validate;


import java.time.Year;
import java.util.Locale;
import java.util.Set;

/**
 * Singapore UEN (ACRA). Samples {@code 00192200M}, {@code 197401143C}, {@code T01FC6132D}.
 */
public final class UenUtil {

    private static final String BUSINESS_CHECK = "XMKECAWLJDB";
    private static final int[] BUSINESS_WEIGHTS = {10, 4, 9, 3, 8, 2, 7, 1};
    private static final String LOCAL_CHECK = "ZKCMDNERGWH";
    private static final int[] LOCAL_WEIGHTS = {10, 8, 6, 4, 9, 7, 5, 3, 1};
    private static final String OTHER_ALPHABET = "ABCDEFGHJKLMNPQRSTUVWX0123456789";
    private static final int[] OTHER_WEIGHTS = {4, 3, 5, 3, 10, 2, 2, 5, 7};
    private static final Set<String> OTHER_TYPES = Set.of(
            "CC", "CD", "CH", "CL", "CM", "CP", "CS", "CX", "DP", "FB", "FC", "FM",
            "FN", "GA", "GB", "GS", "HS", "LL", "LP", "MB", "MC", "MD", "MH", "MM",
            "MQ", "NB", "NR", "PA", "PB", "PF", "RF", "RP", "SM", "SS", "TC", "TU",
            "VH", "XL"
    );

    private UenUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        if (compact.matches("\\d{8}[A-Z]")) {
            return checkBusiness(compact.substring(0, 8)) == compact.charAt(8);
        }
        if (compact.matches("\\d{9}[A-Z]")) {
            return Year.of(Integer.parseInt(compact.substring(0, 4))).getValue() <= Year.now().getValue()
                    && checkLocal(compact.substring(0, 9)) == compact.charAt(9);
        }
        if (compact.matches("[RST]\\d{2}[A-Z]{2}\\d{4}[A-Z]")) {
            if (compact.charAt(0) == 'T' && Integer.parseInt(compact.substring(1, 3)) > Year.now().getValue() % 100) {
                return false;
            }
            return OTHER_TYPES.contains(compact.substring(3, 5))
                    && checkOther(compact.substring(0, 9)) == compact.charAt(9);
        }
        return false;
    }

    public static char checkDigit(String body) {
        String compact = normalize(body);
        if (compact.matches("\\d{8}")) {
            return checkBusiness(compact);
        }
        if (compact.matches("\\d{9}")) {
            return checkLocal(compact);
        }
        if (compact.matches("[RST]\\d{2}[A-Z]{2}\\d{4}")) {
            return checkOther(compact);
        }
        throw new IllegalArgumentException("unsupported UEN body");
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        return compact + checkDigit(compact);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }

    private static char checkBusiness(String body8) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (body8.charAt(i) - '0') * BUSINESS_WEIGHTS[i];
        }
        return BUSINESS_CHECK.charAt(sum % 11);
    }

    private static char checkLocal(String body9) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            sum += (body9.charAt(i) - '0') * LOCAL_WEIGHTS[i];
        }
        return LOCAL_CHECK.charAt(sum % 11);
    }

    private static char checkOther(String body9) {
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int index = OTHER_ALPHABET.indexOf(body9.charAt(i));
            if (index < 0) {
                throw new IllegalArgumentException("UEN other body has invalid character");
            }
            sum += index * OTHER_WEIGHTS[i];
        }
        return OTHER_ALPHABET.charAt(Math.floorMod(sum - 5, 11));
    }
}
