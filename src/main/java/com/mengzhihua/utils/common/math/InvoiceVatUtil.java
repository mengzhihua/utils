package com.mengzhihua.utils.common.math;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 发票价税分离。常见税率 1% / 3% / 6% / 9% / 13%。
 * 样例：价税合计 113、税率 13% → 不含税 100、税额 13。
 */
public final class InvoiceVatUtil {

    private static final Set<String> RATES = Set.of("0.01", "0.03", "0.06", "0.09", "0.13", "1", "3", "6", "9", "13");

    private InvoiceVatUtil() {
    }

    public static Map<String, Object> split(Object amount, Object rate, boolean taxIncluded) {
        BigDecimal money = NumberUtil.toBigDecimal(amount);
        BigDecimal vat = normalizeRate(rate);
        if (money.signum() < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        BigDecimal exclusive;
        BigDecimal tax;
        BigDecimal inclusive;
        if (taxIncluded) {
            inclusive = money.setScale(2, RoundingMode.HALF_UP);
            exclusive = inclusive.divide(BigDecimal.ONE.add(vat), 2, RoundingMode.HALF_UP);
            tax = inclusive.subtract(exclusive);
        } else {
            exclusive = money.setScale(2, RoundingMode.HALF_UP);
            tax = exclusive.multiply(vat).setScale(2, RoundingMode.HALF_UP);
            inclusive = exclusive.add(tax);
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("rate", vat);
        data.put("taxIncluded", taxIncluded);
        data.put("exclusive", exclusive);
        data.put("tax", tax);
        data.put("inclusive", inclusive);
        data.put("rmb", ChineseNumberUtil.toRmb(inclusive));
        return data;
    }

    private static BigDecimal normalizeRate(Object rate) {
        BigDecimal value = NumberUtil.toBigDecimal(rate);
        String key = value.stripTrailingZeros().toPlainString();
        if (!RATES.contains(key)) {
            throw new IllegalArgumentException("unsupported VAT rate: " + rate);
        }
        if (value.compareTo(BigDecimal.ONE) > 0) {
            value = value.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
        }
        return value;
    }
}
