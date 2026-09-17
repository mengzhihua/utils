package com.mengzhihua.utils.web.controller;


import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Base64Util;
import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.id.KsuidUtil;
import com.mengzhihua.utils.common.id.TypeIdUtil;
import com.mengzhihua.utils.common.lang.RangeUtil;
import com.mengzhihua.utils.common.net.HostAndPortUtil;
import com.mengzhihua.utils.common.text.SimHashUtil;
import com.mengzhihua.utils.common.text.SoundexUtil;
import com.mengzhihua.utils.common.text.TextUtil;
import com.mengzhihua.utils.common.text.WordUtil;
import com.mengzhihua.utils.common.time.AgeUtil;
import com.mengzhihua.utils.common.validate.EanUtil;
import com.mengzhihua.utils.common.validate.IbanUtil;
import com.mengzhihua.utils.common.validate.IssnUtil;
import com.mengzhihua.utils.common.validate.VinUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Open Source Align Demo", description = "对标 Commons / Guava / Hutool 的校验与相似度")
public class OpenSourceUtilsDemoController {

    @GetMapping("/similarity")
    @Operation(summary = "Jaro-Winkler / Jaccard / LCS")
    public Result<Map<String, Object>> similarity(
            @RequestParam(defaultValue = "MARTHA") String left,
            @RequestParam(defaultValue = "MARHTA") String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("levenshtein", TextUtil.levenshtein(left, right));
        data.put("similarity", TextUtil.similarity(left, right));
        data.put("jaroWinkler", TextUtil.jaroWinkler(left, right));
        data.put("jaccard", TextUtil.jaccard(left, right));
        data.put("dice", TextUtil.dice(left, right));
        data.put("cosine", TextUtil.cosine(left, right));
        data.put("damerau", TextUtil.damerauLevenshtein(left, right));
        data.put("fuzzyScore", TextUtil.fuzzyScore(left, right));
        data.put("lcs", TextUtil.longestCommonSubsequence(left, right));
        data.put("simhashDistance", SimHashUtil.distance(left, right));
        return Result.ok(data);
    }

    @GetMapping("/soundex")
    @Operation(summary = "Soundex 读音码")
    public Result<Map<String, Object>> soundex(
            @RequestParam(defaultValue = "Robert") String left,
            @RequestParam(defaultValue = "Rupert") String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("left", SoundexUtil.encode(left));
        data.put("right", SoundexUtil.encode(right));
        data.put("similar", SoundexUtil.similar(left, right));
        return Result.ok(data);
    }

    @GetMapping("/iban")
    @Operation(summary = "IBAN 校验")
    public Result<Map<String, Object>> iban(@RequestParam(defaultValue = "GB82WEST12345698765432") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", IbanUtil.isValid(value));
        data.put("normalized", IbanUtil.normalize(value));
        return Result.ok(data);
    }

    @GetMapping("/vin")
    @Operation(summary = "VIN 车架号")
    public Result<Map<String, Object>> vin(@RequestParam(defaultValue = "1M8GDM9AXKP042788") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", VinUtil.isValid(value));
        data.put("normalized", VinUtil.normalize(value));
        return Result.ok(data);
    }

    @GetMapping("/issn")
    @Operation(summary = "ISSN 校验")
    public Result<Map<String, Object>> issn(@RequestParam(defaultValue = "0317-8471") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", IssnUtil.isValid(value));
        data.put("normalized", IssnUtil.normalize(value));
        return Result.ok(data);
    }

    @GetMapping("/ean")
    @Operation(summary = "EAN / UPC / GTIN")
    public Result<Map<String, Object>> ean(@RequestParam(defaultValue = "5901234123457") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", EanUtil.isValid(value));
        data.put("normalized", EanUtil.normalize(value));
        return Result.ok(data);
    }

    @GetMapping("/age")
    @Operation(summary = "年龄 / 下次生日")
    public Result<Map<String, Object>> age(@RequestParam(defaultValue = "1990-03-07") String birthday) {
        LocalDate date = LocalDate.parse(birthday);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("age", AgeUtil.age(date));
        data.put("nextBirthday", AgeUtil.nextBirthday(date).toString());
        data.put("daysUntil", AgeUtil.daysUntilBirthday(date));
        data.put("isBirthday", AgeUtil.isBirthday(date));
        return Result.ok(data);
    }

    @GetMapping("/digest")
    @Operation(summary = "SHA-256 / SHA3-256")
    public Result<Map<String, String>> digest(@RequestParam(defaultValue = "hello") String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("md5", EncryptUtil.md5(text));
        data.put("sha256", EncryptUtil.sha256(text));
        data.put("sha3_256", EncryptUtil.sha3_256(text));
        data.put("sha512", EncryptUtil.sha512(text));
        return Result.ok(data);
    }

    @GetMapping("/ksuid")
    @Operation(summary = "KSUID / TypeID")
    public Result<Map<String, Object>> ksuid() {
        String ksuid = IdUtil.ksuid();
        String typeId = IdUtil.typeId("user");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ksuid", ksuid);
        data.put("ksuidValid", KsuidUtil.isValid(ksuid));
        data.put("typeId", typeId);
        data.put("typeIdValid", TypeIdUtil.isValid(typeId));
        return Result.ok(data);
    }

    @GetMapping("/host-port")
    @Operation(summary = "host:port 解析")
    public Result<Map<String, Object>> hostPort(@RequestParam(defaultValue = "example.com:8443") String value) {
        HostAndPortUtil.HostAndPort parsed = HostAndPortUtil.parse(value);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("host", parsed.host());
        data.put("port", parsed.port());
        data.put("formatted", parsed.toString());
        return Result.ok(data);
    }

    @GetMapping("/base64/url")
    @Operation(summary = "Base64 URL")
    public Result<Map<String, Object>> base64(@RequestParam(defaultValue = "hello 工具") String text) {
        String encoded = Base64Util.encodeUrl(text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", Base64Util.decodeToString(encoded));
        data.put("isBase64", Base64Util.isBase64(Base64Util.encode(text)));
        return Result.ok(data);
    }

    @GetMapping("/word")
    @Operation(summary = "单词首字母 / 标题化")
    public Result<Map<String, String>> word(@RequestParam(defaultValue = "spring BOOT utils") String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("initials", WordUtil.initials(text));
        data.put("capitalizeFully", WordUtil.capitalizeFully(text));
        data.put("wrapped", WordUtil.wrap(text, 12));
        return Result.ok(data);
    }

    @GetMapping("/range")
    @Operation(summary = "区间包含")
    public Result<Map<String, Object>> range(
            @RequestParam(defaultValue = "1") int start,
            @RequestParam(defaultValue = "10") int end,
            @RequestParam(defaultValue = "5") int value) {
        var range = RangeUtil.closed(start, end);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("contains", range.contains(value));
        data.put("empty", range.isEmpty());
        return Result.ok(data);
    }
}
