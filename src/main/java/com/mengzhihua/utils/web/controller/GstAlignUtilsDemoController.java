package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ReportToUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AcnUtil;
import com.mengzhihua.utils.common.validate.GstinUtil;
import com.mengzhihua.utils.common.validate.NpwpUtil;
import com.mengzhihua.utils.common.validate.RegistrikoodUtil;
import com.mengzhihua.utils.common.validate.VknUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "GST Align Demo", description = "GSTIN / ACN / VKN / NPWP / registrikood")
public class GstAlignUtilsDemoController {

    @GetMapping("/gstin")
    @Operation(summary = "印度 GSTIN")
    public Result<Map<String, Object>> gstin(@RequestParam(defaultValue = "27AAPFU0939F1ZV") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", GstinUtil.normalize(value));
        data.put("valid", GstinUtil.isValid(value));
        data.put("regex", RegexUtil.is("gstin", value));
        return Result.ok(data);
    }

    @GetMapping("/acn")
    @Operation(summary = "澳大利亚公司号")
    public Result<Map<String, Object>> acn(@RequestParam(defaultValue = "000 000 019") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AcnUtil.normalize(value));
        data.put("valid", AcnUtil.isValid(value));
        data.put("regex", RegexUtil.is("acn", value));
        return Result.ok(data);
    }

    @GetMapping("/vkn")
    @Operation(summary = "土耳其税号")
    public Result<Map<String, Object>> vkn(@RequestParam(defaultValue = "4540536920") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", VknUtil.normalize(value));
        data.put("valid", VknUtil.isValid(value));
        data.put("regex", RegexUtil.is("vkn", value));
        return Result.ok(data);
    }

    @GetMapping("/npwp")
    @Operation(summary = "印尼税号")
    public Result<Map<String, Object>> npwp(@RequestParam(defaultValue = "01.312.166.0-091.000") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NpwpUtil.normalize(value));
        data.put("valid", NpwpUtil.isValid(value));
        data.put("regex", RegexUtil.is("npwp", value));
        return Result.ok(data);
    }

    @GetMapping("/registrikood")
    @Operation(summary = "爱沙尼亚企业号")
    public Result<Map<String, Object>> registrikood(@RequestParam(defaultValue = "12345678") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", RegistrikoodUtil.normalize(value));
        data.put("valid", RegistrikoodUtil.isValid(value));
        data.put("regex", RegexUtil.is("registrikood", value));
        return Result.ok(data);
    }

    @GetMapping("/report-to")
    @Operation(summary = "HTTP Report-To")
    public Result<Map<String, Object>> reportTo(
            @RequestParam(defaultValue = "[{\"group\":\"nel\",\"max_age\":31536000,\"endpoints\":[{\"url\":\"https://example.com/reports\"}]}]")
            String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("firstGroup", ReportToUtil.firstGroup(header));
        data.put("maxAge", ReportToUtil.firstMaxAge(header));
        data.put("hasNel", ReportToUtil.hasGroup(header, "nel"));
        return Result.ok(data);
    }

    @GetMapping("/crc16-genibus")
    @Operation(summary = "CRC-16/GENIBUS")
    public Result<Map<String, Object>> crc16Genibus(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Genibus", HashUtil.crc16GenibusHex(text));
        return Result.ok(data);
    }
}
