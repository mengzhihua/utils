package com.mengzhihua.utils.web.controller;


import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Bech32Util;
import com.mengzhihua.utils.common.concurrent.JumpHashUtil;
import com.mengzhihua.utils.common.crypto.AesKwUtil;
import com.mengzhihua.utils.common.crypto.Blake2bUtil;
import com.mengzhihua.utils.common.crypto.Ed25519Util;
import com.mengzhihua.utils.common.id.TsidUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.time.GanZhiUtil;
import com.mengzhihua.utils.common.validate.AbaRoutingUtil;
import com.mengzhihua.utils.common.validate.Iso6346Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Next Align Demo", description = "BLAKE2b / AES-KW / Ed25519 / TSID / 干支 / Jump Hash")
public class NextAlignUtilsDemoController {

    @GetMapping("/blake2b")
    @Operation(summary = "BLAKE2b-512")
    public Result<Map<String, Object>> blake2b(@RequestParam(defaultValue = "abc") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("blake2b", Blake2bUtil.hash(text));
        return Result.ok(data);
    }

    @GetMapping("/aes-kw")
    @Operation(summary = "AES Key Wrap RFC 3394")
    public Result<Map<String, Object>> aesKw() {
        Map<String, Object> data = new LinkedHashMap<>();
        String wrapped = AesKwUtil.wrapHex("000102030405060708090A0B0C0D0E0F", "00112233445566778899AABBCCDDEEFF");
        data.put("wrapped", wrapped);
        data.put("unwrapped", AesKwUtil.unwrapHex("000102030405060708090A0B0C0D0E0F", wrapped));
        return Result.ok(data);
    }

    @GetMapping("/ed25519")
    @Operation(summary = "Ed25519 签名")
    public Result<Map<String, Object>> ed25519(@RequestParam(defaultValue = "hello") String text) {
        KeyPair pair = Ed25519Util.generate();
        byte[] message = text.getBytes(StandardCharsets.UTF_8);
        try {
            java.security.Signature signer = java.security.Signature.getInstance("Ed25519");
            signer.initSign(pair.getPrivate());
            signer.update(message);
            byte[] signature = signer.sign();
            java.security.Signature verifier = java.security.Signature.getInstance("Ed25519");
            verifier.initVerify(pair.getPublic());
            verifier.update(message);
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("public", HexFormat.of().formatHex(pair.getPublic().getEncoded()));
            data.put("signature", HexFormat.of().formatHex(signature));
            data.put("verified", verifier.verify(signature));
            return Result.ok(data);
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }

    @GetMapping("/tsid")
    @Operation(summary = "Time-Sorted ID")
    public Result<Map<String, Object>> tsid() {
        long id = TsidUtil.next();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", Long.toUnsignedString(id));
        data.put("encoded", TsidUtil.encode(id));
        data.put("unixMillis", TsidUtil.unixMillis(id));
        data.put("node", TsidUtil.node(id));
        data.put("sequence", TsidUtil.sequence(id));
        return Result.ok(data);
    }

    @GetMapping("/ganzhi")
    @Operation(summary = "天干地支")
    public Result<Map<String, Object>> ganzhi(@RequestParam(defaultValue = "2026") int year) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ganzhi", GanZhiUtil.year(year));
        data.put("animal", GanZhiUtil.animal(year));
        data.put("formatted", GanZhiUtil.format(year));
        return Result.ok(data);
    }

    @GetMapping("/jump-hash")
    @Operation(summary = "Jump consistent hash")
    public Result<Map<String, Object>> jumpHash(
            @RequestParam(defaultValue = "42") long key,
            @RequestParam(defaultValue = "100") int buckets) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("bucket", JumpHashUtil.jump(key, buckets));
        data.put("buckets", buckets);
        return Result.ok(data);
    }

    @GetMapping("/iso6346")
    @Operation(summary = "集装箱号 ISO 6346")
    public Result<Map<String, Object>> iso6346(@RequestParam(defaultValue = "CSQU3054383") String code) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", Iso6346Util.normalize(code));
        data.put("valid", Iso6346Util.isValid(code));
        data.put("regex", RegexUtil.is("iso6346", code));
        return Result.ok(data);
    }

    @GetMapping("/bech32m")
    @Operation(summary = "Bech32m BIP-350")
    public Result<Map<String, Object>> bech32m(@RequestParam(defaultValue = "hello") String text) {
        String encoded = Bech32Util.encodeM("demo", text.getBytes());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("hrp", Bech32Util.decodeM(encoded).hrp());
        data.put("empty", Bech32Util.encodeM("a", new byte[0]));
        return Result.ok(data);
    }

    @GetMapping("/aba")
    @Operation(summary = "ABA routing number")
    public Result<Map<String, Object>> aba(@RequestParam(defaultValue = "021000021") String number) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AbaRoutingUtil.normalize(number));
        data.put("valid", AbaRoutingUtil.isValid(number));
        data.put("regex", RegexUtil.is("aba", number));
        return Result.ok(data);
    }
}
