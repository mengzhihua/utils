package com.mengzhihua.utils.web.controller;


import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.crypto.SignUtil;
import com.mengzhihua.utils.common.crypto.TotpUtil;
import com.mengzhihua.utils.common.extra.GeoUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.id.ShortCodeUtil;
import com.mengzhihua.utils.common.json.YamlUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.math.ByteSizeUtil;
import com.mengzhihua.utils.common.math.MoneyUtil;
import com.mengzhihua.utils.common.net.IpUtil;
import com.mengzhihua.utils.common.net.UrlUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.text.SensitiveWordUtil;
import com.mengzhihua.utils.common.text.TextUtil;
import com.mengzhihua.utils.common.time.CronUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import com.mengzhihua.utils.common.validate.BankCardUtil;
import com.mengzhihua.utils.common.validate.CreditCodeUtil;
import com.mengzhihua.utils.common.validate.PhoneUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Business Utils Demo", description = "业务补充工具演示接口")
public class BusinessUtilsDemoController {

    @GetMapping("/credit-code/parse")
    @Operation(summary = "统一社会信用代码校验")
    public Result<Map<String, Object>> creditCode(@RequestParam String code) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", CreditCodeUtil.isValid(code));
        data.put("normalized", code == null ? "" : code.trim().toUpperCase());
        return Result.ok(data);
    }

    @GetMapping("/bankcard/luhn")
    @Operation(summary = "银行卡 Luhn 校验")
    public Result<Map<String, Object>> bankcard(@RequestParam String cardNo) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", BankCardUtil.isValid(cardNo));
        data.put("masked", BankCardUtil.mask(cardNo));
        data.put("brand", BankCardUtil.brand(cardNo));
        return Result.ok(data);
    }

    @GetMapping("/money/fen")
    @Operation(summary = "元 / 分互转")
    public Result<Map<String, Object>> moneyFen(@RequestParam String yuan) {
        long fen = MoneyUtil.yuanToFen(yuan);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("yuan", yuan);
        data.put("fen", fen);
        data.put("back", MoneyUtil.fenToYuanString(fen));
        data.put("split3", MoneyUtil.split(fen < 3 ? 300 : fen, 3));
        return Result.ok(data);
    }

    @GetMapping("/phone/carrier")
    @Operation(summary = "手机号运营商")
    public Result<Map<String, Object>> carrier(@RequestParam String mobile) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", RegexUtil.isMobile(mobile));
        data.put("carrier", PhoneUtil.carrier(mobile));
        data.put("virtual", PhoneUtil.isVirtual(mobile));
        data.put("region", PhoneUtil.region(mobile));
        data.put("hidden", PhoneUtil.hide(mobile));
        return Result.ok(data);
    }

    @GetMapping("/geo/transform")
    @Operation(summary = "WGS84 / GCJ-02 / BD-09 互转")
    public Result<Map<String, Object>> transform(
            @RequestParam double lat,
            @RequestParam double lon) {
        double[] gcj = GeoUtil.wgs84ToGcj02(lat, lon);
        double[] wgs = GeoUtil.gcj02ToWgs84(gcj[0], gcj[1]);
        double[] bd = GeoUtil.gcj02ToBd09(gcj[0], gcj[1]);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("wgs84", Map.of("lat", lat, "lon", lon));
        data.put("gcj02", Map.of("lat", gcj[0], "lon", gcj[1]));
        data.put("bd09", Map.of("lat", bd[0], "lon", bd[1]));
        data.put("roundTripWgs84", Map.of("lat", wgs[0], "lon", wgs[1]));
        return Result.ok(data);
    }

    @GetMapping("/sign/md5")
    @Operation(summary = "参数排序 MD5 签名")
    public Result<Map<String, String>> sign(
            @RequestParam String appId,
            @RequestParam String timestamp,
            @RequestParam(defaultValue = "demo-secret") String secret) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("appId", appId);
        params.put("timestamp", timestamp);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("canonical", SignUtil.canonical(params, secret));
        data.put("md5", SignUtil.md5(params, secret));
        data.put("hmacSha256", SignUtil.hmacSha256(params, secret));
        return Result.ok(data);
    }

    @GetMapping("/totp")
    @Operation(summary = "TOTP 当前口令")
    public Result<Map<String, Object>> totp(
            @RequestParam(defaultValue = "GEZDGNBVGY3TQOJQGEZDGNBVGY3TQOJQ") String secret) {
        String code = TotpUtil.generate(secret);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", code);
        data.put("valid", TotpUtil.verify(code, secret));
        data.put("periodSeconds", 30);
        return Result.ok(data);
    }

    @GetMapping("/cron/next")
    @Operation(summary = "Cron 下次触发")
    public Result<Map<String, Object>> cron(
            @RequestParam(defaultValue = "0 0 9 * * MON-FRI") String expression) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", CronUtil.isValid(expression));
        data.put("next", CronUtil.nextTimes(expression, ZonedDateTime.now(DateTimeUtil.DEFAULT_ZONE), 3));
        return Result.ok(data);
    }

    @GetMapping("/workday")
    @Operation(summary = "工作日加减（跳过周末）")
    public Result<Map<String, Object>> workday(
            @RequestParam(defaultValue = "2026-09-07") String date,
            @RequestParam(defaultValue = "3") int days) {
        LocalDate start = DateTimeUtil.parseDate(date);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("workday", DateTimeUtil.isWorkday(start));
        data.put("plus", DateTimeUtil.format(DateTimeUtil.plusWorkdays(start, days)));
        data.put("relative", DateTimeUtil.fromNow(DateTimeUtil.startOfDay(start)));
        return Result.ok(data);
    }

    @GetMapping("/id/ulid")
    @Operation(summary = "ULID / Base62 短码")
    public Result<Map<String, Object>> ulid(@RequestParam(defaultValue = "123456") long id) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ulid", IdUtil.ulid());
        data.put("shortCode", ShortCodeUtil.encode(id));
        data.put("decoded", ShortCodeUtil.decode(ShortCodeUtil.encode(id)));
        return Result.ok(data);
    }

    @GetMapping("/bytesize")
    @Operation(summary = "字节大小格式化")
    public Result<Map<String, Object>> byteSize(@RequestParam(defaultValue = "1536000") long bytes) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("formatted", ByteSizeUtil.format(bytes));
        data.put("parsed", ByteSizeUtil.parse(ByteSizeUtil.format(bytes)));
        return Result.ok(data);
    }

    @GetMapping("/ip/cidr")
    @Operation(summary = "CIDR 网络计算")
    public Result<Map<String, Object>> cidr(
            @RequestParam(defaultValue = "172.16.0.10") String ip,
            @RequestParam(defaultValue = "172.16.0.0/24") String cidr) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("inCidr", IpUtil.inCidr(ip, cidr));
        data.put("network", IpUtil.cidrNetwork(cidr));
        data.put("broadcast", IpUtil.cidrBroadcast(cidr));
        data.put("hostCount", IpUtil.cidrHostCount(cidr));
        return Result.ok(data);
    }

    @GetMapping("/template")
    @Operation(summary = "占位符模板")
    public Result<Map<String, String>> template(
            @RequestParam(defaultValue = "你好，{name}") String template,
            @RequestParam(defaultValue = "Ada") String name) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("result", StringUtil.format(template, Map.of("name", name)));
        data.put("kebab", StringUtil.toKebab("userName"));
        data.put("pascal", StringUtil.toPascal("user_name"));
        data.put("halfWidth", StringUtil.toHalfWidth("Ｈｅｌｌｏ　１２３"));
        return Result.ok(data);
    }

    @GetMapping("/text/similar")
    @Operation(summary = "文本相似度")
    public Result<Map<String, Object>> similar(@RequestParam String left, @RequestParam String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("distance", TextUtil.levenshtein(left, right));
        data.put("similarity", TextUtil.similarity(left, right));
        return Result.ok(data);
    }

    @GetMapping("/sensitive")
    @Operation(summary = "敏感词替换")
    public Result<Map<String, Object>> sensitive(@RequestParam String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("hits", SensitiveWordUtil.findAll(text));
        data.put("replaced", SensitiveWordUtil.replace(text, '*'));
        return Result.ok(data);
    }

    @GetMapping("/encrypt/hmac")
    @Operation(summary = "HMAC-SHA256 / CRC32")
    public Result<Map<String, String>> hmac(
            @RequestParam String text,
            @RequestParam(defaultValue = "secret") String secret) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("hmacSha256", EncryptUtil.hmacSha256(text, secret));
        data.put("crc32", EncryptUtil.crc32(text));
        data.put("base64", EncryptUtil.encodeBase64(text));
        return Result.ok(data);
    }

    @GetMapping("/url/query")
    @Operation(summary = "QueryString 解析")
    public Result<Map<String, Object>> query(@RequestParam String url) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("params", UrlUtil.parseQuery(url));
        return Result.ok(data);
    }

    @PostMapping("/yaml/from-json")
    @Operation(summary = "JSON 转 YAML")
    public Result<Map<String, String>> yaml(@Valid @RequestBody JsonBody body) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("yaml", YamlUtil.jsonToYaml(body.json()));
        return Result.ok(data);
    }

    public record JsonBody(@NotBlank String json) {
    }
}
