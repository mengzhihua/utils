package com.mengzhihua.utils.web.controller;


import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Bech32Util;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.HkdfUtil;
import com.mengzhihua.utils.common.id.Cuid2Util;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.id.SqidsUtil;
import com.mengzhihua.utils.common.io.ZipUtil;
import com.mengzhihua.utils.common.lang.BitUtil;
import com.mengzhihua.utils.common.net.EmailUtil;
import com.mengzhihua.utils.common.net.MediaTypeUtil;
import com.mengzhihua.utils.common.text.CaseFormatUtil;
import com.mengzhihua.utils.common.text.HumanizeUtil;
import com.mengzhihua.utils.common.text.InflectorUtil;
import com.mengzhihua.utils.common.text.MetaphoneUtil;
import com.mengzhihua.utils.common.text.MorseUtil;
import com.mengzhihua.utils.common.text.RotUtil;
import com.mengzhihua.utils.common.text.TextUtil;
import com.mengzhihua.utils.common.text.WildcardUtil;
import com.mengzhihua.utils.common.validate.BicUtil;
import com.mengzhihua.utils.common.validate.CheckDigitUtil;
import com.mengzhihua.utils.common.validate.IsinUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Library Align Demo", description = "对标 Sqids / Commons / Guava / Hutool 的更多工具")
public class LibraryAlignUtilsDemoController {

    @GetMapping("/sqids")
    @Operation(summary = "Sqids 可逆 ID")
    public Result<Map<String, Object>> sqids(@RequestParam(defaultValue = "1,2,3") String numbers) {
        long[] parsed = Arrays.stream(numbers.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .mapToLong(Long::parseLong)
                .toArray();
        String encoded = SqidsUtil.encode(parsed);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", Arrays.stream(SqidsUtil.decode(encoded)).boxed().toList());
        return Result.ok(data);
    }

    @GetMapping("/uuid-name")
    @Operation(summary = "UUID v3 / v5")
    public Result<Map<String, Object>> uuidName(@RequestParam(defaultValue = "www.example.com") String name) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("uuidV3", IdUtil.uuidV3(name));
        data.put("uuidV5", IdUtil.uuidV5(name));
        data.put("nanoId", IdUtil.nanoId());
        data.put("cuid2", Cuid2Util.next());
        return Result.ok(data);
    }

    @GetMapping("/metaphone")
    @Operation(summary = "Metaphone 读音码")
    public Result<Map<String, Object>> metaphone(
            @RequestParam(defaultValue = "Philip") String left,
            @RequestParam(defaultValue = "Phillip") String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("left", MetaphoneUtil.encode(left));
        data.put("right", MetaphoneUtil.encode(right));
        data.put("similar", MetaphoneUtil.similar(left, right));
        return Result.ok(data);
    }

    @GetMapping("/isin")
    @Operation(summary = "ISIN 校验")
    public Result<Map<String, Object>> isin(@RequestParam(defaultValue = "US0378331005") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", IsinUtil.isValid(value));
        data.put("normalized", IsinUtil.normalize(value));
        return Result.ok(data);
    }

    @GetMapping("/bic")
    @Operation(summary = "SWIFT BIC")
    public Result<Map<String, Object>> bic(@RequestParam(defaultValue = "DEUTDEFF") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", BicUtil.isValid(value));
        data.put("normalized", BicUtil.normalize(value));
        data.put("bankCode", BicUtil.bankCode(value));
        data.put("countryCode", BicUtil.countryCode(value));
        return Result.ok(data);
    }

    @GetMapping("/case-format")
    @Operation(summary = "Guava CaseFormat")
    public Result<Map<String, String>> caseFormat(@RequestParam(defaultValue = "springBootUtils") String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("lowerCamel", CaseFormatUtil.toLowerCamel(text));
        data.put("upperCamel", CaseFormatUtil.toUpperCamel(text));
        data.put("lowerUnderscore", CaseFormatUtil.toLowerUnderscore(text));
        data.put("upperUnderscore", CaseFormatUtil.toUpperUnderscore(text));
        data.put("lowerHyphen", CaseFormatUtil.toLowerHyphen(text));
        return Result.ok(data);
    }

    @GetMapping("/media-type")
    @Operation(summary = "MediaType 解析")
    public Result<Map<String, Object>> mediaType(
            @RequestParam(defaultValue = "application/json; charset=utf-8") String value) {
        var parsed = MediaTypeUtil.parse(value);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("type", parsed.type());
        data.put("subtype", parsed.subtype());
        data.put("charset", parsed.parameter("charset"));
        data.put("json", MediaTypeUtil.isJson(value));
        data.put("formatted", parsed.toString());
        return Result.ok(data);
    }

    @GetMapping("/morse")
    @Operation(summary = "摩斯电码")
    public Result<Map<String, String>> morse(@RequestParam(defaultValue = "SOS") String text) {
        String encoded = MorseUtil.encode(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", MorseUtil.decode(encoded));
        return Result.ok(data);
    }

    @GetMapping("/gzip")
    @Operation(summary = "Gzip Base64")
    public Result<Map<String, Object>> gzip(@RequestParam(defaultValue = "hello 工具") String text) {
        String encoded = ZipUtil.gzipBase64(text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("gzipBase64", encoded);
        data.put("decoded", ZipUtil.ungzipBase64(encoded));
        return Result.ok(data);
    }

    @GetMapping("/bech32")
    @Operation(summary = "Bech32")
    public Result<Map<String, Object>> bech32(
            @RequestParam(defaultValue = "xyz") String hrp,
            @RequestParam(defaultValue = "hello") String text) {
        String encoded = Bech32Util.encodeText(hrp, text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", Bech32Util.decodeToString(encoded));
        data.put("hrp", Bech32Util.decode(encoded).hrp());
        return Result.ok(data);
    }

    @GetMapping("/hkdf")
    @Operation(summary = "HKDF-SHA256")
    public Result<Map<String, Object>> hkdf(
            @RequestParam(defaultValue = "hello") String ikm,
            @RequestParam(defaultValue = "salt") String salt,
            @RequestParam(defaultValue = "info") String info,
            @RequestParam(defaultValue = "32") int length) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("okm", HkdfUtil.deriveHex(ikm, salt, info, length));
        data.put("length", length);
        return Result.ok(data);
    }

    @GetMapping("/check-digit")
    @Operation(summary = "Luhn / Verhoeff / Damm")
    public Result<Map<String, Object>> checkDigit(@RequestParam(defaultValue = "79927398713") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("luhn", CheckDigitUtil.luhn(value));
        data.put("verhoeff", CheckDigitUtil.verhoeff(value));
        data.put("damm", CheckDigitUtil.damm(value));
        return Result.ok(data);
    }

    @GetMapping("/humanize")
    @Operation(summary = "紧凑数字 / 序数")
    public Result<Map<String, Object>> humanize(
            @RequestParam(defaultValue = "1234") long value,
            @RequestParam(defaultValue = "21") int ordinal) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("compact", HumanizeUtil.compact(value));
        data.put("ordinal", HumanizeUtil.ordinal(ordinal));
        data.put("plural", InflectorUtil.pluralize("city"));
        return Result.ok(data);
    }

    @GetMapping("/rot13")
    @Operation(summary = "ROT13")
    public Result<Map<String, String>> rot13(@RequestParam(defaultValue = "Hello") String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("rot13", RotUtil.rot13(text));
        data.put("roundTrip", RotUtil.rot13(RotUtil.rot13(text)));
        data.put("rot47", RotUtil.rot47(text));
        return Result.ok(data);
    }

    @GetMapping("/wildcard")
    @Operation(summary = "通配符匹配")
    public Result<Map<String, Object>> wildcard(
            @RequestParam(defaultValue = "Foo.java") String text,
            @RequestParam(defaultValue = "*.java") String pattern) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("matched", WildcardUtil.match(text, pattern));
        data.put("regex", WildcardUtil.toRegex(pattern));
        return Result.ok(data);
    }

    @GetMapping("/email-parse")
    @Operation(summary = "邮箱解析")
    public Result<Map<String, Object>> emailParse(@RequestParam(defaultValue = "ada+dev@example.com") String value) {
        var parsed = EmailUtil.parse(value);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", EmailUtil.isValid(value));
        data.put("local", parsed.local());
        data.put("localBase", parsed.localBase());
        data.put("plusTag", parsed.plusTag());
        data.put("domain", parsed.domain());
        data.put("normalized", parsed.normalized());
        return Result.ok(data);
    }

    @GetMapping("/hash/crc32c")
    @Operation(summary = "CRC-32C")
    public Result<Map<String, Object>> crc32c(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc32c", HashUtil.crc32cHex(text));
        data.put("bitCount", BitUtil.count(0b1011));
        return Result.ok(data);
    }
}
