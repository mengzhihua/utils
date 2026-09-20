package com.mengzhihua.utils.common.math;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 中国综合所得个人所得税（累计预扣预缴，2018 税率表）。
 * 起征点每月 5000 元。样例：月薪 30000、无专项 → 首月税额 750。
 */
public final class PitUtil {

    private static final BigDecimal THRESHOLD = new BigDecimal("5000");
    private static final BigDecimal[][] BRACKETS = {
            {new BigDecimal("36000"), new BigDecimal("0.03"), BigDecimal.ZERO},
            {new BigDecimal("144000"), new BigDecimal("0.10"), new BigDecimal("2520")},
            {new BigDecimal("300000"), new BigDecimal("0.20"), new BigDecimal("16920")},
            {new BigDecimal("420000"), new BigDecimal("0.25"), new BigDecimal("31920")},
            {new BigDecimal("660000"), new BigDecimal("0.30"), new BigDecimal("52920")},
            {new BigDecimal("960000"), new BigDecimal("0.35"), new BigDecimal("85920")},
            {null, new BigDecimal("0.45"), new BigDecimal("181920")}
    };

    private PitUtil() {
    }

    public static Map<String, Object> estimate(Object monthlyIncome, Object insurance, Object special, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("month must be 1-12");
        }
        BigDecimal income = money(monthlyIncome);
        BigDecimal social = money(insurance);
        BigDecimal extra = money(special);
        BigDecimal months = BigDecimal.valueOf(month);
        BigDecimal taxable = income.multiply(months)
                .subtract(social.multiply(months))
                .subtract(extra.multiply(months))
                .subtract(THRESHOLD.multiply(months));
        if (taxable.signum() < 0) {
            taxable = BigDecimal.ZERO;
        }
        BigDecimal[] bracket = bracket(taxable);
        BigDecimal tax = taxable.multiply(bracket[1]).subtract(bracket[2]).setScale(2, RoundingMode.HALF_UP);
        if (tax.signum() < 0) {
            tax = BigDecimal.ZERO;
        }
        BigDecimal net = income.subtract(social).subtract(tax).setScale(2, RoundingMode.HALF_UP);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("month", month);
        data.put("monthlyIncome", income);
        data.put("insurance", social);
        data.put("special", extra);
        data.put("taxableCumulative", taxable.setScale(2, RoundingMode.HALF_UP));
        data.put("rate", bracket[1]);
        data.put("quickDeduction", bracket[2]);
        data.put("taxCumulative", tax);
        data.put("netMonthly", net);
        return data;
    }

    private static BigDecimal[] bracket(BigDecimal taxable) {
        for (BigDecimal[] row : BRACKETS) {
            if (row[0] == null || taxable.compareTo(row[0]) <= 0) {
                return row;
            }
        }
        return BRACKETS[BRACKETS.length - 1];
    }

    private static BigDecimal money(Object value) {
        BigDecimal amount = NumberUtil.toBigDecimal(value == null || value.toString().isBlank() ? "0" : value);
        if (amount.signum() < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
