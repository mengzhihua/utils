package com.mengzhihua.utils.common.math;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 商业贷款：等额本息 / 等额本金。样例：100 万、年化 4.2%、30 年等额本息。
 */
public final class MortgageUtil {

    private MortgageUtil() {
    }

    public static Map<String, Object> calculate(Object principal, Object annualRatePercent, int years, String mode) {
        BigDecimal loan = NumberUtil.toBigDecimal(principal);
        BigDecimal annual = NumberUtil.toBigDecimal(annualRatePercent).divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
        if (loan.signum() <= 0 || years < 1 || years > 50) {
            throw new IllegalArgumentException("principal and years are invalid");
        }
        int months = years * 12;
        String kind = mode == null ? "installment" : mode.trim().toLowerCase();
        if ("principal".equals(kind) || "equal-principal".equals(kind)) {
            return equalPrincipal(loan, annual, months);
        }
        return equalInstallment(loan, annual, months);
    }

    public static Map<String, Object> equalInstallment(BigDecimal principal, BigDecimal annualRate, int months) {
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12"), 16, RoundingMode.HALF_UP);
        BigDecimal payment;
        if (monthlyRate.signum() == 0) {
            payment = principal.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        } else {
            BigDecimal factor = monthlyRate.add(BigDecimal.ONE).pow(months);
            payment = principal.multiply(monthlyRate).multiply(factor)
                    .divide(factor.subtract(BigDecimal.ONE), 2, RoundingMode.HALF_UP);
        }
        BigDecimal total = payment.multiply(BigDecimal.valueOf(months)).setScale(2, RoundingMode.HALF_UP);
        return result("installment", principal, months, payment, total.subtract(principal).setScale(2, RoundingMode.HALF_UP),
                total, payment, lastInstallment(principal, monthlyRate, months, payment));
    }

    public static Map<String, Object> equalPrincipal(BigDecimal principal, BigDecimal annualRate, int months) {
        BigDecimal monthlyRate = annualRate.divide(new BigDecimal("12"), 16, RoundingMode.HALF_UP);
        BigDecimal monthlyPrincipal = principal.divide(BigDecimal.valueOf(months), 2, RoundingMode.HALF_UP);
        BigDecimal first = monthlyPrincipal.add(principal.multiply(monthlyRate)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal remaining = principal.subtract(monthlyPrincipal.multiply(BigDecimal.valueOf(months - 1)));
        BigDecimal lastInterest = remaining.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal last = remaining.add(lastInterest).setScale(2, RoundingMode.HALF_UP);
        BigDecimal interest = BigDecimal.ZERO;
        BigDecimal balance = principal;
        for (int i = 0; i < months; i++) {
            BigDecimal part = i == months - 1 ? balance : monthlyPrincipal;
            interest = interest.add(balance.multiply(monthlyRate));
            balance = balance.subtract(part);
        }
        interest = interest.setScale(2, RoundingMode.HALF_UP);
        return result("principal", principal, months, first, interest, principal.add(interest).setScale(2, RoundingMode.HALF_UP),
                first, last);
    }

    private static BigDecimal lastInstallment(BigDecimal principal, BigDecimal monthlyRate, int months, BigDecimal payment) {
        BigDecimal balance = principal;
        for (int i = 0; i < months - 1; i++) {
            balance = balance.multiply(monthlyRate.add(BigDecimal.ONE)).subtract(payment);
        }
        return balance.multiply(monthlyRate.add(BigDecimal.ONE)).setScale(2, RoundingMode.HALF_UP);
    }

    private static Map<String, Object> result(String mode, BigDecimal principal, int months, BigDecimal first,
                                              BigDecimal interest, BigDecimal total, BigDecimal monthly, BigDecimal last) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("mode", mode);
        data.put("principal", principal.setScale(2, RoundingMode.HALF_UP));
        data.put("months", months);
        data.put("monthly", monthly);
        data.put("firstMonth", first);
        data.put("lastMonth", last);
        data.put("interest", interest);
        data.put("total", total);
        return data;
    }
}
