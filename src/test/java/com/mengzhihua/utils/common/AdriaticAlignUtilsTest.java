package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.HttpAgeUtil;
import com.mengzhihua.utils.common.net.WarningUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EmsoUtil;
import com.mengzhihua.utils.common.validate.LatvianPkUtil;
import com.mengzhihua.utils.common.validate.LithuanianAkUtil;
import com.mengzhihua.utils.common.validate.MxRfcUtil;
import com.mengzhihua.utils.common.validate.PeDniUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdriaticAlignUtilsTest {

    @Test
    void lvPkLtAkEmso() {
        assertTrue(LatvianPkUtil.isValid("111111-11111"));
        assertEquals("11111111111", LatvianPkUtil.complete("1111111111"));
        assertEquals(LocalDate.of(1911, 11, 11), LatvianPkUtil.birthDate("111111-11111"));
        assertFalse(LatvianPkUtil.modern("111111-11111"));
        assertTrue(LithuanianAkUtil.isValid("33309240064"));
        assertEquals("33309240064", LithuanianAkUtil.complete("3330924006"));
        assertFalse(LithuanianAkUtil.female("33309240064"));
        assertEquals(LocalDate.of(1933, 9, 24), LithuanianAkUtil.birthDate("33309240064"));
        assertTrue(EmsoUtil.isValid("0101006500006"));
        assertEquals("0101006500006", EmsoUtil.complete("010100650000"));
        assertFalse(EmsoUtil.female("0101006500006"));
        assertEquals(LocalDate.of(2006, 1, 1), EmsoUtil.birthDate("0101006500006"));
        assertFalse(EmsoUtil.isValid("0101006500007"));
        assertTrue(RegexUtil.is("lvpk", "111111-11111"));
        assertTrue(RegexUtil.is("ltak", "33309240064"));
        assertTrue(RegexUtil.is("emso", "0101006500006"));
    }

    @Test
    void peDniMxRfc() {
        assertTrue(PeDniUtil.isValid("713903006"));
        assertEquals("713903006", PeDniUtil.complete("71390300"));
        assertTrue(PeDniUtil.isValid("71390300K"));
        assertFalse(PeDniUtil.isValid("713903002"));
        assertTrue(MxRfcUtil.isValid("GODE561231GR8"));
        assertEquals("GODE561231GR8", MxRfcUtil.complete("GODE561231GR"));
        assertTrue(MxRfcUtil.isValid("XAXX010101000"));
        assertFalse(MxRfcUtil.isValid("GODE561231GR7"));
        assertTrue(RegexUtil.is("pedni", "713903006"));
        assertTrue(RegexUtil.is("mxrfc", "GODE561231GR8"));
    }

    @Test
    void httpAgeWarningCrc() {
        assertEquals(3600L, HttpAgeUtil.parse("3600"));
        assertEquals(3600L, HttpAgeUtil.parse("3600, 12"));
        assertTrue(HttpAgeUtil.fresh("3600", 3600));
        assertFalse(HttpAgeUtil.valid("stale"));
        WarningUtil.WarningValue warning = WarningUtil.parse("110 - \"Response is Stale\"");
        assertEquals(110, warning.code());
        assertEquals("-", warning.agent());
        assertEquals("Response is Stale", warning.text());
        assertTrue(warning.stale());
        assertTrue(WarningUtil.known("110 - \"Response is Stale\""));
        assertEquals("44c2", HashUtil.crc16MaximHex("123456789"));
    }
}
