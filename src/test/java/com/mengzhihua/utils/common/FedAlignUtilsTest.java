package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.OacUtil;
import com.mengzhihua.utils.common.net.TimingAllowUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EinUtil;
import com.mengzhihua.utils.common.validate.NiptUtil;
import com.mengzhihua.utils.common.validate.OgrnUtil;
import com.mengzhihua.utils.common.validate.SnilsUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FedAlignUtilsTest {

    @Test
    void einOgrnSnils() {
        assertTrue(EinUtil.isValid("91-1144442"));
        assertEquals("Philadelphia", EinUtil.campus("91-1144442"));
        assertEquals("Brookhaven", EinUtil.campus("04-2103594"));
        assertEquals("04-2103594", EinUtil.format("042103594"));
        assertFalse(EinUtil.isValid("07-1144442"));
        assertFalse(EinUtil.isValid("911-14-4442"));
        assertTrue(OgrnUtil.isValid("1022200525819"));
        assertTrue(OgrnUtil.isValid("385768585948949"));
        assertEquals("1022200525819", OgrnUtil.complete("102220052581"));
        assertFalse(OgrnUtil.isValid("1022500001328"));
        assertTrue(SnilsUtil.isValid("112-233-445 95"));
        assertEquals("11223344595", SnilsUtil.complete("112233445"));
        assertFalse(SnilsUtil.isValid("11223344500"));
        assertTrue(RegexUtil.is("ein", "91-1144442"));
        assertTrue(RegexUtil.is("ogrn", "1022200525819"));
        assertTrue(RegexUtil.is("snils", "11223344595"));
    }

    @Test
    void niptTaoCrc() {
        assertTrue(NiptUtil.isValid("AL J 91402501 L"));
        assertTrue(NiptUtil.isValid("K22218003V"));
        assertEquals("J91402501L", NiptUtil.normalize("AL J 91402501 L"));
        assertFalse(NiptUtil.isValid("Z22218003V"));
        assertTrue(TimingAllowUtil.wildcard("*"));
        assertTrue(TimingAllowUtil.origin("https://example.com"));
        assertTrue(OacUtil.enabled("?1"));
        assertTrue(OacUtil.known("?0"));
        assertFalse(OacUtil.known("1"));
        assertTrue(RegexUtil.is("nipt", "J91402501L"));
        assertEquals("aee7", HashUtil.crc16CmsHex("123456789"));
    }
}
