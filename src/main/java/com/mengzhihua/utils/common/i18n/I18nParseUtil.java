package com.mengzhihua.utils.common.i18n;


import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.Locale;

import com.mengzhihua.utils.common.lang.StringUtil;

/**
 * Parse locale-formatted numbers and dates back to canonical values.
 */
public final class I18nParseUtil {

    private I18nParseUtil() {
    }

    public static BigDecimal number(String locale, String text) {
        if (StringUtil.isBlank(text)) {
            return BigDecimal.ZERO;
        }
        Locale tag = LocaleUtil.parse(locale);
        try {
            Number parsed = NumberFormat.getNumberInstance(tag).parse(text.trim());
            return new BigDecimal(parsed.toString());
        } catch (ParseException ex) {
            throw new IllegalArgumentException("cannot parse number: " + text, ex);
        }
    }

    public static LocalDate date(String locale, String text) {
        if (StringUtil.isBlank(text)) {
            throw new IllegalArgumentException("date is blank");
        }
        Locale tag = LocaleUtil.parse(locale);
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(tag);
        try {
            return LocalDate.parse(text.trim(), formatter);
        } catch (DateTimeParseException ex) {
            try {
                return LocalDate.parse(text.trim());
            } catch (DateTimeParseException ignored) {
                throw new IllegalArgumentException("cannot parse date: " + text, ex);
            }
        }
    }
}
