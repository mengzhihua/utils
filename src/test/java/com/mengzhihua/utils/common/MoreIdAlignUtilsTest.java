package com.mengzhihua.utils.common;


import java.time.LocalDate;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.AcceptEncodingUtil;
import com.mengzhihua.utils.common.net.CspUtil;
import com.mengzhihua.utils.common.net.HstsUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CnpUtil;
import com.mengzhihua.utils.common.validate.EgnUtil;
import com.mengzhihua.utils.common.validate.IcoUtil;
import com.mengzhihua.utils.common.validate.IsraeliIdUtil;
import com.mengzhihua.utils.common.validate.OibUtil;
import com.mengzhihua.utils.common.validate.RegonUtil;
import com.mengzhihua.utils.common.validate.TcKimlikUtil;
import com.mengzhihua.utils.common.validate.ThaiIdUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MoreIdAlignUtilsTest {

    @Test
    void tcknIsraeliCnpOib() {
        assertTrue(TcKimlikUtil.isValid("10000000146"));
        assertEquals("10000000146", TcKimlikUtil.complete("100000001"));
        assertFalse(TcKimlikUtil.isValid("10000000147"));
        assertTrue(IsraeliIdUtil.isValid("123456782"));
        assertEquals("123456782", IsraeliIdUtil.complete("12345678"));
        assertTrue(CnpUtil.isValid("1800101010015"));
        assertEquals("1800101010015", CnpUtil.complete("180010101001"));
        assertFalse(CnpUtil.female("1800101010015"));
        assertEquals(LocalDate.of(1980, 1, 1), CnpUtil.birthDate("1800101010015"));
        assertTrue(OibUtil.isValid("12345678903"));
        assertEquals("12345678903", OibUtil.complete("1234567890"));
        assertTrue(RegexUtil.is("tckn", "10000000146"));
        assertTrue(RegexUtil.is("israeliid", "123456782"));
        assertTrue(RegexUtil.is("cnp", "1800101010015"));
        assertTrue(RegexUtil.is("oib", "12345678903"));
    }

    @Test
    void egnThaiRegonIco() {
        assertTrue(EgnUtil.isValid("8001010008"));
        assertEquals("8001010008", EgnUtil.complete("800101000"));
        assertTrue(EgnUtil.female("8001010008"));
        assertEquals(LocalDate.of(1980, 1, 1), EgnUtil.birthDate("8001010008"));
        assertTrue(ThaiIdUtil.isValid("1234567890121"));
        assertEquals("1234567890121", ThaiIdUtil.complete("123456789012"));
        assertTrue(RegonUtil.isValid("123456785"));
        assertEquals("123456785", RegonUtil.complete("12345678"));
        assertTrue(RegonUtil.isValid("12345678512347"));
        assertTrue(IcoUtil.isValid("25596641"));
        assertEquals("25596641", IcoUtil.complete("2559664"));
        assertTrue(RegexUtil.is("egn", "8001010008"));
        assertTrue(RegexUtil.is("thaiid", "1234567890121"));
        assertTrue(RegexUtil.is("regon", "123456785"));
        assertTrue(RegexUtil.is("ico", "25596641"));
    }

    @Test
    void hstsCspAcceptEncodingCrc() {
        String hsts = "max-age=31536000; includeSubDomains; preload";
        assertEquals(31536000L, HstsUtil.maxAge(hsts));
        assertTrue(HstsUtil.includeSubDomains(hsts));
        assertTrue(HstsUtil.preload(hsts));
        String csp = "default-src 'self'; script-src 'self' https://cdn.example";
        assertEquals(java.util.List.of("'self'"), CspUtil.directive(csp, "default-src"));
        assertTrue(CspUtil.allows(csp, "script-src", "https://cdn.example"));
        assertEquals("gzip", AcceptEncodingUtil.negotiate("gzip;q=1.0, br;q=0.8", "br", "gzip"));
        assertEquals("2189", HashUtil.crc16KermitHex("123456789"));
        assertEquals("fc891918", HashUtil.crc32Bzip2Hex("123456789"));
    }
}
