package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.DocumentPolicyUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CnicUtil;
import com.mengzhihua.utils.common.validate.GhTinUtil;
import com.mengzhihua.utils.common.validate.IdnoUtil;
import com.mengzhihua.utils.common.validate.KePinUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "CNIC Align Demo", description = "CNIC / IDNO / GH TIN / KE PIN")
public class CnicAlignUtilsDemoController {

    @GetMapping("/cnic")
    @Operation(summary = "巴基斯坦身份证")
    public Result<Map<String, Object>> cnic(@RequestParam(defaultValue = "34201-0891231-8") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CnicUtil.normalize(value));
        data.put("formatted", CnicUtil.format(value));
        data.put("valid", CnicUtil.isValid(value));
        data.put("gender", CnicUtil.gender(value));
        data.put("province", CnicUtil.province(value));
        data.put("regex", RegexUtil.is("cnic", value));
        return Result.ok(data);
    }

    @GetMapping("/idno")
    @Operation(summary = "摩尔多瓦企业号")
    public Result<Map<String, Object>> idno(@RequestParam(defaultValue = "1008600038413") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IdnoUtil.normalize(value));
        data.put("valid", IdnoUtil.isValid(value));
        data.put("regex", RegexUtil.is("idno", value));
        return Result.ok(data);
    }

    @GetMapping("/gh-tin")
    @Operation(summary = "加纳税号")
    public Result<Map<String, Object>> ghTin(@RequestParam(defaultValue = "C0000803561") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", GhTinUtil.normalize(value));
        data.put("valid", GhTinUtil.isValid(value));
        data.put("regex", RegexUtil.is("ghtin", value));
        return Result.ok(data);
    }

    @GetMapping("/ke-pin")
    @Operation(summary = "肯尼亚税号")
    public Result<Map<String, Object>> kePin(@RequestParam(defaultValue = "P051365947M") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", KePinUtil.normalize(value));
        data.put("valid", KePinUtil.isValid(value));
        data.put("individual", KePinUtil.individual(value));
        data.put("regex", RegexUtil.is("kepin", value));
        return Result.ok(data);
    }

    @GetMapping("/document-policy")
    @Operation(summary = "HTTP Document-Policy")
    public Result<Map<String, Object>> documentPolicy(
            @RequestParam(defaultValue = "unsized-media=?0, max-image-bpp=2.0") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("first", DocumentPolicyUtil.first(header));
        data.put("unsizedMedia", DocumentPolicyUtil.value(header, "unsized-media"));
        data.put("hasMaxImageBpp", DocumentPolicyUtil.has(header, "max-image-bpp"));
        return Result.ok(data);
    }

    @GetMapping("/crc32-autosar")
    @Operation(summary = "CRC-32/AUTOSAR")
    public Result<Map<String, Object>> crc32Autosar(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc32Autosar", HashUtil.crc32AutosarHex(text));
        return Result.ok(data);
    }
}
