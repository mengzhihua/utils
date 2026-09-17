package com.mengzhihua.utils.web.controller;


import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Base45Util;
import com.mengzhihua.utils.common.codec.Base85Util;
import com.mengzhihua.utils.common.codec.QuotedPrintableUtil;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.HotpUtil;
import com.mengzhihua.utils.common.crypto.PemUtil;
import com.mengzhihua.utils.common.crypto.SipHashUtil;
import com.mengzhihua.utils.common.crypto.XxHashUtil;
import com.mengzhihua.utils.common.extra.ColorUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.io.BomUtil;
import com.mengzhihua.utils.common.io.ZipUtil;
import com.mengzhihua.utils.common.json.IniUtil;
import com.mengzhihua.utils.common.json.JsonPatchUtil;
import com.mengzhihua.utils.common.json.JsonUtil;
import com.mengzhihua.utils.common.lang.UnsignedUtil;
import com.mengzhihua.utils.common.net.LanguageTagUtil;
import com.mengzhihua.utils.common.text.EscapeUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import com.mengzhihua.utils.common.time.HolidayUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "RFC Align Demo", description = "对标 RFC / Commons Codec / Guava 的编码与日历")
public class RfcAlignUtilsDemoController {

    @GetMapping("/hotp")
    @Operation(summary = "RFC 4226 HOTP")
    public Result<Map<String, Object>> hotp(
            @RequestParam(defaultValue = "12345678901234567890") String key,
            @RequestParam(defaultValue = "0") long counter) {
        String code = HotpUtil.generateFromAscii(key, counter);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", code);
        data.put("counter", counter);
        data.put("valid", HotpUtil.verify(code, key.getBytes(StandardCharsets.US_ASCII), counter));
        return Result.ok(data);
    }

    @GetMapping("/quoted-printable")
    @Operation(summary = "Quoted-Printable")
    public Result<Map<String, String>> quotedPrintable(@RequestParam(defaultValue = "Hello = 工具") String text) {
        String encoded = QuotedPrintableUtil.encode(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", QuotedPrintableUtil.decodeToString(encoded));
        return Result.ok(data);
    }

    @GetMapping("/base45")
    @Operation(summary = "Base45")
    public Result<Map<String, String>> base45(@RequestParam(defaultValue = "AB") String text) {
        String encoded = Base45Util.encode(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", Base45Util.decodeToString(encoded));
        return Result.ok(data);
    }

    @GetMapping("/base85")
    @Operation(summary = "Ascii85")
    public Result<Map<String, String>> base85(@RequestParam(defaultValue = "Man") String text) {
        String encoded = Base85Util.encode(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", Base85Util.decodeToString(encoded));
        return Result.ok(data);
    }

    @GetMapping("/xxhash")
    @Operation(summary = "xxHash32 / SipHash / Adler32")
    public Result<Map<String, Object>> xxhash(@RequestParam(defaultValue = "hello") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("xxhash32", XxHashUtil.hash32Hex(text));
        data.put("xxhash64", XxHashUtil.hash64Hex(text));
        data.put("siphash24", SipHashUtil.hashHex(text));
        data.put("adler32", HashUtil.adler32Hex(text));
        data.put("unsigned", UnsignedUtil.toUnsignedString(XxHashUtil.hash32(text)));
        return Result.ok(data);
    }

    @GetMapping("/holiday")
    @Operation(summary = "中国节假日")
    public Result<Map<String, Object>> holiday(@RequestParam(defaultValue = "2026-10-01") String date) {
        LocalDate parsed = LocalDate.parse(date);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("holiday", HolidayUtil.isHoliday(parsed));
        data.put("name", HolidayUtil.name(parsed));
        data.put("quarter", DateTimeUtil.quarter(parsed));
        return Result.ok(data);
    }

    @GetMapping("/json-patch")
    @Operation(summary = "JSON Patch")
    public Result<Map<String, Object>> jsonPatch(
            @RequestParam(defaultValue = "{\"name\":\"Bob\"}") String json,
            @RequestParam(defaultValue = "[{\"op\":\"replace\",\"path\":\"/name\",\"value\":\"Ada\"}]") String patch) {
        String patched = JsonPatchUtil.apply(json, patch);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("patched", patched);
        data.put("tree", JsonUtil.toMap(patched));
        return Result.ok(data);
    }

    @GetMapping("/contrast")
    @Operation(summary = "WCAG 对比度")
    public Result<Map<String, Object>> contrast(
            @RequestParam(defaultValue = "#FFFFFF") String left,
            @RequestParam(defaultValue = "#000000") String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ratio", ColorUtil.contrastRatio(left, right));
        data.put("aa", ColorUtil.aa(left, right));
        data.put("aaa", ColorUtil.aaa(left, right));
        return Result.ok(data);
    }

    @GetMapping("/ini")
    @Operation(summary = "INI 解析")
    public Result<Map<String, Object>> ini(
            @RequestParam(defaultValue = "[database]\nhost=localhost\nport=3306") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("parsed", IniUtil.parse(text));
        data.put("host", IniUtil.get(text, "database", "host"));
        return Result.ok(data);
    }

    @GetMapping("/pem")
    @Operation(summary = "PEM 封装")
    public Result<Map<String, Object>> pem(@RequestParam(defaultValue = "hello") String text) {
        String pem = PemUtil.wrap("DATA", text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("pem", pem);
        data.put("type", PemUtil.parse(pem).type());
        data.put("decoded", PemUtil.decodeToString(pem));
        return Result.ok(data);
    }

    @GetMapping("/language-tag")
    @Operation(summary = "BCP 47")
    public Result<Map<String, Object>> languageTag(@RequestParam(defaultValue = "zh-CN") String tag) {
        var parsed = LanguageTagUtil.parse(tag);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("language", parsed.language());
        data.put("region", parsed.region());
        data.put("tag", parsed.tag());
        return Result.ok(data);
    }

    @GetMapping("/uuid-v6")
    @Operation(summary = "UUID v6")
    public Result<Map<String, String>> uuidV6() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("uuidV6", IdUtil.uuidV6());
        data.put("uuidV7", IdUtil.uuidV7());
        return Result.ok(data);
    }

    @GetMapping("/percent-encode")
    @Operation(summary = "Percent encoding")
    public Result<Map<String, String>> percentEncode(@RequestParam(defaultValue = "hello 工具") String text) {
        String encoded = EscapeUtil.percent(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", EscapeUtil.unpercent(encoded));
        return Result.ok(data);
    }

    @GetMapping("/zlib")
    @Operation(summary = "zlib")
    public Result<Map<String, String>> zlib(@RequestParam(defaultValue = "hello 工具") String text) {
        String encoded = ZipUtil.zlibBase64(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("zlibBase64", encoded);
        data.put("decoded", ZipUtil.unzlibBase64(encoded));
        data.put("bom", BomUtil.detect(BomUtil.prependUtf8(text.getBytes(StandardCharsets.UTF_8))));
        return Result.ok(data);
    }
}
