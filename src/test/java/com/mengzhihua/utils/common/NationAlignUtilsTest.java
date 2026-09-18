package com.mengzhihua.utils.common;


import java.time.Instant;
import java.time.LocalDate;

import com.mengzhihua.utils.common.codec.Base36Util;
import com.mengzhihua.utils.common.codec.YencUtil;
import com.mengzhihua.utils.common.id.IdUtil;
import com.mengzhihua.utils.common.net.RetryAfterUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AfmUtil;
import com.mengzhihua.utils.common.validate.CprUtil;
import com.mengzhihua.utils.common.validate.MyNumberUtil;
import com.mengzhihua.utils.common.validate.NinoUtil;
import com.mengzhihua.utils.common.validate.NrnUtil;
import com.mengzhihua.utils.common.validate.PtNifUtil;
import com.mengzhihua.utils.common.validate.RrnUtil;
import com.mengzhihua.utils.common.validate.SvnrUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NationAlignUtilsTest {

    @Test
    void cprNrnSvnr() {
        assertTrue(CprUtil.isValid("010170-0003"));
        assertEquals("0101700003", CprUtil.complete("010170000"));
        assertFalse(CprUtil.female("010170-0003"));
        assertEquals(LocalDate.of(1970, 1, 1), CprUtil.birthDate("010170-0003"));
        assertFalse(CprUtil.isValid("010170-0004"));
        assertTrue(NrnUtil.isValid("93.05.18-223.61"));
        assertEquals("93051822361", NrnUtil.complete("930518223"));
        assertFalse(NrnUtil.female("93.05.18-223.61"));
        assertEquals(LocalDate.of(1993, 5, 18), NrnUtil.birthDate("93.05.18-223.61"));
        assertTrue(SvnrUtil.isValid("1237010180"));
        assertEquals("1237010180", SvnrUtil.complete("123010180"));
        assertEquals(LocalDate.of(1980, 1, 1), SvnrUtil.birthDate("1237010180"));
        assertFalse(SvnrUtil.isValid("1238010180"));
        assertTrue(RegexUtil.is("cpr", "010170-0003"));
        assertTrue(RegexUtil.is("nrn", "93.05.18-223.61"));
        assertTrue(RegexUtil.is("svnr", "1237010180"));
    }

    @Test
    void ptNifAfmNinoRrnMyNumber() {
        assertTrue(PtNifUtil.isValid("123456789"));
        assertEquals("123456789", PtNifUtil.complete("12345678"));
        assertFalse(PtNifUtil.isValid("123456788"));
        assertTrue(AfmUtil.isValid("090000045"));
        assertEquals("090000045", AfmUtil.complete("09000004"));
        assertFalse(AfmUtil.isValid("090000046"));
        assertTrue(NinoUtil.isValid("AB123456C"));
        assertFalse(NinoUtil.isValid("BG123456C"));
        assertFalse(NinoUtil.isValid("QQ123456C"));
        assertEquals("AB", NinoUtil.prefix("ab 123456 c"));
        assertTrue(RrnUtil.isValid("900101-1234568"));
        assertEquals("9001011234568", RrnUtil.complete("900101123456"));
        assertFalse(RrnUtil.female("900101-1234568"));
        assertEquals(LocalDate.of(1990, 1, 1), RrnUtil.birthDate("900101-1234568"));
        assertTrue(MyNumberUtil.isValid("123456789019"));
        assertEquals("123456789019", MyNumberUtil.complete("12345678901"));
        assertFalse(MyNumberUtil.isValid("123456789018"));
        assertTrue(RegexUtil.is("ptnif", "123456789"));
        assertTrue(RegexUtil.is("afm", "090000045"));
        assertTrue(RegexUtil.is("nino", "AB123456C"));
        assertTrue(RegexUtil.is("rrn", "900101-1234568"));
        assertTrue(RegexUtil.is("mynumber", "123456789019"));
    }

    @Test
    void yencBase36RetryAfterUuidV1() {
        assertEquals("728f969699", YencUtil.encodeToHex("Hello"));
        assertEquals("Hello", YencUtil.decodeToString(YencUtil.encode("Hello")));
        assertEquals("=@", new String(YencUtil.encode(new byte[]{(byte) 214}), java.nio.charset.StandardCharsets.ISO_8859_1));
        assertEquals("kf12oi", Base36Util.encode(1234567890L));
        assertEquals(1234567890L, Base36Util.decodeLong("kf12oi"));
        assertEquals("5pzcszu7", Base36Util.encode("hello"));
        assertEquals("hello", Base36Util.decodeToString("5pzcszu7"));
        assertEquals(120L, RetryAfterUtil.seconds("120"));
        assertTrue(RetryAfterUtil.isDelaySeconds("120"));
        assertEquals(Instant.parse("2015-10-21T07:28:00Z"),
                RetryAfterUtil.date("Wed, 21 Oct 2015 07:28:00 GMT"));
        String uuid = IdUtil.uuidV1();
        assertEquals(1, Character.digit(uuid.charAt(14), 16));
        int variant = Character.digit(uuid.charAt(19), 16);
        assertTrue(variant >= 8 && variant <= 11);
    }
}
