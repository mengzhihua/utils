package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CifUtil;
import com.mengzhihua.utils.common.validate.CvrUtil;
import com.mengzhihua.utils.common.validate.OrgnrUtil;
import com.mengzhihua.utils.common.validate.SeOrgNrUtil;
import com.mengzhihua.utils.common.validate.YTunnusUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Nordic Biz Align Demo", description = "Y-tunnus / orgnr / CVR / CIF")
public class NordicBizAlignUtilsDemoController {

    @GetMapping("/y-tunnus")
    @Operation(summary = "芬兰企业号")
    public Result<Map<String, Object>> yTunnus(@RequestParam(defaultValue = "1234567-1") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", YTunnusUtil.normalize(value));
        data.put("valid", YTunnusUtil.isValid(value));
        data.put("regex", RegexUtil.is("ytunnus", value));
        return Result.ok(data);
    }

    @GetMapping("/orgnr")
    @Operation(summary = "挪威企业号")
    public Result<Map<String, Object>> orgnr(@RequestParam(defaultValue = "123456785") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", OrgnrUtil.normalize(value));
        data.put("valid", OrgnrUtil.isValid(value));
        data.put("regex", RegexUtil.is("orgnr", value));
        return Result.ok(data);
    }

    @GetMapping("/cvr")
    @Operation(summary = "丹麦 CVR")
    public Result<Map<String, Object>> cvr(@RequestParam(defaultValue = "35408002") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CvrUtil.normalize(value));
        data.put("valid", CvrUtil.isValid(value));
        data.put("regex", RegexUtil.is("cvr", value));
        return Result.ok(data);
    }

    @GetMapping("/cif")
    @Operation(summary = "西班牙 CIF")
    public Result<Map<String, Object>> cif(@RequestParam(defaultValue = "A58818501") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CifUtil.normalize(value));
        data.put("valid", CifUtil.isValid(value));
        data.put("regex", RegexUtil.is("cif", value));
        return Result.ok(data);
    }

    @GetMapping("/orgnr-se")
    @Operation(summary = "瑞典企业号")
    public Result<Map<String, Object>> orgnrSe(@RequestParam(defaultValue = "556036-0793") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SeOrgNrUtil.normalize(value));
        data.put("valid", SeOrgNrUtil.isValid(value));
        data.put("regex", RegexUtil.is("orgnrse", value));
        return Result.ok(data);
    }

    @GetMapping("/crc32-posix")
    @Operation(summary = "CRC-32/POSIX")
    public Result<Map<String, Object>> crc32Posix(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc32Posix", HashUtil.crc32PosixHex(text));
        return Result.ok(data);
    }
}
