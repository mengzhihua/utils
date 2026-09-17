package com.mengzhihua.utils.web.controller;


import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.bean.MapPathUtil;
import com.mengzhihua.utils.common.concurrent.CircuitBreakerUtil;
import com.mengzhihua.utils.common.concurrent.RateLimiterUtil;
import com.mengzhihua.utils.common.concurrent.WeightRandomUtil;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.extra.ColorUtil;
import com.mengzhihua.utils.common.extra.VerifyCodeUtil;
import com.mengzhihua.utils.common.io.FileTypeUtil;
import com.mengzhihua.utils.common.io.FileUtil;
import com.mengzhihua.utils.common.math.PercentUtil;
import com.mengzhihua.utils.common.net.AntPathUtil;
import com.mengzhihua.utils.common.text.EscapeUtil;
import com.mengzhihua.utils.common.text.HighlightUtil;
import com.mengzhihua.utils.common.text.SlugUtil;
import com.mengzhihua.utils.common.time.DurationUtil;
import com.mengzhihua.utils.common.time.ZodiacUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Runtime Utils Demo", description = "限流 / 路径 / 颜色 / 生肖等演示")
public class RuntimeUtilsDemoController {

    @GetMapping("/rate-limit")
    @Operation(summary = "进程内令牌桶试探")
    public Result<Map<String, Object>> rateLimit(
            @RequestParam(defaultValue = "demo") String key,
            @RequestParam(defaultValue = "3") double qps) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("allowed", RateLimiterUtil.tryAcquire(key, qps));
        data.put("qps", qps);
        return Result.ok(data);
    }

    @GetMapping("/ant-path")
    @Operation(summary = "Ant 路径匹配")
    public Result<Map<String, Object>> antPath(
            @RequestParam(defaultValue = "/api/**") String pattern,
            @RequestParam(defaultValue = "/api/utils/ip") String path) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("matched", AntPathUtil.match(pattern, path));
        data.put("extracted", AntPathUtil.extractPath(pattern, path));
        return Result.ok(data);
    }

    @GetMapping("/color")
    @Operation(summary = "颜色 HEX / RGB")
    public Result<Map<String, Object>> color(@RequestParam(defaultValue = "#0f766e") String hex) {
        int[] rgb = ColorUtil.hexToRgb(hex);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("hex", ColorUtil.rgbToHex(rgb[0], rgb[1], rgb[2]));
        data.put("rgb", Map.of("r", rgb[0], "g", rgb[1], "b", rgb[2]));
        data.put("dark", ColorUtil.isDark(hex));
        data.put("luminance", ColorUtil.luminance(hex));
        return Result.ok(data);
    }

    @GetMapping("/zodiac")
    @Operation(summary = "星座 / 生肖")
    public Result<Map<String, String>> zodiac(@RequestParam(defaultValue = "1990-03-07") String date) {
        LocalDate parsed = LocalDate.parse(date);
        Map<String, String> data = new LinkedHashMap<>();
        data.put("constellation", ZodiacUtil.constellation(parsed));
        data.put("chineseZodiac", ZodiacUtil.chineseZodiac(parsed));
        return Result.ok(data);
    }

    @GetMapping("/map-path")
    @Operation(summary = "点路径取值 / 扁平化")
    public Result<Map<String, Object>> mapPath() {
        Map<String, Object> nested = Map.of("user", Map.of("name", "Ada", "city", "Shanghai"));
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", MapPathUtil.getStr(nested, "user.name"));
        data.put("flat", MapPathUtil.flatten(nested));
        return Result.ok(data);
    }

    @GetMapping("/file-type")
    @Operation(summary = "文件类型 / 文件名清洗")
    public Result<Map<String, String>> fileType(
            @RequestParam(defaultValue = "../../a.png") String filename,
            @RequestParam(defaultValue = "89504e47") String hex) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("sanitized", FileUtil.sanitize(filename));
        data.put("byName", FileTypeUtil.ofFilename(filename));
        data.put("byMagic", FileTypeUtil.ofHex(hex));
        return Result.ok(data);
    }

    @GetMapping("/hash/murmur")
    @Operation(summary = "Murmur3-32")
    public Result<Map<String, Object>> murmur(@RequestParam(defaultValue = "hello") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("murmur32", HashUtil.murmur32(text));
        data.put("hex", HashUtil.murmur32Hex(text));
        return Result.ok(data);
    }

    @GetMapping("/escape")
    @Operation(summary = "JS / CSV 转义")
    public Result<Map<String, String>> escape(@RequestParam(defaultValue = "a\"b'c") String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("js", EscapeUtil.js(text));
        data.put("csv", EscapeUtil.csv(text));
        data.put("json", EscapeUtil.json(text));
        return Result.ok(data);
    }

    @GetMapping("/highlight")
    @Operation(summary = "关键字高亮")
    public Result<Map<String, String>> highlight(
            @RequestParam(defaultValue = "Spring Boot 工具集") String text,
            @RequestParam(defaultValue = "工具") String keyword) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("html", HighlightUtil.html(text, keyword));
        return Result.ok(data);
    }

    @GetMapping("/weight-random")
    @Operation(summary = "加权随机")
    public Result<Map<String, Object>> weight() {
        Map<String, Integer> weights = Map.of("A", 1, "B", 3, "C", 6);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("picked", WeightRandomUtil.pick(weights));
        data.put("weights", weights);
        return Result.ok(data);
    }

    @GetMapping("/duration")
    @Operation(summary = "时长解析")
    public Result<Map<String, Object>> duration(@RequestParam(defaultValue = "1h30m") String text) {
        var parsed = DurationUtil.parse(text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("iso", parsed.toString());
        data.put("millis", parsed.toMillis());
        data.put("formatted", DurationUtil.format(parsed));
        return Result.ok(data);
    }

    @GetMapping("/slug")
    @Operation(summary = "URL slug")
    public Result<Map<String, String>> slug(@RequestParam(defaultValue = "Spring Boot 工具集") String text) {
        return Result.ok(Map.of("slug", SlugUtil.of(text)));
    }

    @GetMapping("/verify-code")
    @Operation(summary = "验证码")
    public Result<Map<String, Object>> verifyCode(@RequestParam(defaultValue = "6") int length) {
        String numeric = VerifyCodeUtil.numeric(length);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("numeric", numeric);
        data.put("alphanumeric", VerifyCodeUtil.alphanumeric(length));
        data.put("matchesSelf", VerifyCodeUtil.matches(numeric, numeric));
        return Result.ok(data);
    }

    @GetMapping("/percent")
    @Operation(summary = "百分比")
    public Result<Map<String, Object>> percent(
            @RequestParam(defaultValue = "25") String part,
            @RequestParam(defaultValue = "200") String total) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("percent", PercentUtil.of(part, total));
        data.put("formatted", PercentUtil.format(PercentUtil.of(part, total)));
        data.put("applied", PercentUtil.apply(total, PercentUtil.of(part, total)));
        data.put("change", PercentUtil.change(100, 125));
        return Result.ok(data);
    }

    @GetMapping("/circuit")
    @Operation(summary = "熔断试探")
    public Result<Map<String, Object>> circuit(@RequestParam(defaultValue = "demo") String name) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("allowed", CircuitBreakerUtil.allow(name));
        data.put("state", CircuitBreakerUtil.state(name).name());
        return Result.ok(data);
    }
}
