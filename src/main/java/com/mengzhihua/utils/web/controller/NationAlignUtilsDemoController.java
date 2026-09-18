package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.codec.Base36Util;
import com.mengzhihua.utils.common.codec.YencUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.net.RetryAfterUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AfmUtil;
import com.mengzhihua.utils.common.validate.CprUtil;
import com.mengzhihua.utils.common.validate.MyNumberUtil;
import com.mengzhihua.utils.common.validate.NinoUtil;
import com.mengzhihua.utils.common.validate.NrnUtil;
import com.mengzhihua.utils.common.validate.PtNifUtil;
import com.mengzhihua.utils.common.validate.RrnUtil;
import com.mengzhihua.utils.common.validate.SvnrUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Nation Align Demo", description = "CPR / NRN / SVNR / PT-NIF / AFM / NINO / RRN / My Number")
public class NationAlignUtilsDemoController {

    @GetMapping("/cpr")
    @Operation(summary = "丹麦 CPR")
    public Result<Map<String, Object>> cpr(@RequestParam(defaultValue = "010170-0003") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CprUtil.normalize(value));
        data.put("valid", CprUtil.isValid(value));
        if (CprUtil.isValid(value)) {
            data.put("birthDate", CprUtil.birthDate(value).toString());
            data.put("female", CprUtil.female(value));
        }
        data.put("regex", RegexUtil.is("cpr", value));
        return Result.ok(data);
    }

    @GetMapping("/nrn")
    @Operation(summary = "比利时国家登记号")
    public Result<Map<String, Object>> nrn(@RequestParam(defaultValue = "93.05.18-223.61") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NrnUtil.normalize(value));
        data.put("valid", NrnUtil.isValid(value));
        if (NrnUtil.isValid(value)) {
            data.put("birthDate", NrnUtil.birthDate(value).toString());
            data.put("female", NrnUtil.female(value));
        }
        data.put("regex", RegexUtil.is("nrn", value));
        return Result.ok(data);
    }

    @GetMapping("/svnr")
    @Operation(summary = "奥地利社保号")
    public Result<Map<String, Object>> svnr(@RequestParam(defaultValue = "1237010180") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SvnrUtil.normalize(value));
        data.put("valid", SvnrUtil.isValid(value));
        if (SvnrUtil.isValid(value)) {
            data.put("birthDate", SvnrUtil.birthDate(value).toString());
        }
        data.put("regex", RegexUtil.is("svnr", value));
        return Result.ok(data);
    }

    @GetMapping("/pt-nif")
    @Operation(summary = "葡萄牙 NIF")
    public Result<Map<String, Object>> ptNif(@RequestParam(defaultValue = "123456789") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PtNifUtil.normalize(value));
        data.put("valid", PtNifUtil.isValid(value));
        data.put("regex", RegexUtil.is("ptnif", value));
        return Result.ok(data);
    }

    @GetMapping("/afm")
    @Operation(summary = "希腊 AFM")
    public Result<Map<String, Object>> afm(@RequestParam(defaultValue = "090000045") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AfmUtil.normalize(value));
        data.put("valid", AfmUtil.isValid(value));
        data.put("regex", RegexUtil.is("afm", value));
        return Result.ok(data);
    }

    @GetMapping("/nino")
    @Operation(summary = "英国 NINO")
    public Result<Map<String, Object>> nino(@RequestParam(defaultValue = "AB123456C") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NinoUtil.normalize(value));
        data.put("prefix", NinoUtil.prefix(value));
        data.put("valid", NinoUtil.isValid(value));
        data.put("regex", RegexUtil.is("nino", value));
        return Result.ok(data);
    }

    @GetMapping("/rrn")
    @Operation(summary = "韩国居民登记号")
    public Result<Map<String, Object>> rrn(@RequestParam(defaultValue = "900101-1234568") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", RrnUtil.normalize(value));
        data.put("valid", RrnUtil.isValid(value));
        if (RrnUtil.isValid(value)) {
            data.put("birthDate", RrnUtil.birthDate(value).toString());
            data.put("female", RrnUtil.female(value));
        }
        data.put("regex", RegexUtil.is("rrn", value));
        return Result.ok(data);
    }

    @GetMapping("/my-number")
    @Operation(summary = "日本个人番号")
    public Result<Map<String, Object>> myNumber(@RequestParam(defaultValue = "123456789019") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", MyNumberUtil.normalize(value));
        data.put("valid", MyNumberUtil.isValid(value));
        data.put("regex", RegexUtil.is("mynumber", value));
        return Result.ok(data);
    }

    @GetMapping("/yenc")
    @Operation(summary = "yEnc")
    public Result<Map<String, Object>> yenc(@RequestParam(defaultValue = "Hello") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encodedHex", YencUtil.encodeToHex(text));
        data.put("decoded", YencUtil.decodeToString(YencUtil.encode(text)));
        return Result.ok(data);
    }

    @GetMapping("/base36")
    @Operation(summary = "Base36")
    public Result<Map<String, Object>> base36(
            @RequestParam(defaultValue = "hello") String text,
            @RequestParam(defaultValue = "1234567890") long value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("encodedText", Base36Util.encode(text));
        data.put("decodedText", Base36Util.decodeToString(Base36Util.encode(text)));
        data.put("encodedLong", Base36Util.encode(value));
        data.put("decodedLong", Base36Util.decodeLong(Base36Util.encode(value)));
        return Result.ok(data);
    }

    @GetMapping("/retry-after")
    @Operation(summary = "HTTP Retry-After RFC 9110")
    public Result<Map<String, Object>> retryAfter(@RequestParam(defaultValue = "120") String header) {
        RetryAfterUtil.RetryAfter parsed = RetryAfterUtil.parse(header);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("delaySeconds", parsed.delaySeconds());
        data.put("seconds", parsed.seconds());
        data.put("date", parsed.date() == null ? "" : parsed.date().toString());
        return Result.ok(data);
    }

    @GetMapping("/uuid-v1")
    @Operation(summary = "UUID v1")
    public Result<Map<String, Object>> uuidV1() {
        String id = IdUtil.uuidV1();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("uuid", id);
        data.put("version", Character.digit(id.charAt(14), 16));
        return Result.ok(data);
    }
}
