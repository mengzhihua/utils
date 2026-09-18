package com.mengzhihua.utils.common;


import com.mengzhihua.utils.common.crypto.HashUtil;
import com.mengzhihua.utils.common.net.OriginUtil;
import com.mengzhihua.utils.common.text.RegexUtil;
import com.mengzhihua.utils.common.validate.AdNrtUtil;
import com.mengzhihua.utils.common.validate.DzNifUtil;
import com.mengzhihua.utils.common.validate.LiPeidUtil;
import com.mengzhihua.utils.common.validate.SnNineaUtil;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdAlignUtilsTest {

    @Test
    void andorraLiechtensteinAlgeria() {
        assertTrue(AdNrtUtil.isValid("U-132950-X"));
        assertEquals("U-132950-X", AdNrtUtil.format("U132950X"));
        assertEquals("D-059888-N", AdNrtUtil.format("D059888N"));
        assertFalse(AdNrtUtil.isValid("I 706193 G"));
        assertFalse(AdNrtUtil.isValid("A123B"));
        assertTrue(RegexUtil.is("adnrt", "U132950X"));

        assertTrue(LiPeidUtil.isValid("1234567"));
        assertEquals("1234567", LiPeidUtil.normalize("00001234567"));
        assertTrue(LiPeidUtil.isValid("00001234567"));
        assertFalse(LiPeidUtil.isValid("123"));
        assertTrue(RegexUtil.is("lipeid", "1234567"));

        assertTrue(DzNifUtil.isValid("416001000000007"));
        assertTrue(DzNifUtil.isValid("408 020 000 150 039"));
        assertTrue(DzNifUtil.isValid("41201600000606600001"));
        assertEquals("00021600180833713010", DzNifUtil.normalize("000 216 001 808 337 13010"));
        assertFalse(DzNifUtil.isValid("12345"));
        assertTrue(RegexUtil.is("dznif", "416001000000007"));
    }

    @Test
    void senegalOriginCrc() {
        assertTrue(SnNineaUtil.isValid("306 7221"));
        assertTrue(SnNineaUtil.isValid("30672212G2"));
        assertEquals("3067221", SnNineaUtil.complete("306722"));
        assertEquals("3067221 2G2", SnNineaUtil.format("30672212G2"));
        assertFalse(SnNineaUtil.isValid("3067222"));
        assertFalse(SnNineaUtil.isValid("1234567 0AZ"));
        assertTrue(RegexUtil.is("snninea", "30672212G2"));

        assertEquals("example.com", OriginUtil.host("https://example.com:8443"));
        assertEquals("https", OriginUtil.scheme("https://example.com:8443"));
        assertEquals(8443, OriginUtil.parse("https://example.com:8443").port());
        assertTrue(OriginUtil.isSecure("https://example.com:8443"));
        assertTrue(OriginUtil.isNull("null"));
        assertFalse(OriginUtil.isSecure("http://example.com"));
        assertEquals("d0", HashUtil.crc8RohcHex("123456789"));
    }
}
