package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.UuencodeUtil;
import com.mengzhihua.utils.common.net.EtagUtil;
import com.mengzhihua.utils.common.net.LinkHeaderUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AbnUtil;
import com.mengzhihua.utils.common.validate.FodselsnummerUtil;
import com.mengzhihua.utils.common.validate.HetuUtil;
import com.mengzhihua.utils.common.validate.IswcUtil;
import com.mengzhihua.utils.common.validate.PersonnummerUtil;
import com.mengzhihua.utils.common.validate.SsccUtil;
import com.mengzhihua.utils.common.validate.TfnUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Nordic Align Demo", description = "Personnummer / HETU / ISWC / ABN / Link / ETag / uuencode")
public class NordicAlignUtilsDemoController {

    @GetMapping("/personnummer")
    @Operation(summary = "瑞典个人号")
    public Result<Map<String, Object>> personnummer(@RequestParam(defaultValue = "19811218-9876") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PersonnummerUtil.normalize(value));
        data.put("valid", PersonnummerUtil.isValid(value));
        if (PersonnummerUtil.isValid(value)) {
            data.put("female", PersonnummerUtil.female(value));
            data.put("birthDate", PersonnummerUtil.birthDate(value).toString());
        }
        data.put("regex", RegexUtil.is("personnummer", value));
        return Result.ok(data);
    }

    @GetMapping("/hetu")
    @Operation(summary = "芬兰个人号")
    public Result<Map<String, Object>> hetu(@RequestParam(defaultValue = "131052-308T") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", HetuUtil.normalize(value));
        data.put("valid", HetuUtil.isValid(value));
        if (HetuUtil.isValid(value)) {
            data.put("female", HetuUtil.female(value));
            data.put("birthDate", HetuUtil.birthDate(value).toString());
        }
        data.put("regex", RegexUtil.is("hetu", value));
        return Result.ok(data);
    }

    @GetMapping("/fodselsnummer")
    @Operation(summary = "挪威个人号")
    public Result<Map<String, Object>> fodselsnummer(@RequestParam(defaultValue = "11077941012") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", FodselsnummerUtil.normalize(value));
        data.put("valid", FodselsnummerUtil.isValid(value));
        if (FodselsnummerUtil.isValid(value)) {
            data.put("female", FodselsnummerUtil.female(value));
        }
        data.put("regex", RegexUtil.is("fodselsnummer", value));
        return Result.ok(data);
    }

    @GetMapping("/iswc")
    @Operation(summary = "ISWC")
    public Result<Map<String, Object>> iswc(@RequestParam(defaultValue = "T-034.524.680-8") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IswcUtil.normalize(value));
        data.put("formatted", IswcUtil.format(value));
        data.put("valid", IswcUtil.isValid(value));
        data.put("regex", RegexUtil.is("iswc", value));
        return Result.ok(data);
    }

    @GetMapping("/sscc")
    @Operation(summary = "GS1 SSCC")
    public Result<Map<String, Object>> sscc(@RequestParam(defaultValue = "106141411234567897") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SsccUtil.normalize(value));
        data.put("valid", SsccUtil.isValid(value));
        data.put("regex", RegexUtil.is("sscc", value));
        return Result.ok(data);
    }

    @GetMapping("/abn")
    @Operation(summary = "澳大利亚 ABN")
    public Result<Map<String, Object>> abn(@RequestParam(defaultValue = "51 824 753 556") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AbnUtil.normalize(value));
        data.put("valid", AbnUtil.isValid(value));
        data.put("regex", RegexUtil.is("abn", value));
        return Result.ok(data);
    }

    @GetMapping("/tfn")
    @Operation(summary = "澳大利亚 TFN")
    public Result<Map<String, Object>> tfn(@RequestParam(defaultValue = "123456782") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", TfnUtil.normalize(value));
        data.put("valid", TfnUtil.isValid(value));
        data.put("regex", RegexUtil.is("tfn", value));
        return Result.ok(data);
    }

    @GetMapping("/link")
    @Operation(summary = "HTTP Link RFC 8288")
    public Result<Map<String, Object>> link(
            @RequestParam(defaultValue = "<https://example.com/TheBook/chapter2>; rel=\"previous\"; title=\"previous chapter\"") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("previous", LinkHeaderUtil.rel(header, "previous"));
        data.put("links", LinkHeaderUtil.parse(header).stream()
                .map(link -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("uri", link.uri());
                    item.put("rel", link.rel());
                    item.put("title", link.title());
                    return item;
                })
                .collect(Collectors.toList()));
        return Result.ok(data);
    }

    @GetMapping("/etag")
    @Operation(summary = "HTTP ETag RFC 9110")
    public Result<Map<String, Object>> etag(
            @RequestParam(defaultValue = "W/\"abc\"") String value,
            @RequestParam(defaultValue = "\"abc\", W/\"xyz\"") String ifNoneMatch) {
        EtagUtil.Etag parsed = EtagUtil.parse(value);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("weak", parsed != null && parsed.weak());
        data.put("tag", parsed == null ? "" : parsed.tag());
        data.put("formatted", parsed == null ? "" : parsed.formatted());
        data.put("matches", EtagUtil.matches(value, ifNoneMatch));
        return Result.ok(data);
    }

    @GetMapping("/uuencode")
    @Operation(summary = "uuencode")
    public Result<Map<String, Object>> uuencode(@RequestParam(defaultValue = "Cat") String text) {
        String encoded = UuencodeUtil.encode(text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", UuencodeUtil.decodeToString(encoded));
        return Result.ok(data);
    }
}
