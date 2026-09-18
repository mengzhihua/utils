package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ContentLanguageUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EgTnUtil;
import com.mengzhihua.utils.common.validate.LuTvaUtil;
import com.mengzhihua.utils.common.validate.MkEdbUtil;
import com.mengzhihua.utils.common.validate.SvNitUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "EG Align Demo", description = "EG TN / LU TVA / SV NIT / MK EDB")
public class EgAlignUtilsDemoController {

    @GetMapping("/eg-tn")
    @Operation(summary = "埃及税号")
    public Result<Map<String, Object>> egTn(@RequestParam(defaultValue = "100-531-385") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EgTnUtil.normalize(value));
        data.put("formatted", EgTnUtil.format(value));
        data.put("valid", EgTnUtil.isValid(value));
        data.put("regex", RegexUtil.is("egtn", value));
        return Result.ok(data);
    }

    @GetMapping("/lu-tva")
    @Operation(summary = "卢森堡税号")
    public Result<Map<String, Object>> luTva(@RequestParam(defaultValue = "LU 150 274 42") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", LuTvaUtil.normalize(value));
        data.put("formatted", LuTvaUtil.format(value));
        data.put("valid", LuTvaUtil.isValid(value));
        data.put("regex", RegexUtil.is("lutva", value));
        return Result.ok(data);
    }

    @GetMapping("/sv-nit")
    @Operation(summary = "萨尔瓦多税号")
    public Result<Map<String, Object>> svNit(@RequestParam(defaultValue = "0614-050707-104-8") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SvNitUtil.normalize(value));
        data.put("formatted", SvNitUtil.format(value));
        data.put("valid", SvNitUtil.isValid(value));
        data.put("regex", RegexUtil.is("svnit", value));
        return Result.ok(data);
    }

    @GetMapping("/mk-edb")
    @Operation(summary = "北马其顿税号")
    public Result<Map<String, Object>> mkEdb(@RequestParam(defaultValue = "4030000375897") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MkEdbUtil.normalize(value));
        data.put("valid", MkEdbUtil.isValid(value));
        data.put("regex", RegexUtil.is("mkedb", value));
        return Result.ok(data);
    }

    @GetMapping("/content-language")
    @Operation(summary = "HTTP Content-Language")
    public Result<Map<String, Object>> contentLanguage(
            @RequestParam(defaultValue = "zh-CN, en") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("first", ContentLanguageUtil.first(header));
        data.put("tags", ContentLanguageUtil.parse(header));
        data.put("hasZh", ContentLanguageUtil.has(header, "zh"));
        return Result.ok(data);
    }

    @GetMapping("/crc8-darc")
    @Operation(summary = "CRC-8/DARC")
    public Result<Map<String, Object>> crc8Darc(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc8Darc", HashUtil.crc8DarcHex(text));
        return Result.ok(data);
    }
}
