package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.CorpUtil;
import com.mengzhihua.utils.common.net.XctoUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.InnUtil;
import com.mengzhihua.utils.common.validate.NikUtil;
import com.mengzhihua.utils.common.validate.PeRucUtil;
import com.mengzhihua.utils.common.validate.VnMstUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "CIS Align Demo", description = "INN / RUC / NIK / MST / CORP")
public class CisAlignUtilsDemoController {

    @GetMapping("/inn")
    @Operation(summary = "俄罗斯税号")
    public Result<Map<String, Object>> inn(@RequestParam(defaultValue = "7707083893") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", InnUtil.normalize(value));
        data.put("valid", InnUtil.isValid(value));
        data.put("regex", RegexUtil.is("inn", value));
        return Result.ok(data);
    }

    @GetMapping("/pe-ruc")
    @Operation(summary = "秘鲁税号")
    public Result<Map<String, Object>> peRuc(@RequestParam(defaultValue = "20512333797") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PeRucUtil.normalize(value));
        data.put("valid", PeRucUtil.isValid(value));
        data.put("regex", RegexUtil.is("peruc", value));
        return Result.ok(data);
    }

    @GetMapping("/nik")
    @Operation(summary = "印尼身份证")
    public Result<Map<String, Object>> nik(@RequestParam(defaultValue = "3171011708450001") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NikUtil.normalize(value));
        data.put("valid", NikUtil.isValid(value));
        data.put("female", NikUtil.female(value));
        data.put("regex", RegexUtil.is("nik", value));
        return Result.ok(data);
    }

    @GetMapping("/vn-mst")
    @Operation(summary = "越南税号")
    public Result<Map<String, Object>> vnMst(@RequestParam(defaultValue = "0100233488") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", VnMstUtil.normalize(value));
        data.put("valid", VnMstUtil.isValid(value));
        data.put("regex", RegexUtil.is("vnmst", value));
        return Result.ok(data);
    }

    @GetMapping("/corp")
    @Operation(summary = "HTTP CORP")
    public Result<Map<String, Object>> corp(@RequestParam(defaultValue = "same-origin") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("policy", CorpUtil.parse(header));
        data.put("sameOrigin", CorpUtil.sameOrigin(header));
        data.put("sameSite", CorpUtil.sameSite(header));
        data.put("known", CorpUtil.known(header));
        return Result.ok(data);
    }

    @GetMapping("/xcto")
    @Operation(summary = "HTTP X-Content-Type-Options")
    public Result<Map<String, Object>> xcto(@RequestParam(defaultValue = "nosniff") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("directive", XctoUtil.parse(header));
        data.put("nosniff", XctoUtil.nosniff(header));
        data.put("known", XctoUtil.known(header));
        return Result.ok(data);
    }

    @GetMapping("/crc16-dnp")
    @Operation(summary = "CRC-16/DNP")
    public Result<Map<String, Object>> crc16Dnp(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Dnp", HashUtil.crc16DnpHex(text));
        return Result.ok(data);
    }
}
