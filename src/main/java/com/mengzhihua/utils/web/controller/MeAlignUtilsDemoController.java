package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.VaryUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CyVatUtil;
import com.mengzhihua.utils.common.validate.MePibUtil;
import com.mengzhihua.utils.common.validate.MtVatUtil;
import com.mengzhihua.utils.common.validate.OmVatUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "ME Align Demo", description = "ME PIB / OM VAT / CY VAT / MT VAT")
public class MeAlignUtilsDemoController {

    @GetMapping("/me-pib")
    @Operation(summary = "黑山税号")
    public Result<Map<String, Object>> mePib(@RequestParam(defaultValue = "02655284") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MePibUtil.normalize(value));
        data.put("valid", MePibUtil.isValid(value));
        data.put("regex", RegexUtil.is("mepib", value));
        return Result.ok(data);
    }

    @GetMapping("/om-vat")
    @Operation(summary = "阿曼税号")
    public Result<Map<String, Object>> omVat(@RequestParam(defaultValue = "OM1100006083") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", OmVatUtil.normalize(value));
        data.put("formatted", OmVatUtil.format(value));
        data.put("valid", OmVatUtil.isValid(value));
        data.put("regex", RegexUtil.is("omvat", value));
        return Result.ok(data);
    }

    @GetMapping("/cy-vat")
    @Operation(summary = "塞浦路斯税号")
    public Result<Map<String, Object>> cyVat(@RequestParam(defaultValue = "CY-10259033P") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CyVatUtil.normalize(value));
        data.put("formatted", CyVatUtil.format(value));
        data.put("valid", CyVatUtil.isValid(value));
        data.put("regex", RegexUtil.is("cyvat", value));
        return Result.ok(data);
    }

    @GetMapping("/mt-vat")
    @Operation(summary = "马耳他税号")
    public Result<Map<String, Object>> mtVat(@RequestParam(defaultValue = "MT 1167-9112") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MtVatUtil.normalize(value));
        data.put("formatted", MtVatUtil.format(value));
        data.put("valid", MtVatUtil.isValid(value));
        data.put("regex", RegexUtil.is("mtvat", value));
        return Result.ok(data);
    }

    @GetMapping("/vary")
    @Operation(summary = "HTTP Vary")
    public Result<Map<String, Object>> vary(
            @RequestParam(defaultValue = "Accept-Encoding, User-Agent") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("first", VaryUtil.first(header));
        data.put("fields", VaryUtil.parse(header));
        data.put("hasAcceptEncoding", VaryUtil.has(header, "accept-encoding"));
        data.put("wildcard", VaryUtil.isWildcard(header));
        return Result.ok(data);
    }

    @GetMapping("/crc8-icode")
    @Operation(summary = "CRC-8/I-CODE")
    public Result<Map<String, Object>> crc8Icode(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc8Icode", HashUtil.crc8IcodeHex(text));
        return Result.ok(data);
    }
}
