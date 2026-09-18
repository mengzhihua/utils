package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.PermissionsPolicyUtil;
import com.mengzhihua.utils.common.net.ReferrerPolicyUtil;
import com.mengzhihua.utils.common.net.XFrameOptionsUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.IpnUtil;
import com.mengzhihua.utils.common.validate.IsikukoodUtil;
import com.mengzhihua.utils.common.validate.JmbgUtil;
import com.mengzhihua.utils.common.validate.KennitalaUtil;
import com.mengzhihua.utils.common.validate.NitUtil;
import com.mengzhihua.utils.common.validate.TajUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Baltics Align Demo", description = "JMBG / isikukood / kennitala / TAJ / IPN / NIT")
public class BalticsAlignUtilsDemoController {

    @GetMapping("/jmbg")
    @Operation(summary = "南斯拉夫 JMBG")
    public Result<Map<String, Object>> jmbg(@RequestParam(defaultValue = "0101980500005") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", JmbgUtil.normalize(value));
        data.put("valid", JmbgUtil.isValid(value));
        if (JmbgUtil.isValid(value)) {
            data.put("birthDate", JmbgUtil.birthDate(value).toString());
            data.put("female", JmbgUtil.female(value));
        }
        data.put("regex", RegexUtil.is("jmbg", value));
        return Result.ok(data);
    }

    @GetMapping("/isikukood")
    @Operation(summary = "爱沙尼亚个人号")
    public Result<Map<String, Object>> isikukood(@RequestParam(defaultValue = "37601010003") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IsikukoodUtil.normalize(value));
        data.put("valid", IsikukoodUtil.isValid(value));
        if (IsikukoodUtil.isValid(value)) {
            data.put("birthDate", IsikukoodUtil.birthDate(value).toString());
            data.put("female", IsikukoodUtil.female(value));
        }
        data.put("regex", RegexUtil.is("isikukood", value));
        return Result.ok(data);
    }

    @GetMapping("/kennitala")
    @Operation(summary = "冰岛个人号")
    public Result<Map<String, Object>> kennitala(@RequestParam(defaultValue = "120174-3399") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", KennitalaUtil.normalize(value));
        data.put("valid", KennitalaUtil.isValid(value));
        if (KennitalaUtil.isValid(value)) {
            data.put("birthDate", KennitalaUtil.birthDate(value).toString());
        }
        data.put("regex", RegexUtil.is("kennitala", value));
        return Result.ok(data);
    }

    @GetMapping("/taj")
    @Operation(summary = "匈牙利 TAJ")
    public Result<Map<String, Object>> taj(@RequestParam(defaultValue = "123456788") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", TajUtil.normalize(value));
        data.put("valid", TajUtil.isValid(value));
        data.put("regex", RegexUtil.is("taj", value));
        return Result.ok(data);
    }

    @GetMapping("/ipn")
    @Operation(summary = "乌克兰 IPN")
    public Result<Map<String, Object>> ipn(@RequestParam(defaultValue = "2922000110") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IpnUtil.normalize(value));
        data.put("valid", IpnUtil.isValid(value));
        if (IpnUtil.isValid(value)) {
            data.put("birthDate", IpnUtil.birthDate(value).toString());
            data.put("female", IpnUtil.female(value));
        }
        data.put("regex", RegexUtil.is("ipn", value));
        return Result.ok(data);
    }

    @GetMapping("/nit")
    @Operation(summary = "哥伦比亚 NIT")
    public Result<Map<String, Object>> nit(@RequestParam(defaultValue = "800197268-4") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NitUtil.normalize(value));
        data.put("valid", NitUtil.isValid(value));
        data.put("regex", RegexUtil.is("nit", value));
        return Result.ok(data);
    }

    @GetMapping("/referrer-policy")
    @Operation(summary = "HTTP Referrer-Policy")
    public Result<Map<String, Object>> referrerPolicy(
            @RequestParam(defaultValue = "strict-origin-when-cross-origin") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("policies", ReferrerPolicyUtil.parse(header));
        data.put("known", ReferrerPolicyUtil.known(header.contains(",") ? "strict-origin-when-cross-origin" : header.trim()));
        data.put("hasStrictOriginWhenCrossOrigin", ReferrerPolicyUtil.has(header, "strict-origin-when-cross-origin"));
        return Result.ok(data);
    }

    @GetMapping("/x-frame-options")
    @Operation(summary = "HTTP X-Frame-Options")
    public Result<Map<String, Object>> xFrameOptions(@RequestParam(defaultValue = "SAMEORIGIN") String header) {
        XFrameOptionsUtil.FrameOptions options = XFrameOptionsUtil.parse(header);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("directive", options.directive());
        data.put("sameOrigin", options.sameOrigin());
        data.put("deny", options.deny());
        data.put("allowFrom", options.allowFrom());
        return Result.ok(data);
    }

    @GetMapping("/permissions-policy")
    @Operation(summary = "HTTP Permissions-Policy")
    public Result<Map<String, Object>> permissionsPolicy(
            @RequestParam(defaultValue = "geolocation=(), camera=(self)") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("directives", PermissionsPolicyUtil.parse(header));
        data.put("geolocationDisabled", PermissionsPolicyUtil.disabled(header, "geolocation"));
        data.put("camera", PermissionsPolicyUtil.allowlist(header, "camera"));
        return Result.ok(data);
    }

    @GetMapping("/crc16-arc")
    @Operation(summary = "CRC-16/ARC")
    public Result<Map<String, Object>> crc16Arc(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Arc", HashUtil.crc16ArcHex(text));
        return Result.ok(data);
    }
}
