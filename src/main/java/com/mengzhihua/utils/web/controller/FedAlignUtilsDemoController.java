package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.OacUtil;
import com.mengzhihua.utils.common.net.TimingAllowUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EinUtil;
import com.mengzhihua.utils.common.validate.NiptUtil;
import com.mengzhihua.utils.common.validate.OgrnUtil;
import com.mengzhihua.utils.common.validate.SnilsUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Fed Align Demo", description = "EIN / OGRN / SNILS / NIPT / TAO")
public class FedAlignUtilsDemoController {

    @GetMapping("/ein")
    @Operation(summary = "美国雇主识别号")
    public Result<Map<String, Object>> ein(@RequestParam(defaultValue = "91-1144442") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EinUtil.normalize(value));
        data.put("formatted", EinUtil.format(value));
        data.put("campus", EinUtil.campus(value));
        data.put("valid", EinUtil.isValid(value));
        data.put("regex", RegexUtil.is("ein", value));
        return Result.ok(data);
    }

    @GetMapping("/ogrn")
    @Operation(summary = "俄罗斯统一注册号")
    public Result<Map<String, Object>> ogrn(@RequestParam(defaultValue = "1022200525819") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", OgrnUtil.normalize(value));
        data.put("valid", OgrnUtil.isValid(value));
        data.put("regex", RegexUtil.is("ogrn", value));
        return Result.ok(data);
    }

    @GetMapping("/snils")
    @Operation(summary = "俄罗斯养老金号")
    public Result<Map<String, Object>> snils(@RequestParam(defaultValue = "112-233-445 95") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SnilsUtil.normalize(value));
        data.put("valid", SnilsUtil.isValid(value));
        data.put("regex", RegexUtil.is("snils", value));
        return Result.ok(data);
    }

    @GetMapping("/nipt")
    @Operation(summary = "阿尔巴尼亚税号")
    public Result<Map<String, Object>> nipt(@RequestParam(defaultValue = "J91402501L") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NiptUtil.normalize(value));
        data.put("valid", NiptUtil.isValid(value));
        data.put("regex", RegexUtil.is("nipt", value));
        return Result.ok(data);
    }

    @GetMapping("/timing-allow")
    @Operation(summary = "HTTP Timing-Allow-Origin")
    public Result<Map<String, Object>> timingAllow(@RequestParam(defaultValue = "*") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("value", TimingAllowUtil.parse(header));
        data.put("wildcard", TimingAllowUtil.wildcard(header));
        data.put("origin", TimingAllowUtil.origin(header));
        data.put("known", TimingAllowUtil.known(header));
        return Result.ok(data);
    }

    @GetMapping("/oac")
    @Operation(summary = "HTTP Origin-Agent-Cluster")
    public Result<Map<String, Object>> oac(@RequestParam(defaultValue = "?1") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("value", OacUtil.parse(header));
        data.put("enabled", OacUtil.enabled(header));
        data.put("known", OacUtil.known(header));
        return Result.ok(data);
    }

    @GetMapping("/crc16-cms")
    @Operation(summary = "CRC-16/CMS")
    public Result<Map<String, Object>> crc16Cms(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Cms", HashUtil.crc16CmsHex(text));
        return Result.ok(data);
    }
}
