package com.mengzhihua.utils.web.controller;


import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Base32Util;
import com.mengzhihua.utils.common.crypto.Blake2sUtil;
import com.mengzhihua.utils.common.crypto.CmacUtil;
import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.Sm4Util;
import com.mengzhihua.utils.common.lang.ByteUtil;
import com.mengzhihua.utils.common.text.PinyinUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.text.XmlUtil;
import com.mengzhihua.utils.common.time.SolarTermUtil;
import com.mengzhihua.utils.common.validate.HkIdUtil;
import com.mengzhihua.utils.common.validate.OrgCodeUtil;
import com.mengzhihua.utils.common.validate.TwIdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Continue Align Demo", description = "SM4 / BLAKE2s / CMAC / 港澳台证件 / 节气 / 拼音")
public class ContinueAlignUtilsDemoController {

    private static final String SM4_VECTOR_KEY = "0123456789ABCDEFFEDCBA9876543210";
    private static final String CMAC_KEY = "2b7e151628aed2a6abf7158809cf4f3c";

    @GetMapping("/sm4")
    @Operation(summary = "SM4 国密分组")
    public Result<Map<String, Object>> sm4(
            @RequestParam(defaultValue = "hello") String text,
            @RequestParam(defaultValue = "secret") String password) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("vector", Sm4Util.encryptEcbHex(SM4_VECTOR_KEY, SM4_VECTOR_KEY));
        String cipher = Sm4Util.encrypt(text, password);
        data.put("cipher", cipher);
        data.put("plain", Sm4Util.decrypt(cipher, password));
        return Result.ok(data);
    }

    @GetMapping("/blake2s")
    @Operation(summary = "BLAKE2s-256")
    public Result<Map<String, Object>> blake2s(@RequestParam(defaultValue = "abc") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("blake2s", Blake2sUtil.hash(text));
        return Result.ok(data);
    }

    @GetMapping("/cmac")
    @Operation(summary = "AES-CMAC RFC 4493")
    public Result<Map<String, Object>> cmac(@RequestParam(defaultValue = "") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("cmac", CmacUtil.hex(text, CMAC_KEY));
        data.put("empty", CmacUtil.hex("", CMAC_KEY));
        return Result.ok(data);
    }

    @GetMapping("/hkid")
    @Operation(summary = "香港身份证")
    public Result<Map<String, Object>> hkid(@RequestParam(defaultValue = "A123456(3)") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", HkIdUtil.normalize(value));
        data.put("valid", HkIdUtil.isValid(value));
        data.put("regex", RegexUtil.is("hkid", value));
        return Result.ok(data);
    }

    @GetMapping("/twid")
    @Operation(summary = "台湾身份证")
    public Result<Map<String, Object>> twid(@RequestParam(defaultValue = "A123456789") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", TwIdUtil.normalize(value));
        data.put("valid", TwIdUtil.isValid(value));
        data.put("regex", RegexUtil.is("twid", value));
        return Result.ok(data);
    }

    @GetMapping("/org-code")
    @Operation(summary = "组织机构代码")
    public Result<Map<String, Object>> orgCode(@RequestParam(defaultValue = "12345678") String code) {
        Map<String, Object> data = new LinkedHashMap<>();
        String compact = OrgCodeUtil.normalize(code);
        data.put("normalized", compact);
        if (compact.length() == 8) {
            data.put("complete", OrgCodeUtil.complete(compact));
            data.put("valid", OrgCodeUtil.isValid(OrgCodeUtil.complete(compact)));
        } else {
            data.put("valid", OrgCodeUtil.isValid(code));
        }
        return Result.ok(data);
    }

    @GetMapping("/solar-term")
    @Operation(summary = "二十四节气")
    public Result<Map<String, Object>> solarTerm(
            @RequestParam(defaultValue = "2026") int year,
            @RequestParam(defaultValue = "清明") String name) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("date", String.valueOf(SolarTermUtil.date(year, name)));
        data.put("qingming", SolarTermUtil.date(year, SolarTermUtil.Term.QING_MING).toString());
        data.put("terms", SolarTermUtil.yearTerms(year));
        return Result.ok(data);
    }

    @GetMapping("/pinyin")
    @Operation(summary = "拼音首字母")
    public Result<Map<String, Object>> pinyin(@RequestParam(defaultValue = "中国") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("firstLetters", PinyinUtil.firstLetters(text));
        return Result.ok(data);
    }

    @GetMapping("/xml/pretty")
    @Operation(summary = "XML 格式化 / XPath")
    public Result<Map<String, Object>> xmlPretty(
            @RequestParam(defaultValue = "<root><n>Ada</n></root>") String xml,
            @RequestParam(defaultValue = "/root/n") String xpath) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("pretty", XmlUtil.pretty(xml));
        data.put("xpath", XmlUtil.xpath(xml, xpath));
        return Result.ok(data);
    }

    @GetMapping("/chacha")
    @Operation(summary = "ChaCha20-Poly1305")
    public Result<Map<String, Object>> chacha(
            @RequestParam(defaultValue = "hello") String text,
            @RequestParam(defaultValue = "secret") String password) {
        Map<String, Object> data = new LinkedHashMap<>();
        String cipher = EncryptUtil.chachaEncrypt(text, password);
        data.put("cipher", cipher);
        data.put("plain", EncryptUtil.chachaDecrypt(cipher, password));
        return Result.ok(data);
    }

    @GetMapping("/crc64")
    @Operation(summary = "CRC-64 / CRC-8 / FNV-1a 64")
    public Result<Map<String, Object>> crc64(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc64", HashUtil.crc64Hex(text));
        data.put("crc8", HashUtil.crc8Hex(text));
        data.put("fnv1a64", HashUtil.fnv1a64Hex(text));
        data.put("crockford", Base32Util.encodeCrockford(text));
        data.put("intBytes", HexFormat.of().formatHex(ByteUtil.fromInt(0x01020304)));
        return Result.ok(data);
    }
}
