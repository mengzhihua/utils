package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.XssProtectionUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EcCiUtil;
import com.mengzhihua.utils.common.validate.EcRucUtil;
import com.mengzhihua.utils.common.validate.IeVatUtil;
import com.mengzhihua.utils.common.validate.ItIvaUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EcAlignUtilsTest {

    @Test
    void ecuadorItalyIreland() {
        assertTrue(EcCiUtil.isValid("171430710-3"));
        assertEquals("1714307103", EcCiUtil.complete("171430710"));
        assertEquals("171430710-3", EcCiUtil.format("1714307103"));
        assertFalse(EcCiUtil.isValid("1714307104"));
        assertTrue(RegexUtil.is("ecci", "1714307103"));

        assertTrue(EcRucUtil.isValid("1792060346-001"));
        assertEquals("1792060346001", EcRucUtil.complete("1792060346"));
        assertFalse(EcRucUtil.isValid("1763154690001"));
        assertTrue(RegexUtil.is("ecruc", "1792060346001"));

        assertTrue(ItIvaUtil.isValid("IT 00743110157"));
        assertEquals("00743110157", ItIvaUtil.complete("0074311015"));
        assertFalse(ItIvaUtil.isValid("00743110158"));
        assertTrue(RegexUtil.is("itiva", "00743110157"));

        assertTrue(IeVatUtil.isValid("IE 6433435F"));
        assertTrue(IeVatUtil.isValid("IE 6433435OA"));
        assertTrue(IeVatUtil.isValid("8D79739I"));
        assertEquals("6433435F", IeVatUtil.complete("6433435"));
        assertFalse(IeVatUtil.isValid("6433435E"));
        assertTrue(RegexUtil.is("ievat", "6433435F"));
    }

    @Test
    void xssCrcMaxim() {
        assertTrue(XssProtectionUtil.enabled("1; mode=block"));
        assertTrue(XssProtectionUtil.modeBlock("1; mode=block"));
        assertEquals("https://ex", XssProtectionUtil.report("1; report=https://ex"));
        assertFalse(XssProtectionUtil.isValid("maybe"));
        assertEquals("a1", HashUtil.crc8MaximHex("123456789"));
    }
}
