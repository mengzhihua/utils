package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.PriorityUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AtUidUtil;
import com.mengzhihua.utils.common.validate.NlBtwUtil;
import com.mengzhihua.utils.common.validate.SiDdvUtil;
import com.mengzhihua.utils.common.validate.SkDphUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "AT Align Demo", description = "AT UID / SK DPH / SI DDV / NL BTW")
public class AtAlignUtilsDemoController {

    @GetMapping("/at-uid")
    @Operation(summary = "奥地利税号")
    public Result<Map<String, Object>> atUid(@RequestParam(defaultValue = "AT U13585627") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", AtUidUtil.normalize(value));
        data.put("formatted", AtUidUtil.format(value));
        data.put("valid", AtUidUtil.isValid(value));
        data.put("regex", RegexUtil.is("atuid", value));
        return Result.ok(data);
    }

    @GetMapping("/sk-dph")
    @Operation(summary = "斯洛伐克税号")
    public Result<Map<String, Object>> skDph(@RequestParam(defaultValue = "SK 202 274 96 19") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SkDphUtil.normalize(value));
        data.put("formatted", SkDphUtil.format(value));
        data.put("valid", SkDphUtil.isValid(value));
        data.put("regex", RegexUtil.is("skdph", value));
        return Result.ok(data);
    }

    @GetMapping("/si-ddv")
    @Operation(summary = "斯洛文尼亚税号")
    public Result<Map<String, Object>> siDdv(@RequestParam(defaultValue = "SI 5022 3054") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", SiDdvUtil.normalize(value));
        data.put("formatted", SiDdvUtil.format(value));
        data.put("valid", SiDdvUtil.isValid(value));
        data.put("regex", RegexUtil.is("siddv", value));
        return Result.ok(data);
    }

    @GetMapping("/nl-btw")
    @Operation(summary = "荷兰税号")
    public Result<Map<String, Object>> nlBtw(@RequestParam(defaultValue = "NL004495445B01") String value) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("normalized", NlBtwUtil.normalize(value));
        data.put("formatted", NlBtwUtil.format(value));
        data.put("valid", NlBtwUtil.isValid(value));
        data.put("regex", RegexUtil.is("nlbtw", value));
        return Result.ok(data);
    }

    @GetMapping("/priority")
    @Operation(summary = "HTTP Priority")
    public Result<Map<String, Object>> priority(@RequestParam(defaultValue = "u=1, i") String header) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("urgency", PriorityUtil.urgency(header));
        data.put("incremental", PriorityUtil.incremental(header));
        data.put("valid", PriorityUtil.isValid(header));
        return Result.ok(data);
    }

    @GetMapping("/crc16-dect-r")
    @Operation(summary = "CRC-16/DECT-R")
    public Result<Map<String, Object>> crc16DectR(@RequestParam(defaultValue = "123456789") String text) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("crc16DectR", HashUtil.crc16DectRHex(text));
        return Result.ok(data);
    }
}
