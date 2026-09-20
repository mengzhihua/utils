package com.mengzhihua.utils.common;


import java.math.BigDecimal;
import java.util.Map;

import com.mengzhihua.utils.common.math.InvoiceVatUtil;
import com.mengzhihua.utils.common.math.MortgageUtil;
import com.mengzhihua.utils.common.math.PitUtil;
import com.mengzhihua.utils.config.DesktopLauncher;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OfficeUtilsTest {

    @Test
    void pitMortgageInvoice() {
        Map<String, Object> pit = PitUtil.estimate("30000", "0", "0", 1);
        assertEquals(0, new BigDecimal("750.00").compareTo((BigDecimal) pit.get("taxCumulative")));
        assertEquals(0, new BigDecimal("29250.00").compareTo((BigDecimal) pit.get("netMonthly")));

        Map<String, Object> year = PitUtil.estimate("30000", "0", "0", 12);
        assertEquals(0, new BigDecimal("43080.00").compareTo((BigDecimal) year.get("taxCumulative")));

        Map<String, Object> loan = MortgageUtil.calculate("1000000", "4.2", 30, "installment");
        assertEquals(360, loan.get("months"));
        BigDecimal monthly = (BigDecimal) loan.get("monthly");
        assertTrue(monthly.compareTo(new BigDecimal("4800")) > 0 && monthly.compareTo(new BigDecimal("5000")) < 0);

        Map<String, Object> invoice = InvoiceVatUtil.split("113", "13", true);
        assertEquals(0, new BigDecimal("100.00").compareTo((BigDecimal) invoice.get("exclusive")));
        assertEquals(0, new BigDecimal("13.00").compareTo((BigDecimal) invoice.get("tax")));
        assertTrue(String.valueOf(invoice.get("rmb")).contains("壹佰壹拾叁"));
    }

    @Test
    void desktopHomeUrl() {
        assertEquals("http://127.0.0.1:18765/#/office", DesktopLauncher.homeUrl(18765));
        assertTrue(DesktopLauncher.requested(new String[] {"--desktop"}));
    }
}
