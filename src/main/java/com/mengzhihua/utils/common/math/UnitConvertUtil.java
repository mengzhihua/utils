package com.mengzhihua.utils.common.math;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Length / mass / temperature conversion without extra unit libraries.
 */
public final class UnitConvertUtil {

    private static final Map<String, BigDecimal> LENGTH_TO_M = Map.of(
            "mm", new BigDecimal("0.001"),
            "cm", new BigDecimal("0.01"),
            "m", BigDecimal.ONE,
            "km", new BigDecimal("1000"),
            "in", new BigDecimal("0.0254"),
            "ft", new BigDecimal("0.3048")
    );

    private static final Map<String, BigDecimal> MASS_TO_G = Map.of(
            "mg", new BigDecimal("0.001"),
            "g", BigDecimal.ONE,
            "kg", new BigDecimal("1000"),
            "t", new BigDecimal("1000000"),
            "lb", new BigDecimal("453.59237")
    );

    private UnitConvertUtil() {
    }

    public static BigDecimal convert(Object value, String from, String to) {
        return convert(value, from, to, 8);
    }

    public static BigDecimal convert(Object value, String from, String to, int scale) {
        String src = norm(from);
        String dest = norm(to);
        BigDecimal amount = NumberUtil.toBigDecimal(value);
        if (LENGTH_TO_M.containsKey(src) && LENGTH_TO_M.containsKey(dest)) {
            return amount.multiply(LENGTH_TO_M.get(src))
                    .divide(LENGTH_TO_M.get(dest), scale, RoundingMode.HALF_UP);
        }
        if (MASS_TO_G.containsKey(src) && MASS_TO_G.containsKey(dest)) {
            return amount.multiply(MASS_TO_G.get(src))
                    .divide(MASS_TO_G.get(dest), scale, RoundingMode.HALF_UP);
        }
        if (isTemp(src) && isTemp(dest)) {
            return convertTemp(amount, src, dest, scale);
        }
        throw new IllegalArgumentException("cannot convert " + from + " to " + to);
    }

    public static String format(Object value, String from, String to) {
        return convert(value, from, to).stripTrailingZeros().toPlainString() + " " + norm(to);
    }

    private static BigDecimal convertTemp(BigDecimal amount, String from, String to, int scale) {
        BigDecimal celsius = switch (from) {
            case "c" -> amount;
            case "f" -> amount.subtract(new BigDecimal("32"))
                    .multiply(new BigDecimal("5")).divide(new BigDecimal("9"), 12, RoundingMode.HALF_UP);
            case "k" -> amount.subtract(new BigDecimal("273.15"));
            default -> throw new IllegalArgumentException("unknown temperature: " + from);
        };
        BigDecimal result = switch (to) {
            case "c" -> celsius;
            case "f" -> celsius.multiply(new BigDecimal("9"))
                    .divide(new BigDecimal("5"), 12, RoundingMode.HALF_UP).add(new BigDecimal("32"));
            case "k" -> celsius.add(new BigDecimal("273.15"));
            default -> throw new IllegalArgumentException("unknown temperature: " + to);
        };
        return result.setScale(scale, RoundingMode.HALF_UP);
    }

    private static boolean isTemp(String unit) {
        return "c".equals(unit) || "f".equals(unit) || "k".equals(unit);
    }

    private static String norm(String unit) {
        if (StringUtil.isBlank(unit)) {
            throw new IllegalArgumentException("unit is blank");
        }
        String text = unit.trim().toLowerCase(Locale.ROOT);
        return switch (text) {
            case "celsius", "℃", "degc" -> "c";
            case "fahrenheit", "℉", "degf" -> "f";
            case "kelvin" -> "k";
            case "inch", "inchs", "inches" -> "in";
            case "foot", "feet" -> "ft";
            case "pound", "lbs" -> "lb";
            case "ton", "tonne" -> "t";
            default -> text;
        };
    }
}
