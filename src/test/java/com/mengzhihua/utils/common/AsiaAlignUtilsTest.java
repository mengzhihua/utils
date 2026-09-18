package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.NelUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.EdrpouUtil;
import com.mengzhihua.utils.common.validate.HojinUtil;
import com.mengzhihua.utils.common.validate.KrBrnUtil;
import com.mengzhihua.utils.common.validate.PibUtil;
import com.mengzhihua.utils.common.validate.TwGuiUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsiaAlignUtilsTest {

    @Test
    void hojinBrnGui() {
        assertTrue(HojinUtil.isValid("8700110005901"));
        assertEquals("8700110005901", HojinUtil.complete("700110005901"));
        assertFalse(HojinUtil.isValid("7700110005901"));
        assertTrue(KrBrnUtil.isValid("120-81-47521"));
        assertEquals("1208147521", KrBrnUtil.complete("120814752"));
        assertFalse(KrBrnUtil.isValid("120-81-47522"));
        assertTrue(TwGuiUtil.isValid("53212539"));
        assertEquals("53212539", TwGuiUtil.complete("53212539"));
        assertTrue(TwGuiUtil.isValid(TwGuiUtil.complete("5321253")));
        assertTrue(RegexUtil.is("hojin", "8700110005901"));
        assertTrue(RegexUtil.is("krbrn", "120-81-47521"));
        assertTrue(RegexUtil.is("twgui", "53212539"));
    }

    @Test
    void edrpouPibNelCrc() {
        assertTrue(EdrpouUtil.isValid("14360570"));
        assertEquals("14360570", EdrpouUtil.complete("1436057"));
        assertTrue(EdrpouUtil.isValid("32855961"));
        assertFalse(EdrpouUtil.isValid("14360571"));
        assertTrue(PibUtil.isValid("101134702"));
        assertEquals("101134702", PibUtil.complete("10113470"));
        assertFalse(PibUtil.isValid("101134703"));
        assertEquals("nel", NelUtil.reportTo("{\"report_to\":\"nel\",\"max_age\":31536000,\"include_subdomains\":true}"));
        assertEquals(31536000L, NelUtil.maxAge("{\"report_to\":\"nel\",\"max_age\":31536000,\"include_subdomains\":true}"));
        assertTrue(NelUtil.includeSubdomains("{\"report_to\":\"nel\",\"max_age\":31536000,\"include_subdomains\":true}"));
        assertTrue(RegexUtil.is("edrpou", "14360570"));
        assertTrue(RegexUtil.is("rspib", "101134702"));
        assertEquals("f4", HashUtil.crc8SmbusHex("123456789"));
    }
}
