package com.mengzhihua.utils.common.i18n;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
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

    public static String dateTimeZone(String locale, String isoDateTime, String zone) {
        Locale tag = LocaleUtil.parse(locale);
        ZoneId zoneId = StringUtil.isBlank(zone) ? ZoneId.of("UTC") : ZoneId.of(zone.trim());
        ZonedDateTime zoned = zoned(isoDateTime, zoneId);
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)
                .withLocale(tag)
                .format(zoned);
    }

    public static String currencyName(String locale, String currency) {
        Locale tag = LocaleUtil.parse(locale);
        String code = StringUtil.isBlank(currency) ? "USD" : currency.trim().toUpperCase(Locale.ROOT);
        return Currency.getInstance(code).getDisplayName(tag);
    }

    public static String currencySymbol(String locale, String currency) {
        Locale tag = LocaleUtil.parse(locale);
        String code = StringUtil.isBlank(currency) ? "USD" : currency.trim().toUpperCase(Locale.ROOT);
        return Currency.getInstance(code).getSymbol(tag);
    }

    public static String list(String locale, String items) {
        Locale tag = LocaleUtil.parse(locale);
        List<String> values = new ArrayList<>();
        if (StringUtil.isNotBlank(items)) {
            for (String part : items.split("\\s*,\\s*")) {
                if (!part.isEmpty()) {
                    values.add(part);
                }
            }
        }
        return joinList(tag.getLanguage(), values);
    }

    private static ZonedDateTime zoned(String isoDateTime, ZoneId zoneId) {
        if (StringUtil.isBlank(isoDateTime)) {
            return ZonedDateTime.of(2026, 9, 18, 8, 15, 0, 0, zoneId);
        }
        String text = isoDateTime.trim();
        try {
            return ZonedDateTime.parse(text).withZoneSameInstant(zoneId);
        } catch (Exception ignored) {
            try {
                return LocalDateTime.parse(text).atZone(zoneId);
            } catch (Exception ignoredAgain) {
                return LocalDate.parse(text).atStartOfDay(zoneId);
            }
        }
    }

    private static String joinList(String language, List<String> values) {
        if (values.isEmpty()) {
            return "";
        }
        if (values.size() == 1) {
            return values.get(0);
        }
        String comma;
        String last;
        switch (language) {
            case "zh" -> {
                comma = "、";
                last = "和";
            }
            case "ja" -> {
                comma = "、";
                last = "、";
            }
            case "ko" -> {
                comma = ", ";
                last = " 및 ";
            }
            case "de" -> {
                comma = ", ";
                last = " und ";
            }
            case "fr" -> {
                comma = ", ";
                last = " et ";
            }
            default -> {
                comma = ", ";
                last = values.size() == 2 ? " and " : ", and ";
            }
        }
        if (values.size() == 2) {
            return values.get(0) + last + values.get(1);
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                builder.append(i == values.size() - 1 ? last : comma);
            }
            builder.append(values.get(i));
        }
        return builder.toString();
    }

    private static BigDecimal decimal(String value) {
        if (StringUtil.isBlank(value)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value.trim());
    }
}
