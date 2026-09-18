package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.HttpAgeUtil;
import com.mengzhihua.utils.common.net.WarningUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EmsoUtil;
import com.mengzhihua.utils.common.validate.LatvianPkUtil;
import com.mengzhihua.utils.common.validate.LithuanianAkUtil;
import com.mengzhihua.utils.common.validate.MxRfcUtil;
import com.mengzhihua.utils.common.validate.PeDniUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Adriatic Align Demo", description = "personas kods / asmens kodas / EMŠO / DNI / RFC")
public class AdriaticAlignUtilsDemoController {

    @GetMapping("/lv-pk")
    @Operation(summary = "拉脱维亚个人号")
    public Result<Map<String, Object>> lvPk(@RequestParam(defaultValue = "111111-11111") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", LatvianPkUtil.normalize(value));
        data.put("valid", LatvianPkUtil.isValid(value));
        if (LatvianPkUtil.isValid(value) && !LatvianPkUtil.modern(value)) {
            data.put("birthDate", LatvianPkUtil.birthDate(value).toString());
        }
        data.put("modern", LatvianPkUtil.modern(value));
        data.put("regex", RegexUtil.is("lvpk", value));
        return Result.ok(data);
    }

    @GetMapping("/lt-ak")
    @Operation(summary = "立陶宛个人号")
    public Result<Map<String, Object>> ltAk(@RequestParam(defaultValue = "33309240064") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", LithuanianAkUtil.normalize(value));
        data.put("valid", LithuanianAkUtil.isValid(value));
        if (LithuanianAkUtil.isValid(value)) {
            data.put("birthDate", LithuanianAkUtil.birthDate(value).toString());
            data.put("female", LithuanianAkUtil.female(value));
        }
        data.put("regex", RegexUtil.is("ltak", value));
        return Result.ok(data);
    }

    @GetMapping("/emso")
    @Operation(summary = "斯洛文尼亚 EMŠO")
    public Result<Map<String, Object>> emso(@RequestParam(defaultValue = "0101006500006") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EmsoUtil.normalize(value));
        data.put("valid", EmsoUtil.isValid(value));
        if (EmsoUtil.isValid(value)) {
            data.put("birthDate", EmsoUtil.birthDate(value).toString());
            data.put("female", EmsoUtil.female(value));
        }
        data.put("regex", RegexUtil.is("emso", value));
        return Result.ok(data);
    }

    @GetMapping("/pe-dni")
    @Operation(summary = "秘鲁 DNI")
    public Result<Map<String, Object>> peDni(@RequestParam(defaultValue = "713903006") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PeDniUtil.normalize(value));
        data.put("valid", PeDniUtil.isValid(value));
        data.put("regex", RegexUtil.is("pedni", value));
        return Result.ok(data);
    }

    @GetMapping("/mx-rfc")
    @Operation(summary = "墨西哥 RFC")
    public Result<Map<String, Object>> mxRfc(@RequestParam(defaultValue = "GODE561231GR8") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MxRfcUtil.normalize(value));
        data.put("valid", MxRfcUtil.isValid(value));
        data.put("regex", RegexUtil.is("mxrfc", value));
        return Result.ok(data);
    }

    @GetMapping("/http-age")
    @Operation(summary = "HTTP Age")
    public Result<Map<String, Object>> httpAge(@RequestParam(defaultValue = "3600") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("seconds", HttpAgeUtil.parse(header));
        data.put("valid", HttpAgeUtil.valid(header));
        data.put("freshUnderHour", HttpAgeUtil.fresh(header, 3600));
        return Result.ok(data);
    }

    @GetMapping("/warning")
    @Operation(summary = "HTTP Warning")
    public Result<Map<String, Object>> warning(
            @RequestParam(defaultValue = "110 - \"Response is Stale\"") String header) {
        WarningUtil.WarningValue value = WarningUtil.parse(header);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", value.code());
        data.put("agent", value.agent());
        data.put("text", value.text());
        data.put("stale", value.stale());
        data.put("known", WarningUtil.known(header));
        data.put("codeName", WarningUtil.codeName(value.code()));
        return Result.ok(data);
    }

    @GetMapping("/crc16-maxim")
    @Operation(summary = "CRC-16/MAXIM")
    public Result<Map<String, Object>> crc16Maxim(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Maxim", HashUtil.crc16MaximHex(text));
        return Result.ok(data);
    }
}
