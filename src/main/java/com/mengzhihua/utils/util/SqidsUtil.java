package com.mengzhihua.utils.util;

/**
 * Sqids (sqids.org) reversible IDs — the Hashids successor, without a blocklist.
 */
public final class SqidsUtil {

    private static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Codec DEFAULT = new Codec(DEFAULT_ALPHABET, 0);

    private SqidsUtil() {
    }

    public static String encode(long... numbers) {
        return DEFAULT.encode(numbers);
    }

    public static long decodeOne(String id) {
        long[] numbers = decode(id);
        if (numbers.length != 1) {
            throw new IllegalArgumentException("id does not contain a single number");
        }
        return numbers[0];
    }

    public static long[] decode(String id) {
        return DEFAULT.decode(id);
    }

    public static Codec of(String alphabet, int minLength) {
        return new Codec(alphabet, minLength);
    }

    public static final class Codec {
        private final String alphabet;
        private final int minLength;

        private Codec(String alphabet, int minLength) {
            if (StringUtil.isBlank(alphabet) || alphabet.length() < 3) {
                throw new IllegalArgumentException("alphabet must contain at least 3 characters");
            }
            this.alphabet = shuffle(unique(alphabet));
            this.minLength = Math.max(0, minLength);
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
            return encodeNumbers(numbers, 0);
        }

        public long[] decode(String id) {
            if (StringUtil.isBlank(id)) {
                return new long[0];
            }
            for (int i = 0; i < id.length(); i++) {
                if (alphabet.indexOf(id.charAt(i)) < 0) {
                    return new long[0];
                }
            }
            char prefix = id.charAt(0);
            int offset = alphabet.indexOf(prefix);
            String alpha = alphabet.substring(offset) + alphabet.substring(0, offset);
            alpha = reverse(alpha);
            String sliced = id.substring(1);
            java.util.List<Long> numbers = new java.util.ArrayList<>();
            while (!sliced.isEmpty()) {
                char separator = alpha.charAt(0);
                String[] chunks = splitKeep(sliced, separator);
                if (chunks[0].isEmpty()) {
                    break;
                }
                numbers.add(toNumber(chunks[0], alpha.substring(1)));
                if (chunks.length > 1) {
                    alpha = shuffle(alpha);
                }
                sliced = join(chunks, 1, separator);
            }
            return numbers.stream().mapToLong(Long::longValue).toArray();
        }

        private String encodeNumbers(long[] numbers, int increment) {
            if (increment > alphabet.length()) {
                throw new IllegalStateException("reached max attempts to regenerate id");
            }
            long offset = numbers.length;
            for (int i = 0; i < numbers.length; i++) {
                offset += alphabet.charAt((int) (numbers[i] % alphabet.length())) + i;
            }
            offset = (offset + increment) % alphabet.length();
            String alpha = alphabet.substring((int) offset) + alphabet.substring(0, (int) offset);
            char prefix = alpha.charAt(0);
            alpha = reverse(alpha);
            StringBuilder result = new StringBuilder().append(prefix);
            for (int i = 0; i < numbers.length; i++) {
                result.append(toId(numbers[i], alpha.substring(1)));
                if (i + 1 < numbers.length) {
                    result.append(alpha.charAt(0));
                    alpha = shuffle(alpha);
                }
            }
            String id = result.toString();
            if (minLength > id.length()) {
                id += alpha.charAt(0);
                while (minLength - id.length() > 0) {
                    alpha = shuffle(alpha);
                    int take = Math.min(minLength - id.length(), alpha.length());
                    id += alpha.substring(0, take);
                }
            }
            return id;
        }

        private static String toId(long num, String alphabet) {
            StringBuilder id = new StringBuilder();
            long remaining = num;
            int len = alphabet.length();
            do {
                id.insert(0, alphabet.charAt((int) (remaining % len)));
                remaining /= len;
            } while (remaining > 0);
            return id.toString();
        }

        private static long toNumber(String id, String alphabet) {
            long number = 0;
            int len = alphabet.length();
            for (int i = 0; i < id.length(); i++) {
                int pos = alphabet.indexOf(id.charAt(i));
                if (pos < 0) {
                    return -1;
                }
                number = number * len + pos;
            }
            return number;
        }

        private static String shuffle(String alphabet) {
            char[] chars = alphabet.toCharArray();
            for (int i = 0, j = chars.length - 1; j > 0; i++, j--) {
                int r = (i * j + chars[i] + chars[j]) % chars.length;
                char tmp = chars[i];
                chars[i] = chars[r];
                chars[r] = tmp;
            }
            return new String(chars);
        }

        private static String reverse(String value) {
            return new StringBuilder(value).reverse().toString();
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

        private static String[] splitKeep(String value, char separator) {
            java.util.List<String> parts = new java.util.ArrayList<>();
            int from = 0;
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) == separator) {
                    parts.add(value.substring(from, i));
                    from = i + 1;
                }
            }
            parts.add(value.substring(from));
            return parts.toArray(String[]::new);
        }

        private static String join(String[] chunks, int from, char separator) {
            StringBuilder builder = new StringBuilder();
            for (int i = from; i < chunks.length; i++) {
                if (i > from) {
                    builder.append(separator);
                }
                builder.append(chunks[i]);
            }
            return builder.toString();
        }
    }
}
