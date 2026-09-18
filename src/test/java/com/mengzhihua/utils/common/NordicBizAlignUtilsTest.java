package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CifUtil;
import com.mengzhihua.utils.common.validate.CvrUtil;
import com.mengzhihua.utils.common.validate.OrgnrUtil;
import com.mengzhihua.utils.common.validate.SeOrgNrUtil;
import com.mengzhihua.utils.common.validate.YTunnusUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NordicBizAlignUtilsTest {

    @Test
    void yTunnusOrgnrCvr() {
        assertTrue(YTunnusUtil.isValid("1234567-1"));
        assertEquals("12345671", YTunnusUtil.complete("1234567"));
        assertFalse(YTunnusUtil.isValid("1234567-2"));
        assertTrue(OrgnrUtil.isValid("123456785"));
        assertEquals("123456785", OrgnrUtil.complete("12345678"));
        assertFalse(OrgnrUtil.isValid("123456786"));
        assertTrue(CvrUtil.isValid("35408002"));
        assertEquals("35408002", CvrUtil.complete("3540800"));
        assertTrue(RegexUtil.is("ytunnus", "1234567-1"));
        assertTrue(RegexUtil.is("orgnr", "123456785"));
        assertTrue(RegexUtil.is("cvr", "35408002"));
    }

    @Test
    void cifSeOrgCrc() {
        assertTrue(CifUtil.isValid("A58818501"));
        assertEquals("A58818501", CifUtil.complete("A5881850"));
        assertFalse(CifUtil.isValid("A58818502"));
        assertTrue(SeOrgNrUtil.isValid("556036-0793"));
        assertEquals("5560360793", SeOrgNrUtil.complete("556036079"));
        assertFalse(SeOrgNrUtil.isValid("556036-0794"));
        assertTrue(RegexUtil.is("cif", "A58818501"));
        assertTrue(RegexUtil.is("orgnrse", "556036-0793"));
        assertEquals("765e7680", HashUtil.crc32PosixHex("123456789"));
    }
}
