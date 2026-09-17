package com.mengzhihua.utils.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Four-arithmetic expression evaluator ({@code 1+2*3}, parentheses, decimals).
 */
public final class ExprUtil {

    private ExprUtil() {
    }

    public static BigDecimal eval(String expression) {
        if (StringUtil.isBlank(expression)) {
            throw new IllegalArgumentException("expression is blank");
        }
        return new Parser(expression.replaceAll("\\s+", "")).parse();
    }

    public static String evalPlain(String expression) {
        return eval(expression).stripTrailingZeros().toPlainString();
    }

    private static final class Parser {
        private final String text;
        private int pos;

        private Parser(String text) {
            this.text = text;
        }

        private BigDecimal parse() {
            BigDecimal value = expression();
            if (pos < text.length()) {
                throw new IllegalArgumentException("unexpected token at " + pos + ": " + text);
            }
            return value;
        }

        private BigDecimal expression() {
            BigDecimal value = term();
            while (pos < text.length()) {
                char c = text.charAt(pos);
                if (c == '+') {
                    pos++;
                    value = value.add(term());
                } else if (c == '-') {
                    pos++;
                    value = value.subtract(term());
                } else {
                    break;
                }
            }
            return value;
        }

        private BigDecimal term() {
            BigDecimal value = factor();
            while (pos < text.length()) {
                char c = text.charAt(pos);
                if (c == '*') {
                    pos++;
                    value = value.multiply(factor());
                } else if (c == '/') {
                    pos++;
                    BigDecimal divisor = factor();
                    if (divisor.compareTo(BigDecimal.ZERO) == 0) {
                        throw new ArithmeticException("division by zero");
                    }
                    value = value.divide(divisor, 12, RoundingMode.HALF_UP);
                } else {
                    break;
                }
            }
            return value;
        }

        private BigDecimal factor() {
            if (pos >= text.length()) {
                throw new IllegalArgumentException("unexpected end of expression");
            }
            char c = text.charAt(pos);
            if (c == '+') {
                pos++;
                return factor();
            }
            if (c == '-') {
                pos++;
                return factor().negate();
            }
            if (c == '(') {
                pos++;
                BigDecimal value = expression();
                if (pos >= text.length() || text.charAt(pos) != ')') {
                    throw new IllegalArgumentException("missing )");
                }
                pos++;
                return value;
            }
            return number();
        }

        private BigDecimal number() {
            int start = pos;
            while (pos < text.length()) {
                char c = text.charAt(pos);
                if ((c >= '0' && c <= '9') || c == '.') {
                    pos++;
                } else {
                    break;
                }
            }
            if (start == pos) {
                throw new IllegalArgumentException("expected number at " + start + ": " + text);
            }
            return new BigDecimal(text.substring(start, pos), MathContext.UNLIMITED);
        }
    }
}
