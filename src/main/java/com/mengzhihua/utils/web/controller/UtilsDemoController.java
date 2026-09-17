package com.mengzhihua.utils.web.controller;


import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import com.mengzhihua.utils.common.api.PageResult;
import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.EncryptUtil;
import com.mengzhihua.utils.common.extra.TreeNode;
import com.mengzhihua.utils.common.extra.TreeUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.json.JsonUtil;
import com.mengzhihua.utils.common.lang.CollectionUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import com.mengzhihua.utils.common.math.NumberUtil;
import com.mengzhihua.utils.common.net.IpUtil;
import com.mengzhihua.utils.common.text.ReUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.time.DateTimeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/utils")
@Tag(name = "Utils Demo", description = "通用工具演示接口")
public class UtilsDemoController {

    @GetMapping("/string/mask-phone")
    @Operation(summary = "手机号脱敏")
    public Result<String> maskPhone(@RequestParam String phone) {
        return Result.ok(StringUtil.maskPhone(phone));
    }

    @GetMapping("/string/case")
    @Operation(summary = "驼峰 / 下划线互转")
    public Result<Map<String, String>> convertCase(@RequestParam String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("camelToSnake", StringUtil.camelToSnake(text));
        data.put("snakeToCamel", StringUtil.snakeToCamel(text));
        return Result.ok(data);
    }

    @GetMapping("/datetime/now")
    @Operation(summary = "当前时间")
    public Result<Map<String, Object>> now() {
        LocalDateTime now = DateTimeUtil.now();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("dateTime", DateTimeUtil.format(now));
        data.put("date", DateTimeUtil.nowDate());
        data.put("epochMilli", DateTimeUtil.toEpochMilli(now));
        return Result.ok(data);
    }

    @GetMapping("/id/uuid")
    @Operation(summary = "生成 UUID")
    public Result<Map<String, String>> uuid() {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("uuid", IdUtil.uuid());
        data.put("simpleUuid", IdUtil.simpleUuid());
        data.put("nanoId", IdUtil.nanoId(16));
        data.put("uuidV7", IdUtil.uuidV7());
        return Result.ok(data);
    }

    @GetMapping("/id/snowflake")
    @Operation(summary = "生成雪花 ID")
    public Result<Map<String, Object>> snowflake() {
        long id = IdUtil.snowflakeId();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", id);
        data.put("idStr", Long.toString(id));
        data.put("parsed", IdUtil.parseSnowflake(id));
        return Result.ok(data);
    }

    @GetMapping("/encrypt/digest")
    @Operation(summary = "MD5 / SHA-256")
    public Result<Map<String, String>> digest(@RequestParam String text) {
        Map<String, String> data = new LinkedHashMap<>();
        data.put("md5", EncryptUtil.md5(text));
        data.put("sha256", EncryptUtil.sha256(text));
        data.put("sha3_256", EncryptUtil.sha3_256(text));
        data.put("sha512", EncryptUtil.sha512(text));
        return Result.ok(data);
    }

    @PostMapping("/encrypt/aes")
    @Operation(summary = "AES-GCM 加密解密")
    public Result<Map<String, String>> aes(@Valid @RequestBody AesRequest request) {
        String cipher = EncryptUtil.aesEncrypt(request.text(), request.password());
        Map<String, String> data = new LinkedHashMap<>();
        data.put("cipherText", cipher);
        data.put("plainText", EncryptUtil.aesDecrypt(cipher, request.password()));
        return Result.ok(data);
    }

    @PostMapping("/json/parse")
    @Operation(summary = "解析 JSON 字符串")
    public Result<Map<String, Object>> parseJson(@Valid @RequestBody JsonRequest request) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("valid", JsonUtil.isJson(request.json()));
        data.put("map", JsonUtil.toMap(request.json()));
        data.put("pretty", JsonUtil.toPrettyJson(request.json()));
        return Result.ok(data);
    }

    @GetMapping("/regex/validate")
    @Operation(summary = "常用格式校验")
    public Result<Map<String, Object>> validate(
            @RequestParam String value,
            @RequestParam String type) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("matched", RegexUtil.is(type, value));
        data.put("type", RegexUtil.normalizeType(type));
        return Result.ok(data);
    }

    @GetMapping("/regex/extract")
    @Operation(summary = "从文本抽取手机号 / 邮箱 / URL / IP / 日期 / 色值")
    public Result<Map<String, Object>> extractRegex(
            @RequestParam(defaultValue = "联系 Ada ada@example.com 电话 13812345678 打开 https://example.com 颜色 #0F766E 日期 2026-09-17") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("mobiles", ReUtil.extractMobiles(text));
        data.put("emails", ReUtil.extractEmails(text));
        data.put("urls", ReUtil.extractUrls(text));
        data.put("ipv4", ReUtil.extractIpv4(text));
        data.put("dates", ReUtil.extractDates(text));
        data.put("hexColors", ReUtil.extractHexColors(text));
        data.put("idCards", ReUtil.extractIdCards(text));
        return Result.ok(data);
    }

    @GetMapping("/regex/types")
    @Operation(summary = "可用正则校验类型")
    public Result<Map<String, Object>> regexTypes() {
        return Result.ok(Map.of("types", RegexUtil.types()));
    }

    @GetMapping("/number/money")
    @Operation(summary = "金额格式化")
    public Result<String> money(@RequestParam String amount) {
        return Result.ok(NumberUtil.formatMoney(amount));
    }

    @GetMapping("/tree/sample")
    @Operation(summary = "树结构构建示例")
    public Result<List<TreeNode<Long>>> tree() {
        List<TreeNode<Long>> nodes = List.of(
                new TreeNode<>(1L, 0L, "总部"),
                new TreeNode<>(2L, 1L, "研发中心"),
                new TreeNode<>(3L, 1L, "产品中心"),
                new TreeNode<>(4L, 2L, "后端组"),
                new TreeNode<>(5L, 2L, "前端组")
        );
        return Result.ok(TreeUtil.build(nodes, 0L));
    }

    @GetMapping("/page/sample")
    @Operation(summary = "内存分页示例")
    public Result<PageResult<Integer>> page(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        List<Integer> all = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        return Result.ok(PageResult.of(page, size, all.size(), CollectionUtil.page(all, page, size)));
    }

    @GetMapping("/ip")
    @Operation(summary = "获取客户端 IP")
    public Result<Map<String, Object>> ip(HttpServletRequest request) {
        String ip = IpUtil.getClientIp(request);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ip", ip);
        data.put("internal", IpUtil.isInternalIp(ip));
        return Result.ok(data);
    }

    public record AesRequest(
            @NotBlank(message = "must not be blank") String text,
            @NotBlank(message = "must not be blank") String password) {
    }

    public record JsonRequest(
            @NotBlank(message = "must not be blank") String json) {
    }
}
