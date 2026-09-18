package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.net.CorsUtil;
import com.mengzhihua.utils.common.net.WwwAuthenticateUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CuitUtil;
import com.mengzhihua.utils.common.validate.IrdUtil;
import com.mengzhihua.utils.common.validate.MyKadUtil;
import com.mengzhihua.utils.common.validate.RutUtil;
import com.mengzhihua.utils.common.validate.SaIdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Americas Align Demo", description = "RUT / CUIT / SA ID / IRD / MyKad / CORS")
public class AmericasAlignUtilsDemoController {

    @GetMapping("/rut")
    @Operation(summary = "智利 RUT")
    public Result<Map<String, Object>> rut(@RequestParam(defaultValue = "12.345.678-5") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", RutUtil.normalize(value));
        data.put("valid", RutUtil.isValid(value));
        data.put("regex", RegexUtil.is("rut", value));
        return Result.ok(data);
    }

    @GetMapping("/cuit")
    @Operation(summary = "阿根廷 CUIT")
    public Result<Map<String, Object>> cuit(@RequestParam(defaultValue = "20-12345678-6") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CuitUtil.normalize(value));
        data.put("valid", CuitUtil.isValid(value));
        data.put("regex", RegexUtil.is("cuit", value));
        return Result.ok(data);
    }

    @GetMapping("/sa-id")
    @Operation(summary = "南非身份证")
    public Result<Map<String, Object>> saId(@RequestParam(defaultValue = "8001015009087") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SaIdUtil.normalize(value));
        data.put("valid", SaIdUtil.isValid(value));
        if (SaIdUtil.isValid(value)) {
            data.put("birthDate", SaIdUtil.birthDate(value).toString());
            data.put("female", SaIdUtil.female(value));
            data.put("citizen", SaIdUtil.citizen(value));
        }
        data.put("regex", RegexUtil.is("said", value));
        return Result.ok(data);
    }

    @GetMapping("/ird")
    @Operation(summary = "新西兰 IRD")
    public Result<Map<String, Object>> ird(@RequestParam(defaultValue = "49091850") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IrdUtil.normalize(value));
        data.put("valid", IrdUtil.isValid(value));
        data.put("regex", RegexUtil.is("ird", value));
        return Result.ok(data);
    }

    @GetMapping("/mykad")
    @Operation(summary = "马来西亚 MyKad")
    public Result<Map<String, Object>> mykad(@RequestParam(defaultValue = "900101-14-5671") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MyKadUtil.normalize(value));
        data.put("valid", MyKadUtil.isValid(value));
        if (MyKadUtil.isValid(value)) {
            data.put("birthDate", MyKadUtil.birthDate(value).toString());
            data.put("female", MyKadUtil.female(value));
            data.put("placeCode", MyKadUtil.placeCode(value));
        }
        data.put("regex", RegexUtil.is("mykad", value));
        return Result.ok(data);
    }

    @GetMapping("/cors")
    @Operation(summary = "CORS Access-Control")
    public Result<Map<String, Object>> cors(
            @RequestParam(defaultValue = "https://example.com") String allowOrigin,
            @RequestParam(defaultValue = "https://example.com") String origin,
            @RequestParam(defaultValue = "GET, POST") String allowMethods,
            @RequestParam(defaultValue = "Content-Type") String allowHeaders) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("allowsOrigin", CorsUtil.allowsOrigin(allowOrigin, origin));
        data.put("allowsMethod", CorsUtil.allowsMethod(allowMethods, "GET"));
        data.put("allowsHeader", CorsUtil.allowsHeader(allowHeaders, "content-type"));
        data.put("methods", CorsUtil.methods(allowMethods));
        return Result.ok(data);
    }

    @GetMapping("/www-authenticate")
    @Operation(summary = "HTTP WWW-Authenticate RFC 9110")
    public Result<Map<String, Object>> wwwAuthenticate(
            @RequestParam(defaultValue = "Bearer realm=\"api\", error=\"invalid_token\"") String header) {
        WwwAuthenticateUtil.Challenge challenge = WwwAuthenticateUtil.parse(header);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("scheme", challenge.scheme());
        data.put("realm", challenge.realm());
        data.put("params", challenge.params());
        return Result.ok(data);
    }
}
