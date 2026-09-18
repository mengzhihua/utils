package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.DnsPrefetchUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.FoVnUtil;
import com.mengzhihua.utils.common.validate.FrTvaUtil;
import com.mengzhihua.utils.common.validate.McTvaUtil;
import com.mengzhihua.utils.common.validate.MuNidUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FoAlignUtilsTest {

    @Test
    void faroeFranceMonaco() {
        assertTrue(FoVnUtil.isValid("623857"));
        assertTrue(FoVnUtil.isValid("FO 602 590"));
        assertEquals("602590", FoVnUtil.normalize("FO 602 590"));
        assertFalse(FoVnUtil.isValid("1234"));
        assertTrue(RegexUtil.is("fovn", "623857"));

        assertTrue(FrTvaUtil.isValid("Fr 40 303 265 045"));
        assertTrue(FrTvaUtil.isValid("23334175221"));
        assertTrue(FrTvaUtil.isValid("K7399859412"));
        assertTrue(FrTvaUtil.isValid("4Z123456782"));
        assertEquals("40303265045", FrTvaUtil.complete("303265045"));
        assertEquals("40 303 265 045", FrTvaUtil.format("40303265045"));
        assertFalse(FrTvaUtil.isValid("84 323 140 391"));
        assertFalse(FrTvaUtil.isValid("IO334175221"));
        assertTrue(RegexUtil.is("frtva", "40303265045"));

        assertTrue(McTvaUtil.isValid("53 0000 04605"));
        assertEquals("53000004605", McTvaUtil.complete("000004605"));
        assertFalse(McTvaUtil.isValid("FR 61 954 506 077"));
        assertFalse(McTvaUtil.isValid("40303265045"));
        assertTrue(RegexUtil.is("mctva", "53000004605"));
    }

    @Test
    void mauritiusDnsCrc() {
        assertTrue(MuNidUtil.isValid("B150390123456A"));
        assertEquals("B150390123456A", MuNidUtil.complete("B150390123456"));
        assertEquals(LocalDate.of(2090, 3, 15), MuNidUtil.birthDate("B150390123456A"));
        assertFalse(MuNidUtil.isValid("B150390123456G"));
        assertTrue(RegexUtil.is("munid", "B150390123456A"));

        assertTrue(DnsPrefetchUtil.isOn("ON"));
        assertTrue(DnsPrefetchUtil.isOff("off"));
        assertFalse(DnsPrefetchUtil.isValid("maybe"));
        assertEquals("25", HashUtil.crc8WcdmaHex("123456789"));
    }
}
