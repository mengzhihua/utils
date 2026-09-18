package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Z85Util;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.CacheControlUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AadhaarUtil;
import com.mengzhihua.utils.common.validate.AhvUtil;
import com.mengzhihua.utils.common.validate.NipUtil;
import com.mengzhihua.utils.common.validate.PanUtil;
import com.mengzhihua.utils.common.validate.PpsUtil;
import com.mengzhihua.utils.common.validate.SinUtil;
import com.mengzhihua.utils.common.validate.VatUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Tax Align Demo", description = "VAT / AHV / NIP / Aadhaar / PAN / SIN / PPS / Z85")
public class TaxAlignUtilsDemoController {

    @GetMapping("/vat")
    @Operation(summary = "欧盟 VAT")
    public Result<Map<String, Object>> vat(@RequestParam(defaultValue = "DE136695976") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", VatUtil.normalize(value));
        data.put("country", VatUtil.country(value));
        data.put("valid", VatUtil.isValid(value));
        data.put("regex", RegexUtil.is("vat", value));
        return Result.ok(data);
    }

    @GetMapping("/ahv")
    @Operation(summary = "瑞士 AHV")
    public Result<Map<String, Object>> ahv(@RequestParam(defaultValue = "756.1234.5678.97") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AhvUtil.normalize(value));
        data.put("valid", AhvUtil.isValid(value));
        data.put("regex", RegexUtil.is("ahv", value));
        return Result.ok(data);
    }

    @GetMapping("/nip")
    @Operation(summary = "波兰 NIP")
    public Result<Map<String, Object>> nip(@RequestParam(defaultValue = "1234563218") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NipUtil.normalize(value));
        data.put("valid", NipUtil.isValid(value));
        data.put("regex", RegexUtil.is("nip", value));
        return Result.ok(data);
    }

    @GetMapping("/aadhaar")
    @Operation(summary = "印度 Aadhaar")
    public Result<Map<String, Object>> aadhaar(@RequestParam(defaultValue = "234123412346") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AadhaarUtil.normalize(value));
        data.put("valid", AadhaarUtil.isValid(value));
        data.put("regex", RegexUtil.is("aadhaar", value));
        return Result.ok(data);
    }

    @GetMapping("/pan")
    @Operation(summary = "印度 PAN")
    public Result<Map<String, Object>> pan(@RequestParam(defaultValue = "ABCPE1234F") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PanUtil.normalize(value));
        data.put("valid", PanUtil.isValid(value));
        if (PanUtil.isValid(value)) {
            data.put("entityType", String.valueOf(PanUtil.entityType(value)));
        }
        data.put("regex", RegexUtil.is("pan", value));
        return Result.ok(data);
    }

    @GetMapping("/sin")
    @Operation(summary = "加拿大 SIN")
    public Result<Map<String, Object>> sin(@RequestParam(defaultValue = "046454286") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SinUtil.normalize(value));
        data.put("valid", SinUtil.isValid(value));
        data.put("regex", RegexUtil.is("sin", value));
        return Result.ok(data);
    }

    @GetMapping("/pps")
    @Operation(summary = "爱尔兰 PPS")
    public Result<Map<String, Object>> pps(@RequestParam(defaultValue = "1234567T") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PpsUtil.normalize(value));
        data.put("valid", PpsUtil.isValid(value));
        data.put("regex", RegexUtil.is("pps", value));
        return Result.ok(data);
    }

    @GetMapping("/cache-control")
    @Operation(summary = "HTTP Cache-Control RFC 9111")
    public Result<Map<String, Object>> cacheControl(
            @RequestParam(defaultValue = "max-age=3600, public, must-revalidate") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("directives", CacheControlUtil.parse(header));
        data.put("maxAge", CacheControlUtil.maxAge(header));
        data.put("public", CacheControlUtil.has(header, "public"));
        return Result.ok(data);
    }

    @GetMapping("/z85")
    @Operation(summary = "Z85 RFC 32")
    public Result<Map<String, Object>> z85(@RequestParam(defaultValue = "HelloWorld") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        if (text.length() % 5 == 0 && text.chars().allMatch(c -> c < 128)) {
            try {
                data.put("decodedHex", Z85Util.decodeToHex(text));
            } catch (IllegalArgumentException ex) {
                data.put("decodedHex", "");
            }
        }
        data.put("encoded", Z85Util.encode(text));
        return Result.ok(data);
    }

    @GetMapping("/crc16-xmodem")
    @Operation(summary = "CRC-16/XMODEM")
    public Result<Map<String, Object>> crc16Xmodem(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Xmodem", HashUtil.crc16XmodemHex(text));
        return Result.ok(data);
    }
}
