package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ClearSiteDataUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AdoszamUtil;
import com.mengzhihua.utils.common.validate.CheUidUtil;
import com.mengzhihua.utils.common.validate.CuiUtil;
import com.mengzhihua.utils.common.validate.EikUtil;
import com.mengzhihua.utils.common.validate.KboUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Enterprise Align Demo", description = "CHE-UID / EIK / CUI / KBO / adószám")
public class EnterpriseAlignUtilsDemoController {

    @GetMapping("/che-uid")
    @Operation(summary = "瑞士企业号")
    public Result<Map<String, Object>> cheUid(@RequestParam(defaultValue = "CHE-109.322.551") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CheUidUtil.normalize(value));
        data.put("valid", CheUidUtil.isValid(value));
        data.put("regex", RegexUtil.is("cheuid", value));
        return Result.ok(data);
    }

    @GetMapping("/eik")
    @Operation(summary = "保加利亚 EIK")
    public Result<Map<String, Object>> eik(@RequestParam(defaultValue = "831641791") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EikUtil.normalize(value));
        data.put("valid", EikUtil.isValid(value));
        data.put("regex", RegexUtil.is("eik", value));
        return Result.ok(data);
    }

    @GetMapping("/ro-cui")
    @Operation(summary = "罗马尼亚 CUI")
    public Result<Map<String, Object>> roCui(@RequestParam(defaultValue = "18547290") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CuiUtil.normalize(value));
        data.put("valid", CuiUtil.isValid(value));
        data.put("regex", RegexUtil.is("rocui", value));
        return Result.ok(data);
    }

    @GetMapping("/adoszam")
    @Operation(summary = "匈牙利税号")
    public Result<Map<String, Object>> adoszam(@RequestParam(defaultValue = "18154111-2-41") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AdoszamUtil.normalize(value));
        data.put("valid", AdoszamUtil.isValid(value));
        data.put("regex", RegexUtil.is("adoszam", value));
        return Result.ok(data);
    }

    @GetMapping("/kbo")
    @Operation(summary = "比利时企业号")
    public Result<Map<String, Object>> kbo(@RequestParam(defaultValue = "0123.456.749") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", KboUtil.normalize(value));
        data.put("valid", KboUtil.isValid(value));
        data.put("regex", RegexUtil.is("kbo", value));
        return Result.ok(data);
    }

    @GetMapping("/clear-site-data")
    @Operation(summary = "HTTP Clear-Site-Data")
    public Result<Map<String, Object>> clearSiteData(
            @RequestParam(defaultValue = "\"cache\", \"cookies\"") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("directives", ClearSiteDataUtil.parse(header));
        data.put("clearsCache", ClearSiteDataUtil.has(header, "cache"));
        data.put("knownCookies", ClearSiteDataUtil.known("cookies"));
        return Result.ok(data);
    }

    @GetMapping("/crc16-usb")
    @Operation(summary = "CRC-16/USB")
    public Result<Map<String, Object>> crc16Usb(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Usb", HashUtil.crc16UsbHex(text));
        return Result.ok(data);
    }
}
