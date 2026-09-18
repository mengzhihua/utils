package com.mengzhihua.utils.common.validate;


/**
 * New Zealand IRD number (8/9 digits). Primary weights {@code 3,2,7,6,5,4,3,2},
 * alternate {@code 7,4,3,2,5,2,7,6} when the first check is 10.
 * Sample {@code 49091850}.
 */
public final class IrdUtil {

    private static final int[] PRIMARY = {3, 2, 7, 6, 5, 4, 3, 2};
    private static final int[] ALTERNATE = {7, 4, 3, 2, 5, 2, 7, 6};

    private IrdUtil() {
    }

    public static boolean isValid(String value) {
        String digits = normalize(value);
        if (!digits.matches("\\d{8,9}")) {
            return false;
        }
        String nine = digits.length() == 8 ? "0" + digits : digits;
        Integer check = checkOf(nine.substring(0, 8));
        return check != null && check == nine.charAt(8) - '0';
    }

    public static char checkDigit(String body) {
        String digits = normalize(body);
        if (digits.length() == 9) {
            digits = digits.substring(0, 8);
        } else if (digits.length() == 8) {
            digits = digits.substring(0, 8);
        } else if (digits.length() == 7) {
            digits = "0" + digits;
        }
        if (!digits.matches("\\d{8}")) {
            throw new IllegalArgumentException("IRD body must be 7 or 8 digits");
        }
        Integer check = checkOf(digits);
        if (check == null) {
            throw new IllegalArgumentException("IRD body has no valid check digit");
        }
        return (char) ('0' + check);
    }

    public static String complete(String body) {
        String digits = normalize(body);
        if (digits.length() == 9) {
            digits = digits.substring(1, 8);
        } else if (digits.length() == 8) {
            digits = digits.substring(0, 7);
        }
        if (!digits.matches("\\d{7}")) {
            throw new IllegalArgumentException("IRD body must be 7 digits");
        }
        return digits + checkDigit("0" + digits);
    }

    public static String normalize(String value) {
        return value == null ? "" : value.replaceAll("\\D", "");
    }

    private static Integer checkOf(String body8) {
        Integer check = remainder(body8, PRIMARY);
        if (check == null) {
            check = remainder(body8, ALTERNATE);
        }
        return check;
    }

    private static Integer remainder(String body8, int[] weights) {
        int sum = 0;
        for (int i = 0; i < 8; i++) {
            sum += (body8.charAt(i) - '0') * weights[i];
        }
        int rem = sum % 11;
        int check = 11 - rem;
        if (check == 11) {
            return 0;
        }
        if (check == 10) {
            return null;
        }
        return check;
    }
}
