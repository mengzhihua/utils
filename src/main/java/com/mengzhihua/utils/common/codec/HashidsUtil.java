package com.mengzhihua.utils.common.codec;


import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Hashids-style reversible numeric IDs. Not cryptographic.
 */
public final class HashidsUtil {

    private static final Codec DEFAULT = new Codec("utils", 0);

    private HashidsUtil() {
    }

    public static String encode(long id) {
        return DEFAULT.encode(id);
    }

    public static String encode(long... numbers) {
        return DEFAULT.encode(numbers);
    }

    public static long decodeOne(String hash) {
        long[] numbers = decode(hash);
        if (numbers.length != 1) {
            throw new IllegalArgumentException("hash does not contain a single id");
        }
        return numbers[0];
    }

    public static long[] decode(String hash) {
        return DEFAULT.decode(hash);
    }

    public static Codec of(String salt, int minLength) {
        return new Codec(salt, minLength);
    }

    public static final class Codec {
        private static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        private static final String DEFAULT_SEPS = "cfhistuCFHISTU";
        private static final double SEP_DIV = 3.5;
        private static final double GUARD_DIV = 12;

        private final String salt;
        private final String alphabet;
        private final String seps;
        private final String guards;
        private final int minLength;

        private Codec(String salt, int minLength) {
            this.salt = salt == null ? "" : salt;
            this.minLength = Math.max(0, minLength);
            String alphabet = DEFAULT_ALPHABET;
            StringBuilder sepBuilder = new StringBuilder();
            for (int i = 0; i < DEFAULT_SEPS.length(); i++) {
                char c = DEFAULT_SEPS.charAt(i);
                if (alphabet.indexOf(c) >= 0) {
                    sepBuilder.append(c);
                    alphabet = alphabet.replace(String.valueOf(c), "");
                }
            }
            alphabet = unique(alphabet);
            if (alphabet.length() < 16) {
                throw new IllegalArgumentException("alphabet must contain at least 16 unique characters");
            }
            String shuffledSeps = consistentShuffle(sepBuilder.toString(), this.salt);
            if (shuffledSeps.isEmpty() || alphabet.length() / (double) shuffledSeps.length() > SEP_DIV) {
                int sepsLen = (int) Math.ceil(alphabet.length() / SEP_DIV);
                if (sepsLen == 1) {
                    sepsLen = 2;
                }
                if (sepsLen > shuffledSeps.length()) {
                    int diff = sepsLen - shuffledSeps.length();
                    shuffledSeps += alphabet.substring(0, diff);
                    alphabet = alphabet.substring(diff);
                } else {
                    shuffledSeps = shuffledSeps.substring(0, sepsLen);
                }
            }
            alphabet = consistentShuffle(alphabet, this.salt);
            int guardCount = (int) Math.ceil(alphabet.length() / GUARD_DIV);
            String guards;
            if (alphabet.length() < 3) {
                guards = shuffledSeps.substring(0, guardCount);
                shuffledSeps = shuffledSeps.substring(guardCount);
            } else {
                guards = alphabet.substring(0, guardCount);
                alphabet = alphabet.substring(guardCount);
            }
            this.alphabet = alphabet;
            this.seps = shuffledSeps;
            this.guards = guards;
        }

        public String encode(long... numbers) {
            if (numbers == null || numbers.length == 0) {
                return "";
            }
            for (long number : numbers) {
                if (number < 0) {
                    throw new IllegalArgumentException("numbers must be >= 0");
                }
            }
            long numbersHashInt = 0;
            for (int i = 0; i < numbers.length; i++) {
                numbersHashInt += numbers[i] % (i + 100);
            }
            String alphabet = this.alphabet;
            char lottery = alphabet.charAt((int) (numbersHashInt % alphabet.length()));
            StringBuilder result = new StringBuilder().append(lottery);
            for (int i = 0; i < numbers.length; i++) {
                long number = numbers[i];
                String buffer = lottery + salt + alphabet;
                alphabet = consistentShuffle(alphabet, buffer.substring(0, alphabet.length()));
                String last = hash(number, alphabet);
                result.append(last);
                if (i + 1 < numbers.length) {
                    number %= last.charAt(0) + i;
                    result.append(seps.charAt((int) (number % seps.length())));
                }
            }
            String encoded = result.toString();
            if (encoded.length() < minLength) {
                long guardIndex = (numbersHashInt + encoded.charAt(0)) % guards.length();
                encoded = guards.charAt((int) guardIndex) + encoded;
                if (encoded.length() < minLength) {
                    guardIndex = (numbersHashInt + encoded.charAt(2)) % guards.length();
                    encoded += guards.charAt((int) guardIndex);
                }
            }
            int half = alphabet.length() / 2;
            while (encoded.length() < minLength) {
                alphabet = consistentShuffle(alphabet, alphabet);
                encoded = alphabet.substring(half) + encoded + alphabet.substring(0, half);
                int excess = encoded.length() - minLength;
                if (excess > 0) {
                    int fromIndex = excess / 2;
                    encoded = encoded.substring(fromIndex, fromIndex + minLength);
                }
            }
            return encoded;
        }

        public long[] decode(String hash) {
            if (StringUtil.isBlank(hash)) {
                return new long[0];
            }
            String alphabet = this.alphabet;
            String hashStr = hash;
            for (int i = 0; i < guards.length(); i++) {
                hashStr = hashStr.replace(String.valueOf(guards.charAt(i)), " ");
            }
            String[] hashArray = hashStr.trim().split(" ");
            if (hashArray.length == 3 || hashArray.length == 2) {
                hashStr = hashArray[1];
            } else {
                hashStr = hashArray[0];
            }
            if (hashStr.isEmpty()) {
                return new long[0];
            }
            char lottery = hashStr.charAt(0);
            hashStr = hashStr.substring(1);
            for (int i = 0; i < seps.length(); i++) {
                hashStr = hashStr.replace(String.valueOf(seps.charAt(i)), " ");
            }
            String[] parts = hashStr.split(" ");
            java.util.List<Long> numbers = new java.util.ArrayList<>();
            for (String part : parts) {
                if (part.isEmpty()) {
                    continue;
                }
                String buffer = lottery + salt + alphabet;
                alphabet = consistentShuffle(alphabet, buffer.substring(0, alphabet.length()));
                numbers.add(unhash(part, alphabet));
            }
            long[] result = numbers.stream().mapToLong(Long::longValue).toArray();
            if (!encode(result).equals(hash)) {
                return new long[0];
            }
            return result;
        }

        private static String hash(long input, String alphabet) {
            StringBuilder builder = new StringBuilder();
            int len = alphabet.length();
            do {
                builder.append(alphabet.charAt((int) (input % len)));
                input /= len;
            } while (input > 0);
            return builder.reverse().toString();
        }

        private static long unhash(String input, String alphabet) {
            long number = 0;
            int len = alphabet.length();
            for (int i = 0; i < input.length(); i++) {
                int pos = alphabet.indexOf(input.charAt(i));
                if (pos < 0) {
                    return -1;
                }
                number = number * len + pos;
            }
            return number;
        }
    }

    private static String consistentShuffle(String alphabet, String salt) {
        if (alphabet == null || alphabet.isEmpty() || salt == null || salt.isEmpty()) {
            return alphabet;
        }
        char[] chars = alphabet.toCharArray();
        for (int i = chars.length - 1, v = 0, p = 0; i > 0; i--, v++) {
            v %= salt.length();
            int integer = salt.charAt(v);
            p += integer;
            int j = (integer + v + p) % i;
            char tmp = chars[j];
            chars[j] = chars[i];
            chars[i] = tmp;
        }
        return new String(chars);
    }

    private static String unique(String alphabet) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < alphabet.length(); i++) {
            char c = alphabet.charAt(i);
            if (builder.indexOf(String.valueOf(c)) < 0) {
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
