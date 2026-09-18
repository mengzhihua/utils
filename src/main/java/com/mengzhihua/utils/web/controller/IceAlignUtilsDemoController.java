package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.XRobotsTagUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.MaIceUtil;
import com.mengzhihua.utils.common.validate.PyRucUtil;
import com.mengzhihua.utils.common.validate.UyRutUtil;
import com.mengzhihua.utils.common.validate.VoenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "ICE Align Demo", description = "ICE / VÖEN / UY RUT / PY RUC")
public class IceAlignUtilsDemoController {

    @GetMapping("/ma-ice")
    @Operation(summary = "摩洛哥企业号")
    public Result<Map<String, Object>> maIce(@RequestParam(defaultValue = "001561191000066") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MaIceUtil.normalize(value));
        data.put("valid", MaIceUtil.isValid(value));
        data.put("regex", RegexUtil.is("maice", value));
        return Result.ok(data);
    }

    @GetMapping("/voen")
    @Operation(summary = "阿塞拜疆税号")
    public Result<Map<String, Object>> voen(@RequestParam(defaultValue = "140 155 5071") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", VoenUtil.normalize(value));
        data.put("valid", VoenUtil.isValid(value));
        data.put("legalPerson", VoenUtil.legalPerson(value));
        data.put("regex", RegexUtil.is("voen", value));
        return Result.ok(data);
    }

    @GetMapping("/uy-rut")
    @Operation(summary = "乌拉圭税号")
    public Result<Map<String, Object>> uyRut(@RequestParam(defaultValue = "21-100342-001-7") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", UyRutUtil.normalize(value));
        data.put("formatted", UyRutUtil.format(value));
        data.put("valid", UyRutUtil.isValid(value));
        data.put("regex", RegexUtil.is("uyrut", value));
        return Result.ok(data);
    }

    @GetMapping("/py-ruc")
    @Operation(summary = "巴拉圭税号")
    public Result<Map<String, Object>> pyRuc(@RequestParam(defaultValue = "80028061-0") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PyRucUtil.normalize(value));
        data.put("formatted", PyRucUtil.format(value));
        data.put("valid", PyRucUtil.isValid(value));
        data.put("regex", RegexUtil.is("pyruc", value));
        return Result.ok(data);
    }

    @GetMapping("/x-robots-tag")
    @Operation(summary = "HTTP X-Robots-Tag")
    public Result<Map<String, Object>> xRobotsTag(
            @RequestParam(defaultValue = "noindex, nofollow") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("first", XRobotsTagUtil.first(header));
        data.put("noindex", XRobotsTagUtil.noindex(header));
        data.put("hasNofollow", XRobotsTagUtil.has(header, "nofollow"));
        return Result.ok(data);
    }

    @GetMapping("/crc16-gsm")
    @Operation(summary = "CRC-16/GSM")
    public Result<Map<String, Object>> crc16Gsm(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Gsm", HashUtil.crc16GsmHex(text));
        return Result.ok(data);
    }
}
