package com.mengzhihua.utils.common.validate;


/**
 * Monaco n° TVA. Same checksum as French TVA, SIREN prefix {@code 000}.
 * Sample {@code 53 0000 04605} → {@code FR53000004605}.
 */
public final class McTvaUtil {

    private McTvaUtil() {
    }

    public static boolean isValid(String value) {
        String compact = normalize(value);
        return compact.length() == 11 && compact.substring(2, 5).equals("000") && FrTvaUtil.isValid(compact);
    }

    public static String complete(String body) {
        String compact = normalize(body);
        if (isValid(compact)) {
            return compact;
        }
        String siren = compact.replaceAll("\\D", "");
        if (siren.length() == 11) {
            siren = siren.substring(2);
        }
        if (siren.length() < 9) {
            siren = "000000000".substring(siren.length()) + siren;
        }
        if (siren.length() > 9) {
            siren = siren.substring(siren.length() - 9);
        }
        return FrTvaUtil.complete(siren);
    }

    public static String format(String value) {
        String compact = normalize(value);
        if (compact.length() != 11) {
            return value == null ? "" : value.trim();
        }
        return "FR " + FrTvaUtil.format(compact);
    }

    public static String normalize(String value) {
        return FrTvaUtil.normalize(value);
    }
}
