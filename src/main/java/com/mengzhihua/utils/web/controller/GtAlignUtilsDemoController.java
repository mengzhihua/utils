package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ServerTimingUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CrCpfUtil;
import com.mengzhihua.utils.common.validate.CrCpjUtil;
import com.mengzhihua.utils.common.validate.GtNitUtil;
import com.mengzhihua.utils.common.validate.TnMfUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "GT Align Demo", description = "GT NIT / CR CPF / CR CPJ / TN MF")
public class GtAlignUtilsDemoController {

    @GetMapping("/gt-nit")
    @Operation(summary = "危地马拉税号")
    public Result<Map<String, Object>> gtNit(@RequestParam(defaultValue = "576937-K") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", GtNitUtil.normalize(value));
        data.put("formatted", GtNitUtil.format(value));
        data.put("valid", GtNitUtil.isValid(value));
        data.put("regex", RegexUtil.is("gtnit", value));
        return Result.ok(data);
    }

    @GetMapping("/cr-cpf")
    @Operation(summary = "哥斯达黎加身份证")
    public Result<Map<String, Object>> crCpf(@RequestParam(defaultValue = "3-0455-0175") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CrCpfUtil.normalize(value));
        data.put("formatted", CrCpfUtil.format(value));
        data.put("valid", CrCpfUtil.isValid(value));
        data.put("regex", RegexUtil.is("crcpf", value));
        return Result.ok(data);
    }

    @GetMapping("/cr-cpj")
    @Operation(summary = "哥斯达黎加税号")
    public Result<Map<String, Object>> crCpj(@RequestParam(defaultValue = "3-101-999999") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CrCpjUtil.normalize(value));
        data.put("formatted", CrCpjUtil.format(value));
        data.put("personClass", CrCpjUtil.personClass(value));
        data.put("valid", CrCpjUtil.isValid(value));
        data.put("regex", RegexUtil.is("crcpj", value));
        return Result.ok(data);
    }

    @GetMapping("/tn-mf")
    @Operation(summary = "突尼斯税号")
    public Result<Map<String, Object>> tnMf(@RequestParam(defaultValue = "1234567/M/A/E/001") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", TnMfUtil.normalize(value));
        data.put("formatted", TnMfUtil.format(value));
        data.put("valid", TnMfUtil.isValid(value));
        data.put("regex", RegexUtil.is("tnmf", value));
        return Result.ok(data);
    }

    @GetMapping("/server-timing")
    @Operation(summary = "HTTP Server-Timing")
    public Result<Map<String, Object>> serverTiming(
            @RequestParam(defaultValue = "miss, db;dur=53, app;dur=47.2") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("first", ServerTimingUtil.first(header));
        data.put("names", ServerTimingUtil.names(header));
        data.put("dbDuration", ServerTimingUtil.duration(header, "db"));
        data.put("hasApp", ServerTimingUtil.has(header, "app"));
        return Result.ok(data);
    }

    @GetMapping("/crc8-sae")
    @Operation(summary = "CRC-8/SAE-J1850")
    public Result<Map<String, Object>> crc8Sae(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc8Sae", HashUtil.crc8SaeJ1850Hex(text));
        return Result.ok(data);
    }
}
