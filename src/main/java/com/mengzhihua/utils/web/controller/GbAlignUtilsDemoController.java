package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ExpectCtUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CaBnUtil;
import com.mengzhihua.utils.common.validate.CzDicUtil;
import com.mengzhihua.utils.common.validate.GbVatUtil;
import com.mengzhihua.utils.common.validate.Iso11649Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "GB Align Demo", description = "GB VAT / CA BN / CZ DIC / ISO 11649")
public class GbAlignUtilsDemoController {

    @GetMapping("/gb-vat")
    @Operation(summary = "英国税号")
    public Result<Map<String, Object>> gbVat(@RequestParam(defaultValue = "GB 980 7806 84") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", GbVatUtil.normalize(value));
        data.put("formatted", GbVatUtil.format(value));
        data.put("valid", GbVatUtil.isValid(value));
        data.put("regex", RegexUtil.is("gbvat", value));
        return Result.ok(data);
    }

    @GetMapping("/ca-bn")
    @Operation(summary = "加拿大企业号")
    public Result<Map<String, Object>> caBn(@RequestParam(defaultValue = "12302 6635 RC 0001") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CaBnUtil.normalize(value));
        data.put("formatted", CaBnUtil.format(value));
        data.put("valid", CaBnUtil.isValid(value));
        data.put("regex", RegexUtil.is("cabn", value));
        return Result.ok(data);
    }

    @GetMapping("/cz-dic")
    @Operation(summary = "捷克税号")
    public Result<Map<String, Object>> czDic(@RequestParam(defaultValue = "CZ 25123891") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CzDicUtil.normalize(value));
        data.put("formatted", CzDicUtil.format(value));
        data.put("valid", CzDicUtil.isValid(value));
        data.put("regex", RegexUtil.is("czdic", value));
        return Result.ok(data);
    }

    @GetMapping("/iso11649")
    @Operation(summary = "ISO 11649 债权参考号")
    public Result<Map<String, Object>> iso11649(@RequestParam(defaultValue = "RF18 5390 0754 7034") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", Iso11649Util.normalize(value));
        data.put("formatted", Iso11649Util.format(value));
        data.put("valid", Iso11649Util.isValid(value));
        data.put("regex", RegexUtil.is("iso11649", value));
        return Result.ok(data);
    }

    @GetMapping("/expect-ct")
    @Operation(summary = "HTTP Expect-CT")
    public Result<Map<String, Object>> expectCt(
            @RequestParam(defaultValue = "max-age=86400, enforce") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("maxAge", ExpectCtUtil.maxAge(header));
        data.put("enforce", ExpectCtUtil.enforce(header));
        data.put("reportUri", ExpectCtUtil.reportUri(header));
        data.put("valid", ExpectCtUtil.isValid(header));
        return Result.ok(data);
    }

    @GetMapping("/crc16-x25")
    @Operation(summary = "CRC-16/X-25")
    public Result<Map<String, Object>> crc16X25(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16X25", HashUtil.crc16X25Hex(text));
        return Result.ok(data);
    }
}
