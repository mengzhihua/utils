package com.mengzhihua.utils.web.controller;


import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.JwtUtil;
import com.mengzhihua.utils.common.crypto.PasswordUtil;
import com.mengzhihua.utils.common.extra.GeoUtil;
import com.mengzhihua.utils.common.extra.SystemUtil;
import com.mengzhihua.utils.common.extra.TraceIdUtil;
import com.mengzhihua.utils.common.id.OrderNoUtil;
import com.mengzhihua.utils.common.math.ChineseNumberUtil;
import com.mengzhihua.utils.common.math.VersionUtil;
import com.mengzhihua.utils.common.net.UserAgentUtil;
import com.mengzhihua.utils.common.text.DesensitizeUtil;
import com.mengzhihua.utils.common.text.HtmlUtil;
import com.mengzhihua.utils.common.validate.IdCardUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Extra Utils Demo", description = "补充常用工具演示接口")
public class ExtraUtilsDemoController {

    @GetMapping("/idcard/parse")
    @Operation(summary = "身份证解析（校验码 / 生日 / 性别）")
    public Result<Map<String, Object>> parseIdCard(@RequestParam String idNo) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", IdCardUtil.isValid(idNo));
        data.put("birthday", IdCardUtil.getBirthday(idNo));
        data.put("gender", IdCardUtil.getGender(idNo));
        data.put("age", IdCardUtil.getAge(idNo));
        data.put("province", IdCardUtil.getProvince(idNo));
        data.put("masked", DesensitizeUtil.idCard(idNo));
        return Result.ok(data);
    }

    @GetMapping("/desensitize")
    @Operation(summary = "常用脱敏")
    public Result<Map<String, String>> desensitize(
            @RequestParam String type,
            @RequestParam String value) {
        String masked = switch (type.toLowerCase()) {
            case "name" -> DesensitizeUtil.chineseName(value);
            case "phone" -> DesensitizeUtil.phone(value);
            case "email" -> DesensitizeUtil.email(value);
            case "idcard" -> DesensitizeUtil.idCard(value);
            case "bank" -> DesensitizeUtil.bankCard(value);
            case "address" -> DesensitizeUtil.address(value);
            case "ip" -> DesensitizeUtil.ipv4(value);
            case "plate" -> DesensitizeUtil.plate(value);
            case "password" -> DesensitizeUtil.password(value);
            default -> throw new IllegalArgumentException("unsupported type: " + type);
        };
        return Result.ok(Map.of("masked", masked));
    }

    @GetMapping("/jwt")
    @Operation(summary = "HS256 JWT 签发与解析")
    public Result<Map<String, Object>> jwt(@RequestParam(defaultValue = "demo-user") String subject) {
        String token = JwtUtil.create(Map.of("sub", subject), "demo-secret", Duration.ofMinutes(30));
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("token", token);
        data.put("payload", JwtUtil.parse(token, "demo-secret"));
        data.put("remainingSeconds", JwtUtil.remainingSeconds(token, "demo-secret"));
        data.put("decoded", JwtUtil.decode(token));
        return Result.ok(data);
    }

    @GetMapping("/order-no")
    @Operation(summary = "业务单号")
    public Result<Map<String, String>> orderNo() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("orderNo", OrderNoUtil.next("SO"));
        data.put("tradeNo", OrderNoUtil.nextTradeNo());
        return Result.ok(data);
    }

    @GetMapping("/geo/distance")
    @Operation(summary = "经纬度距离（米）")
    public Result<Map<String, Object>> distance(
            @RequestParam double lat1,
            @RequestParam double lon1,
            @RequestParam double lat2,
            @RequestParam double lon2) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("meters", GeoUtil.distanceMeters(lat1, lon1, lat2, lon2));
        data.put("km", GeoUtil.distanceKm(lat1, lon1, lat2, lon2));
        return Result.ok(data);
    }

    @GetMapping("/html/escape")
    @Operation(summary = "HTML 转义")
    public Result<Map<String, String>> html(@RequestParam String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("escaped", HtmlUtil.escape(text));
        data.put("unescaped", HtmlUtil.unescape(HtmlUtil.escape(text)));
        data.put("stripped", HtmlUtil.stripTags(text));
        return Result.ok(data);
    }

    @GetMapping("/version/compare")
    @Operation(summary = "版本号比较")
    public Result<Map<String, Object>> version(@RequestParam String left, @RequestParam String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("compare", VersionUtil.compare(left, right));
        data.put("leftGreater", VersionUtil.isGreater(left, right));
        return Result.ok(data);
    }

    @GetMapping("/password/strength")
    @Operation(summary = "密码强度")
    public Result<Map<String, Object>> password(@RequestParam String password) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("score", PasswordUtil.score(password));
        data.put("level", PasswordUtil.level(password));
        data.put("strong", PasswordUtil.isStrong(password));
        data.put("generated", PasswordUtil.generate(16));
        return Result.ok(data);
    }

    @GetMapping("/chinese/number")
    @Operation(summary = "中文数字 / 人民币大写")
    public Result<Map<String, String>> chinese(@RequestParam String amount) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("chinese", ChineseNumberUtil.toChinese(Long.parseLong(amount.contains(".") ? amount.substring(0, amount.indexOf('.')) : amount)));
        data.put("rmb", ChineseNumberUtil.toRmb(amount));
        return Result.ok(data);
    }

    @GetMapping("/system")
    @Operation(summary = "运行环境信息")
    public Result<Map<String, Object>> system(HttpServletRequest request) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("os", SystemUtil.osName());
        data.put("java", SystemUtil.javaVersion());
        data.put("pid", SystemUtil.pid());
        data.put("host", SystemUtil.hostName());
        data.put("traceId", TraceIdUtil.get());
        data.put("mobileUa", UserAgentUtil.isMobile(request.getHeader("User-Agent")));
        return Result.ok(data);
    }
}
