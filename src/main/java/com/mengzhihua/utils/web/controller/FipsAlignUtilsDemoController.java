package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.ShakeUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.net.HttpRangeUtil;
import com.mengzhihua.utils.common.text.ColognePhoneticUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.text.TextUtil;
import com.mengzhihua.utils.common.validate.IsmnUtil;
import com.mengzhihua.utils.common.validate.NpiUtil;
import com.mengzhihua.utils.common.validate.NricUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "FIPS Align Demo", description = "SHAKE / NPI / ISMN / NRIC / Cologne / HTTP Range")
public class FipsAlignUtilsDemoController {

    @GetMapping("/shake")
    @Operation(summary = "SHAKE128 / SHAKE256")
    public Result<Map<String, Object>> shake(@RequestParam(defaultValue = "abc") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("shake128", ShakeUtil.shake128(text));
        data.put("shake256", ShakeUtil.shake256(text));
        return Result.ok(data);
    }

    @GetMapping("/npi")
    @Operation(summary = "NPI")
    public Result<Map<String, Object>> npi(@RequestParam(defaultValue = "1234567893") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NpiUtil.normalize(value));
        data.put("valid", NpiUtil.isValid(value));
        data.put("regex", RegexUtil.is("npi", value));
        return Result.ok(data);
    }

    @GetMapping("/ismn")
    @Operation(summary = "ISMN-13")
    public Result<Map<String, Object>> ismn(@RequestParam(defaultValue = "979-0-2600-0043-8") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IsmnUtil.normalize(value));
        data.put("valid", IsmnUtil.isValid(value));
        data.put("regex", RegexUtil.is("ismn", value));
        return Result.ok(data);
    }

    @GetMapping("/nric")
    @Operation(summary = "新加坡 NRIC")
    public Result<Map<String, Object>> nric(@RequestParam(defaultValue = "S1234567D") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NricUtil.normalize(value));
        data.put("valid", NricUtil.isValid(value));
        data.put("regex", RegexUtil.is("nric", value));
        return Result.ok(data);
    }

    @GetMapping("/cologne")
    @Operation(summary = "科隆拼音")
    public Result<Map<String, Object>> cologne(@RequestParam(defaultValue = "Müller") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", ColognePhoneticUtil.encode(text));
        return Result.ok(data);
    }

    @GetMapping("/http-range")
    @Operation(summary = "HTTP Range RFC 7233")
    public Result<Map<String, Object>> httpRange(
            @RequestParam(defaultValue = "bytes=0-499") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ranges", HttpRangeUtil.parse(header).stream()
                .map(range -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("start", range.start());
                    item.put("end", range.end());
                    item.put("suffix", range.suffix());
                    return item;
                })
                .collect(Collectors.toList()));
        return Result.ok(data);
    }

    @GetMapping("/hamming")
    @Operation(summary = "Hamming 距离")
    public Result<Map<String, Object>> hamming(
            @RequestParam(defaultValue = "karolin") String left,
            @RequestParam(defaultValue = "kathrin") String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("distance", TextUtil.hamming(left, right));
        return Result.ok(data);
    }

    @GetMapping("/uuid-v8")
    @Operation(summary = "UUID v8")
    public Result<Map<String, Object>> uuidV8() {
        String id = IdUtil.uuidV8();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("uuid", id);
        data.put("version", Character.digit(id.charAt(14), 16));
        return Result.ok(data);
    }
}
