package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.CorpUtil;
import com.mengzhihua.utils.common.net.XctoUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.InnUtil;
import com.mengzhihua.utils.common.validate.NikUtil;
import com.mengzhihua.utils.common.validate.PeRucUtil;
import com.mengzhihua.utils.common.validate.VnMstUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CisAlignUtilsTest {

    @Test
    void innRucNik() {
        assertTrue(InnUtil.isValid("1234567894"));
        assertTrue(InnUtil.isValid("7707083893"));
        assertTrue(InnUtil.isValid("123456789047"));
        assertEquals("1234567894", InnUtil.complete("123456789"));
        assertEquals("123456789047", InnUtil.complete("1234567890"));
        assertFalse(InnUtil.isValid("1234567895"));
        assertFalse(InnUtil.isValid("123456789037"));
        assertTrue(PeRucUtil.isValid("20512333797"));
        assertEquals("20512333797", PeRucUtil.complete("2051233379"));
        assertFalse(PeRucUtil.isValid("20512333798"));
        assertTrue(NikUtil.isValid("3171011708450001"));
        assertEquals(LocalDate.of(1945, 8, 17), NikUtil.birthDate("3171011708450001"));
        assertTrue(NikUtil.female("3171015708450001"));
        assertTrue(NikUtil.isValid("3171015708450001"));
        assertFalse(NikUtil.isValid("9971011708450001"));
        assertTrue(RegexUtil.is("inn", "7707083893"));
        assertTrue(RegexUtil.is("peruc", "20512333797"));
        assertTrue(RegexUtil.is("nik", "3171011708450001"));
    }

    @Test
    void mstCorpCrc() {
        assertTrue(VnMstUtil.isValid("0100233488"));
        assertTrue(VnMstUtil.isValid("0314409058-002"));
        assertEquals("0100233488", VnMstUtil.complete("010023348"));
        assertFalse(VnMstUtil.isValid("0100233480"));
        assertEquals("same-origin", CorpUtil.parse("same-origin"));
        assertTrue(CorpUtil.sameOrigin("same-origin"));
        assertTrue(CorpUtil.sameSite("same-site"));
        assertTrue(CorpUtil.known("cross-origin"));
        assertTrue(XctoUtil.nosniff("nosniff"));
        assertTrue(XctoUtil.known("NOSNIFF"));
        assertFalse(XctoUtil.known("unsafe"));
        assertTrue(RegexUtil.is("vnmst", "0100233488"));
        assertEquals("ea82", HashUtil.crc16DnpHex("123456789"));
    }
}
