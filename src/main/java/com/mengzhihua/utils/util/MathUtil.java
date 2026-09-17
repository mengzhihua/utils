package com.mengzhihua.utils.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Integer / decimal math: gcd, primes, combinations, square root.
 */
public final class MathUtil {

    private MathUtil() {
    }

    public static long gcd(long a, long b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b != 0) {
            long t = a % b;
            a = b;
            b = t;
        }
        return a;
    }

    public static long lcm(long a, long b) {
        if (a == 0 || b == 0) {
            return 0;
        }
        return Math.multiplyExact(Math.abs(a) / gcd(a, b), Math.abs(b));
    }

    public static boolean isPrime(long n) {
        if (n < 2) {
            return false;
        }
        if (n % 2 == 0) {
            return n == 2;
        }
        long limit = (long) Math.sqrt(n);
        for (long i = 3; i <= limit; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static BigInteger factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        if (n > 1000) {
            throw new IllegalArgumentException("n is too large");
        }
        BigInteger result = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            result = result.multiply(BigInteger.valueOf(i));
        }
        return result;
    }

    /**
     * Arrangement P(n, m) = n! / (n-m)!.
     */
    public static BigInteger arrangement(int n, int m) {
        checkChoose(n, m);
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < m; i++) {
            result = result.multiply(BigInteger.valueOf(n - i));
        }
        return result;
    }

    /**
     * Combination C(n, m) = n! / (m! (n-m)!).
     */
    public static BigInteger combination(int n, int m) {
        checkChoose(n, m);
        m = Math.min(m, n - m);
        BigInteger result = BigInteger.ONE;
        for (int i = 0; i < m; i++) {
            result = result.multiply(BigInteger.valueOf(n - i))
                    .divide(BigInteger.valueOf(i + 1));
        }
        return result;
    }

    public static BigDecimal sqrt(Object value, int scale) {
        BigDecimal number = NumberUtil.toBigDecimal(value);
        if (number.signum() < 0) {
            throw new ArithmeticException("sqrt of negative");
        }
        if (scale < 0) {
            throw new IllegalArgumentException("scale must be >= 0");
        }
        return number.sqrt(new MathContext(scale + 8, RoundingMode.HALF_UP))
                .setScale(scale, RoundingMode.HALF_UP);
    }

    public static boolean isEven(long n) {
        return (n & 1) == 0;
    }

    public static boolean isOdd(long n) {
        return (n & 1) == 1;
    }

    public static long clamp(long value, long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min must be <= max");
        }
        return Math.min(max, Math.max(min, value));
    }

    private static void checkChoose(int n, int m) {
        if (n < 0 || m < 0 || m > n) {
            throw new IllegalArgumentException("require 0 <= m <= n");
        }
    }
}
