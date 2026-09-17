package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ForwardedUtil;
import com.mengzhihua.utils.common.text.PorterStemmerUtil;
import com.mengzhihua.utils.common.text.RefinedSoundexUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CodiceFiscaleUtil;
import com.mengzhihua.utils.common.validate.DoiUtil;
import com.mengzhihua.utils.common.validate.EoriUtil;
import com.mengzhihua.utils.common.validate.IccidUtil;
import com.mengzhihua.utils.common.validate.NirUtil;
import com.mengzhihua.utils.common.validate.PmidUtil;
import com.mengzhihua.utils.common.validate.SteuerIdUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Civic Align Demo", description = "Refined Soundex / Porter / NIR / CF / Steuer-IdNr / EORI / DOI")
public class CivicAlignUtilsDemoController {

    @GetMapping("/refined-soundex")
    @Operation(summary = "Refined Soundex")
    public Result<Map<String, Object>> refinedSoundex(
            @RequestParam(defaultValue = "testing") String text,
            @RequestParam(defaultValue = "The") String other) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", RefinedSoundexUtil.encode(text));
        data.put("other", RefinedSoundexUtil.encode(other));
        data.put("similar", RefinedSoundexUtil.similar(text, other));
        return Result.ok(data);
    }

    @GetMapping("/porter")
    @Operation(summary = "Porter stemmer")
    public Result<Map<String, Object>> porter(@RequestParam(defaultValue = "relational") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("stem", PorterStemmerUtil.stem(text));
        return Result.ok(data);
    }

    @GetMapping("/nir")
    @Operation(summary = "法国 NIR / INSEE")
    public Result<Map<String, Object>> nir(@RequestParam(defaultValue = "255081416812535") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NirUtil.normalize(value));
        data.put("valid", NirUtil.isValid(value));
        data.put("female", NirUtil.isValid(value) && NirUtil.female(value));
        data.put("regex", RegexUtil.is("nir", value));
        return Result.ok(data);
    }

    @GetMapping("/codice-fiscale")
    @Operation(summary = "意大利税号")
    public Result<Map<String, Object>> codiceFiscale(@RequestParam(defaultValue = "RSSMRA80A01H501U") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CodiceFiscaleUtil.normalize(value));
        data.put("valid", CodiceFiscaleUtil.isValid(value));
        if (CodiceFiscaleUtil.isValid(value)) {
            data.put("female", CodiceFiscaleUtil.female(value));
            data.put("birthDate", CodiceFiscaleUtil.birthDate(value).toString());
        }
        data.put("regex", RegexUtil.is("codicefiscale", value));
        return Result.ok(data);
    }

    @GetMapping("/steuer-id")
    @Operation(summary = "德国税号")
    public Result<Map<String, Object>> steuerId(@RequestParam(defaultValue = "86095742719") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SteuerIdUtil.normalize(value));
        data.put("valid", SteuerIdUtil.isValid(value));
        data.put("regex", RegexUtil.is("steuerid", value));
        return Result.ok(data);
    }

    @GetMapping("/eori")
    @Operation(summary = "欧盟 EORI")
    public Result<Map<String, Object>> eori(@RequestParam(defaultValue = "FR73282932000074") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", EoriUtil.normalize(value));
        data.put("country", EoriUtil.country(value));
        data.put("identifier", EoriUtil.identifier(value));
        data.put("valid", EoriUtil.isValid(value));
        data.put("regex", RegexUtil.is("eori", value));
        return Result.ok(data);
    }

    @GetMapping("/doi")
    @Operation(summary = "DOI")
    public Result<Map<String, Object>> doi(@RequestParam(defaultValue = "10.1000/182") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", DoiUtil.normalize(value));
        data.put("valid", DoiUtil.isValid(value));
        data.put("regex", RegexUtil.is("doi", value));
        return Result.ok(data);
    }

    @GetMapping("/pmid")
    @Operation(summary = "PMID")
    public Result<Map<String, Object>> pmid(@RequestParam(defaultValue = "12345678") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PmidUtil.normalize(value));
        data.put("valid", PmidUtil.isValid(value));
        data.put("regex", RegexUtil.is("pmid", value));
        return Result.ok(data);
    }

    @GetMapping("/iccid")
    @Operation(summary = "SIM ICCID")
    public Result<Map<String, Object>> iccid(@RequestParam(defaultValue = "89014103211118510720") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IccidUtil.normalize(value));
        data.put("valid", IccidUtil.isValid(value));
        data.put("regex", RegexUtil.is("iccid", value));
        return Result.ok(data);
    }

    @GetMapping("/forwarded")
    @Operation(summary = "HTTP Forwarded RFC 7239")
    public Result<Map<String, Object>> forwarded(
            @RequestParam(defaultValue = "for=192.0.2.60;proto=http;by=203.0.113.43") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("clientIp", ForwardedUtil.clientIp(header));
        data.put("elements", ForwardedUtil.parse(header).stream()
                .map(element -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("for", element.forParam());
                    item.put("proto", element.proto());
                    item.put("by", element.by());
                    item.put("params", element.params());
                    return item;
                })
                .collect(Collectors.toList()));
        return Result.ok(data);
    }

    @GetMapping("/fletcher")
    @Operation(summary = "Fletcher-16 / Fletcher-32")
    public Result<Map<String, Object>> fletcher(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("fletcher16", HashUtil.fletcher16Hex(text));
        data.put("fletcher32", HashUtil.fletcher32Hex(text));
        return Result.ok(data);
    }
}
