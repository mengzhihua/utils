package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.XssProtectionUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EcCiUtil;
import com.mengzhihua.utils.common.validate.EcRucUtil;
import com.mengzhihua.utils.common.validate.IeVatUtil;
import com.mengzhihua.utils.common.validate.ItIvaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "EC Align Demo", description = "EC CI / EC RUC / IT IVA / IE VAT")
public class EcAlignUtilsDemoController {

    @GetMapping("/ec-ci")
    @Operation(summary = "厄瓜多尔身份证")
    public Result<Map<String, Object>> ecCi(@RequestParam(defaultValue = "171430710-3") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EcCiUtil.normalize(value));
        data.put("formatted", EcCiUtil.format(value));
        data.put("valid", EcCiUtil.isValid(value));
        data.put("regex", RegexUtil.is("ecci", value));
        return Result.ok(data);
    }

    @GetMapping("/ec-ruc")
    @Operation(summary = "厄瓜多尔税号")
    public Result<Map<String, Object>> ecRuc(@RequestParam(defaultValue = "1792060346-001") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EcRucUtil.normalize(value));
        data.put("formatted", EcRucUtil.format(value));
        data.put("valid", EcRucUtil.isValid(value));
        data.put("regex", RegexUtil.is("ecruc", value));
        return Result.ok(data);
    }

    @GetMapping("/it-iva")
    @Operation(summary = "意大利增值税号")
    public Result<Map<String, Object>> itIva(@RequestParam(defaultValue = "IT 00743110157") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", ItIvaUtil.normalize(value));
        data.put("formatted", ItIvaUtil.format(value));
        data.put("valid", ItIvaUtil.isValid(value));
        data.put("regex", RegexUtil.is("itiva", value));
        return Result.ok(data);
    }

    @GetMapping("/ie-vat")
    @Operation(summary = "爱尔兰税号")
    public Result<Map<String, Object>> ieVat(@RequestParam(defaultValue = "IE 6433435F") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IeVatUtil.normalize(value));
        data.put("formatted", IeVatUtil.format(value));
        data.put("valid", IeVatUtil.isValid(value));
        data.put("regex", RegexUtil.is("ievat", value));
        return Result.ok(data);
    }

    @GetMapping("/xss-protection")
    @Operation(summary = "HTTP X-XSS-Protection")
    public Result<Map<String, Object>> xssProtection(
            @RequestParam(defaultValue = "1; mode=block") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("enabled", XssProtectionUtil.enabled(header));
        data.put("modeBlock", XssProtectionUtil.modeBlock(header));
        data.put("report", XssProtectionUtil.report(header));
        data.put("valid", XssProtectionUtil.isValid(header));
        return Result.ok(data);
    }

    @GetMapping("/crc8-maxim")
    @Operation(summary = "CRC-8/MAXIM-DOW")
    public Result<Map<String, Object>> crc8Maxim(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc8Maxim", HashUtil.crc8MaximHex(text));
        return Result.ok(data);
    }
}
