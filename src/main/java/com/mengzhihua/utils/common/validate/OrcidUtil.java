package com.mengzhihua.utils.common.validate;


import java.util.Locale;

/**
 * ORCID checksum (ISO 7064 Mod 11-2).
 */
public final class OrcidUtil {

    private OrcidUtil() {
    }

    public static boolean isValid(String orcid) {
        String compact = normalize(orcid);
        if (compact.length() != 16 || !compact.matches("\\d{15}[\\dX]")) {
            return false;
        }
        return checkDigit(compact.substring(0, 15)) == compact.charAt(15);
    }

    public static char checkDigit(String body) {
        String digits = body == null ? "" : body.replaceAll("\\D", "");
        if (digits.length() != 15) {
            throw new IllegalArgumentException("ORCID body must be 15 digits");
        }
        int total = 0;
        for (int i = 0; i < digits.length(); i++) {
            total = (total + (digits.charAt(i) - '0')) * 2;
        }
        int result = (12 - (total % 11)) % 11;
        return result == 10 ? 'X' : (char) ('0' + result);
    }

    public static String format(String orcid) {
        String compact = normalize(orcid);
        if (compact.length() != 16) {
            return compact;
        }
        return compact.substring(0, 4) + "-" + compact.substring(4, 8) + "-"
                + compact.substring(8, 12) + "-" + compact.substring(12);
    }

    public static String normalize(String orcid) {
        return orcid == null ? "" : orcid.replaceAll("[\\s-]", "").toUpperCase(Locale.ROOT);
    }
}
