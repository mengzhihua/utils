package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.codec.Z85Util;
import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.CacheControlUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AadhaarUtil;
import com.mengzhihua.utils.common.validate.AhvUtil;
import com.mengzhihua.utils.common.validate.NipUtil;
import com.mengzhihua.utils.common.validate.PanUtil;
import com.mengzhihua.utils.common.validate.PpsUtil;
import com.mengzhihua.utils.common.validate.SinUtil;
import com.mengzhihua.utils.common.validate.VatUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaxAlignUtilsTest {

    @Test
    void vatAhvNip() {
        assertTrue(VatUtil.isValid("DE136695976"));
        assertTrue(VatUtil.isValid("FR44732829320"));
        assertTrue(VatUtil.isValid("NL002875718B01"));
        assertEquals("DE", VatUtil.country("DE136695976"));
        assertFalse(VatUtil.isValid("DE136695977"));
        assertTrue(AhvUtil.isValid("756.1234.5678.97"));
        assertEquals("7561234567897", AhvUtil.complete("756123456789"));
        assertTrue(NipUtil.isValid("1234563218"));
        assertEquals("1234563218", NipUtil.complete("123456321"));
        assertFalse(NipUtil.isValid("1234563219"));
        assertTrue(RegexUtil.is("vat", "DE136695976"));
        assertTrue(RegexUtil.is("ahv", "756.1234.5678.97"));
        assertTrue(RegexUtil.is("nip", "1234563218"));
    }

    @Test
    void aadhaarPanSinPps() {
        assertTrue(AadhaarUtil.isValid("234123412346"));
        assertEquals("234123412346", AadhaarUtil.complete("23412341234"));
        assertFalse(AadhaarUtil.isValid("134123412346"));
        assertTrue(PanUtil.isValid("ABCPE1234F"));
        assertEquals('P', PanUtil.entityType("ABCPE1234F"));
        assertFalse(PanUtil.isValid("ABCEE1234F"));
        assertTrue(SinUtil.isValid("046454286"));
        assertEquals("046454286", SinUtil.complete("04645428"));
        assertTrue(PpsUtil.isValid("1234567T"));
        assertEquals("1234567T", PpsUtil.complete("1234567"));
        assertFalse(PpsUtil.isValid("1234567A"));
        assertTrue(RegexUtil.is("aadhaar", "234123412346"));
        assertTrue(RegexUtil.is("pan", "ABCPE1234F"));
        assertTrue(RegexUtil.is("sin", "046454286"));
        assertTrue(RegexUtil.is("pps", "1234567T"));
    }

    @Test
    void cacheControlZ85Xmodem() {
        assertEquals(3600L, CacheControlUtil.maxAge("max-age=3600, public, must-revalidate"));
        assertTrue(CacheControlUtil.has("max-age=3600, public", "public"));
        assertFalse(CacheControlUtil.has("no-cache", "no-store"));
        assertEquals("HelloWorld", Z85Util.encode(new byte[]{
                (byte) 0x86, 0x4F, (byte) 0xD2, 0x6F, (byte) 0xB5, 0x59, (byte) 0xF7, 0x5B}));
        assertEquals("864fd26fb559f75b", Z85Util.decodeToHex("HelloWorld"));
        assertEquals("31c3", HashUtil.crc16XmodemHex("123456789"));
    }
}
