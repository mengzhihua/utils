package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Base92Util;
import com.mengzhihua.utils.common.net.StructuredFieldUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.BsnUtil;
import com.mengzhihua.utils.common.validate.RodneCisloUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "West Align Demo", description = "BSN / rodné číslo / Base92 / Structured Fields")
public class WestAlignUtilsDemoController {

    @GetMapping("/bsn")
    @Operation(summary = "荷兰 BSN")
    public Result<Map<String, Object>> bsn(@RequestParam(defaultValue = "111222333") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", BsnUtil.normalize(value));
        data.put("valid", BsnUtil.isValid(value));
        data.put("regex", RegexUtil.is("bsn", value));
        return Result.ok(data);
    }

    @GetMapping("/rodne")
    @Operation(summary = "捷克/斯洛伐克出生号")
    public Result<Map<String, Object>> rodne(@RequestParam(defaultValue = "680101/0007") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", RodneCisloUtil.normalize(value));
        data.put("valid", RodneCisloUtil.isValid(value));
        if (RodneCisloUtil.isValid(value)) {
            data.put("birthDate", RodneCisloUtil.birthDate(value).toString());
            data.put("female", RodneCisloUtil.female(value));
        }
        data.put("regex", RegexUtil.is("rodne", value));
        return Result.ok(data);
    }

    @GetMapping("/base92")
    @Operation(summary = "Base92")
    public Result<Map<String, Object>> base92(
            @RequestParam(defaultValue = "encode") String action,
            @RequestParam(defaultValue = "Hello") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        if ("decode".equalsIgnoreCase(action)) {
            data.put("decoded", Base92Util.decodeToString(text));
        } else {
            data.put("encoded", Base92Util.encode(text));
        }
        return Result.ok(data);
    }

    @GetMapping("/structured-fields")
    @Operation(summary = "RFC 8941 Structured Fields")
    public Result<Map<String, Object>> structuredFields(
            @RequestParam(defaultValue = "abc=123, def=?0, title=\"hi\"") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("dictionary", StructuredFieldUtil.parseDictionary(header));
        data.put("abc", StructuredFieldUtil.get(header, "abc"));
        data.put("hasDef", StructuredFieldUtil.has(header, "def"));
        return Result.ok(data);
    }
}
