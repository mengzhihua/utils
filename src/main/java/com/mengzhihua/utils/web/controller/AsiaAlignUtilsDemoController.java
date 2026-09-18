package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.NelUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EdrpouUtil;
import com.mengzhihua.utils.common.validate.HojinUtil;
import com.mengzhihua.utils.common.validate.KrBrnUtil;
import com.mengzhihua.utils.common.validate.PibUtil;
import com.mengzhihua.utils.common.validate.TwGuiUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Asia Align Demo", description = "法人番号 / BRN / GUI / EDRPOU / PIB / NEL")
public class AsiaAlignUtilsDemoController {

    @GetMapping("/hojin")
    @Operation(summary = "日本法人番号")
    public Result<Map<String, Object>> hojin(@RequestParam(defaultValue = "8700110005901") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", HojinUtil.normalize(value));
        data.put("valid", HojinUtil.isValid(value));
        data.put("regex", RegexUtil.is("hojin", value));
        return Result.ok(data);
    }

    @GetMapping("/kr-brn")
    @Operation(summary = "韩国事业者注册号")
    public Result<Map<String, Object>> krBrn(@RequestParam(defaultValue = "120-81-47521") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", KrBrnUtil.normalize(value));
        data.put("valid", KrBrnUtil.isValid(value));
        data.put("regex", RegexUtil.is("krbrn", value));
        return Result.ok(data);
    }

    @GetMapping("/tw-gui")
    @Operation(summary = "台湾统一编号")
    public Result<Map<String, Object>> twGui(@RequestParam(defaultValue = "53212539") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", TwGuiUtil.normalize(value));
        data.put("valid", TwGuiUtil.isValid(value));
        data.put("regex", RegexUtil.is("twgui", value));
        return Result.ok(data);
    }

    @GetMapping("/edrpou")
    @Operation(summary = "乌克兰 EDRPOU")
    public Result<Map<String, Object>> edrpou(@RequestParam(defaultValue = "14360570") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EdrpouUtil.normalize(value));
        data.put("valid", EdrpouUtil.isValid(value));
        data.put("regex", RegexUtil.is("edrpou", value));
        return Result.ok(data);
    }

    @GetMapping("/rs-pib")
    @Operation(summary = "塞尔维亚 PIB")
    public Result<Map<String, Object>> rsPib(@RequestParam(defaultValue = "101134702") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PibUtil.normalize(value));
        data.put("valid", PibUtil.isValid(value));
        data.put("regex", RegexUtil.is("rspib", value));
        return Result.ok(data);
    }

    @GetMapping("/nel")
    @Operation(summary = "HTTP NEL")
    public Result<Map<String, Object>> nel(
            @RequestParam(defaultValue = "{\"report_to\":\"nel\",\"max_age\":31536000,\"include_subdomains\":true}")
            String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("reportTo", NelUtil.reportTo(header));
        data.put("maxAge", NelUtil.maxAge(header));
        data.put("includeSubdomains", NelUtil.includeSubdomains(header));
        return Result.ok(data);
    }

    @GetMapping("/crc8-smbus")
    @Operation(summary = "CRC-8/SMBUS")
    public Result<Map<String, Object>> crc8Smbus(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc8Smbus", HashUtil.crc8SmbusHex(text));
        return Result.ok(data);
    }
}
