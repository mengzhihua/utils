package com.mengzhihua.utils.web;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.util.Base32Util;
import com.mengzhihua.utils.util.BasicAuthUtil;
import com.mengzhihua.utils.util.CaptchaUtil;
import com.mengzhihua.utils.util.ColorUtil;
import com.mengzhihua.utils.util.DateTimeUtil;
import com.mengzhihua.utils.util.ExprUtil;
import com.mengzhihua.utils.util.HashUtil;
import com.mengzhihua.utils.util.IdCardUtil;
import com.mengzhihua.utils.util.IdUtil;
import com.mengzhihua.utils.util.IdnUtil;
import com.mengzhihua.utils.util.IsbnUtil;
import com.mengzhihua.utils.util.JsonPathUtil;
import com.mengzhihua.utils.util.LunarUtil;
import com.mengzhihua.utils.util.MacUtil;
import com.mengzhihua.utils.util.ObjectIdUtil;
import com.mengzhihua.utils.util.RadixUtil;
import com.mengzhihua.utils.util.UnicodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Align Utils Demo", description = "对标 Hutool 的农历 / 表达式 / 进制等演示")
public class AlignUtilsDemoController {

    @GetMapping("/unicode")
    @Operation(summary = "Unicode 转义")
    public Result<Map<String, String>> unicode(@RequestParam(defaultValue = "工具集") String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("unicode", UnicodeUtil.toUnicode(text));
        data.put("plain", UnicodeUtil.fromUnicode(UnicodeUtil.toUnicode(text)));
        return Result.ok(data);
    }

    @GetMapping("/radix")
    @Operation(summary = "进制转换")
    public Result<Map<String, String>> radix(
            @RequestParam(defaultValue = "255") String value,
            @RequestParam(defaultValue = "10") int from,
            @RequestParam(defaultValue = "16") int to) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("converted", RadixUtil.convert(value, from, to));
        data.put("hex", RadixUtil.toString(RadixUtil.parse(value, from), 16));
        data.put("base62", RadixUtil.toString(RadixUtil.parse(value, from), 62));
        return Result.ok(data);
    }

    @GetMapping("/object-id")
    @Operation(summary = "Mongo ObjectId")
    public Result<Map<String, Object>> objectId() {
        String id = IdUtil.objectId();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", id);
        data.put("timestamp", ObjectIdUtil.timestamp(id).toString());
        return Result.ok(data);
    }

    @GetMapping("/idn")
    @Operation(summary = "国际化域名 Punycode")
    public Result<Map<String, String>> idn(@RequestParam(defaultValue = "清华大学.cn") String domain) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("ascii", IdnUtil.toAscii(domain));
        data.put("unicode", IdnUtil.toUnicode(IdnUtil.toAscii(domain)));
        return Result.ok(data);
    }

    @GetMapping("/expr")
    @Operation(summary = "四则运算表达式")
    public Result<Map<String, Object>> expr(@RequestParam(defaultValue = "(1+2)*3") String expression) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("result", ExprUtil.evalPlain(expression));
        return Result.ok(data);
    }

    @GetMapping("/lunar")
    @Operation(summary = "公历转农历")
    public Result<Map<String, Object>> lunar(@RequestParam(defaultValue = "2024-02-10") String date) {
        LunarUtil.Lunar lunar = LunarUtil.of(date);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("year", lunar.year());
        data.put("month", lunar.month());
        data.put("day", lunar.day());
        data.put("leap", lunar.leap());
        data.put("ganZhi", lunar.ganZhi());
        data.put("animal", lunar.animal());
        data.put("display", lunar.display());
        data.put("quarter", DateTimeUtil.quarter(LocalDate.parse(date)));
        return Result.ok(data);
    }

    @PostMapping("/json-path")
    @Operation(summary = "JSON Pointer")
    public Result<Map<String, Object>> jsonPath(@RequestBody Map<String, String> body) {
        String json = body.getOrDefault("json", "{\"user\":{\"name\":\"Ada\"}}");
        String path = body.getOrDefault("path", "user.name");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("value", JsonPathUtil.getStr(json, path));
        data.put("exists", JsonPathUtil.exists(json, path));
        return Result.ok(data);
    }

    @GetMapping("/captcha")
    @Operation(summary = "图片验证码")
    public Result<Map<String, String>> captcha() {
        CaptchaUtil.ImageCaptcha captcha = CaptchaUtil.create();
        Map<String, String> data = new LinkedHashMap<>();
        data.put("code", captcha.code());
        data.put("dataUrl", captcha.dataUrl());
        return Result.ok(data);
    }

    @GetMapping("/isbn")
    @Operation(summary = "ISBN 校验")
    public Result<Map<String, Object>> isbn(@RequestParam(defaultValue = "9780306406157") String code) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", IsbnUtil.isValid(code));
        data.put("normalized", IsbnUtil.normalize(code));
        return Result.ok(data);
    }

    @GetMapping("/mac")
    @Operation(summary = "MAC 规范化")
    public Result<Map<String, Object>> mac(@RequestParam(defaultValue = "00-1A-2B-3C-4D-5E") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", MacUtil.isValid(value));
        data.put("normalized", MacUtil.isValid(value) ? MacUtil.normalize(value) : null);
        return Result.ok(data);
    }

    @GetMapping("/idcard/convert")
    @Operation(summary = "15 位身份证升 18 位")
    public Result<Map<String, Object>> convertId(@RequestParam(defaultValue = "110101900307893") String idNo) {
        String converted = IdCardUtil.convert15To18(idNo);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("converted", converted);
        data.put("valid", IdCardUtil.isValid(converted));
        data.put("gender", IdCardUtil.getGender(converted));
        data.put("birthday", IdCardUtil.getBirthday(converted));
        return Result.ok(data);
    }

    @GetMapping("/base32")
    @Operation(summary = "Base32")
    public Result<Map<String, String>> base32(@RequestParam(defaultValue = "hello") String text) {
        String encoded = Base32Util.encode(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", Base32Util.decodeToString(encoded));
        return Result.ok(data);
    }

    @GetMapping("/basic-auth")
    @Operation(summary = "Basic Auth 头")
    public Result<Map<String, Object>> basicAuth(
            @RequestParam(defaultValue = "ada") String username,
            @RequestParam(defaultValue = "secret") String password) {
        String header = BasicAuthUtil.header(username, password);
        String[] parsed = BasicAuthUtil.parse(header);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("header", header);
        data.put("username", parsed[0]);
        data.put("password", parsed[1]);
        return Result.ok(data);
    }

    @GetMapping("/hash/fnv")
    @Operation(summary = "FNV-1a / CRC16")
    public Result<Map<String, Object>> fnv(@RequestParam(defaultValue = "hello") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("fnv1a32", HashUtil.fnv1a32(text));
        data.put("crc16", HashUtil.crc16(text));
        return Result.ok(data);
    }

    @GetMapping("/color/hsl")
    @Operation(summary = "颜色 HSL / 混合")
    public Result<Map<String, Object>> hsl(@RequestParam(defaultValue = "#0f766e") String hex) {
        int[] hsl = ColorUtil.hexToHsl(hex);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("hsl", Map.of("h", hsl[0], "s", hsl[1], "l", hsl[2]));
        data.put("mixed", ColorUtil.mix(hex, "#ffffff", 0.4));
        data.put("dark", ColorUtil.isDark(hex));
        return Result.ok(data);
    }
}
