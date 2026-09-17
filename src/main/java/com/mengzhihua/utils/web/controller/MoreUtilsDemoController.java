package com.mengzhihua.utils.web.controller;


import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Base58Util;
import com.mengzhihua.utils.common.codec.HashidsUtil;
import com.mengzhihua.utils.common.codec.RomanUtil;
import com.mengzhihua.utils.common.concurrent.ConsistentHashUtil;
import com.mengzhihua.utils.common.id.SeqUtil;
import com.mengzhihua.utils.common.math.MathUtil;
import com.mengzhihua.utils.common.math.UnitConvertUtil;
import com.mengzhihua.utils.common.net.UrlBuilder;
import com.mengzhihua.utils.common.net.UrlUtil;
import com.mengzhihua.utils.common.text.ReUtil;
import com.mengzhihua.utils.common.text.TextDiffUtil;
import com.mengzhihua.utils.common.time.WeekUtil;
import com.mengzhihua.utils.common.validate.ImeiUtil;
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
@Tag(name = "More Utils Demo", description = "正则 / 进制 / 单位 / Hashids 等演示")
public class MoreUtilsDemoController {

    @GetMapping("/math")
    @Operation(summary = "最大公约数 / 最小公倍数 / 组合")
    public Result<Map<String, Object>> math(
            @RequestParam(defaultValue = "12") long a,
            @RequestParam(defaultValue = "18") long b) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("gcd", MathUtil.gcd(a, b));
        data.put("lcm", MathUtil.lcm(a, b));
        data.put("aPrime", MathUtil.isPrime(a));
        data.put("bPrime", MathUtil.isPrime(b));
        int n = (int) MathUtil.clamp(Math.max(a, b), 0, 20);
        int m = (int) Math.min(Math.abs(Math.min(a, b)), n);
        data.put("combination", MathUtil.combination(n, m).toString());
        data.put("sqrtA", MathUtil.sqrt(a, 4).toPlainString());
        return Result.ok(data);
    }

    @GetMapping("/unit")
    @Operation(summary = "单位换算")
    public Result<Map<String, Object>> unit(
            @RequestParam(defaultValue = "1") String value,
            @RequestParam(defaultValue = "km") String from,
            @RequestParam(defaultValue = "m") String to) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("value", UnitConvertUtil.convert(value, from, to).stripTrailingZeros().toPlainString());
        data.put("formatted", UnitConvertUtil.format(value, from, to));
        return Result.ok(data);
    }

    @GetMapping("/imei")
    @Operation(summary = "IMEI 校验 / 生成")
    public Result<Map<String, Object>> imei(@RequestParam(defaultValue = "490154203237518") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", ImeiUtil.isValid(value));
        data.put("normalized", ImeiUtil.normalize(value));
        data.put("sample", ImeiUtil.generate());
        return Result.ok(data);
    }

    @GetMapping("/url/parse")
    @Operation(summary = "URL 解析")
    public Result<Map<String, Object>> urlParse(
            @RequestParam(defaultValue = "https://example.com:8443/search?q=工具#top") String url) {
        UrlUtil.Parts parts = UrlUtil.parse(url);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("scheme", parts.scheme());
        data.put("host", parts.host());
        data.put("port", parts.port());
        data.put("path", parts.path());
        data.put("query", parts.query());
        data.put("fragment", parts.fragment());
        data.put("queryMap", UrlUtil.parseQuery(url));
        return Result.ok(data);
    }

    @GetMapping("/url/build")
    @Operation(summary = "URL 拼接")
    public Result<Map<String, String>> urlBuild(
            @RequestParam(defaultValue = "https") String scheme,
            @RequestParam(defaultValue = "example.com") String host,
            @RequestParam(defaultValue = "/search") String path,
            @RequestParam(defaultValue = "q") String key,
            @RequestParam(defaultValue = "工具") String value) {
        String built = UrlBuilder.of()
                .scheme(scheme)
                .host(host)
                .path(path)
                .query(key, value)
                .build();
        return Result.ok(Map.of("url", built));
    }

    @PostMapping("/text-diff")
    @Operation(summary = "文本行 diff")
    public Result<Map<String, Object>> textDiff(@RequestBody Map<String, String> body) {
        String left = body.getOrDefault("left", "a\nb\nc");
        String right = body.getOrDefault("right", "a\nc\nd");
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("unified", TextDiffUtil.unified(left, right));
        data.put("changed", TextDiffUtil.changedLines(left, right));
        data.put("ops", TextDiffUtil.diffLines(left, right).stream()
                .map(diff -> Map.of("op", diff.op().name(), "text", diff.text()))
                .toList());
        return Result.ok(data);
    }

    @GetMapping("/base58")
    @Operation(summary = "Base58")
    public Result<Map<String, String>> base58(@RequestParam(defaultValue = "hello") String text) {
        String encoded = Base58Util.encode(text);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", Base58Util.decodeToString(encoded));
        return Result.ok(data);
    }

    @GetMapping("/roman")
    @Operation(summary = "罗马数字")
    public Result<Map<String, Object>> roman(@RequestParam(defaultValue = "1994") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        if (value.chars().allMatch(Character::isDigit)) {
            int number = Integer.parseInt(value);
            String roman = RomanUtil.toRoman(number);
            data.put("roman", roman);
            data.put("number", RomanUtil.fromRoman(roman));
        } else {
            int number = RomanUtil.fromRoman(value);
            data.put("number", number);
            data.put("roman", RomanUtil.toRoman(number));
        }
        return Result.ok(data);
    }

    @GetMapping("/hashids")
    @Operation(summary = "Hashids 混淆 ID")
    public Result<Map<String, Object>> hashids(@RequestParam(defaultValue = "123") long id) {
        String encoded = HashidsUtil.encode(id);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", HashidsUtil.decodeOne(encoded));
        return Result.ok(data);
    }

    @GetMapping("/week")
    @Operation(summary = "ISO 周 / 星期")
    public Result<Map<String, Object>> week(@RequestParam(defaultValue = "2024-02-10") String date) {
        LocalDate localDate = LocalDate.parse(date);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("isoWeek", WeekUtil.isoWeek(localDate));
        data.put("isoWeekYear", WeekUtil.isoWeekYear(localDate));
        data.put("chinese", WeekUtil.chineseDayOfWeek(localDate));
        data.put("start", WeekUtil.startOfIsoWeek(localDate).toString());
        data.put("end", WeekUtil.endOfIsoWeek(localDate).toString());
        data.put("weekend", WeekUtil.isWeekend(localDate));
        data.put("display", WeekUtil.display(localDate));
        return Result.ok(data);
    }

    @GetMapping("/seq")
    @Operation(summary = "日期序列号")
    public Result<Map<String, Object>> seq(@RequestParam(defaultValue = "ORD") String prefix) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("next", SeqUtil.next(prefix));
        data.put("current", SeqUtil.current(prefix));
        return Result.ok(data);
    }

    @GetMapping("/re")
    @Operation(summary = "正则提取")
    public Result<Map<String, Object>> re(
            @RequestParam(defaultValue = "\\d+") String pattern,
            @RequestParam(defaultValue = "ab12cd34") String text,
            @RequestParam(defaultValue = "*") String replacement) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", ReUtil.isValid(pattern));
        data.put("matches", ReUtil.isMatch(pattern, text));
        data.put("contains", ReUtil.contains(pattern, text));
        data.put("first", ReUtil.getGroup0(pattern, text));
        data.put("all", ReUtil.findAll(pattern, text));
        data.put("count", ReUtil.count(pattern, text));
        data.put("groups", ReUtil.getAllGroups(pattern, text));
        data.put("named", ReUtil.getNamedGroups(pattern, text));
        data.put("split", ReUtil.split(pattern, text));
        data.put("replaced", ReUtil.replaceAll(text, pattern, replacement));
        data.put("escaped", ReUtil.escape(pattern));
        return Result.ok(data);
    }

    @GetMapping("/hashids/nodes")
    @Operation(summary = "一致性哈希示例")
    public Result<Map<String, Object>> consistent(
            @RequestParam(defaultValue = "user-42") String key) {
        var ring = com.mengzhihua.utils.common.concurrent.ConsistentHashUtil.of(List.of("node-a", "node-b", "node-c"));
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("node", ring.get(key));
        data.put("size", ring.size());
        return Result.ok(data);
    }
}
