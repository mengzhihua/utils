package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * Singapore NRIC / FIN checksum. Sample {@code S1234567D}.
 */
public final class NricUtil {

    private static final int[] WEIGHTS = {2, 7, 6, 5, 4, 3, 2};
    private static final String ST = "JZIHGFEDCBA";
    private static final String FG = "XWUTRQPNMLK";

    private NricUtil() {
    }

    public static boolean isValid(String nric) {
        String compact = normalize(nric);
        if (!compact.matches("[STFGM]\\d{7}[A-Z]")) {
            return false;
        }
        return compact.charAt(8) == checkLetter(compact.substring(0, 8));
    }

    public static char checkLetter(String body8) {
        String compact = normalize(body8);
        if (!compact.matches("[STFGM]\\d{7}")) {
            throw new IllegalArgumentException("NRIC body must be prefix + 7 digits");
        }
        char prefix = compact.charAt(0);
        int sum = (prefix == 'T' || prefix == 'G' || prefix == 'M') ? 4 : 0;
        for (int i = 0; i < 7; i++) {
            sum += (compact.charAt(i + 1) - '0') * WEIGHTS[i];
        }
        String table = (prefix == 'F' || prefix == 'G' || prefix == 'M') ? FG : ST;
        return table.charAt(sum % 11);
    }

    public static String complete(String body8) {
        String body = normalize(body8);
        return body + checkLetter(body);
    }

    public static String normalize(String nric) {
        return nric == null ? "" : nric.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
