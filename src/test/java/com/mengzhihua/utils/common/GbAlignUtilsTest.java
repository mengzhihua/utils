package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ExpectCtUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CaBnUtil;
import com.mengzhihua.utils.common.validate.CzDicUtil;
import com.mengzhihua.utils.common.validate.GbVatUtil;
import com.mengzhihua.utils.common.validate.Iso11649Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GbAlignUtilsTest {

    @Test
    void ukCanadaCzechCreditor() {
        assertTrue(GbVatUtil.isValid("GB 980 7806 84"));
        assertEquals("980 7806 84", GbVatUtil.format("980780684"));
        assertTrue(GbVatUtil.isValid("GD123"));
        assertTrue(GbVatUtil.isValid("HA500"));
        assertFalse(GbVatUtil.isValid("802311781"));
        assertTrue(RegexUtil.is("gbvat", "980780684"));

        assertTrue(CaBnUtil.isValid("12302 6635"));
        assertTrue(CaBnUtil.isValid("12302 6635 RC 0001"));
        assertEquals("123026635", CaBnUtil.complete("12302663"));
        assertFalse(CaBnUtil.isValid("123456783"));
        assertTrue(RegexUtil.is("cabn", "123026635RC0001"));

        assertTrue(CzDicUtil.isValid("CZ 25123891"));
        assertTrue(CzDicUtil.isValid("7103192745"));
        assertTrue(CzDicUtil.isValid("640903926"));
        assertEquals("25123891", CzDicUtil.complete("2512389"));
        assertFalse(CzDicUtil.isValid("25123890"));
        assertTrue(RegexUtil.is("czdic", "25123891"));

        assertTrue(Iso11649Util.isValid("RF18 5390 0754 7034"));
        assertTrue(Iso11649Util.isValid("RF18 5390 0754 70Y"));
        assertEquals("RF18539007547034", Iso11649Util.complete("539007547034"));
        assertEquals("RF18 5390 0754 7034", Iso11649Util.format("RF18539007547034"));
        assertFalse(Iso11649Util.isValid("RF17 5390 0754 7034"));
        assertTrue(RegexUtil.is("iso11649", "RF18539007547034"));
    }

    @Test
    void expectCtCrcX25() {
        assertTrue(ExpectCtUtil.isValid("max-age=86400, enforce"));
        assertEquals(86400L, ExpectCtUtil.maxAge("max-age=86400, enforce"));
        assertTrue(ExpectCtUtil.enforce("max-age=86400, enforce"));
        assertEquals("https://ex", ExpectCtUtil.reportUri("max-age=60, report-uri=\"https://ex\""));
        assertFalse(ExpectCtUtil.isValid("enforce"));
        assertEquals("906e", HashUtil.crc16X25Hex("123456789"));
    }
}
