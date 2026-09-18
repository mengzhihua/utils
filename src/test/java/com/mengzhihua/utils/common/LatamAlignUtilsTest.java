package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.AcceptChUtil;
import com.mengzhihua.utils.common.net.ReportingEndpointsUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.ItinUtil;
import com.mengzhihua.utils.common.validate.RifUtil;
import com.mengzhihua.utils.common.validate.RncUtil;
import com.mengzhihua.utils.common.validate.UnpUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LatamAlignUtilsTest {

    @Test
    void rifRncUnp() {
        assertTrue(RifUtil.isValid("V-11470283-4"));
        assertEquals("V114702834", RifUtil.complete("V11470283"));
        assertFalse(RifUtil.isValid("V-11470283-3"));
        assertTrue(RncUtil.isValid("1-01-85004-3"));
        assertEquals("101850043", RncUtil.complete("10185004"));
        assertEquals("1-01-85004-3", RncUtil.format("101850043"));
        assertFalse(RncUtil.isValid("101850042"));
        assertTrue(UnpUtil.isValid("200988541"));
        assertTrue(UnpUtil.isValid("УНП MA1953684"));
        assertEquals("200988541", UnpUtil.complete("20098854"));
        assertEquals("MA1953684", UnpUtil.complete("MA195368"));
        assertFalse(UnpUtil.isValid("200988542"));
        assertTrue(RegexUtil.is("rif", "V114702834"));
        assertTrue(RegexUtil.is("rnc", "101850043"));
        assertTrue(RegexUtil.is("unp", "MA1953684"));
    }

    @Test
    void itinReportCrc() {
        assertTrue(ItinUtil.isValid("912-90-3456"));
        assertEquals("912-90-3456", ItinUtil.format("912903456"));
        assertFalse(ItinUtil.isValid("123-45-6789"));
        assertFalse(ItinUtil.isValid("912-93-4567"));
        assertFalse(ItinUtil.isValid("9129-03456"));
        String header = "csp=\"https://example.com/csp\", default=\"https://example.com/reports\"";
        assertEquals("csp", ReportingEndpointsUtil.firstName(header));
        assertTrue(ReportingEndpointsUtil.has(header, "default"));
        assertTrue(AcceptChUtil.has("Sec-CH-UA-Mobile, DPR", "dpr"));
        assertEquals("sec-ch-ua-mobile", AcceptChUtil.first("Sec-CH-UA-Mobile, DPR"));
        assertTrue(RegexUtil.is("itin", "912-90-3456"));
        assertEquals("4c06", HashUtil.crc16Cdma2000Hex("123456789"));
    }
}
