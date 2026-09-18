package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.ContentLanguageUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EgTnUtil;
import com.mengzhihua.utils.common.validate.LuTvaUtil;
import com.mengzhihua.utils.common.validate.MkEdbUtil;
import com.mengzhihua.utils.common.validate.SvNitUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EgAlignUtilsTest {

    @Test
    void egyptLuxembourgElSalvador() {
        assertTrue(EgTnUtil.isValid("100-531-385"));
        assertEquals("331105268", EgTnUtil.normalize("٣٣١-١٠٥-٢٦٨"));
        assertTrue(EgTnUtil.isValid("٣٣١-١٠٥-٢٦٨"));
        assertEquals("100-531-385", EgTnUtil.format("100531385"));
        assertFalse(EgTnUtil.isValid("12345"));
        assertTrue(RegexUtil.is("egtn", "100531385"));

        assertTrue(LuTvaUtil.isValid("LU 150 274 42"));
        assertEquals("15027442", LuTvaUtil.complete("150274"));
        assertEquals("150 274 42", LuTvaUtil.format("15027442"));
        assertFalse(LuTvaUtil.isValid("150 274 43"));
        assertTrue(RegexUtil.is("lutva", "15027442"));

        assertTrue(SvNitUtil.isValid("0614-050707-104-8"));
        assertTrue(SvNitUtil.isValid("SV 0614-050707-104-8"));
        assertEquals("06140507071048", SvNitUtil.complete("0614050707104"));
        assertEquals("0614-050707-104-8", SvNitUtil.format("06140507071048"));
        assertFalse(SvNitUtil.isValid("0614-050707-104-0"));
        assertTrue(RegexUtil.is("svnit", "06140507071048"));
    }

    @Test
    void macedoniaContentLanguageCrc() {
        assertTrue(MkEdbUtil.isValid("4030000375897"));
        assertTrue(MkEdbUtil.isValid("MK4057009501106"));
        assertEquals("4020990116747", MkEdbUtil.normalize("МК 4020990116747"));
        assertTrue(MkEdbUtil.isValid("МК 4020990116747"));
        assertEquals("4030000375897", MkEdbUtil.complete("403000037589"));
        assertFalse(MkEdbUtil.isValid("4030000375890"));
        assertTrue(RegexUtil.is("mkedb", "4030000375897"));

        assertEquals("zh-CN", ContentLanguageUtil.first("zh-CN, en"));
        assertTrue(ContentLanguageUtil.has("zh-CN, en", "zh"));
        assertTrue(ContentLanguageUtil.has("zh-CN, en", "en"));
        assertFalse(ContentLanguageUtil.has("zh-CN, en", "de"));
        assertEquals("15", HashUtil.crc8DarcHex("123456789"));
    }
}
