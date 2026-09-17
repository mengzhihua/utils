package com.mengzhihua.utils.web;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.util.AccentUtil;
import com.mengzhihua.utils.util.CusipUtil;
import com.mengzhihua.utils.util.EmojiUtil;
import com.mengzhihua.utils.util.EncodedWordUtil;
import com.mengzhihua.utils.util.HttpDateUtil;
import com.mengzhihua.utils.util.IsrcUtil;
import com.mengzhihua.utils.util.JsonMergePatchUtil;
import com.mengzhihua.utils.util.OrcidUtil;
import com.mengzhihua.utils.util.PlateUtil;
import com.mengzhihua.utils.util.SedolUtil;
import com.mengzhihua.utils.util.TotpUtil;
import com.mengzhihua.utils.util.UriTemplateUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Spec Align Demo", description = "对标 Commons Validator / RFC 6238 / 7396 / 6570")
public class SpecAlignUtilsDemoController {

    @GetMapping("/cusip")
    @Operation(summary = "CUSIP")
    public Result<Map<String, Object>> cusip(@RequestParam(defaultValue = "037833100") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CusipUtil.normalize(value));
        data.put("valid", CusipUtil.isValid(value));
        return Result.ok(data);
    }

    @GetMapping("/sedol")
    @Operation(summary = "SEDOL")
    public Result<Map<String, Object>> sedol(@RequestParam(defaultValue = "1234565") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SedolUtil.normalize(value));
        data.put("valid", SedolUtil.isValid(value));
        return Result.ok(data);
    }

    @GetMapping("/orcid")
    @Operation(summary = "ORCID")
    public Result<Map<String, Object>> orcid(@RequestParam(defaultValue = "0000-0002-1825-0097") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", OrcidUtil.normalize(value));
        data.put("formatted", OrcidUtil.format(value));
        data.put("valid", OrcidUtil.isValid(value));
        return Result.ok(data);
    }

    @GetMapping("/isrc")
    @Operation(summary = "ISRC")
    public Result<Map<String, Object>> isrc(@RequestParam(defaultValue = "US-S1Z-99-00001") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IsrcUtil.normalize(value));
        data.put("formatted", IsrcUtil.format(value));
        data.put("country", IsrcUtil.country(value));
        data.put("valid", IsrcUtil.isValid(value));
        return Result.ok(data);
    }

    @GetMapping("/json-merge-patch")
    @Operation(summary = "JSON Merge Patch")
    public Result<Map<String, Object>> jsonMergePatch(
            @RequestParam(defaultValue = "{\"a\":\"b\"}") String json,
            @RequestParam(defaultValue = "{\"a\":\"c\"}") String patch) {
        String patched = JsonMergePatchUtil.apply(json, patch);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("patched", patched);
        return Result.ok(data);
    }

    @GetMapping("/http-date")
    @Operation(summary = "HTTP Date")
    public Result<Map<String, Object>> httpDate(
            @RequestParam(defaultValue = "0") long epochMilli) {
        Instant instant = Instant.ofEpochMilli(epochMilli);
        String formatted = HttpDateUtil.format(instant);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("httpDate", formatted);
        data.put("epochMilli", HttpDateUtil.parse(formatted).toEpochMilli());
        return Result.ok(data);
    }

    @GetMapping("/emoji")
    @Operation(summary = "Emoji")
    public Result<Map<String, Object>> emoji(@RequestParam(defaultValue = "hello 😀 工具") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("contains", EmojiUtil.contains(text));
        data.put("count", EmojiUtil.count(text));
        data.put("extracted", EmojiUtil.extract(text));
        data.put("removed", EmojiUtil.remove(text));
        return Result.ok(data);
    }

    @GetMapping("/accent")
    @Operation(summary = "去音调")
    public Result<Map<String, String>> accent(@RequestParam(defaultValue = "café naïve") String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("stripped", AccentUtil.strip(text));
        data.put("nfc", AccentUtil.nfc(text));
        return Result.ok(data);
    }

    @GetMapping("/plate")
    @Operation(summary = "车牌号")
    public Result<Map<String, Object>> plate(@RequestParam(defaultValue = "京A12345") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", PlateUtil.isValid(value));
        data.put("newEnergy", PlateUtil.isNewEnergy(value));
        data.put("province", PlateUtil.province(value));
        return Result.ok(data);
    }

    @GetMapping("/uri-template")
    @Operation(summary = "URI Template")
    public Result<Map<String, Object>> uriTemplate(
            @RequestParam(defaultValue = "/users/{id}") String template,
            @RequestParam(defaultValue = "42") String id) {
        String expanded = UriTemplateUtil.expand(template, Map.of("id", id));
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("expanded", expanded);
        data.put("vars", UriTemplateUtil.match(template, expanded));
        return Result.ok(data);
    }

    @GetMapping("/totp-rfc6238")
    @Operation(summary = "RFC 6238 TOTP")
    public Result<Map<String, Object>> totpRfc(
            @RequestParam(defaultValue = "12345678901234567890") String key,
            @RequestParam(defaultValue = "59") long unixSeconds) {
        byte[] raw = key.getBytes(StandardCharsets.US_ASCII);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", TotpUtil.generate(raw, unixSeconds, 30, 8, "HmacSHA1"));
        data.put("unixSeconds", unixSeconds);
        return Result.ok(data);
    }

    @GetMapping("/encoded-word")
    @Operation(summary = "RFC 2047 encoded-word")
    public Result<Map<String, String>> encodedWord(@RequestParam(defaultValue = "工具") String text) {
        String encoded = EncodedWordUtil.encode(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", EncodedWordUtil.decode(encoded));
        return Result.ok(data);
    }
}
