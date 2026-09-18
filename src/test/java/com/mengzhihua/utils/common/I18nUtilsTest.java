package com.mengzhihua.utils.common;


import java.util.List;
import java.util.Locale;

import com.mengzhihua.utils.common.i18n.AcceptLanguageUtil;
import com.mengzhihua.utils.common.i18n.CollationUtil;
import com.mengzhihua.utils.common.i18n.I18nFormatUtil;
import com.mengzhihua.utils.common.i18n.I18nUtil;
import com.mengzhihua.utils.common.i18n.LocaleUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class I18nUtilsTest {

    @Test
    void bundleAndLocale() {
        assertEquals("Hello, Ada", I18nUtil.get("hello", Locale.ENGLISH, "Ada"));
        assertEquals("你好，Ada", I18nUtil.get("hello", Locale.SIMPLIFIED_CHINESE, "Ada"));
        assertEquals("こんにちは、Ada", I18nUtil.get("hello", Locale.JAPANESE, "Ada"));
        assertEquals("Hallo, Ada", I18nUtil.get("hello", Locale.GERMANY, "Ada"));
        assertEquals("Bonjour, Ada", I18nUtil.get("hello", Locale.FRANCE, "Ada"));
        assertEquals("안녕하세요, Ada", I18nUtil.get("hello", Locale.KOREA, "Ada"));
        assertEquals("success", I18nUtil.get("result.success", "en"));
        assertEquals("成功", I18nUtil.get("result.success", "zh-CN"));
        assertEquals("Erfolg", I18nUtil.get("result.success", "de"));
        assertEquals("missing parameter: value", I18nUtil.get("error.missing_parameter", Locale.ENGLISH, "value"));
        assertEquals("缺少参数：value", I18nUtil.get("error.missing_parameter", Locale.SIMPLIFIED_CHINESE, "value"));
        assertEquals("zh-CN", LocaleUtil.toTag(LocaleUtil.parse("zh_CN")));
        assertTrue(LocaleUtil.supported("zh-CN"));
        assertFalse(LocaleUtil.supported("xx-YY"));
        assertTrue(LocaleUtil.displayName("zh-CN", "en").toLowerCase(Locale.ROOT).contains("chinese"));
    }

    @Test
    void formatSortAcceptLanguage() {
        String us = I18nFormatUtil.number("en-US", "1234.5");
        String de = I18nFormatUtil.number("de-DE", "1234.5");
        assertTrue(us.contains("1,234") || us.contains("1234"));
        assertTrue(de.contains("1.234") || de.contains("1234"));
        assertTrue(I18nFormatUtil.currency("zh-CN", "1234.5", "CNY").contains("1,234.50")
                || I18nFormatUtil.currency("zh-CN", "1234.5", "CNY").contains("1234"));
        assertEquals(List.of("Zebra", "äpfel", "öl"), CollationUtil.sort("sv", "äpfel,Zebra,öl"));
        assertEquals("zh-CN", AcceptLanguageUtil.first("zh-CN,zh;q=0.9,en;q=0.8"));
        assertEquals("zh-CN", AcceptLanguageUtil.negotiate("zh-CN,en;q=0.8", List.of(Locale.SIMPLIFIED_CHINESE, Locale.ENGLISH))
                .toLanguageTag());
        assertEquals("en", AcceptLanguageUtil.negotiate("fr;q=0.2,en;q=0.8", List.of(Locale.ENGLISH, Locale.SIMPLIFIED_CHINESE))
                .getLanguage());
        assertEquals("apples, oranges, and pears", I18nFormatUtil.list("en", "apples,oranges,pears"));
        assertEquals("apples、oranges和pears", I18nFormatUtil.list("zh-CN", "apples,oranges,pears"));
        assertEquals("apples, oranges und pears", I18nFormatUtil.list("de-DE", "apples,oranges,pears"));
        String cny = I18nFormatUtil.currencyName("zh-CN", "CNY");
        assertTrue(cny.contains("人民") || cny.toLowerCase(Locale.ROOT).contains("yuan"));
        String berlin = I18nFormatUtil.dateTimeZone("de-DE", "2026-09-18T08:15:00", "Europe/Berlin");
        assertTrue(berlin.contains("18") || berlin.contains("Sep") || berlin.contains("2026"));
    }
}
