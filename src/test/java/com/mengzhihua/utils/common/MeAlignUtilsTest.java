package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.VaryUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.CyVatUtil;
import com.mengzhihua.utils.common.validate.MePibUtil;
import com.mengzhihua.utils.common.validate.MtVatUtil;
import com.mengzhihua.utils.common.validate.OmVatUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MeAlignUtilsTest {

    @Test
    void montenegroOmanCyprus() {
        assertTrue(MePibUtil.isValid("02655284"));
        assertEquals("02655284", MePibUtil.complete("0265528"));
        assertFalse(MePibUtil.isValid("02655283"));
        assertTrue(RegexUtil.is("mepib", "02655284"));

        assertTrue(OmVatUtil.isValid("OM1100006083"));
        assertTrue(OmVatUtil.isValid("OM 1100 0060 83"));
        assertEquals("OM1100006083", OmVatUtil.complete("OM110000608"));
        assertEquals("OM 1100 0060 83", OmVatUtil.format("OM1100006083"));
        assertFalse(OmVatUtil.isValid("OM1100006084"));
        assertTrue(RegexUtil.is("omvat", "OM1100006083"));

        assertTrue(CyVatUtil.isValid("CY-10259033P"));
        assertEquals("10259033P", CyVatUtil.complete("10259033"));
        assertEquals("CY-10259033P", CyVatUtil.format("10259033P"));
        assertFalse(CyVatUtil.isValid("CY-10259033Z"));
        assertFalse(CyVatUtil.isValid("CY-12259033P"));
        assertTrue(RegexUtil.is("cyvat", "10259033P"));
    }

    @Test
    void maltaVaryCrc() {
        assertTrue(MtVatUtil.isValid("MT 1167-9112"));
        assertEquals("11679112", MtVatUtil.complete("1167911"));
        assertEquals("1167-9112", MtVatUtil.format("11679112"));
        assertFalse(MtVatUtil.isValid("1167-9113"));
        assertTrue(RegexUtil.is("mtvat", "11679112"));

        assertEquals("Accept-Encoding", VaryUtil.first("Accept-Encoding, User-Agent"));
        assertTrue(VaryUtil.has("Accept-Encoding, User-Agent", "user-agent"));
        assertFalse(VaryUtil.has("Accept-Encoding, User-Agent", "Origin"));
        assertTrue(VaryUtil.isWildcard("*"));
        assertTrue(VaryUtil.has("*", "Accept"));
        assertEquals("7e", HashUtil.crc8IcodeHex("123456789"));
    }
}
