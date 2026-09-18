package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.AcceptEncodingUtil;
import com.mengzhihua.utils.common.net.CspUtil;
import com.mengzhihua.utils.common.net.HstsUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CnpUtil;
import com.mengzhihua.utils.common.validate.EgnUtil;
import com.mengzhihua.utils.common.validate.IcoUtil;
import com.mengzhihua.utils.common.validate.IsraeliIdUtil;
import com.mengzhihua.utils.common.validate.OibUtil;
import com.mengzhihua.utils.common.validate.RegonUtil;
import com.mengzhihua.utils.common.validate.TcKimlikUtil;
import com.mengzhihua.utils.common.validate.ThaiIdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "More ID Align Demo", description = "TCKN / CNP / OIB / EGN / Thai ID / REGON / HSTS")
public class MoreIdAlignUtilsDemoController {

    @GetMapping("/tckn")
    @Operation(summary = "土耳其身份证")
    public Result<Map<String, Object>> tckn(@RequestParam(defaultValue = "10000000146") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", TcKimlikUtil.normalize(value));
        data.put("valid", TcKimlikUtil.isValid(value));
        data.put("regex", RegexUtil.is("tckn", value));
        return Result.ok(data);
    }

    @GetMapping("/israeli-id")
    @Operation(summary = "以色列身份证")
    public Result<Map<String, Object>> israeliId(@RequestParam(defaultValue = "123456782") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IsraeliIdUtil.normalize(value));
        data.put("valid", IsraeliIdUtil.isValid(value));
        data.put("regex", RegexUtil.is("israeliid", value));
        return Result.ok(data);
    }

    @GetMapping("/cnp")
    @Operation(summary = "罗马尼亚 CNP")
    public Result<Map<String, Object>> cnp(@RequestParam(defaultValue = "1800101010015") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CnpUtil.normalize(value));
        data.put("valid", CnpUtil.isValid(value));
        if (CnpUtil.isValid(value)) {
            data.put("birthDate", CnpUtil.birthDate(value).toString());
            data.put("female", CnpUtil.female(value));
        }
        data.put("regex", RegexUtil.is("cnp", value));
        return Result.ok(data);
    }

    @GetMapping("/oib")
    @Operation(summary = "克罗地亚 OIB")
    public Result<Map<String, Object>> oib(@RequestParam(defaultValue = "12345678903") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", OibUtil.normalize(value));
        data.put("valid", OibUtil.isValid(value));
        data.put("regex", RegexUtil.is("oib", value));
        return Result.ok(data);
    }

    @GetMapping("/egn")
    @Operation(summary = "保加利亚 EGN")
    public Result<Map<String, Object>> egn(@RequestParam(defaultValue = "8001010008") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EgnUtil.normalize(value));
        data.put("valid", EgnUtil.isValid(value));
        if (EgnUtil.isValid(value)) {
            data.put("birthDate", EgnUtil.birthDate(value).toString());
            data.put("female", EgnUtil.female(value));
        }
        data.put("regex", RegexUtil.is("egn", value));
        return Result.ok(data);
    }

    @GetMapping("/thai-id")
    @Operation(summary = "泰国身份证")
    public Result<Map<String, Object>> thaiId(@RequestParam(defaultValue = "1234567890121") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", ThaiIdUtil.normalize(value));
        data.put("valid", ThaiIdUtil.isValid(value));
        data.put("regex", RegexUtil.is("thaiid", value));
        return Result.ok(data);
    }

    @GetMapping("/regon")
    @Operation(summary = "波兰 REGON")
    public Result<Map<String, Object>> regon(@RequestParam(defaultValue = "123456785") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", RegonUtil.normalize(value));
        data.put("valid", RegonUtil.isValid(value));
        data.put("regex", RegexUtil.is("regon", value));
        return Result.ok(data);
    }

    @GetMapping("/ico")
    @Operation(summary = "捷克 IČO")
    public Result<Map<String, Object>> ico(@RequestParam(defaultValue = "25596641") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IcoUtil.normalize(value));
        data.put("valid", IcoUtil.isValid(value));
        data.put("regex", RegexUtil.is("ico", value));
        return Result.ok(data);
    }

    @GetMapping("/hsts")
    @Operation(summary = "HTTP Strict-Transport-Security RFC 6797")
    public Result<Map<String, Object>> hsts(
            @RequestParam(defaultValue = "max-age=31536000; includeSubDomains; preload") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("maxAge", HstsUtil.maxAge(header));
        data.put("includeSubDomains", HstsUtil.includeSubDomains(header));
        data.put("preload", HstsUtil.preload(header));
        return Result.ok(data);
    }

    @GetMapping("/csp")
    @Operation(summary = "Content-Security-Policy")
    public Result<Map<String, Object>> csp(
            @RequestParam(defaultValue = "default-src 'self'; script-src 'self' https://cdn.example") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("directives", CspUtil.parse(header));
        data.put("defaultSrc", CspUtil.directive(header, "default-src"));
        data.put("allowsSelfScript", CspUtil.allows(header, "script-src", "'self'"));
        return Result.ok(data);
    }

    @GetMapping("/accept-encoding")
    @Operation(summary = "HTTP Accept-Encoding RFC 9110")
    public Result<Map<String, Object>> acceptEncoding(
            @RequestParam(defaultValue = "gzip;q=1.0, br;q=0.8, deflate;q=0.5") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encodings", AcceptEncodingUtil.parse(header));
        data.put("negotiated", AcceptEncodingUtil.negotiate(header, "br", "gzip"));
        return Result.ok(data);
    }

    @GetMapping("/crc16-kermit")
    @Operation(summary = "CRC-16/KERMIT")
    public Result<Map<String, Object>> crc16Kermit(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Kermit", HashUtil.crc16KermitHex(text));
        data.put("crc32Bzip2", HashUtil.crc32Bzip2Hex(text));
        return Result.ok(data);
    }
}
