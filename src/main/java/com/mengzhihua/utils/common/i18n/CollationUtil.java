package com.mengzhihua.utils.common.i18n;


import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Locale-aware collation.
 */
public final class CollationUtil {

    private CollationUtil() {
    }

    public static int compare(String locale, String left, String right) {
        Collator collator = Collator.getInstance(LocaleUtil.parse(locale));
        collator.setStrength(Collator.SECONDARY);
        return collator.compare(left == null ? "" : left, right == null ? "" : right);
    }

    public static List<String> sort(String locale, String items) {
        List<String> values = new ArrayList<>();
        if (StringUtil.isNotBlank(items)) {
            values.addAll(Arrays.asList(items.split("\\s*,\\s*")));
        }
        values.sort((a, b) -> compare(locale, a, b));
        return List.copyOf(values);
    }
}
