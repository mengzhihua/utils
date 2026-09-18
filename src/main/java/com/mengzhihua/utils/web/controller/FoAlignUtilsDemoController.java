package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.DnsPrefetchUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.FoVnUtil;
import com.mengzhihua.utils.common.validate.FrTvaUtil;
import com.mengzhihua.utils.common.validate.McTvaUtil;
import com.mengzhihua.utils.common.validate.MuNidUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "FO Align Demo", description = "FO VN / FR TVA / MC TVA / MU NID")
public class FoAlignUtilsDemoController {

    @GetMapping("/fo-vn")
    @Operation(summary = "法罗企业号")
    public Result<Map<String, Object>> foVn(@RequestParam(defaultValue = "623857") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", FoVnUtil.normalize(value));
        data.put("valid", FoVnUtil.isValid(value));
        data.put("regex", RegexUtil.is("fovn", value));
        return Result.ok(data);
    }

    @GetMapping("/fr-tva")
    @Operation(summary = "法国税号")
    public Result<Map<String, Object>> frTva(@RequestParam(defaultValue = "Fr 40 303 265 045") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", FrTvaUtil.normalize(value));
        data.put("formatted", FrTvaUtil.format(value));
        data.put("valid", FrTvaUtil.isValid(value));
        data.put("regex", RegexUtil.is("frtva", value));
        return Result.ok(data);
    }

    @GetMapping("/mc-tva")
    @Operation(summary = "摩纳哥税号")
    public Result<Map<String, Object>> mcTva(@RequestParam(defaultValue = "53 0000 04605") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", McTvaUtil.normalize(value));
        data.put("formatted", McTvaUtil.format(value));
        data.put("valid", McTvaUtil.isValid(value));
        data.put("regex", RegexUtil.is("mctva", value));
        return Result.ok(data);
    }

    @GetMapping("/mu-nid")
    @Operation(summary = "毛里求斯身份证")
        public Result<Map<String, Object>> muNid(@RequestParam(defaultValue = "B150390123456A") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MuNidUtil.normalize(value));
        data.put("valid", MuNidUtil.isValid(value));
        if (MuNidUtil.isValid(value)) {
            data.put("birthDate", MuNidUtil.birthDate(value).toString());
        }
        data.put("regex", RegexUtil.is("munid", value));
        return Result.ok(data);
    }

    @GetMapping("/dns-prefetch")
    @Operation(summary = "HTTP X-DNS-Prefetch-Control")
    public Result<Map<String, Object>> dnsPrefetch(@RequestParam(defaultValue = "on") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("value", DnsPrefetchUtil.parse(header));
        data.put("on", DnsPrefetchUtil.isOn(header));
        data.put("off", DnsPrefetchUtil.isOff(header));
        data.put("valid", DnsPrefetchUtil.isValid(header));
        return Result.ok(data);
    }

    @GetMapping("/crc8-wcdma")
    @Operation(summary = "CRC-8/WCDMA")
    public Result<Map<String, Object>> crc8Wcdma(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc8Wcdma", HashUtil.crc8WcdmaHex(text));
        return Result.ok(data);
    }
}
