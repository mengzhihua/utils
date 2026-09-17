package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Base91Util;
import com.mengzhihua.utils.common.codec.BencodeUtil;
import com.mengzhihua.utils.common.net.HttpAcceptUtil;
import com.mengzhihua.utils.common.text.DoubleMetaphoneUtil;
import com.mengzhihua.utils.common.text.MatchRatingUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.IsniUtil;
import com.mengzhihua.utils.common.validate.NifUtil;
import com.mengzhihua.utils.common.validate.SirenUtil;
import com.mengzhihua.utils.common.validate.SiretUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Euro Align Demo", description = "Double Metaphone / SIREN / NIF / ISNI / basE91 / Bencode")
public class EuroAlignUtilsDemoController {

    @GetMapping("/double-metaphone")
    @Operation(summary = "Double Metaphone")
    public Result<Map<String, Object>> doubleMetaphone(@RequestParam(defaultValue = "Smith") String text) {
        DoubleMetaphoneUtil.Codes codes = DoubleMetaphoneUtil.encodeBoth(text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("primary", codes.primary());
        data.put("alternate", codes.alternate());
        return Result.ok(data);
    }

    @GetMapping("/match-rating")
    @Operation(summary = "Match Rating Approach")
    public Result<Map<String, Object>> matchRating(
            @RequestParam(defaultValue = "Smith") String left,
            @RequestParam(defaultValue = "Smyth") String right) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("left", MatchRatingUtil.encode(left));
        data.put("right", MatchRatingUtil.encode(right));
        data.put("similar", MatchRatingUtil.similar(left, right));
        return Result.ok(data);
    }

    @GetMapping("/siren")
    @Operation(summary = "法国 SIREN")
    public Result<Map<String, Object>> siren(@RequestParam(defaultValue = "732829320") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SirenUtil.normalize(value));
        data.put("valid", SirenUtil.isValid(value));
        data.put("regex", RegexUtil.is("siren", value));
        return Result.ok(data);
    }

    @GetMapping("/siret")
    @Operation(summary = "法国 SIRET")
    public Result<Map<String, Object>> siret(@RequestParam(defaultValue = "73282932000074") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SiretUtil.normalize(value));
        data.put("siren", SiretUtil.siren(value));
        data.put("valid", SiretUtil.isValid(value));
        data.put("regex", RegexUtil.is("siret", value));
        return Result.ok(data);
    }

    @GetMapping("/nif")
    @Operation(summary = "西班牙 NIF / NIE")
    public Result<Map<String, Object>> nif(@RequestParam(defaultValue = "12345678Z") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NifUtil.normalize(value));
        data.put("valid", NifUtil.isValid(value));
        data.put("regex", RegexUtil.is("nif", value));
        return Result.ok(data);
    }

    @GetMapping("/isni")
    @Operation(summary = "ISNI")
    public Result<Map<String, Object>> isni(@RequestParam(defaultValue = "0000 0001 2146 358X") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", IsniUtil.normalize(value));
        data.put("valid", IsniUtil.isValid(value));
        data.put("regex", RegexUtil.is("isni", value));
        return Result.ok(data);
    }

    @GetMapping("/base91")
    @Operation(summary = "basE91")
    public Result<Map<String, Object>> base91(@RequestParam(defaultValue = "Hello World") String text) {
        String encoded = Base91Util.encode(text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", Base91Util.decodeToString(encoded));
        return Result.ok(data);
    }

    @GetMapping("/bencode")
    @Operation(summary = "Bencode")
    public Result<Map<String, Object>> bencode(@RequestParam(defaultValue = "spam") String text) {
        String encoded = BencodeUtil.encode(text);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encoded", encoded);
        data.put("decoded", BencodeUtil.decode(encoded));
        data.put("list", BencodeUtil.encode(java.util.List.of(text, 3L)));
        return Result.ok(data);
    }

    @GetMapping("/http-accept")
    @Operation(summary = "HTTP Accept")
    public Result<Map<String, Object>> httpAccept(
            @RequestParam(defaultValue = "text/html,application/json;q=0.9,*/*;q=0.8") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("ranges", HttpAcceptUtil.parse(header).stream()
                .map(range -> {
                    Map<String, Object> item = new LinkedHashMap<>();
                    item.put("type", range.type());
                    item.put("q", range.q());
                    return item;
                })
                .collect(Collectors.toList()));
        data.put("negotiated", HttpAcceptUtil.negotiate(header, "application/json", "text/html"));
        return Result.ok(data);
    }
}
