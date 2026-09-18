package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.CoepUtil;
import com.mengzhihua.utils.common.net.CoopUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.IlHpUtil;
import com.mengzhihua.utils.common.validate.LtJaUtil;
import com.mengzhihua.utils.common.validate.NzbnUtil;
import com.mengzhihua.utils.common.validate.UenUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Ocean Align Demo", description = "NZBN / UEN / ח.פ. / JA / COOP")
public class OceanAlignUtilsDemoController {

    @GetMapping("/nzbn")
    @Operation(summary = "新西兰企业号")
    public Result<Map<String, Object>> nzbn(@RequestParam(defaultValue = "9429000000000") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NzbnUtil.normalize(value));
        data.put("valid", NzbnUtil.isValid(value));
        data.put("regex", RegexUtil.is("nzbn", value));
        return Result.ok(data);
    }

    @GetMapping("/uen")
    @Operation(summary = "新加坡 UEN")
    public Result<Map<String, Object>> uen(@RequestParam(defaultValue = "T01FC6132D") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", UenUtil.normalize(value));
        data.put("valid", UenUtil.isValid(value));
        data.put("regex", RegexUtil.is("uen", value));
        return Result.ok(data);
    }

    @GetMapping("/il-hp")
    @Operation(summary = "以色列公司号")
    public Result<Map<String, Object>> ilHp(@RequestParam(defaultValue = "516179157") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IlHpUtil.normalize(value));
        data.put("valid", IlHpUtil.isValid(value));
        data.put("regex", RegexUtil.is("ilhp", value));
        return Result.ok(data);
    }

    @GetMapping("/lt-ja")
    @Operation(summary = "立陶宛企业号")
    public Result<Map<String, Object>> ltJa(@RequestParam(defaultValue = "119511515") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", LtJaUtil.normalize(value));
        data.put("valid", LtJaUtil.isValid(value));
        data.put("regex", RegexUtil.is("ltja", value));
        return Result.ok(data);
    }

    @GetMapping("/coop")
    @Operation(summary = "HTTP COOP")
    public Result<Map<String, Object>> coop(@RequestParam(defaultValue = "same-origin") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("policy", CoopUtil.parse(header));
        data.put("sameOrigin", CoopUtil.sameOrigin(header));
        data.put("known", CoopUtil.known(header));
        return Result.ok(data);
    }

    @GetMapping("/coep")
    @Operation(summary = "HTTP COEP")
    public Result<Map<String, Object>> coep(@RequestParam(defaultValue = "require-corp") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("policy", CoepUtil.parse(header));
        data.put("requireCorp", CoepUtil.requireCorp(header));
        data.put("known", CoepUtil.known(header));
        return Result.ok(data);
    }

    @GetMapping("/crc32-jamcrc")
    @Operation(summary = "CRC-32/JAMCRC")
    public Result<Map<String, Object>> crc32Jamcrc(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc32Jamcrc", HashUtil.crc32JamcrcHex(text));
        return Result.ok(data);
    }
}
