package com.mengzhihua.utils.web;

import com.mengzhihua.utils.common.api.PageResult;
import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.util.CollectionUtil;
import com.mengzhihua.utils.util.DateTimeUtil;
import com.mengzhihua.utils.util.EncryptUtil;
import com.mengzhihua.utils.util.IdUtil;
import com.mengzhihua.utils.util.IpUtil;
import com.mengzhihua.utils.util.JsonUtil;
import com.mengzhihua.utils.util.NumberUtil;
import com.mengzhihua.utils.util.RegexUtil;
import com.mengzhihua.utils.util.StringUtil;
import com.mengzhihua.utils.util.TreeNode;
import com.mengzhihua.utils.util.TreeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    public Result<Map<String, Boolean>> validate(
            @RequestParam String value,
            @RequestParam String type) {
        boolean matched = switch (type.toLowerCase()) {
            case "mobile" -> RegexUtil.isMobile(value);
            case "email" -> RegexUtil.isEmail(value);
            case "idcard" -> RegexUtil.isIdCard(value);
            case "ipv4" -> RegexUtil.isIpv4(value);
            case "url" -> RegexUtil.isUrl(value);
            case "username" -> RegexUtil.isUsername(value);
            case "credit" -> RegexUtil.isCreditCode(value);
            case "plate" -> RegexUtil.isPlate(value);
            case "ipv6" -> RegexUtil.isIpv6(value);
            case "zipcode" -> RegexUtil.isZipcode(value);
            default -> throw new IllegalArgumentException("unsupported type: " + type);
        };
        return Result.ok(Map.of("matched", matched));
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
