package com.mengzhihua.utils.util;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * TypeID (typeid spec): {@code prefix_ + crockford-base32(UUIDv7)}.
 */
public final class TypeIdUtil {

    private static final char[] ALPHABET = "0123456789abcdefghjkmnpqrstvwxyz".toCharArray();
    private static final Pattern PREFIX = Pattern.compile("^[a-z][a-z0-9_]{0,62}$");

    private TypeIdUtil() {
    }

    public static String next(String prefix) {
        return of(prefix, IdUtil.uuidV7());
    }

    public static String of(String prefix, String uuid) {
        if (prefix == null || !PREFIX.matcher(prefix).matches()) {
            throw new IllegalArgumentException("invalid typeid prefix");
        }
        return prefix + "_" + encode(uuid);
    }

    public static boolean isValid(String typeId) {
        if (StringUtil.isBlank(typeId)) {
            return false;
        }
        int sep = typeId.lastIndexOf('_');
        if (sep <= 0 || sep == typeId.length() - 1) {
            return false;
        }
        String prefix = typeId.substring(0, sep);
        String suffix = typeId.substring(sep + 1);
        return PREFIX.matcher(prefix).matches() && suffix.length() == 26 && decode(suffix) != null;
    }

    public static String prefix(String typeId) {
        int sep = typeId == null ? -1 : typeId.lastIndexOf('_');
        return sep <= 0 ? "" : typeId.substring(0, sep);
    }

    private static String encode(String uuid) {
        String hex = uuid.replace("-", "").toLowerCase(Locale.ROOT);
        if (hex.length() != 32) {
            throw new IllegalArgumentException("uuid must be 32 hex chars");
        }
        java.math.BigInteger value = new java.math.BigInteger(hex, 16);
        char[] out = new char[26];
        for (int i = 25; i >= 0; i--) {
            java.math.BigInteger[] div = value.divideAndRemainder(java.math.BigInteger.valueOf(32));
            out[i] = ALPHABET[div[1].intValue()];
            value = div[0];
        }
        return new String(out);
    }

    private static byte[] decode(String suffix) {
        java.math.BigInteger value = java.math.BigInteger.ZERO;
        for (int i = 0; i < suffix.length(); i++) {
            int index = indexOf(suffix.charAt(i));
            if (index < 0) {
                return null;
            }
            value = value.multiply(java.math.BigInteger.valueOf(32)).add(java.math.BigInteger.valueOf(index));
        }
        return value.toByteArray();
    }

    private static int indexOf(char c) {
        for (int i = 0; i < ALPHABET.length; i++) {
            if (ALPHABET[i] == c) {
                return i;
            }
        }
        return -1;
    }
}
