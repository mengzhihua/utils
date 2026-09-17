package com.mengzhihua.utils.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Weighted random pick.
 */
public final class WeightRandomUtil {

    private WeightRandomUtil() {
    }

    public static <T> T pick(Map<T, ? extends Number> weights) {
        if (MapUtil.isEmpty(weights)) {
            throw new IllegalArgumentException("weights is empty");
        }
        List<T> items = new ArrayList<>();
        List<Double> cumulative = new ArrayList<>();
        double total = 0D;
        for (Map.Entry<T, ? extends Number> entry : weights.entrySet()) {
            double weight = entry.getValue() == null ? 0D : entry.getValue().doubleValue();
            if (weight <= 0) {
                continue;
            }
            total += weight;
            items.add(entry.getKey());
            cumulative.add(total);
        }
        if (items.isEmpty() || total <= 0) {
            throw new IllegalArgumentException("no positive weights");
        }
        double target = ThreadLocalRandom.current().nextDouble() * total;
        for (int i = 0; i < items.size(); i++) {
            if (target < cumulative.get(i)) {
                return items.get(i);
            }
        }
        return items.get(items.size() - 1);
    }
}
