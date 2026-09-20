package com.mengzhihua.utils.web.controller;


import java.util.LinkedHashMap;
import java.util.Map;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.math.InvoiceVatUtil;
import com.mengzhihua.utils.common.math.MortgageUtil;
import com.mengzhihua.utils.common.math.PitUtil;
import com.mengzhihua.utils.config.DesktopLauncher;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/utils")
@Tag(name = "Office Demo", description = "个税 / 房贷 / 发票")
public class OfficeUtilsDemoController {

    @GetMapping("/pit")
    @Operation(summary = "个税估算")
    public Result<Map<String, Object>> pit(
            @RequestParam(defaultValue = "30000") String income,
            @RequestParam(defaultValue = "0") String insurance,
            @RequestParam(defaultValue = "0") String special,
            @RequestParam(defaultValue = "1") int month) {
        return Result.ok(PitUtil.estimate(income, insurance, special, month));
    }

    @GetMapping("/mortgage")
    @Operation(summary = "房贷试算")
    public Result<Map<String, Object>> mortgage(
            @RequestParam(defaultValue = "1000000") String principal,
            @RequestParam(defaultValue = "4.2") String rate,
            @RequestParam(defaultValue = "30") int years,
            @RequestParam(defaultValue = "installment") String mode) {
        return Result.ok(MortgageUtil.calculate(principal, rate, years, mode));
    }

    @GetMapping("/invoice-vat")
    @Operation(summary = "发票价税分离")
    public Result<Map<String, Object>> invoiceVat(
            @RequestParam(defaultValue = "113") String amount,
            @RequestParam(defaultValue = "13") String rate,
            @RequestParam(defaultValue = "true") boolean taxIncluded) {
        return Result.ok(InvoiceVatUtil.split(amount, rate, taxIncluded));
    }

    @GetMapping("/desktop-url")
    @Operation(summary = "桌面控制台地址")
    public Result<Map<String, Object>> desktopUrl(@RequestParam(defaultValue = "18765") int port) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("url", DesktopLauncher.homeUrl(port));
        data.put("desktop", DesktopLauncher.requested(null));
        return Result.ok(data);
    }
}
