package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ReportToUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AcnUtil;
import com.mengzhihua.utils.common.validate.GstinUtil;
import com.mengzhihua.utils.common.validate.NpwpUtil;
import com.mengzhihua.utils.common.validate.RegistrikoodUtil;
import com.mengzhihua.utils.common.validate.VknUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GstAlignUtilsTest {

    @Test
    void gstinAcnVkn() {
        assertTrue(GstinUtil.isValid("27AAPFU0939F1ZV"));
        assertEquals("27AAPFU0939F1ZV", GstinUtil.complete("27AAPFU0939F1Z"));
        assertFalse(GstinUtil.isValid("27AAPFU0939F1ZO"));
        assertTrue(AcnUtil.isValid("000 000 019"));
        assertEquals("000000019", AcnUtil.complete("00000001"));
        assertFalse(AcnUtil.isValid("000 000 018"));
        assertTrue(VknUtil.isValid("4540536920"));
        assertEquals("4540536920", VknUtil.complete("454053692"));
        assertFalse(VknUtil.isValid("4540536921"));
        assertTrue(RegexUtil.is("gstin", "27AAPFU0939F1ZV"));
        assertTrue(RegexUtil.is("acn", "000000019"));
        assertTrue(RegexUtil.is("vkn", "4540536920"));
    }

    @Test
    void npwpRegistrikoodReportCrc() {
        assertTrue(NpwpUtil.isValid("01.312.166.0-091.000"));
        assertEquals("013121660", NpwpUtil.complete("01312166"));
        assertFalse(NpwpUtil.isValid("01.312.166.1-091.000"));
        assertTrue(RegistrikoodUtil.isValid("12345678"));
        assertEquals("12345678", RegistrikoodUtil.complete("1234567"));
        assertFalse(RegistrikoodUtil.isValid("12345679"));
        assertFalse(RegistrikoodUtil.isValid("32345674"));
        String reportTo = "[{\"group\":\"nel\",\"max_age\":31536000,\"endpoints\":[{\"url\":\"https://example.com/reports\"}]}]";
        assertEquals("nel", ReportToUtil.firstGroup(reportTo));
        assertEquals(31536000L, ReportToUtil.firstMaxAge(reportTo));
        assertTrue(ReportToUtil.hasGroup(reportTo, "nel"));
        assertTrue(RegexUtil.is("npwp", "013121660091000"));
        assertTrue(RegexUtil.is("registrikood", "12345678"));
        assertEquals("d64e", HashUtil.crc16GenibusHex("123456789"));
    }
}
