package com.mengzhihua.utils.common.i18n;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Currency;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Locale-aware number, currency, percent and date formatting.
 */
public final class I18nFormatUtil {

    private I18nFormatUtil() {
    }

    public static String number(String locale, String value) {
        Locale tag = LocaleUtil.parse(locale);
        NumberFormat format = NumberFormat.getNumberInstance(tag);
        return format.format(decimal(value));
    }

    public static String currency(String locale, String value, String currency) {
        Locale tag = LocaleUtil.parse(locale);
        NumberFormat format = NumberFormat.getCurrencyInstance(tag);
        if (StringUtil.isNotBlank(currency)) {
            format.setCurrency(Currency.getInstance(currency.trim().toUpperCase(Locale.ROOT)));
        }
        return format.format(decimal(value));
    }

    public static String percent(String locale, String value) {
        Locale tag = LocaleUtil.parse(locale);
        NumberFormat format = NumberFormat.getPercentInstance(tag);
        format.setMaximumFractionDigits(2);
        return format.format(decimal(value));
    }

    public static String date(String locale, String isoDate) {
        Locale tag = LocaleUtil.parse(locale);
        LocalDate date = StringUtil.isBlank(isoDate) ? LocalDate.of(2026, 9, 18) : LocalDate.parse(isoDate.trim());
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(tag).format(date);
    }

    public static String dateTime(String locale, String isoDateTime) {
        Locale tag = LocaleUtil.parse(locale);
        LocalDateTime dateTime = StringUtil.isBlank(isoDateTime)
                ? LocalDateTime.of(2026, 9, 18, 8, 15)
                : LocalDateTime.parse(isoDateTime.trim());
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT).withLocale(tag).format(dateTime);
    }

    private static BigDecimal decimal(String value) {
        if (StringUtil.isBlank(value)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value.trim());
    }
}
