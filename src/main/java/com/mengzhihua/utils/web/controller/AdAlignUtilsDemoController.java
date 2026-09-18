package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.OriginUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AdNrtUtil;
import com.mengzhihua.utils.common.validate.DzNifUtil;
import com.mengzhihua.utils.common.validate.LiPeidUtil;
import com.mengzhihua.utils.common.validate.SnNineaUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "AD Align Demo", description = "AD NRT / LI PEID / DZ NIF / SN NINEA")
public class AdAlignUtilsDemoController {

    @GetMapping("/ad-nrt")
    @Operation(summary = "安道尔税号")
    public Result<Map<String, Object>> adNrt(@RequestParam(defaultValue = "U-132950-X") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AdNrtUtil.normalize(value));
        data.put("formatted", AdNrtUtil.format(value));
        data.put("valid", AdNrtUtil.isValid(value));
        data.put("regex", RegexUtil.is("adnrt", value));
        return Result.ok(data);
    }

    @GetMapping("/li-peid")
    @Operation(summary = "列支敦士登识别号")
    public Result<Map<String, Object>> liPeid(@RequestParam(defaultValue = "00001234567") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", LiPeidUtil.normalize(value));
        data.put("valid", LiPeidUtil.isValid(value));
        data.put("regex", RegexUtil.is("lipeid", value));
        return Result.ok(data);
    }

    @GetMapping("/dz-nif")
    @Operation(summary = "阿尔及利亚税号")
    public Result<Map<String, Object>> dzNif(@RequestParam(defaultValue = "416001000000007") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", DzNifUtil.normalize(value));
        data.put("valid", DzNifUtil.isValid(value));
        data.put("regex", RegexUtil.is("dznif", value));
        return Result.ok(data);
    }

    @GetMapping("/sn-ninea")
    @Operation(summary = "塞内加尔企业号")
    public Result<Map<String, Object>> snNinea(@RequestParam(defaultValue = "30672212G2") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SnNineaUtil.normalize(value));
        data.put("formatted", SnNineaUtil.format(value));
        data.put("valid", SnNineaUtil.isValid(value));
        data.put("regex", RegexUtil.is("snninea", value));
        return Result.ok(data);
    }

    @GetMapping("/origin")
    @Operation(summary = "HTTP Origin")
    public Result<Map<String, Object>> origin(
            @RequestParam(defaultValue = "https://example.com:8443") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        OriginUtil.Origin parsed = OriginUtil.parse(header);
        data.put("scheme", parsed.scheme());
        data.put("host", parsed.host());
        data.put("port", parsed.port());
        data.put("nullOrigin", parsed.nullOrigin());
        data.put("secure", OriginUtil.isSecure(header));
        return Result.ok(data);
    }

    @GetMapping("/crc8-rohc")
    @Operation(summary = "CRC-8/ROHC")
    public Result<Map<String, Object>> crc8Rohc(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc8Rohc", HashUtil.crc8RohcHex(text));
        return Result.ok(data);
    }
}
