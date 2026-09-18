package com.mengzhihua.utils.common;


import java.util.List;

import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ClearSiteDataUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AdoszamUtil;
import com.mengzhihua.utils.common.validate.CheUidUtil;
import com.mengzhihua.utils.common.validate.CuiUtil;
import com.mengzhihua.utils.common.validate.EikUtil;
import com.mengzhihua.utils.common.validate.KboUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EnterpriseAlignUtilsTest {

    @Test
    void cheUidEikCui() {
        assertTrue(CheUidUtil.isValid("CHE-109.322.551"));
        assertEquals("109322551", CheUidUtil.complete("10932255"));
        assertFalse(CheUidUtil.isValid("CHE-109.322.552"));
        assertTrue(EikUtil.isValid("831641791"));
        assertEquals("831641791", EikUtil.complete("83164179"));
        assertTrue(CuiUtil.isValid("18547290"));
        assertEquals("18547290", CuiUtil.complete("1854729"));
        assertTrue(RegexUtil.is("cheuid", "CHE-109.322.551"));
        assertTrue(RegexUtil.is("eik", "831641791"));
        assertTrue(RegexUtil.is("rocui", "18547290"));
    }

    @Test
    void adoszamKboClearCrc() {
        assertTrue(AdoszamUtil.isValid("18154111-2-41"));
        assertEquals("18154111", AdoszamUtil.complete("1815411"));
        assertTrue(KboUtil.isValid("0123.456.749"));
        assertEquals("0123456749", KboUtil.complete("01234567"));
        assertFalse(KboUtil.isValid("0123.456.748"));
        assertEquals(List.of("cache", "cookies"), ClearSiteDataUtil.parse("\"cache\", \"cookies\""));
        assertTrue(ClearSiteDataUtil.has("\"cache\", \"cookies\"", "cache"));
        assertTrue(RegexUtil.is("adoszam", "18154111-2-41"));
        assertTrue(RegexUtil.is("kbo", "0123.456.749"));
        assertEquals("b4c8", HashUtil.crc16UsbHex("123456789"));
    }
}
