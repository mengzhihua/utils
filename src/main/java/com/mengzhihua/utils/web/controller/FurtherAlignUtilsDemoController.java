package com.mengzhihua.utils.web.controller;


import java.security.KeyPair;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.Ripemd160Util;
import com.mengzhihua.utils.common.crypto.X25519Util;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.FigiUtil;
import com.mengzhihua.utils.common.validate.LeiUtil;
import com.mengzhihua.utils.common.validate.NhsNumberUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Further Align Demo", description = "X25519 / RIPEMD-160 / FIGI / LEI / NHS")
public class FurtherAlignUtilsDemoController {

    @GetMapping("/x25519")
    @Operation(summary = "X25519 RFC 7748")
    public Result<Map<String, Object>> x25519() {
        String alice = "77076d0a7318a57d3c16c17251b26645df4c2f87ebc0992ab177fba51db92c2a";
        String bobPub = "de9edb7d7b7dc1b4d35b61c2ece435373f8343c85b78674dadfc7e146f882b4f";
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("shared", X25519Util.sharedSecretHex(alice, bobPub));
        KeyPair pair = X25519Util.generate();
        data.put("public", HexFormat.of().formatHex(pair.getPublic().getEncoded()));
        return Result.ok(data);
    }

    @GetMapping("/ripemd160")
    @Operation(summary = "RIPEMD-160")
    public Result<Map<String, Object>> ripemd160(@RequestParam(defaultValue = "abc") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ripemd160", Ripemd160Util.hash(text));
        return Result.ok(data);
    }

    @GetMapping("/figi")
    @Operation(summary = "FIGI")
    public Result<Map<String, Object>> figi(@RequestParam(defaultValue = "BBG000B9XRY4") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", FigiUtil.normalize(value));
        data.put("valid", FigiUtil.isValid(value));
        data.put("regex", RegexUtil.is("figi", value));
        return Result.ok(data);
    }

    @GetMapping("/lei")
    @Operation(summary = "LEI ISO 17442")
    public Result<Map<String, Object>> lei(@RequestParam(defaultValue = "5493001KJTIIGC8Y1R12") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", LeiUtil.normalize(value));
        data.put("valid", LeiUtil.isValid(value));
        data.put("regex", RegexUtil.is("lei", value));
        return Result.ok(data);
    }

    @GetMapping("/nhs")
    @Operation(summary = "NHS number")
    public Result<Map<String, Object>> nhs(@RequestParam(defaultValue = "943 476 5919") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NhsNumberUtil.normalize(value));
        data.put("valid", NhsNumberUtil.isValid(value));
        data.put("regex", RegexUtil.is("nhs", value));
        return Result.ok(data);
    }
}
