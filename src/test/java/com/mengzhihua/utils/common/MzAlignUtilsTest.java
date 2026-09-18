package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.AltSvcUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CuNiUtil;
import com.mengzhihua.utils.common.validate.GnNifpUtil;
import com.mengzhihua.utils.common.validate.MzNuitUtil;
import com.mengzhihua.utils.common.validate.SmCoeUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MzAlignUtilsTest {

    @Test
    void mozambiqueCubaGuinea() {
        assertTrue(MzNuitUtil.isValid("400339910"));
        assertTrue(MzNuitUtil.isValid("400 005 834"));
        assertEquals("400339910", MzNuitUtil.complete("40033991"));
        assertEquals("400 339 910", MzNuitUtil.format("400339910"));
        assertFalse(MzNuitUtil.isValid("400339911"));
        assertTrue(RegexUtil.is("mznuit", "400339910"));

        assertTrue(CuNiUtil.isValid("91021027775"));
        assertEquals(LocalDate.of(1991, 2, 10), CuNiUtil.birthDate("91021027775"));
        assertTrue(CuNiUtil.female("91021027775"));
        assertEquals(LocalDate.of(1972, 6, 25), CuNiUtil.birthDate("72062506561"));
        assertFalse(CuNiUtil.female("72062506561"));
        assertEquals(LocalDate.of(1885, 2, 2), CuNiUtil.birthDate("85020291531"));
        assertFalse(CuNiUtil.isValid("02023061531"));
        assertTrue(RegexUtil.is("cuni", "91021027775"));

        assertTrue(GnNifpUtil.isValid("693-770-885"));
        assertEquals("693770885", GnNifpUtil.complete("69377088"));
        assertEquals("693-770-885", GnNifpUtil.format("693770885"));
        assertFalse(GnNifpUtil.isValid("693770880"));
        assertTrue(RegexUtil.is("gnnifp", "693770885"));
    }

    @Test
    void sanMarinoAltSvcCrc() {
        assertTrue(SmCoeUtil.isValid("51"));
        assertEquals("24165", SmCoeUtil.normalize("024165"));
        assertTrue(SmCoeUtil.isValid("024165"));
        assertFalse(SmCoeUtil.isValid("2416A"));
        assertFalse(SmCoeUtil.isValid("3"));
        assertTrue(RegexUtil.is("smcoe", "24165"));

        String header = "h3=\":443\"; ma=86400, h2=\":443\"; ma=2592000";
        assertEquals("h3", AltSvcUtil.firstProtocol(header));
        assertTrue(AltSvcUtil.has(header, "h2"));
        assertEquals(":443", AltSvcUtil.parse(header).get(0).authority());
        assertEquals("86400", AltSvcUtil.parse(header).get(0).maxAge());
        assertTrue(AltSvcUtil.isClear("clear"));
        assertEquals("6f63", HashUtil.crc16Mcrf4xxHex("123456789"));
    }
}
