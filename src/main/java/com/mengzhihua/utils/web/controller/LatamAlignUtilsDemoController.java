package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.AcceptChUtil;
import com.mengzhihua.utils.common.net.ReportingEndpointsUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.ItinUtil;
import com.mengzhihua.utils.common.validate.RifUtil;
import com.mengzhihua.utils.common.validate.RncUtil;
import com.mengzhihua.utils.common.validate.UnpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Latam Align Demo", description = "RIF / RNC / UNP / ITIN")
public class LatamAlignUtilsDemoController {

    @GetMapping("/rif")
    @Operation(summary = "委内瑞拉税号")
    public Result<Map<String, Object>> rif(@RequestParam(defaultValue = "V-11470283-4") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", RifUtil.normalize(value));
        data.put("valid", RifUtil.isValid(value));
        data.put("regex", RegexUtil.is("rif", value));
        return Result.ok(data);
    }

    @GetMapping("/rnc")
    @Operation(summary = "多米尼加税号")
    public Result<Map<String, Object>> rnc(@RequestParam(defaultValue = "1-01-85004-3") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", RncUtil.normalize(value));
        data.put("formatted", RncUtil.format(value));
        data.put("valid", RncUtil.isValid(value));
        data.put("regex", RegexUtil.is("rnc", value));
        return Result.ok(data);
    }

    @GetMapping("/unp")
    @Operation(summary = "白俄罗斯税号")
    public Result<Map<String, Object>> unp(@RequestParam(defaultValue = "200988541") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", UnpUtil.normalize(value));
        data.put("valid", UnpUtil.isValid(value));
        data.put("regex", RegexUtil.is("unp", value));
        return Result.ok(data);
    }

    @GetMapping("/itin")
    @Operation(summary = "美国个人税号")
    public Result<Map<String, Object>> itin(@RequestParam(defaultValue = "912-90-3456") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", ItinUtil.normalize(value));
        data.put("formatted", ItinUtil.format(value));
        data.put("valid", ItinUtil.isValid(value));
        data.put("regex", RegexUtil.is("itin", value));
        return Result.ok(data);
    }

    @GetMapping("/reporting-endpoints")
    @Operation(summary = "HTTP Reporting-Endpoints")
    public Result<Map<String, Object>> reportingEndpoints(
            @RequestParam(defaultValue = "csp=\"https://example.com/csp\", default=\"https://example.com/reports\"")
            String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("firstName", ReportingEndpointsUtil.firstName(header));
        data.put("firstUrl", ReportingEndpointsUtil.firstUrl(header));
        data.put("hasCsp", ReportingEndpointsUtil.has(header, "csp"));
        return Result.ok(data);
    }

    @GetMapping("/accept-ch")
    @Operation(summary = "HTTP Accept-CH")
    public Result<Map<String, Object>> acceptCh(
            @RequestParam(defaultValue = "Sec-CH-UA-Mobile, DPR") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("hints", AcceptChUtil.parse(header));
        data.put("first", AcceptChUtil.first(header));
        data.put("hasDpr", AcceptChUtil.has(header, "DPR"));
        return Result.ok(data);
    }

    @GetMapping("/crc16-cdma2000")
    @Operation(summary = "CRC-16/CDMA2000")
    public Result<Map<String, Object>> crc16Cdma2000(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16Cdma2000", HashUtil.crc16Cdma2000Hex(text));
        return Result.ok(data);
    }
}
