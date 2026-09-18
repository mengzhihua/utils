package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.i18n.AcceptLanguageUtil;
import com.mengzhihua.utils.common.i18n.CollationUtil;
import com.mengzhihua.utils.common.i18n.I18nFormatUtil;
import com.mengzhihua.utils.common.i18n.I18nUtil;
import com.mengzhihua.utils.common.i18n.LocaleUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "I18n Demo", description = "Locale / MessageSource / format")
public class I18nUtilsDemoController {

    private final MessageSource messageSource;

    public I18nUtilsDemoController(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @GetMapping("/i18n/message")
    @Operation(summary = "资源包文案")
    public Result<Map<String, Object>> message(
            @RequestParam(defaultValue = "hello") String key,
            @RequestParam(defaultValue = "Ada") String arg,
            @RequestParam(required = false) String locale,
            Locale requestLocale) {
        Locale resolved = locale == null ? requestLocale : LocaleUtil.parse(locale);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("key", key);
        data.put("locale", resolved.toLanguageTag());
        data.put("bundle", I18nUtil.get(key, resolved, arg));
        data.put("messageSource", messageSource.getMessage(key, new Object[] {arg}, resolved));
        data.put("success", I18nUtil.get("result.success", resolved));
        return Result.ok(I18nUtil.get("result.success", resolved), data);
    }

    @GetMapping("/i18n/locale")
    @Operation(summary = "解析语言标签")
    public Result<Map<String, Object>> localeTag(
            @RequestParam(defaultValue = "zh-CN") String tag,
            @RequestParam(defaultValue = "en") String inLocale) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("tag", LocaleUtil.toTag(LocaleUtil.parse(tag)));
        data.put("displayName", LocaleUtil.displayName(tag, inLocale));
        data.put("languageName", LocaleUtil.languageName(tag, inLocale));
        data.put("countryName", LocaleUtil.countryName(tag, inLocale));
        data.put("supported", LocaleUtil.supported(tag));
        data.put("supportedTags", LocaleUtil.supportedTags());
        return Result.ok(data);
    }

    @GetMapping("/i18n/format")
    @Operation(summary = "本地化数字日期")
    public Result<Map<String, Object>> format(
            @RequestParam(defaultValue = "zh-CN") String locale,
            @RequestParam(defaultValue = "1234.5") String amount,
            @RequestParam(defaultValue = "CNY") String currency,
            @RequestParam(defaultValue = "2026-09-18") String date,
            @RequestParam(defaultValue = "Europe/Berlin") String zone,
            @RequestParam(defaultValue = "apples,oranges,pears") String items) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("number", I18nFormatUtil.number(locale, amount));
        data.put("currency", I18nFormatUtil.currency(locale, amount, currency));
        data.put("currencyName", I18nFormatUtil.currencyName(locale, currency));
        data.put("currencySymbol", I18nFormatUtil.currencySymbol(locale, currency));
        data.put("percent", I18nFormatUtil.percent(locale, "0.125"));
        data.put("date", I18nFormatUtil.date(locale, date));
        data.put("dateTimeZone", I18nFormatUtil.dateTimeZone(locale, date + "T08:15:00", zone));
        data.put("list", I18nFormatUtil.list(locale, items));
        return Result.ok(data);
    }

    @GetMapping("/i18n/sort")
    @Operation(summary = "本地化排序")
    public Result<Map<String, Object>> sort(
            @RequestParam(defaultValue = "sv") String locale,
            @RequestParam(defaultValue = "äpfel,Zebra,öl") String items) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("sorted", CollationUtil.sort(locale, items));
        return Result.ok(data);
    }

    @GetMapping("/accept-language")
    @Operation(summary = "HTTP Accept-Language")
    public Result<Map<String, Object>> acceptLanguage(
            @RequestParam(defaultValue = "zh-CN,zh;q=0.9,en;q=0.8") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("first", AcceptLanguageUtil.first(header));
        data.put("negotiated", AcceptLanguageUtil.negotiate(header, List.of(Locale.SIMPLIFIED_CHINESE, Locale.ENGLISH))
                .toLanguageTag());
        data.put("tags", AcceptLanguageUtil.parse(header).stream().map(AcceptLanguageUtil.Weighted::tag).toList());
        return Result.ok(data);
    }
}
