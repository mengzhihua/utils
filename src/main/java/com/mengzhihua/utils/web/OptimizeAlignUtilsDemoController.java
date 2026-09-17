package com.mengzhihua.utils.web;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.util.BankCardUtil;
import com.mengzhihua.utils.util.DateTimeUtil;
import com.mengzhihua.utils.util.EncryptUtil;
import com.mengzhihua.utils.util.HashUtil;
import com.mengzhihua.utils.util.Ipv6Util;
import com.mengzhihua.utils.util.IsbnUtil;
import com.mengzhihua.utils.util.PhoneUtil;
import com.mengzhihua.utils.util.RegexUtil;
import com.mengzhihua.utils.util.SemverUtil;
import com.mengzhihua.utils.util.Sm3Util;
import com.mengzhihua.utils.util.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Optimize Align Demo", description = "SM3 / SemVer / IPv6 / ISBN 互转 / 时长差")
public class OptimizeAlignUtilsDemoController {

    @GetMapping("/sm3")
    @Operation(summary = "SM3 国密摘要")
    public Result<Map<String, Object>> sm3(@RequestParam(defaultValue = "abc") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("sm3", Sm3Util.hash(text));
        data.put("encrypt", EncryptUtil.sm3(text));
        return Result.ok(data);
    }

    @GetMapping("/isbn/convert")
    @Operation(summary = "ISBN-10 / ISBN-13 互转")
    public Result<Map<String, Object>> isbnConvert(@RequestParam(defaultValue = "0306406152") String code) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IsbnUtil.normalize(code));
        data.put("valid", IsbnUtil.isValid(code));
        if (IsbnUtil.isValid(code)) {
            data.put("isbn13", IsbnUtil.toIsbn13(code));
            String compact = IsbnUtil.normalize(code);
            if (compact.length() == 10 || compact.startsWith("978")) {
                data.put("isbn10", IsbnUtil.toIsbn10(code));
            }
        }
        return Result.ok(data);
    }

    @GetMapping("/between")
    @Operation(summary = "时长差中文格式化")
    public Result<Map<String, Object>> between(
            @RequestParam(defaultValue = "183900") long seconds) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("formatted", DateTimeUtil.formatBetween(Duration.ofSeconds(seconds)));
        data.put("seconds", seconds);
        return Result.ok(data);
    }

    @GetMapping("/sub-between")
    @Operation(summary = "提取中间字符串")
    public Result<Map<String, Object>> subBetween(
            @RequestParam(defaultValue = "name=<Ada> age=<18>") String text,
            @RequestParam(defaultValue = "<") String before,
            @RequestParam(defaultValue = ">") String after) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("first", StringUtil.subBetween(text, before, after));
        data.put("all", StringUtil.subBetweenAll(text, before, after));
        data.put("abbreviate", StringUtil.abbreviate(text, 16));
        data.put("normalized", StringUtil.normalizeSpace("  a   b  "));
        return Result.ok(data);
    }

    @GetMapping("/ipv6")
    @Operation(summary = "IPv6 展开 / 压缩")
    public Result<Map<String, Object>> ipv6(@RequestParam(defaultValue = "2001:db8::1") String ip) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", Ipv6Util.isValid(ip));
        data.put("expanded", Ipv6Util.expand(ip));
        data.put("compressed", Ipv6Util.compress(ip));
        data.put("regex", RegexUtil.isIpv6(ip));
        return Result.ok(data);
    }

    @GetMapping("/semver")
    @Operation(summary = "SemVer 2.0 比较")
    public Result<Map<String, Object>> semver(
            @RequestParam(defaultValue = "1.0.0-alpha") String left,
            @RequestParam(defaultValue = "1.0.0") String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("leftValid", SemverUtil.isValid(left));
        data.put("rightValid", SemverUtil.isValid(right));
        data.put("compare", SemverUtil.compare(left, right));
        data.put("leftGreater", SemverUtil.isGreater(left, right));
        return Result.ok(data);
    }

    @GetMapping("/phone/region")
    @Operation(summary = "大陆 / 港澳台手机号")
    public Result<Map<String, Object>> phoneRegion(@RequestParam(defaultValue = "51234567") String mobile) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("region", PhoneUtil.region(mobile));
        data.put("hk", PhoneUtil.isMobileHk(mobile));
        data.put("tw", PhoneUtil.isMobileTw(mobile));
        data.put("mo", PhoneUtil.isMobileMo(mobile));
        data.put("tel400", PhoneUtil.isTel400(mobile));
        data.put("hidden", PhoneUtil.hide(mobile));
        return Result.ok(data);
    }

    @GetMapping("/bankcard/brand")
    @Operation(summary = "银行卡卡组织")
    public Result<Map<String, Object>> bankBrand(@RequestParam(defaultValue = "6222021234567890") String cardNo) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("brand", BankCardUtil.brand(cardNo));
        data.put("valid", BankCardUtil.isValid(cardNo));
        data.put("masked", BankCardUtil.mask(cardNo));
        return Result.ok(data);
    }

    @GetMapping("/crc16")
    @Operation(summary = "CRC-16 MODBUS / CCITT")
    public Result<Map<String, Object>> crc16(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("modbus", HashUtil.crc16Hex(text));
        data.put("ccitt", HashUtil.crc16CcittHex(text));
        return Result.ok(data);
    }
}
