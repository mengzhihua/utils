package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.crypto.Sm3Util;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.id.SonyflakeUtil;
import com.mengzhihua.utils.common.net.ContentDispositionUtil;
import com.mengzhihua.utils.common.text.CaverphoneUtil;
import com.mengzhihua.utils.common.text.NysiisUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.time.JulianDayUtil;
import com.mengzhihua.utils.common.validate.CnpjUtil;
import com.mengzhihua.utils.common.validate.CpfUtil;
import com.mengzhihua.utils.common.validate.PeselUtil;
import com.mengzhihua.utils.common.validate.UpcEUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Locale Align Demo", description = "NYSIIS / Caverphone / CPF / PESEL / Sonyflake / Julian / RFC 6266")
public class LocaleAlignUtilsDemoController {

    @GetMapping("/nysiis")
    @Operation(summary = "NYSIIS 读音码")
    public Result<Map<String, Object>> nysiis(@RequestParam(defaultValue = "Miller") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", NysiisUtil.encode(text));
        return Result.ok(data);
    }

    @GetMapping("/caverphone")
    @Operation(summary = "Caverphone 2.0")
    public Result<Map<String, Object>> caverphone(@RequestParam(defaultValue = "Stevenson") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", CaverphoneUtil.encode(text));
        return Result.ok(data);
    }

    @GetMapping("/sonyflake")
    @Operation(summary = "Sonyflake ID")
    public Result<Map<String, Object>> sonyflake() {
        long id = IdUtil.sonyflake();
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", Long.toUnsignedString(id));
        data.put("unixMillis", SonyflakeUtil.unixMillis(id));
        data.put("machine", SonyflakeUtil.machine(id));
        data.put("sequence", SonyflakeUtil.sequence(id));
        return Result.ok(data);
    }

    @GetMapping("/content-disposition")
    @Operation(summary = "Content-Disposition RFC 6266")
    public Result<Map<String, Object>> contentDisposition(
            @RequestParam(defaultValue = "报表.txt") String filename) {
        String header = ContentDispositionUtil.attachment(filename);
        ContentDispositionUtil.Parsed parsed = ContentDispositionUtil.parse(header);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("header", header);
        data.put("type", parsed.type());
        data.put("filename", parsed.filename());
        return Result.ok(data);
    }

    @GetMapping("/julian")
    @Operation(summary = "儒略日")
    public Result<Map<String, Object>> julian(@RequestParam(defaultValue = "2000-01-01") String date) {
        long jdn = JulianDayUtil.of(date);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("julianDayNumber", jdn);
        data.put("julianDate", JulianDayUtil.julianDate(java.time.LocalDate.parse(date)));
        data.put("iso", JulianDayUtil.toLocalDate(jdn).toString());
        return Result.ok(data);
    }

    @GetMapping("/cpf")
    @Operation(summary = "巴西 CPF")
    public Result<Map<String, Object>> cpf(@RequestParam(defaultValue = "111.444.777-35") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CpfUtil.normalize(value));
        data.put("valid", CpfUtil.isValid(value));
        data.put("regex", RegexUtil.is("cpf", value));
        return Result.ok(data);
    }

    @GetMapping("/cnpj")
    @Operation(summary = "巴西 CNPJ")
    public Result<Map<String, Object>> cnpj(@RequestParam(defaultValue = "00.000.000/0001-91") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", CnpjUtil.normalize(value));
        data.put("valid", CnpjUtil.isValid(value));
        data.put("regex", RegexUtil.is("cnpj", value));
        return Result.ok(data);
    }

    @GetMapping("/pesel")
    @Operation(summary = "波兰 PESEL")
    public Result<Map<String, Object>> pesel(@RequestParam(defaultValue = "44051401359") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", PeselUtil.normalize(value));
        data.put("valid", PeselUtil.isValid(value));
        data.put("birthDate", PeselUtil.isValid(value) ? PeselUtil.birthDate(value).toString() : null);
        data.put("female", PeselUtil.isValid(value) && PeselUtil.female(value));
        data.put("regex", RegexUtil.is("pesel", value));
        return Result.ok(data);
    }

    @GetMapping("/upc-e")
    @Operation(summary = "UPC-E")
    public Result<Map<String, Object>> upcE(@RequestParam(defaultValue = "04252614") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", UpcEUtil.normalize(value));
        data.put("valid", UpcEUtil.isValid(value));
        if (UpcEUtil.isValid(value)) {
            data.put("upcA", UpcEUtil.expand(value));
        }
        data.put("regex", RegexUtil.is("upce", value));
        return Result.ok(data);
    }

    @GetMapping("/crc32-mpeg2")
    @Operation(summary = "CRC-32/MPEG-2")
    public Result<Map<String, Object>> crc32Mpeg2(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc32Mpeg2", HashUtil.crc32Mpeg2Hex(text));
        return Result.ok(data);
    }

    @GetMapping("/hmac-sm3")
    @Operation(summary = "HMAC-SM3")
    public Result<Map<String, Object>> hmacSm3(
            @RequestParam(defaultValue = "abc") String text,
            @RequestParam(defaultValue = "key") String key) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("hmacSm3", Sm3Util.hmac(text, key));
        return Result.ok(data);
    }

    @GetMapping("/murmur128")
    @Operation(summary = "MurmurHash3 x64_128")
    public Result<Map<String, Object>> murmur128(@RequestParam(defaultValue = "abc") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("murmur128", HashUtil.murmur128Hex(text));
        return Result.ok(data);
    }
}
