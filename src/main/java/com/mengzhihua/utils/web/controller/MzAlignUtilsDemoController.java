package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.AltSvcUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CuNiUtil;
import com.mengzhihua.utils.common.validate.GnNifpUtil;
import com.mengzhihua.utils.common.validate.MzNuitUtil;
import com.mengzhihua.utils.common.validate.SmCoeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "MZ Align Demo", description = "MZ NUIT / CU NI / GN NIFp / SM COE")
public class MzAlignUtilsDemoController {

    @GetMapping("/mz-nuit")
    @Operation(summary = "莫桑比克税号")
    public Result<Map<String, Object>> mzNuit(@RequestParam(defaultValue = "400339910") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MzNuitUtil.normalize(value));
        data.put("formatted", MzNuitUtil.format(value));
        data.put("valid", MzNuitUtil.isValid(value));
        data.put("regex", RegexUtil.is("mznuit", value));
        return Result.ok(data);
    }

    @GetMapping("/cu-ni")
    @Operation(summary = "古巴身份证")
    public Result<Map<String, Object>> cuNi(@RequestParam(defaultValue = "91021027775") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CuNiUtil.normalize(value));
        data.put("valid", CuNiUtil.isValid(value));
        if (CuNiUtil.isValid(value)) {
            data.put("birthDate", CuNiUtil.birthDate(value).toString());
            data.put("female", CuNiUtil.female(value));
        }
        data.put("regex", RegexUtil.is("cuni", value));
        return Result.ok(data);
    }

    @GetMapping("/gn-nifp")
    @Operation(summary = "几内亚税号")
    public Result<Map<String, Object>> gnNifp(@RequestParam(defaultValue = "693-770-885") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", GnNifpUtil.normalize(value));
        data.put("formatted", GnNifpUtil.format(value));
        data.put("valid", GnNifpUtil.isValid(value));
        data.put("regex", RegexUtil.is("gnnifp", value));
        return Result.ok(data);
    }

    @GetMapping("/sm-coe")
    @Operation(summary = "圣马力诺经营者号")
    public Result<Map<String, Object>> smCoe(@RequestParam(defaultValue = "024165") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SmCoeUtil.normalize(value));
        data.put("valid", SmCoeUtil.isValid(value));
        data.put("regex", RegexUtil.is("smcoe", value));
        return Result.ok(data);
    }

    @GetMapping("/alt-svc")
    @Operation(summary = "HTTP Alt-Svc")
    public Result<Map<String, Object>> altSvc(
            @RequestParam(defaultValue = "h3=\":443\"; ma=86400, h2=\":443\"; ma=2592000") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("clear", AltSvcUtil.isClear(header));
        data.put("firstProtocol", AltSvcUtil.firstProtocol(header));
        data.put("hasH3", AltSvcUtil.has(header, "h3"));
        data.put("services", AltSvcUtil.parse(header));
        return Result.ok(data);
    }

    @GetMapping("/crc16-mcrf4xx")
    @Operation(summary = "CRC-16/MCRF4XX")
    public Result<Map<String, Object>> crc16Mcrf4xx(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Mcrf4xx", HashUtil.crc16Mcrf4xxHex(text));
        return Result.ok(data);
    }
}
